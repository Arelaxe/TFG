import java.util.Collections;
import java.util.LinkedList; 
import java.util.Random;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
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
 
public class GA_Mizuno {
	static final int NUM_GENERATIONS = 50;
    static final int NUM_INDIVIDUALS = 10;
    static final int NUM_BATTLES_INDIVIDUAL = 3;
    static final int SIZE_INDIVIDUAL = 7;
    static final int PARENT_USE_PERCENT = 10;  // To define the Steady-State approach
    static final double CROSSOVER_PROBABILITY = 1.00;  // For Steady-State is always performed
    static final double MUTATION_PROBABILITY = 0.5;
    // Random numbers generator
    final Random rand = new Random();
 
    // Population: List of Individuals
    LinkedList<Individual_Mizuno> population = new LinkedList<Individual_Mizuno>();
    
    int num_fase;
    int num_individuo;
    int num_combate_individuo;
    int num_generacion;
    
    FileWriter escribir;
	PrintWriter pw;
 
    // Constructor: It creates the initial population
    public GA_Mizuno(MizunoAI ia) {
    	File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;

		try{
			archivo = new File ("poblacion.txt");
			if (!archivo.exists()) { // Si el archivo no existe, creamos una poblaci�n inicial
				num_fase = 0;
				num_individuo = 0;
				num_combate_individuo = 0;
			}
			else { // En caso contrario, la leemos del archivo
				fr = new FileReader(archivo);
				br = new BufferedReader(fr);
				String linea;
				linea=br.readLine();
				String[] reglas = linea.split(" ");
				num_fase = Integer.parseInt(reglas[0]);
				num_individuo = Integer.parseInt(reglas[1]);
				num_combate_individuo = Integer.parseInt(reglas[2]);
				num_generacion = Integer.parseInt(reglas[3]);
				while((linea=br.readLine())!=null){
					if (!linea.equals("")) {
						String[] datos = linea.split(" ");
						Individual_Mizuno c = new Individual_Mizuno(ia);
						for (int i = 0; i < SIZE_INDIVIDUAL; i++) {
							c.genotype[i] = Integer.parseInt(datos[i]);
						}
						c.fitness = Float.parseFloat(datos[SIZE_INDIVIDUAL]);
						
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
				System.out.println("Fallo de fichero de poblaci�n inicial\n");
			}
		}
    	print();
    }
 
    // print: shows the whole population in the screen
    void print() {
        System.out.println("-- Poblaci�n de momento");
        for (Individual_Mizuno c : population) {
            System.out.println(c.genotype[0] + " " +  c.genotype[1] + " " + c.genotype[2] + " " 
            				+ c.genotype[3] + " " + c.genotype[4] + " " + c.genotype[5] + " " + c.genotype[6] + " " + c.fitness);
        }
    }
    
    @Override
    public String toString() {
    	String cadena = "";
    	for (Individual_Mizuno c : population) {
            cadena += c.genotype[0] + " " +  c.genotype[1] + " " + c.genotype[2] + " " + c.genotype[3] + " " 
            		+ c.genotype[4] + " " + c.genotype[5] + " " + c.genotype[6] + " " + c.fitness + "\n";
        }
    	return cadena;
    }
 
    /**
     * This method generates the new population applying the genetic operators:
     *   - Selection strategy: 4-Tournament -> 2 winners are parents
     *   - Crossover method: Uniform crossover -> 2 children are generated
     *   - Replacement strategy: Steady-State (10%) + Elitism
     */
    void gaNextPhase(MizunoAI ia) {
    	if(num_fase == 1 || num_fase == 0) {
    		if (num_fase == 0) {
    			for (int i = 0; i < NUM_INDIVIDUALS; i++) {
		            // Create an individual of the desired size
		            Individual_Mizuno c = new Individual_Mizuno(ia);
		            // It is initialized randomly
		            c.random();
		            // And added to the population
		            population.add(c);
		        }
    			num_fase = 1;
    		}
    		Individual_Mizuno c = population.get(num_individuo);
    		ia.genotype = c.genotype;
    		ia.asociado = c;
    	}
    	else if(num_fase == 2) {
    		gaNextGen(ia);
    	}
    	else if (num_fase == 3) {
    		System.out.println("Fase 3");
    		File archivo = null;
			FileReader fr = null;
			BufferedReader br = null;
			Individual_Mizuno hijo = new Individual_Mizuno(ia);
			Individual_Mizuno hijo2 = new Individual_Mizuno(ia);
			num_combate_individuo++;
			
			try {
				archivo = new File ("hijo1.txt");
				fr = new FileReader(archivo);
				br = new BufferedReader(fr);
				String linea;
				linea=br.readLine();
				String[] genotipo = linea.split(" ");

				for (int n = 0; n < SIZE_INDIVIDUAL; n++) {
					hijo.genotype[n] = Integer.parseInt(genotipo[n]);
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
					System.out.println("Fallo de fichero hijo 1 en fase 3\n");
				}
			}
    		
    		if (num_combate_individuo == NUM_BATTLES_INDIVIDUAL) {
    			num_fase = 4;
    			num_combate_individuo = 0;
    			
    			try {
    				archivo = new File ("hijo2.txt");
    				fr = new FileReader(archivo);
    				br = new BufferedReader(fr);
    				String linea;
    				linea=br.readLine();
    				String[] genotipo = linea.split(" ");

    				for (int n = 0; n < SIZE_INDIVIDUAL; n++) {
    					hijo2.genotype[n] = Integer.parseInt(genotipo[n]);
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
    					System.out.println("Fallo de fichero hijo 1 en fase 3\n");
    				}
    			}
    			
    			ia.genotype = hijo2.genotype;
    			ia.asociado = hijo2;
    			ia.hijo_testeado = 2;
    		}
    		else {
    			ia.genotype = hijo.genotype;
    			ia.asociado = hijo;
    			ia.hijo_testeado = 1;
    		}
    	}
    	else if (num_fase == 4) {
    		System.out.println("Fase 4");
    		File archivo = null;
			FileReader fr = null;
			BufferedReader br = null;
    		Individual_Mizuno hijo2 = new Individual_Mizuno(ia);
    		num_combate_individuo++;
    
			try {
				archivo = new File ("hijo2.txt");
				fr = new FileReader(archivo);
				br = new BufferedReader(fr);
				String linea;
				linea=br.readLine();
				String[] genotipo = linea.split(" ");

				for (int n = 0; n < SIZE_INDIVIDUAL; n++) {
					hijo2.genotype[n] = Integer.parseInt(genotipo[n]);
				}
				if (num_combate_individuo == NUM_BATTLES_INDIVIDUAL) {
					hijo2.fitness = Float.parseFloat(br.readLine());
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
			
			if (num_combate_individuo == NUM_BATTLES_INDIVIDUAL) {
				num_combate_individuo = 0;
				LinkedList<Individual_Mizuno> newpopulation = new LinkedList<>();
				Individual_Mizuno hijo1 = new Individual_Mizuno(ia);
				Individual_Mizuno padre1 = new Individual_Mizuno(ia);
				Individual_Mizuno padre2 = new Individual_Mizuno(ia);
				
				try {
					archivo = new File ("hijo1.txt");
					fr = new FileReader(archivo);
					br = new BufferedReader(fr);
					String linea;
					linea = br.readLine();
					String[] genotipo = linea.split(" ");
					
					for (int n = 0; n < SIZE_INDIVIDUAL; n++) {
						hijo1.genotype[n] = Integer.parseInt(genotipo[n]);
					}
					
					linea = br.readLine();
					
					hijo1.fitness = Float.parseFloat(linea);
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
				
				try {
					archivo = new File ("padre1.txt");
					fr = new FileReader(archivo);
					br = new BufferedReader(fr);
					String linea;
					linea=br.readLine();
					String[] genotipo = linea.split(" ");

					padre1.fitness = Float.parseFloat(genotipo[SIZE_INDIVIDUAL]);
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
				
				try {
					archivo = new File ("padre2.txt");
					fr = new FileReader(archivo);
					br = new BufferedReader(fr);
					String linea;
					linea=br.readLine();
					String[] genotipo = linea.split(" ");

					padre2.fitness = Float.parseFloat(genotipo[SIZE_INDIVIDUAL]);
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
				
				// *********************************
                // ********** REPLACEMENT **********
                // *********************************
                
				population.add(hijo1);
				population.add(hijo2);
				
				System.out.println("Hijo 1: " + hijo1.genotype[0] + " " + hijo1.genotype[1] + " " + hijo1.genotype[2] + " " 
						+ hijo1.genotype[3] + " " + hijo1.genotype[4] + " " + hijo1.genotype[5] + " " + hijo1.genotype[6] + " " + hijo1.fitness);
				
				System.out.println("Hijo 2: " + hijo2.genotype[0] + " " + hijo2.genotype[1] + " " + hijo2.genotype[2] + " " 
						+ hijo2.genotype[3] + " " + hijo2.genotype[4] + " " + hijo2.genotype[5] + " " + hijo2.genotype[6] + " " + hijo2.fitness);
     
                Collections.sort(population);
                
                // Add top percent parent (Elitism)
                for (int n = 0; n < NUM_INDIVIDUALS; n++) {
                	newpopulation.add(population.get(n));
                }
     
                population = newpopulation;
                Collections.sort(population);
                
                float acumulador_fitness = 0;
                
                for (int i = 0; i < population.size(); i++) {
                	System.out.println(population.get(i).genotype[0] + " " + population.get(i).genotype[1] + " " + population.get(i).genotype[2] + " " + population.get(i).genotype[3] + " " + population.get(i).fitness);
                	acumulador_fitness += population.get(i).fitness;
                }
                
                acumulador_fitness = acumulador_fitness/population.size();
                
                try {
        			escribir = new FileWriter("fitness.txt",true);
        			pw = new PrintWriter(escribir);
        			pw.println("GENERACI�N " + num_generacion);
        			pw.println("Mejor fitness: " + population.get(0).fitness);
        			pw.println("Media fitness: " + acumulador_fitness);
        		} catch (Exception ex2){
        			System.out.println("Fallo de fichero fitness\n");
        		}finally{
        			try{
        				if (null!=escribir)
        					escribir.close();
        					pw.close();
        			} catch (Exception e2){
        				e2.printStackTrace();
        			}
        		}
                
                num_generacion++;
                gaNextGen(ia);
    		}
			else {
				ia.genotype = hijo2.genotype;
				ia.asociado = hijo2;
				ia.hijo_testeado = 2;
			}
    	}
    }
    
    void gaNextGen(MizunoAI ia) {
    	System.out.println("Fase 2 del algoritmo: ");
		// *******************************
        // ********** SELECTION **********
        // *******************************
    	
    	Collections.sort(population);
       
        // Get the selected individuals (chromosomes)
        Individual_Mizuno w1 = population.get(0);
        Individual_Mizuno w2 = population.get(1);

        // *******************************
        // ********** CROSSOVER **********
        // *******************************
        Individual_Mizuno child1, child2;
        
        // The Crossover is applied depending on a probability
        if (rand.nextFloat() <= CROSSOVER_PROBABILITY){
            Individual_Mizuno[] childs = newChilds(w1, w2, ia);
            child1 = childs[0];
            child2 = childs[1];
        }
        else {
            child1 = w1;
            child2 = w2;
        }
        
        
        // ******************************
        // ********** MUTATION **********
        // ******************************

        // The individuals are mutated depending on the mutation probability
        if (rand.nextFloat() <= MUTATION_PROBABILITY)
            mutate(child1,ia);
        if (rand.nextFloat() <= MUTATION_PROBABILITY)
            mutate(child2,ia);
        
        Individual_Mizuno a_testear = child1;
		ia.genotype = a_testear.genotype;
		ia.asociado = a_testear;
		ia.hijo_testeado = 1;
		
		try {
			escribir = new FileWriter("hijo1.txt",false);
			pw = new PrintWriter(escribir);
			pw.println(a_testear.genotype[0] + " " + a_testear.genotype[1] + " " + a_testear.genotype[2] + " " 
					+ a_testear.genotype[3] + " " + a_testear.genotype[4] + " " + a_testear.genotype[5] + " "
					+ a_testear.genotype[6]);
		} catch (Exception ex2){
			System.out.println("Fallo de fichero hijo 1\n");
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
			escribir = new FileWriter("hijo2.txt",false);
			pw = new PrintWriter(escribir);
			pw.println(child2.genotype[0] + " " + child2.genotype[1] + " " + child2.genotype[2]  + " " 
					+ child2.genotype[3] + " " + child2.genotype[4] + " " + child2.genotype[5] + " "
					+ child2.genotype[6]);
		} catch (Exception ex2){
			System.out.println("Fallo de fichero hijo 2\n");
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
			escribir = new FileWriter("padre1.txt",false);
			pw = new PrintWriter(escribir);
			pw.println(w1.genotype[0] + " " + w1.genotype[1] + " " + w1.genotype[2]  + " " + w1.genotype[3] + " " 
					+ w1.genotype[4] + " " + w1.genotype[5] + " " + w1.genotype[6] + " " + w1.fitness);
		} catch (Exception ex2){
			System.out.println("Fallo de fichero padre 1\n");
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
			escribir = new FileWriter("padre2.txt",false);
			pw = new PrintWriter(escribir);
			pw.println(w2.genotype[0] + " " + w2.genotype[1] + " " + w2.genotype[2]  + " " + w2.genotype[3] 
					+ " " + w2.genotype[4] + " " + w2.genotype[5] + " " + w2.genotype[6] + " " + w2.fitness);
		} catch (Exception ex2){
			System.out.println("Fallo de fichero padre 2\n");
		}finally{
			try{
				if (null!=escribir)
					escribir.close();
					pw.close();
			} catch (Exception e2){
				e2.printStackTrace();
			}
		}
		
		num_combate_individuo = 0;
		num_fase = 3;
		System.out.println("Deber�a probar el hijo 1");
    }
 
    
    // one-point crossover random pivot
    Individual_Mizuno newChild(Individual_Mizuno c1, Individual_Mizuno c2, int pivot, MizunoAI ia) {
        Individual_Mizuno child = new Individual_Mizuno(ia);
 
        for (int i = 0; i < pivot; i++) {
            child.genotype[i] = c1.genotype[i];
        }
        for (int j = pivot; j < Individual_Mizuno.SIZE; j++) {
            child.genotype[j] = c2.genotype[j];
        }
 
        return child;
    }
 
    
    // Uniform crossover: The genes are copied from parents to childs randomly 
    //                    (following a uniform distribution)
    Individual_Mizuno[] newChilds(Individual_Mizuno c1, Individual_Mizuno c2, MizunoAI ia) {
        Individual_Mizuno child1 = new Individual_Mizuno(ia);
        Individual_Mizuno child2 = new Individual_Mizuno(ia);
 
        // Each gene is taken from one of the parents randomly
        for (int i = 0; i < Individual_Mizuno.SIZE; i++) {
            if (rand.nextFloat() >= 0.5) {
                child1.genotype[i] = c1.genotype[i];
                child2.genotype[i] = c2.genotype[i];
            } else {
                child1.genotype[i] = c2.genotype[i];
                child2.genotype[i] = c1.genotype[i];
            }
        }
 
        return new Individual_Mizuno[] { child1, child2 };
    }
 
    
    // Flip mutation: a random gene is selected and turned from '0' to '1' or viceversa
    void mutate(Individual_Mizuno c, MizunoAI ia) {
    	int option = rand.nextInt(2);
    	if (option>=1) {
    		int i = rand.nextInt(Individual_Mizuno.SIZE);
    		if (i == 0 || i == 1) {
    			c.genotype[i] = rand.nextInt(ia.gd.getStageWidth());
    		}
    		else {
    			c.genotype[i] = rand.nextInt(ia.gd.getMaxHP(ia.playerNumber));
    		}
    	}
    	else {
    		int i = rand.nextInt(Individual_Mizuno.SIZE);
    		int j = rand.nextInt(Individual_Mizuno.SIZE);
    		
    		int aux = c.genotype[i];
    		c.genotype[i] = c.genotype[j];
    		c.genotype[j] = aux;
    	}
        
    }
 
}