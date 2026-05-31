package br.com.mam.sgmc.model.enums;

public enum Batizado {
    
    BATIZADO(0),
    NAO_BATIZADO(1);

    private final Integer codigo;

    Batizado(Integer codigo) {
        this.codigo = codigo;
    }
    
    public Integer getCodigo() {
        return codigo;
    }
}
