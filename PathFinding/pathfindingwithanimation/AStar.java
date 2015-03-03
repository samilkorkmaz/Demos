package pathfindingwithanimation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Full A* implementation.<br>
 *
 * @author Samil Korkmaz
 * @date January 2015
 * @copyright Public Domain
 */
public class AStar {

    private final List<Node> path = new ArrayList<>();
    private static final int SIMPLE_MOVEMENT_COST = 10; //cost when moving horizontal or vertical
    private static final int DIAGONAL_MOVEMENT_COST = 14;
    private final List<Node> openList = new ArrayList<>();
    private final List<Node> closedList = new ArrayList<>();
    private boolean isPathFinished = false;

    public boolean isPathFinished() {
        return isPathFinished;
    }

    private void addToOpenList(Node node) {
        synchronized (openList) { //to prevent ConcurrentModificationException when doing repeated path calculations
            openList.add(node);
            MyPanel.staticRepaint();
        }
    }

    private void removeFromOpenList(Node node) {
        synchronized (openList) {//to prevent ConcurrentModificationException when doing repeated path calculations
            openList.remove(node);
            MyPanel.staticRepaint();
        }
    }

    private void addToClosedList(Node node) {
        synchronized (closedList) {//to prevent ConcurrentModificationException when doing repeated path calculations
            closedList.add(node);
            MyPanel.staticRepaint();
        }
    }

    public List<Node> getPath() {
        if (isPathFinished) {
            return path;
        } else {
            return null;
        }
    }

    public List<Node> getOpenList() {
        return openList;
    }

    public List<Node> getClosedList() {
        return closedList;
    }

    private void sleep(int delayTime_ms) {
        try {
            Thread.sleep(delayTime_ms);
        } catch (InterruptedException ex) {
            Logger.getLogger(AStar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void startPathCalculation(int[][] map, Node startNode, Node endNode) {
        int delayTime_ms = MyPanel.getDelayTime_ms();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                isPathFinished = false;
                openList.clear();
                closedList.clear();
                path.clear();
                path.add(endNode);
                addToOpenList(startNode);
                if (startNode.getRowIndex() == endNode.getRowIndex() && startNode.getColIndex() == endNode.getColIndex()) {
                    //start and end nodes are the same point, path is found, go directly to end of method.
                } else {
                    Node selectedNode = null;
                    while (true) {
                        Node currentNode = getLowestCostNodeFromOpenList();
                        addToClosedList(currentNode);
                        removeFromOpenList(currentNode);
                        sleep(delayTime_ms);
                        //find neighbors of currentNode:
                        for (int iRow = -1; iRow < 2; iRow++) {
                            for (int iCol = -1; iCol < 2; iCol++) {
                                //check adjacent nodes for closeness to target node (cost increases with distance to target):
                                int neighborRowIndex = currentNode.getRowIndex() + iRow;
                                int neighborColIndex = currentNode.getColIndex() + iCol;
                                if (!isInClosedList(neighborRowIndex, neighborColIndex)) { //ignore points already in closed list
                                    if (isInMap(neighborRowIndex, neighborColIndex, map)) { //if candidate row and column is in map
                                        if (isNotWall(map[neighborRowIndex][neighborColIndex])) { //if neighbor is not a wall
                                            //calculate cost of going from currentNode to neighborNode
                                            int gCost;
                                            int singleStepGCost;
                                            if (iRow != 0 && iCol != 0) { //diagonal movement
                                                singleStepGCost = DIAGONAL_MOVEMENT_COST;
                                            } else {
                                                singleStepGCost = SIMPLE_MOVEMENT_COST;
                                            }
                                            gCost = currentNode.getGCost() + singleStepGCost; //cost to start point                                
                                            Node nodeInOpenList = getNodeFromOpenList(neighborRowIndex, neighborColIndex);
                                            int hCost = Math.abs(endNode.getRowIndex() - neighborRowIndex) * SIMPLE_MOVEMENT_COST
                                                    + Math.abs(endNode.getColIndex() - neighborColIndex) * SIMPLE_MOVEMENT_COST; //cost to end point calculated using Manhatten distance.
                                            if (nodeInOpenList != null) { //neighbor was added to openList before (i.e. it has been analyzed before with a different parent)
                                                //System.out.println("gCost = " + gCost + ", nodeInOpenList.getGCost() = " + nodeInOpenList.getGCost());
                                                if (gCost < nodeInOpenList.getGCost()) {
                                                    nodeInOpenList.setParent(currentNode); //change parent to currentNode, because this is the shorter path
                                                    nodeInOpenList.setGCost(gCost);
                                                    nodeInOpenList.setFCost(gCost + hCost);
                                                }
                                            } else {
                                                Node neighbor = new Node(currentNode, neighborRowIndex, neighborColIndex);
                                                neighbor.setGCost(gCost);
                                                neighbor.setFCost(gCost + hCost);
                                                addToOpenList(neighbor);
                                                sleep(delayTime_ms);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        //add node with lowest cost to path
                        selectedNode = getLowestCostNodeFromOpenList();
                        if (selectedNode.getRowIndex() == endNode.getRowIndex() && selectedNode.getColIndex() == endNode.getColIndex()) {
                            //we are at endNode
                            break;
                        }
                        if (openList.isEmpty()) {
                            System.out.println("No path!");
                            break;
                        }
                    }
                    while (selectedNode.getParentNode() != null) {
                        //construct path (start at endNode, ends at startNode)
                        selectedNode = selectedNode.getParentNode();
                        //System.out.println("selectedNode.getGCost = " + selectedNode.getGCost());
                        path.add(selectedNode);
                    }
                    System.out.println("");
                }
                isPathFinished = true;
                MyPanel.staticRepaint();
            }
        });
        t.start();
    }

    private static boolean isNotWall(int value) {
        return value != 0;
    }

    private static boolean isInMap(int iRow, int iCol, int[][] map) {
        return iRow >= 0 && iRow < map.length && iCol >= 0 && iCol < map[0].length;
    }

    private Node getLowestCostNodeFromOpenList() {
        int lowestCost = Integer.MAX_VALUE;
        int iLowestCost = -1;
        for (int i = 0; i < openList.size(); i++) {
            if (openList.get(i).getFCost() < lowestCost) {
                lowestCost = openList.get(i).getFCost();
                iLowestCost = i;
            }
        }
        return openList.get(iLowestCost);
    }

    private boolean isInClosedList(int iRow, int iCol) {
        boolean isInClosedList = false;
        if (!closedList.isEmpty()) {
            for (Node node : closedList) {
                if (node.getRowIndex() == iRow && node.getColIndex() == iCol) {
                    isInClosedList = true;
                }
            }
        }
        return isInClosedList;
    }

    private Node getNodeFromOpenList(int iRow, int iCol) {
        Node nodeToReturn = null;
        if (!openList.isEmpty()) {
            for (Node node : openList) {
                if (node.getRowIndex() == iRow && node.getColIndex() == iCol) {
                    nodeToReturn = node;
                }
            }
        }
        return nodeToReturn;
    }
}
