package gremlins;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;

public class Wizard extends GameCharacter {

    //Right facing sprite is default for start of the game
    private int currentSprite = 1;

    private int currentMovingDirection = 0;
    private int requestedMovingDirection = 0;

    public boolean requestedStop = true;

    public PImage fireballImage;
    public PImage specialFire;
    public ArrayList<Fireball> fireballs;
    public ArrayList<SpecialFire> specialFires;

    public int fireballTimer = 0;
    public boolean startingTimer;
    private double wizardCooldown;

    public boolean initialCharging = true;
    public int specialTimer = 0;
    public boolean startingSpecialTimer;
    private double specialCooldown;

    public boolean powerupTimeout = false;

   
    public Wizard(int x, int y, int speed,  PImage sprite, Map map, Level level, PImage fireballImage) {
        super(x, y, speed, sprite, map, level);
        this.fireballImage = fireballImage;
        this.fireballs = new ArrayList<Fireball>();
        this.specialFires = new ArrayList<SpecialFire>();

        this.wizardCooldown = level.wizardCooldown;
        this.specialFire = App.specialFire;
        this.specialCooldown = level.specialCooldown;
    }

    /**
     * @return current sprite version as an integer
	 */
    public int getCurrentSprite() {
        return this.currentSprite;
    }

    /**
     * @param spriteVersion, integer corresponding to a wizard sprite version
     * Sets the current sprite to desired sprite version
	 */
    public void setSprite(int spriteVersion) {
        this.currentSprite = spriteVersion;
        this.sprite = App.wizardSprites[spriteVersion];
    }
    /**
     * Direction request from left key being pressed changes sprite to the left facing wizard 
	 */
    public void requestLeft() {
        setSprite(0);
        this.requestedMovingDirection = LEFT;
    }

    /**
     * Direction request from right key being pressed changes sprite to the right facing wizard 
	 */
    public void requestRight() {
        setSprite(1);
        this.requestedMovingDirection = RIGHT; 
    }

    /**
     * Direction request from up key being pressed changes sprite to the up facing wizard 
	 */
    public void requestUp() {
        setSprite(2);
        this.requestedMovingDirection = UP;
    }

    /**
     * Direction request from down key being pressed changes sprite to the down facing wizard 
	 */
    public void requestDown() {
        setSprite(3);
        this.requestedMovingDirection = DOWN;
    }

    /**
     * Stops wizard movement by settign velocities to 0 and resetting direction
	 */
    public void stop() {
        this.currentMovingDirection = STOP;
        this.requestedMovingDirection = STOP;
        this.velX = 0;
        this.velY = 0;
    }  

    /**
     * Checks if the requested moving direction is valid by checking if wizard will collide
     * If tile in requested direction is free, the current moving direction is set to the requested direction
     * Wizard moves depending on what current direction is set to
	 */

    private void move() {
        if (this.inTile()) {
            if (!this.map.collision(this.x, this.y, this.requestedMovingDirection)) {
                this.currentMovingDirection = this.requestedMovingDirection;
            }
        }

        if (this.map.collision(this.x, this.y, this.currentMovingDirection) && this.inTile()) {
            return;
        }
        switch (this.currentMovingDirection) {
            case LEFT:
                this.left(this.speed);
                this.x += this.velX;
                break;

            case RIGHT:
                this.right(this.speed);
                this.x += this.velX;
                break;

            case UP:
                this.up(this.speed);
                this.y += this.velY;
                break;

            case DOWN:
                this.down(this.speed);
                this.y += this.velY;
                break;
       }
    }

    /**
     * If the wizard is in a tile and user requests a fireball, new Fireball object is created at the same coordinates as the wizard
     * If the user requests a fireball when the wizard is not in a tile, the fireball will only be created and fired at the nearest tile in the direction of movement
	 */
    public void requestFireball() {

        if (this.inTile()) {
            this.fireballs.add(new Fireball(this.x, this.y, App.FIREBALLSPEED, this.fireballImage, this.map, this.level, this.currentSprite, this));
        }

        else {
            int fireballX = this.x;
            int fireballY = this.y;

            int toNearestTileX = this.x%TILESIZE;
            int toNearestTileY = this.y%TILESIZE;

            switch (this.currentMovingDirection) {
                case LEFT:
                fireballX -= toNearestTileX;
                break;

                case RIGHT:
                fireballX += (TILESIZE - toNearestTileX);
                break;

                case UP:
                fireballY -= toNearestTileY;
                break;

                case DOWN:
                fireballY += (TILESIZE - toNearestTileY);
                break;
            }
            
            if (fireballX%TILESIZE == 0 && fireballY%TILESIZE == 0) {
                this.fireballs.add(new Fireball(fireballX, fireballY, App.FIREBALLSPEED, this.fireballImage, this.map, this.level, this.currentSprite, this));
            }
        }
    }

    /**
     * Similar to previous method but for the special fireball
     * If special fireball is requested, four special fireballs will be created and set to move in four different directions
	 */
    public void requestSpecialFireball() {

        if (this.inTile()) {
            this.specialFires.add(new SpecialFire(this.x, this.y, App.FIREBALLSPEED, this.specialFire, this.map, this.level, 0, this));
            this.specialFires.add(new SpecialFire(this.x, this.y, App.FIREBALLSPEED, this.specialFire, this.map, this.level, 1, this));
            this.specialFires.add(new SpecialFire(this.x, this.y, App.FIREBALLSPEED, this.specialFire, this.map, this.level, 2, this));
            this.specialFires.add(new SpecialFire(this.x, this.y, App.FIREBALLSPEED, this.specialFire, this.map, this.level, 3, this));
            }

        else {
            int fireballX = this.x;
            int fireballY = this.y;

            int toNearestTileX = this.x%TILESIZE;
            int toNearestTileY = this.y%TILESIZE;

            switch (this.currentMovingDirection) {
                case LEFT:
                fireballX -= toNearestTileX;
                break;

                case RIGHT:
                fireballX += (TILESIZE - toNearestTileX);
                break;

                case UP:
                fireballY -= toNearestTileY;
                break;

                case DOWN:
                fireballY += (TILESIZE - toNearestTileY);
                break;
            }
            
            if (fireballX%TILESIZE == 0 && fireballY%TILESIZE == 0) {
                this.specialFires.add(new SpecialFire(fireballX, fireballY, App.FIREBALLSPEED, this.specialFire, this.map, this.level, 0, this));
                this.specialFires.add(new SpecialFire(fireballX, fireballY, App.FIREBALLSPEED, this.specialFire, this.map, this.level, 1, this));
                this.specialFires.add(new SpecialFire(fireballX, fireballY, App.FIREBALLSPEED, this.specialFire, this.map, this.level, 2, this));
                this.specialFires.add(new SpecialFire(fireballX, fireballY, App.FIREBALLSPEED, this.specialFire, this.map, this.level, 3, this));
            }
        }
    }

    /**
     * Reset level by clearing all game elements such as fireballs, gremlins and also reset fireball timer
     * Makes new enemy gremlins by reading the level map again
     * Wizard is set back to starting position
	 */
    public void resetLevel(){
        this.fireballs.clear();
        this.startingTimer = false;
        App.gremlins.clear();
        App.gremlins = this.map.makeGremlins();  
        this.x = this.map.wizardStartingX;
        this.y = this.map.wizardStartingY;
        App.wizardLives --;
        this.map.readMap();
    }

    /**
     * Check if wizard has collided with gremlin
     * If collided with gremlin, reset the level
     * @return true or false
	 */
    public boolean gremlinCollision() {
        for (Gremlin gremlin: App.gremlins) {
            if (gremlin.collideWithWizard(this)) {
                this.resetLevel();
                return true;
            } 
        } return false;
    }

    /**
     * Check if wizard has collided with slimeball
     * If collided with slimeball, reset the level
     * @return true or false
	 */
    public boolean slimeballCollision() {
        for (Gremlin gremlin: App.gremlins) {
            for (Slimeball slimeball: gremlin.slimeballs) {
                if (slimeball.hitWizard(this)) {
                    this.resetLevel();
                    return true;
                }
            }
        } return false;
    }
  
    /**
     * Timer for wizard cooldown
	 */
    public void startFireballTimer() {
        if (startingTimer) {
            fireballTimer++;
            if (fireballTimer > wizardCooldown*App.FPS) {
                fireballTimer = 0;
                startingTimer = false;
            }
        } return;
    }

    /**
     * Timer for special spell cooldown
	 */
    public void startingSpecialTimer() {
        if (startingSpecialTimer) {
            specialTimer++;
            if (specialTimer > this.specialCooldown*App.FPS) {
                specialTimer = 0;
                startingSpecialTimer = false;
            }
         } return;
    }

    /**
     * If wizard reaches tile where the door is, this will return true, otherwise false
     * @return success
	 */
    public boolean nextLevel() {
        if (this.map.exit(this.x, this.y)) {
            return true;
        } return false;
    }

    /**
     * If wizard collides with a snowflake that hasn't been used up yet, the frozen powerup is activated
     * If frozen mode is activated, gremlins freeze and all snowflakes disappear. Return true if snowflake is used, otherwise false
     * @return success
	 */
    private boolean powerup() { 
        for (Powerup snowflake: App.powerups) {
            if (this.x == snowflake.x && this.y == snowflake.y && !snowflake.dead && !snowflake.usedUp) {
                snowflake.usedUp = true;
                for (Gremlin gremlin: App.gremlins) {
                    gremlin.frozen = true;
                }
                for (Powerup powerup: App.powerups) {
                    powerup.dead = true;
                }
                return true;
            }
        } return false;    
    }

    /**
     * Handles logic such as wizard movement, checks for collisions, fireball requests, powerups and whether wizard reaches end of level
	 */
    public void tick() {
        this.move();
        if (requestedStop && this.inTile()) {
            this.stop();
        }
        this.gremlinCollision();
        this.slimeballCollision();
        this.startFireballTimer();
        this.startingSpecialTimer();
        this.nextLevel();
        
        this.powerup();
    }

    /**
     * Draw wizard frame by frame
	 */
    public void draw(PApplet app) {
        app.image(this.sprite, this.x, this.y);   
    }

}
