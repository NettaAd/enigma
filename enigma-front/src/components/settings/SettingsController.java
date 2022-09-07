package components.settings;

import common.Constants;
import components.editSettingsContainer.EditSettingsPaneController;
import components.main.MainController;
import components.showMachinePane.showMachinePaneController;
import components.showSettingsContainer.ShowSettingsPaneController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;

public class SettingsController {

    @FXML private  AnchorPane showMachinePane;

    @FXML private HBox editPane;

    @FXML private VBox settingsCon;
    @FXML showMachinePaneController showMachinePaneController;
    @FXML private AnchorPane showSettingsPane;
    @FXML private ShowSettingsPaneController showSettingsPaneController;
    @FXML private AnchorPane editSettingsPane;
    @FXML private EditSettingsPaneController editSettingsPaneController;
    private SimpleBooleanProperty isMachineSet;
    private  MainController mainController;


    @FXML
    private void initialize() {
        editSettingsPaneController.setParent(this);
        showSettingsPaneController.setParent(this);
        showMachinePaneController.setParent(this);
        editPane.minWidthProperty().bind(settingsCon.widthProperty());
//        editSettingsPane.setScaleY().bind(settingsCon.widthProperty());
//        System.out.println(this.isMachineSetProperty().get());
    }
    public SimpleBooleanProperty isMachineSetProperty() {

        return isMachineSet;
    }
    public SettingsController() {

        this.isMachineSet= new SimpleBooleanProperty(false);
    }
    public void updateSettings(){

        showSettingsPaneController.updatePane();
        showMachinePaneController.updatePane();
        editSettingsPaneController.updatePane();
    }
    public void clearAll(){

        editSettingsPaneController.ClearAll();
        showMachinePaneController.ClearAll();
    }
    public void setParent(MainController mainController) {

        this.mainController = mainController;
        editSettingsPaneController.setMainController( this.mainController);
        showSettingsPaneController.setMainController(this.mainController);
        showMachinePaneController.setMainController(this.mainController);
    }
    public MainController getMainController() {

        return mainController;
    }


}
