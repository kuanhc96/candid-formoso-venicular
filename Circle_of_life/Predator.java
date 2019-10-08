import java.util.*;
import java.awt.*;

public class Predator extends animal {

    private static final int PREDATOR_ID = 2;
    private static final int STARVATION_LIMIT = 15;
    private static final double MUTATION_PROBABILITY = 0.00001;
    // private static final int COMPANY = 4;

    public Predator() {
        super(PREDATOR_ID, STARVATION_LIMIT, MUTATION_PROBABILITY);
    }

    @Override
    public String toString() {
        return "Predator";
    }

    public void eat() {
        resetAge();
    }
}