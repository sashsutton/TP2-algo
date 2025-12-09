package Graph;

import java.util.ArrayList;
import java.util.LinkedList;

public class Graph {
    // Classe de graphe non orienté permettant de manipuler
    // en même temps des arcs (orientés) pour les parcours

    public int order;
    public int upperBound;
    int edgeCardinality;

    // Liste d'adjacence pour les arêtes (non orienté)
    ArrayList<LinkedList<Edge>> incidency;
    // Liste d'adjacence pour les arcs entrants (non utilisé ici mais prévu par la structure)
    ArrayList<LinkedList<Arc>> inIncidency;
    // Liste d'adjacence pour les arcs sortants (essentiel pour les parcours)
    ArrayList<LinkedList<Arc>> outIncidency;

    public Graph(int upperBound) {
        this.upperBound = upperBound;
        this.order = upperBound; // On suppose au début que tous les sommets existent
        this.edgeCardinality = 0;

        this.incidency = new ArrayList<>(upperBound);
        this.inIncidency = new ArrayList<>(upperBound);
        this.outIncidency = new ArrayList<>(upperBound);

        for (int i = 0; i < upperBound; i++) {
            incidency.add(new LinkedList<>());
            inIncidency.add(new LinkedList<>());
            outIncidency.add(new LinkedList<>());
        }
    }

    public boolean isVertex(int vertex) {
        return vertex >= 0 && vertex < upperBound;
    }

    public void addVertex(int vertex) {
        // Dans ce TP, on considère généralement que les sommets sont fixes à la création
        // Mais on pourrait réactiver un sommet ici si besoin
    }

    public void addEdge(Edge edge) {
        // Ajout dans la liste des arêtes (non orienté)
        incidency.get(edge.source).add(edge);
        incidency.get(edge.dest).add(edge);

        // Ajout des deux arcs correspondants (u->v et v->u)
        // Ceci est CRUCIAL pour que BreadthFirstSearch et les autres algos fonctionnent
        Arc forward = new Arc(edge, false); // source -> dest
        Arc backward = new Arc(edge, true); // dest -> source

        addArc(forward);
        addArc(backward);

        edgeCardinality++;
    }

    public void addArc(Arc arc) {
        outIncidency.get(arc.getSource()).add(arc);
        inIncidency.get(arc.getDest()).add(arc);
    }

    // Retourne les arcs sortants d'un sommet (pour les parcours)
    public Arc[] outEdges(int vertex) {
        return outIncidency.get(vertex).toArray(new Arc[0]);
    }

    // Accesseur utile pour les algos basés sur les arêtes (Kruskal)
    public ArrayList<Edge> getAllEdges() {
        ArrayList<Edge> all = new ArrayList<>();
        // On parcourt incidency. Comme c'est non orienté, chaque arête y est 2 fois.
        // Pour éviter les doublons, on peut parcourir outIncidency et prendre seulement arc.reversed == false
        for (int i = 0; i < order; i++) {
            for (Arc a : outIncidency.get(i)) {
                if (!a.reversed) { // On ne prend l'arête qu'une seule fois
                    all.add(a.support);
                }
            }
        }
        return all;
    }
}