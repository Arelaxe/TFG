import java.util.Random;

public class Individual implements Comparable<Individual>{
	public int[] genotype;
	public static int SIZE;
	final Random rand = new Random();
	IA ia;
	int fitness;
	
	public Individual(IA ia) {
		genotype = new int[4];
		SIZE = 4;
		this.ia = ia;
	}
	
	public void random() {
		for (int i = 0; i < genotype.length; i++) {
			genotype[i] = rand.nextInt(500);
		}
	}
	
	int fitness() {
		if (ia.frameData == null || ia.frameData.getCharacter(ia.playerNumber) == null) fitness = 0;
		else fitness = ia.frameData.getCharacter(ia.playerNumber).getHp() - ia.frameData.getCharacter(ia.otherPlayer).getHp();
		return fitness;
	}
	
	// Comparison method (Overrided): Compares the fitness of the individuals
    @Override
    public int compareTo(Individual o) {
        int f1 = this.fitness();
        int f2 = o.fitness();
 
        if (f1 < f2)
            return 1;
        else if (f1 > f2)
            return -1;
        else
            return 0;
    }
	
}
