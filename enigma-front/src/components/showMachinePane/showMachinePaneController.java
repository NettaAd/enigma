package components.showMachinePane;

import Machine.SpinningRotor;
import backend.BackEndMain;
import components.main.MainController;
import components.settings.SettingsController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class showMachinePaneController {

    private SettingsController settingsController;

    private MainController mainController;

    @FXML
    Label activeRotorsLabel;

    @FXML Label machineSettingsLaebl;

    @FXML private  VBox notchRotorsList;

    @FXML private  Label numberOfCodesEncodedLabel;
    public void setParent(SettingsController settingsController) {
        this.settingsController=settingsController;

    }

    public void updatePane() {
        BackEndMain backend = mainController.getBackEnd();
        int activeRotorsNum = backend.getAmountOfActiveRotors();
        int rotorsNum = backend.getAmountOfRotors();
        activeRotorsLabel.setText(activeRotorsNum+"/"+rotorsNum);
//        VBox rotorNotchList = new VBox();
        notchRotorsList.getChildren().clear();
        for(SpinningRotor r: backend.getRotorsArr()){
            Label rotorNotch = new Label();
            rotorNotch.getStyleClass().add("notchRotorInfo");
            rotorNotch.setText("The notch of Rotor " + r.getId() + " is: "+ (r.getNotch() + 1));
            notchRotorsList.getChildren().add(rotorNotch);

        }
        machineSettingsLaebl.setText(backend.getFormatStats());
        numberOfCodesEncodedLabel.setText(""+backend.getMessagesCount());


    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
