package components.settings;

import common.Constants;
import components.editSettingsContainer.EditSettingsPaneController;
import components.main.MainController;
import components.showMachinePane.showMachinePaneController;
import components.showSettingsContainer.ShowSettingsPaneController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;

public class SettingsController {

    @FXML private  AnchorPane showMachinePane;

    @FXML
    showMachinePaneController showMachinePaneController;
    @FXML
    private AnchorPane showSettingsPane;

    @FXML
    private ShowSettingsPaneController showSettingsPaneController;

    @FXML
    private AnchorPane editSettingsPane;

    @FXML
    private EditSettingsPaneController editSettingsPaneController;

    private SimpleBooleanProperty isMachineSet;

    private  MainController mainController;



    public SimpleBooleanProperty isMachineSetProperty() {
        return isMachineSet;
    }

    public SettingsController() {
        this.isMachineSet= new SimpleBooleanProperty(false);
    }

    @FXML
    private void initialize() {
        editSettingsPaneController.setParent(this);
        showSettingsPaneController.setParent(this);
        showMachinePaneController.setParent(this);
        System.out.println(this.isMachineSetProperty().get());
//
//        if(this.isMachineSetProperty().get()){
//            showSettingsPaneController.updatePane();
//        }
//
//       this.isMachineSet.addListener((observable, oldValue, newValue)->{
//
//           System.out.println("e->"+newValue+" "+oldValue);
//           if(newValue || oldValue){
//               showSettingsPaneController.updatePane();
//           }
//       });

    }

    public void updateSettings(){
        showSettingsPaneController.updatePane();
        showMachinePaneController.updatePane();

    }

    public void clearAll(){
        editSettingsPaneController.ClearAll();

    }


    public void setParent(MainController mainController) {
        this.mainController=mainController;
//        this.mainController.getFirstRotor().addListener((observable, oldValue, newValue)->{
//            showSettingsPaneController.updatePane();
//        } );
        editSettingsPaneController.setMainController( this.mainController);
        showSettingsPaneController.setMainController(this.mainController);
        showMachinePaneController.setMainController(this.mainController);
    }


    public MainController getMainController() {
        System.out.println(mainController);
        return mainController;
    }
}
