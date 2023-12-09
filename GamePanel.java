import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    final int originalTileSize = 16;
    final int scale=3;
    final int tileSize = originalTileSize * scale;
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol;
    final int screenHeight = tileSize * maxScreenRow;

    double pi =3.14159265359;

    int[] map1 = {
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
    };
    int[] map2 = {
            1,0,0,0,0,0,1,1,0,0,1,1,0,0,0,1,
    };
    int[] map3 = {
            1,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1,
    };
    int[] map4 = {
            1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1,
    };
    int[] map5 = {
            1,0,0,0,1,0,0,0,1,0,0,0,0,0,0,1,
    };
    int[] map6 = {
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
    };
    int[] map7 = {
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
    };
    int[] map8 = {
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
    };
    int[] map9 = {
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
    };
    int[] map10 = {
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
    };
    int[] map11 = {
            1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,
    };
    int[] map12 = {
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1
    };
    int[][] map = {map1,map2,map3,map4,map5,map6,map7,map8,map9,map10,map11,map12};

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;

    // set defautl position
    double playerx=100;
    double playery=100;
    double angle=-pi/2;
    double dx;
    double dy;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this); // lance thread du panel
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000/60; // pour 60 fps
        double nextDrawTime = System.nanoTime()+drawInterval;
        while(gameThread != null) {

            /* boucle du jeux */
            update();
            repaint();

            /* pour 60 fps */
            try {
                double remainTime = nextDrawTime - System.nanoTime();
                remainTime = remainTime/1000000;
                if(remainTime<0) {
                    remainTime=0;
                }
                Thread.sleep((long) remainTime);
                nextDrawTime += drawInterval;
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {

        if(keyH.left) {
            angle-=0.05;
            if (angle<-pi) {
                angle=pi;
            }
            dx=(Math.cos(angle)*5);
            dy=(Math.sin(angle)*5);
        }
        else if(keyH.up) {
            playerx+=dx;
            playery+=dy;
        }
        else if(keyH.right) {
            angle+=0.05;
            if (angle>pi) {
                angle=-pi;
            }
            dx=(Math.cos(angle)*5);
            dy=(Math.sin(angle)*5);
        }
        else if(keyH.down) {
            playerx-=dx;
            playery-=dy;
        }
    }


    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        /* affichage des carrés la map */
        for(int i=0; i<12; i++) {
            for(int j=0; j<16; j++) {
                if (map[i][j]==1){
                    g2.setColor(Color.blue);
                    g2.fillRect(j*48, i*48, 47, 47); // 47 POUR AVOIR LES LIGNES DE LA GRILLE
                }
                else {
                    g2.setColor(Color.gray);
                    g2.fillRect(j*48, i*48, 47, 47);
                }
            }
        }

        /* affichage d'un rayon */

        g2.setColor(Color.red);

        for (double i=-50; i<=50; i++) {
            //un rayon check les collision verticale et un autre ls horizontales
            Ray rayonHorizontale = new Ray(angle+i/60, playerx, playery);
            Ray rayonVerticale = new Ray(rayonHorizontale.angle, playerx, playery);

            // collision horizontale


            if (rayonHorizontale.angle==pi || rayonHorizontale.angle==-pi){
                System.out.println("rayon pi -pi");
                rayonHorizontale.rx=10000000;
                rayonHorizontale.ry=10000000;
            }
            else if (rayonHorizontale.angle < 0) {

                rayonHorizontale.trouveIntersectionHaut();
                rayonHorizontale.agrandiHaut(map);
            }
            else {
                rayonHorizontale.trouveIntersectionBas();
                rayonHorizontale.agrandiBas(map);
            }
            // collision verticale

            if (rayonVerticale.angle==pi/2 || rayonVerticale.angle==-pi/2){
                System.out.println("rayon pi -pi");
                rayonVerticale.rx=-10000000;
                rayonVerticale.ry=-10000000;
            }
            else if (rayonVerticale.angle > -pi / 2 && rayonVerticale.angle < pi / 2) {
                rayonVerticale.trouveIntersectionDroite();
                rayonVerticale.agrandiDroite(map);
            }
            else {
                rayonVerticale.trouveIntersectionGauche();
                rayonVerticale.agrandiGauche(map);
            }



            //affiche le plus petit
            if (rayonHorizontale.distance() > rayonVerticale.distance()) {
                g2.drawLine((int) (playerx), (int) (playery), (int) (rayonVerticale.rx), (int) (rayonVerticale.ry));
            } else {
                g2.drawLine((int) (playerx), (int) (playery), (int) (rayonHorizontale.rx), (int) (rayonHorizontale.ry));
            }
        }


        /* affichage du joueur et de sa direction */
        g2.setColor(Color.white);
        g2.drawLine((int)(playerx),(int)(playery),(int)(playerx+dx),(int)(playery+dy));
        g2.fillRect((int)(playerx),(int) (playery), 1, 1);

        g2.dispose();
    }
}

