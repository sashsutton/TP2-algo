package RandomTreeAlgos;

import Graph.*;
import java.util.*;

public class SpanningTreeAlgos {

    private static final Random gen = new Random();

    // --- ALGO 1: KRUSKAL ALÉATOIRE ---
    public static ArrayList<Edge> randomKruskal(Graph graph) {
        ArrayList<Edge> tree = new ArrayList<>();
        ArrayList<Edge> allEdges = graph.getAllEdges(); // Nécessite la méthode ajoutée dans Graph.java

        // 1. Mélanger les arêtes (équivalent à donner des poids aléatoires et trier)
        Collections.shuffle(allEdges, gen);

        // 2. Union-Find pour éviter les cycles
        UnionFind uf = new UnionFind(graph.order);

        for (Edge e : allEdges) {
            if (uf.union(e.source, e.dest)) {
                tree.add(e);
            }
        }
        return tree;
    }

    // --- ALGO 2: PRIM ALÉATOIRE ---
    public static ArrayList<Edge> randomPrim(Graph graph) {
        ArrayList<Edge> tree = new ArrayList<>();
        boolean[] visited = new boolean[graph.order];

        // PriorityQueue stocke les Arcs. On trie sur un poids aléatoire assigné à la volée ?
        // Pour simplifier : on peut attribuer un poids aléatoire à chaque Edge au début.
        // Mais modifier l'objet Edge est risqué si partagé.
        // Alternative simple : Une map Edge -> Poids ou mélanger les voisins.

        // Version simple avec PriorityQueue d'objets custom
        class WeightedArc implements Comparable<WeightedArc> {
            Arc arc;
            double weight;
            public WeightedArc(Arc a, double w) { this.arc = a; this.weight = w; }
            public int compareTo(WeightedArc o) { return Double.compare(this.weight, o.weight); }
        }

        PriorityQueue<WeightedArc> pq = new PriorityQueue<>();

        // Départ arbitraire
        int startNode = 0;
        visited[startNode] = true;

        // Ajouter voisins initiaux avec poids aléatoires
        for (Arc a : graph.outEdges(startNode)) {
            pq.add(new WeightedArc(a, gen.nextDouble()));
        }

        while (!pq.isEmpty()) {
            WeightedArc wa = pq.poll();
            Arc arc = wa.arc;

            if (visited[arc.getDest()]) continue;

            visited[arc.getDest()] = true;
            tree.add(arc.support); // Ajout de l'arête à l'arbre

            for (Arc neighbor : graph.outEdges(arc.getDest())) {
                if (!visited[neighbor.getDest()]) {
                    pq.add(new WeightedArc(neighbor, gen.nextDouble()));
                }
            }
        }
        return tree;
    }

    // --- ALGO 3: ALDOUS-BRODER (Marche Aléatoire) ---
    public static ArrayList<Edge> aldousBroder(Graph graph) {
        ArrayList<Edge> tree = new ArrayList<>();
        boolean[] visited = new boolean[graph.order];

        int current = gen.nextInt(graph.order);
        visited[current] = true;
        int visitedCount = 1;

        while (visitedCount < graph.order) {
            Arc[] neighbors = graph.outEdges(current);
            if (neighbors.length == 0) break; // Sécurité

            Arc neighborArc = neighbors[gen.nextInt(neighbors.length)];
            int neighbor = neighborArc.getDest();

            if (!visited[neighbor]) {
                visited[neighbor] = true;
                visitedCount++;
                tree.add(neighborArc.support);
            }
            current = neighbor;
        }
        return tree;
    }

    // --- ALGO 4: WILSON (Loop-Erased Random Walk) ---
    public static ArrayList<Edge> wilson(Graph graph) {
        ArrayList<Edge> tree = new ArrayList<>();
        boolean[] inTree = new boolean[graph.order];

        // Init : mettre la racine (0) dans l'arbre
        inTree[0] = true;

        // Tableau pour stocker le chemin courant (next[u] = l'arc emprunté depuis u)
        Arc[] next = new Arc[graph.order];

        for (int i = 1; i < graph.order; i++) {
            if (inTree[i]) continue;

            int u = i;
            // Phase 1 : Marche aléatoire jusqu'à toucher l'arbre
            while (!inTree[u]) {
                Arc[] neighbors = graph.outEdges(u);
                Arc move = neighbors[gen.nextInt(neighbors.length)];
                next[u] = move; // Enregistre le mouvement (écrase les cycles automatiquement)
                u = move.getDest();
            }

            // Phase 2 : Ajouter le chemin sans boucle à l'arbre
            u = i;
            while (!inTree[u]) {
                Arc arc = next[u];
                tree.add(arc.support);
                inTree[u] = true;
                u = arc.getDest();
            }
        }
        return tree;
    }
}