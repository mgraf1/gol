package ParallelGameVisual;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/* Responsible for the Game of Life data model. */
public class VDataArray {

	private boolean[][] grid;
	private boolean[][] nextGrid;

	public VDataArray(int x, int y) {
		
		// Populate the grid with random data.
		Random r = new Random();
		
		grid = new boolean[y][x];
		nextGrid = new boolean[y][x];
		for (int j = 0; j < y; j++) {
			for (int i = 0; i < x; i++) {
				grid[j][i]=r.nextBoolean();
			}
		}
	}

	// Perform a single time step of the simulation.
	public void step(int sx, int ex) {
		for (int j = 0; j < grid.length; j++) {
			for (int i = sx; i <= ex && i < grid[0].length; i++) {
				int count = this.countNeighbors(i, j);
				
				// Determine if cell is alive or dead and act accordingly.
				if (grid[j][i]) {
					if (count < 2 || count > 3)
						nextGrid[j][i] = false;
					else
						nextGrid[j][i] = true;
				} else { //current cell is dead
					if (count == 3)
						nextGrid[j][i] = true;
					else
						nextGrid[j][i] = false;
				}
			}
		}
	}

	// Count a cell's living neighbors.
	public int countNeighbors(int x, int y) {
		int count =0;

		for (int j = (y - 1); j <= (y + 1); j++) {
			for (int i = (x - 1); i <= (x + 1); i++) {
				if (j >= 0 && j < grid.length && i >= 0 
						&& i < grid[0].length && (!(i == x && j == y))) {
					if (grid[j][i])
						count++;
				} 					
			}
		}
		return count;
	}

	// Run the simulation for the specified number of time steps.
	public void run(int steps,GOLPannel gp) {
		
		// Determine the level of concurrency to use.
		int cores = Runtime.getRuntime().availableProcessors();
		ExecutorService es = Executors.newFixedThreadPool(cores);
		Future[] currTasks = new Future[cores];
		VWork p;
		int i = 0;
		boolean[][] temp;
		
		// Create a task for each segment of the grid.
		while(i < steps) {
			for (int j = 0; j < cores; j++) {
				p = new VWork(j * (grid[0].length / cores),
						(j + 1) * (grid[0].length / cores),
						this);
				currTasks[j] = es.submit(p);
			}
			
			// Wait for each task to finish.
			for (int j = 0; j < cores; j++) {
				try {
					currTasks[j].get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
			
			// Swap the grids.
			temp = grid;
			grid = nextGrid;
			nextGrid = temp;
			gp.repaint();
			i++;
		}
		
		// Clean up Executor.
		es.shutdown();
		try {
			es.awaitTermination(Integer.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	public boolean[][] getGrid() {
		return grid;
	}

}