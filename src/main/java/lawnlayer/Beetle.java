package lawnlayer;

//import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class Beetle extends Enemy {

    public Beetle(int x, int y, PImage sprite) {
        super(x, y, sprite);
    }

    public static boolean hasBeetle(int x, int y, ArrayList<Beetle> ls) {
        for (Enemy obj: ls) {
            if (obj.getX()-obj.getX()%App.SPRITESIZE == x && obj.getY()-obj.getY()%App.SPRITESIZE == y)
                return true;
        }
        return false;
    }

    public void collideGrass() {
        for (GameObject grass: App.grass) {
            if (GameObject.isCollision(grass.getX(), grass.getY(), this.getX(), this.getY())) {
                App.grass.remove(grass);
                break;
            }
        }
    }


}