package br.com.telebraz.tarifador.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessamentoRequest {
    private List<LigacaoDTO> ligacoes;
    private String observacoes;
}