package lawnlayer;

//import processing.core.PApplet;
import processing.core.PImage;

import java.util.Random;
import java.util.ArrayList;

public class Enemy extends GameObject {

    private final int NORMALVEL = 2;
    private final int POWERUPVEL = 1; 
    private int vel;
    protected int dir;

    public Enemy(int x, int y, PImage sprite) {
        super(x, y, sprite);

        this.vel = NORMALVEL;

        Random rand = new Random();
        // 0: upleft, 1: upright, 2: downright, 3: downleft
        this.dir = rand.nextInt(4);
    }

    /**
     * Gets the velocity.
     * @return The velocity.
     */
    public int getVel() {
        return this.vel;
    }
    
    /**
     * Check if an object ovelapp an object in a list of Enemy.
     * @param x The x-coordinate of the object.
     * @param y The y-coordinate of the object.
     * @param ls The list of Enemy.
     * @return true if they overlap, else false
     */
    public static boolean hasEnemy(int x, int y, ArrayList<Enemy> ls) {
        for (Enemy obj: ls) {
            if (obj.getX()-obj.getX()%App.SPRITESIZE == x && obj.getY()-obj.getY()%App.SPRITESIZE == y)
                return true;
        }
        return false;
    }

    /**
     * Make movement for an enemy.
     */
    public void move() {

        if (App.powerUpEnemies) {
            this.vel = POWERUPVEL;
        }
        else {
            this.vel = NORMALVEL;
        }
        
        int curDir = this.dir;
        findDirection(this, App.concreteTiles);
        if (this.dir == curDir) // dont collide with concrete tiles
            findDirection(this, App.grass);
        
        if (this.dir == 0 || this.dir == 1)
            this.setY(this.getY()-this.vel);
        else 
            this.setY(this.getY()+this.vel);

        if (this.dir == 0 || this.dir == 3)
            this.setX(this.getX()-this.vel);
        else
            this.setX(this.getX()+this.vel);
        
    }

    /**
     * Find the next direction of an enemy if it changes its direction.
     * @param e The enemy.
     * @param ls The list of GameObject to be checked if there is a collision.
     */
    public static void findDirection(Enemy e, ArrayList<GameObject> ls) {
        boolean collide = false;

        for (GameObject cell: ls) {
            if (GameObject.isCollision(cell.getX(), cell.getY(), e.getX(), e.getY())) {
                if (e.dir == 0) {
                    if ((cell.getX()+cell.getWidth()-e.getX()) > (cell.getY()+cell.getHeight() - e.getY())) {
                        e.dir = 3;
                        collide = false;
                    }
                    else if ((cell.getX()+cell.getWidth()-e.getX()) < (cell.getY()+cell.getHeight() - e.getY())) {
                        e.dir = 1;
                        collide = false;
                    }
                    else {
                        collide = true;
                        continue;
                    }
                }
                else if (e.dir == 1) {
                    if ((e.getX()+e.getWidth()-cell.getX()) > (cell.getY()+cell.getHeight() - e.getY())) {
                        e.dir = 2;
                        collide = false;
                    }
                    else if ((e.getX()+e.getWidth()-cell.getX()) < (cell.getY()+cell.getHeight() - e.getY())) {
                        e.dir = 0;
                        collide = false;
                    }
                    else {
                        collide = true;
                        continue;
                    }
                }
                else if (e.dir == 2) {
                    if ((e.getX()+e.getWidth()-cell.getX()) > (e.getY()+e.getHeight()-cell.getY())) {
                        e.dir = 1;
                        collide = false;
                    }
                    else if ((e.getX()+e.getWidth()-cell.getX()) < (e.getY()+e.getHeight()-cell.getY())) {
                        e.dir = 3;
                        collide = false;
                    }
                    else {
                        collide = true;
                        continue;
                    }
                }
                else {
                    if ((cell.getX()+cell.getWidth()-e.getX()) > (e.getY()+e.getHeight()-cell.getY())) {
                        e.dir = 0;
                        collide = false;
                    }
                    else if ((cell.getX()+cell.getWidth()-e.getX()) < (e.getY()+e.getHeight()-cell.getY())) {
                        e.dir = 2;
                        collide = false;
                    }
                    else {
                        collide = true;
                        continue;
                    }
                }
                break;
            }
        }
        if (collide)
            e.dir = Math.abs(2-e.dir);
    }
}