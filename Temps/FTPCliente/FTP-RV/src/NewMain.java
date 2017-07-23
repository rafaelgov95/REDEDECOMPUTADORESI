
import javafx.stage.Stage;
import org.eclipse.fx.ui.controls.sample.DirectoryViewSample;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rafael
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Stage dashboardStage = new Stage();
        DirectoryViewSample file = new DirectoryViewSample();
        file.start(dashboardStage);
    }

}
