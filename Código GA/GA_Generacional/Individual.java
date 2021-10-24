import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
 
/**
 * @author Kunuk Nykjaer
 * Improvements: simplifications, comments - Antonio Mora
 */
 
public class Individual implements Comparable<Individual> {
    public static int SIZE;
    static final int NUM_COMBATS_INDIVIDUAL = 2;
    public int[] genotype;
    final Random rand = new Random();
    float fitness;
    // Declaración de variables para leer/escribir archivos
 	FileWriter escribir = null;
 	PrintWriter pw = null;
 
    public Individual(int size) {
        genotype = new int[size];
        SIZE = size;
    }
 
    // Generates random values for each gene
    void random() {
    	File file = null;
		FileReader fr = null;
		BufferedReader br = null;
		int stageWidth = 0;
		int maxEnergy = 0;
		int maxHP = 0;
    	
    	try {
			file = new File ("C:\\Users\\n-o-e\\OneDrive\\Escritorio\\IA'S\\LANZADOR\\GameData.txt");
			if (file.exists()) {
				fr = new FileReader(file);
				br = new BufferedReader(fr);
				String line = br.readLine();
				String[] data = line.split(" ");
				stageWidth = Integer.parseInt(data[0]);
				maxEnergy = Integer.parseInt(data[1]);
				maxHP = Integer.parseInt(data[2]);
			} 
			else {
				stageWidth = 1000;
				maxEnergy = 500;
				maxHP = 500;
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
    	
        for (int i = 0; i < genotype.length; i++) {
        	if (i == 0 || i == 1) {
        		do {
        			genotype[i] = rand.nextInt(stageWidth);
        		} while (i == 1 && genotype[1] < genotype[0]);
        	}
        	else {
        		genotype[i] = rand.nextInt(maxEnergy);
        	}
        }
    }
 
    private String gene() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < genotype.length; i++) {
            sb.append(genotype[i] + " ");
        }
        return sb.toString();
    }

    float calculateFitness() {
    	File file = null;
		FileReader fr = null;
		BufferedReader br = null;
		float acum = 0;
		int combatNumber = 0;
		
		try {
	        escribir = new FileWriter("IndividuoAEvaluar.txt",false);
	        pw = new PrintWriter(escribir);
	        pw.println(this.toString());
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
		
		try {
			file = new File ("C:\\Users\\n-o-e\\OneDrive\\Escritorio\\IA'S\\LANZADOR\\Evaluado.txt");
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line;
			
			while((line = br.readLine()) != null) {
				String[] results = line.split(" ");
				float fitnessThisCombat = 0;
				fitnessThisCombat += Float.parseFloat(results[0]);
				fitnessThisCombat -= Float.parseFloat(results[1]);
				fitnessThisCombat *= 1/Float.parseFloat(results[2]);
				acum += fitnessThisCombat;
				combatNumber++;
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
		
		try {
	        escribir = new FileWriter("C:\\Users\\n-o-e\\OneDrive\\Escritorio\\IA'S\\LANZADOR\\Evaluado.txt",false);
	        pw = new PrintWriter(escribir);
	        pw.print("");
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
		
		fitness = acum/combatNumber;
		
		return fitness;
    }
    
	// Flip a gene
    void flip(int index){
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
			String[] data = line.split(" ");
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
    	
    	if (index == 0 || index == 1) {
    		genotype[index] = rand.nextInt(stageWidth);
    	}
    	else {
    		genotype[index] = rand.nextInt(maxEnergy);
    	}
    }
 
    // Comparison method (Overrided): Compares the fitness of the individuals
    @Override
    public int compareTo(Individual o) {
        float f1 = this.fitness;
        float f2 = o.fitness;
 
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
        return gene();
    }
}