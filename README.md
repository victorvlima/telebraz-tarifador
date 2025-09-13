# 📞 Sistema de Tarifador Telecom

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![Maven](https://img.shields.io/badge/Maven-3.8+-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)
![Status](https://img.shields.io/badge/Status-Active-success)

Sistema de billing para operadoras de telecomunicações desenvolvido com Spring Boot e Java 21.

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **H2 Database** (para desenvolvimento)
- **Lombok**
- **Maven**

## 📋 Funcionalidades

- ✅ Processamento de ligações em lote
- ✅ Cálculo automático de tarifas por tipo de plano
- ✅ Suporte a diferentes tipos de ligação (Local, LD, Internacional)
- ✅ Diferenciação de horários (Comercial/Noturno)
- ✅ Geração de faturas detalhadas
- ✅ API REST para integração externa

## 🏗️ Arquitetura

### Estrutura do Projeto
```
src/main/java/br/com/telebraz/tarifador/
├── config/          # Configurações e inicializadores
├── controller/      # Controllers REST
├── dto/            # Data Transfer Objects
├── entity/         # Entidades JPA
├── enums/          # Enumerações
├── repository/     # Repositórios JPA
├── service/        # Lógica de negócio
└── strategy/       # Padrões Strategy para cálculo
```

### Padrões Utilizados
- **Strategy Pattern** - Para cálculo de tarifas
- **Repository Pattern** - Para acesso a dados
- **DTO Pattern** - Para transferência de dados

## 🚀 Como Executar

### Pré-requisitos
- Java 21+
- Maven 3.8+

### Passos
1. Clone o repositório:
```bash
git clone https://github.com/victorvlima/telebraz-tarifador.git
cd telebraz-tarifador
```

2. Execute o projeto:
```bash
./mvnw spring-boot:run
```

3. Acesse a aplicação:
- **API**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
  - URL: `jdbc:h2:mem:tarifador`
  - User: `sa`
  - Password: (vazio)

## 🌐 Endpoints da API

### Processar Ligações
```http
POST /api/v1/tarifador/processar-ligacoes
Content-Type: application/json

{
  \"ligacoes\": [
    {
      \"numeroOrigem\": \"987654321\",
      \"numeroDestino\": \"123456789\",
      \"inicioLigacao\": \"2024-01-15T14:30:00\",
      \"duracaoMinutos\": 10,
      \"tipoLigacao\": \"LOCAL\"
    },
    {
      \"numeroOrigem\": \"987654321\",
      \"numeroDestino\": \"555666777\",
      \"inicioLigacao\": \"2024-01-15T20:15:00\",
      \"duracaoMinutos\": 25,
      \"tipoLigacao\": \"LONGA_DISTANCIA\"
    }
  ],
  \"observacoes\": \"Processamento de teste\"
}
```

### Endpoints de Teste
- `GET /api/v1/test/status` - Status do sistema
- `GET /api/v1/test/clientes` - Listar clientes
- `GET /api/v1/test/telefones` - Listar telefones
- `GET /api/v1/test/telefones/principais` - Listar telefones principais
- `GET /api/v1/tarifador/health` - Health check da API

## 💰 Tipos de Plano

### Pré-pago
- **Tarifa**: R$ 0,50 por minuto
- **Desconto noturno**: 20% (18h-8h)
- **Franquia**: Não possui

### Pós-pago Básico
- **Franquia**: 100 minutos gratuitos
- **Excedente**: R$ 0,30 por minuto
- **Desconto noturno**: 20% (18h-8h)

### Pós-pago Premium
- **Franquia**: 500 minutos gratuitos
- **Excedente**: R$ 0,20 por minuto
- **Desconto noturno**: 20% (18h-8h)

## 📊 Tipos de Ligação

- **Local**: Tarifa normal (multiplicador 1.0x)
- **Longa Distância**: +50% sobre a tarifa (multiplicador 1.5x)
- **Internacional**: +200% sobre a tarifa (multiplicador 3.0x)

## 🕐 Horários

- **Comercial**: 8h às 18h - Tarifa normal
- **Noturno**: 18h às 8h - Desconto de 20%

## 🧪 Dados de Teste

O sistema já vem com dados pré-carregados:

### Clientes
| Nome | CPF | Plano | Telefone Principal |
|------|-----|-------|-------------------|
| João Silva | 12345678901 | Pré-pago | (11) 987654321 |
| Maria Santos | 98765432100 | Pós-pago Básico | (11) 123456789 |
| Pedro Oliveira | 11122233344 | Pós-pago Premium | (11) 555666777 |

### Exemplo de Teste com cURL
```bash
curl -X POST http://localhost:8080/api/v1/tarifador/processar-ligacoes \\
  -H \"Content-Type: application/json\" \\
  -d '{
    \"ligacoes\": [
      {
        \"numeroOrigem\": \"987654321\",
        \"numeroDestino\": \"123456789\",
        \"inicioLigacao\": \"2024-01-15T14:30:00\",
        \"duracaoMinutos\": 10,
        \"tipoLigacao\": \"LOCAL\"
      }
    ],
    \"observacoes\": \"Teste de processamento\"
  }'
```

## 📈 Exemplo de Resposta

```json
[
  {
    \"faturaId\": 1,
    \"cpfCliente\": \"12345678901\",
    \"nomeCliente\": \"João Silva\",
    \"tipoPlano\": \"Pré-pago\",
    \"totalMinutos\": 10,
    \"minutosGratuitosUtilizados\": 0,
    \"valorTotal\": 5.00,
    \"dataGeracao\": \"2024-01-15T15:30:00\",
    \"quantidadeLigacoes\": 1
  }
]
```

## 🔧 Configuração

### application.yml
```yaml
spring:
  application:
    name: tarifador
  datasource:
    url: jdbc:h2:mem:tarifador
    driver-class-name: org.h2.Driver
    username: sa
    password: 
  h2:
    console:
      enabled: true
      path: /h2-console
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true

server:
  port: 8080
```

## 🧪 Testes

### Executar Testes
```bash
./mvnw test
```

### Verificar Status
```bash
curl http://localhost:8080/api/v1/test/status
```

Resposta esperada:
```
Database: 3 clientes, 4 telefones (3 principais)
```

## 🏛️ Arquitetura Técnica

### Design Patterns Implementados

1. **Strategy Pattern**
   - `CalculoTarifaStrategy` - Interface base
   - `PrePagoStrategy` - Implementação para pré-pago
   - `PosPagoBasicoStrategy` - Implementação pós-pago básico
   - `PosPagoPremiumStrategy` - Implementação pós-pago premium

2. **Repository Pattern**
   - Abstraindo acesso aos dados
   - Queries customizadas com Spring Data JPA

3. **DTO Pattern**
   - Separação entre entidades e dados de transferência
   - `LigacaoDTO`, `ProcessamentoRequest`, `FaturaResponse`

### Entidades Principais

- **Cliente** - Dados do cliente e tipo de plano
- **Telefone** - Números associados ao cliente
- **Ligacao** - Registro de chamadas realizadas
- **Fatura** - Consolidação de cobrança por período

## 🔄 Fluxo de Processamento

1. **Recebimento** - API recebe massa de ligações via JSON
2. **Associação** - Sistema associa ligações aos clientes via número
3. **Cálculo** - Strategy Pattern calcula tarifas por tipo de plano
4. **Agrupamento** - Ligações são agrupadas por cliente
5. **Faturamento** - Geração de faturas consolidadas
6. **Resposta** - Retorno das faturas geradas

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para detalhes.

## 🚀 Próximas Funcionalidades ??

- [ ] Documentação com OpenAPI/Swagger
- [ ] Autenticação e autorização
- [ ] Persistência em banco PostgreSQL
- [ ] Cache com Redis
- [ ] Métricas com Micrometer
- [ ] Containerização com Docker
- [ ] CI/CD com GitHub Actions

## 👨‍💻 Autor

**Victor Lima**
- GitHub: [@victorvlima](https://github.com/victorvlima)
- Projeto: [telebraz-tarifador](https://github.com/victorvlima/telebraz-tarifador)

---

⭐ Se este projeto te ajudou, considere dar uma estrela!

**Desenvolvido como exemplo de sistema de billing para telecomunicações utilizando Spring Boot e boas práticas de desenvolvimento.**