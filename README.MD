#### Démarrer l'application

- clonez le projet avec la commande </br>
> ```git clone git@github.com:gouchere/Lymytz_Erp.git```

- Gérez la dépendance primefaces 3.5
- Modifier le fichiers settings.xml, ou créer un fichier maven settings.xml propre au projet
- Copier la librairie jdbc dans le dossier lib de glassfish
- modifier la version d'eclipse link
- Modifier la conf glassfish: </br>
  - La version 3.1.2 de glassfish utilise par defaut Jdk 1.7. Si ce n'est pas cette version qui est installé,
vous devez modifier le fichier de conf qui se trouve dans ```GLASSFISH_HOME/glassfish/config/osgi.properties``` et ajouter à la fin
une ligne ```jre-1.8=${jre-1.7},com.sun.tracing```
  - Modifier également le fichier ```GLASSFISH_HOME/glassfish/config/asenv.conf``` ou ```GLASSFISH_HOME/glassfish/config/asenv.bat``` selon que vous vous trouvez sur windows ou linux
modifier la variable **AS_JAVA** ```set AS_JAVA path_for_java_home```
- Créer les resources ```jdbc/lymytz-erp et lymytz-erp``` à partir de la console glassfish ou en ligne de commande avec l'outil **asadmin**

#### Ignorer les fichier
Les fichiers de configuration de votre dataSource et de l'Entitimanager doivent être rétiré de la zone de staging 
dans votre git loca. Faites le avec les commandes git
```
git rm Lymmytz_Ejb/src/conf/persistence.xml --cached  
git rm Lymmytz_Ejb/setup/glassfish-resources.xml --cached  
```
#### Migrer la version de eclipseLink du conteneur glassfish vers la 2.5.0