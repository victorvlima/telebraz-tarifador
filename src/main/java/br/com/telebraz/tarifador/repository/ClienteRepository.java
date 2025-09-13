package br.com.telebraz.tarifador.repository;

import br.com.telebraz.tarifador.entity.Cliente;
import br.com.telebraz.tarifador.enums.StatusCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByCpf(String cpf);
    
    List<Cliente> findByStatus(StatusCliente status);
    
    @Query("SELECT c FROM Cliente c JOIN c.telefones t WHERE t.numero = :numero")
    Optional<Cliente> findByTelefoneNumero(String numero);
    
    boolean existsByCpf(String cpf);
}