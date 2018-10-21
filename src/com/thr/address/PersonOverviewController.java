package com.thr.address;

import com.thr.address.model.Person;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * @author Thr
 *
 * Model �� TableView
 * ���� �������ڽ�������ʾ
 * 1. ����ģ����
 * 2. �������򴴽�ģ�������͵��б���Ϊ����Դ
 * 3. �����������������������������
 * 4. ������������Դ���ݵ�������
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
    }

    /**
     * ����������ã��Ա㴫��mainApp����
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        personTable.setItems(mainApp.getPersonData());
    }
}
