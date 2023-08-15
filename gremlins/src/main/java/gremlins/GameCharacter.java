package gremlins;
import processing.core.PApplet;
import processing.core.PImage;

public abstract class GameCharacter {
    
    protected int x;                    
    protected int y;
    protected int velX;
    protected int velY;
    protected int speed;

    protected Map map;
    protected Level level;
    protected char[][] mapArray;

    protected PImage sprite;
    protected int direction;

    protected int TILESIZE = 20;

    //DIRECTIONS
    protected final int STOP = 0;
    protected final int LEFT = 1;
    protected final int RIGHT = 2;
    protected final int UP = 3;
    protected final int DOWN = 4;

    public GameCharacter(int x, int y, int speed, PImage sprite, Map map, Level level) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.sprite = sprite;
        this.map = map;
        this.level = level;

        this.mapArray = map.getMapArray();
    
        this.velX = 0;
        this.velY = 0;
    }

    /**
     * @return x coordinate
     */
    public int getX() {
        return this.x;
    }

    /**
     * @return y coordinate
     */
    public int getY() {
        return this.y;
    }

    /**
     * Set x velocity to negative speed if moving left
     */
    public void left(int speed) {
        this.velX = -speed;   
    }

    /**
     * Set x velocity to speed if moving right
     */
    public void right(int speed) {
        this.velX = speed;
    }
    
    /**
     * Set y velocity to negative speed if moving up
     */
    public void up(int speed) {
        this.velY = -speed;
    }

    /**
     * Set y velocity to speed if moving down
     */
    public void down(int speed) {
        this.velY = speed;
    }

    /**
     * Set velocites and direction to 0 to stop movement
     */
    public void stop() {
        this.direction = STOP;
        this.velX = 0;
        this.velY = 0;
}  
    /**
     * @return the opposite direction of given direction
     */
    public int getOppDirection(int direction) {
        switch(direction) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case RIGHT:
                return LEFT;
            case LEFT: 
                return RIGHT;
        } return 0;
    }

    /**
     * Check if object is in a tile
     */
    public boolean inTile() {
        if (this.x%TILESIZE == 0 && this.y%TILESIZE == 0) {
            return true;
        } return false;
    }
    /**
     * Abstract method to handle all logic, subclasses will have different tick methods
     */
    abstract public void tick(); 
    /**
     * Draw method to draw entity frame by frame, subclasses will have different draw methods depending on different conditions
     */
    abstract public void draw(PApplet app);
}
