package pathfinding03;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Display map and path.
 *
 * @author Samil Korkmaz
 * @date January 2015
 * @license Public Domain
 */
public class MyPanel extends JPanel {

    private static final int PREF_WIDTH = 400;
    private static final int PREF_HEIGHT = 300;
    private static final int RECT_WIDTH = 25;
    private static final int RECT_HEIGHT = 25;
    private static final int X_OFFSET = 50;
    private static final int Y_OFFSET = 70;
    private final List<Rectangle> mapRectList = new ArrayList<>();

    private int[][] map;
    private int mapNRows;
    private int mapNCols;
    private List<Node> path;
    private static final JLabel jlStart = new JLabel("Start indices (row, col): ");
    private static final JTextField jtfStartRowIndex = new JTextField("1");
    private static final JTextField jtfStartColIndex = new JTextField("1");
    private static final JLabel jlEnd = new JLabel("End indices (row, col): ");
    private static final JTextField jtfEndRowIndex = new JTextField("5");
    private static final JTextField jtfEndColIndex = new JTextField("7");
    private static final JButton jbCalculate = new JButton("calculate");
    private static AStarPathFinder aStarPathFinder = new AStarPathFinder();
    
    public static AStarPathFinder getAStarPathFinder() {
        return aStarPathFinder;
    }

    public MyPanel() {
        super();
    }

    private MyPanel(final int[][] map, final List<Node> path) {
        super();
        this.map = map;
        mapNRows = map.length;
        mapNCols = map[0].length;
        this.path = path;
        setRectList();
        add(jlStart);
        add(jtfStartRowIndex);
        add(jtfStartColIndex);
        add(jlEnd);
        add(jtfEndRowIndex);
        add(jtfEndColIndex);
        add(jbCalculate);

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseMoved(e.getX(), e.getY());
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //set rectangle that contains the mouse pointer as the startNode
                Rectangle rect = getMouseContainingRect(e.getX(), e.getY());
                if (rect != null) {
                    Node startNode = createNode(rect);
                    jtfStartRowIndex.setText(String.valueOf(startNode.getRowIndex()));
                    jtfStartColIndex.setText(String.valueOf(startNode.getColIndex()));
                }
            }
        });

        jbCalculate.addActionListener((ActionEvent ae) -> {
            Node startNode = new Node(null, Integer.parseInt(jtfStartRowIndex.getText()), Integer.parseInt(jtfStartColIndex.getText()));
            Node endNode = new Node(null, Integer.parseInt(jtfEndRowIndex.getText()), Integer.parseInt(jtfEndColIndex.getText()));
            this.path = aStarPathFinder.calcPath(map, startNode, endNode);
            printPath(this.path);
            repaint();
        });
    }

    private Rectangle getMouseContainingRect(int mouseX, int mouseY) {
        Rectangle mouseContainingRect = null;
        for (Rectangle rect : mapRectList) {
            if (rect.contains(mouseX, mouseY) && isNotWall(rect)) {
                mouseContainingRect = rect;
            }
        }
        return mouseContainingRect;
    }

    private boolean isNotWall(Rectangle rect) {
        RectRowCol rectRowCol = getRowCol(rect);
        return map[rectRowCol.rowIndex][rectRowCol.colIndex] != 0;
    }

    private Node createNode(Rectangle rect) {
        RectRowCol rectRowCol = getRowCol(rect);
        return new Node(null, rectRowCol.rowIndex, rectRowCol.colIndex);
    }

    private static class RectRowCol {

        int rowIndex;
        int colIndex;
    }

    private RectRowCol getRowCol(Rectangle rect) {
        RectRowCol rectRowCol = new RectRowCol();
        int i1D = mapRectList.indexOf(rect);
        rectRowCol.colIndex = i1D % mapNCols;
        rectRowCol.rowIndex = (i1D - rectRowCol.colIndex) / mapNCols;
        return rectRowCol;
    }

    private void handleMouseMoved(int mouseX, int mouseY) {
        //find cell over which the mouse pointer is
        Rectangle rect = getMouseContainingRect(mouseX, mouseY);
        if (rect != null) {
            int iStartRow = Integer.parseInt(jtfStartRowIndex.getText());
            int iStartCol = Integer.parseInt(jtfStartColIndex.getText());
            Node startNode = new Node(null, iStartRow, iStartCol);
            Node endNode = createNode(rect);
            path = aStarPathFinder.calcPath(map, startNode, endNode);
            MyPanel.printPath(path);
            repaint();

            jtfEndRowIndex.setText(String.valueOf(endNode.getRowIndex()));
            jtfEndColIndex.setText(String.valueOf(endNode.getColIndex()));
        }
    }

    public static void printPath(List<Node> path) {
        if (aStarPathFinder.isPathFound()) {
            System.out.print("path (row, col):");
            for (Node node : path) {
                System.out.print("(" + node.getRowIndex() + ", " + node.getColIndex() + "), ");
            }
            System.out.println("");
        }
    }

    private void setRectList() {
        for (int iRow = 0; iRow < mapNRows; iRow++) {
            for (int iCol = 0; iCol < mapNCols; iCol++) {
                Rectangle rect = new Rectangle(iCol * RECT_WIDTH + X_OFFSET, iRow * RECT_HEIGHT + Y_OFFSET, RECT_WIDTH, RECT_HEIGHT);
                mapRectList.add(rect);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < mapRectList.size(); i++) {
            g2.setColor(Color.WHITE);
            Rectangle rect = mapRectList.get(i);
            int i1D = mapRectList.indexOf(rect);
            int iCol = i1D % mapNCols;
            int iRow = (i1D - iCol) / mapNCols;

            if (map[iRow][iCol] == AStarPathFinder.WALL) {
                g2.setColor(Color.BLACK);
            } else {
                if (path != null) {
                    for (int j = 0; j < path.size(); j++) {
                        if (path.get(j).getColIndex() == iCol && path.get(j).getRowIndex() == iRow) {
                            if (j == path.size() - 1) {
                                g2.setColor(Color.BLUE);
                            } else if (j == 0) {
                                g2.setColor(Color.RED);
                            } else {
                                g2.setColor(Color.GREEN);
                            }
                            break;
                        }
                    }
                }

            }
            g2.fill(rect);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRect(rect.x, rect.y, rect.width, rect.height);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREF_WIDTH, PREF_HEIGHT);
    }

    public static void createAndShowGUI(final int[][] map, final List<Node> path) {
        JFrame frame = new JFrame("A* Path Finding");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MyPanel(map, path));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
