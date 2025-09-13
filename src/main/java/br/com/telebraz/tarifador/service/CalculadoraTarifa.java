package br.com.telebraz.tarifador.service;

import br.com.telebraz.tarifador.entity.Ligacao;
import br.com.telebraz.tarifador.enums.TipoPlano;
import br.com.telebraz.tarifador.strategy.CalculoTarifaStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalculadoraTarifa {
    
    private final List<CalculoTarifaStrategy> strategies;
    
    private Map<String, CalculoTarifaStrategy> getStrategyMap() {
        return strategies.stream()
                .collect(Collectors.toMap(
                        CalculoTarifaStrategy::getTipoPlano,
                        Function.identity()
                ));
    }
    
    public BigDecimal calcularTarifa(List<Ligacao> ligacoes, TipoPlano tipoPlano) {
        CalculoTarifaStrategy strategy = getStrategyMap().get(tipoPlano.name());
        
        if (strategy == null) {
            throw new IllegalArgumentException("Estratégia não encontrada para o plano: " + tipoPlano);
        }
        
        return strategy.calcularTarifa(ligacoes);
    }
}