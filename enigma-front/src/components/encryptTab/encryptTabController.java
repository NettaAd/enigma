package components.encryptTab;

import backend.BackEndMain;
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

    private SimpleStringProperty encryptOutPut;

    private SimpleStringProperty encryptInPut;

    private SimpleBooleanProperty isStream;
    private MainController mainController;

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
                System.out.println("gogogo");
                String toDecode=EncryptInputTextField.getText();
                toDecode = toDecode.toUpperCase();
                try {
                    System.out.println("org "+toDecode);
//                start = System.nanoTime();
                    String res = backend.DecodeString(toDecode);
                    System.out.println(res);
//                end = System.nanoTime();
                    encryptOutPut.set(res);
                    mainController.Sync();
                }catch (Exception err){
                    System.out.println("ERROR"+err);
                }
            }else{
                System.out.println("streaming");
            }


        });

        encryptInPut.addListener(e->{
            if(isStream.get()) {
                System.out.println(e);
                String toDecode = encryptInPut.get();
                toDecode = toDecode.substring(toDecode.length() - 1);
                toDecode = toDecode.toUpperCase();
                try {
                    System.out.println("org " + toDecode);
//                start = System.nanoTime();
                    String res = backend.DecodeString(toDecode);
                    System.out.println(res);
//                end = System.nanoTime();
                    encryptOutPut.setValue(encryptOutPut.get()+res);
                    mainController.Sync();

                } catch (Exception err) {
                    System.out.println("ERROR" + err);
                }
            }

        });

        toggleEncryptTypeBtn.setOnMouseClicked(e->{
            isStream.set(!isStream.get());
            System.out.println("ysys "+e);
        });

    }

    public void updateSettings() {
        System.out.println("collll");

        BackEndMain backend = mainController.getBackEnd();
        ArrayList<activeRotor> rotors =  backend.getActiveRotorsBeter();
        System.out.println(rotors);


        HBox con = new HBox();
        con.getStyleClass().add("rotorPosCon");
        configPane.setCenterShape(true);
        for (activeRotor r:rotors ) {
            VBox rotorBox = new VBox();
            rotorBox.getStyleClass().add("rotorPosBox");
            Label rotorPos = new Label();
            rotorPos.getStyleClass().add("rotorPosText");
            rotorBox.getChildren().add(rotorPos);
            rotorsPosList.add(r.getPosition());
            rotorPos.setText(r.getPosition());
            con.getChildren().add(rotorBox);
        }
        configPane.getChildren().add(con);





    }
}
