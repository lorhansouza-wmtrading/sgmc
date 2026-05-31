package br.com.mam.sgmc.model.enums;

public enum Ativo {
    ATIVO(0),
    INATIVO(1);

    private Integer codigo;

    Ativo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public static Integer valueOf(Ativo ehAtivo) {
        if (ehAtivo == null) {
            return null;
        }
        for (Ativo a : Ativo.values()) {
            if (a.equals(ehAtivo)) {
                return a.getCodigo();
            }
        }
        throw new IllegalArgumentException("Valor inválido para Ativo: " + ehAtivo);
    }

    public static Ativo fromCodigo(Integer codigo) {
        if (codigo == null) {
            return null;
        }
        for (Ativo a : Ativo.values()) {
            if (a.codigo.equals(codigo)) {
                return a;
            }
        }
        throw new IllegalArgumentException("Código inválido para Ativo: " + codigo);
    }
}
