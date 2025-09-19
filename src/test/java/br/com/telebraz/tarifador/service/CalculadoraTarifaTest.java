package br.com.telebraz.tarifador.service;

import br.com.telebraz.tarifador.entity.Ligacao;
import br.com.telebraz.tarifador.enums.TipoLigacao;
import br.com.telebraz.tarifador.enums.TipoPlano;
import br.com.telebraz.tarifador.strategy.CalculoTarifaStrategy;
import br.com.telebraz.tarifador.strategy.PrePagoStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalculadoraTarifaTest {

    @Mock
    private CalculoTarifaStrategy mockStrategy;

    private CalculadoraTarifa calculadoraTarifa;
    private List<Ligacao> ligacoes;

    @BeforeEach
    void setUp() {
        calculadoraTarifa = new CalculadoraTarifa(Arrays.asList(mockStrategy));
        
        ligacoes = Arrays.asList(
            Ligacao.builder()
                .numeroOrigem("11987654321")
                .numeroDestino("11123456789")
                .inicioLigacao(LocalDateTime.of(2024, 1, 15, 14, 0))
                .duracaoMinutos(10)
                .tipoLigacao(TipoLigacao.LOCAL)
                .build()
        );
    }

    @Test
    void deveCalcularTarifaComSucesso() {
        // Given
        when(mockStrategy.getTipoPlano()).thenReturn(TipoPlano.PRE_PAGO.name());
        when(mockStrategy.calcularTarifa(ligacoes)).thenReturn(new BigDecimal("5.00"));

        // When
        BigDecimal resultado = calculadoraTarifa.calcularTarifa(ligacoes, TipoPlano.PRE_PAGO);

        // Then
        assertEquals(new BigDecimal("5.00"), resultado);
        verify(mockStrategy).calcularTarifa(ligacoes);
    }

    @Test
    void deveLancarExcecaoParaPlanoInexistente() {
        // Given
        when(mockStrategy.getTipoPlano()).thenReturn("OUTRO_PLANO");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            calculadoraTarifa.calcularTarifa(ligacoes, TipoPlano.PRE_PAGO);
        });
    }

    @Test
    void deveLancarExcecaoParaLigacoesNulas() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            calculadoraTarifa.calcularTarifa(null, TipoPlano.PRE_PAGO);
        });
    }
}