package sample;
//http://docs.oracle.com/javase/8/javafx/user-interface-tutorial/ui_controls.htm#JFXUI336
//http://docs.oracle.com/javase/8/javafx/api/
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.*;
import javafx.geometry.Orientation;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;

public class Main extends Application {
    private static final String OUTSIDE_TEXT = "Outside Label";
    private static double iXCoor, iYCoor;

    public static void main(String[] args) { launch(args); }

    @Override public void start(final Stage stage) {
        final Label reporter = new Label(OUTSIDE_TEXT);
        Label monitored = createMonitoredLabel(reporter);
        ColorPicker colorPicker1 = new ColorPicker(Color.BLACK);
        ColorPicker colorPickerBackground = new ColorPicker(Color.WHITE);
        ToggleButton drawTogg = new ToggleButton("Regular Draw");
        ToggleButton raysTogg = new ToggleButton("Rays");
        ToggleButton eraserTogg = new ToggleButton("Eraser");
        ToggleGroup group = new ToggleGroup();
        drawTogg.setToggleGroup(group);
        raysTogg.setToggleGroup(group);
        eraserTogg.setToggleGroup(group);
        drawTogg.setSelected(true);
        Button clearButt = new Button("Clear");
        TextField brushSize = new TextField("5");
        Canvas canvas = createCanvas(colorPicker1, colorPickerBackground, clearButt, drawTogg, raysTogg, eraserTogg, brushSize);

        ToolBar toolBar = new ToolBar(
                drawTogg,
                raysTogg,
                eraserTogg,//Eventually have many options in a toggleGroup
                clearButt,
                new Separator(Orientation.VERTICAL),
                brushSize,
                new Separator(Orientation.VERTICAL),
                colorPicker1,
                colorPickerBackground
        );

        VBox layout = new VBox(10);
        layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 20px;");
        layout.getChildren().setAll(
                toolBar,
                canvas,
                reporter
        );
        layout.setPrefWidth(600);

        stage.setScene(
                new Scene(layout)
        );

        stage.show();
    }

    private Label createMonitoredLabel(final Label reporter) {
        final Label monitored = new Label();

        monitored.setPrefSize(560, 400);
        monitored.setStyle("-fx-background-color: lightpink;");

        monitored.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String msg =
                        "(x: "       + event.getX()      + ", y: "       + event.getY()       + ") -- " +
                                "(sceneX: "  + event.getSceneX() + ", sceneY: "  + event.getSceneY()  + ") -- " +
                                "(screenX: " + event.getScreenX()+ ", screenY: " + event.getScreenY() + ")";

                reporter.setText(msg);
            }
        });

        monitored.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                reporter.setText(OUTSIDE_TEXT);
            }
        });

        monitored.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Circle circle = new Circle(event.getX(), event.getX(), 5, Color.BLACK);
            }
        });

        return monitored;
    }

    private Canvas createCanvas(ColorPicker colorPicker1, ColorPicker colorPickerBackground, Button clear, ToggleButton draw, ToggleButton rays, ToggleButton eraser, TextField brushSize) {
        final Canvas canvas = new Canvas(560, 400);
        double size = 5;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(colorPickerBackground.getValue());
        gc.fillRect(0, 0, 560, 400);
        gc.setFill(colorPicker1.getValue());
        gc.setLineWidth(size);

        clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                gc.setFill(colorPickerBackground.getValue());
                gc.fillRect(0, 0, 560, 400);
            }
        });

        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (rays.isSelected() == true) {
                    iXCoor = event.getX();
                    iYCoor = event.getY();
                } else {
                    gc.beginPath();
                    gc.moveTo(event.getX(), event.getY());
                }
            }
        });

        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (rays.isSelected() == false) {
                    if (eraser.isSelected() == true) {
                        gc.setFill(colorPickerBackground.getValue());
                        gc.fillOval(event.getX() - (size / 2), event.getY() - (size / 2), size, size);
                    }
                    else {
                        gc.setFill(colorPicker1.getValue());
                        gc.fillOval(event.getX() - (size / 2), event.getY() - (size / 2), size, size);
                    }
                }
            }
        });

        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (rays.isSelected() == true) {
                    gc.setStroke(colorPicker1.getValue());
                    gc.setLineWidth(1);
                    gc.strokeLine(iXCoor, iYCoor, event.getX(), event.getY());
                    gc.setLineWidth(size);
                } else if (eraser.isSelected() == true) {
                    gc.setStroke(colorPickerBackground.getValue());
                    gc.lineTo(event.getX(), event.getY());
                    gc.stroke();
                }
                else if (draw.isSelected() == true) {
                    gc.setStroke(colorPicker1.getValue());
                    gc.lineTo(event.getX(), event.getY());
                    gc.stroke();
                }
            }
        });

        /*monitored.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
                reporter.setText(OUTSIDE_TEXT);
            }
        });

        monitored.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Circle circle = new Circle(event.getX(), event.getX(), 5, Color.BLACK);
            }
        });*/

        return canvas;
    }
}