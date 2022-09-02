package components.main;

import backend.BackEndMain;
import common.Constants;
import components.editSettingsContainer.EditSettingsPaneController;
import components.encryptTab.encryptTabController;
import components.settings.SettingsController;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URL;


public class MainController {

    private BackEndMain backend = new BackEndMain();

    @FXML private  AnchorPane encryptTab;

    @FXML
    encryptTabController encryptTabController;

    @FXML Tab settingsTab;
    @FXML
    private Label selectedFileName;
    @FXML
    private Button openFileButton;

    @FXML private  AnchorPane downPane;

    @FXML
    private VBox settings;

    @FXML
    private  SettingsController settingsController;




    private SimpleBooleanProperty isFileSelected;

    private SimpleBooleanProperty triggerUpdate;

    private SimpleBooleanProperty isSettings;


    private SimpleStringProperty selectedFileProperty;

    private Stage primaryStage;

    public MainController() {

        selectedFileProperty = new SimpleStringProperty("");
        isSettings=new SimpleBooleanProperty(false);
        isFileSelected = new SimpleBooleanProperty(false);
        triggerUpdate= new SimpleBooleanProperty(false);
    }


    public BackEndMain getBackEnd() {
        return this.backend;
    }

    public SimpleBooleanProperty getIsSettings(){
        return isSettings;
    }



    @FXML
    private void initialize() {
        selectedFileName.textProperty().bind(selectedFileProperty);
        settingsController.setParent(this);
        encryptTabController.setParent(this);
        downPane.disableProperty().bind(isFileSelected.not());

    }

    public SimpleBooleanProperty getIsFileSelected(){
        return isFileSelected;
    }

    public SimpleStringProperty getSelectedFileProperty(){
        return selectedFileProperty;
    }



    public void Sync(){
        if(backend.getAmountOfActiveRotors()>2){
            isSettings.set(true);
        }
        settingsController.updateSettings();
        encryptTabController.updateSettings();
    }







    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    @FXML
    public void openFileButtonAction()  {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select words file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return;
        }

        String absolutePath = selectedFile.getAbsolutePath();

        try {
            backend.setXmlData(absolutePath);

            settingsController.clearAll();
            selectedFileProperty.set(absolutePath);
            isFileSelected.set(true);

            settingsController.isMachineSetProperty().set(false);
        }catch ( JAXBException e){
            e.printStackTrace();
        }

    }


}
