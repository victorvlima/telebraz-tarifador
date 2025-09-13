package br.com.telebraz.tarifador.strategy;

import br.com.telebraz.tarifador.entity.Ligacao;
import br.com.telebraz.tarifador.enums.TipoPlano;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class PrePagoStrategy implements CalculoTarifaStrategy {
    
    @Override
    public BigDecimal calcularTarifa(List<Ligacao> ligacoes) {
        return ligacoes.stream()
                .map(this::calcularTarifaLigacao)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private BigDecimal calcularTarifaLigacao(Ligacao ligacao) {
        BigDecimal tarifaBase = BigDecimal.valueOf(TipoPlano.PRE_PAGO.getTarifaPorMinuto())
                .multiply(BigDecimal.valueOf(ligacao.getDuracaoMinutos()));
        
        // Aplicar multiplicador do tipo de ligação
        tarifaBase = tarifaBase.multiply(BigDecimal.valueOf(ligacao.getTipoLigacao().getMultiplicador()));
        
        // Aplicar desconto noturno
        if (!ligacao.isHorarioComercial()) {
            tarifaBase = tarifaBase.multiply(BigDecimal.valueOf(0.8));
        }
        
        return tarifaBase.setScale(2, RoundingMode.HALF_UP);
    }
    
    @Override
    public String getTipoPlano() {
        return TipoPlano.PRE_PAGO.name();
    }
}