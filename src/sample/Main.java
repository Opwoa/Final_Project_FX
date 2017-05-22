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

    public static void main(String[] args) { launch(args); }

    @Override public void start(final Stage stage) {
        final Label reporter = new Label(OUTSIDE_TEXT);
        Label monitored = createMonitoredLabel(reporter);
        Canvas canvas = createCanvas();
        ColorPicker colorPicker1 = new ColorPicker();
        ToolBar toolBar = new ToolBar(
                new ToggleButton("New"),
                new Button("Open"),
                new Button("Save"),
                new Separator(Orientation.VERTICAL),
                new Button("Clean"),
                new Button("Compile"),
                new Button("Run"),
                new Separator(Orientation.VERTICAL),
                new Button("Debug"),
                new Button("Profile"),
                colorPicker1
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

    private Canvas createCanvas() {
        final Canvas canvas = new Canvas(560, 400);
        canvas.setStyle("-fx-background-color: lightpink;");

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLUE);
        gc.setStroke(Color.ORANGE);
        gc.strokeRect(75,75,100,100);

        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                gc.fillRect(event.getX(), event.getY(), 10, 10);
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