package br.com.telebraz.tarifador.strategy;

import br.com.telebraz.tarifador.entity.Ligacao;
import br.com.telebraz.tarifador.enums.TipoPlano;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class PosPagoBasicoStrategy implements CalculoTarifaStrategy {
    
    @Override
    public BigDecimal calcularTarifa(List<Ligacao> ligacoes) {
        int totalMinutos = ligacoes.stream()
                .mapToInt(Ligacao::getDuracaoMinutos)
                .sum();
        
        int minutosGratuitos = TipoPlano.POS_PAGO_BASICO.getMinutosGratuitos();
        
        if (totalMinutos <= minutosGratuitos) {
            return BigDecimal.ZERO;
        }
        
        int minutosExcedentes = totalMinutos - minutosGratuitos;
        BigDecimal valorExcedente = BigDecimal.valueOf(TipoPlano.POS_PAGO_BASICO.getTarifaPorMinuto())
                .multiply(BigDecimal.valueOf(minutosExcedentes));
        
        // Aplicar multiplicadores e descontos proporcionalmente
        BigDecimal valorAjustado = aplicarAjustes(ligacoes, valorExcedente, minutosExcedentes);
        
        return valorAjustado.setScale(2, RoundingMode.HALF_UP);
    }
    
    private BigDecimal aplicarAjustes(List<Ligacao> ligacoes, BigDecimal valorBase, int minutosExcedentes) {
        // Lógica simplificada - em produção seria mais complexa
        double multiplicadorMedio = ligacoes.stream()
                .mapToDouble(l -> l.getTipoLigacao().getMultiplicador())
                .average()
                .orElse(1.0);
        
        return valorBase.multiply(BigDecimal.valueOf(multiplicadorMedio));
    }
    
    @Override
    public String getTipoPlano() {
        return TipoPlano.POS_PAGO_BASICO.name();
    }
}