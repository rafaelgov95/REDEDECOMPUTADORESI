/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EC1;

/**
 *
 * @author Higor
 */
public class Config {

    private int maxPastas;
    private int maxArquivos;
    private int maxNiveis;

    public Config(int pastas, int arquivos, int niveis) {
        maxPastas = pastas;
        maxArquivos = arquivos;
        maxNiveis = niveis;
    }

    /**
     * @return the maxPastas
     */
    public int getMaxPastas() {
        return maxPastas;
    }

    /**
     * @param maxPastas the maxPastas to set
     */
    public void setMaxPastas(int maxPastas) {
        this.maxPastas = maxPastas;
    }

    /**
     * @return the maxArquivos
     */
    public int getMaxArquivos() {
        return maxArquivos;
    }

    /**
     * @param maxArquivos the maxArquivos to set
     */
    public void setMaxArquivos(int maxArquivos) {
        this.maxArquivos = maxArquivos;
    }

    /**
     * @return the maxNiveis
     */
    public int getMaxNiveis() {
        return maxNiveis;
    }

    /**
     * @param maxNiveis the maxNiveis to set
     */
    public void setMaxNiveis(int maxNiveis) {
        this.maxNiveis = maxNiveis;
    }

}
