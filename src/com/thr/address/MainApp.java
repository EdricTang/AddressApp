package com.thr.address;

import com.thr.address.model.Person;
import com.thr.address.model.PersonListWrapper;
import com.thr.address.view.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.prefs.Preferences;

/**
 * 主程序
 *
 * @author Thr
 */
public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private ObservableList<Person> personData = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    public MainApp() {
        personData.add(new Person("Hans", "Muster"));
        personData.add(new Person("Ruth", "Mueller"));
        personData.add(new Person("Heinz", "Kurz"));
        personData.add(new Person("Cornelia", "Meier"));
        personData.add(new Person("Werner", "Meyer"));
        personData.add(new Person("Lydia", "Kunz"));
        personData.add(new Person("Anna", "Best"));
        personData.add(new Person("Stefan", "Meier"));
        personData.add(new Person("Martin", "Mueller"));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("AddressApp");

        this.primaryStage.getIcons().add(new Image("images/address_book_32.png"));

        initRootLayout();

        showPersonOverview();
    }

    /**
     * 加载主布局，尝试加载上次打开的person file
     */
    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            RootLayoutController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = getPersonFilePath();
        if (null != file) {
            loadPersonDataFromFile(file);
        }
    }

    private void showPersonOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
            AnchorPane personOverView = loader.load();
            rootLayout.setCenter(personOverView);

            //连接MainApp 和 PersonOverviewController
            PersonOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Person> getPersonData() {
        return personData;
    }

    public boolean showPersonEditDialog(Person person) {

        try {
            //加载自定义弹出框的fxml文件，并为其创建新的stage
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
            AnchorPane page = loader.load();

            //创建 dialog Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.getIcons().add(new Image("images/edit.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            //将person数据源设入 controller
            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            //显示dialog，在用户关闭之前一直显示
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 返回用户最终的preference文件，preference是从操作系统特定位置中读取
     * 如果没有获取到preference，返回null
     */
    public File getPersonFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath", null);
        if (null != filePath) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    /**
     * 设置当前加载文件的路径. 路径被保存在邮件系统特定位置
     * @param file
     */
    public void setPersonFilePath(File file) {

        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (null != file) {
            prefs.put("filePath", file.getPath());

            //更新stage的title
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            //更新stage的title
            primaryStage.setTitle("AddressApp");
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }


    /**
     * 从文件加载person数据，当前的person数据将会被替代
     * @param file
     */
    public void loadPersonDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Unmarshaller um =  context.createUnmarshaller();

            //从文件和解组中读数据
            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

            personData.clear();
            personData.addAll(wrapper.getPersons());

            //把文件路径保存记录
            setPersonFilePath(file);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("无法正常读取文件：\n" + file.getPath());
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();
        }
    }

    /**
     * 将当前用户数据保存到特定文件
     * @param file
     */
    public void savePersonDataToFile(File file) {

        try {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPersons(personData);

            m.marshal(wrapper, file);

            setPersonFilePath(file);

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("无法正常保存到文件：\n" + file.getPath());
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();
        }
    }

}
