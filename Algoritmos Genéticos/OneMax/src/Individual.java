import java.util.Random;
 
/**
 * @author Kunuk Nykjaer
 * Improvements: simplifications, comments - Antonio Mora
 */
 
public class Individual implements Comparable<Individual> {
    public static int SIZE;
    public boolean[] genotype;
    final Random rand = new Random();
 
    // Creates an individual as an array of 'size' booleans
    public Individual(int size) {
        genotype = new boolean[size];
        SIZE = size;
    }
 
    // Generates random values for each gene
    void random() {
        for (int i = 0; i < genotype.length; i++) {
            genotype[i] = 0.5 > rand.nextFloat();
        }
    }
 
    // Transforms 'true' into '1' and 'false' into '0' 
    private String gene() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < genotype.length; i++) {
            sb.append(genotype[i] == true ? 1 : 0);
        }
        return sb.toString();
    }
 
    // Computes the fitness value: OneMax -> Numer of 'true' ('1')
    int fitness() {
        int sum = 0;
        for (int i = 0; i < genotype.length; i++) {
            if (genotype[i])
                sum++;
        }
        return sum;
    }
    
    // Flip a gene
    void flip(int index){
        genotype[index] = !genotype[index];
    }
 
    // Comparison method (Overrided): Compares the fitness of the individuals
    @Override
    public int compareTo(Individual o) {
        int f1 = this.fitness();
        int f2 = o.fitness();
 
        if (f1 < f2)
            return 1;
        else if (f1 > f2)
            return -1;
        else
            return 0;
    }
 
    // Shows an individual as a String
    @Override
    public String toString() {
        return "gene=" + gene() + " fit=" + fitness();
    }
}