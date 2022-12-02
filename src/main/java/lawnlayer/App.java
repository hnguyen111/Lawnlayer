package lawnlayer;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;
//import processing.core.PFont;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.time.Instant;
import java.time.Duration;

public class App extends PApplet {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int TOPBAR = 80;

    public static final int FPS = 60;

    public String configPath;
    public JSONArray levels;
    public int curLevel;
    public int totalLevels;
    public int curLives;
    public int totalLives;
    public double goal;
    public int totalInitialSoil;
    public static ArrayList<GameObject> concreteTiles;
    public static ArrayList<Enemy> worms;
    public static ArrayList<Beetle> beetles;
    public static ArrayList<GameObject> grass;
    public Player player;
    public static GameObject cheese;
    public int delayTime;
    public final int POWERUPTIME = 10;
    public Instant startDelay;
    public Instant endDelay;
    public Instant startPowerUp;
    public Instant endPowerUp;
    public static boolean powerUpEnemies;
    public static boolean powerUpPlayer;
    
	
	public static PImage grassImage;
    public static PImage concreteImage;
    public static PImage wormImage;
    public static PImage beetleImage;
    public static PImage playerImage;
    public static PImage cheeseImage;

    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);        
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        frameRate(FPS);

        // Load images during setup
		grassImage = loadImage(this.getClass().getResource("grass.png").getPath());
        concreteImage = loadImage(this.getClass().getResource("concrete_tile.png").getPath());
        wormImage = loadImage(this.getClass().getResource("worm.png").getPath());
        beetleImage = loadImage(this.getClass().getResource("beetle.png").getPath());
        playerImage = loadImage(this.getClass().getResource("ball.png").getPath());
        cheeseImage = loadImage(this.getClass().getResource("cheese.png").getPath());

        JSONObject jsonObj = this.loadJSONObject(this.configPath);
        this.levels = jsonObj.getJSONArray("levels");
        this.curLevel = -1;
        this.totalLevels = levels.size();
        this.totalLives = jsonObj.getInt("lives");

        startNewLevel();
        
    }

    public void startNewLevel() {
        this.curLevel += 1;
        this.curLives = this.totalLives;

        concreteTiles = new ArrayList<GameObject>();
        worms = new ArrayList<Enemy>();
        beetles = new ArrayList<Beetle>();
        grass = new ArrayList<GameObject>();
        this.player = new Player(playerImage);

        JSONObject level = levels.getJSONObject(curLevel);
        readMapFile(level.getString("outlay"));
        this.goal = level.getDouble("goal");
        JSONArray enemies = level.getJSONArray("enemies");
        //System.out.println(this.concreteTiles.size());

        for (int i = 0 ; i < enemies.size(); i++) {
            JSONObject enemy = enemies.getJSONObject(i);
            int type = enemy.getInt("type");
            String spawn = enemy.getString("spawn");

            int x, y;
            if (spawn.equals("random")) {
                Random rand = new Random();
                x = rand.nextInt(WIDTH-3*SPRITESIZE+1) + SPRITESIZE;
                y = rand.nextInt(HEIGHT-2*SPRITESIZE-TOPBAR+1) + TOPBAR;
            }
            else {
                String[] coordinates = spawn.split(",");
                y = Integer.parseInt(coordinates[0]);
                x = Integer.parseInt(coordinates[1]);
            }

            if (type == 0) {
                worms.add(new Enemy(x, y, wormImage));
            }
            else if (type == 1) {
                beetles.add(new Beetle(x, y, beetleImage));
            }
        }

        Random rand = new Random();
        delayTime = rand.nextInt(11);
        this.startDelay = Instant.now();
        cheese = null;
        powerUpEnemies = powerUpPlayer = false;
    }

    public void readMapFile(String filename) {
        this.totalInitialSoil = 0;
        int x = 0;
        int y = TOPBAR;
        File f = new File(filename);
        try {
            Scanner scan = new Scanner(f);
            while(scan.hasNextLine()) {
                String line = scan.nextLine();
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == 'X') {
                        concreteTiles.add(new GameObject(x, y, concreteImage));
                    }
                    else
                        this.totalInitialSoil += 1;
                    x += SPRITESIZE;
                }
                x = 0;
                y += SPRITESIZE;
            }

        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void handleGameLogic() {
        if (player.hitPath()) {
            this.curLives -= 1;
            this.player = new Player(playerImage);
        }
        if (this.curLives == 0) {
            loseScreen();
        }
        if (grass.size() >= this.goal*this.totalInitialSoil) {
            if (this.curLevel + 1 < this.totalLevels) {
                startNewLevel();
            }
            else {
                winScreen();
            }
            
        }
        
        this.endDelay = Instant.now();
        Duration delayDuration = Duration.between(startDelay, endDelay);
        if (cheese == null && delayDuration.getSeconds() == delayTime && !powerUpEnemies && !powerUpPlayer) {
            respawnCheese();
        }
        else if (cheese != null && GameObject.isIncluded(cheese.getX(), cheese.getY(), grass))
            respawnCheese();
        
        if (player.checkPowerUp()) {
            this.startPowerUp = Instant.now();
        }
        if (powerUpEnemies || powerUpPlayer) {
            this.endPowerUp = Instant.now();
            Duration powerUpDuration = Duration.between(startPowerUp, endPowerUp);
            if (powerUpDuration.getSeconds() == POWERUPTIME) {
                powerUpEnemies = powerUpPlayer = false;
                this.startDelay = Instant.now();
                Random rand = new Random();
                delayTime = rand.nextInt(11);
            }
        }
    }

    public void respawnCheese() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(WIDTH-3*SPRITESIZE+1) + SPRITESIZE;
            x = x-x%SPRITESIZE;
            y = rand.nextInt(HEIGHT-2*SPRITESIZE-TOPBAR+1) + TOPBAR;
            y = y-y%SPRITESIZE;
        } while (GameObject.isIncluded(x, y, concreteTiles) || 
                GameObject.isIncluded(x, y, grass) || 
                (x == player.getX() && y == player.getX()));
        cheese = new GameObject(x, y, cheeseImage);
    }

    public void winScreen() {
        background(0);
        textAlign(CENTER);
        fill(255);
        textSize(30);
        text("You Win", WIDTH/2 - 20, HEIGHT/2);
        delay(1000);
    }

    public void loseScreen() {
        background(0);
        textAlign(CENTER);
        fill(255);
        textSize(30);
        text("Game Over", WIDTH/2 - 20, HEIGHT/2);
        delay(1000);
    }
	
    /**
     * Draw all elements in the game by current frame. 
    */
    public void draw() {

        fill(101,42,14);
        this.rect(0, 0, WIDTH, HEIGHT);       

        for (GameObject tile: concreteTiles) {
            tile.draw(this);
        }

        for (Enemy worm: worms) {
            worm.move();
            worm.draw(this);            
        }

        for (Beetle beetle: beetles) {            
            beetle.move();
            beetle.collideGrass();
            beetle.draw(this);
        }

        if (cheese != null)
            cheese.draw(this);

        for (Cell cell: player.path) {
            fill(0, 200, 0);
            this.rect(cell.x, cell.y, SPRITESIZE-2, SPRITESIZE-2);
        }

        if (!player.stop) {
            player.move();
            player.hittedByEnemy();
            if (player.redPathRange[0] >= 0) {
                int i1 = (int) Math.ceil(player.redPathRange[0]);
                int i2 = (int) Math.floor(player.redPathRange[1]);
                for (int i = i1; i < i2; i++) {
                    fill(200, 0, 0);
                    this.rect(player.path.get(i).x, player.path.get(i).y, SPRITESIZE-2, SPRITESIZE-2);
                }
                if ((int)player.propagate() == player.path.size()-1) {
                    this.curLives -= 1;
                    this.player = new Player(playerImage);
                }
            }
        }

        for (GameObject grass: grass) {
            grass.draw(this);
        }

        player.draw(this);

        textAlign(CENTER);
        fill(255);
        textSize(30);
        text("Lives:", 100, TOPBAR/2);
        text(this.curLives, 170, TOPBAR/2);
        text((int)((float)grass.size()/this.totalInitialSoil*100), WIDTH/6+150, TOPBAR/2);
        text("%/", WIDTH/6+190, TOPBAR/2);
        text((int)(this.goal*100), WIDTH/6+230, TOPBAR/2);
        text("%", WIDTH/6+260, TOPBAR/2);
        text("Level:", WIDTH-120, TOPBAR/2);
        text(this.curLevel+1, WIDTH-55, TOPBAR/2);
        if (powerUpEnemies) {
            text("Slowing Down Enemies:", (float) (WIDTH/2)+100, TOPBAR/2);
            this.endPowerUp = Instant.now();
            Duration powerUpDuration = Duration.between(startPowerUp, endPowerUp);
            text((int)(POWERUPTIME-powerUpDuration.getSeconds()), (float) (WIDTH/2)+300, TOPBAR/2);
        }
        if (powerUpPlayer) {
            text("Speeding Up Player:", (float) (WIDTH/2)+100, TOPBAR/2);
            this.endPowerUp = Instant.now();
            Duration powerUpDuration = Duration.between(startPowerUp, endPowerUp);
            text((int)(POWERUPTIME-powerUpDuration.getSeconds()), (float) (WIDTH/2)+300, TOPBAR/2);
        }

        handleGameLogic();
        
    }

    public void keyPressed() {
        if ((keyCode+player.dir == 39 || keyCode+player.dir == 41) && !player.stop) {
            return;
        }
        if (keyCode == LEFT) {
            if (player.counter == 0)
                player.dir = 0;
            else {
                player.nextDir = 0;
            }
        }
        else if (keyCode == UP) {
            if (player.counter == 0)
                player.dir = 1;
            else {
                player.nextDir = 1;               
            }
        }
        else if (keyCode == RIGHT) {
            if (player.counter == 0)
                player.dir = 2;
            else {
                player.nextDir = 2;
            }
        }
        else if (keyCode == DOWN) {
            if (player.counter == 0)
                player.dir = 3;
            else {
                player.nextDir = 3;               
            }
        }

        player.stop = false;
    }

    public void keyReleased() {
        if (player.counter != 0) {
            player.stop = false;
        }
    }


    public static void main(String[] args) {
        PApplet.main("lawnlayer.App");
    }
}
