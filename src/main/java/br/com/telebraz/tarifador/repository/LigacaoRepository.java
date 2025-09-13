package br.com.telebraz.tarifador.repository;

import br.com.telebraz.tarifador.entity.Ligacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LigacaoRepository extends JpaRepository<Ligacao, Long> {
    
    List<Ligacao> findByClienteId(Long clienteId);
    
    List<Ligacao> findByProcessadaFalse();
    
    List<Ligacao> findByFaturaId(Long faturaId);
    
    @Query("SELECT l FROM Ligacao l WHERE l.cliente.id = :clienteId " +
           "AND l.inicioLigacao BETWEEN :inicio AND :fim")
    List<Ligacao> findByClienteIdAndPeriodo(Long clienteId, LocalDateTime inicio, LocalDateTime fim);
    
    @Query("SELECT l FROM Ligacao l WHERE l.fatura IS NULL AND l.processada = false")
    List<Ligacao> findLigacoesSemFatura();
}