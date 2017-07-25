/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;

/**
 *
 * @author Higor
 */
public class ClienteFTP {

    public static void main(String[] args) throws IOException, SocketException {
        BufferedReader ler = new BufferedReader(new InputStreamReader(System.in));
        
        //Para tornar mais rápido o acesso, você pode pré-configurá-lo
        String urlServidorFTP = "127.0.0.1";
        String usuario = "teste";
        String senha = "Iloveprog";

        System.out.println("Deseja inserir os dados ou usar os pré-configurados?");
        System.out.println("1 - Pré-configurdos");
        System.out.println("2 - Inserir dados");

        if ("2".equals(ler.readLine())) {
            System.out.println("Insire a URL do servidor FTP: ");
            urlServidorFTP = ler.readLine();
            System.out.println("Insira seu usuário: ");
            usuario = ler.readLine();
            System.out.println("Insira sua senha: ");
            senha = ler.readLine();
        }

        new Controle(urlServidorFTP, usuario, senha);
    }
}
