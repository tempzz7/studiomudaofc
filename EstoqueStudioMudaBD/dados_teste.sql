-- Script de inserção de dados para teste de todas as funcionalidades
USE studiomuda;

-- Limpar dados existentes (opcional - remova se quiser preservar dados atuais)
DELETE FROM item_pedido;
DELETE FROM pedido;
DELETE FROM movimentacao_estoque;
DELETE FROM historico_estoque;
DELETE FROM historico_cliente;
DELETE FROM historico_funcionario;
DELETE FROM produto;
DELETE FROM cliente;
DELETE FROM funcionario;
DELETE FROM cupom;

-- Inserir produtos para teste
INSERT INTO produto (nome, descricao, tipo, quantidade, valor) VALUES 
('Shampoo Professional', 'Shampoo para cabelos profissional 500ml', 'Cabelo', 20, 45.90),
('Condicionador Premium', 'Condicionador hidratante 500ml', 'Cabelo', 15, 49.90),
('Máscara Hidratante', 'Máscara de tratamento intensivo 300g', 'Cabelo', 10, 65.90),
('Óleo de Argan', 'Óleo finalizador 60ml', 'Cabelo', 8, 39.90),
('Escova Profissional', 'Escova de madeira com cerdas mistas', 'Acessórios', 5, 89.90),
('Secador 2000W', 'Secador profissional com 3 velocidades', 'Equipamentos', 3, 299.90),
('Chapinha Titanium', 'Chapinha com placas de titânio', 'Equipamentos', 2, 259.90),
('Tintura Loiro Platinado', 'Coloração permanente', 'Coloração', 12, 29.90),
('Tintura Castanho Escuro', 'Coloração permanente', 'Coloração', 12, 29.90),
('Luvas de Proteção', 'Pacote com 10 pares', 'Acessórios', 30, 15.90);

-- Inserir funcionários para teste
INSERT INTO funcionario (nome, cpf, cargo, data_nasc, telefone, cep, rua, numero, bairro, cidade, estado, ativo) VALUES 
('Ana Silva', '12345678901', 'Cabeleireira', '1990-05-15', '(11) 99888-7766', '01234-567', 'Rua das Flores', '123', 'Centro', 'São Paulo', 'SP', true),
('João Oliveira', '98765432109', 'Auxiliar', '1995-08-22', '(11) 99777-8855', '02345-678', 'Av. Principal', '456', 'Jardins', 'São Paulo', 'SP', true),
('Maria Santos', '45678912345', 'Atendente', '1988-12-10', '(11) 99666-5544', '03456-789', 'Rua do Comércio', '789', 'Vila Nova', 'São Paulo', 'SP', true),
('Carlos Ferreira', '78912345678', 'Gerente', '1982-03-25', '(11) 99555-4433', '04567-890', 'Av. Brasil', '1010', 'Consolação', 'São Paulo', 'SP', true),
('Julia Martins', '32165498701', 'Cabeleireira', '1992-07-08', '(11) 99444-3322', '05678-901', 'Rua das Palmeiras', '234', 'Pinheiros', 'São Paulo', 'SP', true);

-- Inserir clientes para teste
INSERT INTO cliente (nome, cpf_cnpj, telefone, email, tipo, ativo, cep, rua, numero, bairro, cidade, estado) VALUES 
('Fernanda Lima', '11122233344', '(11) 98765-4321', 'fernanda@email.com', 'PF', true, '12345-678', 'Rua dos Clientes', '100', 'Vila Mariana', 'São Paulo', 'SP'),
('Roberto Gomes', '22233344455', '(11) 97654-3210', 'roberto@email.com', 'PF', true, '23456-789', 'Av. dos Compradores', '200', 'Moema', 'São Paulo', 'SP'),
('Claudia Mendes', '33344455566', '(11) 96543-2109', 'claudia@email.com', 'PF', true, '34567-890', 'Rua das Compras', '300', 'Itaim', 'São Paulo', 'SP'),
('Moda & Estilo LTDA', '12345678000190', '(11) 3322-1100', 'contato@modaestilo.com', 'PJ', true, '45678-901', 'Av. Comercial', '400', 'Brás', 'São Paulo', 'SP'),
('Beleza Total Salão', '98765432000121', '(11) 3311-2200', 'contato@belezatotal.com', 'PJ', true, '56789-012', 'Rua do Comércio', '500', 'Centro', 'São Paulo', 'SP');

-- Inserir cupons para teste
INSERT INTO cupom (codigo, descricao, valor, data_inicio, validade, condicoes_uso) VALUES 
('BEMVINDO10', 'Cupom de boas-vindas R$ 10,00 de desconto', 10.00, '2025-01-01', '2025-12-31', 'Válido para primeira compra'),
('PROMO20', 'Promoção especial R$ 20,00 de desconto', 20.00, '2025-05-01', '2025-06-30', 'Compras acima de R$ 100,00'),
('ANIVERSARIO50', 'Cupom de aniversário R$ 50,00 de desconto', 50.00, '2025-05-01', '2025-05-31', 'Compras acima de R$ 200,00'),
('AMIGO15', 'Indique um amigo e ganhe R$ 15,00 de desconto', 15.00, '2025-04-01', '2025-07-31', 'Válido uma vez por cliente'),
('EXPIRADO25', 'Cupom expirado R$ 25,00 de desconto', 25.00, '2024-01-01', '2024-12-31', 'Cupom vencido para teste');

-- Inserir pedidos para teste (com funcionário e cupom)
-- Pedido 1: Com cupom válido e funcionário
INSERT INTO pedido (data_requisicao, data_entrega, cliente_id, funcionario_id, cupom_id, valor_desconto) 
VALUES ('2025-05-12', '2025-05-15', 1, 1, 1, 10.00);

-- Pedido 2: Com funcionário, sem cupom
INSERT INTO pedido (data_requisicao, data_entrega, cliente_id, funcionario_id) 
VALUES ('2025-05-12', '2025-05-16', 2, 2);

-- Pedido 3: Com cupom de maior valor
INSERT INTO pedido (data_requisicao, data_entrega, cliente_id, funcionario_id, cupom_id, valor_desconto) 
VALUES ('2025-05-12', '2025-05-17', 3, 3, 3, 50.00);

-- Pedido 4: Para pessoa jurídica com funcionário gerente
INSERT INTO pedido (data_requisicao, data_entrega, cliente_id, funcionario_id) 
VALUES ('2025-05-13', null, 4, 4);

-- Pedido 5: Com cupom e outro funcionário
INSERT INTO pedido (data_requisicao, data_entrega, cliente_id, funcionario_id, cupom_id, valor_desconto) 
VALUES ('2025-05-13', '2025-05-20', 5, 2, 2, 20.00);

-- Inserir itens nos pedidos para teste
-- Itens do pedido 1
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (1, 1, 2); -- 2 Shampoos
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (1, 2, 1); -- 1 Condicionador

-- Itens do pedido 2
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (2, 4, 1); -- 1 Óleo de Argan
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (2, 5, 1); -- 1 Escova Profissional

-- Itens do pedido 3
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (3, 6, 1); -- 1 Secador
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (3, 3, 2); -- 2 Máscaras Hidratantes

-- Itens do pedido 4
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (4, 8, 5); -- 5 Tinturas Loiro
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (4, 9, 5); -- 5 Tinturas Castanho
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (4, 10, 10); -- 10 Luvas

-- Itens do pedido 5
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (5, 7, 1); -- 1 Chapinha
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (5, 1, 1); -- 1 Shampoo
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (5, 2, 1); -- 1 Condicionador

-- Registrar as movimentações de estoque correspondentes aos itens dos pedidos
-- Movimentações para pedido 1
INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, motivo, data) 
VALUES (1, 'saida', 2, 'Venda - Pedido #1', '2025-05-12');
INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, motivo, data) 
VALUES (2, 'saida', 1, 'Venda - Pedido #1', '2025-05-12');

-- Movimentações para pedido 2
INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, motivo, data) 
VALUES (4, 'saida', 1, 'Venda - Pedido #2', '2025-05-12');
INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, motivo, data) 
VALUES (5, 'saida', 1, 'Venda - Pedido #2', '2025-05-12');

-- Movimentações para pedido 3
INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, motivo, data) 
VALUES (6, 'saida', 1, 'Venda - Pedido #3', '2025-05-12');
INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, motivo, data) 
VALUES (3, 'saida', 2, 'Venda - Pedido #3', '2025-05-12');

-- Movimentações para pedido 4
INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, motivo, data) 
VALUES (8, 'saida', 5, 'Venda - Pedido #4', '2025-05-13');
INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, motivo, data) 
VALUES (9, 'saida', 5, 'Venda - Pedido #4', '2025-05-13');
INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, motivo, data) 
VALUES (10, 'saida', 10, 'Venda - Pedido #4', '2025-05-13');

-- Movimentações para pedido 5
INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, motivo, data) 
VALUES (7, 'saida', 1, 'Venda - Pedido #5', '2025-05-13');
INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, motivo, data) 
VALUES (1, 'saida', 1, 'Venda - Pedido #5', '2025-05-13');
INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, motivo, data) 
VALUES (2, 'saida', 1, 'Venda - Pedido #5', '2025-05-13');

-- Atualizar o estoque dos produtos após as vendas
UPDATE produto SET quantidade = 17 WHERE id = 1; -- 20 - 2 - 1 = 17 Shampoo
UPDATE produto SET quantidade = 13 WHERE id = 2; -- 15 - 1 - 1 = 13 Condicionador
UPDATE produto SET quantidade = 8 WHERE id = 3;  -- 10 - 2 = 8 Máscara
UPDATE produto SET quantidade = 7 WHERE id = 4;  -- 8 - 1 = 7 Óleo
UPDATE produto SET quantidade = 4 WHERE id = 5;  -- 5 - 1 = 4 Escova
UPDATE produto SET quantidade = 2 WHERE id = 6;  -- 3 - 1 = 2 Secador
UPDATE produto SET quantidade = 1 WHERE id = 7;  -- 2 - 1 = 1 Chapinha
UPDATE produto SET quantidade = 7 WHERE id = 8;  -- 12 - 5 = 7 Tintura Loiro
UPDATE produto SET quantidade = 7 WHERE id = 9;  -- 12 - 5 = 7 Tintura Castanho
UPDATE produto SET quantidade = 20 WHERE id = 10; -- 30 - 10 = 20 Luvas

-- Consulta para verificar se os dados foram inseridos corretamente
SELECT 'Produtos inseridos:' AS 'Verificação de Dados';
SELECT COUNT(*) AS total_produtos FROM produto;

SELECT 'Funcionários inseridos:' AS 'Verificação de Dados';
SELECT COUNT(*) AS total_funcionarios FROM funcionario;

SELECT 'Clientes inseridos:' AS 'Verificação de Dados';
SELECT COUNT(*) AS total_clientes FROM cliente;

SELECT 'Cupons inseridos:' AS 'Verificação de Dados';
SELECT COUNT(*) AS total_cupons FROM cupom;

SELECT 'Pedidos inseridos:' AS 'Verificação de Dados';
SELECT COUNT(*) AS total_pedidos FROM pedido;

SELECT 'Itens de pedido inseridos:' AS 'Verificação de Dados';
SELECT COUNT(*) AS total_itens_pedido FROM item_pedido;

SELECT 'Movimentações de estoque inseridas:' AS 'Verificação de Dados';
SELECT COUNT(*) AS total_movimentacoes FROM movimentacao_estoque;

-- Verificação dos pedidos com funcionários e cupons
SELECT 'Pedidos com dados completos:' AS 'Verificação Completa';
SELECT 
    p.id AS pedido_id,
    p.data_requisicao,
    p.data_entrega,
    c.nome AS cliente_nome,
    f.nome AS funcionario_nome,
    f.cargo AS funcionario_cargo,
    cp.codigo AS cupom_codigo,
    p.valor_desconto,
    (SELECT SUM(ip.quantidade * pr.valor) FROM item_pedido ip 
     JOIN produto pr ON ip.id_produto = pr.id 
     WHERE ip.id_pedido = p.id) AS valor_total,
    ((SELECT SUM(ip.quantidade * pr.valor) FROM item_pedido ip 
     JOIN produto pr ON ip.id_produto = pr.id 
     WHERE ip.id_pedido = p.id) - p.valor_desconto) AS valor_final
FROM 
    pedido p
    JOIN cliente c ON p.cliente_id = c.id
    JOIN funcionario f ON p.funcionario_id = f.id
    LEFT JOIN cupom cp ON p.cupom_id = cp.id
ORDER BY 
    p.id;
