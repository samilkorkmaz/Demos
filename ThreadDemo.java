package threaddemo;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Demonstrates how to make a thread wait and how to wake it up (notify).
 *
 * @author Samil Korkmaz
 * @date 26.12.2014
 * @license Public Domain
 */
public class ThreadDemo extends javax.swing.JFrame {

    private boolean isBusy;
    private Thread countTo10Thread;
    private final Object counterLock = new Object();

    private final javax.swing.JButton jbSleep = new javax.swing.JButton("Sleep fo 4 seconds");
    private final javax.swing.JLabel jlStartEnd = new javax.swing.JLabel("Sleep not started");
    private final javax.swing.JButton jbSayHello = new javax.swing.JButton("Say hello");
    private final javax.swing.JLabel jlHello = new javax.swing.JLabel();
    private final javax.swing.JLabel jlSleepTime = new javax.swing.JLabel("0");
    private final javax.swing.JLabel jlCounter = new javax.swing.JLabel();

    private void setBusy(boolean isBusy) {
        jbSleep.setEnabled(!isBusy);
        this.isBusy = isBusy;
        if (!isBusy) {
            synchronized (counterLock) {
                if (countTo10Thread.isAlive()) {
                    counterLock.notify(); //Note: Notify has to be called from a thread other than counterThread.
                } else {
                    startCounter();
                }
            }
        }
    }

    private class CounterThread extends Thread implements Runnable {

        @Override
        public void run() {
            try {
                while (!isBusy) {
                    for (int i = 0; i < 10; i++) {
                        if (!isBusy) {
                            jlCounter.setText(String.valueOf(i + 1));
                            Thread.sleep(400);
                        } else {
                            i--;
                            synchronized (counterLock) {
                                counterLock.wait(); //Note: wait has to be called from inside counterThread.run
                            }
                        }
                    }
                }
                jlCounter.setText("Counter thread ended, a new one will be created."); //Note: You cannot restart a stopped thread. You have to re-create it.
            } catch (InterruptedException ex) {
                Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void startCounter() {
        countTo10Thread = new CounterThread();
        countTo10Thread.start();
    }

    public ThreadDemo() {
        initComponents();
        setBounds(75, 0, 500, 300);
        setTitle("Thread wait/notify demo - Dec 2014");
        startCounter();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {
        int x = 10;
        int y = 5;
        int w = 150;
        int h = 25;
        int g = 5;

        jbSleep.setBounds(x, y, w, h);
        jlStartEnd.setBounds(jbSleep.getX(), jbSleep.getY() + jbSleep.getHeight() + g, w, h);
        jlSleepTime.setBounds(jbSleep.getX(), jlStartEnd.getY() + jlStartEnd.getHeight() + g, w, h);
        jlCounter.setBounds(jbSleep.getX(), jlSleepTime.getY() + jlSleepTime.getHeight() + g, 2 * w, h);

        jbSayHello.setBounds(jbSleep.getX() + jbSleep.getWidth() + g, y, w, h);
        jlHello.setBounds(jbSayHello.getX(), jbSayHello.getY() + jbSayHello.getHeight() + g, 2*w, h);

        setLayout(null);
        
        add(jbSleep);
        add(jlStartEnd);
        add(jbSayHello);
        add(jlHello);
        add(jlSleepTime);
        add(jlCounter);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);        

        jbSleep.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSleepActionPerformed(evt);
            }
        });

        jbSayHello.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (isBusy) {
                    jlHello.setText("Sleeping, can't say hello now.");
                } else {
                    jlHello.setText("Hello!");
                }
            }
        });

        pack();
    }// </editor-fold>                        

    private void jbSleepActionPerformed(java.awt.event.ActionEvent evt) {
        jlStartEnd.setText("Started...");//you must do this outside of sleepThread
        jlSleepTime.setText("0");
        setBusy(true); //you must do this outside of sleepThread
        Thread sleepFor4Thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 4; i++) {
                        Thread.sleep(1000);
                        jlSleepTime.setText(String.valueOf(i + 1));
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                setBusy(false); //you must do this inside of sleepThread
                jlStartEnd.setText("Ended."); //you must do this inside of sleepThread
            }
        });
        sleepFor4Thread.start();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ThreadDemo().setVisible(true);
            }
        });
    }
}
