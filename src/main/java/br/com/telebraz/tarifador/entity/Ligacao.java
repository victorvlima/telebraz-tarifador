package br.com.telebraz.tarifador.entity;

import br.com.telebraz.tarifador.enums.TipoLigacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ligacoes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ligacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numero_origem", nullable = false)
    private String numeroOrigem;
    
    @Column(name = "numero_destino", nullable = false)
    private String numeroDestino;
    
    @Column(name = "inicio_ligacao", nullable = false)
    private LocalDateTime inicioLigacao;
    
    @Column(name = "duracao_minutos", nullable = false)
    private Integer duracaoMinutos;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_ligacao", nullable = false)
    private TipoLigacao tipoLigacao;
    
    @Column(name = "valor_calculado", precision = 10, scale = 2)
    private BigDecimal valorCalculado;
    
    @Column(name = "processada")
    private Boolean processada;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    // Adicionar referência à fatura (opcional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fatura_id")
    private Fatura fatura;
    
    @PrePersist
    protected void onCreate() {
        if (processada == null) {
            processada = false;
        }
    }
    
    public boolean isHorarioComercial() {
        int hora = inicioLigacao.getHour();
        return hora >= 8 && hora < 18;
    }
}