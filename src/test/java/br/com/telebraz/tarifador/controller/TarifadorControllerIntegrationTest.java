package br.com.telebraz.tarifador.controller;

import br.com.telebraz.tarifador.dto.LigacaoDTO;
import br.com.telebraz.tarifador.dto.ProcessamentoRequest;
import br.com.telebraz.tarifador.enums.TipoLigacao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureTestWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestWebMvc
@ActiveProfiles("test")
class TarifadorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveProcessarLigacoesComSucesso() throws Exception {
        // Given
        ProcessamentoRequest request = ProcessamentoRequest.builder()
                .ligacoes(Arrays.asList(
                    LigacaoDTO.builder()
                        .numeroOrigem("987654321")
                        .numeroDestino("123456789")
                        .inicioLigacao(LocalDateTime.of(2024, 1, 15, 14, 30))
                        .duracaoMinutos(10)
                        .tipoLigacao(TipoLigacao.LOCAL)
                        .build()
                ))
                .observacoes("Teste de integração")
                .build();

        // When & Then
        mockMvc.perform(post("/api/v1/tarifador/processar-ligacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].cpfCliente").value("12345678901"))
                .andExpect(jsonPath("$[0].nomeCliente").value("João Silva"))
                .andExpect(jsonPath("$[0].tipoPlano").value("Pré-pago"))
                .andExpect(jsonPath("$[0].valorTotal").value(5.00));
    }

    @Test
    void deveRetornarHealthCheck() throws Exception {
        mockMvc.perform(get("/api/v1/tarifador/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Tarifador API está funcionando!"));
    }

    @Test
    void deveRetornarErroParaRequestInvalido() throws Exception {
        mockMvc.perform(post("/api/v1/tarifador/processar-ligacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().is5xxServerError());
    }
}