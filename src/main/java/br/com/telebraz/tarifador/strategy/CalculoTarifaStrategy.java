package br.com.telebraz.tarifador.strategy;

import br.com.telebraz.tarifador.entity.Ligacao;

import java.math.BigDecimal;
import java.util.List;

public interface CalculoTarifaStrategy {
    BigDecimal calcularTarifa(List<Ligacao> ligacoes);
    String getTipoPlano();
}