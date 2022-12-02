package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class SampleTest {

    @Test
    public void gameObjectTest() {
        PImage mockPImage = new PImage();
        GameObject gameObject = new GameObject(80, 100, mockPImage);

        // Test getter
        assertEquals(80, gameObject.getX());
        assertEquals(100, gameObject.getY());

        // Test setter
        gameObject.setX(60);
        assertEquals(60, gameObject.getX());
        gameObject.setY(80);
        assertEquals(80, gameObject.getY());

        // Test get width and height of image
        assertEquals(0, gameObject.getWidth());
        assertEquals(0, gameObject.getHeight());

        App app = new App();
        //assertNotNull(app.draw());

        // Test isCollision, assume obj1 and obj2 are squares with side 20
        // obj1 is on the left of obj2
        assertFalse(GameObject.isCollision(80, 100, 110, 110));
        // Swap obj1 and obj2
        assertFalse(GameObject.isCollision(110, 110, 80, 100));
        //obj1 is on top of obj2
        assertFalse(GameObject.isCollision(80, 100, 80, 130));
        // Swap obj1 and obj2
        assertFalse(GameObject.isCollision(80, 130, 80, 100));
        // One corner of obj1 touches one corner of obj2
        assertFalse(GameObject.isCollision(80, 100, 100, 120));
        assertFalse(GameObject.isCollision(100, 120, 80, 100));
        assertFalse(GameObject.isCollision(80, 100, 60, 120));
        assertFalse(GameObject.isCollision(60, 120, 80, 100));
        // Ovelaps
        assertTrue(GameObject.isCollision(80, 100, 90, 110));
        assertTrue(GameObject.isCollision(80, 100, 100, 110));
        assertTrue(GameObject.isCollision(100, 110, 80, 100));

        // Test isIncluded
        ArrayList<GameObject> ls = new ArrayList<GameObject>();
        GameObject obj1 = new GameObject(80, 100, mockPImage);
        GameObject obj2 = new GameObject(90, 100, mockPImage);
        ls.add(obj1); ls.add(obj2);

        assertTrue(GameObject.isIncluded(80, 100, ls));
        assertFalse(GameObject.isIncluded(80, 90, ls));
        assertFalse(GameObject.isIncluded(90, 80, ls));
        assertFalse(GameObject.isIncluded(90, 90, ls));

    }

    @Test
    public void enemyTest() {
        ArrayList<Enemy> ls = new ArrayList<Enemy>();
        Enemy obj1 = new Enemy(80, 105, new PImage());
        Enemy obj2 = new Enemy(91, 127, new PImage());
        ls.add(obj1); ls.add(obj2);

        assertTrue(Enemy.hasEnemy(80, 100, ls));
        assertFalse(Enemy.hasEnemy(80, 110, ls));
        assertFalse(Enemy.hasEnemy(90, 120, ls));
        assertFalse(Enemy.hasEnemy(90, 130, ls));


        ArrayList<GameObject> arr = new ArrayList<GameObject>();
        GameObject o1 = new GameObject(80, 100, new PImage());
        GameObject o2 = new GameObject(100, 100, new PImage());
        GameObject o3 = new GameObject(120, 100, new PImage());
        GameObject o4 = new GameObject(80, 120, new PImage());
        GameObject o5 = new GameObject(80, 140, new PImage());
        GameObject o6 = new GameObject(100, 140, new PImage());
        GameObject o7 = new GameObject(120, 140, new PImage());
        GameObject o8 = new GameObject(120, 120, new PImage());
        arr.add(o1); arr.add(o2); arr.add(o3); arr.add(o4); arr.add(o5);
        // 0: upleft, 1: upright, 2: downright, 3: downleft

        /* Only check the first tile enemy collides */
        Enemy e = new Enemy(99, 125, new PImage());
        e.dir = 0; Enemy.findDirection(e, arr); // upleft to upright
        assertEquals(1, e.dir);
        e.dir = 3; Enemy.findDirection(e, arr); // downleft to downright
        assertEquals(2, e.dir);

        e = new Enemy(125, 119, new PImage());
        e.dir = 0; Enemy.findDirection(e, arr); // upleft to downleft
        assertEquals(3, e.dir);
        e.dir = 1; Enemy.findDirection(e, arr); // upright to downright
        assertEquals(2, e.dir);

        e = new Enemy(61, 125, new PImage());
        e.dir = 1; Enemy.findDirection(e, arr); // upright to upleft
        assertEquals(0, e.dir);
        e.dir = 2; Enemy.findDirection(e, arr); // downright to downleft
        assertEquals(3, e.dir);

        e = new Enemy(105, 81, new PImage());
        e.dir = 2; Enemy.findDirection(e, arr); // downright to upright
        assertEquals(1, e.dir);
        e.dir = 3; Enemy.findDirection(e, arr); // downleft to upleft
        assertEquals(0, e.dir);

        /* May check the first two tiles collide enemy collides 
        since we don't know they are horizontal or vertical*/
        e = new Enemy(99, 139, new PImage());
        e.dir = 0; Enemy.findDirection(e, arr); // upleft to upright
        assertEquals(1, e.dir);
        e.dir = 3; Enemy.findDirection(e, arr); // downleft to downright
        assertEquals(2, e.dir);

        e = new Enemy(109, 119, new PImage());
        e.dir = 0; Enemy.findDirection(e, arr); // upleft to downleft
        assertEquals(3, e.dir);
        e.dir = 1; Enemy.findDirection(e, arr); // upright to downright
        assertEquals(2, e.dir);

        e = new Enemy(61, 139, new PImage());
        e.dir = 1; Enemy.findDirection(e, arr); // upright to upleft
        assertEquals(0, e.dir);
        e.dir = 2; Enemy.findDirection(e, arr); // downright to downleft
        assertEquals(3, e.dir);

        e = new Enemy(121, 81, new PImage());
        e.dir = 2; Enemy.findDirection(e, arr); // downright to upright
        assertEquals(1, e.dir);
        e.dir = 3; Enemy.findDirection(e, arr); // downleft to upleft
        assertEquals(0, e.dir);

        e = new Enemy(119, 81, new PImage());
        e.dir = 2; Enemy.findDirection(e, arr); // downright to upright
        assertEquals(1, e.dir);
        e.dir = 3; Enemy.findDirection(e, arr); // downleft to upleft
        assertEquals(0, e.dir);

        e = new Enemy(61, 81, new PImage());
        e.dir = 2; Enemy.findDirection(e, arr); // opposite direction
        assertEquals(0, e.dir);

    }

    @Test
    public void beetleTest() {
        ArrayList<Beetle> ls = new ArrayList<Beetle>();
        Beetle obj1 = new Beetle(100, 125, new PImage());
        Beetle obj2 = new Beetle(111, 147, new PImage());
        ls.add(obj1); ls.add(obj2);

        assertTrue(Beetle.hasBeetle(100, 120, ls));
        assertFalse(Beetle.hasBeetle(100, 130, ls));
        assertFalse(Beetle.hasBeetle(110, 140, ls));
        assertFalse(Beetle.hasBeetle(110, 150, ls));

        //obj1.collideGrass();
        
    }

    @Test
    public void cellTest() {
        ArrayList<Cell> ls = new ArrayList<Cell>();
        Cell c1 = new Cell(80, 100);
        Cell c2 = new Cell(90, 100);
        ls.add(c1); ls.add(c2);

        assertTrue(Cell.isIncluded(80, 100, ls));
        assertFalse(Cell.isIncluded(80, 90, ls));
        assertFalse(Cell.isIncluded(90, 80, ls));
        assertFalse(Cell.isIncluded(90, 90, ls));
    }

    @Test
    public void playerTest() {
        Player player = new Player(new PImage());
        assertEquals(2, player.getVel());
        
        Cell c1 = new Cell(100, 120);
        Cell c2 = new Cell(120, 120);
        Cell c3 = new Cell(140, 120);
        Cell c4 = new Cell(140, 140);
        Cell c5 = new Cell(120, 140);
        player.path.add(c1); player.path.add(c2);
        player.path.add(c3); player.path.add(c4);
        player.path.add(c5);
        player.setX(100); player.setY(140);
        assertFalse(player.hitPath());
        player.setX(120); player.setY(120);
        assertTrue(player.hitPath());


        player.redPathRange[0] = player.redPathRange[1] = 3;
        for (int i = 0; i < 9; i++)
            player.propagate();
        assertEquals(0, (int)player.redPathRange[0]);
        assertEquals(6, (int)player.redPathRange[1]);
        player.propagate();
        assertEquals(0, (int)player.redPathRange[0]);

        
    }
}
