package lawnlayer;

import processing.core.PImage;
import processing.core.PApplet;

import java.util.ArrayList;

/**
 * Represents an GameObject that is inherited by Enemy and Player.
 */
public class GameObject {
    
    /**
     * The GameObject's x-coordinate.
     */
    private int x;
    
    /**
     * The GameObject's y-coordinate.
     */
    private int y;

    /**
     * The GameObject's sprite.
     */
    protected PImage sprite;


    /**
     * Creates a new GameObject object.
     * 
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param sprite The image
     */
    public GameObject(int x, int y, PImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    /**
     * Draws the GameObject to the screen.
     * 
     * @param app The window to draw onto.
     */
    public void draw(PApplet app) {
        // The image() method is used to draw PImages onto the screen.
        // The first argument is the image, the second and third arguments are coordinates
        app.image(this.sprite, this.x, this.y);
    }

    /**
     * Gets the x-coordinate.
     * @return The x-coordinate.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Gets the y-coordinate.
     * @return The y-coordinate.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Gets the width of image.
     * @return The width of image.
     */
    public int getWidth() {
        return this.sprite.width;
    }

    /**
     * Gets the height of image.
     * @return The height of image.
     */
    public int getHeight() {
        return this.sprite.height;
    }

    /**
     * Sets the x-coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Check if two objects collide.
     * @return true if they collide, else false
     */
    public static boolean isCollision(int x1, int y1, int x2, int y2) {
        int size = App.SPRITESIZE;
        if (x1 + size < x2 ||
            x1 > x2 + size ||
            y1 + size < y2 ||
            y1 > y2 + size ||
            (x1 + size == x2 && y1 + size == y2) ||
            (x1 == x2 + size && y1 == y2 + size) ||
            (x1 == x2 + size && y1 + size == y2) ||
            (x1 + size == x2 && y1 == y2 + size))
            return false;

        return true;
    }

    /**
     * Check if an object is included in a list of GameObjects.
     * @param x The x-coordinate of the object.
     * @param y The y-coordinate of the object.
     * @param ls The list of GameObjects.
     * @return true if it is included, else false
     */
    public static boolean isIncluded(int x, int y, ArrayList<GameObject> ls) {
        for (GameObject obj: ls) {
            if (obj.x == x && obj.y == y)
                return true;
        }
        return false;
    }
}
