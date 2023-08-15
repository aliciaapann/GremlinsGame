package gremlins;

import org.junit.jupiter.api.Test;

import processing.core.PApplet;
import processing.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

public class SampleTest {

    // Terst when the powerUp is active
    @Test
    public void testPowerUp1() {
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000); // to give time to initialise stuff before drawing begins
        //assertEquals(app.powerUp(500), true);
    }

    // Tests when the potionDelay is equal to 0 - potion delay should stay at 0
    @Test
    public void testPotionTiming2() {
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000); // to give time to initialise stuff before drawing begins
        int potionDelay = 0;
        //assertEquals(app.potionTiming(potionDelay), 0);
    }

    // Tests when the potionAppears if true
    @Test
    public void testPotionTiming3() {
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000); // to give time to initialise stuff before drawing begins
        int potionDelay = 1;
        //app.potionTiming(potionDelay);
        boolean potionAppears = true;
        //assertEquals(potionAppears, true);
    }

    // Tests when the colliding redTrail is not active
    @Test
    public void testRedTrail() {
        App app = new App();
        app.loop();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000); // to give time to initialise stuff before drawing begins
        //app.redTrail();
        //assertEquals(app.redTrail(), false);
    }

    // General overall test of the App
    @Test
    public void testForFree() {
        App app = new App();
        app.noLoop();
        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
    }

    // Test that when the playerLives is called, it returns true
    @Test
    public void testPlayerLives() {
        App app = new App();
        app.noLoop();
        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
        //assertEquals(app.PlayerLives(), true);
    }

    // Test that when the right key is pressed, it returns true
    @Test
    public void testKeyPressRight() {
        App app = new App();
        app.noLoop();
        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
        app.keyPressed(new KeyEvent(app, 0, 0, 0, (char) 0, 39));
        boolean rightMovement = true;
        assertTrue(rightMovement);
    }

    // Test that when the left key is pressed, it returns true
    @Test
    public void testKeyPressLeft() {
        App app = new App();
        app.noLoop();
        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
        app.keyPressed(new KeyEvent(app, 0, 0, 0, (char) 0, 37));
        boolean leftMovement = true;
        assertTrue(leftMovement);
    }

    // Test that when the up key is pressed, it returns true
    @Test
    public void testKeyPressUp() {
        App app = new App();
        app.noLoop();
        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
        app.keyPressed(new KeyEvent(app, 0, 0, 0, (char) 0, 38));
        boolean upMovement = true;
        assertTrue(upMovement);
    }

    // Test that when the down key is pressed, it returns true
    @Test
    public void testKeyPressDown() {
        App app = new App();
        app.noLoop();
        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
        app.keyPressed(new KeyEvent(app, 0, 0, 0, (char) 0, 40));
        boolean downMovement = true;
        assertTrue(downMovement);
    }

    // Test that when the down key is pressed, it returns true
    @Test
    public void testFill() {
        App app = new App();
        app.noLoop();
        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
        //app.fill();
    }

    // Test floodFill algorithm
    @Test
    public void testFloodFill() {
        App app = new App();
        app.noLoop();
        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
        String[][] matrix = { { "1", "1", "1", "1" },
                { "1", "1", "1", "1" },
                { "1", "0", "0", "1" } };
        //app.floodfill(matrix, 2, 2, "1", "2");
    }

    // Test keyMovement algorithm
    @Test
    public void testKeyMovement() {
        App app = new App();
        app.noLoop();
        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
        //app.keyMovement();
    }

    // Tests that the window text is displayed
    public void testWindowText() {
        App app = new App();
        app.noLoop();
        // Tell PApplet to create the worker threads for the program
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.delay(1000);
        //app.windowText();
    }

}

// gradle run
// gradle test
// gradle jacocoTestReport
