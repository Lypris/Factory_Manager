# ApplicationWebM3

# Projet M3 - BDD et Gestion d'un atelier

Voici le projet Github de VIRQUIN Rudy, TRENCHANT Evan et TROULLIER Laël pour le Semestre 3 d'Informatique. Ce projet réalisé à l'aide de JAVA, MySQL et Vaadin a pour but de réaliser un site web pour gérer l'ensemble de la chaîne de production d'un atelier de fabrication.

 - Diagramme UML et relationel: https://drive.google.com/file/d/1KDaZfZN__362PJQ8x0-6sh7QD8fNa-En/view?usp=sharing

## Installation

Ce projet est basé sur **Java JDK 21**

Pour actualiser le projet: `Team/Remote/Pull to Uptstream`
Pour sauvegarder les modifications sur le GitHub : `Team/commit` puis `Team/Remote/Push to Upstream` 

## TODO

- [] Définir le plan d'opération de chaque produit
- [] Définir els exemplaires avec la dernière opération subie 
- Localiser les machines sur un plan de l’entreprise
- une description des produits
- une photo des produits
- Détailler plus ou moins les types d’opération
- définir un modèle de machine : C’est le modèle de machine qui fixe les types d’opérations possibles. L’entreprise peut posséder plusieurs machines du même type.
- Définition d’objets individuels/ exemplaire : un produit correspond à un type d’objet : par exemple un modèle de téléphone. On parlera d’exemplaire pour faire référence à un téléphone particulier (par exemple celui que vous avez dans votre poche). Tout exemplaire correspond à un produit, et pourra par exemple posséder un numéro de série.
- Définition de la fabrication effective des objets: 
	- chaque exemplaire est produit par un ensemble d’opérations effectives : une opération effective correspond à une opération du plan de fabrication du produit, effectuée sur une machine donnée (qui est capable de réaliser le type d’opération) avec une date (timestamp) de début et une date de fin (qui peuvent donner une durée différente de la durée théorique définie au niveau de la machine).
	- On peut alors retrouver l’ensemble des exemplaires fabriqués par exemple entre deux dates, ou les exemplaires qui ont été fabriqués au moins en partie sur une machine donnée (si par exemple on s’aperçoit que la machine a un défaut).
- Les machines sont souvent regroupées par postes de travail. Il faudrait tenir compte du passage d’un poste de travail à un autre lorsque l’on calcule par exemple le temps de fabrication effective d’un produit en fonction des machines choisies pour le fabriquer.
- Gestion des qualifications des opérateurs pour les différents postes de travail.
- Etats possibles pour les machine. Chaque machine est associée à différents états possibles pour différentes plages horaire (avec par exemple un timestamp de début et un timestamp de fin avec la convention que le timestamp de fin est NULL pour un état courant sans connaissance de sa fin (panne...)).
- Horaires des opérateurs, congés, éventuellement des maladies... Comme pour les machines, on peut définir un ensemble d’états possibles, et des plages horaires.
- Représentation/Gestion des:
	- Stocks
	- Produits intermédiaires ou semi-finis
	- Produits bruts : en général, une opération va utiliser des produits bruts ou semi-fini pour produire des produits semi-fini ou un produit final.
- Algorithmes de statistiques:
	- Analyse de fiabilité des machines à partir de leur historique d'états.
	- Si l’on a également l’historique de production des exemplaires, on peut faire pas mal de statistiques sur le taux d’occupation des machines, éventuellement les relations entre la productions de certains produits et certaines pannes...
	- L’entreprise a un certain nombre d’exemplaires de différent produits à fabriquer durant une période donnée (par exemple un jour). On voudrait calculer un plan global de fabrication : quel opérateur, sur quel poste, utilise quelle machine pour faire quelle opération effective et à quel moment pour que ces exemplaires soient effectivement fabriqués.## Color Reference


## Auteurs
- [@Troullier Laël](https://github.com/ltroullier01)
- [@Evan Trenchant](https://github.com/EvanTrenchant)
- [@Rudy Virquin](https://github.com/Lypris)
