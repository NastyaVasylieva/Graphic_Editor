package src.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import src.Model.ConvexHull;
import src.Model.Polygon;
import src.Model.Point;
import src.View.HelpView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EventController implements Initializable{

    //Список точок, які користувач додає на поле в режимі "Додавання точок"
    public List<Point> pointList = new ArrayList<>();

    //Згенерована опукла множина
    public ConvexHull convexHull = new ConvexHull();

    //Список точок, які користувач додає на поле в режимі "Перевірка точки на належність оболонці"
    public List<Point> checkingPoints = new ArrayList<>();

    //Об'єкт класу Полігон
    Polygon polygon = new Polygon();

    public static int id=1;

    //Перелічення можливих режимів роботи програми.
    public enum Mode{
        ADDING_POINTS,
        CHECKING_POINT,
        CREATING_HULL
    }

    Mode mode = Mode.ADDING_POINTS;
    String current_mode= "";

    @FXML Button addPointButton;

    @FXML Button clearButton;

    @FXML TextField newX;

    @FXML TextField newY;

    @FXML Button generateConvexHullButton;

    @FXML Canvas canvas;

    @FXML ToolBar toolBar;

    @FXML Label labelXY;

    @FXML Label modeLabel;

    @FXML TableView<Point> tableView;

    @FXML TableColumn<Point, Integer> idColumn;

    @FXML TableColumn<Point, Double> xColumn;

    @FXML TableColumn<Point, Double> yColumn;

    @FXML MenuItem helpMenu;

    private ObservableList<Point> pointObservableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources){

        idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        xColumn.setCellValueFactory(new PropertyValueFactory<>("X"));
        yColumn.setCellValueFactory(new PropertyValueFactory<>("Y"));

        tableView.setItems(pointObservableList);

        //Ручне додавання точки
        addPointButton.setOnAction(event -> {
            Point point;
            if(mode == Mode.CHECKING_POINT){
                try {
                    point = new Point(Double.parseDouble(newX.getText()), Double.parseDouble(newY.getText()));
                    addCheckPoint(point);
                } catch (Exception e) {
                    showWarning("Помилка при введенні координат!");
                }
            }else {
                mode = Mode.ADDING_POINTS;
                try {
                    point = new Point(Double.parseDouble(newX.getText()), Double.parseDouble(newY.getText()));
                    addSimplePoint(point, point.getX(), point.getY());
                } catch (Exception e) {
                    showWarning("Помилка при введенні координат!");
                }

            }
            newX.clear();
            newY.clear();
        });

        //Автоматичне додавання точки по кліку на поле
        canvas.setOnMouseClicked(event -> {
            Point point = new Point(event.getX(), event.getY());

            if(mode == Mode.CHECKING_POINT){
                addCheckPoint(point);
            }else {
                mode = Mode.ADDING_POINTS;
                checkMode(mode);
                modeLabel.setText(current_mode);
                addSimplePoint(point, event.getX(), event.getY());
            }
        });

        //Очищення поля
        clearButton.setOnAction(event -> {
            try {
                if(pointList.isEmpty()) throw new Exception("Поле вже пусте!");
                pointList.clear();
                checkingPoints.clear();
            }catch (Exception e){
                showWarning(e.getMessage());
            }
            pointObservableList.clear();
            id=1;
            clearCanvas();
            mode = Mode.ADDING_POINTS;
        });

        //Зміна інформації в рядку стану
        canvas.setOnMouseMoved(event -> {
            checkMode(mode);
            labelXY.setText("x: " + Math.round(event.getX()) + " y: " + Math.round(event.getY()));
            modeLabel.setText(current_mode);
        });

        //Створення опуклої оболонки та її відмальовка
        generateConvexHullButton.setOnAction(event -> {
            if(mode == Mode.CHECKING_POINT){
                showWarning("Оболонка вже побудована!");
            }else {
                mode = Mode.CREATING_HULL;
                checkMode(mode);
                modeLabel.setText(current_mode);

                convexHull.clear();
                try {
                    convexHull = polygon.getConvexHull(pointList);
                    drawConvexHull(convexHull);
                    redrawPoints(pointList);
                    mode = Mode.CHECKING_POINT;

                } catch (Exception e) {
                    showWarning(e.getMessage());
                }
            }
        });

        helpMenu.setOnAction(event -> {
            Stage helpStage = new Stage();
            HelpView helpView = new HelpView();
            try{
                helpView.start(helpStage);
            }catch (Exception e){}
        });

    }

    //Додавання звичайної точки в режимі "Додавання точок"
    public void addSimplePoint(Point point, double x, double y) {
        try{
            checkIfExist(pointList,point);
            pointList.add(point);
            pointObservableList.add(new Point(id, x, y));
            id++;
            canvas.getGraphicsContext2D().setFill(Color.rgb(184, 72, 72));
            canvas.getGraphicsContext2D().fillOval(x - 2, y - 2, 4, 4);
        }catch (Exception e){
            showWarning(e.getMessage());
        }
    }

    //Додавання перевірочної точки в режимі "Перевірка точки на належність оболонці"
    public void addCheckPoint(Point point) {
        try {
            checkIfExist(checkingPoints,point);
            checkingPoints.add(point);
            drawCheckPoint(point);
            pointObservableList.add(new Point(id,point.getX(),point.getY()));
            id++;
        }catch (Exception e){
            showWarning(e.getMessage());
        }
    }

    //Відмальовка знайденої опуклої оболонки
    public void drawConvexHull(ConvexHull convexHull){
        clearCanvas();

        canvas.getGraphicsContext2D().setStroke(Color.rgb(221,221,221));

        for(int i=0;i<convexHull.getSize()-1;i++){
            Point currentStartPoint = convexHull.getPoint(i);
            System.out.println("x "+currentStartPoint.getX()+" y "+currentStartPoint.getY());
            Point currentEndPoint = convexHull.getPoint(i+1);
            canvas.getGraphicsContext2D().strokeLine(currentStartPoint.getX(), currentStartPoint.getY(),
                                                     currentEndPoint.getX(), currentEndPoint.getY());
        }
    }

    //Перемальовування точок
    public void redrawPoints(List<Point> pointList){
        for(int i=0;i<pointList.size();i++){
            canvas.getGraphicsContext2D().setFill(Color.rgb(184,72,72));
            canvas.getGraphicsContext2D().fillOval(pointList.get(i).getX()-2,
                                                    pointList.get(i).getY()-2,4,4);
        }
    }

    //Малювання точок різними кольорами в залежност від їх розташування
    public void drawCheckPoint(Point point){
        if(polygon.isInside(point, convexHull)){
            canvas.getGraphicsContext2D().setFill(Color.rgb(184, 72, 72));
        }
        if(!polygon.isInside(point, convexHull)) {
            canvas.getGraphicsContext2D().setFill(Color.rgb(186, 186, 186));
        }
        canvas.getGraphicsContext2D().fillOval(point.getX() - 2, point.getY() - 2, 4, 4);

    }

    //Очищення поля
    public void clearCanvas(){

        double width = canvas.getWidth();
        double height = canvas.getHeight();
        canvas.getGraphicsContext2D().clearRect(0,0, width, height);
    }

    //Перевірка поточного режиму
    public void checkMode(Mode mode){
        switch (mode){
            case ADDING_POINTS: current_mode="Режим: Додавання точок"; break;
            case CHECKING_POINT: current_mode="Режим: Перевірка точки на належність оболонці"; break;
            case CREATING_HULL: current_mode="Режим: Створення опуклої оболонки"; break;
        }
    }

    //Показування сповіщення з небезпекою
    public void showWarning(String text){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(text);
        alert.showAndWait();
    }

    //Перевірка, чи точка уже існує в списку
    public void checkIfExist(List<Point> points,Point point) throws Exception{
        for(int i=0;i<points.size();i++){
            if(points.get(i).equals(point)){
                throw new Exception("Точка вже існує!");
            }
        }
    }
}
