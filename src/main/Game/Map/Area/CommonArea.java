package main.Game.Map.Area;

import main.GUI.WindowPanel;
import main.Game.ParentClass.Area;

import java.awt.*;

public class CommonArea extends Area {
    WindowPanel wp;
    Graphics2D g2;

    public CommonArea(WindowPanel wp) {
        super(wp);
        getTileImage("src/main/Resources/Tiles/grassTile.png");
    }

    public void draw(Graphics2D g2) {
        // tileSize is 60
        // 0,0 itu pojok kiri atas
        for(int i=0; i<9; i++) {
            draw(g2, (1+i)*60, 1*60);
        }
        for(int i=0; i<9; i++) {
            draw(g2, (1+i)*60, 2*60);
        }
        for(int i=0; i<9; i++) {
            draw(g2, (1+i)*60, 5*60);
        }
        for(int i=0; i<9; i++) {
            draw(g2, (1+i)*60, 6*60);
        }
    }
}
