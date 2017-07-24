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

    private int p;
    private int a;

    public Limitador(int p, int a) {
        this.p = p;
        this.a = a;
    }

    public int getMP() {
        return p;
    }

    public int getMA() {
        return a;
    }




}
