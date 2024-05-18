package main.Game.Zombies;
import main.GUI.KeyHandler;
import main.GUI.WindowPanel;
import main.Game.ParentClass.Zombie;

public class NewspaperZombie extends Zombie {
    private boolean hasNewspaper = true;
    public NewspaperZombie(WindowPanel wp) {
        super(wp);
        getZombieImage("src/main/Resources/Zombies/newspaper.png");
        name = "Newspaper Zombie";
        health = 375;
        attack_damage = 200;
        attack_speed = 1;
    }

    public void loseNewspaper(){
        hasNewspaper = false;
        speedIncrease();
        getZombieImage("src/main/Resources/Zombies/nonewspaper.png");
    }
}
