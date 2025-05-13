#!/bin/bash
# Script para verificar e corrigir problemas com a aplicação Spring Boot

echo "===== VERIFICAÇÃO DE ERROS E SOLUÇÃO DO WHITELABEL ERROR ====="
echo

# 1. Verificar se o banco de dados está acessível
echo "1. Verificando conexão com o banco de dados..."
mysql -u root -e "SHOW DATABASES;" 2>/dev/null

if [ $? -eq 0 ]; then
  echo "✓ Conexão com o banco de dados MySQL OK!"
else
  echo "✗ Problema na conexão com o banco de dados. Verifique suas credenciais."
  echo "  Tente executar: mysql -u seu_usuario -p"
fi

# 2. Verificar configurações do application.properties
echo
echo "2. Verificando configurações do application.properties..."
APP_PROPERTIES_PATH="/home/temp/Área de trabalho/studiomudaofc/EstoqueStudioMudaBD/src/main/resources/application.properties"

if [ -f "$APP_PROPERTIES_PATH" ]; then
  echo "✓ Arquivo application.properties encontrado."
  
  # Exibir as configurações de banco de dados
  echo "  Configurações atuais de banco de dados:"
  grep -E "spring.datasource.(url|username|password)" "$APP_PROPERTIES_PATH" | sed 's/^/    /'
else
  echo "✗ Arquivo application.properties não encontrado!"
fi

# 3. Verificar se as tabelas existem no banco
echo
echo "3. Verificando se as tabelas necessárias existem no banco de dados..."
TABLES=$(mysql -u root -e "USE studiomuda; SHOW TABLES;" 2>/dev/null)

if [ $? -eq 0 ]; then
  echo "✓ Conseguiu acessar as tabelas do banco de dados."
  echo "  Tabelas encontradas:"
  echo "$TABLES" | grep -v "Tables_in_" | sed 's/^/    /'
  
  # Verificar especificamente as tabelas utilizadas pelas novas funcionalidades
  CUPOM_TABLE=$(mysql -u root -e "USE studiomuda; DESC cupom;" 2>/dev/null)
  if [ $? -eq 0 ]; then
    echo "    ✓ Tabela cupom está correta."
  else 
    echo "    ✗ Tabela cupom não encontrada ou com problemas!"
  fi
  
  PEDIDO_TABLE=$(mysql -u root -e "USE studiomuda; DESC pedido;" 2>/dev/null)
  if [ $? -eq 0 ]; then
    echo "    ✓ Tabela pedido está correta."
    
    # Verificar se os novos campos existem na tabela pedido
    CAMPOS_NOVOS=$(mysql -u root -e "USE studiomuda; SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'studiomuda' AND TABLE_NAME = 'pedido' AND COLUMN_NAME IN ('funcionario_id', 'cupom_id', 'valor_desconto');" 2>/dev/null)
    
    if [[ "$CAMPOS_NOVOS" == *"funcionario_id"* && "$CAMPOS_NOVOS" == *"cupom_id"* && "$CAMPOS_NOVOS" == *"valor_desconto"* ]]; then
      echo "    ✓ Campos novos na tabela pedido estão todos presentes."
    else
      echo "    ✗ Alguns campos novos não foram encontrados na tabela pedido!"
      echo "      Execute o script setup_database.sql para recriar o banco."
    fi
  else 
    echo "    ✗ Tabela pedido não encontrada ou com problemas!"
  fi
else
  echo "✗ Não foi possível verificar as tabelas. Verifique suas credenciais de banco de dados."
fi

# 4. Sugestão para resolver o problema Whitelabel
echo 
echo "4. Recomendações para resolver o erro Whitelabel:"
echo "  • Reinicie a aplicação para aplicar as alterações do banco de dados:"
echo "    cd \"/home/temp/Área de trabalho/studiomudaofc/EstoqueStudioMudaBD\""
echo "    mvn spring-boot:run"
echo ""
echo "  • Se persistir, verifique os logs da aplicação:"
echo "    tail -n 50 \"/home/temp/Área de trabalho/studiomudaofc/EstoqueStudioMudaBD/logs/application.log\" (se existir)"
echo ""
echo "  • Certifique-se que o script setup_database.sql foi executado:"
echo "    mysql -u seu_usuario -p < \"/home/temp/Área de trabalho/studiomudaofc/EstoqueStudioMudaBD/setup_database.sql\""
echo ""
echo "  • Depois aplique os dados de teste:"
echo "    mysql -u seu_usuario -p < \"/home/temp/Área de trabalho/studiomudaofc/EstoqueStudioMudaBD/dados_teste.sql\""
echo ""
echo "  • Se precisar testar as funcionalidades específicas:"
echo "    mysql -u seu_usuario -p < \"/home/temp/Área de trabalho/studiomudaofc/EstoqueStudioMudaBD/teste_funcionalidades.sql\""

# 5. Orientação sobre como executar e testar a aplicação
echo
echo "===== GUIA RÁPIDO DE EXECUÇÃO E TESTE ====="
echo "1. Recriar banco de dados: mysql -u seu_usuario -p < setup_database.sql"
echo "2. Inserir dados de teste: mysql -u seu_usuario -p < dados_teste.sql"
echo "3. Iniciar a aplicação: mvn spring-boot:run"
echo "4. Acessar a aplicação: http://localhost:8081"
echo "5. Testar as funcionalidades:"
echo "   - Criação de pedidos com cupom"
echo "   - Seleção de funcionário nos pedidos"
echo "   - Tentativa de adicionar mais itens que o estoque disponível"
echo "6. Verificar o dashboard para visualizar as vendas por funcionário"
echo "===== FIM DO GUIA ====="
