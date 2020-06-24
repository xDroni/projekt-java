package sample;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;

public class Controller {
    public Canvas canvas;
    private Game game = null;

    private boolean steeringLeft = false;
    private boolean steeringRight = false;

    public void initListeners(Scene scene) {
        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode().toString()) {
                    case "A":
                    case "LEFT":
                        steeringLeft = true;
                    break;

                    case "D":
                    case "RIGHT":
                        steeringRight = true;
                    break;
                }
            }
        });
        scene.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode().toString()) {
                    case "A":
                    case "LEFT":
                        steeringLeft = false;
                    break;

                    case "D":
                    case "RIGHT":
                        steeringRight = false;
                    break;
                }
            }
        });
    }

    @FXML
    public void initialize() {
        GraphicsContext ctx = canvas.getGraphicsContext2D();

        game = new Game(() -> {
            System.out.println("GAME OVER");
        }, totalPoints -> {
            System.out.println("POINTS: " + totalPoints);
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

    @FXML
    public void startGame() {
        game.startGame();
    }
}
