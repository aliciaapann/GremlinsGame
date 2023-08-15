package gremlins;
import processing.core.PApplet;
import processing.core.PImage;

public class Powerbar {

    private final int WIDTH = 240;
    private final int HEIGHT = 10;
    
    private Level level;
    private int wizardCooldown;
    private int specialCooldown;

    private int r = WIDTH;
    private int d = 0;

    private int chargeAmountWiz;
    private int chargeAmountSpecial;
    private Wizard wizard;
    

    public Powerbar(double wizardCooldown, Level level, Wizard wizard){
        this.level = level;

        //Čhange cooldowns from seconds to number of frames
        this.wizardCooldown =(int)(this.level.wizardCooldown*App.FPS);
        this.specialCooldown = (int)(this.level.specialCooldown*App.FPS);

        //Calculate how much rectangle has to increase by to match the cooldown time
        this.chargeAmountWiz =  (WIDTH/this.wizardCooldown);
        this.chargeAmountSpecial =  (WIDTH/this.specialCooldown);

        this.wizard = wizard;    
    }

    /**
     * To show the wizard cooldown, draw an increasing bar corresponding to the cooldown time
     * When the fireball is available, the bar will be full and turn green
	 */
    private void wizardCooldown(PApplet app) {

        //When fireball is available, rectangle is fully filled
        if (this.wizard.fireballTimer == 0) {
            r = WIDTH;
        } else if (this.wizard.fireballTimer == 1) {
            r = 0;
        }

        //When rectangle is fully filled, make it green
        if (r == WIDTH) {
            app.fill(0, 230, 0);
            app.rect(460, 675, r, HEIGHT);
        //When fireball is charging, make bar pink
        } else {
            app.fill(251,72,196);
            app.rect(460, 675, r, HEIGHT);
        }

        r+= this.chargeAmountWiz;
        
        //border of rectangle
        app.noFill();
        app.stroke(255, 255, 255);
        app.rect(460, 675, WIDTH, HEIGHT);
    }

    /**
     * To show the special spell cooldown, draw an increasing bar corresponding to the cooldown time
     * When the special fireball is available, the bar will be full and turn purple
	 */
    private void specialCooldown(PApplet app){
       //When fireball is available, rectangle is fully filled
        if (this.wizard.specialTimer == 0) {
            d = WIDTH;}
        
        if (d>WIDTH) {
            d = 0;
        }

         //When rectangle is fully filled, make it purple
        if (d == WIDTH) {
            app.fill(171, 153, 240);
            app.stroke(255, 255, 255);
            app.rect(460, 700, d, HEIGHT);
        //When fireball is charging, make bar white
        } else {
            app.fill(255, 255, 255);
            app.rect(460, 700, d, HEIGHT);
        }

        
        d+= this.chargeAmountSpecial;
        
        //border of rectangle
        app.noFill();
        app.stroke(255, 255, 255);
        app.rect(460, 700, WIDTH, HEIGHT);
        
    }


    /**
     * Draw each powerbar frame by frame
	 */
    public void draw(PApplet app) {
        this.wizardCooldown(app);
        this.specialCooldown(app);
    }
    
}
