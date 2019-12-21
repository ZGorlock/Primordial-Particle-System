/*
 * File:    Particle.java
 * Package: 
 * Author:  Zachary Gill
 */

import java.awt.Color;
import java.awt.Graphics2D;

import javafx.scene.shape.Circle;

/**
 * Defines a particle.
 */
public class Particle {
    
    //Fields
    
    /**
     * The position of the Particle.
     */
    private Point position;
    
    /**
     * The orientation of the Particle.
     */
    private double orientation;
    
    /**
     * The heading of the Particle.
     */
    private Point heading = new Point();
    
    /**
     * The size of the Particle.
     */
    private double size;
    
    /**
     * The velocity of the Particle.
     */
    private double velocity;
    
    /**
     * The intrinsic rotation of the Particle.
     */
    private double rotation;
    
    /**
     * The turn of the Particle.
     */
    private double turn;
    
    /**
     * The state of neighbors near the Particle.
     */
    private NeighborState neighbors = new NeighborState();
    
    /**
     * The color of the Particle.
     */
    private Color color;
    
    /**
     * The Particle State of the Particle.
     */
    private ParticleState state;
    
    
    //Constructor
    
    /**
     * The constructor for a Particle.
     * 
     * @param state       The Particle State of the Particle.
     * @param position    The position of the Particle.
     * @param orientation The orientation of the Particle.
     * @param size        The size of the Particle.
     * @param velocity    The velocity of the Particle.
     * @param rotation    The intrinsic rotation of the Particle.
     * @param turn        The turn of the Particle.
     */
    public Particle(ParticleState state, Point position, double orientation, double size, double velocity, double rotation, double turn) {
        this.state = state;
        this.position = position;
        this.orientation = orientation;
        this.size = size;
        this.velocity = velocity * size;
        this.rotation = rotation;
        this.turn = turn;
    
        heading.x = Math.cos(orientation);
        heading.y = Math.sin(orientation);
    }
    
    
    //Methods
    
    /**
     * Performs a time step.
     */
    public void step() {
        neighbors = state.getNeighbors(this);
        
        orientation += rotation + (((neighbors.right - neighbors.left < 0) ? -1 : 1) * turn * neighbors.count);
//        orientation %= (Math.PI * 2);
        
        heading.x = Math.cos(orientation);
        heading.y = Math.sin(orientation);
    }
    
    /**
     * Performs a move.
     */
    public void move() {
        position.x += heading.x * velocity;
        position.y += heading.y * velocity;
    }
    
    /**
     * Renders the Particle.
     *
     * @param g The render screen.
     */
    public void render(Graphics2D g) {
        calculateColor();
        
        g.setColor(color);
        g.fillArc((int) position.x, (int) position.y, (int) size, (int) size, 0, 360);
        
        g.drawLine((int) position.x, (int) position.y, (int) (position.x + (heading.x * velocity)), (int) (position.y + (heading.y * velocity)));
    }
    
    /**
     * Calculates the color of the Particle.
     */
    private void calculateColor() {
        if (neighbors.count > 25) {
            color = Color.RED;
        } else if (neighbors.count > 15) {
            color = Color.MAGENTA;
        } else if (neighbors.count > 7) {
            color = Color.BLUE;
        } else if (neighbors.count > 3) {
            color = Color.YELLOW;
        } else {
            color = Color.GREEN;
        }
    }
    
    
    //Getters
    
    /**
     * Returns the position of the Particle.
     * 
     * @return The position of the Particle.
     */
    public Point getPosition() {
        return position;
    }
    
    /**
     * Returns the heading of the Particle.
     * 
     * @return The heading of the Particle.
     */
    public Point getHeading() {
        return heading;
    }
    
}
