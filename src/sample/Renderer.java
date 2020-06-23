package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sample.common.Object;

public class Renderer {
    private GraphicsContext ctx;
    private final double width, height;

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
        ctx.clearRect(0, 0, width, height);

        ctx.setFill(Color.RED);
        for(Object bubble : game.getBubbles()) {
            drawCircle(bubble);
        }

        ctx.setFill(Color.BLUEVIOLET);
        drawRect(game.getPlatform());
    }
}
