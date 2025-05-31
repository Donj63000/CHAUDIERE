# Instructions à destination de l'agent dev

## Contexte du projet
Ce dépôt contient un petit logiciel de saisie de données utilisé pour les contrôles de chaudières industrielles en usine. L'application est écrite en Java 21 et repose sur JavaFX pour l'interface graphique. Les opérateurs saisissent différentes valeurs (niveau d'eau, pression, température, etc.) au moyen de champs dynamiques. Les données sont enregistrées au format JSON dans le fichier `inspections.json`.

## Rôle de l'agent
L'agent assigné à ce dépôt est un agent de développement (« dev »). Il a pour mission de maintenir et d'améliorer ce logiciel de saisie. Les nouvelles fonctionnalités ou corrections doivent respecter la structure existante et faciliter le travail des opérateurs en usine.

## Directives de travail
- Les messages de commit doivent être courts et en français.
- Aucune suite de tests n'est fournie ; il n'est donc pas nécessaire d'exécuter de commandes de test. Un build Maven (`mvn package`) peut être lancé pour vérifier la compilation si l'outil est disponible.
- Pour ajouter un champ ou modifier l'interface, éditer la liste `defs` dans `ChecklistController` et les fabriques correspondantes dans `FieldFactory`.
- La persistance des check-lists est gérée par `ChecklistStorage`.

## Pull requests
Chaque PR doit inclure un résumé en français des modifications apportées.
