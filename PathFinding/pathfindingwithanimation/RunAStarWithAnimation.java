package pathfindingwithanimation;

/**
 * Test for A* pathfinding algorithm with animation showing search progress.
 *
 * @author Samil Korkmaz
 * @date March 2015
 * @copyright Public Domain
 */
public class RunAStarWithAnimation {

    public static void main(String[] args) {
        int[][] map = {
            {1, 1, 0, 1, 1, 1, 1, 1},
            {1, 1, 0, 1, 0, 0, 0, 1},
            {1, 0, 0, 1, 1, 0, 1, 1},
            {1, 1, 1, 1, 1, 0, 1, 1},
            {1, 1, 1, 1, 1, 0, 1, 1},
            {1, 1, 1, 1, 1, 1, 1, 1}}; 
        
        MyPanel.createAndShowGUI(map);
    }
}
