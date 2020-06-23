package sample;

import java.util.Random;

public class SquareMovement extends Thread {
    private int x;
    private int speed;

    SquareMovement() {
        this.x = 0;

        Random r = new Random();
        this.speed = 60 + r.nextInt(2000 - 60);
    }

    public int getX() {
        return x;
    }

    @Override
    public void run() {
        do {
            try {
                Thread.sleep(this.speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (Controller.SIZE - 1 != ++this.x);
    }
}
