package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.common.Object;

import java.util.function.Consumer;

public class Renderer {
    private final GraphicsContext ctx;
    private final double width, height;

    private static final class Palette {
        static final Color BACKGROUND = Color.rgb(0, 172, 193);
        static final Color PADDLE = Color.rgb(0, 96, 100);
        static final Color BUBBLE = Color.rgb(77, 208, 225);
        static final Color MEAL = Color.rgb(197, 225, 165);
    }

    public Renderer(GraphicsContext ctx, final double width, final double height) {
        this.ctx = ctx;
        this.width = width;
        this.height = height;
    }

    public void drawRect(Object obj) {
        ctx.fillRect((obj.x - obj.width / 2.0) * width, (obj.y - obj.height / 2.0) * height, obj.width * width, obj.height * height);
    }

    public void drawCircle(Object obj) {
        ctx.fillOval((obj.x - obj.width / 2.0) * width, (obj.y - obj.height / 2.0) * height, obj.width * width, obj.height * height);
    }

    public void render(Game game) {
        ctx.setFill(Palette.BACKGROUND);
        ctx.fillRect(0, 0, width, height);

        ctx.setFill(Palette.BUBBLE);
        game.getBubbles().forEach((Consumer<Object>) this::drawCircle);

        ctx.setFill(Palette.MEAL);
        game.getMeals().forEach((Consumer<Object>) this::drawCircle);

        ctx.setFill(Palette.PADDLE);
        drawRect(game.getPaddle());
    }
}
