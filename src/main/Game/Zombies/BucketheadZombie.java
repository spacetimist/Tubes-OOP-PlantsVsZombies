package main.Game.Zombies;
import main.GUI.KeyHandler;
import main.GUI.WindowPanel;
import main.Game.ParentClass.Zombie;


public class BucketheadZombie extends Zombie {
    public BucketheadZombie(WindowPanel wp, KeyHandler kh) {
        super(wp, kh);
        getZombieImage("src/main/Resources/Zombies/buckethead.png");
    }
}