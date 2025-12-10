import Graph.*;
import GraphClasses.*;
import RandomTreeAlgos.SpanningTreeAlgos;
import Graphics.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;

public class Main {

    static Grid grid = null;

    public static void main(String argv[]) throws InterruptedException {
        Graph graph = chooseFromGraphFamily();

        // Comparaison des algorithmes
        // Note: Kruskal et Prim étant similaires (gloutons pondérés),
        // nous remplaçons Prim par Random DFS pour explorer une autre topologie.
        testAlgorithm(graph, "Kruskal", 1);
        testAlgorithm(graph, "RandomDFS", 2); // Remplacement ici
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

        if (grid != null && randomTree != null) {
            showGrid(grid, randomTree, name);
        }
        System.out.println();
    }

    private static Graph chooseFromGraphFamily() {
        grid = new Grid(40, 30);
        return grid.graph;
    }

    public static ArrayList<Edge> genTree(Graph graph, int algoID) {
        switch (algoID) {
            case 1: return SpanningTreeAlgos.randomKruskal(graph);
            case 2: return SpanningTreeAlgos.randomDFS(graph); // Appel Random DFS
            case 3: return SpanningTreeAlgos.aldousBroder(graph);
            case 4: return SpanningTreeAlgos.wilson(graph);
            default: return new ArrayList<>();
        }
    }

    private static class Stats {
        public int nbrOfSamples;
        private int diameterSum = 0;
        private double eccentricitySum = 0;
        private long wienerSum = 0;
        long startingTime = 0;

        public Stats(int noOfSamples) {
            this.nbrOfSamples = noOfSamples;
            startingTime = System.nanoTime();
        }

        public void print() {
            long delay = System.nanoTime() - startingTime;
            System.out.println("Sur " + nbrOfSamples + " échantillons:");
            System.out.println(" - Diamètre moyen : " + (diameterSum / nbrOfSamples));
            System.out.println(" - Indice de Wiener moyen : " + (wienerSum / nbrOfSamples));
            System.out.println(" - Excentricité moyenne : " + String.format("%.2f", (eccentricitySum / nbrOfSamples)));
            System.out.println(" - Temps moyen : " + delay / (nbrOfSamples * 1_000_000) + "ms");
        }

        public void update(ArrayList<Edge> randomTree) {
            RootedTree rooted = new RootedTree(randomTree, 0);
            diameterSum += rooted.getDiameter();
            eccentricitySum += rooted.getAverageEccentricity();
            wienerSum += rooted.getWienerIndex();
        }
    }

    private static void showGrid(Grid grid, ArrayList<Edge> randomTree, String algoName) {
        RootedTree rooted = new RootedTree(randomTree, 0);
        JFrame window = new JFrame("Solution: " + algoName);
        final Labyrinth laby = new Labyrinth(grid, rooted);
        laby.setStyleBalanced();
        laby.setShapeSmoothSmallNodes();
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.getContentPane().add(laby);
        window.pack();
        window.setLocationRelativeTo(null);
        for (final Edge e : randomTree) {
            laby.addEdge(e);
        }
        laby.drawLabyrinth();
        window.setVisible(true);
        try {
            laby.saveImage("resources/" + algoName + ".png");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}