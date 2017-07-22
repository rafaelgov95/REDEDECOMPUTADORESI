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
    
    private FTPFactory() {
        this.ftp = new FTPClient();
    }

    public static FTPFactory getInstance() {
        
        return FTPFactoryHolder.INSTANCE;
    }
//    public FTP(){}
    /**
     * Classe privada que armazena a única instância de FTPFactory.
     */
    private static class FTPFactoryHolder {

        private static final FTPFactory INSTANCE = new FTPFactory();
    }
    public FTPClient getFTP(){
        return this.ftp;
    }
    public  FTPFile[] FTPDirectory() throws IOException{
        
       FTPListParseEngine engine = ftp.initiateListParsing();
       FTPFile[] files = engine.getFiles();
       return files;
    
    }
    public int FTPConecta(String host, String user, String pwd) throws Exception {
//        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
        int reply;
        ftp.connect(host);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("Exception in connecting to FTP Server");
        }
        ftp.login(user, pwd);
        reply = ftp.getReplyCode();
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
        return reply;
    }

    public void uploadFile(String localFileFullName, String fileName, String hostDir)
            throws Exception {
        try (InputStream input = new FileInputStream(new File(localFileFullName))) {
            this.ftp.storeFile(hostDir + fileName, input);
        }
    }

    public void disconnect() {
        if (this.ftp.isConnected()) {
            try {
                this.ftp.logout();
                this.ftp.disconnect();
            } catch (IOException f) {
                // do nothing as file is already saved to server
            }
        }
    }

}
