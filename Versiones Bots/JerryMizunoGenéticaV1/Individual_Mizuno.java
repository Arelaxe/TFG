import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.util.Random;

public class Individual_Mizuno implements Comparable<Individual_Mizuno>{
	public int[] genotype;
	public static int SIZE;
	final Random rand = new Random();
	MizunoAI ia;
	float fitness;
	
	public Individual_Mizuno(MizunoAI ia) {
		genotype = new int[7];
		SIZE = 7;
		this.ia = ia;
	}
	
	public void random() {
		int max_x_distance = ia.gd.getStageWidth();
		int max_hp = ia.gd.getMaxHP(ia.playerNumber);
		
		genotype[0] = rand.nextInt(max_x_distance);
		genotype[1] = rand.nextInt(max_x_distance);
		genotype[2] = rand.nextInt(max_hp);
		genotype[3] = rand.nextInt(max_hp);
		genotype[4] = rand.nextInt(max_hp);
		genotype[5] = rand.nextInt(max_hp);
		genotype[6] = rand.nextInt(max_hp);
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
    public int compareTo(Individual_Mizuno o) {
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
