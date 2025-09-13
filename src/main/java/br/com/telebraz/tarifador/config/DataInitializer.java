package br.com.telebraz.tarifador.config;

import br.com.telebraz.tarifador.entity.Cliente;
import br.com.telebraz.tarifador.entity.Telefone;
import br.com.telebraz.tarifador.enums.StatusCliente;
import br.com.telebraz.tarifador.enums.TipoPlano;
import br.com.telebraz.tarifador.repository.ClienteRepository;
import br.com.telebraz.tarifador.repository.TelefoneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {
    
    private final ClienteRepository clienteRepository;
    private final TelefoneRepository telefoneRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (clienteRepository.count() == 0) {
            log.info("Inicializando dados de teste...");
            criarDadosIniciais();
            log.info("Dados de teste criados com sucesso!");
        } else {
            log.info("Dados já existem no banco. Total de clientes: {}", clienteRepository.count());
        }
    }
    
    private void criarDadosIniciais() {
        // Criar clientes
        Cliente cliente1 = Cliente.builder()
                .cpf("12345678901")
                .nome("João Silva")
                .email("joao@email.com")
                .tipoPlano(TipoPlano.PRE_PAGO)
                .status(StatusCliente.ATIVO)
                .dataCadastro(LocalDateTime.now())
                .build();
        
        Cliente cliente2 = Cliente.builder()
                .cpf("98765432100")
                .nome("Maria Santos")
                .email("maria@email.com")
                .tipoPlano(TipoPlano.POS_PAGO_BASICO)
                .status(StatusCliente.ATIVO)
                .dataCadastro(LocalDateTime.now())
                .build();
        
        Cliente cliente3 = Cliente.builder()
                .cpf("11122233344")
                .nome("Pedro Oliveira")
                .email("pedro@email.com")
                .tipoPlano(TipoPlano.POS_PAGO_PREMIUM)
                .status(StatusCliente.ATIVO)
                .dataCadastro(LocalDateTime.now())
                .build();
        
        // Salvar clientes
        cliente1 = clienteRepository.save(cliente1);
        cliente2 = clienteRepository.save(cliente2);
        cliente3 = clienteRepository.save(cliente3);
        
        log.info("Clientes criados: {}, {}, {}", cliente1.getNome(), cliente2.getNome(), cliente3.getNome());
        
        // Criar telefones
        Telefone telefone1 = Telefone.builder()
                .numero("987654321")
                .ddd("11")
                .principal(true)  // Agora funciona corretamente
                .cliente(cliente1)
                .build();
        
        Telefone telefone2 = Telefone.builder()
                .numero("123456789")
                .ddd("11")
                .principal(true)
                .cliente(cliente2)
                .build();
        
        Telefone telefone3 = Telefone.builder()
                .numero("555666777")
                .ddd("11")
                .principal(true)
                .cliente(cliente3)
                .build();
        
        Telefone telefone4 = Telefone.builder()
                .numero("999888777")
                .ddd("11")
                .principal(false)  // Telefone secundário
                .cliente(cliente1)
                .build();
        
        // Salvar telefones
        telefoneRepository.save(telefone1);
        telefoneRepository.save(telefone2);
        telefoneRepository.save(telefone3);
        telefoneRepository.save(telefone4);
        
        log.info("Telefones criados: {}", telefoneRepository.count());
        
        // Log final
        log.info("Inicialização completa - {} clientes e {} telefones", 
                clienteRepository.count(), telefoneRepository.count());
    }
}