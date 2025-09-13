# 📞 Sistema de Tarifador Telecom

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

src/main/java/br/com/telebraz/tarifador/ 
├── config/ # Configurações e inicializadores 
├── controller/ # Controllers REST 
├── dto/ # Data Transfer Objects 
├── entity/ # Entidades JPA 
├── enums/ # Enumerações 
├── repository/ # Repositórios JPA 
├── service/ # Lógica de negócio 
└── strategy/ # Padrões Strategy para cálculo


### Padrões Utilizados
- **Strategy Pattern** - Para cálculo de tarifas
- **Repository Pattern** - Para acesso a dados
- **DTO Pattern** - Para transferência de dados