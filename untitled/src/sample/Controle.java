/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sample;

import java.io.*;
import java.net.SocketException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

/**
 *
 * @author Higor
 */
public class Controle {

    private final FTPClient conexaoFTP = new FTPClient();
    private final BufferedReader ler = new BufferedReader(new InputStreamReader(System.in));
    Config config = new Config();

    public Controle(String urlServidorFTP, String usuario, String senha) throws IOException, SocketException {
        try {
            //INICIANDO CONEXÃO
            System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println("Conectando a URL Informada...");
            conexaoFTP.connect(urlServidorFTP);
            System.out.println("Conectado!");
            System.out.println("Verificando seu usuário e senha...");
            conexaoFTP.login(usuario, senha);
            System.out.println("Autenticado!");
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++\n");
//            conexaoFTP.enterLocalPassiveMode();

            //INICIA O CONSOLE
            console();

        } catch (SocketException ex) {
            System.err.println(ex);
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private void console() throws IOException {
        System.out.println("CONSOLE");
        while (true) {
            System.out.print(conexaoFTP.printWorkingDirectory() + ">");
            String comando[] = ler.readLine().split(" ");
            identificarComando(comando);
        }
    }

    private void listarArquivos() throws IOException {
        String[] arquivos = conexaoFTP.listNames();
        System.out.println("---------------------------------");
        System.out.println("Arquivos: \n");
        for (String f : arquivos) {
            System.out.println(f);
        }
        System.out.println("---------------------------------");
    }

    private void mudarDiretorio(String diretorio) throws IOException {
        conexaoFTP.changeWorkingDirectory(diretorio);
    }

    private void criarDiretorio(String novoDiretorio) throws IOException {
        conexaoFTP.makeDirectory(novoDiretorio);
        System.out.println("Diretório criado com sucesso!");
        conexaoFTP.changeWorkingDirectory(novoDiretorio);
    }

    private void criarArquivo(String novoArquivo) throws IOException {
    OutputStream out =  conexaoFTP.appendFileStream(novoArquivo);
        if(!FTPReply.isPositiveIntermediate(ftp.getReplyCode())) {
            out .close();
            cone.logout();
            ftp.disconnect();
            System.err.println("File transfer failed.");
            System.exit(1);
        }

        System.out.println("Arquivo criado!");
    }

    private void renomear(String nomeAntigo, String nomeNovo) throws IOException {
        conexaoFTP.rename(nomeAntigo, nomeNovo);
        System.out.println("Renomeado com sucesso!");
    }

    private void deletar(String arquivo) throws IOException {
        boolean resultado;
        if (arquivo.contains(".")) {
            resultado = conexaoFTP.deleteFile(arquivo);
            if (resultado) {
                System.out.println("Arquivo removido!");
            } else {
                System.out.println("Ocorreu um problema, tente novamente!");
            }
        } else {
            resultado = conexaoFTP.removeDirectory(arquivo);
            if (resultado) {
                System.out.println("Diretório removido");
            } else {
                System.out.println("Antes de remover um diretório, remova tudo dentro dele!");
            }
        }
    }

    private void upload() throws FileNotFoundException, IOException {
        if (podeCriarArquivo()) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = fc.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File arquivo = fc.getSelectedFile();
                InputStream isArquivo = new FileInputStream(arquivo.getAbsolutePath());
                System.out.println("Enviando Arquivo "
                        + arquivo.getAbsolutePath() + " para "
                        + conexaoFTP.printWorkingDirectory());
                conexaoFTP.storeFile(arquivo.getName(), isArquivo);
                System.out.println("Arquivo Enviado!");
            } else {
                JOptionPane.showMessageDialog(null, "Arquivo não selecionado!");
            }
        } else {
            System.err.println("Não é possivel criar mais arquivos!");
        }

    }

    private void download(String arquivo) throws FileNotFoundException, IOException {
        System.out.println("\n---------- Iniciando Download ----------");
        System.out.println("Digite o local onde será baixado o arquivo: ");
        String local = ler.readLine();

        FileOutputStream fos = new FileOutputStream(local + "\\" + arquivo);

        if (conexaoFTP.retrieveFile(arquivo, fos)) {
            System.out.println("Download efetuado com sucesso!");
        } else {
            System.out.println("Erro ao efetuar download do arquivo.");
        }

    }

    private void listarComandos() {
        System.out.println("Comandos disponíveis:");
        System.out.println("    ls - Lista arquivos no diretório atual");
        System.out.println("    cd 'nomediretorio' - Altera o local para o especificado");
        System.out.println("    mk 'nome' - Cria um novo diretório ou arquivo");
        System.out.println("    ren 'nomeatual' 'novonome' - Renomeia o arquivo ou diretório");
        System.out.println("    delete 'arquivoadeletar - Deleta o arquivo ou diretório");
        System.out.println("    download 'nomearquivo' - Faz o download do arquivo para um local especificado");
        System.out.println("    upload - Abre uma janela de escolha, onde deve ser selecionado o arquivo a ser upado");
        System.out.println("    exit - Finaliza o programa");
    }

    private boolean podeCriarArquivo() throws IOException {
        int num_diretorios = conexaoFTP.listDirectories().length;
        int num_arquivos = conexaoFTP.listFiles().length - num_diretorios;
        return num_arquivos < config.getMaxArquivos();
    }

    private boolean podeCriarDiretorio() throws IOException {
        int num_diretorios = conexaoFTP.listDirectories().length;
        return num_diretorios < config.getMaxPastas();
    }

    private int contadorDeNivel() throws IOException {
        int cont = conexaoFTP.printWorkingDirectory().split("/").length;
        return cont;
    }

    private void sair() throws IOException {
        System.out.println(".");
        System.out.println("..");
        System.out.println("...");
        System.out.println("Desenvolvido por Higor Chaves | Ramon Santos");
        System.out.println("Orientação do profº Ekler P. Mattos");
        System.out.println("Redes de Computadores I - 5º Semestre");
        System.out.println("UFMS - Câmpus Coxim");
        System.out.println("Julho - 2017");
        System.out.println("...");
        System.out.println("..");
        System.out.println(".");

        System.out.println("Finalizando....");
        conexaoFTP.logout();
        conexaoFTP.disconnect();
    }

    private void identificarComando(String comando[]) throws IOException {
        switch (comando[0]) {
            case "help":
                listarComandos();
                break;
            case "ls":
                listarArquivos();
                contadorDeNivel();
                break;
            case "cd":
                mudarDiretorio(comando[1]);
                break;
            case "mk":
                if (comando[1].contains(".")) {
                    if (podeCriarArquivo()) {
                        criarArquivo(comando[1]);
                    } else {
                        System.err.println("Não é possivel criar mais arquivos!");
                    }
                } else {
                    if (contadorDeNivel() == 3 || !podeCriarDiretorio()) {
                        System.err.println("Não é possivel criar mais pastas!");
                    } else {
                        criarDiretorio(comando[1]);
                    }
                }
                break;
            case "ren":
                renomear(comando[1], comando[2]);
                break;
            case "delete":
                deletar(comando[1]);
                break;
            case "upload":
                upload();
                break;
            case "download":
                download(comando[1]);
                break;
            case "exit":
                sair();
                break;
            default:
                System.out.println("Comando desconhecido ou errado. Digite 'help' para consultar os comandos disponíveis.");
                break;
        }
    }

}
