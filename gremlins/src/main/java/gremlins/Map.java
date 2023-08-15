package gremlins;
import java.io.IOException;
import java.io.*;
import java.util.*;

import processing.core.PApplet;
import processing.core.PImage;

public class Map {

    private int mapWidth;
    private int mapHeight;
    public final int TILESIZE = 20;

    private String mapFile;
    private Level level;

    private PImage brickwall;
    public PImage[] brickImages;
    public PImage stonewall;
    private PImage gremlinImage;
    private PImage slimeball;
    private PImage door;
    private PImage snowflake;

    public int wizardStartingX;
    public int wizardStartingY;

    public int gremlinCount = 0;
    public ArrayList<Gremlin> gremlins;
    public ArrayList<Powerup> powerups;

    public char[][] mapArray; 

    public boolean wizardStarted;

    private char[] storeTiles;

    //Directions (left = 1, right = 2, up = 3, down = 4)
    private static final int STOP = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int UP = 3;
    private static final int DOWN = 4;

    public Map(int mapWidth, int mapHeight, Level level, PImage brickwall, PImage[] brickImages, PImage stonewall, PImage gremlinImage, PImage slimeball, PImage door, PImage snowflake) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        this.level = level;
        this.mapFile = level.mapFile;
        this.brickwall = brickwall;
        this.brickImages = brickImages;
        this.stonewall = stonewall;
        this.gremlinImage = gremlinImage;
        this.slimeball = slimeball;
        this.door = door;
        this.snowflake = snowflake;

        this.gremlins = new ArrayList<Gremlin>();
        this.powerups = new ArrayList<Powerup>();
        this.storeTiles = new char[3];
    }

    /**
     * Reads map file and converts it to a 2D array
	 */
    public void readMap() {
        try {
           char[][] mapArray = new char[this.mapHeight][this.mapWidth];
           String line = null;
           File map = new File(this.mapFile);
           Scanner scan = new Scanner(map);
           while (scan.hasNextLine()) {
               for (int row = 0; row < this.mapHeight; row++) {
                   line = scan.nextLine();
                   for (int column = 0; column < this.mapWidth; column++) {
                       mapArray[row][column] = line.charAt(column);
                   }
               }
           }
           this.mapArray = mapArray;
       } catch (FileNotFoundException e) {
           System.out.println("File not found.");
       }
   } 

    /**
     * @return the map Array for current level
	 */
   public char[][] getMapArray() {
    return this.mapArray;
   }

    /**
     * Reads the map array and draws images for stone walls, brick walls and the door
	 */
    public void drawMap(PApplet app) {
        for (int row = 0; row < this.mapHeight; row++) {
            for (int column = 0; column < this.mapWidth; column++) {
                
                char c = this.mapArray[row][column];
                if (c == 'X') {
                    drawStonewall(app, column, row, TILESIZE);
                } 
                else if (c == 'B') {
                    drawBrickwall(app, column, row, TILESIZE);
                }
                else if (c == 'E') {
                    drawDoor(app, column, row, TILESIZE);
                }
            }
        }
     }

     /**
     * Draw stonewall
	 */
     public void drawStonewall(PApplet app, int column, int row, int imageSize) {
        app.image(this.stonewall, column*imageSize, row*imageSize);
     }

     /**
     * Draw brickwall
	 */
     public void drawBrickwall(PApplet app, int column, int row, int imageSize) {
        app.image(this.brickwall, column*imageSize, row*imageSize);
     }

     /**
     * Draw door
	 */
     public void drawDoor(PApplet app, int column, int row, int imageSize) {
        app.image(this.door, column*imageSize+3, row*imageSize);
     }

    /**
     * Find where the wizard should start and set the starting x and y values to the corresponding coordinates
	 */
    public void startWizard() { 
        for (int row = 0; row < this.mapHeight; row++) {
            for (int column = 0; column < this.mapWidth; column++) {
                char c = mapArray[row][column];
                if (c == 'W') {
                    this.wizardStartingX = column*TILESIZE;
                    this.wizardStartingY = row*TILESIZE;
                }
            }
        }
    }

    /**
     * Read the array to check where gremlins should start and create gremlins at these tiles
     * Add all snowflakes created to an ArrayList
     * @return the Gremlin ArrayList
	 */
    public ArrayList<Gremlin> makeGremlins() {
        
        for (int row = 0; row < this.mapHeight; row++) {
            for (int column = 0; column < this.mapWidth; column++) {
                char c = mapArray[row][column];
                if (c == 'G') {
                    gremlins.add(new Gremlin(column*TILESIZE, row*TILESIZE, App.GREMLINSPEED, this.gremlinImage, this, this.level, this.slimeball));

                }
            }
        }
        return this.gremlins;
    }

    /**
     * Read the array to check where snowflakes should be and create snowflakes at these tiles
     * Add all snowflakes created to an ArrayList
     * @return the snowflake ArrayList
	 */
    public ArrayList<Powerup> loadPowerups(char symbol) {

        for (int row = 0; row < this.mapHeight; row++) {
            for (int column = 0; column < this.mapWidth; column++) {
                char c = mapArray[row][column];
                if (c == symbol) {
                    powerups.add(new Powerup(column*TILESIZE, row*TILESIZE, this.snowflake));
                }
            }
        }
        return this.powerups;
    }

    /**
     * Check for collisions by checking if the next tile in front of the wizard is a brick or stone and if so, returns true, otherwise false
     * Only checks when character is in a tile
	 */
    public boolean collision(int x, int y, int direction) {
        if (x%TILESIZE == 0 && y%TILESIZE == 0) {
            int column = 0;
            int nextColumn = 0;
            int row = 0;
            int nextRow = 0;

            switch (direction) {
                case RIGHT: 
                    nextColumn = x + TILESIZE;
                    column = nextColumn/ TILESIZE;
                    row = y/ TILESIZE;
                    break;
    
                case LEFT: 
                    nextColumn = x - TILESIZE;
                    column = nextColumn/ TILESIZE;
                    row = y/ TILESIZE;
                    break;
    
                case UP: 
                    nextRow = y - TILESIZE;
                    row = nextRow/ TILESIZE;
                    column = x/ TILESIZE;
                    break;
    
                case DOWN: 
                    nextRow = y + TILESIZE;
                    row = nextRow/ TILESIZE;
                    column = x/ TILESIZE;
                    break;
            }
            char tile = mapArray[row][column];
            if (tile == 'B' || tile == 'X') {
                return true;
            }
        } return false;
    }

    /**
     * Check for dead ends (when surrounded by bricks or stones), if so returns true, otherwise false
	 */
    public boolean deadEnd(int x, int y, int direction) {
        if (x%TILESIZE == 0 && y%TILESIZE == 0) {

            char upTile = mapArray[(y - TILESIZE)/TILESIZE][x/ TILESIZE];
            char downTile = mapArray[(y + TILESIZE)/TILESIZE][x/ TILESIZE];
            char rightTile = mapArray[y/ TILESIZE][(x + TILESIZE)/TILESIZE];
            char leftTile = mapArray[y/ TILESIZE][(x - TILESIZE)/TILESIZE];

            // depending on direction, surrounding tiles are stored in an array 
            switch (direction) {
                
                case RIGHT: 
                    this.storeTiles[0] = rightTile;
                    this.storeTiles[1] = upTile;
                    this.storeTiles[2] = downTile;
                    break;

                case LEFT: 
                    this.storeTiles[0] = leftTile;
                    this.storeTiles[1] = upTile;
                    this.storeTiles[2] = downTile;
                    break;
    
                case UP: 
                    this.storeTiles[0] = upTile;
                    this.storeTiles[1] = rightTile;
                    this.storeTiles[2] = leftTile;
                    break;
    
                case DOWN: 
                    this.storeTiles[0] = downTile;
                    this.storeTiles[1] = rightTile;
                    this.storeTiles[2] = leftTile;
                    break;
            }

            //If either of the tiles in the array are bricks or stones, return true
            if ((storeTiles[0] == 'X' || storeTiles[0] == 'B') &&
                (storeTiles[1] == 'X' || storeTiles[1] == 'B') &&
                (storeTiles[2] == 'X' || storeTiles[2] == 'B') 
            ) { 
                return true;
            }
        }
        return false;
    }

    /**
     * Check for specific stone collision and if so, returns true, otherwise false
	 */
    public boolean stoneCollision(int x, int y, int direction) {
        if (x%TILESIZE == 0 && y%TILESIZE == 0) {
            int column = 0;
            int nextColumn = 0;
            int row = 0;
            int nextRow = 0;

            switch (direction) {
                case RIGHT: 
                    nextColumn = x + TILESIZE;
                    column = nextColumn/ TILESIZE;
                    row = y/ TILESIZE;
                    break;
    
                case LEFT: 
                    nextColumn = x - TILESIZE;
                    column = nextColumn/ TILESIZE;
                    row = y/ TILESIZE;
                    break;
    
                case UP: 
                    nextRow = y - TILESIZE;
                    row = nextRow/ TILESIZE;
                    column = x/ TILESIZE;
                    break;
    
                case DOWN: 
                    nextRow = y + TILESIZE;
                    row = nextRow/ TILESIZE;
                    column = x/ TILESIZE;
                    break;
            }
            char tile = mapArray[row][column];
            if (tile == 'X') {
                return true;
            }
        } return false;
    }

    /**
     * Checks if brick is hit
     * @return success
	 */
    public boolean hitBrick(int x, int y) {
        int row = y/ this.TILESIZE;
        int column = x/ this.TILESIZE;
        if (x%TILESIZE == 0 && y%TILESIZE == 0) {
            if (this.mapArray[row][column] == 'B') {
                this.mapArray[row][column] = ' ';
                return true;
            }
        } return false;

    }

    /**
     * Checks if tile is clear of stoners and bricks
     * @return success
	 */
    public boolean tileClear(int x, int y) {
        int row = y/ this.TILESIZE;
        int column = x/ this.TILESIZE;
        if (x%TILESIZE == 0 && y%TILESIZE == 0) {
            if (this.mapArray[row][column] == 'B' || this.mapArray[row][column] == 'X') {
                return false;
            }
        } return true;

    }

    /**
     * Checks if wizard has reached the door
     * @return success
	 */
    public boolean exit(int x, int y) {
        int row = y/ this.TILESIZE;
        int column = x/ this.TILESIZE;
        if (x%TILESIZE == 0 && y%TILESIZE == 0) {
            if (this.mapArray[row][column] == 'E') {
                return true;
            }
        } return false;

    }

    /**
     * Checks if snowflakje has been picked up
     *  @return success
	 */
    public boolean powerupPickup(int x, int y) {
        int row = y/ this.TILESIZE;
        int column = x/ this.TILESIZE;
        if (x%TILESIZE == 0 && y%TILESIZE == 0) {
            if (this.mapArray[row][column] == this.level.powerupSymbol) {
                return true;
            }
        } return false;
    }
}

