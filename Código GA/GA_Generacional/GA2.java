import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
 
public class GA2 {
    
    static final int NUM_GENERATIONS = 30;
    static final int SIZE_INDIVIDUAL = 8;
    static final int NUM_INDIVIDUALS = 16;
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
    public GA2() {
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
 
    void produceNextGen() {
        LinkedList<Individual> newpopulation = new LinkedList<Individual>();
        LinkedList<Individual> candidates = new LinkedList<Individual>();
 
        int size = population.size();
        int a, b;
        
        for (int i = 0; i < size/2; i++) {
        	a = rand.nextInt(size);
            b = a;
            
            while (b == a) {
            	b = rand.nextInt(size);
            }
            
            Individual c1 = population.get(a);
            Individual c2 = population.get(b);
            
            if (c1.fitness > c2.fitness) {
            	candidates.add(c1);
            }
            else {
            	candidates.add(c2);
            }
        }
        
        // CRUCE
        for (int i = 0; i < candidates.size(); i+=2) {
        	Individual c1 = population.get(i);
        	Individual c2 = population.get(i+1);
        	
        	Individual [] children = newChilds(c1, c2);
        	Individual child1 = children[0];
        	Individual child2 = children[1];
        	
        	System.out.println("Padres: ");
            System.out.println(c1 + " " + c1.fitness);
            System.out.println(c2 + " " + c2.fitness);
            
            
            // MUTACIÓN
            if (rand.nextFloat() <= MUTATION_PROBABILITY)
                mutate(child1);
            if (rand.nextFloat() <= MUTATION_PROBABILITY)
                mutate(child2);
            
            child1.calculateFitness();
            
            System.out.println("Hijo 1: ");
            System.out.println(child1 + " " + child1.fitness);
            
        	child2.calculateFitness();
        	
        	System.out.println("Hijo 2: ");
            System.out.println(child2 + " " + child2.fitness);
            
            //REEMPLAZAMIENTO
        	if (child1.fitness > c1.fitness || child1.fitness > c2.fitness) {
        		newpopulation.add(child1);
        	} 
        	else {
        		newpopulation.add(c1);
        	}
        	
        	if (child2.fitness > c1.fitness || child2.fitness > c2.fitness) {
        		newpopulation.add(child2);
        	}
        	else {
        		newpopulation.add(c2);
        	}
        }
        
        for (int i = 0; i < size/2; i++) {
        	Individual c = new Individual(SIZE_INDIVIDUAL);
        	c.random();
        	c.calculateFitness();
        	System.out.println("Nuevos elementos aleatorios de la población");
        	System.out.println("He evaluado: " + c + " " + c.fitness);
        	newpopulation.add(c);
        }
 
        population = newpopulation;
        Collections.sort(population);
        System.out.println("Ahora mi población es: ");
        System.out.println(population);
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
        File file = null;
		FileReader fr = null;
		BufferedReader br = null;
		int stageWidth = 0;
		int maxEnergy = 0;
		int maxHP = 0;
        
        try {
			file = new File ("C:\\Users\\n-o-e\\OneDrive\\Escritorio\\IA'S\\LANZADOR\\GameData.txt");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line = br.readLine();
			String [] data = line.split(" ");
			stageWidth = Integer.parseInt(data[0]);
			maxEnergy = Integer.parseInt(data[1]);
			maxHP = Integer.parseInt(data[2]);
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
        
        int index = rand.nextInt(SIZE_INDIVIDUAL-1);
        
        if (index == 0 || index == 1) {
        	c.genotype[index] = rand.nextInt(stageWidth);
        }
        else {
        	c.genotype[index] = rand.nextInt(maxEnergy);
        }
    }
 
 
    // PROCESS OF THE GENETIC ALGORITHM
    void run() {
        int count = 0;
 
        while (count < NUM_GENERATIONS) {
        	float acumFitness = 0;
        	System.out.println("Estoy en la generación " + count);
            produceNextGen();
            count++;
            numGeneration++;
            System.out.println("Generación "+count);
            print();
            
            for (int i = 0; i < population.size(); i++){
            	acumFitness += population.get(i).fitness;
            }
            
            try {
    	        escribir = new FileWriter("EvolucionFitness.txt",true);
    	        pw = new PrintWriter(escribir);
    	        pw.println("GENERACIÓN " + count);
    	        pw.println("Mejor fitness: " + population.get(0).fitness);
    	        pw.println("Media fitness: " + acumFitness/population.size());
    	        pw.println("Peor fitness: " + population.get(population.size()-1).fitness);
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
    	        escribir = new FileWriter("toPlot.dat",true);
    	        pw = new PrintWriter(escribir);
    	        pw.println(count + " " + population.get(0).fitness + " " + acumFitness/population.size() + " " 
    	        			+ population.get(population.size()-1).fitness);
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
    	        for (int i = 0; i < population.size(); i ++) {
    	        	String s = "";
    	        	for (int j = 0; j < SIZE_INDIVIDUAL; j++) {
    	        		s += population.get(i).genotype[j] + " ";
    	        	}
    	        	s += population.get(i).fitness;
    	        	pw.println(s);
    	        }
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
        GA2 ga = new GA2();
        float acumFitness = 0;
        
        for (int i = 0; i < ga.population.size(); i++) {
        	Individual c = ga.population.get(i);
	        	
	        c.calculateFitness();
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
	        pw.println("Peor fitness: " + ga.population.get(ga.population.size()-1).fitness);
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
	        escribir = new FileWriter("EvolucionGeneraciones.txt",false);
	        pw = new PrintWriter(escribir);
	        pw.println("GENERACIÓN " + 0);
	        for (int i = 0; i < ga.population.size(); i ++) {
	        	String s = "";
	        	for (int j = 0; j < SIZE_INDIVIDUAL; j++) {
	        		s += ga.population.get(i).genotype[j] + " ";
	        	}
	        	s += ga.population.get(i).fitness;
	        	pw.println(s);
	        }
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
        
        try {
	        escribir = new FileWriter("toPlot.dat",false);
	        pw = new PrintWriter(escribir);
	        pw.println(0 + " " + ga.population.get(0).fitness + " " + acumFitness/ga.population.size() 
	        			+ " " + ga.population.get(ga.population.size()-1).fitness);
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