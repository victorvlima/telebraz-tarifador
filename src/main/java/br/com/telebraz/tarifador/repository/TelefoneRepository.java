package br.com.telebraz.tarifador.repository;

import br.com.telebraz.tarifador.entity.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
    
    Optional<Telefone> findByNumero(String numero);
    
    List<Telefone> findByClienteId(Long clienteId);
    
    // Corrigir query para usar o nome correto da coluna
    @Query("SELECT t FROM Telefone t WHERE t.cliente.id = :clienteId AND t.principal = true")
    Optional<Telefone> findByClienteIdAndPrincipalTrue(@Param("clienteId") Long clienteId);
    
    // Query adicional para buscar por DDD
    List<Telefone> findByDdd(String ddd);
    
    // Buscar telefones principais
    @Query("SELECT t FROM Telefone t WHERE t.principal = true")
    List<Telefone> findTelefonesPrincipais();
}