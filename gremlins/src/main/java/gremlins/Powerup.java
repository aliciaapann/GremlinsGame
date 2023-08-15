package gremlins;
import processing.core.PApplet;
import processing.core.PImage;

public class Powerup {
    public int x;
    public int y;
    private PImage sprite;
    public boolean dead = true;
    public boolean usedUp = false;
    private int timer = 0;
    private final int POWERUPTIME = 10;

    public Powerup(int x, int y, PImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    /**
     * Start timer to wait for powerup to become available again
     * After 10 seconds, timer is reset and snowflakes become available again
	 */
    public void powerupAvailable() {
        timer ++;
        if (timer ==  POWERUPTIME*App.FPS) {
            this.dead = false;
            timer = 0;
        }
    }

    /**
     * Draw snowflakes frame by frame
     * If powerup wait time is over, snowflakes are displayed on the screen
     * If snowflakes have already been collected, they are no longer drawn on the screen
	 */
    public void draw(PApplet app){
        if (this.dead) {
            this.powerupAvailable(); 
            App.powerupAvailable = false;
        }
        else if (!this.dead && !this.usedUp) {
            app.image(this.sprite, this.x, this.y);
            App.powerupAvailable = true;
        }  
    }  
}
