package components.showSettingsContainer;

import common_models.activePlug;
import common_models.activeRotor;
import components.main.MainController;
import components.settings.SettingsController;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class ShowSettingsPaneController {
    private  SettingsController settingsController;
    private  MainController mainController;

    @FXML private TableColumn<activePlug,String> activePlugFromCol;
    @FXML private TableColumn<activePlug,String> activePlugToCol;
    @FXML private TableView activePlugTable;

    @FXML private HBox activeRotorsCon;

    @FXML private  Label activeRef;
    private ObservableList<activePlug> activePlugsList = FXCollections.observableArrayList();

    @FXML private FlowPane plugPane;


    @FXML
    private void initialize() {



    }
    public void setParent(SettingsController settingsController) {
        this.settingsController=settingsController;


    }

    public void display(){
        if(this.mainController!=null){
            ObservableList<activePlug> activePlugsList = mainController.getActivePlugsList();

        }
    }

    public void setMainController(MainController mainController) {
        this.mainController=mainController;
        System.out.println(settingsController);
        updatePane();

    }

    public void updatePane() {
//   ArrayList<activePlug> plugs = mainController.getBackEnd().getActivePlugsBetter();
//   ArrayList<activeRotor> rotors = mainController.getBackEnd().getActiveRotorsBeter();

   String refId = mainController.getBackEnd().getReflectorId();
        activeRef.setText(refId);
        activeRotorsCon.getChildren().clear();
//        activePlugsList.setAll(plugs);
//   activePlugTable.setItems(mainController.getActivePlugsList());
//        activePlugFromCol.setCellValueFactory(new PropertyValueFactory<activePlug, String>("from"));
//        activePlugToCol.setCellValueFactory(new PropertyValueFactory<activePlug, String>("to"));

        plugPane.setHgap(25);
        ObservableList list = plugPane.getChildren();
        list.clear();
        Random rand = new Random(System.currentTimeMillis());

        for (activePlug p:mainController.getActivePlugsList()) {
            StackPane plugHole = new StackPane();
            Text fromLabel = new Text();
            fromLabel.setBoundsType(TextBoundsType.VISUAL);
            fromLabel.setText(p.getFrom());
            Circle from = new Circle(30.0f,13.0f,10.0f);
            Circle to = new Circle(30.0f,13.0f,10.0f);

            plugHole.getChildren().addAll(from,fromLabel);
            int red = rand.nextInt(255);
            int green = rand.nextInt(255);
            int blue = rand.nextInt(255);
            from.setFill(Color.rgb(red, green, blue, .99));

            to.setFill(Color.rgb(red, green, blue, .99));

            list.add(from);
            list.add(to);
        }


        for (activeRotor rotor: mainController.getActiveRotorsList()  ) {
            ListView<String> posList = new ListView<>();
            posList.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    System.out.println(">> Mouse Clicked");
                    event.consume();
                }
            });

//            VBox rotorEditCon = new VBox();
//            Label rotorIdLabel = new Label();
//            Label rotorPosLabel = new Label();
//            rotorPosLabel.setTextFill(Color.BLACK);
//            rotorPosLabel.setText(rotor.getPosition());
//            rotorIdLabel.setLabelFor(posList);
//            rotorIdLabel.setText(rotor.getId());
//            rotorEditCon.getChildren().add(rotorIdLabel);
//            rotorEditCon.getChildren().add(rotorPosLabel);
//            ObservableList<String> RotorPositions = FXCollections.observableArrayList();
//            posList.setItems(RotorPositions);
//            ArrayList<String> currRotor = mainController.getBackEnd().getRotorPositions(Integer.valueOf(rotor.getId()));
//            System.out.println(currRotor);
//            currRotor.stream().forEach(r -> RotorPositions.add(r));
//            activeRotorsCon.getChildren().add(rotorEditCon);
//            posList.getSelectionModel().select(rotor.getPosition());
//            posList.scrollTo(rotor.getPosition());

        }

    }
}
