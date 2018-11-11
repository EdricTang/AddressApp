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
 * ������
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
     * ���������֣����Լ����ϴδ򿪵�person file
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

            //����MainApp �� PersonOverviewController
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
            //�����Զ��嵯�����fxml�ļ�����Ϊ�䴴���µ�stage
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
            AnchorPane page = loader.load();

            //���� dialog Stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.getIcons().add(new Image("images/edit.png"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            //��person����Դ���� controller
            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);

            //��ʾdialog�����û��ر�֮ǰһֱ��ʾ
            dialogStage.showAndWait();
            return controller.isOkClicked();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * �����û����յ�preference�ļ���preference�ǴӲ���ϵͳ�ض�λ���ж�ȡ
     * ���û�л�ȡ��preference������null
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
     * ���õ�ǰ�����ļ���·��. ·�����������ʼ�ϵͳ�ض�λ��
     * @param file
     */
    public void setPersonFilePath(File file) {

        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if (null != file) {
            prefs.put("filePath", file.getPath());

            //����stage��title
            primaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            prefs.remove("filePath");

            //����stage��title
            primaryStage.setTitle("AddressApp");
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }


    /**
     * ���ļ�����person���ݣ���ǰ��person���ݽ��ᱻ���
     * @param file
     */
    public void loadPersonDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Unmarshaller um =  context.createUnmarshaller();

            //���ļ��ͽ����ж�����
            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

            personData.clear();
            personData.addAll(wrapper.getPersons());

            //���ļ�·�������¼
            setPersonFilePath(file);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("�޷�������ȡ�ļ���\n" + file.getPath());
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();
        }
    }

    /**
     * ����ǰ�û����ݱ��浽�ض��ļ�
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
            alert.setHeaderText("�޷��������浽�ļ���\n" + file.getPath());
            alert.setContentText(e.getLocalizedMessage());
            alert.showAndWait();
        }
    }

}
