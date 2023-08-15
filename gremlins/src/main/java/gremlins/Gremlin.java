package gremlins;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.Random;
import java.util.ArrayList;

public class Gremlin extends GameCharacter {

    public int movingDirection;
    private int timer = 0;
    public double enemyCooldown;
    private final int FROZENTIME = 5;
    public boolean frozen = false;
    
    private PImage slimeImage;
    

    public ArrayList<Slimeball> slimeballs;

    public Gremlin(int x, int y, int speed, PImage sprite, Map map, Level level, PImage slimeImage) {
        super(x, y, speed, sprite, map, level);
        this.movingDirection = App.randomGenerator.nextInt(4) + 1;

        this.slimeballs = new ArrayList<Slimeball>();
        this.slimeImage = slimeImage;
        this.enemyCooldown = this.level.enemyCooldown;
    }

    /**
     * Stops gremlin movement by settign velocities to 0 and resetting direction
	 */
    public void stop() {
        this.movingDirection = STOP;
        this.velX = 0;
        this.velY = 0;
    } 
   
    /**
     * If tile in requested direction is free, gremlin moves depending on what its direction is set to
     * @return true if gremlin moves, otherwise false
	 */

    private boolean move() {
        if (!this.map.collision(this.x, this.y, this.movingDirection)){   
            
            switch (this.movingDirection) {
            case UP:
              this.up(this.speed);
              this.y += this.velY;
              return true;
            case DOWN:
                this.down(this.speed);
                this.y += this.velY;
              return true;
            case LEFT:
                this.left(this.speed);
                this.x += this.velX;
              return true;
            case RIGHT:
                this.right(this.speed);
                this.x += this.velX;
              return true;
            case STOP:
                return false;
          }
        } return false;
      }
    
      /**
     * Generate a random integer between 1-4 to rperesent random new direction change if gremlin collides with a wall
     * The new moving directin can not be the direction that the gremlin came from
	 */

    private boolean changeDirection() {
        int newMovingDirection = App.randomGenerator.nextInt(4) + 1;
        if (!(newMovingDirection == this.movingDirection))
            if (!(newMovingDirection == getOppDirection(this.movingDirection))) {
                if (this.inTile()) {
                    if (!this.map.collision(this.x, this.y, newMovingDirection)) {
                        this.movingDirection = newMovingDirection;
                        return true;
                    } 
                }     
        } return false;
    }
    /**
     * Handles dead end cases, if the only direction the gremlin can move is the direction it came from
	 */

    private void reverseDirection() {
        if (this.map.deadEnd(this.x, this.y, this.movingDirection)) {
            this.movingDirection = getOppDirection(this.movingDirection);
        }
    }

    /**
     * If gremlin has collided with wizard, return true, otherwise false
     * @return success
	 */

    public boolean collideWithWizard(Wizard wizard) {
        int size = TILESIZE;
        if (this.x < wizard.x + size &&
            this.x + size > wizard.x &&
            this.y < wizard.y + size &&
            this.y + size > wizard.y) {
                return true;
        } return false;
    }

    /**
     * Start a timer and if the timer is equal to the enemy cooldown specified in the level config and the gremlin is in a tile, create a slimeball
     * The slimeballs are fired from each gremlin at the specified frequency
	 */
    public void generateSlime() {
        timer ++;
        if (timer >= enemyCooldown*App.FPS && this.inTile()) {
            this.slimeballs.add(new Slimeball(this.x, this.y, App.SLIMEBALLSPEED, this.slimeImage, this.map, this.level, this.movingDirection));
            timer = 0;
        }
    }    

    /**
     * Timer for frozen poerup
     * After 10 seconds, gremlins unfreeze and display is no longer frozen
     */
    public void frozenTimer() {
        timer ++;
        if (timer ==  FROZENTIME*App.FPS) {
            this.frozen = false;
            timer = 0;
        }
    }

    /**
     * Handles logic such as gremlin movement, direction changes, collisions and slimeballs
     * If frozen powerup mode is activated, handles logic to change display such as changing stone and gremlin sprites
	 */
    public void tick() {
        if (!this.frozen) {
            this.map.stonewall = App.stonewall;
            this.sprite = App.gremlinImage;
            App.frozenDisplay = false;
            
            if (!this.move()) {
            this.changeDirection();
            this.move();
            this.reverseDirection();   
        }

            this.generateSlime();  
        }

        else {
            frozenTimer();
            this.map.stonewall = App.iceblock;
            this.sprite = App.frozenGremlin;
            App.frozenDisplay = true;
            
        }
        
        
    }

    /**
     * Draw gremlin frame by frame if it is not dead
	 */
    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y); 
    }
}
