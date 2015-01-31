package test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * Check speed of TreeSet and ArrayList. Adds items to set and list, then finds the item with the lowest cost. ArrayList is an order of magnitude faster.
 *
 * @author Samil Korkmaz
 * @date January 2015
 * @license Public Domain
 */
public class SetListTest {

    private static final int NB_OF_ITEMS = 100;
    private static final int[] costArray = new int[NB_OF_ITEMS];

    public static class Node implements Comparable {

        private final int cost;

        public Node(int cost) {
            this.cost = cost;
        }

        public int getCost() {
            return cost;
        }

        @Override
        public int compareTo(Object t) {
            return this.cost > ((Node) t).cost ? 1 : -1;
        }
    }

    private static void showDt(long t1) {
        long t2 = System.nanoTime();
        long dt = t2 - t1;
        System.out.println("dt = " + dt * 1e-6 + " ms");
    }

    public static void showLowestCostNode(Collection collection) {
        Iterator iterator = collection.iterator();
        int lowestCost = Integer.MAX_VALUE;
        Node lowestCostNode = null;
        while (iterator.hasNext()) {
            Node node = (Node) iterator.next();
            if (node.getCost() < lowestCost) {
                lowestCost = node.getCost();
                lowestCostNode = node;
            }
        }
        System.out.println("lowestCost = " + lowestCostNode.getCost());
    }

    public static void runTreeSetTest() {
        System.out.println("TreeSet test:");
        long t1 = System.nanoTime();
        Collection<Node> treeSet = new TreeSet();
        for (int i = 0; i < NB_OF_ITEMS; i++) {
            treeSet.add(new Node(costArray[i]));
        }
        showLowestCostNode(treeSet);
        showDt(t1);
    }

    public static void runArrayListTest() {
        System.out.println("ArrayList test:");
        long t1 = System.nanoTime();
        Collection<Node> arrayList = new ArrayList<>();
        for (int i = 0; i < NB_OF_ITEMS; i++) {
            arrayList.add(new Node(costArray[i]));
        }
        showLowestCostNode(arrayList);
        showDt(t1);
    }

    public static void calcCostArray() {
        System.out.print("cost = ");
        for (int i = 0; i < NB_OF_ITEMS; i++) {
            int cost = (int) (1e4 * Math.random());
            System.out.print(cost + ", ");
            costArray[i] = cost;
        }
        System.out.println("");

    }

    public static void main(String[] args) {
        calcCostArray();
        System.out.println("");
        runTreeSetTest();
        System.out.println("");
        runArrayListTest();
    }

}
