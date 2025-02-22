#!/bin/bash
# Variables
DOMAIN_NAME="domain1"
ADMIN_PORT=4848
SERVER_HOST="localhost"
RETRY_COUNT=5  # Nombre de tentatives pour vérifier si le serveur est démarré
RETRY_INTERVAL=5  # Intervalle de temps entre chaque tentative (en secondes)
APP_VERSION=$1
ADMIN_SERVER="admin"
ADMIN_PASSWORD="./glassfish-password"
EAR_PATH="./Lymytz_Ear/target/lymytz-erp-$APP_VERSION.ear"

echo "begin process deployement in... $2"
echo "begin process deployement in $GLASSFISH_HOME"
echo "undeploy lymytz-erp-${APP_VERSION}"


is_server_running(){
  curl --silent --head --fail $SERVER_HOST:$ADMIN_PORT  || return 1
  return 0
}

start_domain(){
  echo "Démarrage du domain glassfish..."
  $GLASSFISH_HOME/bin/asadmin start-domain $DOMAIN_NAME

  #Attendre que le serveur demarre
  for i in $(seq 1 $RETRY_COUNT) ; do
      echo "Vérification de l'état du serveur (tentative $i)..."
      if is_server_running ;  then
        echo "Le serveur est demarré."
        return 0
      fi
      sleep $RETRY_INTERVAL
  done
  echo "Echec du demarrage du serveur"
  return 1
}
ear_exist(){
  ls $EAR_PATH &>/dev/null || return 1
  return 0
}
if is_server_running; then
  echo "le domaine glassfish est en cours d'exécution..."
  echo "undeploy..."
  $GLASSFISH_HOME/bin/asadmin --user $ADMIN_SERVER --passwordfile $ADMIN_PASSWORD --port $ADMIN_PORT undeploy lymytz-erp-${APP_VERSION}
  if ear_exist; then
    echo "begin deployement deploy..."
    $GLASSFISH_HOME/bin/asadmin --user $ADMIN_SERVER --passwordfile $ADMIN_PASSWORD --port $ADMIN_PORT deploy $EAR_PATH
  else
    echo "unable to find archive in $EAR_PATH"
  fi
else
  echo "lancement du domain glassfish"
  start_domain
fi
tail -f $GLASSFISH_HOME/glassfish/domains/domain1/logs/server.log
#