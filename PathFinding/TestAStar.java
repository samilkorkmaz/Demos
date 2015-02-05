package pathfinding03;

import java.util.List;

/**
 * Test for AStarPathFinding.
 *
 * @author Samil Korkmaz
 * @date February 2015
 * @license Public Domain
 */
public class TestAStar {

    public static void main(String[] args) {
        int wall = AStarPathFinder.WALL;
        int open = AStarPathFinder.OPEN;
        int[][] map = {
            {open, open, open, open, wall, open, open, open},
            {open, open, wall, open, wall, open, open, open},
            {open, open, wall, open, wall, open, open, open},
            {wall, wall, wall, open, wall, wall, wall, wall},
            {open, open, open, open, open, open, open, open},
            {open, open, wall, open, wall, wall, open, wall},
            {open, open, wall, open, open, open, open, open},
            {open, open, open, open, open, open, open, open}};

        Node startNode = new Node(null, 1, 1);
        Node endNode = new Node(null, 6, 7);
        List<Node> path = MyPanel.getAStarPathFinder().calcPath(map, startNode, endNode);
        MyPanel.printPath(path);
        MyPanel.createAndShowGUI(map, path);
    }
}
