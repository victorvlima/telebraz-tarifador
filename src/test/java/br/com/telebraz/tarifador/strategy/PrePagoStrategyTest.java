package br.com.telebraz.tarifador.strategy;

import br.com.telebraz.tarifador.entity.Ligacao;
import br.com.telebraz.tarifador.enums.TipoLigacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrePagoStrategyTest {

    private PrePagoStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new PrePagoStrategy();
    }

    @Test
    void deveCalcularTarifaLocalHorarioComercial() {
        // Given
        List<Ligacao> ligacoes = Arrays.asList(
            criarLigacao("11987654321", "11123456789", 
                        LocalDateTime.of(2024, 1, 15, 14, 0), 
                        10, TipoLigacao.LOCAL)
        );

        // When
        BigDecimal resultado = strategy.calcularTarifa(ligacoes);

        // Then
        assertEquals(new BigDecimal("5.00"), resultado);
    }

    @Test
    void deveCalcularTarifaLocalHorarioNoturno() {
        // Given
        List<Ligacao> ligacoes = Arrays.asList(
            criarLigacao("11987654321", "11123456789", 
                        LocalDateTime.of(2024, 1, 15, 20, 0), 
                        10, TipoLigacao.LOCAL)
        );

        // When
        BigDecimal resultado = strategy.calcularTarifa(ligacoes);

        // Then
        assertEquals(new BigDecimal("4.00"), resultado); // 20% desconto
    }

    @ParameterizedTest
    @CsvSource({
        "LOCAL, 1.0, 10, 14, 5.00",
        "LOCAL, 1.0, 10, 20, 4.00",
        "LONGA_DISTANCIA, 1.5, 10, 14, 7.50",
        "INTERNACIONAL, 3.0, 5, 14, 7.50"
    })
    void deveCalcularTarifaCorretamente(TipoLigacao tipo, double multiplicador, 
                                      int minutos, int hora, BigDecimal esperado) {
        // Given
        List<Ligacao> ligacoes = Arrays.asList(
            criarLigacao("11987654321", "11123456789", 
                        LocalDateTime.of(2024, 1, 15, hora, 0), 
                        minutos, tipo)
        );

        // When
        BigDecimal resultado = strategy.calcularTarifa(ligacoes);

        // Then
        assertEquals(esperado, resultado);
    }

    @Test
    void deveCalcularMultiplasLigacoes() {
        // Given
        List<Ligacao> ligacoes = Arrays.asList(
            criarLigacao("11987654321", "11123456789", 
                        LocalDateTime.of(2024, 1, 15, 14, 0), 
                        10, TipoLigacao.LOCAL),
            criarLigacao("11987654321", "21123456789", 
                        LocalDateTime.of(2024, 1, 15, 15, 0), 
                        5, TipoLigacao.LONGA_DISTANCIA)
        );

        // When
        BigDecimal resultado = strategy.calcularTarifa(ligacoes);

        // Then
        assertEquals(new BigDecimal("8.75"), resultado); // 5.00 + 3.75
    }

    private Ligacao criarLigacao(String origem, String destino, LocalDateTime inicio, 
                                int duracao, TipoLigacao tipo) {
        return Ligacao.builder()
                .numeroOrigem(origem)
                .numeroDestino(destino)
                .inicioLigacao(inicio)
                .duracaoMinutos(duracao)
                .tipoLigacao(tipo)
                .build();
    }
}