package sample;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    public Canvas canvas;
    private Game game = null;

    @FXML
    private Label pointsLabel;

    @FXML
    private Label healthLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private TableView<Score> highscoresTable = new TableView<>();

    @FXML
    private TableColumn<Score, String> nicknameColumn = new TableColumn<>("Nickname");

    @FXML
    private TableColumn<Score, Integer> scoreColumn = new TableColumn<>("Punkty");

    @FXML
    private TableColumn<Score, Integer> timeColumn = new TableColumn<>("Czas");


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
        highscoresTable.setEditable(true);
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        timerLabel.setText("");

        Database database = new Database("main.db");
        database.connect();
//        database.addNewScore("asd", 9, 32);
        ResultSet highscores = database.getHighscores();
        ObservableList<Score> data = FXCollections.observableArrayList();

        nicknameColumn.setMinWidth(200);
        nicknameColumn.setCellValueFactory(
                new PropertyValueFactory<>("nickname"));

        scoreColumn.setMinWidth(50);
        scoreColumn.setCellValueFactory(
                new PropertyValueFactory<>("points"));

        timeColumn.setMinWidth(50);
        timeColumn.setCellValueFactory(
                new PropertyValueFactory<>("time"));


        try {
            while(highscores.next()) {
                data.add(new Score(highscores.getString("Nickname"), highscores.getInt("Points"), highscores.getInt("Time")));
                System.out.println(highscores.getString("Nickname") + ": " + highscores.getInt("Points") + ": " + highscores.getInt("Time"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        highscoresTable.setItems(data);
        highscoresTable.getColumns().addAll(nicknameColumn, scoreColumn, timeColumn);


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
