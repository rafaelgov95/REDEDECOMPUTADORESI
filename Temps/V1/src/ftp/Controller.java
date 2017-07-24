package ftp;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Controller {
    @FXML
    private Button serverConnectButton;
    @FXML
    private Button selectFileButton;
    @FXML
    private Button transferFileButton;
    @FXML
    private Button serverDisconnectButton;
    @FXML
    private Button HelpButton;
    @FXML
    private Button ExitButton;
    @FXML
    private TextArea textArea;

    private File file = null;
    private Socket socket;
    DataInputStream dis;
    DataOutputStream dos;
    BufferedReader br;


    @FXML
    void onConnectButtonClick() {
        try {
            socket = new Socket("127.0.0.1", 21);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            String reply = dis.readUTF();
            if (reply.equals("READY")) {
                textArea.appendText("Connected to server\n");
                serverConnectButton.setDisable(true);
                serverDisconnectButton.setDisable(false);
                selectFileButton.setDisable(false);
            } else textArea.appendText("Unable to connect to server. Server message:" + reply + "\n");
        } catch (IOException e) {
            textArea.appendText("Unable to connect to server - did you start it?\n");
        }
    }

    @FXML
    void onFileSelectButtonClick() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Enabled", "*.txt", "*.html", "*.htm", "*.xml")
        );
        fc.setTitle("Select file");
        file = fc.showOpenDialog(new Stage());
        if (file != null) {
            textArea.appendText("File to Transfer is:" + file.toString() + "\n");
            transferFileButton.setDisable(false);
        } else
            textArea.appendText("Save command cancelled by user - no file selected\n");
    }

    @FXML
    void onTransferButtonClick() {
        try {
            dos.writeUTF("RECEIVE");
            if (dis.readUTF().equals("READY")) {
                dos.writeUTF(file.getName());
                br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    dos.writeUTF(line);
                }
                dos.writeUTF("EOF");
                br.close();
                if (dis.readUTF().equals("OK"))
                    textArea.appendText("File transferred to server.\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void onDisconnectButtonClick() {
        disconnect();
    }

    private void disconnect() {
        try {
            if (socket.isConnected() || !socket.isClosed()) {
                dos.writeUTF("DISCONNECT");
                socket.close();
                dis.close();
                dos.close();
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            serverConnectButton.setDisable(false);
            serverDisconnectButton.setDisable(true);
            transferFileButton.setDisable(true);
            textArea.appendText("Disconnected from server.\n");
        }
    }

    @FXML
    void onHelpButtonClick() {

    }

    @FXML
    void onExitButtonClick() {
        disconnect();
        Platform.exit();
    }


}