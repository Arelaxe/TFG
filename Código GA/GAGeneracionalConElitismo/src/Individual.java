import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
 
public class Individual implements Comparable<Individual> {
    public static int SIZE;
    static final int NUM_COMBATS_INDIVIDUAL = 2;
    public int[] genotype;
    final Random rand = new Random();
    boolean mutado = false;
    float fitness = Float.NaN;
    // Declaración de variables para leer/escribir archivos
 	FileWriter escribir = null;
 	PrintWriter pw = null;
 
 	// Constructor: Genera un individuo
    public Individual(int size) {
        genotype = new int[size];
        SIZE = size;
    }
 
    // random: Genera un valor aleatorio para un gen
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
 
    // gene: Imprime el genotipo del individuo
    private String gene() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < genotype.length; i++) {
            sb.append(genotype[i] + " ");
        }
        return sb.toString();
    }

    // calculateFitness: Calcula el fitness del individuo
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
		
		try {
			String cmd = "scriptBCP.bat";
	        Process p = Runtime.getRuntime().exec(cmd); 
	        try {
	        	p.waitFor();
            } catch (InterruptedException e) {
                System.out.println("Main thread is interrupted");
            }
	    } catch (IOException ioe) {
	          System.out.println (ioe);
	    }
		
		try {
			String cmd = "scriptDora.bat";
	        Process p = Runtime.getRuntime().exec(cmd); 
	        try {
	        	p.waitFor();
            } catch (InterruptedException e) {
                System.out.println("Main thread is interrupted");
            }
	    } catch (IOException ioe) {
	          System.out.println (ioe);
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
				fitnessThisCombat *= Float.parseFloat(results[2]);
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
    
	// flip: Cambia el valor de un gen por un aleatorio
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
 
    // comparteTo: Compara dos individuos según su fitness
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
 
    // toString: Imprime un individuo
    @Override
    public String toString() {
        return gene();
    }
}