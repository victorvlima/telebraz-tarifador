package br.com.telebraz.tarifador.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "telefones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Telefone {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 15)
    private String numero;
    
    @Column(length = 2)
    private String ddd;
    
    // CORREÇÃO: Mapear corretamente a coluna principal
    @Column(name = "is_principal", nullable = false)
    @Builder.Default
    private Boolean principal = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    public String getNumeroCompleto() {
        return ddd != null ? "(" + ddd + ") " + numero : numero;
    }
}