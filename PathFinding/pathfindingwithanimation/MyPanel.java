package pathfindingwithanimation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
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
 * @copyright Public Domain
 */
public class MyPanel extends JPanel {

    private final AStar aStar = new AStar();
    private static MyPanel instance;
    private static final int PREF_WIDTH = 400;
    private static final int PREF_HEIGHT = 300;
    private static final int RECT_WIDTH = 25;
    private static final int RECT_HEIGHT = 25;
    private static final int X_OFFSET = 50;
    private static final int Y_OFFSET = 120;
    private final List<Rectangle> mapRectList = new ArrayList<>();

    private int[][] map;
    private static int mapNRows;
    private static int mapNCols;
    private static final JLabel jlTimeDelay = new JLabel("time delay [ms]: ");
    private static final JTextField jtfTimeDelay = new JTextField("500");
    private static final JLabel jlStart = new JLabel("Start indices (row, col): ");
    private static final JTextField jtfStartRowIndex = new JTextField("0");
    private static final JTextField jtfStartColIndex = new JTextField("0");
    private static final JLabel jlEnd = new JLabel("End indices (row, col): ");
    private static final JTextField jtfEndRowIndex = new JTextField("2");
    private static final JTextField jtfEndColIndex = new JTextField("7");
    private static final JButton jbCalculate = new JButton("find path");
    private static Node startNode;
    private static Node endNode;

    public MyPanel() {
        super();
    }

    public static void staticRepaint() {
        if (instance != null) {
            instance.repaint();            
        }
    }

    private MyPanel(final int[][] map) {
        super();
        setLayout(null);
        this.map = map;
        mapNRows = map.length;
        mapNCols = map[0].length;
        setRectList();
        jlTimeDelay.setBounds(10, 10, 150, 20);
        jtfTimeDelay.setBounds(150, 10, 100, 20);
        jlStart.setBounds(10, 30, 150, 20);
        jtfStartRowIndex.setBounds(150, 30, 50, 20);
        jtfStartColIndex.setBounds(200, 30, 50, 20);
        jlEnd.setBounds(10, 50, 150, 20);
        jtfEndRowIndex.setBounds(150, 50, 50, 20);
        jtfEndColIndex.setBounds(200, 50, 50, 20);
        jbCalculate.setBounds(10, 75, 250, 30);

        add(jlTimeDelay);
        add(jtfTimeDelay);
        add(jlStart);
        add(jtfStartRowIndex);
        add(jtfStartColIndex);
        add(jlEnd);
        add(jtfEndRowIndex);
        add(jtfEndColIndex);
        add(jbCalculate);

        jbCalculate.addActionListener((ActionEvent ae) -> {
            jbCalculate.setEnabled(false);
            startNode = new Node(null, Integer.parseInt(jtfStartRowIndex.getText()), Integer.parseInt(jtfStartColIndex.getText()));
            endNode = new Node(null, Integer.parseInt(jtfEndRowIndex.getText()), Integer.parseInt(jtfEndColIndex.getText()));
            aStar.startPathCalculation(map, startNode, endNode);
            repaint();
        });
    }

    public static int getDelayTime_ms() {
        return Integer.parseInt(jtfTimeDelay.getText());
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

            //set wall color
            if (map[iRow][iCol] == 0) { //0 : wall
                g2.setColor(Color.BLACK);
            }

            //set open list color
            List<Node> myOpenList = aStar.getOpenList();
            synchronized (myOpenList) {//to prevent ConcurrentModificationException when doing repeated path calculations
                for (Node node : myOpenList) {
                    if (node.getColIndex() == iCol && node.getRowIndex() == iRow) {
                        g2.setColor(Color.YELLOW);
                        break;
                    }
                }
            }

            //set closed list color
            List<Node> myClosedList = aStar.getClosedList();
            synchronized (myClosedList) {//to prevent ConcurrentModificationException when doing repeated path calculations
                for (Node node : myClosedList) {
                    if (node.getColIndex() == iCol && node.getRowIndex() == iRow) {
                        g2.setColor(Color.CYAN);
                        break;
                    }
                }
            }

            //set path color
            if (aStar.isPathFinished()) {
                jbCalculate.setEnabled(true);
                List<Node> path = aStar.getPath();
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
            
            //set start and end cell colors
            if (startNode != null) {
                if (i == get1DIndex(startNode)) {
                    g2.setColor(Color.BLUE);
                }
                if (i == get1DIndex(endNode)) {
                    g2.setColor(Color.RED);
                }
            }

            //draw cell with set color
            g2.fill(rect);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawRect(rect.x, rect.y, rect.width, rect.height);
        }
    }
    
    private static int get1DIndex(Node node) {
        return node.getRowIndex() * mapNCols+ node.getColIndex();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREF_WIDTH, PREF_HEIGHT);
    }

    public static void createAndShowGUI(final int[][] map) {
        JFrame frame = new JFrame("Animated A*");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        if (instance == null) {
            instance = new MyPanel(map);
        }
        frame.getContentPane().add(instance);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
