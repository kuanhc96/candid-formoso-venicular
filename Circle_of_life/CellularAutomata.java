import java.util.*;
import java.awt.*;


public class CellularAutomata {
   
   private int[][] grid;
   private int[][] nextGrid;
   private int size;
   private Random rand = new Random();
   private DrawingPanel panel;
   private Graphics g;
   private int cellLength;
   
   private static final int DEFAULT_GRID_DIMENSIONS = 10;
   private static final int PANEL_SIZE = 500;
  
   public CellularAutomata() {
      this(DEFAULT_GRID_DIMENSIONS);
   }
   
   
   
   public CellularAutomata(int size) {
      grid = new int[size][size];
      nextGrid = new int[size][size];
      this.size = size;
      cellLength = PANEL_SIZE/size;
      initializeGrid();
      panel = new DrawingPanel(PANEL_SIZE, PANEL_SIZE);
      g = panel.getGraphics();
   }
   
   private void initializeGrid() {
      
      for (int j = 0; j < size; j++) {
         
         
         for (int k = 0; k < size; k++) {
            int temp = rand.nextInt(101);
            if (temp < 50) {
               grid[j][k] = 1;
            } else {
               grid[j][k] = 0;
            }
            
         }
      }
      
   }

   public void graphGrid() {
      int counter = 0;
      for (int j = 0; j < size; j++) {
         for (int i = 0; i < size; i++) {
            g.setColor(Color.WHITE);
            g.fillRect(i*cellLength,j*cellLength,cellLength,cellLength);
            if (grid[j][i] == 0) { // white spots are dead
               g.setColor(Color.BLACK);
               g.drawRect(i*cellLength,j*cellLength,cellLength,cellLength);
            } else {
               counter++;
               g.setColor(Color.BLACK); // black spots are alive
               g.fillRect(i*cellLength,j*cellLength,cellLength,cellLength);
            }
            
         }
      }
      
      
      /*
      g.setFont(new Font("SansSerif", Font.PLAIN, 10));
      String caption = "# of animals alive = " + counter;
      g.drawString(caption,PANEL_SIZE,0);
      */
   }
   
   public void nextGeneration() {
      for (int j = 1; j < size - 1; j++) {
         for (int i = 1; i < size - 1; i++) {
            rules(j, i);             
         }
      }
      
      grid = nextGrid;
      nextGrid = new int[size][size];
   }
   
   private void rules(int j, int i) {
      int neighborhoodSum = 0;
      for (int y = -1; y <= 1; y++) {
         for (int x = -1; x <= 1; x++) {
            neighborhoodSum += grid[j+y][i+x];
         }
      }
      neighborhoodSum -= grid[j][i];
      
      if (grid[j][i] == 1 && neighborhoodSum < 2) {
         nextGrid[j][i] = 0;
      } else if (grid[j][i] == 1 && neighborhoodSum > 3){
         nextGrid[j][i] = 0;
      } else if (grid[j][i] == 0 && neighborhoodSum == 3) {
         nextGrid[j][i] = 1;
      } else {
         nextGrid[j][i] = grid[j][i];
      }
      /*
      for (int a = 0; a < size; a++) {
         nextGrid[0][a] = grid[0][a];
         nextGrid[size - 1][a] = grid[size - 1][a];
      }
      
      for (int b = 0; b < size; b++) {
         nextGrid[b][0] = grid[b][0];
         nextGrid[b][size - 1] = grid[b][size - 1];
      }
      */
      
   }
   
   public int[][] getGrid() {
      return grid;
   }
   
   public int getGridDimensions() {
      return size;
   }
   
}