import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class frontend extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();

        Scene scene = new Scene(root,420,420 ,Color.LIGHTBLUE);
        primaryStage.setTitle("test this 47");
        Text text = new Text();
        text.setText("okkkkkkkkkkkkk");
        text.setX(50);
        text.setY(50);
        text.setFill(Color.RED);
        root.getChildren().add(text);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
