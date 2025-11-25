
Viewed
Original file line number	Original file line	Diff line number	Diff line change
@@ -0,0 +1,54 @@
@echo off
REM ============================
REM Script de deploiement GlassFish
REM ============================

REM --- Configurer les chemins ---
set DOMAIN_NAME=domain1
set NAME_APP=Lymytz_Erp
set EAR_PATH=%NAME_APP%.ear
set PASS_FILE=password.txt

echo Deploiement de l'application %NAME_APP%

rem Vérifie que la variable d'env pour AS_ADMIN_PWD est bien définie
if "%AS_ADMIN_PWD%"=="" (
    echo [ERREUR] La variable d'environnement AS_ADMIN_PWD n'est pas definie.
    echo          Exemple : set AS_ADMIN_PWD=monSuperMotDePasse
    exit /b 1
)

rem Fichier password temporaire
set "PASS_FILE=%TEMP%\asadmin_pwd_%RANDOM%.txt"

> "%PASS_FILE%" echo AS_ADMIN_PASSWORD=%AS_ADMIN_PWD%

REM --- Verifier si le domaine est demarre ---
echo.
echo ==============================================================
echo Verification de l'etat du domaine %DOMAIN_NAME%...
echo ==============================================================
asadmin --user admin --passwordfile %PASS_FILE% list-domains | findstr /i "%DOMAIN_NAME% running"
if errorlevel 1 (
    echo Demarrage du domaine...
    call asadmin --user admin --passwordfile %PASS_FILE% start-domain %DOMAIN_NAME%
) else (
    echo Domaine deja demarre.
)

REM --- Verifier si l'application est deployee ---
echo.
echo ==============================================================
echo Verification du deploiement de l'application : %NAME_APP%
echo ==============================================================
asadmin --user admin --passwordfile %PASS_FILE% list-applications | findstr /i "%NAME_APP%"
if errorlevel 0 (
    echo L'application %NAME_APP% est deja deployee.
    echo Suppression de l'ancienne version...
	call asadmin --user admin --passwordfile %PASS_FILE% undeploy %NAME_APP%
) else (
    echo L'application %NAME_APP% n'est pas encore deployee.
)

REM --- Deploiement de la nouvelle version et redemarrage du serveur ---
echo.
echo ==============================================================
echo Deploiement de la nouvelle version
echo ==============================================================
call asadmin --user admin --passwordfile %PASS_FILE% deploy --force=true %EAR_PATH%
if errorlevel 0 (
    echo Deploiement reussi !
	call asadmin --user admin --passwordfile %PASS_FILE% restart-domain %DOMAIN_NAME%
) else (
    echo Erreur lors du deploiement.
)
rem Nettoyage
echo Nettoyage
echo ==============================================================
del "%PWFILE%" >nul 2>&1
pause
