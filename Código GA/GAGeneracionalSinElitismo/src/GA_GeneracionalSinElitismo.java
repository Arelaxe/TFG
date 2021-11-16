import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
 
public class GA_GeneracionalSinElitismo {
    
    static final int NUM_GENERATIONS = 20;
    static final int SIZE_INDIVIDUAL = 8;
    static final int NUM_INDIVIDUALS = 16;
    static final double CROSSOVER_PROBABILITY = 0.6;
    static final double MUTATION_PROBABILITY = 1/SIZE_INDIVIDUAL;
    int numGeneration = 0;
    // Declaración de variables para leer/escribir archivos
	FileWriter escribir = null;
	PrintWriter pw = null;

	// Generadores de números aleatorios
    final Random rand = new Random();
    final Random randFloat = new Random();
    Individual theBest;
 
    // Población: Lista de individuos
    LinkedList<Individual> population = new LinkedList<Individual>();
 
    // Constructor: Crea una población inicial
    public GA_GeneracionalSinElitismo() {
    	for (int i = 0; i < NUM_INDIVIDUALS; i++) {
        	// Crea un individuo
            Individual c = new Individual(SIZE_INDIVIDUAL);
            // Inicializa el individuo aleatoriamente
            c.random();
            // Añade el individuo a la población
            population.add(c);
        }

        print();
    }
 
    // print: Imprime los genotipos de los individuos de la población y su fitness 
    void print() {
        System.out.println("-- print");
        for (Individual c : population) {
            System.out.println(c);
        }
    }
 
    // produceNextGen: Crea la siguiente generación de individuos
    void produceNextGen() {
        LinkedList<Individual> newpopulation = new LinkedList<Individual>();
        LinkedList<Individual> candidates = new LinkedList<Individual>();
 
        int size = population.size();
        int a, b;
        
        for (int i = 0; i < size; i++) {
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
        	Individual child1 = new Individual(SIZE_INDIVIDUAL); 
        	Individual child2 = new Individual(SIZE_INDIVIDUAL);
        	Individual parent1 = population.get(i);
        	Individual parent2 = population.get(i+1);
            
            if (randFloat.nextFloat() <= CROSSOVER_PROBABILITY) {
            	System.out.println("Ha habido cruce");
            	Individual [] children = newChilds(parent1, parent2);
            	child1 = children[0];
            	child2 = children[1];
            }
            else {
            	System.out.println("No ha habido cruce");
            	child1.genotype = parent1.genotype;
            	child1.fitness = parent1.fitness;
            	child1.mutado = false;
            	child2.genotype = parent2.genotype;
            	child2.fitness = parent2.fitness;
            	child2.mutado = false;
            }
        	
        	System.out.println("Padres: ");
            System.out.println(parent1 + " " + parent1.fitness);
            System.out.println(parent2 + " " + parent2.fitness);
            
            // MUTACIÓN
            mutate(child1);
            
            mutate(child2);
            
            if (child1.fitness != Float.NaN && !child1.mutado) {
            	child1.calculateFitness();
            }
            
            System.out.println("Hijo 1: ");
            System.out.println(child1 + " " + child1.fitness);
            
            if (child2.fitness != Float.NaN && !child2.mutado) {
            	child2.calculateFitness();
            }
            
        	System.out.println("Hijo 2: ");
            System.out.println(child2 + " " + child2.fitness);
            
            newpopulation.add(child1);
            newpopulation.add(child2);
        }
 
        // REEMPLAZO
        population = newpopulation;
        Collections.sort(population);
        System.out.println("Ahora mi población es: ");
        System.out.println(population);
        if (population.get(0).fitness > theBest.fitness) {
        	theBest.fitness = population.get(0).fitness;
        	theBest.genotype = population.get(0).genotype;
        }
    }
    
    // newChilds: Genera dos hijos a partir de dos padres
    Individual[] newChilds(Individual c1, Individual c2) {
        Individual child1 = new Individual(SIZE_INDIVIDUAL);
        Individual child2 = new Individual(SIZE_INDIVIDUAL);
 
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
 
    
    // mutate: Muta un individuo
    void mutate(Individual c) {
        File file = null;
		FileReader fr = null;
		BufferedReader br = null;
		int stageWidth = 0;
		int maxEnergy = 0;
		int maxHP = 0;
        
		// Lee parámetros de juego desde GameData
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
        
        // Aplica mutación sobre cada gen
        for (int i = 0; i < c.genotype.length; i++) {
        	if (randFloat.nextFloat() <= MUTATION_PROBABILITY) {
        		if (i == 0 || i == 1) {
                	c.genotype[i] = rand.nextInt(stageWidth);
                }
                else {
                	c.genotype[i] = rand.nextInt(maxEnergy);
                }
        		c.mutado = true;
        	}
        }
    }
 
 
    // run: Lanza el AG
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
    	        pw.println("Mejor fitness (generación): " + population.get(0).fitness);
    	        pw.println("Mejor fitness (total): " + theBest.fitness);
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
    	        			+ population.get(population.size()-1).fitness + " " + theBest.fitness);
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
    	        String s = "Mejor: ";
    	        for (int j = 0; j < SIZE_INDIVIDUAL; j++) {
	        		s += theBest.genotype[j] + " ";
	        	}
	        	s += theBest.fitness;
	        	pw.println(s);
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
    
    
    public static void main(String[] args) {
    	// Declaración de variables para leer/escribir archivos
    	FileWriter escribir = null;
    	PrintWriter pw = null;
    	
        // Tiempo inicial
        long BEGIN = System.currentTimeMillis();
 
        // Lanza el AG
        GA_GeneracionalSinElitismo ga = new GA_GeneracionalSinElitismo();
        float acumFitness = 0;
        
        for (int i = 0; i < ga.population.size(); i++) {
        	Individual c = ga.population.get(i);
	        	
	        c.calculateFitness();
	        acumFitness += c.fitness;
	        System.out.println("He evaluado: " + c + " " + c.fitness);
		}
        
        Collections.sort(ga.population);
        ga.theBest = ga.population.get(0);
        
        try {
	        escribir = new FileWriter("EvolucionFitness.txt",false);
	        pw = new PrintWriter(escribir);
	        pw.println("GENERACIÓN " + 0);
	        pw.println("Mejor fitness (generación): " + ga.population.get(0).fitness);
	        pw.println("Mejor fitness (total): " + ga.theBest.fitness);
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
	        String s = "Mejor: ";
	        for (int j = 0; j < SIZE_INDIVIDUAL; j++) {
	        	s += ga.theBest.genotype[j] + " ";
	        }
	        s += ga.theBest.fitness;
	        pw.println(s);
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
	        			+ " " + ga.population.get(ga.population.size()-1).fitness + " " + ga.theBest.fitness);
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
 
        // Tiempo final
        long END = System.currentTimeMillis();
        System.out.println("Time: " + (END - BEGIN) / 1000.0 + " sec.");
    }
 
}