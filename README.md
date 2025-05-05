# 🌿 Studio Muda - Sistema de Gerenciamento de Estoque

Sistema web desenvolvido para **Studio Muda LTDA**, empresa especializada em paisagismo e comercialização de produtos naturais. Esta aplicação permite o controle completo de produtos, pedidos, clientes, funcionários e todas as movimentações de estoque em uma interface web moderna e intuitiva.

## 📋 Visão Geral

O sistema implementa uma solução completa de gerenciamento empresarial com as seguintes características:

- **Arquitetura**: Aplicação web Java com Spring Boot
- **Front-end**: Thymeleaf, Bootstrap, CSS personalizado, JavaScript
- **Back-end**: Java 11, Spring (Web, Security, MVC)
- **Banco de Dados**: MySQL com acesso via JDBC puro (sem ORM)
- **Autenticação**: Sistema de login seguro com Spring Security

---

## ✅ Módulos Implementados

[Conceitual - StudioMuda.brM3 | Logo - StudioMuda.brM3 | MiniMundo - StudioMuda | Relatório versão 1.0 - StudioMuda.pdf](https://drive.google.com/drive/folders/1qwyT-uzrC67BZYqoUOrW95nYa3ip0eBV)

### 1. 📦 Produtos
- CRUD completo: cadastrar, listar, atualizar e deletar produtos.
- Campos: `nome`, `descricao`, `tipo`, `valor`, `quantidade`.
- Validações:
    - `nome`, `tipo` e `valor` são obrigatórios.
    - Tipos definidos via menu: Adubo, Plantas, Vasos, etc.
    - Quantidade é gerenciada exclusivamente pelo módulo de estoque.
    - Valor exibido com 2 casas decimais.
- Interface web intuitiva para gerenciamento de produtos
- Visualização em tabelas com ordenação e busca rápida

---

### 2. 👤 Funcionários
- Cadastro com validação de CPF (11 dígitos, com ou sem pontuação).
- Campos: `nome`, `cpf`, `cargo`, `data_nasc`, `telefone`, `endereco` (com CEP, rua, número, bairro, cidade e estado), `ativo`.
- Cargos disponíveis: Diretor, Auxiliar, Estoquista.
- Exclusão lógica: `ativo = false` (mantém histórico).
- Listagem separada de funcionários ativos e inativos.
- Sistema de auditoria que registra todas as alterações em funcionários via triggers.
- Formulários web com validação em tempo real.
- Formatação automática de CPF e telefone no frontend.

---

### 3. 🧍 Clientes
- Cadastro com validação de CPF (11 dígitos) e CNPJ (14 dígitos).
- Tipo PF/PJ definido automaticamente pela quantidade de dígitos.
- Campos: `nome`, `cpf_cnpj`, `telefone`, `email`, `endereco` (com CEP, rua, número, bairro, cidade e estado), `tipo`, `ativo`.
- Exclusão lógica (`ativo = false`) com listagens separadas para gerenciamento eficiente.
- Visualização de top clientes no dashboard principal.
- Sistema de auditoria que registra todas as alterações de dados via triggers.
- Validação completa de dados via frontend e backend.
- CPF/CNPJ e telefone formatados automaticamente conforme padrão nacional.

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
- Todas as tabelas criadas via `setup_database.sql`.
- Estrutura do banco:
  - **produto**: Catálogo completo de itens
  - **funcionario**: Cadastro da equipe com histórico
  - **cliente**: Base de clientes PF/PJ
  - **pedido**: Registro de vendas
  - **item_pedido**: Relação n:n entre pedidos e produtos
  - **cupom**: Sistema promocional
  - **movimentacao_estoque**: Controle de entrada/saída
  - **historico_estoque**: Auditoria de alterações em produtos
  - **historico_funcionario**: Auditoria de dados de funcionários
  - **historico_cliente**: Registro de atualizações de clientes
- Triggers de auditoria implementados para rastreamento de alterações

---

## 🚀 Tecnologias Utilizadas

- **Java 11**: Linguagem de programação principal
- **Spring Boot 2.7.9**: Framework para aplicações web
- **MySQL 8.0**: Sistema gerenciador de banco de dados
- **Thymeleaf**: Engine de templates para front-end
- **Bootstrap**: Framework CSS para interface responsiva
- **Spring Security**: Controle de autenticação e autorização
- **JDBC**: Conexão nativa com banco de dados (sem ORM)
- **Padrão DAO**: Data Access Objects para operações de banco
- **Maven**: Gerenciamento de dependências e build

## 🔧 Instalação e Configuração

### Pré-requisitos
- JDK 11
- MySQL 8.0
- Maven

### Passos para Instalação

1. **Clone o repositório**
   ```bash
   git clone https://github.com/seu-usuario/EstoqueStudioMudaBD.git
   cd EstoqueStudioMudaBD
   ```

2. **Configure o banco de dados**
   ```bash
   mysql -u root -p < setup_database.sql
   ```

3. **Verifique as configurações em application.properties**
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/studiomuda
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ```

4. **Compile e execute o projeto**
   ```bash
   mvn clean package
   java -jar target/estoque-0.0.1-SNAPSHOT.jar
   ```

5. **Acesse a aplicação**
   ```
   http://localhost:8081
   ```

## 📊 Dashboard e Relatórios

A aplicação possui um dashboard que exibe:
- Top 10 produtos mais vendidos
- Top 10 clientes com mais pedidos
- Visualização rápida do estado atual do negócio

## 🔒 Segurança

O sistema implementa:
- Autenticação baseada em Spring Security
- Senhas criptografadas no banco de dados
- Validação de dados em todos os formulários
- Proteção contra SQL Injection via PreparedStatements

## 💡 Autores
Este projeto foi desenvolvido como parte da disciplina de Banco de Dados.

---

## 📞 Suporte

Para suporte técnico ou dúvidas sobre o sistema, entre em contato com a equipe de desenvolvimento.

