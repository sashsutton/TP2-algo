# Projet Algorithmique 2 : Génération de Labyrinthes et Arbres Couvrants Aléatoires

**Auteurs :** Sasha Sutton, Rayan Yousfi  
**Cadre :** TP2 - Université Aix-Marseille

## 1. Introduction et Objectifs
Ce projet a pour objectif la mise en œuvre, la visualisation et la comparaison d'algorithmes de génération de labyrinthes parfaits (Arbres Couvrants Aléatoires). Il explore comment différentes approches issues de la **théorie des graphes** transforment une grille vide en structures complexes, et analyse les propriétés structurelles (diamètre, indice de Wiener) ainsi que les performances temporelles de chaque méthode.

Nous avons implémenté **4 algorithmes distincts**, choisis pour représenter différentes familles de générateurs (Biaisés vs Uniformes).

---

## 2. Cadre Théorique

### 2.1 Le Labyrinthe comme Graphe
Mathématiquement, la grille est modélisée comme un graphe non orienté $G = (V, E)$ :
* **$V$ (Sommets)** : L'ensemble des cellules de la grille.
* **$E$ (Arêtes)** : Les connexions potentielles entre cellules adjacentes.

Un labyrinthe parfait est défini comme un **Arbre Couvrant (Spanning Tree)** du graphe $G$, respectant deux propriétés fondamentales :
1.  **Connexité :** Tout point du labyrinthe est accessible (pas de zones isolées).
2.  **Acyclicité :** Le labyrinthe ne contient aucune boucle fermée.

---

## 3. Recherche et Méthodologie : Analyse des Algorithmes

### 3.1 Random DFS (Recursive Backtracker)
*Famille : Parcours Aléatoire Glouton*

**Fonctionnement :**
1.  On choisit une cellule de départ et on l'ajoute à une **Pile (Stack)**.
2.  Tant que la pile n'est pas vide :
    * On regarde la cellule actuelle.
    * On choisit aléatoirement un voisin **non visité** (mélange de Fisher-Yates sur la liste d'adjacence).
    * Si un voisin existe : on brise le mur, on avance vers ce voisin et on l'empile.
    * Si aucun voisin n'est libre (cul-de-sac) : on dépile (on recule) jusqu'à trouver une cellule avec des voisins libres.

**Signature Topologique :**
* **River Factor élevé :** Produit de longs corridors sinueux avec peu d'embranchements.
* **Biais :** L'algorithme favorise les longs chemins exploratoires (profondeur maximale).
* **Diamètre :** Extrêmement élevé ("Labyrinthes serpents").

**Implémentation Technique :**
* Utilise une simple liste utilisée comme Pile (LIFO).
* Alternative à l'algorithme de Prim (qui est statistiquement trop proche de Kruskal).

### 3.2 Randomized Kruskal
*Famille : Glouton Biaisé*

**Fonctionnement :**
1.  Chaque cellule commence dans son propre ensemble unique.
2.  On liste tous les murs possibles et on les mélange aléatoirement (poids aléatoires sur les arêtes).
3.  Pour chaque mur de la liste :
    * Si les deux cellules séparées par le mur sont dans des ensembles différents : on brise le mur et on **fusionne** (Union) les deux ensembles.
    * Sinon : on ignore le mur (car cela créerait une boucle).

**Signature Topologique :**
* Beaucoup de petits culs-de-sac courts.
* Aspect très "haché" ou bruité ("High branching factor").
* **Diamètre :** Faible (Labyrinthes compacts).

**Implémentation Technique :**
* Basé sur les travaux de Joseph B. Kruskal (1956).
* Utilise une classe `DisjointSet` (Union-Find) optimisée pour la gestion des ensembles.

### 3.3 Algorithme d'Aldous-Broder
*Famille : Uniforme (Marche Aléatoire Simple)*

**Fonctionnement :**
1.  On choisit une cellule au hasard.
2.  On effectue une marche aléatoire (Random Walk) vers une cellule voisine.
3.  Si la voisine n'a **jamais été visitée**, on brise le mur entre les deux et on la marque comme visitée.
4.  Si la voisine a déjà été visitée, on s'y déplace simplement sans rien modifier.
5.  On répète jusqu'à ce que toutes les cellules aient été visitées.

**Signature Topologique :**
* **Uniformité :** Génère des arbres selon une distribution uniforme (tous les arbres possibles sont équiprobables).
* Labyrinthes "équilibrés", ni trop longs ni trop branchus.

**Problème :**
* Extrêmement inefficace en fin de génération (Problème du collectionneur de vignettes).

### 3.4 Algorithme de Wilson
*Famille : Uniforme Rapide (Loop-Erased Random Walk)*

**Fonctionnement (LERW) :**
1.  **Initialisation :** On choisit une cellule arbitraire (l'ancre) et on l'ajoute à l'arbre.
2.  **Marche :** On choisit une cellule non visitée qui entame une marche aléatoire.
3.  **Effacement :** Si la marche repasse sur elle-même, la boucle (cycle) est immédiatement effacée.
4.  **Connexion :** Dès que la marche touche l'arbre existant, le chemin est solidifié.

**Signature Topologique :**
* Comme Aldous-Broder, il génère des labyrinthes **uniformes** (UST).
* Indiscernable statistiquement d'Aldous-Broder.

**Implémentation Technique :**
* Utilise un dictionnaire pour le "Loop-Erasure" rapide.
* Beaucoup plus rapide qu'Aldous-Broder sur les grandes grilles car on ne "piétine" pas inutilement l'arbre déjà construit.

---

## 4. Implémentation Technique Globale

### 4.1 Gestion de l'Aléatoire
Pour éviter les biais directionnels (ex: toujours aller au Nord en premier) dans le **Random DFS** et **Kruskal** :
* L'ordre des voisins ou la liste des murs n'est jamais fixe.
* On utilise `Collections.shuffle` (algorithme de Fisher-Yates) pour mélanger les possibilités avant de faire un choix.

### 4.2 Structures de Données
* **Random DFS :** `Stack` / `LinkedList`.
* **Kruskal :** `Union-Find` avec compression de chemin et union par rang.
* **Aldous-Broder :** `Set` ou compteur de cellules visitées.
* **Wilson :** `Map` pour suivre le chemin courant et détecter les boucles en $O(1)$.

---

## 5. Analyse des Résultats

Nous avons testé les algorithmes sur quatre topologies de graphes différentes pour analyser leur comportement. Voici les observations basées sur les moyennes de 10 échantillons :

### 5.1 Tableau Comparatif des Résultats (Moyenne sur 10 échantillons)

| Topologie | Algorithme | Diamètre Moyen | Indice de Wiener Moyen | Excentricité Moyenne |
| :--- | :--- | :--- | :--- | :--- |
| **Graphe Complet** | Kruskal | 16 | 8,534 | 4.60 |
| | Random DFS | **49** | 20,825 | 12.50 |
| | Aldous-Broder | 19 | 9,414 | 5.00 |
| | Wilson | 18 | 9,135 | 4.92 |
| **Erdos-Renyi** | Kruskal | 16 | 8,494 | 4.42 |
| | Random DFS | 25 | 11,611 | 6.74 |
| | Aldous-Broder | 18 | 8,961 | 4.81 |
| | Wilson | 17 | 8,869 | 4.74 |
| **Lollipop** | Kruskal | 27 | 11,878 | 7.76 |
| | Random DFS | 38 | 17,365 | 9.67 |
| | Aldous-Broder | 27 | 12,391 | 7.68 |
| | Wilson | 27 | 11,820 | 7.26 |
| **Grille (40x30)** | Kruskal | 161 | 45,779,850 | 43.13 |
| | Random DFS | **614** | 152,593,142 | 156.26 |
| | Aldous-Broder | 177 | 48,677,700 | 46.06 |
| | Wilson | 172 | 47,337,360 | 44.47 |

### 5.2 Interprétation et Conclusion

L'analyse quantitative des résultats ci-dessus nous permet de tirer plusieurs conclusions majeures sur la nature des algorithmes implémentés :

1.  **Le Biais "Serpent" du Random DFS :**
    *   Sur la topologie **Grille**, le Random DFS produit un diamètre moyen spectaculaire de **614**, contre environ 175 pour les algorithmes uniformes. 
    *   Cela confirme que le Random DFS génère des labyrinthes avec de très longs couloirs et peu d'embranchements (labyrinthes difficiles à résoudre pour un humain mais faciles à générer). L'indice de Wiener (somme des distances) est également 3 fois supérieur aux autres, indiquant une structure très étirée.

2.  **L'Uniformité (Aldous-Broder & Wilson) :**
    *   Les résultats de **Aldous-Broder** et **Wilson** sont statistiquement très proches sur toutes les topologies (ex: Diamètre Grille 177 vs 172, Complet 19 vs 18).
    *   Cela valide empiriquement qu'ils échantillonnent la même distribution uniforme d'arbres couvrants (UST), conformément à la théorie.

3.  **La Compacité de Kruskal :**
    *   Kruskal a tendance à produire les diamètres les plus faibles (161 sur la grille, 16 sur complet).
    *   Son approche "fusion de composantes" favorise la création d'arbres très branchus mais moins profonds, ce qui en fait un "mauvais" générateur de labyrinthes difficiles si l'on cherche la complexité du chemin.

4.  **Influence de la Topologie :**
    *   Sur un **Graphe Complet**, le Random DFS arrive parfois à créer des lignes quasi-droites (Diamètre ~50 sur 50 nœuds potentiels), se comportant presque comme une recherche de chemin Hamiltonien.
    *   Sur la **Lollipop**, la différence s'estompe légèrement mais le biais DFS reste visible.

**En conclusion**, si l'objectif est de générer un **labyrinthe impartial et complexe** de manière authentique, **Wilson** est le meilleur candidat (aussi uniforme qu'Aldous-Broder mais plus efficace). Si l'on souhaite des labyrinthes **visuellement tortueux** avec de longs corridors, le **Random DFS** est le plus adapté malgré son biais.

---

## 6. Instructions de Compilation

Utiliser le Makefile fourni à la racine :

```bash
make clean
make exec
```

---

## 7. Références Bibliographiques

**Aldous-Broder :**
* A. Broder, *"Generating random spanning trees"*, FOCS, 1989.
* [Explication détaillée](https://weblog.jamisbuck.org/2011/1/17/maze-generation-aldous-broder-algorithm)

**Wilson :**
* David B. Wilson, *"Generating random spanning trees more quickly than the cover time"*, STOC, 1996.
* [Explication et Implémentation](https://gist.github.com/mbostock/11357811)

**Kruskal :**
* Joseph B. Kruskal, *"On the Shortest Spanning Subtree of a Graph and the Traveling Salesman Problem"*, 1956.

**Randomisation :**
* [Java Collections.shuffle](https://www.baeldung.com/java-shuffle-collection)