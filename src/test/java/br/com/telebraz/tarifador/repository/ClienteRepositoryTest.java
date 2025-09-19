package br.com.telebraz.tarifador.repository;

import br.com.telebraz.tarifador.entity.Cliente;
import br.com.telebraz.tarifador.entity.Telefone;
import br.com.telebraz.tarifador.enums.StatusCliente;
import br.com.telebraz.tarifador.enums.TipoPlano;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ClienteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void deveBuscarClientePorCpf() {
        // Given
        Cliente cliente = Cliente.builder()
                .cpf("12345678901")
                .nome("João Silva")
                .email("joao@email.com")
                .tipoPlano(TipoPlano.PRE_PAGO)
                .status(StatusCliente.ATIVO)
                .dataCadastro(LocalDateTime.now())
                .build();
        
        entityManager.persistAndFlush(cliente);

        // When
        Optional<Cliente> resultado = clienteRepository.findByCpf("12345678901");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());
        assertEquals(TipoPlano.PRE_PAGO, resultado.get().getTipoPlano());
    }

    @Test
    void deveBuscarClientePorTelefone() {
        // Given
        Cliente cliente = Cliente.builder()
                .cpf("12345678901")
                .nome("João Silva")
                .email("joao@email.com")
                .tipoPlano(TipoPlano.PRE_PAGO)
                .status(StatusCliente.ATIVO)
                .dataCadastro(LocalDateTime.now())
                .build();
        
        cliente = entityManager.persistAndFlush(cliente);

        Telefone telefone = Telefone.builder()
                .numero("987654321")
                .ddd("11")
                .principal(true)
                .cliente(cliente)
                .build();
        
        entityManager.persistAndFlush(telefone);

        // When
        Optional<Cliente> resultado = clienteRepository.findByTelefoneNumero("987654321");

        // Then
        assertTrue(resultado.isPresent());
        assertEquals("João Silva", resultado.get().getNome());
    }

    @Test
    void deveVerificarExistenciaPorCpf() {
        // Given
        Cliente cliente = Cliente.builder()
                .cpf("12345678901")
                .nome("João Silva")
                .email("joao@email.com")
                .tipoPlano(TipoPlano.PRE_PAGO)
                .status(StatusCliente.ATIVO)
                .dataCadastro(LocalDateTime.now())
                .build();
        
        entityManager.persistAndFlush(cliente);

        // When & Then
        assertTrue(clienteRepository.existsByCpf("12345678901"));
        assertFalse(clienteRepository.existsByCpf("99999999999"));
    }
}