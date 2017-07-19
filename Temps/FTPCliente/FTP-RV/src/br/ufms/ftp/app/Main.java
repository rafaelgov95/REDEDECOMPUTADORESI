/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufms.ftp.app;

import java.io.IOException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author rafael
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        FTPClient ftp = new FTPClient();
        FTPClientConfig config = new FTPClientConfig();
        ftp.configure(config);
        boolean erro = false;
        int reply;

        String server = "localhost";
        ftp.connect(server);
        System.out.println("Conectado a" + server + ".");
        System.out.print(ftp.getReplyString());
        reply = ftp.getReplyCode();
        ftp.login("rafael", "Iloveprog");
        reply = ftp.getReplyCode();
        FTPListParseEngine engine = ftp.initiateListParsing(".");

        while (engine.hasNext()) {
            FTPFile[] files = engine.getNext(25);
            for (FTPFile fTPFile : files) {
                System.out.println(fTPFile.getName());
            }
            System.out.println(files.length);
        }
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            System.err.println("FTP server refused connection.");
            System.exit(1);
        }
    }

}
