#!/bin/bash

echo "========================================"
echo "   SIGEAJ - Sistema de Gestão EAJ"
echo "========================================"
echo ""

echo "Verificando Maven..."
if ! command -v mvn &> /dev/null; then
    echo "[ERRO] Maven não encontrado!"
    echo "Por favor, instale o Maven primeiro."
    exit 1
fi

echo "Maven encontrado!"
echo ""

echo "Verificando Java..."
if ! command -v java &> /dev/null; then
    echo "[ERRO] Java não encontrado!"
    exit 1
fi

java -version
echo ""

echo "Compilando e executando o SIGEAJ..."
echo ""
echo "Use Ctrl+C para parar a aplicação"
echo "========================================"
echo ""

mvn clean javafx:run
