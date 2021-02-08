/*
 * File:    ParticleState.java
 * Package:
 * Author:  Zachary Gill
 */

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines a configuration of particles.
 */
public class ParticleState {
    
    //Fields
    
    /**
     * The list of Particles in the state.
     */
    private final List<Particle> particles = new ArrayList<>();
    
    /**
     * The reactive radius of Particles in the state.
     */
    private double reactiveRadius;
    
    
    //Constructors
    
    /**
     * The constructor for a Particle State.
     *
     * @param particleCount  The number of Particles.
     * @param centerCount    The number of additional Particles to start in the center of the screen.
     * @param size           The size of a Particle.
     * @param velocity       The velocity of the Particles in the state.
     * @param rotation       The intrinsic rotation of the Particles in the state.
     * @param turn           The turn of the Particles in the state.
     * @param reactiveRadius The reactive radius of Particles in the state.
     */
    public ParticleState(int particleCount, int centerCount, double size, double velocity, double rotation, double turn, double reactiveRadius) {
        this.reactiveRadius = size * reactiveRadius;
        
        for (int i = 0; i < particleCount; i++) {
            particles.add(new Particle(this, getRandomPosition(), getRandomOrientation(), size, velocity, rotation, turn));
        }
        
        for (int i = 0; i < centerCount; i++) {
            particles.add(new Particle(this, getCenterPosition(), getRandomOrientation(), size, velocity, rotation, turn));
        }
    }
    
    
    //Methods
    
    /**
     * Performs a time step.
     */
    public void step() {
        for (Particle p : particles) {
            p.step();
        }
    }
    
    /**
     * Performs a move.
     */
    public void move() {
        for (Particle p : particles) {
            p.move();
        }
    }
    
    /**
     * Renders the Particle State.
     *
     * @param g The render screen.
     */
    public void render(Graphics2D g) {
        for (Particle p : particles) {
            p.render(g);
        }
    }
    
    /**
     * Returns the number of neighbors of a Particle.
     *
     * @return The number of neighbors of a Particle.
     */
    public NeighborState getNeighbors(Particle particle) {
        NeighborState neighborState = new NeighborState();
        for (Particle p : particles) {
            if (p.equals(particle)) {
                continue;
            }
            if (Math.sqrt(Math.pow(p.getPosition().x - particle.getPosition().x, 2) + Math.pow(p.getPosition().y - particle.getPosition().y, 2)) <= reactiveRadius) {
                neighborState.count++;
                if (findSide(particle, p) > 0) {
                    neighborState.left++;
                } else {
                    neighborState.right++;
                }
            }
        }
        return neighborState;
    }
    
    /**
     * Determines the side of the orientation that a neighbor is on.
     *
     * @param particle The Particle.
     * @param neighbor The neighbor.
     * @return 1 if the neighbor is on the left, -1 if it is on the right, 0 if it is on the orientation.
     */
    private int findSide(Particle particle, Particle neighbor) {
        Point pa = new Point();
        pa.x = particle.getHeading().x * -100;
        pa.y = particle.getHeading().y * -100;
        Point pb = new Point();
        pb.x = particle.getHeading().x * 100;
        pb.y = particle.getHeading().y * 100;
        Point pc = neighbor.getPosition();
        
        if (pb.x - pa.x < 0.0000001) { // vertical line
            if (pc.x < pb.x) {
                return pb.y > pa.y ? 1 : -1;
            }
            if (pc.x > pb.x) {
                return pb.y > pa.y ? -1 : 1;
            }
            return 0;
        }
        if (pb.y - pa.y < 0.0000001) { // horizontal line
            if (pc.y < pb.y) {
                return pb.x > pa.x ? -1 : 1;
            }
            if (pc.y > pb.y) {
                return pb.x > pa.x ? 1 : -1;
            }
            return 0;
        }
        
        double slope = (pb.y - pa.y) / (pb.x - pa.x);
        double yIntercept = pa.y - pa.x * slope;
        double cSolution = (slope * pc.x) + yIntercept;
        
        if (slope != 0) {
            if (pc.y > cSolution) {
                return pb.x > pa.x ? 1 : -1;
            }
            if (pc.y < cSolution) {
                return pb.x > pa.x ? -1 : 1;
            }
            return 0;
        }
        return 0;
    }
    
    /**
     * Returns a random position on the screen.
     *
     * @return A random position on the screen.
     */
    private Point getRandomPosition() {
        Point p = new Point();
        p.x = Math.random() * PrimordialParticleSystem.screenX;
        p.y = Math.random() * PrimordialParticleSystem.screenY;
        return p;
    }
    
    /**
     * Returns a random orientation for a Particle.
     *
     * @return A random orientation for a Particle.
     */
    private double getRandomOrientation() {
        return Math.random() * Math.PI * 2;
    }
    
    /**
     * Returns the center position on the screen.
     *
     * @return The center position on the screen.
     */
    private Point getCenterPosition() {
        Point p = new Point();
        p.x = PrimordialParticleSystem.screenX / 2.0;
        p.y = PrimordialParticleSystem.screenY / 2.0;
        return p;
    }
    
}
