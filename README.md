# TP : Génération Aléatoire d'Arbres Couvrants

**Auteurs :** Sasha Sutton

## Description
Ce projet implémente et compare plusieurs algorithmes de génération d'arbres couvrants aléatoires sur différents types de graphes (Grilles, Complets, Erdős-Rényi, Lollipop).

Le projet repart du squelette fourni (package `Graph`, `GraphClasses`) et ajoute les implémentations demandées.

## Algorithmes Implémentés (`src/RandomTreeAlgos/SpanningTreeAlgos.java`)

1.  **Kruskal Aléatoire** : Mélange les arêtes et utilise une structure Union-Find pour construire l'arbre sans cycle.
    * *Propriété* : Biaisé (privilégie les arbres de petit diamètre sur certains graphes).
2.  **Prim Aléatoire** : Part d'un sommet et étend l'arbre en choisissant un voisin aléatoire via une file de priorité.
    * *Propriété* : Biaisé.
3.  **Aldous-Broder** : Marche aléatoire simple. On garde la première arête qui permet de découvrir un sommet non visité.
    * *Propriété* : Génération **Unifome** (tous les arbres ont la même probabilité). Lent sur les gros graphes.
4.  **Wilson** : Marche aléatoire à effacement de boucles (Loop-Erased Random Walk).
    * *Propriété* : Génération **Unifome**. Plus rapide qu'Aldous-Broder.

## Modifications apportées au squelette

* **`Graph.java`** : Implémentation complète du constructeur et des méthodes `addEdge`/`addArc` pour gérer correctement les listes d'adjacence (`incidency`, `outIncidency`), nécessaires au fonctionnement des algorithmes.
* **`Main.java`** : Adaptation pour exécuter les 4 algorithmes à la suite et générer des statistiques comparatives.
* **`UnionFind.java`** : Ajout d'une classe utilitaire pour l'algorithme de Kruskal.

## Résultats et Comparaisons

Les statistiques calculées (Diamètre, Indice de Wiener, Excentricité) montrent que :
* Les algorithmes uniformes (Aldous-Broder, Wilson) produisent des arbres avec des propriétés structurelles très proches (mêmes moyennes).
* Les algorithmes biaisés (Kruskal, Prim) produisent des arbres souvent très différents (par exemple, Prim a tendance à faire des arbres plus compacts ou différents selon la topologie, tandis que Kruskal sur une grille peut créer des "longs couloirs").

## Compilation et Exécution

Utiliser le Makefile fourni :

```bash
make compile
make exec
```