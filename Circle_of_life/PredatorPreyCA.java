import java.util.*;
import java.awt.*;


public class PredatorPreyCA {

    private animal[][] grid;
    private animal[][] nextGrid;
    private int size;
    private Random rand = new Random();
    private DrawingPanel panel;
    private Graphics g;
    private int cellLength;


    private static final int DEFAULT_GRID_DIMENSIONS = 10;
    private static final int PANEL_SIZE = 500;
    private static final int INITIAL_EMPTY_PERCENTAGE = 50;
    private static final int INITIAL_PREY_PERCENTAGE = 45;
    private static final int INITIAL_PREDATOR_PERCENTAGE =
            100 - INITIAL_EMPTY_PERCENTAGE - INITIAL_PREY_PERCENTAGE;
    private static final int K_PREY = 2; // parameter defined by model
    private static final int K_PREDATOR = 2; // parameter defined by model

    public PredatorPreyCA() {
        this(DEFAULT_GRID_DIMENSIONS);
    }


    public PredatorPreyCA(int size) {
        grid = new animal[size][size];
        nextGrid = new animal[size][size];
        this.size = size;
        cellLength = PANEL_SIZE / size;
        initializeGrid();
        panel = new DrawingPanel(PANEL_SIZE, PANEL_SIZE);
        g = panel.getGraphics();
    }

    private void initializeGrid() {
        initializeEdges();
        for (int j = 1; j < size - 1; j++) {
            for (int k = 1; k < size - 1; k++) {
                int temp = rand.nextInt(101);
                if (temp < INITIAL_EMPTY_PERCENTAGE) {
                    grid[j][k] = new Empty();
                } else if (temp < INITIAL_EMPTY_PERCENTAGE + INITIAL_PREY_PERCENTAGE) {
                    grid[j][k] = new Cicadas();
                } else {
                    grid[j][k] = new Predator();
                }
            }
        }
    }

    private void initializeEdges() {
        for (int j = 0; j < size; j++) {
            grid[j][0] = new Empty();
            grid[j][size - 1] = new Empty();
        }
        for (int i = 0; i < size; i++) {
            grid[0][i] = new Empty();
            grid[size - 1][i] = new Empty();
        }
    }

    public void graphGrid() {
        int cicadasCounter = 0;
        int predatorCounter = 0;
        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
                g.setColor(Color.WHITE);
                g.fillRect(i * cellLength, j * cellLength, cellLength, cellLength);
                if (grid[j][i].getID() == 0) { // white spots are dead
                    g.setColor(Color.BLACK);
                    g.drawRect(i * cellLength, j * cellLength, cellLength, cellLength);
                } else if (grid[j][i].getID() == 1 && grid[j][i].getAge() <= grid[j][i].getLifeSpan()) {
                    cicadasCounter++; // black spots are incubating prey
                    g.setColor(Color.BLACK);
                    g.fillRect(i * cellLength, j * cellLength, cellLength, cellLength);
                } else if (grid[j][i].getID() == 1 && grid[j][i].getAge() > grid[j][i].getLifeSpan()) {
                    cicadasCounter++;// blue spots are emerged prey
                    g.setColor(Color.BLUE);
                    g.fillRect(i * cellLength, j * cellLength, cellLength, cellLength);
                } else {
                    predatorCounter++;
                    g.setColor(Color.RED); // red spots are predators
                    g.fillRect(i * cellLength, j * cellLength, cellLength, cellLength);
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
                grid[j][i].mutate();
                grid[j][i].age();
                rules(j, i);
            }
        }

        grid = nextGrid;
        initializeEdges();
        nextGrid = new animal[size][size];
    }

    private void rules(int j, int i) {
        if (grid[j][i].getID() == 0) { // check what would happen to the empty slot
            int predatorCounter = 0;
            int emergedCicadasCounter = 0;
            predatorCounter = calculatePredators(j, i, predatorCounter); // examine Moore Neighborhood
            emergedCicadasCounter = calculateEmergedCicadas(j, i, emergedCicadasCounter);

            // empty rules are as follows:
            if (emergedCicadasCounter >= K_PREY) {
                nextGrid[j][i] = grid[j][i].birtheAndInherit(1); // empty cell occupied by new cicadas that inherits properties of parent
                // after birth, Cicadas is marked for death
                outerloop:
                for (int y = -1; y <= 1; y++) {
                      //boolean calledBreak = false;
                      int parentCounter = 0;
                      for (int x = -1; x <= 1; x++) {
                        if (x != 0 || y != 0) {
                            if (grid[j + y][i + x].getID() == 1) {
                                grid[j + y][i + x].giveBirth();
                                parentCounter++;
                                if (parentCounter == 2) {
                                   //calledBreak = true;
                                   break outerloop; // break since only one of the predators will eat the prey.
                                }
                              
                            }
                         }
                      }
                      //if (calledBreak) {
                        //  break;
                      //}
                  }
                
            } //else if (predatorCounter >= K_PREDATOR) {
               //nextGrid[j][i] = grid[j][i].birtheAndInherit(2);
             else {
                nextGrid[j][i] = grid[j][i];
            }
        } else if (grid[j][i].getID() == 1) { // check what would happen to the cicadas in its next iteration
            if (grid[j][i].getAge() > grid[j][i].getLifeSpan()) { // cicadas is emerged and is now in a vulnerable state
                if (grid[j][i].gaveBirth()) {
                    nextGrid[j][i] = new Empty();
                } else {
                  int predatorCounter = 0;
                  int emergedCicadasCounter = 0;
                  predatorCounter = calculatePredators(j, i, predatorCounter); // examine Moore Neighborhood
                  emergedCicadasCounter = calculateEmergedCicadas(j, i, emergedCicadasCounter);
                  // cicadas survival rules are as follows:
                  if (predatorCounter >= K_PREDATOR) { // there are enough predators to replace the cicadas
                      nextGrid[j][i] = grid[j][i].birtheAndInherit(2); // cicadas turns into predator that inherits properties of parent
                  } else if (predatorCounter > 0 && predatorCounter < K_PREY) { // enough predators to eat the cicadas
                      nextGrid[j][i] = new Empty(); // cicadas turns into empty
                      // the predator has now eaten
                      outerloop:
                      for (int y = -1; y <= 1; y++) {
                          //boolean calledBreak = false;
                          for (int x = -1; x <= 1; x++) {
                             if (x != 0 || y != 0) { // do not count self
                                 if (grid[j + y][i + x].getID() == 2) {
                                     grid[j + y][i + x].resetAge();
                                     //calledBreak = true;
                                     break outerloop; // break since only one of the predators will eat the prey.
                                 }
                             }
                          }
                          //if (calledBreak) {
                          //    break;
                          //}
                      }
                  } else if (emergedCicadasCounter >= 4) {
                     nextGrid[j][i] = new Empty();
                  } else { // cicadas retains original state
                      nextGrid[j][i] = grid[j][i];
                  }
              }
            } else {
                // else case is when the cicadas is still incubating, in which state it is invincible.
                // Its state will not be changed.
                nextGrid[j][i] = grid[j][i];
            }
        } else { // check what would happen to the predator in its next iteration
            // predator survival rules
            if (grid[j][i].getAge() == grid[j][i].getLifeSpan()) { // predator dies of starvation
                nextGrid[j][i] = new Empty();
            } else {
                nextGrid[j][i] = grid[j][i];
            }
        }
    }

    private int calculatePredators(int j, int i, int predatorCounter) {
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (x != 0 || y != 0) { // Do not count self
                    if (grid[j + y][i + x].getID() == 2) {
                        predatorCounter++;
                    } 
                }
            }
        }
        return predatorCounter;
    }
    
    private int calculateEmergedCicadas(int j, int i, int emergedCicadasCounter) {
        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                if (x != 0 || y != 0) { // Do not count self
                    if (grid[j + y][i + x].getID() == 1 && 
                        grid[j + y][i + x].getAge() > grid[j + y][i + x].getLifeSpan()) {
                        emergedCicadasCounter++;
                    } 
                }
            }
        }
        return emergedCicadasCounter;
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


    public animal[][] getGrid() {
        return grid;
    }

    public int getGridDimensions() {
        return size;
    }

}