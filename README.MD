####Prerequis pour le lancement de la solution

clonez le projet avec la commande  
git clone git@github.com:gouchere/Lymytz_Erp.git

#Completer la configuration  

Les dépendance du projet sont géré avec Ant. il faut donc les mettre en place manuellement. Pour cela, 
vous devez:  
1. Copier le contenu du dossier "ejb nbproject" dans Lymytz_Ejb/nbproject  
2. Copier le contenu du dossier "web nbproject" dans Lymytz_Web/nbproject

####Dépendances
  Toutes les dépendances du projet se trouve dans le dossier /librairies du présent repository.  


####Ignorer les fichier+++  
Les fichiers de configuration de votre dataSource et de l'Entitimanager doivent être rétiré de la zone de staging 
dans votre git local; exécuté les commandes suivante pour celà.

git rm Lymmytz_Ejb/src/conf/persistence.xml --cached  
git rm Lymmytz_Ejb/setup/glassfish-resources.xml --cached  

-- Copier la librairie jdbc dans le dossier lib de glassfish
-- modifier la version d'eclipse link
-- démarrer glassfish lorsque JDK1.7 n'est pas votre jdk par défaut
Si la version par défaut de jdk sur votre poste n'est pas la 1.7, vous devez alors modifier la configuration de glassfish
	1. Commencer par instaler une version compatible de jdk 1.7
	2. Modifier le fichier osgi.properties qui se trouve généralement dans le dossier C:\glassfish3\glassfish\config
		i. trouver la ligne **jre-1.8=${jre-1.7},com.sun.tracing** et remplacez là par **jre-1.7=${jre-1.6},com.sun.tracing**
		ii. Ajouter à la fin du fichier la ligne   **jre-1.8=${jre-1.7}**
	3. Modifier le fichier asenv.bat qui set trouve généralement dans le dossier 
		modifier la variable **AS_JAVA** ainsi **set AS_JAVA=C:\Program Files\Java\jdk1.7.0_25**
		
-- Créer les ressources jdbc/lymytz-erp et lymytz_erp