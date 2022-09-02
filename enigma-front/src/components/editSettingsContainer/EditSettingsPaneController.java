package components.editSettingsContainer;

import common_models.activeRotor;
import common_models.activePlug;
import components.main.MainController;
import components.settings.SettingsController;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class EditSettingsPaneController {

    private SettingsController parentController;

    @FXML
    private Button randomBtn;
    @FXML
    private Button submitBtn;



    @FXML private  ComboBox<String> rotorsSelect;

    private ObservableList<String> rotorsIdsValues = FXCollections.observableArrayList();

    private ObservableList<String> fromSelectValues = FXCollections.observableArrayList();

    private ObservableList<String> toSelectValues = FXCollections.observableArrayList();

    @FXML
    private ComboBox<String> fromselect;

    @FXML
    private  ComboBox<String> toselect;

    @FXML private HBox rotorsPreSetCon;
    
    @FXML private  Button addRotorBtn;

    @FXML
    private Button preSetAddPlugButton;

    @FXML
    private TableView plugsTablePreSet;

    @FXML private TableColumn activePlugFromColPreSet;
    @FXML private TableColumn activePlugToColPreSet;

    @FXML private  ComboBox refselect;

    private ObservableList<String> preSetRefValues = FXCollections.observableArrayList();


    @FXML private Label removeRotorBtn;
    @FXML private Label removePlugBtn;


    private MainController mainController;


    private ObservableList<activePlug> preSetTableList = FXCollections.observableArrayList();

    private ObservableList<activeRotor> preSetRotors = FXCollections.observableArrayList();

    private ObservableList<Label> rotorsPreSetsLabelList = FXCollections.observableArrayList();



    public EditSettingsPaneController() {

    }



    @FXML
    private  void initialize(){



        refselect.setItems(preSetRefValues);

        plugsTablePreSet.setOnMousePressed(event -> {
            if(plugsTablePreSet.getItems().isEmpty()){

                return;
            }
            if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                int toRemoveIndex=plugsTablePreSet.getSelectionModel().getSelectedIndex();
                removePlugBtn.visibleProperty().set(true);
                removePlugBtn.setOnMouseClicked(event1 -> {
                    plugsTablePreSet.getItems().remove(toRemoveIndex);


                    removePlugBtn.visibleProperty().set(false);
                    toSelectValues.clear();
                });
            }
        });

        preSetTableList.addListener((ListChangeListener<? super activePlug>) pChange->{

                while(pChange.next()) {
                    if(!pChange.getRemoved().isEmpty()){
                        pChange.getRemoved().stream().forEach(r-> {
                            fromSelectValues.add(r.getFrom());
                            fromSelectValues.add(r.getTo());
                        });

                    }
                    if(!pChange.getAddedSubList().isEmpty()){
                        pChange.getAddedSubList().stream().forEach(r-> {
                            fromSelectValues.remove(r.getFrom());
                            fromSelectValues.remove(r.getTo());
                        });
                    }

                }
        });


        addRotorBtn.disableProperty().bind(rotorsSelect.valueProperty().isNull());

        preSetRotors.addListener((ListChangeListener<? super activeRotor>) c ->{
            rotorsPreSetCon.getChildren().clear();
            while(c.next()) {
                for (activeRotor n_r : c.getList()) {
                    rotorsIdsValues.remove(n_r.getId());
                    ListView<String> posList = new ListView<>();
                    VBox rotorEditCon = new VBox();
                    Label rotorIdLabel = new Label();
                    rotorIdLabel.setLabelFor(posList);
                    rotorIdLabel.setText(n_r.getId());
                    rotorEditCon.getChildren().add(rotorIdLabel);
                    rotorEditCon.getChildren().add(posList);
                    ObservableList<String> RotorPositions = FXCollections.observableArrayList();
                    rotorsPreSetsLabelList.add(rotorIdLabel);
                    posList.setItems(RotorPositions);
                    ArrayList<String> currRotor = mainController.getBackEnd().getRotorPositions(Integer.valueOf(n_r.getId()));
                    currRotor.stream().forEach(r -> RotorPositions.add(r));
                    rotorsPreSetCon.getChildren().add(rotorEditCon);


                    posList.setOnMouseClicked(event -> {
//               reset delete button if user toched another colom
                        removeRotorBtn.visibleProperty().set(false);
                        for (Label l : rotorsPreSetsLabelList) {
                            l.setTextFill(Color.BLACK);
                        }
                        rotorIdLabel.setTextFill(Color.BLACK);
                        preSetRotors.stream().filter(r -> r.getId() == n_r.getId()).findFirst().get().setPosition(posList.getSelectionModel().getSelectedItem());

                        preSetRotors.forEach(k -> {
                            System.out.println("Id: " + k.getId() + " Pos: " + k.getPosition() + " Order: " + k.getOrder());

                        });
                    });

                    rotorIdLabel.setOnMouseClicked(event -> {
                        removeRotorBtn.visibleProperty().set(true);
                        for (Label l : rotorsPreSetsLabelList) {
                            if (l.getText() == n_r.getId()) {
                                l.setTextFill(Color.RED);
                            } else {
                                l.setTextFill(Color.BLACK);
                            }

                        }


                        removeRotorBtn.setOnMouseClicked(rmvEvent -> {
                            preSetRotors.removeIf(k -> k.getId() == n_r.getId());
                            rotorsPreSetCon.getChildren().remove(rotorEditCon);
                            rotorsIdsValues.add(n_r.getId());
//update all other rotor order.
                            for (activeRotor r : preSetRotors) {
                                if (r.getOrder() > n_r.getOrder()) {
                                    r.setOrder(r.getOrder() - 1);
                                }

                            }
                            removeRotorBtn.visibleProperty().set(false);
                            rotorIdLabel.setTextFill(Color.BLACK);

                        });

                    });
                    posList.getSelectionModel().select(n_r.getPosition());
                    posList.scrollTo(n_r.getPosition());
                }
            }

        });
        addRotorBtn.setOnMousePressed(e->{
            String clickedRotorId = rotorsSelect.valueProperty().getValue();
            activeRotor formattedRotor = new activeRotor(clickedRotorId,preSetRotors.size(),"A");
            preSetRotors.add(formattedRotor);
            });
        submitBtn.setOnMouseClicked(e->{
            try {
//                TODO make this a function (maybe a service?)
                System.out.println(preSetRotors.size());
                if(preSetRotors.size()>=2  ) {
                    mainController.getBackEnd().setPlugBoardViaUserBetter(preSetTableList.stream().collect(Collectors.toList()));
                    mainController.getBackEnd().setRotorsViaUserBetter(preSetRotors.stream().collect(Collectors.toList()));
                    mainController.getBackEnd().setReflectorViaUser((String) refselect.getSelectionModel().getSelectedItem());
                    mainController.Sync();
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            System.out.println(mainController.getBackEnd().getFormatStats());

        });




        fromSelectValues.addListener((ListChangeListener<? super String>) c -> {
            toSelectValues.clear();

        });
        fromselect.setItems(fromSelectValues);
        toselect.setItems(toSelectValues);
        rotorsSelect.setItems(rotorsIdsValues);
        fromselect.valueProperty().addListener((obs, oldItem, newItem) -> {
            if(newItem==null) return;
            System.out.println(newItem);
            toSelectValues.setAll(fromSelectValues);
            toSelectValues.removeIf(e->fromselect.getValue()==e);
        });

        plugsTablePreSet.setItems(preSetTableList);
        activePlugFromColPreSet.setCellValueFactory(new PropertyValueFactory<activePlug, String>("from"));
        activePlugToColPreSet.setCellValueFactory(new PropertyValueFactory<activePlug, String>("to"));

        preSetAddPlugButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(toselect.getValue() + " " + fromselect.getValue());
                String toNode = toselect.getValue();
                String fromNode = fromselect.getValue();
                if (toNode != null && fromNode != null && fromNode!=toNode) {
                    fromSelectValues.remove(toNode);
                    fromSelectValues.remove(fromNode);


                    preSetTableList.add(new activePlug(fromNode,toNode));

                    toselect.setValue(null);
                }

            }


        });


    }


    public void setParent(SettingsController settingsController) {
        this.parentController = settingsController;

    }

    public void ClearAll() {
//        TODO--check what else to restart..
       rotorsPreSetCon.getChildren().clear();
        plugsTablePreSet.getItems().clear();
        refselect.getSelectionModel().clearSelection();
        fromSelectValues.clear();
        toSelectValues.clear();
        rotorsIdsValues.clear();
        preSetRefValues.clear();


    }

//            rotors
    public void setMainController(MainController mainController) {
        this.mainController=mainController;
       mainController.getSelectedFileProperty().addListener(e->{

           fromSelectValues.setAll(mainController.getBackEnd().getAbc().getAbc().split(""));
           mainController.getBackEnd().getAllRotorsArr().stream().forEach(r->rotorsIdsValues.add(String.valueOf(r.getId())));
           mainController.getBackEnd().getReflectorsIds().stream().forEach(r->preSetRefValues.add(r));

           mainController.getBackEnd().setReflectorViaUser(String.valueOf(refselect.valueProperty().getName()));
           randomBtn.setOnMouseClicked(c->{
               rotorsIdsValues.clear();
               mainController.getBackEnd().getAllRotorsArr().stream().forEach(r->rotorsIdsValues.add(String.valueOf(r.getId())));

               System.out.println( mainController.getBackEnd().getRandomRotors());
               preSetRotors.clear();
               mainController.getBackEnd().getRandomRotors().stream().forEach(rotor-> {
                   activeRotor newRotor= new activeRotor(rotor.get("id"),Integer.parseInt(rotor.get("order")),rotor.get("pos"));

                   preSetRotors.add(newRotor);
               });
               refselect.getSelectionModel().select(mainController.getBackEnd().getRandomRef());
               preSetTableList.clear();
               mainController.getBackEnd().getRandomPlugs().stream().forEach(p-> {
                   activePlug newPlug = new activePlug(p.get("from"), p.get("to"));
                   preSetTableList.add(newPlug);
               });
           });
       });
    }
}
