package cliente;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXToolbar;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.apache.commons.net.ftp.FTPFile;
import socket.FTPFactory;
import socket.Limitador;

import javax.swing.*;

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
    @FXML
    private JFXButton btnEditar;
    @FXML
    private JFXButton btnUp;
    @FXML
    private JFXButton btnBaixar;

    private AnchorPane nav;

    @FXML
    private TreeView<FTPFile> Tree;


    Limitador limite = new Limitador(5, 2);
    String separador = System.getProperty("file.separator");
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


    private boolean limiteArquivo() throws IOException {
        int num = FTPFactory.getInstance().getFTP().listDirectories().length;
        int numa = FTPFactory.getInstance().getFTP().listFiles().length - num;
        return numa < limite.getMA();
    }

    private boolean limitePasta() throws IOException {
        int num_diretorios = FTPFactory.getInstance().getFTP().listDirectories().length;
        return num_diretorios < limite.getMP();
    }

    private int limiteNivel() throws IOException {
        int num = FTPFactory.getInstance().getFTP().printWorkingDirectory().split("separador").length;
        return num;
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

    private void Navegacao() throws IOException {
        FTPFile files[];


        TreeItem<FTPFile> treeRoot;

        files = FTPFactory.getInstance().getFTP().listFiles();
        Tree.setEditable(true);

        if (files != null && files.length > 0) {
            files[0].setRawListing(FTPFactory.getInstance().getFTP().getPassiveHost());
            treeRoot = getNodesForDirectory(files[0], true);
        } else {
            FTPFile file = new FTPFile();
            file.setType(FTPFile.DIRECTORY_TYPE);
            file.setLink(FTPFactory.getInstance().getFTP().printWorkingDirectory());
            file.setRawListing(FTPFactory.getInstance().getFTP().getPassiveHost());
            treeRoot = new TreeItem<>(file, new ImageView(computador));
        }
        Tree.getSelectionModel().select(treeRoot);
        Tree.setRoot(treeRoot);


        btnBaixar.disableProperty().bind(Tree.getSelectionModel().selectedItemProperty().isNull()
                .or(Tree.getSelectionModel().selectedItemProperty().isEqualTo(treeRoot)));


        btnBaixar.setOnAction(e -> {
            TreeItem<FTPFile> selected = Tree.getSelectionModel().getSelectedItem();
            if (selected.getValue().isFile()) {
                JFileChooser local = new JFileChooser();
                local.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                local.setDialogTitle("Escolha um local para salvar");
                local.setFileHidingEnabled(false);
                int res = local.showSaveDialog(null);
                if (res == JFileChooser.APPROVE_OPTION) {
                    String caminho = String.valueOf(local.getSelectedFile());
                    try {
                        FileOutputStream fos = new FileOutputStream(caminho);
                        if (FTPFactory.getInstance().getFTP().retrieveFile(selected.getValue().getLink(), fos)) {
                            JOptionPane.showMessageDialog(null, "Arquivo Baixado com Sucesso!" + "\n\n",
                                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        }

                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro ao Baixado  Arquivo : " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Somente Arquivos!" + "\n\n",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });


        btnUp.setOnAction(e -> {
            TreeItem<FTPFile> selected = Tree.getSelectionModel().getSelectedItem();
            try {
                FTPFactory.getInstance().getFTP().changeWorkingDirectory(selected.getValue().getLink());
                if (limiteNivel() <= 5) {
                    if (limiteArquivo()) {
                        JFileChooser fc = new JFileChooser();
                        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        int result = fc.showOpenDialog(null);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            File arquivo = fc.getSelectedFile();
                            InputStream isArquivo = null;
                            try {
                                isArquivo = new FileInputStream(arquivo.getAbsolutePath());
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            }
                            try {
                                FTPFile f = new FTPFile();

                                if (selected.getValue().isDirectory()) {
                                    FTPFactory.getInstance().getFTP().changeWorkingDirectory(selected.getValue().getLink());
                                    f.setLink(selected.getValue().getLink() + separador + arquivo.getName());
                                } else {
                                    FTPFactory.getInstance().getFTP().changeWorkingDirectory(selected.getParent().getValue().getLink());
                                    f.setLink(selected.getParent().getValue().getLink() + separador + arquivo.getName());
                                }

                                if (FTPFactory.getInstance().getFTP().storeFile(arquivo.getName(), isArquivo)) {

                                    f.setName(arquivo.getName());
                                    f.setRawListing(arquivo.getName());
                                    f.setType(FTPFile.FILE_TYPE);
                                    TreeItem<FTPFile> newItem = new TreeItem<FTPFile>(f, new ImageView(this.arquivo));
                                    if (selected.getValue().isDirectory()) {
                                        selected.getChildren().add(newItem);
                                    } else {
                                        selected.getParent().getChildren().add(newItem);
                                    }

                                    JOptionPane.showMessageDialog(null, "Arquivo Enviado!");
                                } else {
                                    JOptionPane.showMessageDialog(null, "Arquivo não enviado!");
                                }


                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }

                        } else {
                            JOptionPane.showMessageDialog(null, "Arquivo não selecionado!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Apenas 2 arquivos por pasta", "Limite Atingido", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Apenas 3 níveis de Diretórios", "Limite Atingido", JOptionPane.WARNING_MESSAGE);

                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });


        btnEditar.setOnAction(e -> {
            TreeItem<FTPFile> selected = Tree.getSelectionModel().getSelectedItem();
            String novolink = "";
            if (selected.getParent().getValue() != null) {

                try {
                    FTPFactory.getInstance().getFTP().changeWorkingDirectory(selected.getParent().getValue().getLink());
                    novolink = FTPFactory.getInstance().getFTP().printWorkingDirectory();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    String novoNome = JOptionPane.showInputDialog("Digite um novo nome para " + selected.getValue().getName());

                    if (FTPFactory.getInstance().getFTP().rename(selected.getValue().getName(), novoNome)) {
                        selected.getValue().setRawListing(novoNome);
                        selected.getValue().setLink(novolink + separador + novoNome);
                        Tree.refresh();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

        });

        btnEditar.disableProperty().bind(Tree.getSelectionModel().selectedItemProperty().isNull()
                .or(Tree.getSelectionModel().selectedItemProperty().isEqualTo(treeRoot)));


        btnExcluir.setOnAction(e -> {
            TreeItem<FTPFile> selected = Tree.getSelectionModel().getSelectedItem();
            int reply = JOptionPane.showConfirmDialog(null, "Deseja deletar esse arquivo ?", "Confirma Exclusão", JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                if (FTPFactory.getInstance().Excluir(selected.getValue())) {
                    System.out.println(selected.getValue().getLink());
                    selected.getParent().getChildren().remove(selected);
                    JOptionPane.showMessageDialog(null, "Excluido ", "OK", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao excluir arquivo ", "Erro", JOptionPane.WARNING_MESSAGE);
                }
            }

        });


        btnExcluir.disableProperty().bind(Tree.getSelectionModel().selectedItemProperty().isNull()
                .or(Tree.getSelectionModel().selectedItemProperty().isEqualTo(treeRoot)));

        TextField textField = new TextField();


        EventHandler<ActionEvent> addAction = e -> {
            try {
                TreeItem<FTPFile> selected = Tree.getSelectionModel().getSelectedItem();
                FTPFactory.getInstance().getFTP().changeWorkingDirectory(selected.getValue().getLink());
                if (limiteNivel() <= 5) {
                    if (limitePasta()) {
                        if (selected == null) {
                            selected = treeRoot;
                        }
                        String text = JOptionPane.showInputDialog("Nome da Pasta");
                        if (text.isEmpty()) {
                            text = "NovaPasta";
                        }
                        FTPFile f = new FTPFile();
                        f.setType(FTPFile.DIRECTORY_TYPE);
                        f.setRawListing(text);
                        f.setName(text);
                        TreeItem<FTPFile> newItem = new TreeItem<FTPFile>(f, new ImageView(pasta));

                        if (selected.getValue().isDirectory()) {
                            f.setLink(selected.getValue().getLink() +separador + text);
                            System.out.println("Link: " + f.getLink());
                            if (FTPFactory.getInstance().getFTP().makeDirectory(f.getLink())) {
                                selected.getChildren().add(newItem);
                                Tree.getSelectionModel().select(newItem);
                            } else {
                                JOptionPane.showMessageDialog(null, "Erro pasta nao pode ser criado com esse nome.", "Diretório Existente", JOptionPane.WARNING_MESSAGE);
                            }
                        } else {
                            if (selected.getParent().getValue() != null) {
                                f.setLink(selected.getParent().getValue().getLink() + separador + text);
                                System.out.println("Link: " + f.getLink());
                                if (FTPFactory.getInstance().getFTP().makeDirectory(f.getLink())) {
                                    selected.getParent().getChildren().add(newItem);
                                    Tree.getSelectionModel().select(newItem);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Erro pasta nao pode ser criado com esse nome.", "Diretório Existente", JOptionPane.WARNING_MESSAGE);
                                }
                            }

                        }

                    } else {
                        JOptionPane.showMessageDialog(null, "Apenas 5 pastas por Diretório", "Limite Atingido", JOptionPane.WARNING_MESSAGE);

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Apenas 3 níveis de Diretórios", "Limite Atingido", JOptionPane.WARNING_MESSAGE);

                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        };
        textField.setOnAction(addAction);
        btnAdd.setOnAction(addAction);

    }


    public TreeItem<FTPFile> getNodesForDirectory(FTPFile directory, boolean v) throws IOException {
        TreeItem<FTPFile> root;
        if (v) {
            directory.setType(FTPFile.DIRECTORY_TYPE);
            directory.setLink(FTPFactory.getInstance().getFTP().printWorkingDirectory());
            root = new TreeItem<FTPFile>(directory, new ImageView(computador));

        } else {
            root = new TreeItem<FTPFile>(directory, new ImageView(pasta));
        }
        FTPFile[] files = FTPFactory.getInstance().getFTP().listFiles();
        for (FTPFile f : files) {
            System.out.println("Carregando .. " + f.getName());
            if (f.isDirectory()) {
                FTPFactory.getInstance().getFTP().changeWorkingDirectory(f.getName());
                f.setLink(FTPFactory.getInstance().getFTP().printWorkingDirectory());
                System.out.println(f.getLink());
                root.getChildren().add(getNodesForDirectory(f, false));
            } else {
                f.setLink(FTPFactory.getInstance().getFTP().printWorkingDirectory() + separador + f.getName());
                root.getChildren().add(new TreeItem<FTPFile>(f, new ImageView(this.arquivo)));
            }
        }
        FTPFactory.getInstance().getFTP().changeToParentDirectory();
        return root;
    }


    @FXML
    private void logOff(ActionEvent event) throws IOException {
        FTPFactory.getInstance().disconnect();


    }

    @FXML
    private void exit(ActionEvent event) {
        FTPFactory.getInstance().disconnect();
        Platform.exit();
    }
}

