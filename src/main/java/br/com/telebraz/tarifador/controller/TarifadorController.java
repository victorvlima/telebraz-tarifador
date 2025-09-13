package br.com.telebraz.tarifador.controller;

import br.com.telebraz.tarifador.dto.FaturaResponse;
import br.com.telebraz.tarifador.dto.ProcessamentoRequest;
import br.com.telebraz.tarifador.service.TarifadorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tarifador")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class TarifadorController {
    
    private final TarifadorService tarifadorService;
    
    @PostMapping("/processar-ligacoes")
    public ResponseEntity<List<FaturaResponse>> processarLigacoes(
            @RequestBody ProcessamentoRequest request) {
        
        try {
            log.info("Recebida solicitação de processamento de {} ligações", 
                    request.getLigacoes().size());
            
            List<FaturaResponse> faturas = tarifadorService.processarLigacoes(request);
            
            return ResponseEntity.ok(faturas);
            
        } catch (Exception e) {
            log.error("Erro ao processar ligações: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Tarifador API está funcionando!");
    }
}