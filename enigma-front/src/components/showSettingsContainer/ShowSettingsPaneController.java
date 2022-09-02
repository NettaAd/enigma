package components.showSettingsContainer;

import common_models.activePlug;
import common_models.activeRotor;
import components.main.MainController;
import components.settings.SettingsController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class ShowSettingsPaneController {
    private  SettingsController settingsController;
    private  MainController mainController;

    @FXML private TableColumn<activePlug,String> activePlugFromCol;
    @FXML private TableColumn<activePlug,String> activePlugToCol;
    @FXML private TableView activePlugTable;

    @FXML private HBox activeRotorsCon;

    @FXML private  Label activeRef;
    private ObservableList<activePlug> activePlugsList = FXCollections.observableArrayList();


    @FXML
    private void initialize() {



    }
    public void setParent(SettingsController settingsController) {
        this.settingsController=settingsController;
        System.out.println(settingsController);
    }

    public void setMainController(MainController mainController) {
        this.mainController=mainController;
    }

    public void updatePane() {
   ArrayList<activePlug> plugs = mainController.getBackEnd().getActivePlugsBetter();
   ArrayList<activeRotor> rotors = mainController.getBackEnd().getActiveRotorsBeter();
   String refId = mainController.getBackEnd().getReflectorId();
        activeRef.setText(refId);
        activeRotorsCon.getChildren().clear();
        activePlugsList.setAll(plugs);
   activePlugTable.setItems(activePlugsList);
        activePlugFromCol.setCellValueFactory(new PropertyValueFactory<activePlug, String>("from"));
        activePlugToCol.setCellValueFactory(new PropertyValueFactory<activePlug, String>("to"));
        for (activeRotor rotor:rotors  ) {
            ListView<String> posList = new ListView<>();
            posList.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    System.out.println(">> Mouse Clicked");
                    event.consume();
                }
            });
            VBox rotorEditCon = new VBox();
            Label rotorIdLabel = new Label();
            Label rotorPosLabel = new Label();
            rotorPosLabel.setTextFill(Color.BLACK);
            rotorPosLabel.setText(rotor.getPosition());
            rotorIdLabel.setLabelFor(posList);
            rotorIdLabel.setText(rotor.getId());
            rotorEditCon.getChildren().add(rotorIdLabel);
//            rotorEditCon.getChildren().add(posList);
            rotorEditCon.getChildren().add(rotorPosLabel);
            ObservableList<String> RotorPositions = FXCollections.observableArrayList();
            posList.setItems(RotorPositions);
            ArrayList<String> currRotor = mainController.getBackEnd().getRotorPositions(Integer.valueOf(rotor.getId()));
            System.out.println(currRotor);
            currRotor.stream().forEach(r -> RotorPositions.add(r));
            activeRotorsCon.getChildren().add(rotorEditCon);
            posList.getSelectionModel().select(rotor.getPosition());
            posList.scrollTo(rotor.getPosition());

        }

    }
}
