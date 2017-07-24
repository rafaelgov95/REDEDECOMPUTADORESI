/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Arrays;
//import org.apache.commons.net.PrintCommandListener;
import javafx.scene.control.TreeItem;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;
import org.apache.commons.net.ftp.FTPReply;


/**
 *
 * @author rafael
 */
public class FTPFactory {

    private final FTPClient ftp;
    private TreeItem<FTPFile> file;
    private FTPFactory() {
        this.ftp = new FTPClient();
//        this.file = new TreeItem<FTPFile>();
    }

    public static FTPFactory getInstance() {
        
        return FTPFactoryHolder.INSTANCE;
    }


    /**
     * Classe privada que armazena a única instância de FTPFactory.
     */
    private static class FTPFactoryHolder {

        private static final FTPFactory INSTANCE = new FTPFactory();
    }



    public FTPClient getFTP(){
        return this.ftp;
    }


    public boolean Excluir(FTPFile file){
        try {
            if(file.isDirectory()){
                System.out.println(file.getLink());
             return    ftp.removeDirectory(file.getLink());
            }else{
                System.out.println(file.getLink());
             return   ftp.deleteFile(file.getLink());
            }
        } catch (IOException e) {
            e.printStackTrace();

        }
        return false;
    }


    public int FTPConecta(String host,int port, String user, String pwd) throws Exception {
        int reply;
        ftp.connect(host,port);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }
        ftp.login(user, pwd);
        reply = ftp.getReplyCode();
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
        ftp.setAutodetectUTF8(true);
        return reply;
    }




    public void disconnect() {
        if (this.ftp.isConnected()) {
            try {
                this.ftp.logout();
                this.ftp.disconnect();
            } catch (IOException f) {

            }
        }
    }

}
