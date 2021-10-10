import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
 
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
    
    static final int NUM_GENERATIONS = 30;
    static final int SIZE_INDIVIDUAL = 7;
    static final int NUM_INDIVIDUALS = 10;
    static final int NUM_COMBATS_INDIVIDUAL = 2;
    static final int PARENT_USE_PERCENT = 10;  // To define the Steady-State approach
    static final double CROSSOVER_PROBABILITY = 1.00;  // For Steady-State is always performed
    static final double MUTATION_PROBABILITY = 0.5;
    int numGeneration = 0;
    // Declaración de variables para leer/escribir archivos
	FileWriter escribir = null;
	PrintWriter pw = null;

    // Random numbers generator
    final Random rand = new Random();
 
    // Population: List of Individuals
    LinkedList<Individual> population = new LinkedList<Individual>();
 
    // Constructor: It creates the initial population
    public GA() {
    	// ************************************
        // ********** INITIALIZATION **********
        // ************************************
        for (int i = 0; i < NUM_INDIVIDUALS; i++) {
            // Create an individual of the desired size
            Individual c = new Individual(SIZE_INDIVIDUAL);
            // It is initialized randomly
            c.random();
            // And added to the population
            population.add(c);
        }
 
        // Sort it (from highest to lowest fitness value)
        //Collections.sort(population); // sort method
        //System.out.println("Init population sorted");
        print();
    }
 
    // print: shows the whole population in the screen
    void print() {
        System.out.println("-- print");
        for (Individual c : population) {
            System.out.println(c);
        }
    }
 
    /**
     * This method generates the new population applying the genetic operators:
     *   - Selection strategy: 4-Tournament -> 2 winners are parents
     *   - Crossover method: Uniform crossover -> 2 children are generated
     *   - Replacement strategy: Steady-State (10%) + Elitism
     */
    void produceNextGen() {
        LinkedList<Individual> newpopulation = new LinkedList<Individual>();
 
        while (newpopulation.size() < NUM_INDIVIDUALS
                * (1.0 - (PARENT_USE_PERCENT / 100.0))) {
 
            // *******************************
            // ********** SELECTION **********
            // *******************************
            
            // select 4 different and random indexes of individuals in the population
            int size = population.size();
            int i = rand.nextInt(size);
            int j, k, l;
 
            j = k = l = i;
 
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
            float f1 = c1.fitness;
            float f2 = c2.fitness;
            float f3 = c3.fitness;
            float f4 = c4.fitness;
            
            System.out.println("Padres iniciales: ");
            System.out.println(c1 + " " + f1);
            System.out.println(c2 + " " + f2);
            System.out.println(c3 + " " + f3);
            System.out.println(c4 + " " + f4);
 
            // Perform the tournament and select the winners (parents)
            Individual w1, w2;
 
            if (f1 >= f2)
                w1 = c1;
            else
                w1 = c2;
 
            if (f3 >= f4)
                w2 = c3;
            else
                w2 = c4;
            
            System.out.println("Padres escogidos: ");
            System.out.println(w1 + " " + w1.fitness);
            System.out.println(w2 + " " + w2.fitness);
 
            // *******************************
            // ********** CROSSOVER **********
            // *******************************
            Individual child1, child2;
 
            // ONE-POINT CROSSOVER - Random pivot
            // int pivot = rand.nextInt(Candidate.SIZE-2) + 1; // cut interval is 1 .. size-1
            // child1 = newChild(w1,w2,pivot);
            // child2 = newChild(w2,w1,pivot);
 
            
            // The Crossover is applied depending on a probability
            if (rand.nextFloat() <= CROSSOVER_PROBABILITY){
                // UNIFORM CROSSOVER
                Individual[] childs = newChilds(w1, w2);
                child1 = childs[0];
                child2 = childs[1];
            }
            else {
                // If not applied, the childs are the same as the parents
                child1 = w1;
                child2 = w2;
            }
            
            
            // ******************************
            // ********** MUTATION **********
            // ******************************

            // The individuals are mutated depending on the mutation probability
            if (rand.nextFloat() <= MUTATION_PROBABILITY)
                mutate(child1);
            if (rand.nextFloat() <= MUTATION_PROBABILITY)
                mutate(child2);
 
            
            // *********************************
            // ********** REPLACEMENT **********
            // *********************************
            
            try {
		        escribir = new FileWriter("IndividuoAEvaluar.txt",false);
		        pw = new PrintWriter(escribir);
		        pw.println(child1.toString() + " " + i + " " + numGeneration);
		    } catch (Exception ex2){
		        System.out.println("Fallo de fichero\n");
		    }finally{
		    	try{
		    		if (null!=escribir)
		        		escribir.close();
		        		pw.close();
		        } catch (Exception e2){
		        		e2.printStackTrace();
		        }
		     }
        	
            for (int n = 0; n < NUM_COMBATS_INDIVIDUAL; n++) {
		        try {
		            String cmd = "script.bat";
		            Process p = Runtime.getRuntime().exec(cmd); 
		            try {
	            		p.waitFor();
	            	} catch (InterruptedException e) {
	                    System.out.println("Main thread is interrupted");
	                }
		        } catch (IOException ioe) {
		           System.out.println (ioe);
		        }
            }
	        	
	        child1.fitness();
	        
	        System.out.println("Hijo 1: ");
	        System.out.println(child1 + " " + child1.fitness);
	        
	        try {
		        escribir = new FileWriter("IndividuoAEvaluar.txt",false);
		        pw = new PrintWriter(escribir);
		        pw.println(child2.toString() + " " + i + " " + numGeneration);
		    } catch (Exception ex2){
		        System.out.println("Fallo de fichero\n");
		    }finally{
		    	try{
		    		if (null!=escribir)
		        		escribir.close();
		        		pw.close();
		        } catch (Exception e2){
		        		e2.printStackTrace();
		        }
		     }
        	
	        for (int n = 0; n < NUM_COMBATS_INDIVIDUAL; n++) {
		        try {
		            String cmd = "script.bat";
		            Process p = Runtime.getRuntime().exec(cmd); 
		            try {
	            		p.waitFor();
	            	} catch (InterruptedException e) {
	                    System.out.println("Main thread is interrupted");
	                }
		        } catch (IOException ioe) {
		           System.out.println (ioe);
		        }
	        }
	        	
	        child2.fitness();
	        
	        System.out.println("Hijo 2: ");
	        System.out.println(child2 + " " + child2.fitness);
            
            // The new childs are added to the population if they are better
            // than their parents. Otherwise the parents remain.
            if (child1.fitness >= w1.fitness || child1.fitness >= w2.fitness)
                newpopulation.add(child1);
            else
                newpopulation.add(w1);
            
            if (child2.fitness >= w1.fitness || child2.fitness >= w2.fitness)
                newpopulation.add(child2);
            else
                newpopulation.add(w2);
        }
 
        // Add top percent parent (Elitism)
        int j = (int) (NUM_INDIVIDUALS * PARENT_USE_PERCENT / 100.0);
        for (int i = 0; i < j; i++) {
            newpopulation.add(population.get(i));
        }
 
        population = newpopulation;
        Collections.sort(population);
    }
 
    
    // one-point crossover random pivot
    Individual newChild(Individual c1, Individual c2, int pivot) {
        Individual child = new Individual(SIZE_INDIVIDUAL);
 
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
    Individual[] newChilds(Individual c1, Individual c2) {
        Individual child1 = new Individual(SIZE_INDIVIDUAL);
        Individual child2 = new Individual(SIZE_INDIVIDUAL);
 
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
        
        File file = null;
		FileReader fr = null;
		BufferedReader br = null;
		int stageWidth = 0;
        
        try {
			file = new File ("C:\\Users\\n-o-e\\OneDrive\\Escritorio\\IA'S\\LANZADOR\\GameData.txt");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line = br.readLine();
			stageWidth = Integer.parseInt(line);
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
        
        c.genotype[i] = rand.nextInt(stageWidth); // flip
    }
 
 
    // PROCESS OF THE GENETIC ALGORITHM
    void run() {
        int count = 0;
 
        while (count < NUM_GENERATIONS) {
        	float acumFitness = 0;
            produceNextGen();
            count++;
            numGeneration++;
            System.out.println("Generación "+count);
            print();
            
            for (int i = 0; i < population.size(); i++){
            	acumFitness = population.get(i).fitness;
            }
            
            try {
    	        escribir = new FileWriter("EvolucionFitness.txt",true);
    	        pw = new PrintWriter(escribir);
    	        pw.println("GENERACIÓN " + count);
    	        pw.println("Mejor fitness: " + population.get(0).fitness);
    	        pw.println("Media fitness: " + acumFitness/population.size());
    	    } catch (Exception ex2){
    	        System.out.println("Fallo de fichero\n");
    	    }finally{
    	    	try{
    	    		if (null!=escribir)
    	        		escribir.close();
    	        		pw.close();
    	        } catch (Exception e2){
    	        		e2.printStackTrace();
    	        }
    	     }
            
            try {
    	        escribir = new FileWriter("EvolucionGeneraciones.txt",true);
    	        pw = new PrintWriter(escribir);
    	        pw.println("GENERACIÓN " + count);
    	        pw.print(population);
    	        pw.println("");
    	        pw.println("-------------------------------------------------");
    	    } catch (Exception ex2){
    	        System.out.println("Fallo de fichero\n");
    	    }finally{
    	    	try{
    	    		if (null!=escribir)
    	        		escribir.close();
    	        		pw.close();
    	        } catch (Exception e2){
    	        		e2.printStackTrace();
    	        }
    	     }
        }
        
 
        System.out.println("\nResult");
        print();
    }
    
    
    // ################################################################
    // ##########    Genetic Algorithm for ONEMAX PROBLEM    ##########
    // ################################################################
    public static void main(String[] args) {
    	// Declaración de variables para leer/escribir archivos
    	FileWriter escribir = null;
    	PrintWriter pw = null;
    	
        // Initial time
        long BEGIN = System.currentTimeMillis();
 
        // RUN the Genetic Algorithm
        GA ga = new GA();
        float acumFitness = 0;
        
        for (int i = 0; i < ga.population.size(); i++) {
        	Individual c = ga.population.get(i);
        	
        	try {
		        escribir = new FileWriter("IndividuoAEvaluar.txt",false);
		        pw = new PrintWriter(escribir);
		        pw.println(c.toString() + " " + i + " " + 0);
		    } catch (Exception ex2){
		        System.out.println("Fallo de fichero\n");
		    }finally{
		    	try{
		    		if (null!=escribir)
		        		escribir.close();
		        		pw.close();
		        } catch (Exception e2){
		        		e2.printStackTrace();
		        }
		     }
        	
	        for (int j = 0; j < NUM_COMBATS_INDIVIDUAL; j++) {
	        	try {
	            	String cmd = "script.bat";
	            	Process p = Runtime.getRuntime().exec(cmd); 
	            	try {
	            		p.waitFor();
	            	} catch (InterruptedException e) {
	                    System.out.println("Main thread is interrupted");
	                }
	            } catch (IOException ioe) {
	            	System.out.println (ioe);
	            }
	        }
	        	
	        c.fitness();
	        acumFitness += c.fitness;
	        System.out.println("He evaluado: " + c + " " + c.fitness);
		}
        
        Collections.sort(ga.population);
        
        try {
	        escribir = new FileWriter("EvolucionFitness.txt",false);
	        pw = new PrintWriter(escribir);
	        pw.println("GENERACIÓN " + 0);
	        pw.println("Mejor fitness: " + ga.population.get(0).fitness);
	        pw.println("Media fitness: " + acumFitness/ga.population.size());
	    } catch (Exception ex2){
	        System.out.println("Fallo de fichero\n");
	    }finally{
	    	try{
	    		if (null!=escribir)
	        		escribir.close();
	        		pw.close();
	        } catch (Exception e2){
	        		e2.printStackTrace();
	        }
	     }
        
        ga.run();
 
        // Final time
        long END = System.currentTimeMillis();
        System.out.println("Time: " + (END - BEGIN) / 1000.0 + " sec.");
    }
 
}