-- Script de inserção de dados para teste de todas as funcionalidades para uma empresa de paisagismo
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
('Adubo Orgânico', 'Adubo para plantas 5kg', 'Materiais', 50, 20.90),
('Semente de Grama', 'Semente para grama esmeralda', 'Materiais', 200, 15.90),
('Régua de Jardinagem', 'Régua para medir áreas de jardinagem', 'Ferramentas', 30, 25.00),
('Canteiro Modular', 'Canteiro de plantas modular', 'Materiais', 15, 120.00),
('Corta-Grama Elétrico', 'Máquina corta-grama elétrica 1500W', 'Equipamentos', 8, 350.00),
('Tesoura de Poda', 'Tesoura de poda de alta precisão', 'Ferramentas', 12, 80.00),
('Kit de Jardinagem', 'Kit completo com pás, ancinhos e regadores', 'Ferramentas', 20, 95.00),
('Piso para Jardim', 'Piso modular para áreas externas', 'Materiais', 100, 45.00),
('Bancada de Jardinagem', 'Bancada resistente para trabalho no jardim', 'Equipamentos', 6, 220.00),
('Iluminação LED para Jardim', 'Lâmpada de LED para áreas externas', 'Acessórios', 50, 50.00);

-- Inserir funcionários para teste
INSERT INTO funcionario (nome, cpf, cargo, data_nasc, telefone, cep, rua, numero, bairro, cidade, estado, ativo) VALUES 
('Paulo Souza', '12345678901', 'Diretor', '1985-02-15', '(11) 99888-7766', '01234-567', 'Rua dos Jardins', '123', 'Centro', 'São Paulo', 'SP', true),
('Fernanda Costa', '98765432109', 'Auxiliar', '1992-06-10', '(11) 99777-8855', '02345-678', 'Av. Verde', '456', 'Vila Verde', 'São Paulo', 'SP', true),
('Carlos Martins', '45678912345', 'Estoquista', '1990-11-25', '(11) 99666-5544', '03456-789', 'Rua das Flores', '789', 'Vila Nova', 'São Paulo', 'SP', true),
('Juliana Almeida', '78912345678', 'Diretor', '1983-03-30', '(11) 99555-4433', '04567-890', 'Av. Brasil', '1010', 'Consolação', 'São Paulo', 'SP', true),
('Roberta Lima', '32165498701', 'Auxiliar', '1987-09-22', '(11) 99444-3322', '05678-901', 'Rua das Palmeiras', '234', 'Pinheiros', 'São Paulo', 'SP', true);

-- Inserir clientes para teste
INSERT INTO cliente (nome, cpf_cnpj, telefone, email, tipo, ativo, cep, rua, numero, bairro, cidade, estado) VALUES 
('Cláudia Silva', '11122233344', '(11) 98765-4321', 'claudia@email.com', 'PF', true, '12345-678', 'Rua do Jardim', '100', 'Jardim Paulista', 'São Paulo', 'SP'),
('Carlos Rocha', '22233344455', '(11) 97654-3210', 'carlos@email.com', 'PF', true, '23456-789', 'Av. Comercial', '200', 'Moema', 'São Paulo', 'SP'),
('Beatriz Torres', '33344455566', '(11) 96543-2109', 'beatriz@email.com', 'PF', true, '34567-890', 'Rua da Horta', '300', 'Itaim', 'São Paulo', 'SP'),
('Paisagens & Cia LTDA', '12345678000190', '(11) 3322-1100', 'contato@paisagenscia.com', 'PJ', true, '45678-901', 'Av. Verde', '400', 'Brás', 'São Paulo', 'SP'),
('Jardim Perfeito Salão', '98765432000121', '(11) 3311-2200', 'contato@jardimperfeito.com', 'PJ', true, '56789-012', 'Rua das Flores', '500', 'Centro', 'São Paulo', 'SP');

-- Inserir cupons para teste
INSERT INTO cupom (codigo, descricao, valor, data_inicio, validade, condicoes_uso) VALUES 
('PAISAGISMO10', 'Cupom de boas-vindas R$ 10,00 de desconto', 10.00, '2025-01-01', '2025-12-31', 'Válido para primeira compra'),
('DESCONTO20', 'Promoção especial R$ 20,00 de desconto', 20.00, '2025-05-01', '2025-06-30', 'Compras acima de R$ 100,00'),
('ANIVERSARIO30', 'Cupom de aniversário R$ 30,00 de desconto', 30.00, '2025-05-01', '2025-05-31', 'Compras acima de R$ 150,00'),
('AMIGOS15', 'Indique um amigo e ganhe R$ 15,00 de desconto', 15.00, '2025-04-01', '2025-07-31', 'Válido uma vez por cliente'),
('EXPIRADO10', 'Cupom expirado R$ 10,00 de desconto', 10.00, '2024-01-01', '2024-12-31', 'Cupom vencido para teste');

-- Inserir pedidos para teste (com funcionário e cupom)
-- Pedido 1: Com cupom válido e funcionário
INSERT INTO pedido (data_requisicao, data_entrega, cliente_id, funcionario_id, cupom_id, valor_desconto) 
VALUES ('2025-05-12', '2025-05-15', 1, 1, 1, 10.00);

-- Pedido 2: Com funcionário, sem cupom
INSERT INTO pedido (data_requisicao, data_entrega, cliente_id, funcionario_id) 
VALUES ('2025-05-12', '2025-05-16', 2, 2);

-- Pedido 3: Com cupom de maior valor
INSERT INTO pedido (data_requisicao, data_entrega, cliente_id, funcionario_id, cupom_id, valor_desconto) 
VALUES ('2025-05-12', '2025-05-17', 3, 3, 3, 30.00);

-- Pedido 4: Para pessoa jurídica com funcionário gerente
INSERT INTO pedido (data_requisicao, data_entrega, cliente_id, funcionario_id) 
VALUES ('2025-05-13', null, 4, 4);

-- Pedido 5: Com cupom e outro funcionário
INSERT INTO pedido (data_requisicao, data_entrega, cliente_id, funcionario_id, cupom_id, valor_desconto) 
VALUES ('2025-05-13', '2025-05-20', 5, 2, 2, 20.00);

-- Inserir itens nos pedidos para teste
-- Itens do pedido 1
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (1, 1, 2); -- 2 Adubos
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (1, 2, 1); -- 1 Semente de Grama

-- Itens do pedido 2
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (2, 4, 1); -- 1 Canteiro Modular
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (2, 5, 1); -- 1 Corta-Grama Elétrico

-- Itens do pedido 3
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (3, 6, 1); -- 1 Tesoura de Poda
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (3, 3, 2); -- 2 Kits de Jardinagem

-- Itens do pedido 4
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (4, 7, 5); -- 5 Pisos para Jardim
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (4, 8, 5); -- 5 Bancadas de Jardinagem
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (4, 9, 10); -- 10 Iluminações LED

-- Itens do pedido 5
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (5, 10, 1); -- 1 Iluminação LED
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (5, 1, 1); -- 1 Adubo Orgânico
INSERT INTO item_pedido (id_pedido, id_produto, quantidade) VALUES (5, 2, 1); -- 1 Semente de Grama

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
VALUES (7, 'saida', 5, 'Venda - Pedido #4', '2025-05-13');
INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, motivo, data) 
VALUES (8, 'saida', 5, 'Venda - Pedido #4', '2025-05-13');
INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, motivo, data) 
VALUES (9, 'saida', 10, 'Venda - Pedido #4', '2025-05-13');

-- Movimentações para pedido 5
INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, motivo, data) 
VALUES (10, 'saida', 1, 'Venda - Pedido #5', '2025-05-13');
INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, motivo, data) 
VALUES (1, 'saida', 1, 'Venda - Pedido #5', '2025-05-13');
INSERT INTO movimentacao_estoque (id_produto, tipo, quantidade, motivo, data) 
VALUES (2, 'saida', 1, 'Venda - Pedido #5', '2025-05-13');

-- Atualizar o estoque dos produtos após as vendas
UPDATE produto SET quantidade = 48 WHERE id = 1; -- 50 - 2 = 48 Adubo
UPDATE produto SET quantidade = 199 WHERE id = 2; -- 200 - 1 = 199 Semente de Grama
UPDATE produto SET quantidade = 29 WHERE id = 3; -- 30 - 2 = 29 Kit Jardinagem
UPDATE produto SET quantidade = 14 WHERE id = 4; -- 15 - 1 = 14 Canteiro Modular
UPDATE produto SET quantidade = 7 WHERE id = 5;  -- 8 - 1 = 7 Corta-Grama
UPDATE produto SET quantidade = 11 WHERE id = 6; -- 12 - 1 = 11 Tesoura de Poda
UPDATE produto SET quantidade = 19 WHERE id = 7; -- 20 - 1 = 19 Piso Jardim
UPDATE produto SET quantidade = 15 WHERE id = 8; -- 16 - 1 = 15 Bancada Jardinagem
UPDATE produto SET quantidade = 49 WHERE id = 9; -- 50 - 10 = 49 Iluminação LED

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
