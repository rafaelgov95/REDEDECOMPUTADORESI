package modelo;

import org.apache.commons.net.ftp.FTPFile;

public class FTPFIle extends FTPFile {

    public FTPFIle(){
        super();

    }

    @Override
    public String toString() {
        return super.getName();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
