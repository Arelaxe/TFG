import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import struct.FrameData;
/**
 * @author Kunuk Nykjaer
 * Improvements: simplifications, comments - Antonio Mora
 * 
* Simple Genetic Algorithm example - OneMax Problem.
* 
* Selection -> 4-Tournament
* Crosssover -> Uniform (2 parents => 2 childs)
* Mutation -> Bitflip
* Replacement: Steady State (Elitism)
* 
*/
 
public class GA {
    
    static int NUM_INDIVIDUALS = 1;
    static final int INITIAL_NUM_INDIVIDUALS = 1;
    static final int PARENT_USE_PERCENT = 10;  // To define the Steady-State approach
    static final double CROSSOVER_PROBABILITY = 1.00;  // For Steady-State is always performed
    static final double MUTATION_PROBABILITY = 0.5;
    // Random numbers generator
    final Random rand = new Random();
 
    // Population: List of Individuals
    LinkedList<Individual> population = new LinkedList<Individual>();
 
    // Constructor: It creates the initial population
    public GA(IA ia) {
    	Individual c = new Individual(ia);
    	c.random();
    	population.add(c);
    	print();
    }
 
    // print: shows the whole population in the screen
    void print() {
        System.out.println("-- Población de momento");
        for (Individual c : population) {
            System.out.println(c.genotype[0] + " " +  c.genotype[1] + " " + c.genotype[2] + " " + c.genotype[3] + " " + c.fitness);
        }
    }
 
    /**
     * This method generates the new population applying the genetic operators:
     *   - Selection strategy: 4-Tournament -> 2 winners are parents
     *   - Crossover method: Uniform crossover -> 2 children are generated
     *   - Replacement strategy: Steady-State (10%) + Elitism
     */
    void produceNextGen(IA ia) {
        LinkedList<Individual> newpopulation = new LinkedList<Individual>();
 
        if (population.size() < 2) {
        	Individual c = new Individual(ia);
        	c.random();
        	population.add(c);
        }
        else {
        	// *******************************
            // ********** SELECTION **********
            // *******************************
        		
        		int size = population.size();
                int i = rand.nextInt(size);
                int j;
                Individual child;
                Individual w1, w2;
                
                j = i;
                
                while (j == i)
                    j = rand.nextInt(size);
                
                w1 = population.get(i);
                w2 = population.get(j);
                
                if (size > 3) {
                	int k, l;
                	
                	k = l = i;
                	
                	while (k == i || k == j)
                        k = rand.nextInt(size);
                    while (l == i || l == j || k == l)
                        l = rand.nextInt(size);
                    
                 // Get the selected individuals (chromosomes)
                    Individual c1 = population.get(i);
                    Individual c2 = population.get(j);
                    Individual c3 = population.get(k);
                    Individual c4 = population.get(l);
         
                    // Obtain their fitness
                    int f1 = c1.fitness;
                    int f2 = c2.fitness;
                    int f3 = c3.fitness;
                    int f4 = c4.fitness;
         
                    if (f1 >= f2)
                        w1 = c1;
                    else
                        w1 = c2;
         
                    if (f3 >= f4)
                        w2 = c3;
                    else
                        w2 = c4;
                    
                }
                
                // *******************************
                // ********** CROSSOVER **********
                // *******************************
     
                
                // The Crossover is applied depending on a probability
                if (rand.nextFloat() <= CROSSOVER_PROBABILITY){
                	int pivot = rand.nextInt(Individual.SIZE-2) + 1;
                	child = newChild(w1, w2, pivot, ia);
                }
                else {
                    child = new Individual(ia);
                    child.random();
                }
                
                // ******************************
                // ********** MUTATION **********
                // ******************************

                // The individuals are mutated depending on the mutation probability
                if (rand.nextFloat() <= MUTATION_PROBABILITY)
                    mutate(child);
                
                population.add(child);
        	}
        print();
    }
 
    
    // one-point crossover random pivot
    Individual newChild(Individual c1, Individual c2, int pivot, IA ia) {
        Individual child = new Individual(ia);
 
        for (int i = 0; i < pivot; i++) {
            child.genotype[i] = c1.genotype[i];
        }
        for (int j = pivot; j < Individual.SIZE; j++) {
            child.genotype[j] = c2.genotype[j];
        }
 
        return child;
    }
 
    
    // Uniform crossover: The genes are copied from parents to childs randomly 
    //                    (following a uniform distribution)
    Individual[] newChilds(Individual c1, Individual c2, IA ia) {
        Individual child1 = new Individual(ia);
        Individual child2 = new Individual(ia);
 
        // Each gene is taken from one of the parents randomly
        for (int i = 0; i < Individual.SIZE; i++) {
            if (rand.nextFloat() >= 0.5) {
                child1.genotype[i] = c1.genotype[i];
                child2.genotype[i] = c2.genotype[i];
            } else {
                child1.genotype[i] = c2.genotype[i];
                child2.genotype[i] = c1.genotype[i];
            }
        }
 
        return new Individual[] { child1, child2 };
    }
 
    
    // Flip mutation: a random gene is selected and turned from '0' to '1' or viceversa
    void mutate(Individual c) {
        int i = rand.nextInt(Individual.SIZE);
        c.genotype[i] = rand.nextInt(500); // flip
    }
 
    /*
    // PROCESS OF THE GENETIC ALGORITHM
    void run() {
      //  int count = 0;
 
      //  while (count < NUM_GENERATIONS) {
            produceNextGen();
          //  count++;
       // }
 
        System.out.println("\nResult");
        print();
    }*/
    
    
    // ################################################################
    // ##########    Genetic Algorithm for ONEMAX PROBLEM    ##########
    // ################################################################
    /*
    public static void main(String[] args) {
        
        // InitIndividuall time
        long BEGIN = System.currentTimeMillis();
 
        // RUN the Genetic Algorithm
        GA ga = new GA();
        ga.run();
 
        // Final time
        long END = System.currentTimeMillis();
        System.out.println("Time: " + (END - BEGIN) / 1000.0 + " sec.");
    }*/
 
}