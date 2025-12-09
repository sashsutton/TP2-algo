import Graph.*;
import GraphClasses.*;
import RandomTreeAlgos.SpanningTreeAlgos; // Notre nouvelle classe
import RandomTreeAlgos.BreadthFirstSearch;
import Graphics.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;


public class Main {

    @SuppressWarnings("unused")
    private final static Random gen = new Random();

    static Grid grid = null;


    public static void main(String argv[]) throws InterruptedException {

        Graph graph = chooseFromGraphFamily();

        // TESTER TOUS LES ALGOS
        // Vous pouvez commenter/décommenter ceux que vous voulez tester
        testAlgorithm(graph, "Kruskal", 1);
        testAlgorithm(graph, "Prim", 2);
        testAlgorithm(graph, "Aldous-Broder", 3);
        testAlgorithm(graph, "Wilson", 4);
    }

    private static void testAlgorithm(Graph graph, String name, int algoID) throws InterruptedException {
        System.out.println("--- TESTING " + name + " ---");
        ArrayList<Edge> randomTree = null;
        int noOfSamples = 10;
        Stats stats = new Stats(noOfSamples);

        for (int i = 0; i < noOfSamples; i++) {
            randomTree = genTree(graph, algoID);
            stats.update(randomTree);
        }
        stats.print();

        // Afficher le dernier arbre généré si c'est une grille
        if (grid != null && randomTree != null) {
            showGrid(grid, randomTree, name);
        }
        System.out.println();
    }

    private static Graph chooseFromGraphFamily() {
        // Parametriser ici cette fonction afin de pouvoir choisir
        // quelle classe de graphe utiliser

        // Grille plus petite pour tests rapides
        grid = new Grid(40, 30);
        Graph graph = grid.graph;

        // Graph graph = new Complete(400).graph;
        // Graph graph = new ErdosRenyi(1_000, 100).graph;
        // Graph graph = new Lollipop(1_000).graph;
        return graph;
    }

    public static ArrayList<Edge> genTree(Graph graph, int algoID) {
        switch (algoID) {
            case 1: return SpanningTreeAlgos.randomKruskal(graph);
            case 2: return SpanningTreeAlgos.randomPrim(graph);
            case 3: return SpanningTreeAlgos.aldousBroder(graph);
            case 4: return SpanningTreeAlgos.wilson(graph);
            default: return new ArrayList<>();
        }
    }


    private static class Stats {
        public int nbrOfSamples = 10;
        private int diameterSum = 0;
        private double eccentricitySum = 0;
        private long wienerSum = 0;
        private int degreesSum[] = {0, 0, 0, 0, 0};
        private int degrees[];
        long startingTime = 0;

        public Stats(int noOfSamples) {
            this.nbrOfSamples = noOfSamples;
            startingTime = System.nanoTime();
        }

        public void print() {
            long delay = System.nanoTime() - startingTime;

            System.out.println("On " + nbrOfSamples + " samples:");
            System.out.println("Average eccentricity: "
                    + (eccentricitySum / nbrOfSamples));
            System.out.println("Average wiener index: "
                    + (wienerSum / nbrOfSamples));
            System.out.println("Average diameter: "
                    + (diameterSum / nbrOfSamples));
            System.out.println("Average computation time: "
                    + delay / (nbrOfSamples * 1_000_000) + "ms");

        }

        public void update(ArrayList<Edge> randomTree) {
            RootedTree rooted = new RootedTree(randomTree, 0);
            diameterSum = diameterSum + rooted.getDiameter();
            eccentricitySum = eccentricitySum + rooted.getAverageEccentricity();
            wienerSum = wienerSum + rooted.getWienerIndex();
        }

    }

    private static void showGrid(
            Grid grid,
            ArrayList<Edge> randomTree,
            String algoName
    ) throws InterruptedException {
        RootedTree rooted = new RootedTree(randomTree, 0);

        JFrame window = new JFrame("Solution: " + algoName);
        final Labyrinth laby = new Labyrinth(grid, rooted);

        laby.setStyleBalanced();
        laby.setShapeSmoothSmallNodes();

        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Evite de tuer tout le programme
        window.getContentPane().add(laby);
        window.pack();
        window.setLocationRelativeTo(null);


        for (final Edge e : randomTree) {
            laby.addEdge(e);
        }
        laby.drawLabyrinth();

        window.setVisible(true);

        // Pour générer un fichier image.
        try {
            laby.saveImage("resources/" + algoName + ".png");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

}