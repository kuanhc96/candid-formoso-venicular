import java.util.*;

public class animal {

    private int ID;
    private int lifeSpan;
    private double mutationProbability; // whether mutation occurred should be tested at every iteration
    private int age; // changes at every iteration
    //private int company;
    private boolean gaveBirth;
    private Random rand = new Random();

    public animal(int ID, int lifeSpan, double mutationProbability) {
        this.ID = ID;
        this.lifeSpan = lifeSpan;
        this.mutationProbability = mutationProbability;
        // this.company = company; // k_prey or k_predator
        this.age = age;
        this.gaveBirth = false;
    }

    public void age() {
        age++;
    }

    public void mutate() { // if mutation succeeds, lifespan will increase by 1.
        int factor = rand.nextInt((int)(1/mutationProbability + 1));
        lifeSpan += factor*mutationProbability; // if succeeds in mutation, factor*mutationProbability = 1; otherwise = 0.
    }

    public int getID() {
        return ID;
    }

    public int getLifeSpan() {
        return lifeSpan;
    }

    public double getMutationProbability() {
        return mutationProbability;
    }
    
    

    public int getAge() {
        return age;
    }

    public void resetAge() { age = 0; }
    
    public boolean gaveBirth() {
      return gaveBirth;
    }
    
    public void giveBirth() {
        gaveBirth = true;
    }

    public animal birtheAndInherit(int ID) { // does not inherit lifespan yet!!!!
        if (ID == 1) {
            
            return new Cicadas();
        } else if (ID == 2) {
            return new Predator();
        } else {
            return new Empty();
        }
    }

}