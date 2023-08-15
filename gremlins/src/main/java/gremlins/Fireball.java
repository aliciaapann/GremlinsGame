package gremlins;
import processing.core.PApplet;
import processing.core.PImage;
import java.util.ArrayList;   

public class Fireball extends GameCharacter {

    private Wizard wizard;

    public int direction;
    private brickAnimation brickAnimation;
    private int timer = 0;
    private boolean animateBrick;

    public boolean dead;

    
    public Fireball(int x, int y, int speed, PImage sprite, Map map, Level level, int wizardVersion, Wizard wizard) {
        super(x, y, speed, sprite, map, level);
        this.wizard = wizard;

        switch(wizardVersion) {
            case 0:
                this.direction = LEFT;
                break;
            case 1:
                this.direction = RIGHT;
                break;
            case 2:
                this.direction = UP;
                break;
            case 3:
                this.direction = DOWN;
                break;
        }

        this.brickAnimation = new brickAnimation(4, this.map.brickImages);
        this.dead = false;
    }

    /**
     * If no collisions, fireball moves in the direction the wizard is moving after it has been fired
	 */
    private boolean move() {
        if (!(this.map.stoneCollision(this.x, this.y, this.direction))) {   
            switch(this.direction) {
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
        }
        return false;
    }

    /**
     *  Stop fireball movement by resetting direction and seetting velocities to 0
	 */
    public void stop() {
            this.direction = STOP;
            this.velX = 0;
            this.velY = 0;
    }  

    /**
     * If fireball has hit a brick, start the brick animation and stop the fireball, return true, otherwise false
     * @return success
	 */
    private boolean hitBrick() {
        if (this.map.hitBrick(this.x, this.y)) {
            animateBrick = true;
            this.stop();
            return true;
        } return false;
    }

    /**
     * If the fireball has hit a brick, set it to dead so it no longer has effect and return true, otherwise false
     * @return success
	 */
    private boolean fireballDead() {
        if (this.map.hitBrick(this.x, this.y) || this.map.stoneCollision(this.x, this.y, this.direction) && this.inTile()) {
            this.dead = true;
            return true;
        } return false;
    }

    /**
     * Time the duration of the brick animation so brick animation method can be triggered at the same time
	 */
    private void brickAnimating() {
        timer++;
        if (timer >= this.brickAnimation.speed*this.brickAnimation.frames) {
            animateBrick = false;
            timer = 0;
        } 
    }

    /**
     * If the fireball is still effective and hits a gremlin, set it to dead and return true, otherwise false
     * If gremlin is hit, respawn the gremlin in a tiler at least 10 tiles sway from the wizard
     * @return success
	 */
    public boolean hitGremlin(Gremlin gremlin) {
        //IFINTILE
        if (!this.dead) {
            if (this.x < gremlin.x + TILESIZE && 
                this.x + TILESIZE > gremlin.x &&
                this.y < gremlin.y + TILESIZE &&
                this.y + TILESIZE > gremlin.y) {
                this.stop();
                this.dead = true;

                respawnGremlin(gremlin);

                return true;
            } 
        } return false;
    }

    /**
     * If the new spawn tile is within 1o tiles opf the wizard, return false, otherwise true
     * @return success
	 */
    public boolean validSpawnDistance(int x, int y){
        if (this.wizard.x < x + 10*TILESIZE &&
            this.wizard.x + 10*TILESIZE > x &&
            this.wizard.y < y + 10*TILESIZE &&
            this.wizard.y + 10*TILESIZE > y) {
                return false;
            } return true;
    }

    /**
     * Respawn the gremlin in a different tile
     * If the randomly generated new spawn tile is not clear or is not at least 10 tiles away, keep generating a new tile
     * Once valid tile is generated, set the gremlin's position to the new tile
	 */
    public void respawnGremlin(Gremlin gremlin){
        int newX = (App.randomGenerator.nextInt(36))*TILESIZE;
        int newY = 0;
        
        while (!(this.validSpawnDistance(newX, newY)) || !(this.map.tileClear(newX, newY))) {
            newX = App.randomGenerator.nextInt(36)*TILESIZE;
            newY = App.randomGenerator.nextInt(33)*TILESIZE;
            
        }

        if (this.validSpawnDistance(newX, newY)) {
            gremlin.x = newX;
            gremlin.y = newY;
        }
    }


    /**
     * Handles logic such as fireball movement, checks for collisions, fireball status and brick animation
	 */
    public void tick() {
        for (Gremlin gremlin: this.map.gremlins) {
            this.hitGremlin(gremlin);
        }

    
        if (!this.map.stoneCollision(this.x, this.y, this.direction)) {
            this.move(); 
        }
        this.hitBrick();
        this.fireballDead();

        // if brick is hit, start the timer and trigger animation
        if (this.animateBrick) {
            brickAnimation.triggerBrickAnimation();
            this.brickAnimating();
        }

        
    }
    
    /**
     * Draw fireball frame by frame if it is not dead
     * Call brick animation when hit
	 */
    public void draw(PApplet app) {
         if (!this.dead) {
            app.image(this.sprite, this.x, this.y);
        }

        if (this.animateBrick){
        brickAnimation.drawAnimation(app, this.x, this.y);
        }
    }
}
