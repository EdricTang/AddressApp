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
 * 二. Model 和 TableView
 * 需求： 将数据在界面上显示
 * 1. 创建模型类
 * 2. 在主程序创建模型类类型的列表作为数据源
 * 3. 创建控制器，将数据设入界面的组件
 * 4. 在主程序将数据源传递到控制器
 *
 * 三. 与用户的交互
 * 1. 在控制器中添加方法，对组件的内容进行处理
 * 2. 在控制器的initialize()方法中对组件添加事件监听
 *
 * 如何弹出对话框
 * 1. 下载controlsfx-8.0.6_20.jar  https://github.com/marcojakob/tutorial-javafx-8/releases/download/v1.0/controlsfx-8.0.6_20.jar
 * 2. 添加到工程引用
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

    //主程序的对象引用
    private MainApp mainApp;

    //构造方法，在initialize()方法调用之前被调用
    public PersonOverviewController(){
    }

    /**
     * 初始化Controller类，该方法在fxml文件被加载后被自动执行
     */
    @FXML
    private void initialize() {
        firstNameColumn.setCellValueFactory(param -> param.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(param -> param.getValue().lastNameProperty());

        //添加表格的监听事件
        showPersonDetails(null);
        personTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    /**
     * 由主程序调用，以便传回mainApp引用
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        personTable.setItems(mainApp.getPersonData());
    }

    /**
     * 填充右侧文本区域以显示person的所有信息
     * 如果person为空，则文本区域的内容显示为空
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
     * 点击删除按钮事件
     * 注意@FXML注解，添加后可以让Scene Builder访问
     */
    @FXML
    private void handleDeletePerson() {
        //注意，如果表格中未选择人员，selectedIndex = -1；所以尽量提供用户错误信息-> 提供对话框
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();

        if (selectedIndex >= 0) {
            personTable.getItems().remove(selectedIndex);
        } else {
            //创建弹出框
            //以下方式为非官方，使用报错
            /*Dialogs.create().title("No Selection")
                    .masthead("No Person Selected")
                    .message("Please select a person in the table.")
                    .showWarning();*/
            //推荐使用方式
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            alert.showAndWait();
        }
    }

    /**
     * 当用户点击新建按钮时显示
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
