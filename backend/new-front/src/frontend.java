import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class frontend extends Application {
    private Parent rootNode;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void init() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main.fxml"));
        rootNode = fxmlLoader.load();
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
//        Group root = new Group();
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/main.fxml"));
//        URL url = getClass().getResource("/views/main.fxml");
//        fxmlLoader.setLocation(url);

//        Parent root =fxmlLoader.load();


        primaryStage.setScene(new Scene(rootNode,1017,606));
//        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
