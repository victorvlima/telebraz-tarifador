package br.com.telebraz.tarifador.service;

import br.com.telebraz.tarifador.dto.FaturaResponse;
import br.com.telebraz.tarifador.dto.LigacaoDTO;
import br.com.telebraz.tarifador.dto.ProcessamentoRequest;
import br.com.telebraz.tarifador.entity.Cliente;
import br.com.telebraz.tarifador.entity.Fatura;
import br.com.telebraz.tarifador.entity.Ligacao;
import br.com.telebraz.tarifador.entity.Telefone;
import br.com.telebraz.tarifador.repository.ClienteRepository;
import br.com.telebraz.tarifador.repository.LigacaoRepository;
import br.com.telebraz.tarifador.repository.TelefoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TarifadorService {

    private final ClienteRepository clienteRepository;
    private final LigacaoRepository ligacaoRepository;
    private final TelefoneRepository telefoneRepository;
    private final ProcessadorFatura processadorFatura;

    @Transactional
    public List<FaturaResponse> processarLigacoes(ProcessamentoRequest request) {
        log.info("Iniciando processamento de {} ligações", request.getLigacoes().size());

        // 1. Converter DTOs para entidades e associar clientes
        List<Ligacao> ligacoes = converterEAssociarLigacoes(request.getLigacoes());

        // 2. Salvar ligações
        ligacoes = ligacaoRepository.saveAll(ligacoes);

        // 3. Agrupar ligações por cliente
        Map<Cliente, List<Ligacao>> ligacoesPorCliente = ligacoes.stream()
                .filter(l -> l.getCliente() != null)
                .collect(Collectors.groupingBy(Ligacao::getCliente));

        // 4. Gerar faturas
        List<FaturaResponse> faturas = new ArrayList<>();

        for (Map.Entry<Cliente, List<Ligacao>> entry : ligacoesPorCliente.entrySet()) {
            Cliente cliente = entry.getKey();
            List<Ligacao> ligacaoesCliente = entry.getValue();

            try {
                Fatura fatura = processadorFatura.gerarFatura(cliente, ligacaoesCliente);
                faturas.add(converterParaResponse(fatura));

                // Marcar ligações como processadas
                ligacaoesCliente.forEach(l -> l.setProcessada(true));
                ligacaoRepository.saveAll(ligacaoesCliente);

            } catch (Exception e) {
                log.error("Erro ao processar fatura para cliente {}: {}", cliente.getCpf(), e.getMessage());
            }
        }

        log.info("Processamento concluído. {} faturas geradas", faturas.size());
        return faturas;
    }

    private List<Ligacao> converterEAssociarLigacoes(List<LigacaoDTO> ligacoesDTO) {
        return ligacoesDTO.stream()
                .map(this::converterLigacao)
                .collect(Collectors.toList());
    }

    private Ligacao converterLigacao(LigacaoDTO dto) {
        Ligacao ligacao = Ligacao.builder()
                .numeroOrigem(dto.getNumeroOrigem())
                .numeroDestino(dto.getNumeroDestino())
                .inicioLigacao(dto.getInicioLigacao())
                .duracaoMinutos(dto.getDuracaoMinutos())
                .tipoLigacao(dto.getTipoLigacao())
                .processada(false)
                .build();

        // Buscar cliente pelo número de origem
        Optional<Telefone> telefone = telefoneRepository.findByNumero(dto.getNumeroOrigem());

        if (telefone.isPresent()) {
            ligacao.setCliente(telefone.get().getCliente());
            log.debug("Cliente encontrado para número {}: {}",
                    dto.getNumeroOrigem(), telefone.get().getCliente().getCpf());
        } else {
            log.warn("Cliente não encontrado para número: {}", dto.getNumeroOrigem());
        }

        return ligacao;
    }

    private FaturaResponse converterParaResponse(Fatura fatura) {
        return FaturaResponse.builder()
                .faturaId(fatura.getId())
                .cpfCliente(fatura.getCliente().getCpf())
                .nomeCliente(fatura.getCliente().getNome())
                .tipoPlano(fatura.getCliente().getTipoPlano().getDescricao())
                .totalMinutos(fatura.getTotalMinutos())
                .minutosGratuitosUtilizados(fatura.getMinutosGratuitosUtilizados())
                .valorTotal(fatura.getValorTotal())
                .dataGeracao(fatura.getDataGeracao())
                .quantidadeLigacoes(fatura.getQuantidadeLigacoes())
                .build();
    }
}