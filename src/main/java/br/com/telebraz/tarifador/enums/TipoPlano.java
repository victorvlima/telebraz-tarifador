package br.com.telebraz.tarifador.enums;

import lombok.Getter;

@Getter
public enum TipoPlano {
    PRE_PAGO("Pré-pago", 0.50, 0),
    POS_PAGO_BASICO("Pós-pago Básico", 0.30, 100),
    POS_PAGO_PREMIUM("Pós-pago Premium", 0.20, 500);

    private final String descricao;
    private final double tarifaPorMinuto;
    private final int minutosGratuitos;

    TipoPlano(String descricao, double tarifaPorMinuto, int minutosGratuitos) {
        this.descricao = descricao;
        this.tarifaPorMinuto = tarifaPorMinuto;
        this.minutosGratuitos = minutosGratuitos;
    }
}
