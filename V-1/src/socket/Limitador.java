/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

/**
 *
 * @author rafael
 */
public class Limitador {

    private int maxPastas;
    private int maxArquivos;
    private int maxNiveis;

    public Limitador(int pastas, int arquivos, int niveis) {
        maxPastas = pastas;
        maxArquivos = arquivos;
        maxNiveis = niveis;
    }

    public int getMaxPastas() {
        return maxPastas;
    }


    public void setMaxPastas(int maxPastas) {
        this.maxPastas = maxPastas;
    }


    public int getMaxArquivos() {
        return maxArquivos;
    }


    public void setMaxArquivos(int maxArquivos) {
        this.maxArquivos = maxArquivos;
    }


    public int getMaxNiveis() {
        return maxNiveis;
    }


    public void setMaxNiveis(int maxNiveis) {
        this.maxNiveis = maxNiveis;
    }

}
