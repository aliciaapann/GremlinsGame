package gremlins;
import processing.core.PApplet;
import processing.core.PImage;

public class Slimeball extends GameCharacter {

    private int direction;
    public boolean dead = false;

    public Slimeball(int x, int y, int speed, PImage sprite, Map map, Level level, int direction) {
        super(x, y, speed, sprite, map, level);
        this.direction = direction;
    }
    
    /**
     * If no collisions, slimeball moves in the direction the gremlin is moving after it has been fired
	 */
    private boolean move() {
        if (!this.map.collision(this.x, this.y, this.direction)) {   
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
            } 
        }
        return false;
    }

    /**
     * If the slimeball has hit anything, set it to dead so it no longer has effect and return true, otherwise false
     * @return success
	 */
    private boolean slimeballDead() {
        if ( this.map.collision(this.x, this.y, this.direction) && this.inTile()) {
            this.dead = true;
            return true;
        } return false;
    }

    /**
     * If the slimeball has hit a wizard, set it to dead so it no longer has effect and return true, otherwise false
     * @return success
	 */
    public boolean hitWizard(Wizard wizard) {
        if (!this.dead) {
            if (this.x < wizard.x + TILESIZE && 
                this.x + TILESIZE > wizard.x &&
                this.y < wizard.y + TILESIZE &&
                this.y + TILESIZE > wizard.y) {
                this.dead = true;
                return true;
            } 
        } return false;
    }

    /**
     * If the slimeball has hit a fireball, stop it from moving and set it to dead so it no longer has effect and return true, otherwise false
     * @return success
	 */
    public boolean hitFireball(Fireball fireball) {
        if (!this.dead && !fireball.dead) {
            if (this.x < fireball.x + TILESIZE && 
                this.x + TILESIZE > fireball.x &&
                this.y < fireball.y + TILESIZE &&
                this.y + TILESIZE > fireball.y) {
                this.dead = true;
                fireball.stop();
                fireball.dead = true;
                return true;
            } 
        } return false;
    }

    /**
     * Handlers logic such as slimeball movement and colisions
	 */
    public void tick() {
        this.move(); 
        this.slimeballDead();
        for (Fireball fireball: App.wizard.fireballs) {
            this.hitFireball(fireball);
        }
    }
    /**
     * Only draw active slimeballs frame by frame
	 */
    public void draw(PApplet app) {
        if (!this.dead) {
            app.image(this.sprite, this.x, this.y);
        }
    }
}