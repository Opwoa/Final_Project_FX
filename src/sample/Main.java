//http://docs.oracle.com/javase/8/javafx/user-interface-tutorial/ui_controls.htm#JFXUI336
//http://docs.oracle.com/javase/8/javafx/api/
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.*;

public class Main extends Application {
    private static final String OUTSIDE_TEXT = "Outside Label";

    public static void main(String[] args) { launch(args); }

    @Override public void start(final Stage stage) {
        final Label reporter = new Label(OUTSIDE_TEXT);
        Label monitored = createMonitoredLabel(reporter);
        ToolBar toolBar = new ToolBar(
                new ToggleButton("New"),
                new Button("Open"),
                new Button("Save"),
                //new Separator(true),
                new Button("Clean"),
                new Button("Compile"),
                new Button("Run"),
                //new Separator(true),
                new Button("Debug"),
                new Button("Profile")
        );

        VBox layout = new VBox(10);
        layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 20px;");
        layout.getChildren().setAll(
                toolBar,
                monitored,
                reporter
        );
        layout.setPrefWidth(800);

        stage.setScene(
                new Scene(layout)
        );

        stage.show();
    }

    private Label createMonitoredLabel(final Label reporter) {
        final Label monitored = new Label("     ");

        monitored.setStyle("-fx-background-color: lightpink; -fx-text-fill: white; -fx-font-size: 400px;");

        monitored.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent event) {
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

        return monitored;
    }
}