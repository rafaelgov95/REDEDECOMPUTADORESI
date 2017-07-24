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
//        try {
//            FTPFactory.getInstance().FTPConecta("localhost",21,"teste","Iloveprog");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////            if(err){}
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