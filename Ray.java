public class Ray {
    public double playerx;
    public double playery;
    public double rx;
    public double ry;
    public double angle;
    public static double pi = 3.14159265359;

    public Ray(double angle, double playerx, double playery){
        this.angle = angle;
        this.playerx=playerx;
        this.playery=playery;
        this.ajusteAngle();
    }

    public void ajuste(){
        if (this.rx < 0) {
            this.rx = 0;
        }
        if (this.rx > 15 * 48) {
            this.rx = 15 * 48;
        }
        if (this.ry < 0) {
            this.ry = 0;
        }
        if (this.ry > 11 * 48) {
            this.ry = 11 * 48;
        }
    }

    public void ajusteAngle(){
        if (this.angle>pi) {
            this.angle-=2*pi;
        }
        if (this.angle<-pi) {
            this.angle+=2*pi;
        }
    }

    public void trouveIntersectionHaut(){
        ry = ((int) (playery / 48)) * 48;
        rx = playerx - Math.tan(pi / 2 - angle) * (playery - ry);
        this.ajuste();
    }
    public void agrandiHaut(int[][] map){
        while (map[(int) (ry / 48) - 1][(int) (rx / 48)] != 1) {
            this.ry = this.ry - 48;
            this.rx = this.rx - Math.tan(pi / 2 - angle) * 48;
            this.ajuste();
        }
    }

    public void trouveIntersectionBas(){
        ry = (((int) (playery / 48)) + 1) * 48;
        rx = playerx - Math.tan(pi / 2 - angle) * (playery - ry);
        this.ajuste();
    }
    public void agrandiBas(int[][] map){
        while (map[(int) (ry / 48)][(int) (rx / 48)] != 1) {
            ry = ry + 48;
            rx = rx + Math.tan(pi / 2 - angle) * 48;
            this.ajuste();
        }
    }

    public void trouveIntersectionDroite(){
        rx = (((int) (playerx / 48)) + 1) * 48;
        ry = playery + Math.tan(angle) * (rx - playerx);
        this.ajuste();
    }
    public void agrandiDroite(int[][] map){
        while (map[(int) (ry / 48)][(int) (rx / 48)] != 1) {
            rx= rx + 48;
            ry = ry + Math.tan(angle) * 48;
            this.ajuste();
        }
    }

    public void trouveIntersectionGauche(){
        rx = ((int) (playerx / 48)) * 48;
        ry = playery + Math.tan(angle) * (rx - playerx);
        this.ajuste();
    }
    public void agrandiGauche(int[][] map){
        while (map[(int) (ry / 48)][(int) (rx / 48) - 1] != 1) {
            rx = rx - 48;
            ry = ry - Math.tan(angle) * 48;
            this.ajuste();
        }
    }

    public void trouveIntersectionPi(){
        ry=playery;
        rx=playerx - (playerx-(playerx/48));
    }

    public void agrandiPi(int[][] map){
        while (map[(int) (ry / 48)][(int) (rx / 48) ] != 1) {
            rx = rx - 48;
            this.ajuste();
        }
    }

    public double distance(){
        return Math.sqrt((playerx - rx) * (playerx - rx) + (playery - ry) * (playery - ry));
    }
}
