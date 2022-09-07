package components.showSettingsContainer;

import common_models.activePlug;
import common_models.activeRotor;
import components.main.MainController;
import components.settings.SettingsController;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
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
        plugPane.setVgap(10);

        ObservableList<StackPane> ObsPluglist = FXCollections.observableArrayList();
        ObsPluglist.clear();
        Random rand = new Random(System.currentTimeMillis());
        String[] nonActivePlugs = mainController.getBackEnd().getAbc().getAbc().split("");
        Color[] plugsColor = mainController.getPlugsColor();

//        for (int i = 0; i < mainController.getActivePlugsList().size(); i++) {
//
//            int red = rand.nextInt(255);
//            int green = rand.nextInt(255);
//            int blue = rand.nextInt(255);
//            plugsColor[i]=Color.rgb(red, green, blue, .99);
//
//
//        }
        int colorCount= 0;
        int pairCount= 0;
        for (int i = 0; i < nonActivePlugs.length; i++) {
            StackPane plugHole = new StackPane();
            Text plugText = new Text();
            plugText.setFont(new Font(20));
            plugText.setBoundsType(TextBoundsType.VISUAL);
            plugText.setText(nonActivePlugs[i]);
            Circle plugCon = new Circle(50.0f,20.0f,15.0f);
            plugCon.setFill(Color.GRAY);
            int index =0;
                for (activePlug p : mainController.getActivePlugsList()) {
                    String to = p.getTo();
                    String from = p.getFrom();
                    if(nonActivePlugs[i].equals(to)|| nonActivePlugs[i].equals(from)){
                        plugCon.setFill(plugsColor[index]);
                    }

                    index++;
                }

            plugHole.getChildren().addAll(plugCon,plugText);
            ObsPluglist.add(plugHole);
        }

//        mainController.getBackEnd().getUnActivePlugs().forEach(p->{
//            StackPane plugHole = new StackPane();
//            Text plugText = new Text();
//            plugText.setFont(new Font(25));
//            plugText.setBoundsType(TextBoundsType.VISUAL);
//            plugText.setText(p);
//            Circle plugCon = new Circle(60.0f,26.0f,20.0f);
//            plugCon.setFill(Color.GRAY);
//            plugHole.getChildren().addAll(plugCon,plugText);
//            ObsPluglist.add(plugHole);
//        });
//        for (activePlug p:mainController.getActivePlugsList()) {
//
//            StackPane plugHoleFrom = new StackPane();
//            StackPane plugHoleTo = new StackPane();
//            Text fromLabel = new Text();
//            fromLabel.setFont(new Font(25));
//            fromLabel.setBoundsType(TextBoundsType.VISUAL);
//            fromLabel.setText(p.getFrom());
//            Text toLabel = new Text();
//            toLabel.setFont(new Font(25));
//            toLabel.setBoundsType(TextBoundsType.VISUAL);
//            toLabel.setText(p.getTo());
//            Circle from = new Circle(60.0f,26.0f,20.0f);
//            Circle to = new Circle(60.0f,26.0f,20.0f);
//
//            int red = rand.nextInt(255);
//            int green = rand.nextInt(255);
//            int blue = rand.nextInt(255);
//            from.setFill(Color.rgb(red, green, blue, .99));
//            to.setFill(Color.rgb(red, green, blue, .99));
//            plugHoleFrom.getChildren().addAll(from,fromLabel);
//            plugHoleTo.getChildren().addAll(to,toLabel);
//            ObsPluglist.add(plugHoleFrom);
//            ObsPluglist.add(plugHoleTo);
//
//        }
        plugPane.getChildren().setAll(ObsPluglist);

        activeRotorsCon.setSpacing(15.0);
        for (activeRotor rotor: mainController.getActiveRotorsList()  ) {
            ListView<String> posList = new ListView<>();
            posList.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    System.out.println(">> Mouse Clicked");
                    event.consume();
                }
            });

            VBox rotorEditCon = new VBox();
            Rectangle rectangle = new Rectangle(10, 10, 20, 100);
            StackPane rotorBox = new StackPane();
            Label rotorId = new Label();
            rotorId.setText(rotor.getPosition());
            rectangle.setFill(Color.GRAY);
            rotorBox.getStyleClass().add("rotorWindow");
            rotorId.setTextFill(Color.WHEAT);
            rotorId.setFont(Font.font(20));
            rotorBox.getChildren().addAll(rectangle,rotorId);

            activeRotorsCon.getChildren().add(rotorBox);
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

//            posList.getSelectionModel().select(rotor.getPosition());
//            posList.scrollTo(rotor.getPosition());

        }

    }
}
