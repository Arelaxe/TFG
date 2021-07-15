import java.util.Random;
import java.util.ArrayList;
 
/**
 * @author Kunuk Nykjaer
 * Improvements: simplifications, comments - Antonio Mora
 */
 
public class Individual implements Comparable<Individual> {
    public static int SIZE;
    public static final int MAX_ITEMS_EQUAL = 10;
    public static final int MAX_WEIGHT = 4200;
    public boolean[] genotype;
    final Random rand = new Random();
    private ArrayList<Item> items = new ArrayList();
 
    // Creates an individual as an array of 'size' booleans
    public Individual(int size) {
        genotype = new boolean[size];
        SIZE = size;
        
        items.add(new Item(150,20));
        items.add(new Item(325, 40));
        items.add(new Item(600, 50));
        items.add(new Item(805, 36));
        items.add(new Item(430, 25));
        items.add(new Item(1200, 64));
        items.add(new Item(770, 54));
        items.add(new Item(60,18));
        items.add(new Item(930,46));
        items.add(new Item(353, 28));
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
        int value = 0;
        int weight = 0;
        int fitness;
        for (int i = 0; i < genotype.length; i++) {
            if (genotype[i]){
                Item item = items.get(i/10);
                value += item.value;
                weight += item.weight;
            }
        }
        if(weight > MAX_WEIGHT){
            fitness = -weight;
        }
        else{
            fitness = value;
        }
        return fitness;
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