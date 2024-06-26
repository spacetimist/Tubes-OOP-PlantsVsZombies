package main.GUI;

import main.Game.*;
import main.Game.Map.Map;
import main.Game.Menu;
import main.Game.ParentClass.Plant;
import main.Game.ParentClass.Zombie;
import main.Game.Plants.*;
import main.Game.Zombies.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class WindowPanel extends JPanel implements Runnable {
    final int originalTileSize = 20;

    // scaling
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 11; // horizontally
    final int maxScreenRow = 8; // vertically
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    public int fps = 3;
    public State state = new State(this);
    public Map map = new Map(this);
    KeyHandler kh = new KeyHandler(this);
    Thread gameThread;
    public Sun sun = new Sun(this);
    public boolean isMorning;

    // instantiate collision
    public Collision collision = new Collision(this);

    // instantiate plant list, zombie list (yg di map)
    public ArrayList<Plant> PlantList = new ArrayList<>();
    public ArrayList<Zombie> ZombieList = new ArrayList<>();
    // deck and inventory
    public ArrayList<Plant> Deck = new ArrayList<>(6);
    public ArrayList<Plant> Inventory = new ArrayList<>(10);
    // batch
    public Zombie Batch[] = new Zombie[6];

    // instantiate setter
    Planting planting = new Planting(this);

    ZombieSpawn zSpawn = new ZombieSpawn(this);
    Inventory inv = new Inventory(this);
    // game state
    public int gameState;
    public final int playState = 1;
    public final int inventoryState = 2;
    public final int deckState = 3;
    public final int plantingState = 4;
    public final int menuState = 5;
    public final int finished = 6;

    //LIST OF ALL CHARACTERS
    public List<Plant> plantList = new ArrayList<>();
    public List<Zombie> zombieList = new ArrayList<>();
    Menu<Plant> plantMenu = new Menu<>(this, plantList);
    Menu<Zombie> zombieMenu = new Menu<>(this, zombieList);
    int i;
    public int xValue, yValue;
    public WindowPanel() {
        i = 0;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(kh);
        this.setFocusable(true);
    }

    public void initializeCharacters() {
        plantList.add(new Cactus(this));
        plantList.add(new Jalapeno(this));
        plantList.add(new Lilypad(this));
        plantList.add(new PeaShooter(this));
        plantList.add(new Repeater(this));
        plantList.add(new SnowPea(this));
        plantList.add(new Squash(this));
        plantList.add(new Sunflower(this));
        plantList.add(new Tallnut(this));
        plantList.add(new Wallnut(this));

        zombieList.add(new BalloonZombie(this));
        zombieList.add(new BucketheadZombie(this));
        zombieList.add(new ConeheadZombie(this));
        zombieList.add(new DolphinRiderZombie(this));
        zombieList.add(new DuckyTubeZombie(this));
        zombieList.add(new FootballZombie(this));
        zombieList.add(new NewspaperZombie(this));
        zombieList.add(new NormalZombie(this));
        zombieList.add(new PoleVaultingZombie(this));
        zombieList.add(new ScreenDoorZombie(this));
    }

    public void openInputDialog() {
        JDialog dialog = new JDialog((Frame) null, "Input Variables", true);
        dialog.setSize(300, 200);
        dialog.setLayout(null);

        JLabel xLabel = new JLabel("Enter x:");
        xLabel.setBounds(20, 20, 80, 25);
        dialog.add(xLabel);

        JTextField xField = new JTextField();
        xField.setBounds(100, 20, 160, 25);
        dialog.add(xField);

        JLabel yLabel = new JLabel("Enter y:");
        yLabel.setBounds(20, 60, 80, 25);
        dialog.add(yLabel);

        JTextField yField = new JTextField();
        yField.setBounds(100, 60, 160, 25);
        dialog.add(yField);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(100, 100, 80, 25);
        dialog.add(submitButton);

        submitButton.addActionListener(e -> {
            try {
                xValue = Integer.parseInt(xField.getText());
                yValue = Integer.parseInt(yField.getText());
                System.out.println("x: " + xValue + ", y: " + yValue);  // Cetak nilai ke konsol
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid integers.");
            }
        });

        dialog.setVisible(true);

    }

    public void swapPositionInputDialog() {
        JDialog dialog = new JDialog((Frame) null, "Swap Position", true);
        dialog.setSize(300, 200);
        dialog.setLayout(null);

        JLabel indexLabel1 = new JLabel("Enter index 1:");
        indexLabel1.setBounds(20, 20, 100, 25);
        dialog.add(indexLabel1);

        JTextField indexField1 = new JTextField();
        indexField1.setBounds(150, 20, 50, 25);
        dialog.add(indexField1);

        JLabel indexLabel2 = new JLabel("Enter index 2:");
        indexLabel2.setBounds(20, 60, 100, 25);
        dialog.add(indexLabel2);

        JTextField indexField2 = new JTextField();
        indexField2.setBounds(150, 60, 50, 25);
        dialog.add(indexField2);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(100, 100, 80, 25);
        dialog.add(submitButton);

        submitButton.addActionListener(e -> {
            try {
                int index1 = Integer.parseInt(indexField1.getText());
                int index2 = Integer.parseInt(indexField2.getText());
                System.out.println("Swap index " + index1 + " with " + index2);  // Cetak indeks ke konsol
                inv.swapPosition(index1, index2); // Panggil metode untuk menukar posisi
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid indexes.");
            }
        });

        dialog.setVisible(true);
    }


    public void setUp() {
        gameState = menuState; // initial state
        initializeCharacters();
        inv.set();
    }
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        zSpawn.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/fps;
        double nextDrawTime = System.nanoTime() + drawInterval;
        while(gameThread != null) {
            // System.out.println("The loop is running");
            update();
            repaint();
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000;
                if(remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long)remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void update() {
        if(gameState == playState || gameState == plantingState) {
            isMorning = (state.playTime % 200) <= 100;
            map.setTiles(isMorning);
            for (Zombie zombie : ZombieList) {
                zombie.update();
            }
            for (Plant  plant : PlantList) {
                plant.update();
            }
            sun.startGeneratingSun();
        }else{
            sun.stopGeneratingSun();
        }
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        if(gameState == menuState) {
            state.draw(g2);
        }
        if(gameState == inventoryState) {
            state.draw(g2);
        }
        if(gameState == deckState) {
            state.draw(g2);
        }
        if(gameState == playState || gameState == plantingState) {
            map.draw(g2);

            state.draw(g2);

            for (Zombie zombie : ZombieList) {
                zombie.draw(g2);
            }

        }
        if(gameState == finished) {
            map.draw(g2);
            state.draw(g2);
            for (Zombie zombie : ZombieList) {
                zombie.draw(g2);
            }
        }

        g2.dispose();
    }

}