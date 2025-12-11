package RandomTreeAlgos;

import Graph.*;
import java.util.*;

public class SpanningTreeAlgos {

    private static final Random gen = new Random();

    // --- ALGO 1: KRUSKAL ALÉATOIRE ---
    public static ArrayList<Edge> randomKruskal(Graph graph) {
        ArrayList<Edge> tree = new ArrayList<>();
        ArrayList<Edge> allEdges = graph.getAllEdges();

        // Mélange aléatoire des arêtes (Simule des poids aléatoires)
        Collections.shuffle(allEdges, gen);

        UnionFind uf = new UnionFind(graph.order);

        for (Edge e : allEdges) {
            if (uf.union(e.source, e.dest)) {
                tree.add(e);
            }
        }
        return tree;
    }

    // --- ALGO 2: PARCOURS EN PROFONDEUR ALÉATOIRE (Random DFS) ---
    // Principe : On explore le graphe récursivement en choisissant les voisins dans un ordre aléatoire.
    public static ArrayList<Edge> randomDFS(Graph graph) {
        ArrayList<Edge> tree = new ArrayList<>();
        boolean[] visited = new boolean[graph.order];

        // On commence au sommet 0 (ou un autre aléatoire)
        int startNode = gen.nextInt(graph.order);
        dfsRecursive(graph, startNode, visited, tree);

        return tree;
    }

    private static void dfsRecursive(Graph g, int u, boolean[] visited, ArrayList<Edge> tree) {
        visited[u] = true;

        // Récupérer les voisins
        Arc[] neighbors = g.outEdges(u);

        // Conversion en liste pour mélanger facilement
        List<Arc> neighborsList = new ArrayList<>(Arrays.asList(neighbors));
        Collections.shuffle(neighborsList, gen);

        for (Arc a : neighborsList) {
            if (!visited[a.getDest()]) {
                tree.add(a.support);
                dfsRecursive(g, a.getDest(), visited, tree);
            }
        }
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
            if (neighbors.length == 0) break;

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

        inTree[0] = true; // La racine
        Arc[] next = new Arc[graph.order];

        for (int i = 1; i < graph.order; i++) {
            if (inTree[i]) continue;

            int u = i;
            // Marche aléatoire jusqu'à l'arbre
            while (!inTree[u]) {
                Arc[] neighbors = graph.outEdges(u);
                next[u] = neighbors[gen.nextInt(neighbors.length)];
                u = next[u].getDest();
            }

            // Reconstruction du chemin sans cycle
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