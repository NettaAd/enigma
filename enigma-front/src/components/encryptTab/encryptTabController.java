package components.encryptTab;

import backend.BackEndMain;
import backend.SavedEncode;
import common_models.activePlug;
import common_models.activeRotor;
import components.main.MainController;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class encryptTabController {

    @FXML private Pane configPane;

    @FXML private AnchorPane mainCon;

    @FXML private HBox inputWindow;

    @FXML private FlowPane keyBoardPane;

    @FXML private FlowPane keyBoardPaneShow;

    @FXML private  TextField EncryptInputTextField;
    @FXML private TextField EncryptOutputTextField;

    @FXML private Button encryptBtn;

    @FXML private Button toggleEncryptTypeBtn;
    @FXML private  Label errorHelper;

    @FXML private FlowPane plugPane;

    @FXML private VBox historyPane;

    @FXML private Button resetBtn;

    private SimpleStringProperty encryptOutPut;

    private SimpleStringProperty encryptInPut;

    private SimpleBooleanProperty isStream;
    private MainController mainController;

//    private ArrayList<activeRotor> rotors;

    private ObservableList<String> rotorsPosList= FXCollections.observableArrayList();;

    public encryptTabController() {
        encryptOutPut= new SimpleStringProperty("");
        encryptInPut =new SimpleStringProperty("");
        isStream=new SimpleBooleanProperty(false);
    }
    @FXML private void initialize (){
        EncryptOutputTextField.textProperty().bindBidirectional(encryptOutPut);
        EncryptInputTextField.textProperty().bindBidirectional(encryptInPut);
        toggleEncryptTypeBtn.textProperty().bind(Bindings.when(isStream).then("stream mode").otherwise("encrypt mode"));


    }

    public void setParent(MainController mainController) {
        this.mainController=mainController;
        encryptBtn.disableProperty().bind(mainController.getIsSettings().not());
        BackEndMain backend = mainController.getBackEnd();

        resetBtn.setOnMouseClicked(event -> {
            System.out.println("check");
            mainController.resetMachine();
        });

        encryptBtn.setOnMouseClicked(e->{

            if(!isStream.get()){
                String toDecode=EncryptInputTextField.getText();
                toDecode = toDecode.toUpperCase();
                try {
                    mainController.getBackEnd().testAgent(toDecode);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                try {
//                start = System.nanoTime();
                    String res = backend.DecodeString(toDecode,true);
//                end = System.nanoTime();
                    encryptOutPut.set(res);
                    mainController.getActiveRotorsList().setAll(backend.getActiveRotorsBeter());
                    encryptInPut.set("");
                }catch (Exception err){
                    System.out.println("ERROR"+err);
                }
            }else{
                System.out.println("streaming");
                backend.addMsgCount(EncryptInputTextField.getText(),encryptOutPut.get());
                mainController.getActiveRotorsList().setAll(backend.getActiveRotorsBeter());

            }


        });
        encryptInPut.addListener( (observable, oldValue, newValue)  ->{
            if(newValue.length()<oldValue.length() ){
//                encryptOutPut.setValue("");
//                observable.removeListener((InvalidationListener) this);
                Platform.runLater(() -> {
                    EncryptInputTextField.setText("");
                });


                return;
            }
            String toDecode = newValue;

            System.out.println("event "+newValue+" "+oldValue);
            toDecode = toDecode.toUpperCase();
            if(!backend.getAbc().checkInAbc(toDecode).isEmpty()){
                errorHelper.setText("bad letters->"+backend.getAbc().checkInAbc(toDecode));
                return;
            }else{
                errorHelper.setText("");
            }

            if(isStream.get()) {

                toDecode = toDecode.substring(toDecode.length() - 1);
                try {
//                start = System.nanoTime();
                    String res = backend.DecodeString(toDecode,false);
//                end = System.nanoTime();
                    encryptOutPut.setValue(encryptOutPut.get()+res);
                    String[] lights = backend.getAbc().getAbc().split("");
                    keyBoardPane.getChildren().clear();
                    for (int i = 0; i < lights.length; i++) {
                        StackPane plugHole = new StackPane();
                        Text plugText = new Text();
                        plugText.setFont(new Font(20));
                        plugText.setBoundsType(TextBoundsType.VISUAL);
                        plugText.setText(lights[i]);
                        Circle plugCon = new Circle(50.0f,20.0f,15.0f);
//                        System.out.println("res "+res+" "+lights[i]);
                        if(lights[i].toUpperCase().equals(res)){
                            plugCon.setFill(Color.YELLOW);
                        }else{
                            plugCon.setFill(Color.GRAY);
                        }


                        plugHole.getChildren().addAll(plugCon,plugText);
                        keyBoardPane.getChildren().add(plugHole);
                    }
                    mainController.getActiveRotorsList().setAll(backend.getActiveRotorsBeter());

                } catch (Exception err) {
                    System.out.println("ERROR" + err);
                }
            }

        });

        toggleEncryptTypeBtn.setOnMouseClicked(e->{
            isStream.set(!isStream.get());
            encryptInPut.set("");
            encryptOutPut.set("");
        });

    }

    public void updateSettings() {

        BackEndMain backend = mainController.getBackEnd();

//        keyboard
//        keyBoardPane
        keyBoardPane.setVgap(5.0);
        keyBoardPane.setHgap(5.0);
        keyBoardPaneShow.setVgap(5.0);
        keyBoardPaneShow.setHgap(5.0);
        keyBoardPaneShow.getChildren().clear();
        String[] letters = backend.getAbc().getAbc().split("");
        for (int i = 0; i < letters.length; i++) {
            StackPane plugHole = new StackPane();
            Text plugText = new Text();
            plugText.setFont(new Font(20));
            plugText.setBoundsType(TextBoundsType.VISUAL);
            plugText.setText(letters[i]);
            Circle plugCon = new Circle(50.0f,20.0f,15.0f);
            plugCon.setFill(Color.GRAY);
            plugHole.getChildren().addAll(plugCon,plugText);
            keyBoardPaneShow.getChildren().add(plugHole);
//            keyBoardPane.getChildren().add(plugHole);
        }





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
        plugPane.getChildren().setAll(ObsPluglist);



        ArrayList<activeRotor> rotors =  backend.getActiveRotorsBeter();
        HBox con = new HBox();
        con.getStyleClass().add("rotorPosCon");
        configPane.setCenterShape(true);
        mainController.getActiveRotorsList().stream().forEach(r->{
            VBox rotorBox = new VBox();
            rotorBox.getStyleClass().add("rotorPosBox");
            Label rotorPos = new Label();
            rotorPos.getStyleClass().add("rotorPosText");
            rotorBox.getChildren().add(rotorPos);
//            rotorsPosList.add(r.getPosition());
            rotorPos.setText(r.getPosition());
            con.getChildren().add(rotorBox);
        });

//        for (activeRotor r:rotors ) {
//            VBox rotorBox = new VBox();
//            rotorBox.getStyleClass().add("rotorPosBox");
//            Label rotorPos = new Label();
//            rotorPos.getStyleClass().add("rotorPosText");
//            rotorBox.getChildren().add(rotorPos);
//            rotorsPosList.add(r.getPosition());
//            rotorPos.setText(r.getPosition());
//            con.getChildren().add(rotorBox);
//        }
        configPane.getChildren().add(con);
        historyPane.getChildren().clear();
        ArrayList<SavedEncode> messages = backend.getMessages();

        for (String set : backend.getMachineSettings()) {


            long end=0;
            long start=0;
            System.out.println();
VBox settingsBox = new VBox();
Label settingsTitle = new Label();
settingsTitle.setText("The machine settings are: " + set);
            settingsBox.getChildren().add(settingsTitle);
            for (SavedEncode s : messages) {
                Label msgEncrypt = new Label();

                if (set.equals(s.getMachineSettings())) {


                    long time = end - start;
                    String in = s.getInString();
                    String out = s.getOutString();
                    msgEncrypt.setText("#. <" + in + "> --> <" + out + "> (" + time + " nano-seconds)");
                    settingsBox.getChildren().add(msgEncrypt);
                }
            }
            historyPane.getChildren().add(settingsBox);
        }



    }
}
