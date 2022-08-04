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