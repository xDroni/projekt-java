package sample;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {
    public Canvas canvas;
    private Game game = null;
    private final Database database = new Database("main.db");

    @FXML
    private Label pointsLabel;

    @FXML
    private Label healthLabel;

    @FXML
    private Label timerLabel;

    @FXML
    private Label gameOverLabel;

    @FXML
    private TextField nicknameInput;

    @FXML
    private Button saveButton;

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
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        timerLabel.setText("");

        initTable();
        updateTable();

        game = new Game(() -> {
            System.out.println("GAME OVER");
            gameOverLabel.setVisible(true);
            nicknameInput.setVisible(true);
            nicknameInput.requestFocus();
            saveButton.setVisible(true);
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

    private void initTable() {
        database.connect();
        highscoresTable.setEditable(true);

        nicknameColumn.setPrefWidth(180);
        nicknameColumn.setCellValueFactory(
                new PropertyValueFactory<>("nickname"));

        scoreColumn.setPrefWidth(59);
        scoreColumn.setSortType(TableColumn.SortType.DESCENDING);
        scoreColumn.setCellValueFactory(
                new PropertyValueFactory<>("points"));

        timeColumn.setPrefWidth(59);
        timeColumn.setCellValueFactory(
                new PropertyValueFactory<>("time"));

        highscoresTable.getColumns().addAll(nicknameColumn, scoreColumn, timeColumn);
    }

    private void updateTable() {
        ResultSet highscores = database.getHighscores();
        ObservableList<Score> data = FXCollections.observableArrayList();

        try {
            while (highscores.next()) {
                data.add(new Score(highscores.getString("Nickname"), highscores.getInt("Points"), highscores.getInt("Time")));
//                System.out.println(highscores.getString("Nickname") + ": " + highscores.getInt("Points") + ": " + highscores.getInt("Time"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        highscoresTable.setItems(data);
        highscoresTable.getSortOrder().add(scoreColumn);
    }

    public void startGame() {
        pointsLabel.setText("0");
        healthLabel.setText("3");
        timerLabel.setText("0");
        pointsLabel.setVisible(true);
        healthLabel.setVisible(true);
        timerLabel.setVisible(true);
        gameOverLabel.setVisible(false);
        nicknameInput.setVisible(false);
        saveButton.setVisible(false);
        game.startGame();
    }

    public void addNewScoreInput(KeyEvent keyEvent) {
        if (keyEvent.getCode().toString().equals("ENTER")) {
            if(!nicknameInput.getText().trim().isEmpty()) {
                database.addNewScore(nicknameInput.getText(), (int) game.getPoints(), (int) game.getTimer());
                updateTable();
                clearGUI();
            }
        }
    }

    public void addNewScoreButton() {
        if(!nicknameInput.getText().trim().isEmpty()) {
            database.addNewScore(nicknameInput.getText(), (int) game.getPoints(), (int) game.getTimer());
            updateTable();
            clearGUI();
        }
    }

    private void clearGUI() {
        gameOverLabel.setVisible(false);
        nicknameInput.setVisible(false);
        saveButton.setVisible(false);
        pointsLabel.setVisible(false);
        healthLabel.setVisible(false);
        timerLabel.setVisible(false);
    }
}
