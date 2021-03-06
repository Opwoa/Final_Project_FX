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
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class Main extends Application {
    private static final String OUTSIDE_TEXT = "Outside Label";
    private static double iXCoor, iYCoor;

    public static void main(String[] args) { launch(args); }

    @Override public void start(final Stage stage) {
        final Label reporter = new Label(OUTSIDE_TEXT);
        Label monitored = createMonitoredLabel(reporter);
        //color pickers
        ColorPicker colorPicker1 = new ColorPicker(Color.BLACK);
        ColorPicker colorPickerBackground = new ColorPicker(Color.WHITE);
        //toggle buttons and group
        ToggleButton drawTogg = new ToggleButton("Regular Draw");
        ToggleButton raysTogg = new ToggleButton("Rays");
        ToggleButton eraserTogg = new ToggleButton("Eraser");
        ToggleGroup group = new ToggleGroup();
        drawTogg.setToggleGroup(group);
        raysTogg.setToggleGroup(group);
        eraserTogg.setToggleGroup(group);
        drawTogg.setSelected(true);
        //buttons
        Button clearButt = new Button("Clear");
        //Sliders
        Slider slider = new Slider(0, 25, 5);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setSnapToTicks(true);
        slider.setMajorTickUnit(5f);
        slider.setBlockIncrement(1f);

        //create the canvas using all inputs as parameters
        Canvas background = createBackground(colorPickerBackground);
        Canvas canvas = createCanvas(colorPicker1, clearButt, new Button(), drawTogg, raysTogg, eraserTogg, slider);

        ToolBar toolBar = new ToolBar(
                drawTogg,
                raysTogg,
                eraserTogg,
                clearButt,
                new Separator(Orientation.VERTICAL),
                slider,
                new Separator(Orientation.VERTICAL),
                new Label("Brush Color:"),
                colorPicker1,
                new Label("Background Color:"),
                colorPickerBackground
        );

        StackPane stack = new StackPane();
        stack.getChildren().addAll(background, canvas);

        BorderPane border = new BorderPane();
        border.setRight(addFlowPane());
        border.setCenter(stack);
        border.setTop(toolBar);

        stage.setScene(
                new Scene(border)
        );

        stage.show();
    }

    private FlowPane addFlowPane() {
        FlowPane flow = new FlowPane();
        // flow.setPadding(new Insets(5, 0, 5, 0));
        flow.setVgap(4);
        flow.setHgap(4);
        flow.setPrefWrapLength(170); // preferred width allows for two columns
        flow.setStyle("-fx-background-color: DAE6F3;");

        //ImageView pages[] = new ImageView[8];
        for (int i=0; i<8; i++) {
            //pages[i] = new ImageView(
            //new Image(LayoutSample.class.getResourceAsStream(
            // "graphics/chart_"+(i+1)+".png";)));
            //flow.getChildren().add(pages[i]);
        }

        return flow;
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

    private Canvas createBackground(ColorPicker colorPickerBackground) {
        final Canvas background = new Canvas(568, 400);

        GraphicsContext gc = background.getGraphicsContext2D();
        gc.setFill(colorPickerBackground.getValue());
        gc.fillRect(0, 0, 568, 400);

        colorPickerBackground.setOnHiding(new EventHandler<Event>() {
            @Override public void handle(Event e) {
                gc.setFill(colorPickerBackground.getValue());
                gc.fillRect(0, 0, 568, 400);
            }
        });

        return background;
    }

    private Canvas createCanvas(ColorPicker colorPicker1,
                                Button clear,Button undo, ToggleButton draw, ToggleButton rays, ToggleButton eraser,
                                Slider brushSlider) {
        Canvas canvas = new Canvas(568, 400);
        Canvas backup = new Canvas(568, 400);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, 568, 400);
        gc.setStroke(colorPicker1.getValue());
        gc.setLineWidth(brushSlider.getValue());
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setLineJoin(StrokeLineJoin.ROUND);

        clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                gc.clearRect(0, 0, 568, 400);
            }
        });

        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (rays.isSelected() == true) {
                    iXCoor = event.getX();
                    iYCoor = event.getY();
                } else if (eraser.isSelected() == true) {
                    gc.beginPath();
                    gc.moveTo(event.getX(), event.getY());
                    gc.clearRect(event.getX() - (brushSlider.getValue() / 2), event.getY() - (brushSlider.getValue() / 2), brushSlider.getValue(), brushSlider.getValue());
                }
                else {
                    gc.beginPath();
                    gc.moveTo(event.getX(), event.getY());
                    gc.setFill(colorPicker1.getValue());
                    gc.fillOval(event.getX() - (brushSlider.getValue() / 2), event.getY() - (brushSlider.getValue() / 2), brushSlider.getValue(), brushSlider.getValue());
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
                    gc.setLineWidth(brushSlider.getValue());
                } else if (eraser.isSelected() == true) {
                    gc.clearRect(event.getX() - (brushSlider.getValue() / 2), event.getY() - (brushSlider.getValue() / 2), brushSlider.getValue(), brushSlider.getValue());
                }
                else if (draw.isSelected() == true) {
                    gc.setStroke(colorPicker1.getValue());
                    gc.lineTo(event.getX(), event.getY());
                    gc.setLineWidth(brushSlider.getValue());
                    gc.stroke();
                }
            }
        });

        canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                gc.closePath();
            }
        });

        return canvas;
    }
}