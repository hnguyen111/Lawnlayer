package lawnlayer;

//import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Random;

public class Player extends GameObject {

    private int vel;
    protected int dir; // 0: left, 1: up, 2: right, 3: down
    protected int nextDir;
    protected int counter;
    protected boolean stop;
    protected ArrayList<Cell> path;
    protected float[] redPathRange = {-1, -1};
    private static final float PROPAGATIONRATE = (float)1/3;
    private final int NORMALVEL = 2;
    private final int POWERUPVEL = 4; 

    public Player(PImage sprite) {
        super(0, App.TOPBAR, sprite);

        this.vel = NORMALVEL;
        this.counter = 0;
        this.dir = -1;
        this.nextDir = -1;
        this.stop = true;
        this.path = new ArrayList<Cell>();
    }

    public int getVel() {
        return this.vel;
    }

    public boolean isOnTile() {
        return GameObject.isIncluded(this.getX(), this.getY(), App.concreteTiles);
    }

    public boolean isOnGrass() {
        return GameObject.isIncluded(this.getX(), this.getY(), App.grass);
    }

    public boolean hitPath() {
        for (int i = 0; i < this.path.size()-2; i++) {
            Cell c = this.path.get(i);
            if (c.x == this.getX() && c.y == this.getY())
                return true;
        }
        return false;
    }

    public boolean hittedByEnemy() {
        for (Enemy worm: App.worms) {
            for (Cell c: this.path)
                if (GameObject.isCollision(worm.getX(), worm.getY(), c.x, c.y)) {
                    this.redPathRange[0] = this.redPathRange[1] = this.path.indexOf(c);
                    return true;
                }
        }

        for (Beetle beetle: App.beetles) {
            for (Cell c: this.path)
                if (GameObject.isCollision(beetle.getX(), beetle.getY(), c.x, c.y)) {
                    this.redPathRange[0] = this.redPathRange[1] = this.path.indexOf(c);
                    return false;
                }
        }
        return false;
    }

    public float propagate() {
        if (this.redPathRange[0] >= PROPAGATIONRATE)
            this.redPathRange[0] -= PROPAGATIONRATE;
        this.redPathRange[1] += PROPAGATIONRATE;
        return this.redPathRange[1];
    }

    public boolean checkPowerUp() {
        if (App.cheese == null)
            return false;
        if (this.getX() == App.cheese.getX() && this.getY() == App.cheese.getY()) {
            Random rand = new Random();
            int powerUpType = rand.nextInt(2);
            if (powerUpType == 0)
                App.powerUpEnemies = true;
            else
                App.powerUpPlayer = true;
            App.cheese = null;
            return true;
        }
        return false;
    }

    public void move() {
        
        if (App.powerUpPlayer) {
            this.vel = POWERUPVEL;
        }
        else {
            this.vel = NORMALVEL;
        }

        if (this.nextDir != -1  && this.counter == 0) {
            this.dir = this.nextDir;
            this.nextDir = -1;
            this.stop = false;
        }

        if (this.counter < App.SPRITESIZE && !this.stop) {
            if (this.dir == 0)
                this.setX(this.getX()-this.vel);
            else if (this.dir == 2)
                this.setX(this.getX()+this.vel);
            else if (this.dir == 1)
                this.setY(this.getY()-this.vel);
            else if (this.dir == 3)
                this.setY(this.getY()+this.vel);
            
            if (this.getX() < 0)
                this.setX(0);
            else if (this.getX() > App.WIDTH-App.SPRITESIZE)
                this.setX(App.WIDTH-App.SPRITESIZE);
            if (this.getY() < App.TOPBAR)
                this.setY(App.TOPBAR);
            else if (this.getY() > App.HEIGHT-App.SPRITESIZE)
                this.setY(App.HEIGHT-App.SPRITESIZE);
            
            this.counter += vel;
        }

        if (this.counter == App.SPRITESIZE) {
            this.counter = 0;
            if (isOnTile() || isOnGrass()) {
                if (isOnTile()) {
                    this.dir = -1;
                    this.stop = true;
                }

                redPathRange[0] = redPathRange[1] = -1;

                if (!this.path.isEmpty()) {
                    ArrayList<Cell> cellsToBeGrass = new ArrayList<Cell>();
                    Cell cell = this.path.get(this.path.size()-1);
                    Cell lcell = new Cell(cell.x-App.SPRITESIZE, cell.y);
                    Cell rcell = new Cell(cell.x+App.SPRITESIZE, cell.y);
                    Cell ucell = new Cell(cell.x, cell.y-App.SPRITESIZE);
                    Cell dcell = new Cell(cell.x, cell.y+App.SPRITESIZE);

                    fillGrass(lcell, cellsToBeGrass, App.concreteTiles, this.path, App.grass, App.worms, App.beetles);
                    fillGrass(rcell, cellsToBeGrass, App.concreteTiles, this.path, App.grass, App.worms, App.beetles);
                    fillGrass(ucell, cellsToBeGrass, App.concreteTiles, this.path, App.grass, App.worms, App.beetles);
                    fillGrass(dcell, cellsToBeGrass, App.concreteTiles, this.path, App.grass, App.worms, App.beetles);
                    
                    for (Cell c: this.path) {
                        App.grass.add(new GameObject(c.x, c.y, App.grassImage));
                    }
                }
                
                this.path.clear();
            }
            else
                this.path.add(new Cell(this.getX(), this.getY()));
        }       
    }


    public int findDirtToFill(Cell cell, ArrayList<Cell> result, ArrayList<GameObject> concreteTiles, ArrayList<Cell> path, 
            ArrayList<GameObject> grass, ArrayList<Enemy> worms, ArrayList<Beetle> beetles) {
        
        if (Cell.isIncluded(cell.x, cell.y, result))
            return 0;
        
        if (GameObject.isIncluded(cell.x, cell.y, concreteTiles) ||
            Cell.isIncluded(cell.x, cell.y, path) ||
            GameObject.isIncluded(cell.x, cell.y, grass))
            return 0;
        
        if (Enemy.hasEnemy(cell.x, cell.y, worms) ||
            Beetle.hasBeetle(cell.x, cell.y, beetles)) {
            if (!result.isEmpty())
                result.clear();
            return -1;
        }

        result.add(cell);
    
        Cell l = new Cell(cell.x-App.SPRITESIZE, cell.y);
        Cell r = new Cell(cell.x+App.SPRITESIZE, cell.y);
        Cell u1 = new Cell(cell.x-App.SPRITESIZE, cell.y-App.SPRITESIZE);
        Cell u2 = new Cell(cell.x, cell.y-App.SPRITESIZE);
        Cell u3 = new Cell(cell.x+App.SPRITESIZE, cell.y-App.SPRITESIZE);
        Cell d1 = new Cell(cell.x-App.SPRITESIZE, cell.y+App.SPRITESIZE);
        Cell d2 = new Cell(cell.x, cell.y+App.SPRITESIZE);
        Cell d3 = new Cell(cell.x+App.SPRITESIZE, cell.y+App.SPRITESIZE);

        if (findDirtToFill(l, result, concreteTiles, path, grass, worms, beetles) == -1||
            findDirtToFill(r, result, concreteTiles, path, grass, worms, beetles) == -1 ||
            findDirtToFill(u1, result, concreteTiles, path, grass, worms, beetles) == -1||
            findDirtToFill(u2, result, concreteTiles, path, grass, worms, beetles) == -1||
            findDirtToFill(u3, result, concreteTiles, path, grass, worms, beetles) == -1||
            findDirtToFill(d1, result, concreteTiles, path, grass, worms, beetles) == -1||
            findDirtToFill(d2, result, concreteTiles, path, grass, worms, beetles) == -1||
            findDirtToFill(d3, result, concreteTiles, path, grass, worms, beetles) == -1)
            return -1;
        
        return 1;

    }

    public void fillGrass(Cell cell, ArrayList<Cell> cellsToBeGrass, ArrayList<GameObject> concreteTiles, ArrayList<Cell> path, 
            ArrayList<GameObject> grass, ArrayList<Enemy> worms, ArrayList<Beetle> beetles) {
        findDirtToFill(cell, cellsToBeGrass, App.concreteTiles, this.path, App.grass, App.worms, App.beetles);
        if (!cellsToBeGrass.isEmpty()) {
            for (Cell c: cellsToBeGrass) {
                App.grass.add(new GameObject(c.x, c.y, App.grassImage));
            }
            cellsToBeGrass.clear();
        }
    }



}

class Cell {
    public int x;
    public int y;
    
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static boolean isIncluded(int x, int y, ArrayList<Cell> ls) {
        for (Cell obj: ls) {
            if (obj.x == x && obj.y == y)
                return true;
        }
        return false;
    }
}