package components.encryptTab;

import backend.BackEndMain;
import backend.SavedEncode;
import common_models.activeRotor;
import components.main.MainController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.util.ArrayList;

public class encryptTabController {

    @FXML private Pane configPane;

    @FXML private HBox inputWindow;

    @FXML private  TextField EncryptInputTextField;
    @FXML private TextField EncryptOutputTextField;

    @FXML private Button encryptBtn;

    @FXML private Button toggleEncryptTypeBtn;
    @FXML private  Label errorHelper;

    @FXML private VBox historyPane;

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



        encryptBtn.setOnMouseClicked(e->{
            if(!isStream.get()){
                String toDecode=EncryptInputTextField.getText();
                toDecode = toDecode.toUpperCase();

                try {
                    System.out.println("org "+toDecode);
//                start = System.nanoTime();
                    String res = backend.DecodeString(toDecode,true);
                    System.out.println(res);
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

        encryptInPut.addListener(e->{
            String toDecode = encryptInPut.get();

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
                    System.out.println(res);
//                end = System.nanoTime();
                    encryptOutPut.setValue(encryptOutPut.get()+res);
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
        ArrayList<activeRotor> rotors =  backend.getActiveRotorsBeter();
        System.out.println(rotors);
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
