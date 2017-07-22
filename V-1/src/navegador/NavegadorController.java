
package navegador;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;
import socket.FTPFactory;

/**
 * FXML Controller class
 *
 * @author danml
 */
public class NavegadorController implements Initializable {

    @FXML
    private HBox groupHolder;
    @FXML
    private Group groupRegistered;
    @FXML
    private Group groupTarget;
    @FXML
    private Group groupGents;
    @FXML
    private Group groupLadies;
    @FXML
    private JFXListView<String> departmentList;
    private ObservableList<String> departments;
    @FXML
    private Button btnAddDepartment;

    protected Connection connection;
    Statement stmt = null;
    ResultSet rs = null;
    JFXTextField txtDepartment;
    @FXML
    ListView<org.apache.commons.net.ftp.FTPFile> remoteList;
    private String currentRemoteDir = "";
    @FXML
    private StackPane deptStackPane;
    String directory= ".";
    FTPListParseEngine engine;
    FTPFile[] listaNegra ;
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }


    private void comeca()throws IOException{
        engine = FTPFactory.getInstance().getFTP().initiateListParsing(directory);
         listaNegra = engine.getFiles();
    }


    @FXML
    private void addDepartment(ActionEvent event) {


    }



}
