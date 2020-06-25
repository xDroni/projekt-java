package sample;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;

public class Controller {
    public Canvas canvas;
    private Game game = null;

    @FXML
    private Label pointsLabel;

    @FXML
    private Label healthLabel;

    @FXML
    private Label timerLabel;

    private boolean steeringLeft = false;
    private boolean steeringRight = false;

    public void initListeners(Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode().toString()) {
                case "A":
                case "LEFT": {
                    steeringLeft = true;
                    break;
                }

                case "D":
                case "RIGHT": {
                    steeringRight = true;
                    break;
                }
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode().toString()) {
                case "A":
                case "LEFT": {
                    steeringLeft = false;
                    break;
                }

                case "D":
                case "RIGHT": {
                    steeringRight = false;
                    break;
                }
            }
        });
    }

    @FXML
    public void initialize() {
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        timerLabel.setText("");

        game = new Game(() -> {
            System.out.println("GAME OVER");
        }, totalPoints -> {
            System.out.println("POINTS: " + totalPoints);
            pointsLabel.setText(String.valueOf(totalPoints));
        }, newHealth -> {
            System.out.println("New health: " + newHealth);
            healthLabel.setText(String.valueOf(newHealth));
        }, newTime -> {
            System.out.println("Time: " + newTime);
            timerLabel.setText(String.valueOf(newTime));
        });

        Renderer renderer = new Renderer(ctx, canvas.getWidth(), canvas.getHeight());

        final long[] last = {0};

        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                if (!game.isRunning()) {
                    return;
                }
                double delta = (currentNanoTime - last[0]) / 1000000000.0;
                game.update(Math.min(delta, 1.0), steeringLeft, steeringRight);
                renderer.render(game);

                last[0] = currentNanoTime;
            }
        }.start();
    }

    public void startGame() {
        pointsLabel.setText("0");
        healthLabel.setText("3");
        timerLabel.setText("0");
        game.startGame();
    }
}
