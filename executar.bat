@echo off
echo ========================================
echo    SIGEAJ - Sistema de Gestao EAJ
echo ========================================
echo.

echo Verificando Maven...
where mvn >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Maven nao encontrado!
    echo Por favor, instale o Maven primeiro.
    pause
    exit /b 1
)

echo Maven encontrado!
echo.

echo Verificando Java...
java -version
if %ERRORLEVEL% NEQ 0 (
    echo [ERRO] Java nao encontrado!
    pause
    exit /b 1
)

echo.
echo Compilando e executando o SIGEAJ...
echo.
echo Use Ctrl+C para parar a aplicacao
echo ========================================
echo.

mvn clean javafx:run

pause
