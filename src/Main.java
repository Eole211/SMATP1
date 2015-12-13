
import java.util.Timer;

/**
 * Created by Léo on 02-11-15.
 */
public class Main {

    public static void main(String[] args) {
        Grid g = Grid.getInstance();
        try {
            int nbAgents = 22;
            String letters="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            for(int i = 0; i < nbAgents; i++) {           
                g.addAgent(String.valueOf(letters.charAt(i)));
            }

            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
            mainWindow.drawGrid(g);

            LetterBox.getInstance().init(g.m_lAg);
            g.startAgents();

        } catch (Exception e) {

        }

    }
}
