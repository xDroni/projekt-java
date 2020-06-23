package sample;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Controller {
    public static int SIZE = 10;

    public Canvas canvas;

    @FXML
    public void initialize() {
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        ctx.setFill(Color.BLUEVIOLET);

        SquareMovement[] squareMovements = new SquareMovement[SIZE];
        for (int i = 0; i < SIZE; i++) {
            squareMovements[i] = new SquareMovement();
            squareMovements[i].setDaemon(true);
            squareMovements[i].start();
        }

        double w = canvas.getWidth() / (double) SIZE;
        double h = canvas.getHeight() / (double) SIZE;

        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                for (int i = 0; i < SIZE; i++) {
                    ctx.fillOval(i * h, squareMovements[i].getX() * w, w, h);
                    ctx.fillRect(canvas.getWidth() / (SIZE / 2f), canvas.getHeight() - 10, 100, 10);

                }
            }
        }.start();
    }
}
