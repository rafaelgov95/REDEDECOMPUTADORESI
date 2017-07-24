package cliente;

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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
//from ww w . jav  a 2s. c om
import javafx.scene.layout.VBox;
import org.apache.commons.net.ftp.FTPFile;
import socket.FTPFactory;

/**
 * FXML Controller class
 *
 * @author rafael
 */
public class NavegadorController implements Initializable {

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
    private JFXButton btnExcluir;
    @FXML
    private JFXButton btnAdd;

    private AnchorPane nav;

    @FXML
    private TreeView<FTPFile> Tree;


    public static Image computador = new Image(ClassLoader.getSystemResourceAsStream("icons/computer.png"));
    public static Image pasta = new Image(ClassLoader.getSystemResourceAsStream("icons/folder.png"));
    public static Image arquivo = new Image(ClassLoader.getSystemResourceAsStream("icons/text-x-generic.png"));

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
            Logger.getLogger(NavegadorController.class.getName()).log(Level.SEVERE, null, ex);
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

    @FXML
    private void Excluir() throws IOException {
       if(FTPFactory.getInstance().Excluir()){
           System.out.println("Apago");

       }else{
           System.out.println("nao Apago");
       }
    }




    private void Navegacao() throws IOException {
        FTPFile files[] ;

        FTPFile file = new FTPFile();

        file.setRawListing("New File");


        TreeItem<FTPFile> treeRoot ;

        files = FTPFactory.getInstance().getFTP().listFiles();

        if(files != null && files.length> 0){
            files[0].setRawListing(FTPFactory.getInstance().getFTP().getPassiveHost());
            treeRoot = getNodesForDirectory(files[0],true);
            Tree.getSelectionModel().select(treeRoot);
            Tree.setRoot(treeRoot);
        }else {
            file.setRawListing(FTPFactory.getInstance().getFTP().getPassiveHost());
            file.setLink(FTPFactory.getInstance().getFTP().getPassiveHost());
            file.setName(FTPFactory.getInstance().getFTP().getPassiveHost());
            treeRoot = new TreeItem<>(file);
            Tree.getSelectionModel().select(treeRoot);
            Tree.setRoot(treeRoot);
        }

        btnExcluir.setOnAction(e -> {
            TreeItem<FTPFile> selected = Tree.getSelectionModel().getSelectedItem();
            selected.getParent().getChildren().remove(selected);
        });


        btnExcluir.disableProperty().bind(Tree.getSelectionModel().selectedItemProperty().isNull()
                .or(Tree.getSelectionModel().selectedItemProperty().isEqualTo(treeRoot)));

        TextField textField = new TextField();


        EventHandler<ActionEvent> addAction = e -> {
            TreeItem<FTPFile> selected = Tree.getSelectionModel().getSelectedItem();
            if (selected == null) {
                selected = treeRoot ;
            }
            String text = textField.getText();
            if (text.isEmpty()) {
                text = "New Item";
            }

            TreeItem<FTPFile> newItem = new TreeItem<FTPFile>(file);
            selected.getChildren().add(newItem);
            newItem.setExpanded(true);
            Tree.getSelectionModel().select(newItem);
        };
        textField.setOnAction(addAction);
        btnAdd.setOnAction(addAction);

    }




    public TreeItem<FTPFile> getNodesForDirectory(FTPFile directory, boolean v) throws IOException {

        TreeItem<FTPFile> root;
        if(v){
            root = new TreeItem<FTPFile>(directory);
        }else{
            root = new TreeItem<FTPFile>(directory);
        }

        FTPFile[] files = FTPFactory.getInstance().getFTP().listFiles();
        for (FTPFile f : files) {
            System.out.println("Carregando .. " + f.getName());
            System.out.println(f.getLink());
            if (f.isDirectory()) {
                FTPFactory.getInstance().getFTP().changeWorkingDirectory(f.getName());
                f.setLink(  FTPFactory.getInstance().getFTP().printWorkingDirectory());
                root.getChildren().add(getNodesForDirectory(f,false));
            } else {
                f.setLink(FTPFactory.getInstance().getFTP().printWorkingDirectory()+"/"+f.getName());
                root.getChildren().add(new TreeItem<FTPFile>(f));
            }
        }
        FTPFactory.getInstance().getFTP().changeToParentDirectory();
        return root;
    }


    @FXML
    public void mouseClick(MouseEvent mouseEvent)

    {
//        TreeItem<FTPFile> item = tree.getSelectionModel().getSelectedItem();
//
//        System.out.println(item.getValue().getLink());
    }

    @FXML
    private void logOff(ActionEvent event) {

    }

    @FXML
    private void exit(ActionEvent event) {
             Platform.exit();
    }
}

