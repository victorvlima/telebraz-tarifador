-- data.sql
-- Este arquivo será executado após a criação das tabelas pelo Hibernate

-- Inserir clientes de teste
INSERT INTO clientes (cpf, nome, email, tipo_plano, status, data_cadastro) VALUES
('12345678901', 'João Silva', 'joao@email.com', 'PRE_PAGO', 'ATIVO', CURRENT_TIMESTAMP),
('98765432100', 'Maria Santos', 'maria@email.com', 'POS_PAGO_BASICO', 'ATIVO', CURRENT_TIMESTAMP),
('11122233344', 'Pedro Oliveira', 'pedro@email.com', 'POS_PAGO_PREMIUM', 'ATIVO', CURRENT_TIMESTAMP);

-- Inserir telefones (usando o nome correto da coluna)
INSERT INTO telefones (numero, ddd, is_principal, cliente_id) VALUES
('987654321', '11', true, 1),
('123456789', '11', true, 2),
('555666777', '11', true, 3),
('999888777', '11', false, 1);