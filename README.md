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

## Résultats et Comparaisons

Nous avons testé les algorithmes sur quatre topologies de graphes différentes pour analyser leur comportement. Voici les observations basées sur les moyennes de 10 échantillons :

### 1. Sur une Grille (40x30 sommets) - *Labyrinthes*
C'est ici que les différences topologiques sont les plus flagrantes.
* **Aldous-Broder / Wilson (Uniformes) :** Produisent des labyrinthes équilibrés (Diamètre ~186, Wiener ~50M).
* **Kruskal (Biaisé) :** Produit des labyrinthes plus compacts, avec des chemins plus courts (Diamètre ~161).
* **Random DFS (Biaisé) :** Produit des labyrinthes "rivière" extrêmement tortueux (Diamètre ~648, Wiener ~160M). Il cherche la profondeur maximale.

### 2. Sur un Graphe Complet (20 sommets)
Tous les sommets sont reliés entre eux.
* **Random DFS :** Trouve quasi-systématiquement un **Chemin Hamiltonien** (une ligne unique passant par tous les sommets). Diamètre moyen de **19** pour 20 sommets.
* **Autres algos :** Produisent des arbres en étoile ou équilibrés avec un diamètre beaucoup plus petit (~9-11).

### 3. Sur une "Sucette" (Lollipop - 20 sommets)
Un graphe composé d'une clique (tête) et d'une tige.
* Le **Random DFS** a tendance à s'enfermer dans la clique ou à parcourir la tige linéairement, augmentant le diamètre (~15) par rapport aux algorithmes uniformes (~12).

### Conclusion Générale
Les résultats confirment les propriétés théoriques :
1.  **Wilson et Aldous-Broder** sont indiscernables statistiquement, validant leur propriété d'**uniformité**.
2.  **Random DFS** présente un biais fort vers la création de **chemins longs et profonds** (diamètre élevé), quelle que soit la topologie du graphe.
3.  **Kruskal** présente un biais inverse, favorisant des structures plus **compactes et branchues** (diamètre faible).


*Note : Aldous-Broder et Wilson produisent statistiquement les mêmes résultats (aux variations près), confirmant leur propriété d'uniformité.*

## Instructions de Compilation

Utiliser le Makefile fourni à la racine :

```bash
make clean
make exec
```
## Références Bibliographiques
Les algorithmes implémentés dans ce projet sont basés sur les sources suivantes:
**Aldous-Broder** :
    * Génération uniforme par marche aléatoire.
    * *Réf :* https://weblog.jamisbuck.org/2011/1/17/maze-generation-aldous-broder-algorithm

**Wilson** :
    * Génération uniforme par marche aléatoire à effacement de boucles (Loop-Erased Random Walk).
    * *Réf :* https://gist.github.com/mbostock/11357811