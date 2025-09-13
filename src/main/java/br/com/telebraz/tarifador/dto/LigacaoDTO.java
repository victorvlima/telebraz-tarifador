package br.com.telebraz.tarifador.dto;

import br.com.telebraz.tarifador.enums.TipoLigacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigacaoDTO {
    private String numeroOrigem;
    private String numeroDestino;
    private LocalDateTime inicioLigacao;
    private Integer duracaoMinutos;
    private TipoLigacao tipoLigacao;
}