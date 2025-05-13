-- Script para testar as três novas funcionalidades implementadas:
-- 1. Aplicação de cupons de desconto
-- 2. Vinculação de funcionários aos pedidos
-- 3. Validação de estoque

USE studiomuda;

-- ================================================================
-- 1. TESTE DE APLICAÇÃO DE CUPONS
-- ================================================================

-- 1.1 Listar todos os cupons disponíveis
SELECT 'LISTA DE CUPONS DISPONÍVEIS:' AS 'Teste de Cupons';
SELECT 
    id, 
    codigo, 
    valor, 
    data_inicio, 
    validade, 
    CASE 
        WHEN CURRENT_DATE() BETWEEN data_inicio AND validade THEN 'VÁLIDO'
        ELSE 'INVÁLIDO'
    END AS status
FROM cupom;

-- 1.2 Verificar pedidos com cupons aplicados
SELECT 'PEDIDOS COM CUPONS:' AS 'Teste de Cupons';
SELECT 
    p.id,
    p.data_requisicao,
    c.nome AS cliente,
    cp.codigo AS cupom,
    cp.valor AS valor_cupom,
    p.valor_desconto AS desconto_aplicado,
    (SELECT SUM(ip.quantidade * pr.valor) FROM item_pedido ip 
     JOIN produto pr ON ip.id_produto = pr.id 
     WHERE ip.id_pedido = p.id) AS valor_bruto,
    ((SELECT SUM(ip.quantidade * pr.valor) FROM item_pedido ip 
     JOIN produto pr ON ip.id_produto = pr.id 
     WHERE ip.id_pedido = p.id) - p.valor_desconto) AS valor_final
FROM 
    pedido p
    JOIN cliente c ON p.cliente_id = c.id
    JOIN cupom cp ON p.cupom_id = cp.id;

-- 1.3 Testar a criação de um novo cupom
INSERT INTO cupom (codigo, descricao, valor, data_inicio, validade, condicoes_uso) 
VALUES ('TESTE100', 'Cupom de teste R$ 100,00', 100.00, CURRENT_DATE(), DATE_ADD(CURRENT_DATE(), INTERVAL 30 DAY), 'Cupom para teste de sistema');

SELECT 'NOVO CUPOM CRIADO:' AS 'Teste de Cupons';
SELECT * FROM cupom WHERE codigo = 'TESTE100';

-- ================================================================
-- 2. TESTE DE VINCULAÇÃO DE FUNCIONÁRIOS
-- ================================================================

-- 2.1 Listar todos os funcionários disponíveis
SELECT 'LISTA DE FUNCIONÁRIOS:' AS 'Teste de Funcionários';
SELECT id, nome, cargo, ativo FROM funcionario;

-- 2.2 Verificar pedidos com seus respectivos funcionários
SELECT 'PEDIDOS COM FUNCIONÁRIOS:' AS 'Teste de Funcionários';
SELECT 
    p.id AS pedido_id,
    p.data_requisicao,
    c.nome AS cliente,
    f.id AS funcionario_id,
    f.nome AS funcionario_nome,
    f.cargo AS funcionario_cargo
FROM 
    pedido p
    JOIN cliente c ON p.cliente_id = c.id
    JOIN funcionario f ON p.funcionario_id = f.id;

-- 2.3 Contagem de vendas por funcionário
SELECT 'VENDAS POR FUNCIONÁRIO:' AS 'Teste de Funcionários';
SELECT 
    f.nome,
    f.cargo,
    COUNT(p.id) AS total_vendas,
    SUM((SELECT SUM(ip.quantidade * pr.valor) FROM item_pedido ip 
         JOIN produto pr ON ip.id_produto = pr.id 
         WHERE ip.id_pedido = p.id) - COALESCE(p.valor_desconto, 0)) AS valor_total_vendas
FROM 
    funcionario f
    LEFT JOIN pedido p ON f.id = p.funcionario_id
GROUP BY 
    f.id, f.nome, f.cargo
ORDER BY 
    valor_total_vendas DESC;

-- 2.4 Top funcionários com cargo "Auxiliar" (para testar dashboard)
SELECT 'TOP AUXILIARES:' AS 'Teste de Funcionários';
SELECT 
    f.nome AS auxiliar, 
    COUNT(p.id) AS vendas 
FROM 
    funcionario f 
    JOIN pedido p ON f.id = p.funcionario_id 
WHERE 
    f.cargo = 'Auxiliar' 
GROUP BY 
    f.nome 
ORDER BY 
    vendas DESC 
LIMIT 10;

-- ================================================================
-- 3. TESTE DE VALIDAÇÃO DE ESTOQUE
-- ================================================================

-- 3.1 Verificar estoque atual dos produtos
SELECT 'ESTOQUE ATUAL DOS PRODUTOS:' AS 'Teste de Validação de Estoque';
SELECT 
    id,
    nome,
    quantidade AS estoque_disponivel
FROM 
    produto
ORDER BY
    quantidade;

-- 3.2 Simulação de validação de estoque (produtos que podem ser comprados em diferentes quantidades)
SELECT 'SIMULAÇÃO DE VALIDAÇÃO DE ESTOQUE:' AS 'Teste de Validação de Estoque';
SELECT 
    id,
    nome,
    quantidade AS estoque_atual,
    CASE 
        WHEN quantidade >= 10 THEN 'Pode comprar 10 unidades'
        WHEN quantidade >= 5 THEN 'Pode comprar 5 unidades'
        WHEN quantidade >= 1 THEN 'Pode comprar 1 unidade'
        ELSE 'Sem estoque disponível'
    END AS validacao
FROM 
    produto
ORDER BY
    quantidade;

-- 3.3 Testar tentativa de pedido com quantidade maior que o estoque disponível
-- Selecionar um produto com baixo estoque para teste
SELECT 'PRODUTO COM BAIXO ESTOQUE PARA TESTE:' AS 'Teste de Validação de Estoque';
SELECT 
    id,
    nome,
    quantidade AS estoque_disponivel
FROM 
    produto
WHERE 
    quantidade <= 2  -- Produtos com estoque crítico
LIMIT 1;

-- Use o resultado acima para testar manualmente adicionar mais unidades que o disponível

-- 3.4 Verificar movimentações de estoque já registradas
SELECT 'MOVIMENTAÇÕES DE ESTOQUE:' AS 'Teste de Validação de Estoque';
SELECT 
    me.id,
    p.nome AS produto,
    me.tipo,
    me.quantidade,
    me.motivo,
    me.data
FROM 
    movimentacao_estoque me
    JOIN produto p ON me.id_produto = p.id
ORDER BY
    me.data DESC, me.id DESC;

-- ================================================================
-- TESTE COMPLETO DAS TRÊS FUNCIONALIDADES
-- ================================================================

SELECT 'RESUMO COMPLETO DOS PEDIDOS:' AS 'Teste Integrado';
SELECT 
    p.id AS pedido_id,
    DATE_FORMAT(p.data_requisicao, '%d/%m/%Y') AS data_pedido,
    c.nome AS cliente,
    f.nome AS funcionario,
    f.cargo AS cargo,
    cp.codigo AS cupom,
    p.valor_desconto AS desconto,
    
    (SELECT SUM(ip.quantidade) FROM item_pedido ip WHERE ip.id_pedido = p.id) AS itens_totais,
    
    (SELECT GROUP_CONCAT(pr.nome SEPARATOR ', ') FROM item_pedido ip 
     JOIN produto pr ON ip.id_produto = pr.id 
     WHERE ip.id_pedido = p.id) AS produtos,
     
    (SELECT SUM(ip.quantidade * pr.valor) FROM item_pedido ip 
     JOIN produto pr ON ip.id_produto = pr.id 
     WHERE ip.id_pedido = p.id) AS valor_bruto,
     
    ((SELECT SUM(ip.quantidade * pr.valor) FROM item_pedido ip 
     JOIN produto pr ON ip.id_produto = pr.id 
     WHERE ip.id_pedido = p.id) - COALESCE(p.valor_desconto, 0)) AS valor_final
FROM 
    pedido p
    JOIN cliente c ON p.cliente_id = c.id
    JOIN funcionario f ON p.funcionario_id = f.id
    LEFT JOIN cupom cp ON p.cupom_id = cp.id
ORDER BY 
    p.id;
