package gremlins;

public class Level {

    public int levelNumber;
    public String mapFile;
    public double wizardCooldown;
    public double enemyCooldown;
    public char powerupSymbol;
    public double specialCooldown;

    public Level(int levelNumber, String mapFile, double wizardCooldown, double enemyCooldown, String powerupSymbol, double specialCooldown){
        this.levelNumber = levelNumber;
        this.mapFile = mapFile;
        this.wizardCooldown = wizardCooldown;
        this.enemyCooldown = enemyCooldown;
        this.powerupSymbol = powerupSymbol.charAt(0);
        this.specialCooldown = specialCooldown;
    }
    
}
