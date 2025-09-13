package br.com.telebraz.tarifador.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaturaResponse {
    private Long faturaId;
    private String cpfCliente;
    private String nomeCliente;
    private String tipoPlano;
    private Integer totalMinutos;
    private Integer minutosGratuitosUtilizados;
    private BigDecimal valorTotal;
    private LocalDateTime dataGeracao;
    private Integer quantidadeLigacoes;
}