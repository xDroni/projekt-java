package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Score {
    private final SimpleStringProperty nickname;
    private final SimpleIntegerProperty points;
    private final SimpleIntegerProperty time;


    Score(String nickname, int score, int time) {
        this.nickname = new SimpleStringProperty(nickname);
        this.points = new SimpleIntegerProperty(score);
        this.time = new SimpleIntegerProperty(time);
    }

    public SimpleStringProperty nicknameProperty() {
        return nickname;
    }

    public SimpleIntegerProperty pointsProperty() {
        return points;
    }

    public SimpleIntegerProperty timeProperty() {
        return time;
    }


}
