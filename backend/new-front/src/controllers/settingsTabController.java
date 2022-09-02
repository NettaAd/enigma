package controllers;


import Machine.SpinningRotor;
import backend.BackEndMain;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import common_models.activePlug;
import common_models.activeRotor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class settingsTabController {
    private BackEndMain backend;

    public void setBackend(BackEndMain backend){
        this.backend=backend;
        System.out.println("inject!");
    }

    @FXML
    Label ref_Id;



    public TableView getRotorTable() {
        return rotorTable;
    }

    public ComboBox<String> getFromselect() {
        return fromselect;
    }

    public ComboBox<String> getToselect() {
        return toselect;
    }

    @FXML
    private TableView rotorTable;

    @FXML
    private TableColumn<activeRotor,String> activeRotorOrderCol;
    @FXML
    private  TableColumn<activeRotor,String> activeRotorIdCol;

    @FXML
    private TableView plugsTable;


    @FXML
    private  TableColumn<activePlug,String> activePlugFromCol;
    @FXML
    private  TableColumn<activePlug,String> activePlugToCol;

    @FXML
    private  TableColumn<activeRotor,String> activeRotorPositionCol;

    @FXML
    private ComboBox<String> fromselect;

    @FXML
    private  ComboBox<String> toselect;

    @FXML
    private Button preSetAddPlugButton;

    @FXML
    private TableView plugsTablePreSet;
    @FXML
    private  TableColumn<activePlug,String> activePlugFromColPreSet;
    @FXML
    private  TableColumn<activePlug,String> activePlugToColPreSet;

    @FXML
    private Button removePreSetPlug;

    @FXML
    private GridPane rotorsPreSet;

    @FXML Button addRotorButton;

    @FXML private  ScrollPane rotorsPreSetScrollBar;
    private ObservableList<String> fromSelectValues = FXCollections.observableArrayList();
    
    private ObservableList<String> toSelectValues = FXCollections.observableArrayList();

    private ObservableList<activePlug> preSetTableList = FXCollections.observableArrayList();

    ArrayList<String> plugsPreSetPicked=new ArrayList<>();

    int preSetRotorsAmount =0;

    @FXML private void initialize(){
        plugsTablePreSet.setOnMousePressed(event -> {
            if(plugsTablePreSet.getItems().isEmpty()){

                return;
            }
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                int toRemoveIndex=plugsTablePreSet.getSelectionModel().getSelectedIndex();
                removePreSetPlug.visibleProperty().set(true);
                removePreSetPlug.setOnAction(event1 -> {
                    plugsTablePreSet.getItems().remove(toRemoveIndex);


                    removePreSetPlug.visibleProperty().set(false);
                });
            }
        });

        addRotorButton.setOnAction(event -> {
            ArrayList<SpinningRotor> rotors=backend.getAllRotorsArr();

            if(rotors.size() < preSetRotorsAmount){
                return;
            }
            ComboBox<String> rotorsIdOptions = new ComboBox<>();
            ComboBox<String> rotorsPosOptions = new ComboBox<>();
            ObservableList<String> rotorsObservableOptions= FXCollections.observableArrayList();
            ObservableList<String> rotorsObservablePos= FXCollections.observableArrayList();
            rotorsPosOptions.setItems(rotorsObservablePos);
            rotorsIdOptions.setItems(rotorsObservableOptions);
            backend.getAbc().getAbcAsCollation().stream().forEach(c->rotorsObservablePos.add(c.toString()));
            System.out.println( rotorsPreSet.getChildren().size());
            rotors.stream().forEach(r->rotorsObservableOptions.add(String.valueOf(r.getId())));
            rotorsPreSet.add(rotorsIdOptions, 0   , rotorsPreSet.getChildren().size() );
            rotorsPreSet.add(rotorsPosOptions,  1, rotorsPreSet.getChildren().size()-1 );
            preSetRotorsAmount+=1;
        });
//        for (int i = 0; i < 5; i++) {
//            ComboBox<String> test = new ComboBox<>();
//            ObservableList<String> testob= FXCollections.observableArrayList();
//            test.setItems(testob);
////            test.setOnDragDone();
//            testob.add("wow");
//            testob.add("cool");
//            testob.add("amazing");
//            rotorsPreSet.add(test,  1, i, 1, 1 );
//
//        }

        fromselect.valueProperty().addListener((obs, oldItem, newItem) -> {
            if(newItem==null) return;
            String machineAbc = backend.getAbc().getAbc();

            String valuePressed = newItem;
            int pos = backend.getAbc().toIndex(valuePressed.toCharArray()[0]);
            String abcWithOutFrom = machineAbc.substring(0, pos) + machineAbc.substring(pos + 1);
            toSelectValues.setAll(fromSelectValues);
            toSelectValues.removeIf(e->fromselect.getValue()==e);
            toselect.setValue(null);
        });

        preSetTableList.addListener((ListChangeListener<activePlug>) pChange -> {
            if(preSetTableList.isEmpty()) {
                fromSelectValues.setAll(backend.getAbc().getAbc().split(""));
                plugsPreSetPicked.clear();
            }
            else
                while(pChange.next()) {
                    if(!pChange.getRemoved().isEmpty()){
                        plugsPreSetPicked.removeIf(e-> {
                            return (pChange.getRemoved().get(0).getFrom().contains(e) || pChange.getRemoved().get(0).getTo().contains(e));
                        });
                        fromSelectValues.add(pChange.getRemoved().get(0).getFrom());
                        fromSelectValues.add(pChange.getRemoved().get(0).getTo());

                    }else if(!pChange.getAddedSubList().isEmpty()){
                        String fromNode = pChange.getAddedSubList().get(0).getFrom();
                        String toNode = pChange.getAddedSubList().get(0).getTo();

                        plugsPreSetPicked.add(fromNode);
                        plugsPreSetPicked.add(toNode);
                        fromSelectValues.remove(fromNode);
                        fromSelectValues.remove(toNode);
                        toSelectValues.remove(fromNode);
                        toSelectValues.remove(fromNode);
                    }

                }
        });

        preSetAddPlugButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(toselect.getValue() + " " + fromselect.getValue());
                String toNode = toselect.getValue();
                String fromNode = fromselect.getValue();
                if (toNode != null && fromNode != null && fromNode!=toNode  && !plugsPreSetPicked.contains(toNode) && !plugsPreSetPicked.contains(fromNode)) {
                    preSetTableList.add(new activePlug(fromNode, toNode));
                    toselect.setValue(null);
                }

            }


        });
    }
    

    public void clearTables(){
            rotorTable.getItems().clear();
            plugsTable.getItems().clear();
            ref_Id.textProperty().setValue("");
    }



    public void setPlugsTablePreSetSelectors() {
        plugsTablePreSet.setItems(preSetTableList);


        fromSelectValues.setAll(backend.getAbc().getAbc().split(""));
        fromselect.setItems(fromSelectValues);
        toselect.setItems(toSelectValues);

        toselect.valueProperty().addListener((obs, oldItem, newItem) -> {
            if(newItem==null) return;

            if (newItem != "" && fromselect.getValue() != "" && newItem != fromselect.getValue()) {
                preSetAddPlugButton.disableProperty().set(false);
            }
        });

        activePlugFromColPreSet.setCellValueFactory(new PropertyValueFactory<activePlug, String>("from"));
        activePlugToColPreSet.setCellValueFactory(new PropertyValueFactory<activePlug, String>("to"));
    }

            public void random(ActionEvent actionEvent) {
        backend.RandomMachine();
        System.out.println(backend.getFormatStats());
//weird late decision to use mvc concept  and put all table data in models, seen in the next video:
//        https://www.youtube.com/watch?v=fnU1AlyuguE&ab_channel=Randomcode
//should clean that a bit
        ArrayList<HashMap<String, String>> rotors = backend.getActiveRotors();
        ArrayList<activeRotor> activeRotors = new ArrayList<>();
        for (HashMap<String, String> tmp: rotors ) {
            activeRotors.add(new activeRotor(tmp.get("id"),tmp.get("order"),tmp.get("position")) );

        }

        System.out.println(backend.getActiveRotors());
        ObservableList<activeRotor> rotorsObv = FXCollections.observableArrayList(activeRotors);
        activeRotorOrderCol.setCellValueFactory(new PropertyValueFactory<activeRotor,String>("order"));
        activeRotorIdCol.setCellValueFactory(new PropertyValueFactory<activeRotor,String>("id"));
        activeRotorPositionCol.setCellValueFactory(new PropertyValueFactory<activeRotor,String>("position"));

        rotorTable.setItems(rotorsObv);
        ArrayList<activePlug> activeModelPlugs = new ArrayList<>();
        HashMap<String, String> activePlugs=backend.getActivePlugs();

        for (Map.Entry<String, String> tmp: activePlugs.entrySet() ) {
            activeModelPlugs.add(new activePlug(tmp.getKey() ,tmp.getValue()));
        }
        ObservableList<activePlug> plugsObv = FXCollections.observableArrayList(activeModelPlugs);
        activePlugFromCol.setCellValueFactory(new PropertyValueFactory<activePlug,String>("from"));
        activePlugToCol.setCellValueFactory(new PropertyValueFactory<activePlug,String>("to"));
        plugsTable.setItems(plugsObv);
        ref_Id.textProperty().setValue(backend.getReflectorId());


    }



}
