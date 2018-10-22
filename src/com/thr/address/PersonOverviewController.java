package com.thr.address;

import com.thr.address.model.Person;
import com.thr.address.util.DateUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * @author Thr
 *
 * ��. Model �� TableView
 * ���� �������ڽ�������ʾ
 * 1. ����ģ����
 * 2. �������򴴽�ģ�������͵��б���Ϊ����Դ
 * 3. �����������������������������
 * 4. ������������Դ���ݵ�������
 *
 * ��. ���û��Ľ���
 * 1. �ڿ���������ӷ���������������ݽ��д���
 * 2. �ڿ�������initialize()�����ж��������¼�����
 *
 * ��ε����Ի���
 * 1. ����controlsfx-8.0.6_20.jar  https://github.com/marcojakob/tutorial-javafx-8/releases/download/v1.0/controlsfx-8.0.6_20.jar
 * 2. ��ӵ���������
 */
public class PersonOverviewController  {

    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private Label firstNameLabel;
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

    //������Ķ�������
    private MainApp mainApp;

    //���췽������initialize()��������֮ǰ������
    public PersonOverviewController(){
    }

    /**
     * ��ʼ��Controller�࣬�÷�����fxml�ļ������غ��Զ�ִ��
     */
    @FXML
    private void initialize() {
        firstNameColumn.setCellValueFactory(param -> param.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(param -> param.getValue().lastNameProperty());

        //��ӱ��ļ����¼�
        showPersonDetails(null);
        personTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    /**
     * ����������ã��Ա㴫��mainApp����
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        personTable.setItems(mainApp.getPersonData());
    }

    /**
     * ����Ҳ��ı���������ʾperson��������Ϣ
     * ���personΪ�գ����ı������������ʾΪ��
     * @param person
     */
    private void showPersonDetails(Person person) {

        if (null != person) {
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            cityLabel.setText(person.getCity());
            birythdayLabel.setText(DateUtil.format(person.getBirthday()));

        } else {
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birythdayLabel.setText("");
        }
    }

    /**
     * ���ɾ����ť�¼�
     * ע��@FXMLע�⣬��Ӻ������Scene Builder����
     */
    @FXML
    private void handleDeletePerson() {
        //ע�⣬��������δѡ����Ա��selectedIndex = -1�����Ծ����ṩ�û�������Ϣ-> �ṩ�Ի���
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            personTable.getItems().remove(selectedIndex);
        } else {
            //����������
            //���·�ʽΪ�ǹٷ���ʹ�ñ���
            /*Dialogs.create().title("No Selection")
                    .masthead("No Person Selected")
                    .message("Please select a person in the table.")
                    .showWarning();*/
            //�Ƽ�ʹ�÷�ʽ
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            alert.showAndWait();
        }
    }

    /**
     * ���û�����½���ťʱ��ʾ
     */
    @FXML
    private void handleNewPerson() {
        Person tempPerson = new Person();
        boolean okClicked = mainApp.showPersonEditDialog(tempPerson);
        if (okClicked) {
            mainApp.getPersonData().add(tempPerson);
        }
    }

    @FXML
    private void handleEditPerson() {
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();
        if (null != selectedPerson) {
            boolean okClicked = mainApp.showPersonEditDialog(selectedPerson);
            if (okClicked) {
                showPersonDetails(selectedPerson);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            alert.showAndWait();
        }
    }
}
