package Graph;

import java.util.ArrayList;
import java.util.LinkedList;


public class Graph {
    // classe de graphe non orientés permettant de manipuler
    // en même temps des arcs (orientés)
    // pour pouvoir stocker un arbre couvrant, en plus du graphe

    public int order;
    public int upperBound;
    int edgeCardinality;

    ArrayList<LinkedList<Edge>> incidency;
    ArrayList<LinkedList<Arc>> inIncidency;
    ArrayList<LinkedList<Arc>> outIncidency;

    public Graph(int upperBound) {
        // Au début, upperBound==order
        // Ensuite, on pourrait retirer des sommets du graphe.
        // Ainsi, on pourrait avoir upperBound > order
        // Cette modification de la classe devient nécessaire
        // si vous implémentez
        // ou l'algorithme de génération d'arbre couvrant
        // par suppression de sommet, ou l'opération de contraction d’arête.
        // Autrement, on pourra asssumer que upperBound==order.

        // à compléter
    }

    public boolean isVertex(int vertex) {
        // Après avori supprimé certains sommets
        // pas tous le sommets numerotés 0,...,n-1 sont 'vivant'.

        // à compléter
        return true;
    }

    public void addVertex(int vertex) {
        // à compléter
    }

    public void deleteVertex(int vertex){
        // à compléter
    }

    public void ensureVertex(int vertex) {
        // Synonime de addVertex ?

        // à compléter
    }

    public void addArc(Arc arc) {
        // à compléter
    }

    public void addEdge(Edge edge) {
        // à compléter
    }

    public Arc[] outEdges(int vertex) {
        // à modifier, si nécessaire

        // Pour la prochaine ligne voir
        // https://www.baeldung.com/java-collection-toarray-methods
        return outIncidency.get(vertex).toArray(new Arc[0]);
   }

}
