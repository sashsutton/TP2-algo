# Projet Algorithmique 2 : Génération d'Arbres Couvrants Aléatoires

**Auteurs :** Sasha Sutton, Rayan Yousfi
**Cadre :** TP2 - Université Aix-Marseille

## Description du Projet
Ce projet explore la génération d'arbres couvrants aléatoires (Random Spanning Trees) sur des graphes, en particulier des grilles (labyrinthes). L'objectif est de comparer les propriétés structurelles (diamètre, indice de Wiener) et les performances temporelles de différents algorithmes.

Nous avons implémenté **4 algorithmes distincts**, choisis pour représenter différentes familles de générateurs (Biaisés vs Uniformes).

## Algorithmes Implémentés & Références

### 1. Kruskal Aléatoire (Famille : Glouton Biaisé)
* **Principe :** On attribue un poids aléatoire à chaque arête, puis on applique l'algorithme de Kruskal (tri des arêtes + Union-Find).
* **Propriété :** Cet algorithme favorise les arbres de faible diamètre (très branchus). Il est biaisé : tous les arbres n'ont pas la même probabilité d'apparition.
* **Source :** Basé sur les travaux de Joseph B. Kruskal, *"On the Shortest Spanning Subtree of a Graph and the Traveling Salesman Problem"*, Proceedings of the American Mathematical Society, 1956.

### 2. Random DFS (Famille : Parcours Aléatoire)
* **Remplacement de Prim :** L'algorithme de Prim étant statistiquement trop proche de Kruskal (tous deux gloutons sur des poids), nous avons choisi d'implémenter un **Parcours en Profondeur Aléatoire** (Randomized Depth-First Search) suggéré dans la section 3.2 du sujet ("Parcours aléatoire").
* **Principe :** On part d'un sommet et on plonge récursivement dans le graphe en visitant les voisins dans un ordre aléatoire (mélange de Fisher-Yates sur la liste d'adjacence).
* **Propriété :** Produit des arbres avec de très **longs chemins** (diamètre élevé) et peu de bifurcations ("labyrinthes serpents"). C'est l'opposé topologique de Kruskal.

### 3. Aldous-Broder (Famille : Uniforme)
* **Principe :** Simulation d'une marche aléatoire simple. On ajoute une arête à l'arbre si elle permet de visiter un sommet inconnu pour la première fois.
* **Propriété :** Génère des arbres selon une **distribution uniforme** (tous les arbres possibles sont équiprobables).
* **Inconvénient :** Lent à converger (problème du collectionneur de vignettes).
* [cite_start]**Source :** A. Broder, *"Generating random spanning trees"*, FOCS, 1989[cite: 80, 206].

### 4. Algorithme de Wilson (Famille : Uniforme Rapide)
* **Principe :** "Loop-Erased Random Walk" (Marche aléatoire à effacement de boucle). On construit l'arbre branche par branche en effaçant les cycles dès qu'ils se forment lors de la marche.
* **Propriété :** Génération **uniforme** comme Aldous-Broder, mais beaucoup plus efficace en pratique.
* [cite_start]**Source :** David B. Wilson, *"Generating random spanning trees more quickly than the cover time"*, STOC, 1996[cite: 96, 261].

## Résultats et Comparaison

Les tests effectués sur une grille 40x30 montrent des différences flagrantes :

| Algorithme | Type | Diamètre (Forme) | Indice de Wiener |
| :--- | :--- | :--- | :--- |
| **Kruskal** | Biaisé | Faible (Compact) | Moyen |
| **Random DFS** | Biaisé | **Très Élevé** (Longs couloirs) | **Très Élevé** |
| **Aldous-Broder** | Uniforme | Moyen | Moyen |
| **Wilson** | Uniforme | Moyen | Moyen |

*Note : Aldous-Broder et Wilson produisent statistiquement les mêmes résultats (aux variations près), confirmant leur propriété d'uniformité.*

## Instructions de Compilation

Utiliser le Makefile fourni à la racine :

```bash
make clean
make exec
```