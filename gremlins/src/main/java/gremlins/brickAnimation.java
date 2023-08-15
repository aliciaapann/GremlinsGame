package gremlins;
import processing.core.PImage;
import processing.core.PApplet;

public class brickAnimation {
    
    public int speed;
    public int frames;

    private int timer = 0;

    private PImage[] images;
    private PImage currentImage;

    public brickAnimation(int speed, PImage[] images) {
        this.speed = speed;
        this.images = images;
        this.currentImage = images[0];

        this.frames = images.length;
    }

    /**
     * If brick animation is triggered, start a timer that increments by 1 every frame
     * Every 4 frames, the brick image will change
     * Timer will reset after the 4 brick images have finished being displayed
     
	 */

    public void triggerBrickAnimation() {
        timer++;
        switch(timer){
            case 0:
                this.currentImage = this.images[0];
                break;
            case 4:
                this.currentImage = this.images[1];
                break;
            case 8:
                this.currentImage = this.images[2];
                break;
            case 12:
                this.currentImage = this.images[3];
                break;
        }

        if (!(timer < speed*frames)) {
            timer = 0;
        }  
    }
    /**
     * Draw brick image by frame
	 */
    public void drawAnimation(PApplet app, int x, int y) {
        app.image(this.currentImage, x, y);   
    }
}
