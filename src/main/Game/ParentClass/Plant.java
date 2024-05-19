package main.Game.ParentClass;

import main.GUI.KeyHandler;
import main.GUI.WindowPanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Plant extends Character {
    WindowPanel wp;
    KeyHandler kh;
    // attributes
    public int cost, range, cooldown;

    public Rectangle solidArea = new Rectangle(0, 0, 60, 60);
    public Plant(WindowPanel wp, KeyHandler kh) {
        this.wp = wp;
        this.kh = kh;
        collision = true;

        solidAreaDefaultX = 0;
        solidAreaDefaultY = 0;

        setDefaultValues(1, 1);
    }

    public void setDefaultValues(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void getPlantImage(String imgPath) {
        try {
            img = ImageIO.read(new File(imgPath));
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2){
        BufferedImage image = img;
        g2.drawImage(image, x*60, y*60, null);
    }
}
