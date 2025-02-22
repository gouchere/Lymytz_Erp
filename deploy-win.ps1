#!/bin/bash
# Variables
$DOMAIN_NAME = "domain1"
$ADMIN_PORT = 4848
$SERVER_HOST = "localhost"
$RETRY_COUNT = 5  # Nombre de tentatives pour vérifier si le serveur est démarré
$RETRY_INTERVAL = 5  # Intervalle de temps entre chaque tentative (en secondes)
$APP_VERSION = $args[0]  # Argument passé à la commande
$ADMIN_SERVER = "admin"
$ADMIN_PASSWORD = ".\glassfish-password"
$EAR_PATH = ".\Lymytz_Ear\target\lymytz-erp-$APP_VERSION.ear"

# Vérification si GLASSFISH_HOME est défini
if (-not $env:GLASSFISH_HOME) {
    Write-Host "GLASSFISH_HOME n'est pas défini. Arrêt du script."
    exit 1
}

# Vérification si l'argument APP_VERSION est fourni
if (-not $APP_VERSION) {
    Write-Host "Le numéro de version de l'application est manquant. Veuillez fournir la version comme argument."
    exit 1
}

Write-Host "Début du processus de déploiement..."

function is_server_running {
    try {
        $response = Invoke-WebRequest -Uri "http://${SERVER_HOST}:${ADMIN_PORT}" -Method Head -UseBasicP -ErrorAction Stop
        return $true
    } catch {
        return $false
    }
}

function start_domain {
    Write-Host "Démarrage du domaine GlassFish..."
    & "$env:GLASSFISH_HOME\bin\asadmin.bat" start-domain $DOMAIN_NAME

    # Attendre que le serveur démarre
    for ($i = 1; $i -le $RETRY_COUNT; $i++) {
        Write-Host "Vérification de l'état du serveur (tentative $i)..."
        if (is_server_running) {
            Write-Host "Le serveur est démarré."
            return $true
        }
        Start-Sleep -Seconds $RETRY_INTERVAL
    }

    Write-Host "Échec du démarrage du serveur."
    return $false
}

function ear_exist {
    if (Test-Path $EAR_PATH) {
        return $true
    } else {
        return $false
    }
}


if (is_server_running) {
    Write-Host "Le domaine GlassFish est déjà en cours d'exécution."
    Write-Host "Démontage de l'application..."
    & "$env:GLASSFISH_HOME\bin\asadmin.bat" --user $ADMIN_SERVER --port $ADMIN_PORT undeploy "lymytz-erp-$APP_VERSION"

    if (ear_exist) {
        Write-Host "Déploiement de l'application..."
        & "$env:GLASSFISH_HOME\bin\asadmin.bat" --user $ADMIN_SERVER --port $ADMIN_PORT deploy $EAR_PATH
    } else {
        Write-Host "Impossible de trouver l'archive à $EAR_PATH"
        exit 1
    }
} else {
    Write-Host "Démarrage du domaine GlassFish..."
    if (start_domain) {
        Write-Host "Déploiement de l'application..."
        & "$env:GLASSFISH_HOME\bin\asadmin.bat" --user $ADMIN_SERVER --port $ADMIN_PORT deploy $EAR_PATH
    } else {
        Write-Host "Échec du démarrage du domaine GlassFish. Arrêt du script."
        exit 1
    }
}
Get-Content -Path "$env:GLASSFISH_HOME\glassfish\domains\domain1\logs\server.log" -Wait
