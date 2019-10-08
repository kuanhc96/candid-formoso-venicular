import java.util.*;

public class CAMain { // Used for debugging
   
   public static void main(String[] args) {
//      CellularAutomata CA = new CellularAutomata(50);
      PredatorPreyCA PPCA = new PredatorPreyCA(50);
      //int[][] grid = CA.getGrid();
      //PPCA.graphGrid();
      
      
      animal[][] grid = PPCA.getGrid();
      PPCA.graphGrid();
      for (int i = 0; i < 5000; i++) {
         PPCA.graphGrid();
         PPCA.nextGeneration();
         try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
      }
      
      
      /*
      for (int i = 0; i < 1000; i++) {
         CA.graphGrid();
         CA.nextGeneration();
         try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
      }
      */      
   }
   
}