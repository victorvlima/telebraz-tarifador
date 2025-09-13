package br.com.telebraz.tarifador.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "faturas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Fatura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @Column(name = "data_geracao")
    private LocalDateTime dataGeracao;
    
    @Column(name = "periodo_inicio")
    private LocalDateTime periodoInicio;
    
    @Column(name = "periodo_fim")
    private LocalDateTime periodoFim;
    
    @Column(name = "total_minutos")
    private Integer totalMinutos;
    
    @Column(name = "minutos_gratuitos_utilizados")
    private Integer minutosGratuitosUtilizados;
    
    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;
    
    @Column(name = "quantidade_ligacoes")
    private Integer quantidadeLigacoes;
    
    @PrePersist
    protected void onCreate() {
        dataGeracao = LocalDateTime.now();
    }
}