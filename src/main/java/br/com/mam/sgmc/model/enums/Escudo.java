package br.com.mam.sgmc.model.enums;

public enum Escudo {
    
    TEM_ESCUDO(0),
    NAO_TEM_ESCUDO(1);
    
    private final Integer codigo;
    
    Escudo(Integer codigo) {
        this.codigo = codigo;
    }
    
    public Integer getCodigo() {
        return codigo;
    }
}
