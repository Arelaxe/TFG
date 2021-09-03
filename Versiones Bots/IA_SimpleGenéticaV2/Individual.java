import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.util.Random;

public class Individual implements Comparable<Individual>{
	public int[] genotype;
	public static int SIZE;
	final Random rand = new Random();
	IA ia;
	float fitness;
	
	public Individual(IA ia) {
		genotype = new int[4];
		SIZE = 4;
		this.ia = ia;
	}
	
	public void random() {
		int max_x_distance = ia.gameData.getStageWidth();
		
		for (int i = 0; i < genotype.length; i++) {
			genotype[i] = rand.nextInt(max_x_distance/2);
		}
	}
	
	float fitness() {
		float fitness = 0;
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		float acumulador = 0;
		int num_individuos = 0;
		
		try {
			archivo = new File ("resultados.txt");
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);
			String linea;
			
			while((linea=br.readLine())!=null){
				String[] resultados = linea.split(" ");
				acumulador += Float.parseFloat(resultados[0]);
				acumulador -= Float.parseFloat(resultados[1]);
				num_individuos++;
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
		
		fitness = acumulador/num_individuos;
		
		this.fitness = fitness;
		
		return fitness;
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
	
}
