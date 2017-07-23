package client;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXToolbar;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import java.io.File;
//from ww w . jav  a 2s. c om
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.VBox;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;
import socket.FTPFactory;
import sun.reflect.generics.tree.Tree;

import javax.swing.*;

/**
 * FXML Controller class
 *
 * @author danml
 */
public class DashboardController implements Initializable {

    private Label lblDash;
    @FXML
    private StackPane stackPane;

    @FXML
    private AnchorPane holderPane;
    @FXML
    private AnchorPane sideAnchor;
    @FXML
    private Label lblMenu;
    @FXML
    private JFXToolbar toolBar;
    @FXML
    private HBox toolBarRight;
    @FXML
    private VBox overflowContainer;
    @FXML
    private ToggleButton menuHome;
    @FXML
    private ToggleButton menuAdd;
    @FXML
    private ToggleButton menuList;
    @FXML
    private ToggleButton menuLogg;

    private AnchorPane navegacao, home, add, list;
    @FXML
    private JFXButton btnLogOut;
    @FXML
    private JFXButton btnExit;
    @FXML
    private JFXButton btnHome;
    @FXML
    private JFXButton btnAdd;
    @FXML
    private JFXButton btnView;
    @FXML
    private JFXButton btnLogout;
    @FXML
    private JFXButton btnClose;
    @FXML
    private TreeView<String> Tree;
    FTPFile[] listaNegra;
    TreeItem treeItem;
    FTPFile[] files;
    FTPFile select = new FTPFile();
public static Image folderCollapseImage = new Image(ClassLoader.getSystemResourceAsStream("utilfull/folder.png"));

    public static Image file = new Image(ClassLoader.getSystemResourceAsStream("utilfull/text-x-generic.png"));
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXRippler fXRippler = new JFXRippler(lblDash);
        JFXRippler fXRippler2 = new JFXRippler(lblMenu);
        fXRippler2.setMaskType((JFXRippler.RipplerMask.RECT));
        sideAnchor.getChildren().add(fXRippler);
        toolBarRight.getChildren().add(fXRippler2);
        openMenus();
        try {
            Navegacao();
        } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void openMenus() {
        JFXPopup popup = new JFXPopup();
        popup.setContent(overflowContainer);
        popup.setPopupContainer(stackPane);
        popup.setSource(lblMenu);
        lblMenu.setOnMouseClicked((MouseEvent e) -> {
            popup.show(JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, -10, 40);
        });

    }

    private void setNode(Node node) {
        holderPane.getChildren().clear();
        holderPane.getChildren().add((Node) node);

        FadeTransition ft = new FadeTransition(Duration.millis(1500));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
    }
    private void Navegacao() throws IOException {
        files = FTPFactory.getInstance().getFTP().listFiles();
        files[0].setName(FTPFactory.getInstance().getFTP().getPassiveHost());
        Tree.setRoot( getNodesForDirectory(files[0]));

    }

    public TreeItem<String> getNodesForDirectory(FTPFile directory) throws IOException { //Returns a TreeItem representation of the specified directory

        TreeItem<String> root = new TreeItem<String>(directory.getName(),new ImageView(folderCollapseImage));
        FTPFile[] files = FTPFactory.getInstance().getFTP().listFiles();
        for (FTPFile f : files) {
            System.out.println("Loading " + f.getName());
            if (f.isDirectory()) { //Then we call the function recursively
                FTPFactory.getInstance().getFTP().changeWorkingDirectory(f.getName());
                root.getChildren().add(getNodesForDirectory(f));

            } else {
                root.getChildren().add(new TreeItem<String>(f.getName(),new ImageView(file)));
            }
        }
        FTPFactory.getInstance().getFTP().changeToParentDirectory();
        return root;
    }


    public void mouseClick(MouseEvent mouseEvent)
    {
     TreeItem<String> item = Tree.getSelectionModel().getSelectedItem();
        System.out.println(item.getValue());
    }

    @FXML
    private void logOff(ActionEvent event) {

    }

    @FXML
    private void exit(ActionEvent event) {
        Platform.exit();
    }
}

