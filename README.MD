####Prerequis pour le lancement de la solution

clonez le projet avec la commande  
git clone git@github.com:gouchere/Lymytz_Erp.git

#Completer la configuration  

Les d�pendance du projet sont g�r� avec Ant. il faut donc les mettre en place manuellement. Pour cela, 
vous devez:  
1. Copier le contenu du dossier "ejb nbproject" dans Lymytz_Ejb/nbproject  
2. Copier le contenu du dossier "web nbproject" dans Lymytz_Web/nbproject

####D�pendances
  Toutes les d�pendances du projet se trouve dans le dossier /librairies du pr�sent repository.  


####Ignorer les fichier+++  
Les fichiers de configuration de votre dataSource et de l'Entitimanager doivent �tre r�tir� de la zone de staging 
dans votre git local; ex�cut� les commandes suivante pour cel�.

git rm Lymmytz_Ejb/src/conf/persistence.xml --cached  
git rm Lymmytz_Ejb/setup/glassfish-resources.xml --cached  

-- Copier la librairie jdbc dans le dossier lib de glassfish
-- modifier la version d'eclipse link
-- d�marrer glassfish lorsque JDK1.7 n'est pas votre jdk par d�faut
Si la version par d�faut de jdk sur votre poste n'est pas la 1.7, vous devez alors modifier la configuration de glassfish
	1. Commencer par instaler une version compatible de jdk 1.7
	2. Modifier le fichier osgi.properties qui se trouve g�n�ralement dans le dossier C:\glassfish3\glassfish\config
		i. trouver la ligne **jre-1.8=${jre-1.7},com.sun.tracing** et remplacez l� par **jre-1.7=${jre-1.6},com.sun.tracing**
		ii. Ajouter � la fin du fichier la ligne   **jre-1.8=${jre-1.7}**
	3. Modifier le fichier asenv.bat qui set trouve g�n�ralement dans le dossier 
		modifier la variable **AS_JAVA** ainsi **set AS_JAVA=C:\Program Files\Java\jdk1.7.0_25**
		
-- Cr�er les ressources jdbc/lymytz-erp et lymytz_erp