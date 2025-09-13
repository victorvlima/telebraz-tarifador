package br.com.telebraz.tarifador.enums;

import lombok.Getter;

@Getter
public enum TipoLigacao {
    LOCAL("Local", 1.0),
    LONGA_DISTANCIA("Longa Distância", 1.5),
    INTERNACIONAL("Internacional", 3.0);

    private final String descricao;
    private final double multiplicador;

    TipoLigacao(String descricao, double multiplicador) {
        this.descricao = descricao;
        this.multiplicador = multiplicador;
    }
}