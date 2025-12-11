# Rapport Technique : Comparaison et Visualisation d'Algorithmes de Labyrinthes

## 1. Introduction
Ce projet a pour objectif la mise en œuvre, la visualisation et la comparaison d'algorithmes de génération de labyrinthes parfaits. Il explore comment différentes approches issues de la **théorie des graphes** transforment une grille vide en structures complexes.

Le projet implémente quatre algorithmes spécifiques :
1.  **Random DFS** (Recursive Backtracker)
2.  **Randomized Kruskal**
3.  **Algorithme d'Aldous-Broder**
4.  **Algorithme de Wilson**

## 2. Cadre Théorique

### 2.1 Le Labyrinthe comme Graphe
Mathématiquement, la grille est modélisée comme un graphe non orienté $G = (V, E)$ :
* **$V$ (Sommets)** : L'ensemble des cellules de la grille.
* **$E$ (Arêtes)** : Les connexions potentielles entre cellules adjacentes.

Un labyrinthe parfait est défini comme un **Arbre Couvrant (Spanning Tree)** du graphe $G$, respectant deux propriétés :
1.  **Connexité :** Tout point du labyrinthe est accessible (pas de zones isolées).
2.  **Acyclicité :** Le labyrinthe ne contient aucune boucle fermée.

---

## 3. Analyse Détaillée des Algorithmes

### 3.1 Random DFS (Recursive Backtracker)
C'est l'approche la plus intuitive, basée sur la recherche en profondeur.

* **Fonctionnement :**
    1.  On choisit une cellule de départ et on l'ajoute à une **Pile (Stack)**.
    2.  Tant que la pile n'est pas vide :
        * On regarde la cellule actuelle.
        * On choisit aléatoirement un voisin **non visité**.
        * Si un voisin existe : on brise le mur, on avance vers ce voisin et on l'empile.
        * Si aucun voisin n'est libre (cul-de-sac) : on dépile (on recule) jusqu'à trouver une cellule avec des voisins libres.
* **Signature Topologique :**
    * **River Factor élevé :** Produit de longs corridors sinueux avec peu d'embranchements.
    * **Biais :** L'algorithme favorise les longs chemins exploratoires.

### 3.2 Randomized Kruskal
Approche basée sur la fusion d'ensembles disjoints.

* **Fonctionnement :**
    1.  Chaque cellule commence dans son propre ensemble unique.
    2.  On liste tous les murs possibles et on les mélange aléatoirement.
    3.  Pour chaque mur de la liste :
        * Si les deux cellules séparées par le mur sont dans des ensembles différents : on brise le mur et on **fusionne** (Union) les deux ensembles.
        * Sinon : on ignore le mur (car cela créerait une boucle).
* **Signature Topologique :** Beaucoup de petits culs-de-sac courts. Aspect très "haché" ou bruité ("High branching factor").

### 3.3 Algorithme d'Aldous-Broder
C'est l'un des deux algorithmes de ce projet générant un **Arbre Couvrant Uniforme (UST)**.

* **Fonctionnement :**
    1.  On choisit une cellule au hasard.
    2.  On effectue une marche aléatoire (Random Walk) vers une cellule voisine.
    3.  Si la voisine n'a **jamais été visitée**, on brise le mur entre les deux et on la marque comme visitée.
    4.  Si la voisine a déjà été visitée, on s'y déplace simplement sans rien modifier.
    5.  On répète jusqu'à ce que toutes les cellules aient été visitées.
* **Caractéristique :** Comme Wilson, il génère des labyrinthes impartiaux. Cependant, il est **extrêmement inefficace** en fin de génération (Problème du collectionneur de vignettes) car le marcheur perd beaucoup de temps à errer dans la partie déjà construite.

### 3.4 Algorithme de Wilson
L'autre algorithme générant un **Arbre Couvrant Uniforme (UST)**, mais de manière beaucoup plus efficace qu'Aldous-Broder.

* **Fonctionnement (LERW) :**
    1.  **Initialisation :** On choisit une cellule arbitraire (l'ancre) et on l'ajoute à l'arbre.
    2.  **Marche :** On choisit une cellule non visitée qui entame une marche aléatoire.
    3.  **Effacement :** Si la marche repasse sur elle-même, la boucle est immédiatement effacée.
    4.  **Connexion :** Dès que la marche touche l'arbre existant, le chemin est solidifié.
* **Propriété Unique :** Génère exactement la même distribution statistique de labyrinthes qu'Aldous-Broder (UST), mais beaucoup plus rapidement sur les grandes grilles.

---

## 4. Implémentation Technique

### 5.1 Gestion de l'Aléatoire (`Collections.shuffle`)
Pour éviter les biais directionnels dans le **Random DFS** et **Kruskal** :
* L'ordre des voisins ou la liste des murs n'est jamais fixe.
* On utilise `random.shuffle` (algorithme de Fisher-Yates) pour mélanger les possibilités avant de faire un choix.


### 5.2 Structures de Données Spécifiques
* **Random DFS :** Utilise une simple liste comme Pile (LIFO).
* **Kruskal :** Implémente une classe `DisjointSet` (Union-Find) optimisée.
* **Aldous-Broder :** Utilise un simple compteur ou un set pour savoir combien de cellules restent à visiter.
* **Wilson :** Utilise un dictionnaire pour le "Loop-Erasure" rapide.

---

## Références Bibliographiques
Les algorithmes implémentés dans ce projet sont basés sur les sources suivantes:
**Aldous-Broder** :
* Génération uniforme par marche aléatoire.
* *Réf :* https://weblog.jamisbuck.org/2011/1/17/maze-generation-aldous-broder-algorithm

**Wilson** :
* Génération uniforme par marche aléatoire à effacement de boucles (Loop-Erased Random Walk).
* *Réf :* https://gist.github.com/mbostock/11357811

**Randomisation(Collection.shuffle)**:
* *Réf: * https://www.baeldung.com/java-shuffle-collection
