/*
 * File:    PrimordialParticleSystem.java
 * Package:
 * Author:  Zachary Gill
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * The renderer for the Primordial Particle System.
 */
public class PrimordialParticleSystem {
    
    
    //Static Fields
    
    /**
     * The Frame of the Window.
     */
    public static JFrame frame;
    
    /**
     * The x dimension of the Window.
     */
    public static final int screenX = 2560;
    
    /**
     * The y dimension of the Window.
     */
    public static final int screenY = 1440;
    
    /**
     * The number of frames to render per second.
     */
    public static final int FPS = 1;
    
    /**
     * The particle state.
     */
    public static ParticleState particles;
    
    
    //Main Method
    
    /**
     * The main method of of the program.
     *
     * @param args Arguments to the main method.
     */
    public static void main(String[] args) {
        particles = new ParticleState(1000, 0, 10.0, 0.67, Math.PI, 17.0 * (Math.PI / 180.0), 5.0);
        
        setupRenderer();
    }
    
    /**
     * Sets up the renderer.
     */
    private static void setupRenderer() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());
        frame.setFocusable(true);
        frame.setFocusTraversalKeysEnabled(false);
        
        JPanel renderPanel = new JPanel() {
            
            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                
                particles.step();
                particles.move();
                particles.render(g2);
                
                g2.drawImage(img, 0, 0, null);
            }
        };
        pane.add(renderPanel, BorderLayout.CENTER);
        
        frame.setSize(screenX, screenY);
        frame.setVisible(true);
        
        Timer renderTimer = new Timer();
        renderTimer.scheduleAtFixedRate(new TimerTask() {
            private AtomicBoolean rendering = new AtomicBoolean(false);
            
            @Override
            public void run() {
                if (rendering.compareAndSet(false, true)) {
                    renderPanel.repaint();
                    rendering.set(false);
                }
            }
        }, 0, 1000 / FPS);
    }
    
}
