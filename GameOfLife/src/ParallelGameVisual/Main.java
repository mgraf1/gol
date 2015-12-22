package ParallelGameVisual;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/* Concurrent Game of Life animation using Java Swing and ExecutorService
*
*  Created by: Mike Graf, 2013
*/
public class Main {
    
    public static void main(String[] args) {
        
        int width = 0;
        int height = 0;
        int numSteps = 0;
        
        // Validate input.
        if (args.length != 3) {
            System.out.printf("Correct usage: gol <width> <height> <num_steps>\n");
            System.exit(1);
        }
        
        try {
            width = Integer.parseInt(args[0]);
            height = Integer.parseInt(args[1]);
            numSteps = Integer.parseInt(args[2]);
        } catch(NumberFormatException e) {
            System.out.printf("Arguments must be numeric.\n");
            System.out.printf("Correct usage: gol <width> <height> <num_steps>\n");
            System.exit(1);
        }
        
        if (width < 0 || height < 0 || numSteps < 0) {
            System.out.printf("Arguments must be positive.\n");
            System.exit(1);
        }
        
        // Launch the GUI.
        createAndShowGUI(width, height, numSteps);
    }
    
    private static void createAndShowGUI(int width, int height, int numSteps) {
        
        // Create JFrame window.
        JFrame f = new JFrame("Game of Life");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(width, height);
        
        // Create JPanel for animation.
        GOLPannel gp = new GOLPannel(width, height);
        f.add(gp);
        f.setVisible(true);
        
        // Run the simulation.
        gp.runSim(numSteps);
    }
}
