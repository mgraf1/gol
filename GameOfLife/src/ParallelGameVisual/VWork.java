package ParallelGameVisual;

/* Encapsulates the tasks for the Executor to run. */
public class VWork implements Runnable {
    public int sx,ex;
    public VDataArray pArr;
    
    // Each segment of work takes a horizontal partition of the grid.
    public VWork(int sx, int ex, VDataArray arr) {
        this.sx = sx;
        this.ex = ex;
        this.pArr = arr;
    }
    
    public void run() {
        pArr.step(sx, ex);
    }
}
