package components.main;

import backend.BackEndMain;
import common.Constants;
import common_models.activePlug;
import common_models.activeRotor;
import components.editSettingsContainer.EditSettingsPaneController;
import components.encryptTab.encryptTabController;
import components.settings.SettingsController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;


public class MainController {

    private BackEndMain backend = new BackEndMain();

    @FXML private VBox mainCon;

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

    private Color[] plugColors;


    private ObservableList<activePlug> activePlugsList = FXCollections.observableArrayList();

    private ObservableList<activeRotor> activeRotorsList = FXCollections.observableArrayList();





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

    public ObservableList<activePlug> getActivePlugsList(){
        return activePlugsList;
    }

    public ObservableList<activeRotor> getActiveRotorsList(){
        return activeRotorsList;
    }



    @FXML
    private void initialize() {
        selectedFileName.textProperty().bind(selectedFileProperty);
        settings.minWidthProperty().bind(mainCon.widthProperty());

        downPane.disableProperty().bind(isFileSelected.not());
        activeRotorsList.addListener((ListChangeListener<? super activeRotor>) e->{
            System.out.println("sync");
            Sync();
        });
        isSettings.bind(Bindings.createBooleanBinding(()->activeRotorsList.size()>1).not());
    }

    public SimpleBooleanProperty getIsFileSelected(){
        return isFileSelected;
    }

    public SimpleStringProperty getSelectedFileProperty(){
        return selectedFileProperty;
    }



    public void Sync(){
        if(backend.getAmountOfActiveRotors()>2){
        }

//        ArrayList<activePlug> plugs = mainController.getBackEnd().getActivePlugsBetter();
//        ArrayList<activeRotor> rotors = mainController.getBackEnd().getActiveRotorsBeter();
        System.out.println(activePlugsList);
        System.out.println(activeRotorsList);
        settingsController.updateSettings();
        encryptTabController.updateSettings();

    }







    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Color[] getPlugsColor(){
        return plugColors;
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
            activeRotorsList.clear();
            activePlugsList.clear();
            Random rand = new Random(System.currentTimeMillis());
            Color[] newPlugsColor = new Color[backend.getUnActivePlugs().size()];
            for (int i = 0; i < backend.getUnActivePlugs().size(); i++) {

                int red = rand.nextInt(255);
                int green = rand.nextInt(255);
                int blue = rand.nextInt(255);
                newPlugsColor[i]=Color.rgb(red, green, blue, .99);


            }
            plugColors = newPlugsColor;
            settingsController.clearAll();
            settingsController.setParent(this);
            encryptTabController.setParent(this);
            selectedFileProperty.set(absolutePath);
            isFileSelected.set(true);
            settingsController.isMachineSetProperty().set(false);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void resetMachine() {
        backend.resetMachine();
        activePlugsList.setAll(backend.getActivePlugsBetter());
        activeRotorsList.setAll(backend.getActiveRotorsBeter());
        Sync();
    }
}
