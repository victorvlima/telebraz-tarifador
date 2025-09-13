package br.com.telebraz.tarifador.controller;

import br.com.telebraz.tarifador.entity.Cliente;
import br.com.telebraz.tarifador.entity.Telefone;
import br.com.telebraz.tarifador.repository.ClienteRepository;
import br.com.telebraz.tarifador.repository.TelefoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {
    
    private final ClienteRepository clienteRepository;
    private final TelefoneRepository telefoneRepository;
    
    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> listarClientes() {
        return ResponseEntity.ok(clienteRepository.findAll());
    }
    
    @GetMapping("/telefones")
    public ResponseEntity<List<Telefone>> listarTelefones() {
        return ResponseEntity.ok(telefoneRepository.findAll());
    }
    
    @GetMapping("/telefones/principais")
    public ResponseEntity<List<Telefone>> listarTelefonesPrincipais() {
        return ResponseEntity.ok(telefoneRepository.findTelefonesPrincipais());
    }
    
    @GetMapping("/cliente/{numero}/telefone")
    public ResponseEntity<Optional<Telefone>> buscarTelefonePorNumero(@PathVariable String numero) {
        return ResponseEntity.ok(telefoneRepository.findByNumero(numero));
    }
    
    @GetMapping("/status")
    public ResponseEntity<String> status() {
        long clientes = clienteRepository.count();
        long telefones = telefoneRepository.count();
        long telefonesPrincipais = telefoneRepository.findTelefonesPrincipais().size();
        
        return ResponseEntity.ok(
            String.format("Database: %d clientes, %d telefones (%d principais)", 
                         clientes, telefones, telefonesPrincipais)
        );
    }
}