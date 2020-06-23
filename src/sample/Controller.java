package sample;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Controller {
    public Canvas canvas;
    private Game game = null;

    private boolean steeringLeft = false;
    private boolean steeringRight = false;

    public void initListeners(Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case A, LEFT -> steeringLeft = true;
                case D, RIGHT -> steeringRight = true;
            }
        });

        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case A, LEFT -> steeringLeft = false;
                case D, RIGHT -> steeringRight = false;
            }
        });
    }

    @FXML
    public void initialize() {
        GraphicsContext ctx = canvas.getGraphicsContext2D();

        game = new Game();
        Renderer renderer = new Renderer(ctx, canvas.getWidth(), canvas.getHeight());

        final long[] last = {0};

        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                if(!game.running) {
                    return;
                }
                game.update((currentNanoTime - last[0]) / 1000000000.0, steeringLeft, steeringRight);
                renderer.render(game);

                last[0] = currentNanoTime;
            }
        }.start();
    }
}
