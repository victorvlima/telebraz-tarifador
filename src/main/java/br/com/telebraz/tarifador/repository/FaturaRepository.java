package br.com.telebraz.tarifador.repository;

import br.com.telebraz.tarifador.entity.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FaturaRepository extends JpaRepository<Fatura, Long> {
    
    List<Fatura> findByClienteId(Long clienteId);
    
    List<Fatura> findByDataGeracaoBetween(LocalDateTime inicio, LocalDateTime fim);
    
    List<Fatura> findByClienteCpf(String cpf);
}