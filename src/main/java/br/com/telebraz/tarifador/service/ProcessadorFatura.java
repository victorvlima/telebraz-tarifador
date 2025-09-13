package br.com.telebraz.tarifador.service;

import br.com.telebraz.tarifador.entity.Cliente;
import br.com.telebraz.tarifador.entity.Fatura;
import br.com.telebraz.tarifador.entity.Ligacao;
import br.com.telebraz.tarifador.repository.FaturaRepository;
import br.com.telebraz.tarifador.repository.LigacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessadorFatura {
    
    private final FaturaRepository faturaRepository;
    private final LigacaoRepository ligacaoRepository;
    private final CalculadoraTarifa calculadoraTarifa;
    
    @Transactional
    public Fatura gerarFatura(Cliente cliente, List<Ligacao> ligacoes) {
        log.info("Gerando fatura para cliente: {} com {} ligações", cliente.getCpf(), ligacoes.size());
        
        BigDecimal valorTotal = calculadoraTarifa.calcularTarifa(ligacoes, cliente.getTipoPlano());
        
        int totalMinutos = ligacoes.stream()
                .mapToInt(Ligacao::getDuracaoMinutos)
                .sum();
        
        int minutosGratuitosUtilizados = Math.min(totalMinutos, cliente.getTipoPlano().getMinutosGratuitos());
        
        LocalDateTime agora = LocalDateTime.now();
        
        // Criar e salvar a fatura
        Fatura fatura = Fatura.builder()
                .cliente(cliente)
                .dataGeracao(agora)
                .periodoInicio(agora.minusMonths(1))
                .periodoFim(agora)
                .totalMinutos(totalMinutos)
                .minutosGratuitosUtilizados(minutosGratuitosUtilizados)
                .valorTotal(valorTotal)
                .quantidadeLigacoes(ligacoes.size())
                .build();
        
        fatura = faturaRepository.save(fatura);
        
        // Associar ligações à fatura e atualizar valores
        final Fatura faturaFinal = fatura;
        ligacoes.forEach(ligacao -> {
            ligacao.setFatura(faturaFinal);
            ligacao.setProcessada(true);
        });
        
        ligacaoRepository.saveAll(ligacoes);
        
        return fatura;
    }
    
    public List<Ligacao> buscarLigacoesDaFatura(Long faturaId) {
        return ligacaoRepository.findByFaturaId(faturaId);
    }
}