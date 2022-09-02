import common.Constants;
import components.main.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class fe extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();

        // load main fxml
        URL mainFXML = getClass().getResource(Constants.MAIN_FXML_RESOURCE_IDENTIFIER);
        loader.setLocation(mainFXML);
        SplitPane root = loader.load();

        // wire up controller
        MainController mainController = loader.getController();
        mainController.setPrimaryStage(primaryStage);

        // set stage
        primaryStage.setTitle("Enigma");
        Scene scene = new Scene(root, 1050, 1000);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
