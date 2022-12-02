package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AppTest extends App {

    @Test
    public void MyFirstTest() {
        PApplet.runSketch(new String[] {"App"}, this);
        delay(3000);
        noLoop();
        this.setup();
        this.readMapFile("NoFile"); // FileNotFoundException

        // level1.txt
        assertEquals(188, this.concreteTiles.size());
        assertEquals(1, this.beetles.size());
        assertEquals(2, this.worms.size());
        assertEquals(0, this.grass.size());
        assertEquals(125, this.beetles.get(0).getX());
        assertEquals(175, this.beetles.get(0).getY());

        Enemy e = new Enemy(80, 105, new PImage());
        assertEquals(2, e.getVel());
        this.powerUpEnemies = true;
        e.move();
        assertEquals(1, e.getVel());
        this.powerUpEnemies = false;

        this.grass.add(new GameObject(140, 170, new PImage()));
        this.grass.add(new GameObject(120, 150, new PImage()));
        this.beetles.get(0).collideGrass(); // beetle collides with grass
        assertEquals(1, this.grass.size());
        this.beetles.get(0).collideGrass(); // no collision
        assertEquals(1, this.grass.size());

        assertTrue(this.player.isOnTile());
        assertFalse(this.player.isOnGrass());

        this.handleGameLogic();
        assertEquals(3, this.curLives);

        this.respawnCheese();
        assertNotNull(this.cheese);

        this.powerUpPlayer = true;
        this.player.move();
        assertEquals(4, this.player.getVel());
        this.powerUpPlayer = false;

        assertNotNull(this.player.hittedByEnemy());
        
        
        


        

        

    }
}