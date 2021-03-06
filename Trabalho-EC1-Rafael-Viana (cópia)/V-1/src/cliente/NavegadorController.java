package cliente;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXToolbar;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.Iterator;
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
import org.apache.commons.net.ftp.FTPReply;
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
    @FXML
    private JFXButton btnAddFile;
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
        int pasta = FTPFactory.getInstance().getFTP().listDirectories().length;
        int arq = FTPFactory.getInstance().getFTP().listFiles().length - pasta;
        return arq < limite.getMA();
    }

    private boolean limitePasta() throws IOException {
        int pasta = FTPFactory.getInstance().getFTP().listDirectories().length;
        return pasta < limite.getMP();
    }

    private int limiteNivel() throws IOException {
        int nivel = FTPFactory.getInstance().getFTP().printWorkingDirectory().split("/").length;
        return nivel;
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
            treeRoot = CarregarFiles(files[0], true);
        } else {
            FTPFile file = new FTPFile();
            file.setType(FTPFile.DIRECTORY_TYPE);
            file.setLink(FTPFactory.getInstance().getFTP().printWorkingDirectory());
            file.setRawListing(FTPFactory.getInstance().getFTP().getPassiveHost());
            treeRoot = new TreeItem<>(file, new ImageView(computador));
        }
        Tree.getSelectionModel().select(treeRoot);
        Tree.setRoot(treeRoot);

        btnAddFile.setOnAction(e -> {
            try {
                TreeItem<FTPFile> selected = Tree.getSelectionModel().getSelectedItem();
                if (selected.getValue().isDirectory()) {
                    FTPFactory.getInstance().getFTP().changeWorkingDirectory(selected.getValue().getLink());

                } else {
                    FTPFactory.getInstance().getFTP().changeWorkingDirectory(selected.getParent().getValue().getLink());
                }
                if (limiteNivel() <= 5) {
                    if (limiteArquivo()) {
                        String file = JOptionPane.showInputDialog("Nome do File");
                        if (file != null) {
                            if (file.isEmpty()) {
                                file = "New File";
                            }
                            if (selected.getValue().isDirectory() && selected.getChildren() != null) {
                                for (TreeItem<FTPFile> fa : selected.getChildren()) {
                                    if (file.equals(fa.getValue().getName())) {
                                        JOptionPane.showMessageDialog(null, "Erro arquivo já existente nesse diretório", "Erro arquivo existente", JOptionPane.ERROR_MESSAGE);
                                        return;
                                    }
                                }
                            } else {
                                if (selected.getParent().getValue().isDirectory() && selected.getParent().getChildren() != null) {
                                    for (TreeItem<FTPFile> fa : selected.getParent().getChildren()) {
                                        if (file.equals(fa.getValue().getName())) {
                                            JOptionPane.showMessageDialog(null, "Erro arquivo já existente nesse diretório", "Erro arquivo existente", JOptionPane.ERROR_MESSAGE);
                                            return;
                                        }
                                    }
                                }
                            }
                            FTPFactory.getInstance().getFTP().changeWorkingDirectory(selected.getValue().getLink());

                            if (selected == null) {
                                selected = treeRoot;
                            }

                            FTPFile f = new FTPFile();
                            f.setRawListing(file);
                            f.setName(file);
                            TreeItem<FTPFile> newItem = new TreeItem<FTPFile>(f, new ImageView(arquivo));
                            OutputStream out;
                            if (selected.getValue().isDirectory()) {
                                f.setType(FTPFile.DIRECTORY_TYPE);
                                newItem.getValue().setLink(selected.getValue().getLink() + separador + file);
                                out = FTPFactory.getInstance().getFTP().storeFileStream(newItem.getValue().getLink());
                                selected.getChildren().add(newItem);

                            } else {
                                f.setType(FTPFile.FILE_TYPE);
                                newItem.getValue().setLink(selected.getParent().getValue().getLink() + separador + file);
                                out = FTPFactory.getInstance().getFTP().storeFileStream(newItem.getValue().getLink());
                                selected.getParent().getChildren().add(newItem);

                            }
                            out.close();
                            if (!FTPFactory.getInstance().getFTP().completePendingCommand()) {
                                FTPFactory.getInstance().getFTP().logout();
                                FTPFactory.getInstance().getFTP().disconnect();
                                System.err.println("File transfer failed.");
                                System.exit(1);
                            }

                            JOptionPane.showMessageDialog(null, "File adicionado com sucesso !!", "File Adicionado", JOptionPane.INFORMATION_MESSAGE);
                            Tree.getSelectionModel().select(newItem);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Apenas 2 pastas por Diretório", "Limite Atingido", JOptionPane.WARNING_MESSAGE);

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Apenas 3 níveis de Diretórios", "Limite Atingido", JOptionPane.WARNING_MESSAGE);

                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }


        });

        btnAdd.setOnAction(e -> {
            try {
                TreeItem<FTPFile> selected = Tree.getSelectionModel().getSelectedItem();
                FTPFactory.getInstance().getFTP().changeWorkingDirectory(selected.getValue().getLink());
                if (limiteNivel() <= 5) {
                    if (limitePasta()) {
                        String text = JOptionPane.showInputDialog("Nome da Pasta");
                        if (text != null) {
                            if (selected == null) {
                                selected = treeRoot;
                            }
                            if (text.isEmpty()) {
                                text = "NovaPasta";
                            }
                            FTPFile f = new FTPFile();
                            f.setType(FTPFile.DIRECTORY_TYPE);
                            f.setRawListing(text);
                            f.setName(text);
                            TreeItem<FTPFile> newItem = new TreeItem<FTPFile>(f, new ImageView(pasta));

                            if (selected.getValue().isDirectory()) {
                                f.setLink(selected.getValue().getLink() + separador + text);
                                if (FTPFactory.getInstance().getFTP().makeDirectory(f.getLink())) {
                                    selected.getChildren().add(newItem);
                                    Tree.getSelectionModel().select(newItem);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Erro pasta já existente nesse diretório", "Erro arquivo existente", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                if (selected.getParent().getValue() != null) {
                                    f.setLink(selected.getParent().getValue().getLink() + separador + text);
                                    if (FTPFactory.getInstance().getFTP().makeDirectory(f.getLink())) {
                                        selected.getParent().getChildren().add(newItem);
                                        Tree.getSelectionModel().select(newItem);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Erro pasta já existente nesse diretório.", "Diretório Existente", JOptionPane.ERROR_MESSAGE);
                                    }
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

        });
        btnBaixar.disableProperty().bind(Tree.getSelectionModel().selectedItemProperty().isNull()
                .or(Tree.getSelectionModel().selectedItemProperty().isEqualTo(treeRoot)));


        btnBaixar.setOnAction(e -> {
            TreeItem<FTPFile> selected = Tree.getSelectionModel().getSelectedItem();
            if (selected.getValue().isFile()) {
                JFileChooser local = new JFileChooser();
                local.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
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
                        JOptionPane.showMessageDialog(null, "Erro ao Baixado  Arquivo : " + e1.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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
            try {
                FTPFactory.getInstance().getFTP().changeWorkingDirectory(selected.getParent().getValue().getLink());
                novolink = FTPFactory.getInstance().getFTP().printWorkingDirectory();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                String novoNome = JOptionPane.showInputDialog("Digite um novo nome para " + selected.getValue().getName());
                if (FTPFactory.getInstance().getFTP().rename(selected.getValue().getName(), novoNome)) {
                    novolink = novolink + separador + novoNome;
                    selected.getValue().setRawListing(novoNome);
                    selected.getValue().setLink(novolink);
                    RenameRecursivao(selected);
                    JOptionPane.showMessageDialog(null, " Renomeado ! ", "Rename", JOptionPane.INFORMATION_MESSAGE);
                    Tree.refresh();
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao Renomear! ", "Erro Rename", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        btnEditar.disableProperty().bind(Tree.getSelectionModel().selectedItemProperty().isNull()
                .or(Tree.getSelectionModel().selectedItemProperty().isEqualTo(treeRoot)));


        btnExcluir.setOnAction(e -> {
            TreeItem<FTPFile> selected = Tree.getSelectionModel().getSelectedItem();
            int reply;
            if (selected.getValue().isDirectory()) {
                reply = JOptionPane.showConfirmDialog(null, "Deseja deletar esta Pasta ?\n" + "Nome da Pasta: " + selected.getValue().getName(), "Confirma Exclusão", JOptionPane.YES_NO_OPTION);
            } else {
                reply = JOptionPane.showConfirmDialog(null, "Deseja deletar este Arquivo ?\n" + "Nome do Arquivo: " + selected.getValue().getName(), "Confirma Exclusão", JOptionPane.YES_NO_OPTION);
            }
            if (reply == JOptionPane.YES_OPTION) {
                if (DeletarRecursivo(selected)) {
                    selected.getParent().getChildren().remove(selected);
                    if (selected.getValue().isDirectory()) {
                        JOptionPane.showMessageDialog(null, "Pasta Foi Apagada com sucesso !.", "Exclusão", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Arquivo Foi Apagado com sucesso !.", "Exclusão", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao tentar Excluir !.", "Exclusão", JOptionPane.WARNING_MESSAGE);

                }
            }


        });

        btnExcluir.disableProperty().bind(Tree.getSelectionModel().selectedItemProperty().isNull()
                .or(Tree.getSelectionModel().selectedItemProperty().isEqualTo(treeRoot)));

        TextField textField = new TextField();


    }

    public boolean DeletarRecursivo(TreeItem<FTPFile> a) {
        boolean flag = false;

        if (a.getChildren().isEmpty()) {
            if (FTPFactory.getInstance().Excluir(a.getValue())) {
                return true;
            }
        } else {
            for (TreeItem<FTPFile> iterator : a.getChildren()) {
                DeletarRecursivo(iterator);
            }
            if (FTPFactory.getInstance().Excluir(a.getValue())) {
                return true;
            }
        }
        return false;
    }


    public void RenameRecursivao(TreeItem<FTPFile> a) {
        String novolink;
        for (Iterator<TreeItem<FTPFile>> iterator = a.getChildren().iterator(); iterator.hasNext(); ) {
            TreeItem<FTPFile> c = iterator.next();
            novolink = a.getValue().getLink() + separador + c.getValue().getName();
            c.getValue().setLink(novolink);
            if (!c.getChildren().isEmpty()) {
                RenameRecursivao(c);
            }

        }
    }

    public TreeItem<FTPFile> CarregarFiles(FTPFile directory, boolean v) throws IOException {
        TreeItem<FTPFile> root;

        if (v) {
            directory.setType(FTPFile.DIRECTORY_TYPE);
            directory.setLink(FTPFactory.getInstance().getFTP().printWorkingDirectory());
            root = new TreeItem<>(directory, new ImageView(computador));

        } else {
            root = new TreeItem<>(directory, new ImageView(pasta));
        }
        root.setExpanded(true);
        FTPFile[] files = FTPFactory.getInstance().getFTP().listFiles();
        for (FTPFile f : files) {
            System.out.println("Carregando .. " + f.getName());
            if (f.isDirectory()) {
                FTPFactory.getInstance().getFTP().changeWorkingDirectory(f.getName());
                f.setLink(FTPFactory.getInstance().getFTP().printWorkingDirectory());
                root.getChildren().add(CarregarFiles(f, false));
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

