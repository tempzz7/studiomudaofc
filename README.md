# 🌿 Studio Muda - Sistema de Controle de Estoque

Sistema desenvolvido para a empresa fictícia **Studio Muda LTDA**, especializada em paisagismo e comercialização de produtos naturais. O sistema permite o controle total de produtos, pedidos, clientes, funcionários e movimentações de estoque.

---

## ✅ Módulos Implementados

### 1. 📦 Produtos
- CRUD completo: cadastrar, listar, atualizar e deletar produtos.
- Campos: `nome`, `descricao`, `tipo`, `valor`, `quantidade`.
- Validações:
    - `nome`, `tipo` e `valor` são obrigatórios.
    - Tipos definidos via menu: Adubo, Plantas, Vasos, etc.
    - Quantidade é gerenciada exclusivamente pelo módulo de estoque.
    - Valor exibido com 2 casas decimais.

---

### 2. 👤 Funcionários
- Cadastro com validação de CPF (11 dígitos, com ou sem pontuação).
- Campos: `nome`, `cpf`, `cargo`, `data_nasc`, `telefone`, `endereco`, `ativo`.
- Cargos disponíveis: Diretor, Auxiliar, Estoquista.
- Exclusão lógica: `ativo = false`.
- Listagem separa funcionários ativos e inativos.
- Atualização permite manter campos com ENTER.
- Formatação de CPF e telefone aplicadas.

---

### 3. 🧍 Clientes
- Cadastro com validação de CPF (11 dígitos) e CNPJ (14 dígitos).
- Tipo PF/PJ definido automaticamente pela quantidade de dígitos.
- Campos: `nome`, `cpf_cnpj`, `telefone`, `email`, `endereco`, `tipo`, `ativo`.
- Exclusão lógica (`ativo = false`) com listagens separadas.
- Atualização permite manter os dados antigos com ENTER.
- CPF/CNPJ e telefone formatados conforme padrão nacional.

---

### 4. 🧾 Pedidos
- Criação de pedidos de venda vinculados a clientes ativos.
- Cadastro de itens por pedido com tabela intermediária (`item_pedido`).
- Campos: `data_requisicao`, `data_entrega`, `cliente_id`.
- Itens: `id_pedido`, `id_produto`, `quantidade`.

---

### 5. 📊 Estoque (Movimentação)
- Registro de entradas e saídas de produtos.
- CRUD completo para movimentações.
- Campos: `id_produto`, `tipo`, `quantidade`, `motivo`, `data`.
- Entrada de tipo via `E` ou `S`, convertido internamente.
- Quantidade deve ser positiva e válida em relação ao estoque.
- Filtro por tipo de movimentação (entrada, saída, todas).
- Permite retorno ao menu anterior em qualquer etapa.

---

## 🧠 Funcionalidades Extras
- Validação robusta de entrada (evita exceções de digitação).
- Menu principal com integração de todos os módulos.
- Formatação visual clara e amigável.
- Interface via terminal com menus numerados e padronizados.
- Todos os módulos seguem a mesma estrutura de CRUD.

---

## 🗃️ Banco de Dados
- Gerenciado em MySQL.
- Todas as tabelas criadas via `create_studiomuda.sql`.
- Tabelas: `produto`, `funcionario`, `cliente`, `pedido`, `item_pedido`, `movimentacao_estoque`.
- Triggers de auditoria e histórico foram removidas para simplificação.

---

## 📁 Scripts SQL separados
O projeto conta com arquivos separados para:
- Criação do banco (`create_studiomuda.sql`)
- Cadastro inicial de:
    - Produtos
    - Funcionários
    - Clientes
    - Pedidos
    - Itens de pedido
    - Movimentações de estoque

---

## 💡 Autor
Este projeto foi desenvolvido como parte da disciplina de Banco de Dados, da César School.

---

