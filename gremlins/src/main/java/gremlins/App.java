package gremlins;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.data.JSONArray;


import java.util.Random;
import java.io.*;
import java.util.*;


public class App extends PApplet {

    //DISPLAY
    public static final int WIDTH = 720;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int BOTTOMBAR = 60;

    public static final int FPS = 60;
    public static PFont font;

    public boolean userWin = false;
    public boolean gameOver = false;
    public int bufferTimer;
    public static boolean frozenDisplay = false;
    public static boolean powerupAvailable = false;

    public static int count = 10;

    //JSON
    public JSONObject config;
    public JSONArray JSONLevels;
    public Level[] levels;
    public int currentLevel;
    public JSONObject JSONlives;
    public String configPath;

    public int lives;

    public Map currentMap;
    
    //MAPS
    public static final int MAPWIDTH = 36;
    public static final int MAPHEIGHT = 33;

    public Powerbar powerbar;

    public PImage brickwall;
    public static PImage stonewall;
    public static PImage gremlinImage;
    public PImage slime;
    public static PImage fireballImage;
    public PImage door;
    public static PImage snowflake;
    public static PImage iceblock;
    public static PImage frozenGremlin;
    public static PImage specialFire;

    public PImage gameOverScreen;
    public PImage winScreen;

    //CHARACTERS//
    //SPEEDS//
    public static final int WIZARDSPEED = 2;
    public static final int GREMLINSPEED = 1;
    public static final int FIREBALLSPEED = 4;
    public static final int SLIMEBALLSPEED = 4;

    //Wizard
    public static Wizard wizard;
    public static int wizardLives = 3;
    

    public static final int WIZARDVERSIONS = 4;
    public static PImage[] wizardSprites;

    //Gremlins
    public static ArrayList<Gremlin> gremlins;
    public static ArrayList<Powerup> powerups;
    
    public static final Random randomGenerator = new Random();

    //Fireball
    
    //public Fireball fireball;

    //Bricks
    public static PImage[] brickImages;
    public static int BRICKVERSIONS = 4;

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
     * Set up the current level by reading the map and finiding where the wizard should start. Initialise the game elements such as the wizard, gremlins as well as the snowflake powerups.
    */
    public void setUpMap(int currentLevel) {
        this.currentMap = new Map(MAPWIDTH, MAPHEIGHT, levels[currentLevel], this.brickwall, this.brickImages, this.stonewall, this.gremlinImage, this.slime, this.door, this.snowflake);
        
        this.currentMap.readMap();
        this.currentMap.startWizard();
        wizard = new Wizard(this.currentMap.wizardStartingX, this.currentMap.wizardStartingY, WIZARDSPEED, wizardSprites[1], currentMap, levels[currentLevel], fireballImage);
        gremlins = currentMap.makeGremlins();
        this.powerbar = new Powerbar(levels[this.currentLevel].wizardCooldown, levels[this.currentLevel], this.wizard);
        powerups = currentMap.loadPowerups(levels[this.currentLevel].powerupSymbol);
    }

    /** 
     * Set up the background, current level and FPS
     * Read the relevant config file and create a Level array to store all levels
     * Create font for display and load all images
     * Call setUpMap() in order to initialise the first level
    */

    public void setup() {
        frameRate(FPS);
        background(216, 178, 129);
        this.currentLevel = 0;

        //Get data from JSON config
        config = loadJSONObject(new File(this.configPath));
        JSONLevels = config.getJSONArray("levels");
        
        levels = new Level[JSONLevels.size()];

        //iterate through config to make a level object for each level
        for (int i = 0; i < JSONLevels.size(); i++) {
            JSONObject JSONLevel = JSONLevels.getJSONObject(i);
            levels[i] = new Level(
                i+1,
                JSONLevel.getString("layout"),
                JSONLevel.getDouble("wizard_cooldown"),
                JSONLevel.getDouble("enemy_cooldown"),
                JSONLevel.getString("powerup_symbol"),
                JSONLevel.getDouble("special_cooldown")
            );
        }

        font = createFont("Arial", 16, true);
        this.gameOverScreen = loadImage(this.getClass().getResource("gameoverscreen.png").getPath().replace("%20", ""));
        this.winScreen =  loadImage(this.getClass().getResource("winscreen.png").getPath().replace("%20", ""));
        
        // Load images during setup
        stonewall = loadImage(this.getClass().getResource("stonewall.png").getPath().replace("%20", ""));
        this.brickwall = loadImage(this.getClass().getResource("brickwall.png").getPath().replace("%20", ""));
        gremlinImage = loadImage(this.getClass().getResource("gremlin.png").getPath().replace("%20", ""));
        this.slime = loadImage(this.getClass().getResource("slime.png").getPath().replace("%20", ""));
        fireballImage = loadImage(this.getClass().getResource("fireball.png").getPath().replace("%20", ""));
        this.door = loadImage(this.getClass().getResource("door.png").getPath().replace("%20", ""));
        this.door.resize(SPRITESIZE-4, SPRITESIZE);
        snowflake = loadImage(this.getClass().getResource("snowflake.png").getPath().replace("%20", ""));
        snowflake.resize(SPRITESIZE-2, SPRITESIZE-2);

        iceblock = loadImage(this.getClass().getResource("iceblock.png").getPath().replace("%20", ""));
        frozenGremlin = loadImage(this.getClass().getResource("frozengremlin.png").getPath().replace("%20", ""));
        specialFire = loadImage(this.getClass().getResource("bluefireball.png").getPath().replace("%20", ""));
        
        //Load all wizard images
        wizardSprites = new PImage[WIZARDVERSIONS];
        for (int i = 0; i < WIZARDVERSIONS; i++) {
            wizardSprites[i] = loadImage(this.getClass().getResource("wizard" + i + ".png").getPath().replace("%20", ""));
        }

        //load all brick images
        brickImages = new PImage[BRICKVERSIONS];
        for (int i = 0; i < BRICKVERSIONS; i++) {
            brickImages[i] = loadImage(this.getClass().getResource("brickwall_destroyed" + i + ".png").getPath().replace("%20", ""));
        }
        
        setUpMap(currentLevel);
      
    }

    /**
     * Receive key pressed signal from the keyboard.
     * If an arrow key is pressed, the wizard will change direction accoordingly
     * If a space key is pressed, a fireball will be shot if it is available
     * If the 'A' key is presed, a special fireball will be shot if it is available
     * 
    */
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        //RIGHT
        if (key == 39) {
            wizard.requestedStop = false;
            wizard.requestRight();
        }
        //LEFT
        if (key == 37) {
            wizard.requestedStop = false;
            wizard.requestLeft();
        }
        //UP
        if (key == 38) {
            wizard.requestedStop = false;
            wizard.requestUp();
        } 
        //DOWN
        if (key == 40) {
            wizard.requestedStop = false;
            wizard.requestDown();
        }

        if (key == 32) {

            if (wizard.fireballTimer == 0) {
                wizard.startingTimer = true;
                wizard.requestFireball();
            }
        }

        if (key == 65) {
            if (wizard.specialTimer == 0) {
                wizard.startingSpecialTimer = true;
                wizard.requestSpecialFireball();
            }
            
        }
    }
    
    /**
     * Receive key released signal from the keyboard.
     * If key is released, request wizard to stop (won't stop until wizard is in a tile)
    */
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        if (key == 37 || key == 38 || key == 39 || key == 40) {
            wizard.requestedStop = true;  
        }
    }

    /**
     * Keep track of whether the game is over or if the user has won (completed all levels)
    */
    public void updateGameStatus(){
        if (wizardLives == 0) {
            gameOver = true;
        } 
        if (this.currentLevel >= levels.length) {
            userWin = true;
        }
    }

    /**
     * Create a buffer of 1 second before the game can be restarted (to prevent instant restart if key is still being pressed)
     * If any key is pressed after the buffer, the game resets to level 1 and the map is set up again
    */

    public void restartGame() {
        bufferTimer++;
        if (bufferTimer> FPS) {
            if (keyPressed) {
                gameOver = false;
                userWin = false;
                this.currentLevel = 0;
                setUpMap(this.currentLevel);
                wizardLives = 3;
                bufferTimer = 0;
            } 
        } 
    }

    /**
     * Move to next level incrementing the current level by 1
    */
    public void moveToNextLevel() {
        this.currentLevel ++;
       
    }
    
    /**
     * If the game is not over and the user has not won, draw beige background
     * If the frozen powerup is activated, make the background of the map light blue
     * Draw all elements in the game by current frame. 
     * Call all tick and draw functions for all game objects such as the wizard, gremlins, slimeballs, etc.
     * If the user has lost or won, display the corresponding end screen and check if user wants to restart the game
	 */

    public void draw() {
        if (!gameOver && !userWin) {
            //beige background
            fill(216, 178, 129);
            rect(0, 0, WIDTH, HEIGHT);

            //Keep background beige if frozenpowerup is not active
            if (!frozenDisplay) {
                fill(216, 178, 129);
                rect(0, 0, WIDTH, HEIGHT);
            }

            // change display to light blue if frozen powerup is activated
            else {
                fill(227, 247, 255);
                rect(0, 0, WIDTH, HEIGHT-BOTTOMBAR-2);
            }
            
            currentMap.drawMap(this);
            this.updateGameStatus();

            for (Gremlin gremlin: gremlins) {
                gremlin.tick();
                gremlin.draw(this);
                if (!(gremlin.slimeballs == null)) {
                    for (Slimeball slimeball: gremlin.slimeballs) {
                        slimeball.tick();
                        slimeball.draw(this);
                    }
                }
            }

            for (Powerup powerup: powerups) {
                powerup.draw(this);
            }
        
            if (!(wizard.fireballs == null)) {
                for (Fireball fireball: wizard.fireballs) {
                    fireball.tick();
                    fireball.draw(this);
                }
            }  

            if (!(wizard.specialFires == null)) {
                for (Fireball specialFire: wizard.specialFires) {
                    specialFire.tick();
                    specialFire.draw(this);
                }
            } 

            wizard.tick();
            wizard.draw(this);

            //text for lives and levels
            textFont(font, 16);
            fill(255);
            text("Lives: ", SPRITESIZE-10, 695);
            text("Level " + levels[this.currentLevel].levelNumber + "/" + levels.length , 7*SPRITESIZE, 695);

            //WHen special fire is available, display message
            if (wizard.specialTimer == 0) {
                text("Press A!",19*SPRITESIZE, 710);
            }
            int lifeTile = 3;
            for (int i = 0; i < wizardLives; i++ ) {
                this.image(wizardSprites[1], lifeTile*SPRITESIZE, 680);
                lifeTile+=1;
            }
            
            // draw powerbars
            this.powerbar.draw(this);

            //move to next level if wizard has reached door
            if (wizard.nextLevel()) {
                moveToNextLevel(); 
                if (this.currentLevel < levels.length) {
                    setUpMap(this.currentLevel);
                }
                else {
                    userWin = true;
                    }
               
            }
        }

        //display gameover screen and check if user wants to restart
        else if (gameOver && !userWin) {
            this.image(gameOverScreen, 0, 0);
            restartGame();
        }

        //display win screen and check if user wants to restart
        else if (userWin && !gameOver) {
            this.image(winScreen, 0, 0);
            restartGame();
        }
    }

    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
}
