import java.util.LinkedList; 
import java.util.Random;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
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
    	File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;

		try{
			archivo = new File ("poblacion.txt");
			if (!archivo.exists()) {
				Individual c = new Individual(ia);
		    	c.random();
		    	population.add(c);
			}
			else {
				fr = new FileReader(archivo);
				br = new BufferedReader(fr);
				String linea;
				while((linea=br.readLine())!=null){
					if (!linea.equals("")) {
						String[] datos = linea.split(" ");
						Individual c = new Individual(ia);
						c.genotype[0] = Integer.parseInt(datos[0]);
						c.genotype[1] = Integer.parseInt(datos[1]);
						c.genotype[2] = Integer.parseInt(datos[2]);
						c.genotype[3] = Integer.parseInt(datos[3]);
						c.fitness = Integer.parseInt(datos[4]);
						
						population.add(c);
					}
				}
			}
			
		} catch (Exception ex){
			ex.printStackTrace();
		} finally{
			try {
				if (null != fr){
					fr.close();
					br.close();
				}
			} catch (Exception ex2){
				System.out.println("Fallo de fichero\n");
			}
		}
    	print();
    }
 
    // print: shows the whole population in the screen
    void print() {
        System.out.println("-- Población de momento");
        for (Individual c : population) {
            System.out.println(c.genotype[0] + " " +  c.genotype[1] + " " + c.genotype[2] + " " + c.genotype[3] + " " + c.fitness);
        }
    }
    
    @Override
    public String toString() {
    	String cadena = "";
    	for (Individual c : population) {
            cadena += c.genotype[0] + " " +  c.genotype[1] + " " + c.genotype[2] + " " + c.genotype[3] + " " + c.fitness + "\n";
        }
    	return cadena;
    }
 
    /**
     * This method generates the new population applying the genetic operators:
     *   - Selection strategy: 4-Tournament -> 2 winners are parents
     *   - Crossover method: Uniform crossover -> 2 children are generated
     *   - Replacement strategy: Steady-State (10%) + Elitism
     */
    void produceNextGen(IA ia) {
        if (population.size() < 10) {
        	Individual c = new Individual(ia);
        	c.random();
        	population.add(c);
        }
        else {
        	// *******************************
            // ********** SELECTION **********
            // *******************************
        		
        		int size = population.size();
                
                Individual child;
                Individual w1, w2;
                	int i = rand.nextInt(size);
                    int j, k, l;
                	
                	k = l = j = i;
                	
                	while (j == i)
                        j = rand.nextInt(size);
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
 
}