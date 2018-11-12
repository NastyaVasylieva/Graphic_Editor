package src.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import src.Model.Point;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EventController implements Initializable{

    public List<Point> pointList = new ArrayList<Point>();

    public enum Mode{
        ADDING_POINTS,
        CHECKING_POINT
    }

    Mode mode = Mode.ADDING_POINTS;

    @FXML
    Button addPointButton;

    @FXML
    Button clearButton;

    @FXML
    TextField newX;

    @FXML
    TextField newY;

    @FXML
    Button generateConvexHullButton;

    @FXML
    Canvas canvas;

    @FXML
    Label progressBar;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        addPointButton.setOnAction(event -> {
            Point point = new Point(Double.parseDouble(newX.getText()), Double.parseDouble(newY.getText()));
            pointList.add(point);
            canvas.getGraphicsContext2D().setFill(Color.rgb(184,72,72));
            canvas.getGraphicsContext2D().fillOval(point.getX(), point.getY(),5,5);
        });

        canvas.setOnMouseClicked(event -> {
            pointList.add(new Point(event.getX(), event.getY()));
            canvas.getGraphicsContext2D().setFill(Color.rgb(184,72,72));
            canvas.getGraphicsContext2D().fillOval(event.getX(), event.getY(),5,5);
        });

        clearButton.setOnAction(event -> {
            double width = canvas.getWidth();
            double height = canvas.getHeight();
            canvas.getGraphicsContext2D().clearRect(0,0, width, height);

            pointList.clear();
        });

        canvas.setOnMouseMoved(event -> {
            String current_mode= "";

            switch (mode){
                case ADDING_POINTS: current_mode="Режим: Додавання точок"; break;
                case CHECKING_POINT: current_mode="Режим: Перевірка точки на належність оболонці"; break;
            }
            progressBar.setText("x: " + Math.round(event.getX()) + " y: " + Math.round(event.getY())+"   " + current_mode);
        });
    }
}
