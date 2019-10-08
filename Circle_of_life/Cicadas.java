import java.util.*;
import java.awt.*;

public class Cicadas extends animal {

    private static final int CICADAS_ID = 1;
    private static final int INCUBATION_PERIOD = 13;
    private static final double MUTATION_PROBABILITY = 0.00001;
    //private static final int COMPANY = 4;

    public Cicadas() {
        super(CICADAS_ID, INCUBATION_PERIOD, MUTATION_PROBABILITY);
    }

    @Override
    public String toString() {
        return "Cicadas";
    }

    public boolean isEmerged() {
        return getAge() > getLifeSpan();
    }




}