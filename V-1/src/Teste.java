//import javafx.application.Application;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextField;
//import javafx.scene.control.TreeItem;
//import javafx.scene.control.TreeView;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.HBox;
//import javafx.stage.Stage;
//import org.apache.commons.net.ftp.FTPFile;
//import socket.FTPFactory;
//
//import java.io.IOException;
//
//public class Teste extends Application {
//
//    @Override
//    public void start(Stage primaryStage) throws IOException {
//        FTPFile files[] ;
//
//        FTPFile file = new FTPFile();
//        file.setRawListing("New File");
//        TreeItem<FTPFile> treeRoot = new TreeItem<FTPFile>(file);
//
//        treeRoot.setExpanded(true);
//
//        TreeView<FTPFile> tree = new TreeView<>(treeRoot);
//
//
//
//        try {
//            FTPFactory.getInstance().FTPConecta("localhost","teste","Iloveprog");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        files = FTPFactory.getInstance().getFTP().listFiles();
//
//        TreeItem<FTPFile> root;
//        if(files != null && files.length> 0){
//            files[0].setRawListing(FTPFactory.getInstance().getFTP().getPassiveHost());
//            TreeItem<FTPFile> rooot = getNodesForDirectory(files[0],true);
//            tree.getSelectionModel().select(rooot);
//            tree.setRoot(rooot);
//        }else {
//
//            file.setRawListing(FTPFactory.getInstance().getFTP().getPassiveHost());
//            file.setLink(FTPFactory.getInstance().getFTP().getPassiveHost());
//            file.setName(FTPFactory.getInstance().getFTP().getPassiveHost());
//            tree.getSelectionModel().select(treeRoot);
//            root = new TreeItem<>(file);
//            tree.getSelectionModel().select(root);
//            tree.setRoot(root);
//        }
//
//
//
//
//
//        Button delete = new Button("Delete");
//
//
//        delete.setOnAction(e -> {
//            TreeItem<FTPFile> selected = tree.getSelectionModel().getSelectedItem();
//            selected.getParent().getChildren().remove(selected);
//        });
//
//
//        delete.disableProperty().bind(tree.getSelectionModel().selectedItemProperty().isNull()
//                .or(tree.getSelectionModel().selectedItemProperty().isEqualTo(treeRoot)));
//
//        TextField textField = new TextField();
//        Button add = new Button("Add");
//        EventHandler<ActionEvent> addAction = e -> {
//            TreeItem<FTPFile> selected = tree.getSelectionModel().getSelectedItem();
//            if (selected == null) {
//                selected = treeRoot ;
//            }
//            String text = textField.getText();
//            if (text.isEmpty()) {
//                text = "New Item";
//            }
//
//            TreeItem<FTPFile> newItem = new TreeItem<FTPFile>(file);
//            selected.getChildren().add(newItem);
//            newItem.setExpanded(true);
//            tree.getSelectionModel().select(newItem);
//        };
//        textField.setOnAction(addAction);
//        add.setOnAction(addAction);
//
//        HBox controls = new HBox(5, textField, add, delete);
//        controls.setAlignment(Pos.CENTER);
//        controls.setPadding(new Insets(5));
//
//        BorderPane rooot = new BorderPane(tree, null, null, controls, null);
//        Scene scene = new Scene(rooot, 600, 600);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//
//
//    public TreeItem<FTPFile> getNodesForDirectory(FTPFile directory, boolean v) throws IOException {
//
//        TreeItem<FTPFile> root;
//        if(v){
//            root = new TreeItem<FTPFile>(directory);
//        }else{
//            root = new TreeItem<FTPFile>(directory);
//        }
//
//        FTPFile[] files = FTPFactory.getInstance().getFTP().listFiles();
//        for (FTPFile f : files) {
//            System.out.println("Carregando .. " + f.getName());
//            System.out.println(f.getLink());
//            if (f.isDirectory()) {
//                FTPFactory.getInstance().getFTP().changeWorkingDirectory(f.getName());
//                f.setLink(  FTPFactory.getInstance().getFTP().printWorkingDirectory());
//                root.getChildren().add(getNodesForDirectory(f,false));
//            } else {
//                f.setLink(FTPFactory.getInstance().getFTP().printWorkingDirectory()+"/"+f.getName());
//                root.getChildren().add(new TreeItem<FTPFile>(f));
//            }
//        }
//        FTPFactory.getInstance().getFTP().changeToParentDirectory();
//        return root;
//    }
//
//}