package br.com.mam.sgmc.config;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import br.com.mam.sgmc.model.moto.Moto;
import br.com.mam.sgmc.repository.MotoRepository;
import br.com.mam.sgmc.api.dto.request.InscricaoRequestDTO;
import lombok.RequiredArgsConstructor;

@Component("sgmcSecurity")
@RequiredArgsConstructor
public class SgmcSecurity {

    private static final String ROLE_ADMIN = "ROLE_admin";
    private static final String ROLE_DIRETORIA = "ROLE_diretoria";
    private static final String ROLE_MEMBRO = "ROLE_membro";

    private final MotoRepository motoRepository;

    private Long getMembroIdFromPrincipal(Authentication authentication) {
        if (authentication == null) return null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt jwt) {
            Object claim = jwt.getClaim("membro_id");
            if (claim instanceof Number number) {
                return number.longValue();
            } else if (claim instanceof String str) {
                try {
                    return Long.parseLong(str);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }

    private boolean isAdminOrDiretoria(Authentication auth) {
        return auth.getAuthorities().stream().anyMatch(a -> 
            ROLE_ADMIN.equals(a.getAuthority()) || ROLE_DIRETORIA.equals(a.getAuthority()));
    }

    private boolean isMembro(Authentication auth) {
        return auth.getAuthorities().stream().anyMatch(a -> ROLE_MEMBRO.equals(a.getAuthority()));
    }

    public boolean isSelf(Long idMembro) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        
        if (isAdminOrDiretoria(auth)) {
            return true;
        }

        Long tokenMembroId = getMembroIdFromPrincipal(auth);
        return tokenMembroId != null && tokenMembroId.equals(idMembro);
    }

    public boolean isMotoOwner(String placa) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;

        if (isAdminOrDiretoria(auth)) {
            return true;
        }

        Long tokenMembroId = getMembroIdFromPrincipal(auth);
        if (tokenMembroId == null) return false;

        Moto moto = motoRepository.findById(placa).orElse(null);
        return moto != null && moto.getMembro() != null && tokenMembroId.equals(moto.getMembro().getId());
    }

    public boolean canInscribe(List<InscricaoRequestDTO> dtos) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }

        if (isAdminOrDiretoria(auth)) {
            return true;
        }

        if (isMembro(auth)) {
            return areAllInscricoesForSelf(dtos, auth);
        }

        return false;
    }

    private boolean areAllInscricoesForSelf(List<InscricaoRequestDTO> dtos, Authentication auth) {
        if (dtos == null || dtos.isEmpty()) {
            return false;
        }
        Long tokenMembroId = getMembroIdFromPrincipal(auth);
        if (tokenMembroId == null) {
            return false;
        }
        
        for (InscricaoRequestDTO dto : dtos) {
            if (!tokenMembroId.equals(dto.getIdMembro())) {
                return false;
            }
        }
        return true;
    }
}
