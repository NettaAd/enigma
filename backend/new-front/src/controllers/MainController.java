package controllers;

import backend.BackEndMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import javax.xml.bind.JAXBException;
import java.io.File;


public class MainController {

    public Button fileChooser;
    BackEndMain backend = new BackEndMain();


    @FXML
    private Label filePathLabel;

    @FXML
    private TextField myInput;
    @FXML
    private Label myTest;



    String res;


    @FXML private settingsTabController settingsTabController;

    public void initialize() throws JAXBException {
        this.settingsTabController.setBackend(backend);
        backend.setXmlData("D:\\studying\\code\\java\\ex_one\\ex1-sanity-paper-enigma.xml");

    }

    @FXML
    void uploadFileClick(ActionEvent event) {
//        init file chooser
        Window window = ((Node) (event.getSource())).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        //        show file chooser
        File file = fileChooser.showOpenDialog(window);
        filePathLabel.textProperty().setValue(file.getAbsolutePath());
        try {
            backend.setXmlData("D:\\studying\\code\\java\\ex_one\\ex1-sanity-paper-enigma.xml");
//after xml load- clear all prev data in the tables
            this.settingsTabController.clearTables();
            System.out.println(backend.getFormatStats());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.settingsTabController.setPlugsTablePreSetSelectors();
        event.consume();
    }




    public void input(ActionEvent e){
        res=myInput.getText();
        System.out.println(res);
        myTest.textProperty().setValue(res);
    }



}
