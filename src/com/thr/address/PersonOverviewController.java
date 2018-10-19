package com.thr.address;

import com.thr.address.model.Person;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * @author Thr
 */
public class PersonOverviewController  {

    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColum;
    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private Label fitstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birythdayLabel;

    private MainApp mainApp;

    public PersonOverviewController(){
    }

    @FXML
    private void initialize() {
        firstNameColum.setCellValueFactory(param -> param.getValue().firstNameProperty());
        firstNameColum.setCellValueFactory(param -> param.getValue().lastNameProperty());
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        personTable.setItems(mainApp.getPersonData());
    }
}
