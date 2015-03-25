import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Get coordinates left clicked by mouse.<br/>
 *
 * I used this tool to get coordinates of images for my Save My Cheese levels.
 *
 * @author Samil Korkmaz
 * @date February 2015
 * @license Public Domain
 */
public class CoordinateExtractionPanel extends JPanel {

    private static ImageIcon imageIcon;
    private static final int PREF_WIDTH = 800;
    private static final int PREF_HEIGHT = 700;
    private static final int STANDARD_HEIGHT = 20;
    private static final int STANDARD_WIDTH = 150;
    private final JTextArea jTextAreaX = new JTextArea();
    private final JTextArea jTextAreaY = new JTextArea();
    private final List<Integer> mouseXlist = new ArrayList<>();
    private final List<Integer> mouseYList = new ArrayList<>();
    private static File currentDir;

    public class MyLabel extends JLabel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.RED);
            int r = 5;
            for (int i = 0; i < mouseXlist.size(); i++) {
                g2.fillOval(mouseXlist.get(i) - this.getX() - r, mouseYList.get(i) - this.getY() - r, 2 * r, 2 * r);
            }
        }
    }

    public CoordinateExtractionPanel() {
        MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
        addMouseListener(myMouseAdapter);
        addMouseMotionListener(myMouseAdapter);
        JLabel jLabelX = new JLabel("x:");
        jLabelX.setBounds(10, 10, 10, STANDARD_HEIGHT);
        add(jLabelX);
        JLabel jLabelY = new JLabel("y:");
        jLabelY.setBounds(jLabelX.getBounds().x, jLabelX.getBounds().height + 15, 10, STANDARD_HEIGHT);
        add(jLabelY);
        createTextArea(jTextAreaX, jLabelX.getBounds().x + 15, jLabelX.getBounds().y);
        createTextArea(jTextAreaY, jLabelX.getBounds().x + 15, jLabelY.getBounds().y);
        setLayout(null);

        MyLabel jlImage = new MyLabel();
        jlImage.setOpaque(true);
        jlImage.setBackground(Color.LIGHT_GRAY);
        jlImage.setBounds(jLabelX.getBounds().x, jLabelY.getBounds().y + 2 * STANDARD_HEIGHT, 600, 600);        
        add(jlImage);

        JButton jbOpen = new JButton("Open Image...");
        jbOpen.setBounds(jlImage.getBounds().x + jlImage.getBounds().width + 10, jlImage.getBounds().y, STANDARD_WIDTH, STANDARD_HEIGHT);
        jbOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG & JPG Images", "png", "jpg");                
                chooser.setCurrentDirectory(currentDir);
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {  
                    currentDir = chooser.getCurrentDirectory();
                    imageIcon = new ImageIcon(currentDir + "\\" + chooser.getSelectedFile().getName());
                    jlImage.setIcon(imageIcon);
                }
                repaint();
            }
        });
        add(jbOpen);

        JButton jbClear = new JButton("Clear");
        jbClear.setBounds(jlImage.getBounds().x + jlImage.getBounds().width + 10, jlImage.getBounds().y + STANDARD_HEIGHT + 5, STANDARD_WIDTH, STANDARD_HEIGHT);
        jbClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                mouseXlist.clear();
                jTextAreaX.setText("");
                mouseYList.clear();
                jTextAreaY.setText("");
                repaint();
            }
        });
        add(jbClear);
    }

    private void createTextArea(JTextArea jt, int x, int y) {
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(jt);
        jScrollPane.setBounds(x, y, 575, STANDARD_HEIGHT);
        add(jScrollPane);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(PREF_WIDTH, PREF_HEIGHT);
    }

    private class MyMouseAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent evt) {
            if (evt.getButton() == MouseEvent.BUTTON1) {
                mouseXlist.add(evt.getX());
                String totalStringX = "";
                for (Integer val : mouseXlist) {
                    totalStringX = totalStringX + val + ", ";
                }
                jTextAreaX.setText(totalStringX);

                mouseYList.add(evt.getY());
                String totalStringY = "";
                for (Integer val : mouseYList) {
                    totalStringY = totalStringY + val + ", ";
                }
                jTextAreaY.setText(totalStringY);
                repaint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent evt) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("CoordinateExtractionPanel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new CoordinateExtractionPanel());
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

}
