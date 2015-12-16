package ParallelGameVisual;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/* JPanel that animates the Game of Life. */
class GOLPannel extends JPanel {
    
    private VDataArray d;
    private int width;
    private int height;
    
    public GOLPannel(int w, int h) {
        width = w;
        height = h;
        d = new VDataArray(width, height);
    }
    
    // Launches simulation in a new thread to keep the GUI responsive.
    public void runSim(final int steps) {
        
        final GOLPannel gp = this;
        Thread r = new Thread() {
            public void run()
            {
                d.run(steps, gp);
            }
        };
        
        r.start();
        
        // Wait for animation to finish.
        try {
            r.join();
        } catch (InterruptedException e) {
            System.exit(1);
        }
        System.exit(0);
    }
    
    // Draws living cells as black pixels. Dead cells as white.
    public void drawBoard(Graphics g) {
        
        boolean[][] grid = d.getGrid();
        
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                if (grid[j][i]) {
                    g.setColor(Color.black);
                    g.drawRect(i, j, 1, 1);
                } else {
                    g.setColor(Color.white);
                    g.drawRect(i, j, 1, 1);
                }
            }
        }
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw Board
        drawBoard(g);
    }
    
    public void update(Graphics g) {
        
        // Draw Board
        drawBoard(g);
    }
}
