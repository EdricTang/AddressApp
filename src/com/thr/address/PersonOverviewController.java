package com.thr.address;

import com.thr.address.model.Person;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * @author Thr
 *
 * Model 和 TableView
 * 需求： 将数据在界面上显示
 * 1. 创建模型类
 * 2. 在主程序创建模型类类型的列表作为数据源
 * 3. 创建控制器，将数据设入界面的组件
 * 4. 在主程序将数据源传递到控制器
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
    }

    /**
     * 由主程序调用，以便传回mainApp引用
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        personTable.setItems(mainApp.getPersonData());
    }
}
