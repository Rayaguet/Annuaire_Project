# Présentation du Annuaire-Project

![vegabot](images/vegabot.jpg)

## Sommaire
- [I. Objectif](#ii-bot-discord)
  - [a. Objectif du projet](#a-commandes-simples)

- [II. Conception et SQL](#i-conception-et-sql)
  - [a. MCD](#a-mcd)
  - [b. MLD](#b-mld)
  - [c. MPD](#c-mpd)
  - [d. Structure de la base de données]

- [III. Réalisation](#i-conception-et-sql)
  - [a. Page de connexion](#a-mcd)
  - [b. Ajouer un utilisateur ou un administrateur](#b-mld)
  - [c. Mofier ses informations](#c-mpd)
  - [d. Rechercher une personne](#c-mpd)

## I. Note de cadrage

### a. Objectif du projet
Le but de ce projet est de vous faire développer un logiciel complet en Java disposant de trois couches :
une IHM Swing, une couche de Services réalisant les traitements et règles de gestion et une base de
données pour la persistance des données. Le logiciel effectue la gestion d’un Annuaire simplifié de
l’ESIEE en s’inspirant par exemple de pages blanches, sauf qu’ici il est implémenté en mode client lourd avec Swing et non pas web.

## II. Conception et SQL

### a. MCD
La conception du Modèle Conceptuel de Données (MCD) pour l'annuaire de l'école comprend les entités principales et leurs relations.

![MCD](Images_Readme/MCD.png)

### b. MLD
Le Modèle Logique de Données (MLD) traduit le MCD en un schéma logique adapté à un système de gestion de base de données relationnelle.

![MLD](Images_Readme/MLD.png)

### c. MPD
Le Modèle Physique de Données (MPD) détaille la structure de stockage des données sur le support physique.

![MPD](Images_Readme/MPD.png)

## III. Réalisation

### a. Page de connexion
La page de connexion permet à l'utilisateur ainsi qu'à l'administrateur de se connecter via leur e-mail et leur mot de passe.

![hey+who](images/hey_commande.png)

### b. Ajouer un utilisateur ou un administrateur
La page "Ajouter un utilisateur" permet d'ajouter une personne en tant qu'utilisateur de l'annuaire.

![vegabotquibouge](images/vegabotvideo.gif)

La page "Ajouter un utilisateur" permet d'ajouter une personne en tant qu'administrateur de l'annuaire.

![showslash](images/showslash.png)

### c. Mofier ses informations
L'utilisateur ainsi que l'administrateur peuvent modifier leurs informations quand ils le veulent. La différence étant que l'utilisateur devra renseigner son adresse
via son numéro de rue, le nom de la rue ainsi que son code postal alors que l'administrateur n'aura pas à le faire.

### d. Rechercher une personne
Cette page permet de rechercher une personne, qu'elle soit un utilisateur ou un administrateur via son nom, son prénom, son e-mail ainsi que son profil.

---
