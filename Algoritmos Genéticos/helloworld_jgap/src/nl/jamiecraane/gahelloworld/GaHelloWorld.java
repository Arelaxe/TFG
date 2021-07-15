package nl.jamiecraane.gahelloworld;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DeltaFitnessEvaluator;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.impl.DefaultConfiguration;
import org.jgap.impl.StringGene;

/**
 * Example genetic algorithms program where the goal is to find a solution
 * that matches the target String. The chromosomes are implemented with StringGene's
 * where each value of the StringGene matches one character. The number of genes is
 * the same as the number of characters in the target String.
 */
public class GaHelloWorld {
        // Objective String
	private static final String TARGET = "Hello GAs World!";
        // Number of generations
	private static final int EVOLUTIONS = 3000;

	public GaHelloWorld() throws Exception {
                // Configure the GA 
		Genotype genotype = this.setupGenoType();
                // Run the GA
		this.evolve(genotype);
	}
	
        // Set the Configuration of the GA
	private Genotype setupGenoType() throws Exception {
                // Creates a Default Configuration for the GA
		Configuration gaConf = new DefaultConfiguration();
                
                // Set a Fitness evaluator where lower values means a better fitness
		gaConf.resetProperty(Configuration.PROPERTY_FITEVAL_INST);
		gaConf.setFitnessEvaluator(new DeltaFitnessEvaluator());

                // We are only interested in the fittest individual
		gaConf.setPreservFittestIndividual(true);
		gaConf.setKeepPopulationSizeConstant(false);

                // Define the population size
		gaConf.setPopulationSize(50);

                // Define the Chromosome size (length of the target String)
		int chromeSize = TARGET.length();

                // We use a String-type Gene. We define the possible alphabet
		StringGene gene = new StringGene(gaConf);
		gene.setMaxLength(1);
		gene.setMinLength(1);
		gene.setAlphabet(StringGene.ALPHABET_CHARACTERS_LOWER+StringGene.ALPHABET_CHARACTERS_UPPER+" !");

                // Define a sample chromosome (as a model for the rest)
		IChromosome sampleChromosome = new Chromosome(gaConf, gene, chromeSize);
		gaConf.setSampleChromosome(sampleChromosome);

                // Create an instance of the Fitness Function to use
		HelloWorldFitnessFunction fitnessFunction = new HelloWorldFitnessFunction();
		fitnessFunction.setTarget(TARGET);
                // Assign this fitness function to the GA configuration
		gaConf.setFitnessFunction(fitnessFunction);

                // Initialize the genotype (the whole population) randomly
		return Genotype.randomInitialGenotype(gaConf);
	}
        
        // Evolution of the GA
	private void evolve(Genotype genotype) {
		// Print the first solution
                String solution = this.getSolution(genotype.getFittestChromosome());
		System.out.println(solution);
		
		double previousFitness = Double.MAX_VALUE;
		int numEvolutions = 0;
		
                // Evolutionary Loop
                for (int i = 0; i < EVOLUTIONS; i++) {
			// Perform a generation (with the default operators and our defined fitness)
                        genotype.evolve();
                        
                        // Check if the solution (the best) has changed
			double fitness = genotype.getFittestChromosome().getFitnessValue();
			if (fitness < previousFitness) {
				previousFitness = fitness;
				solution = this.getSolution(genotype.getFittestChromosome());
				System.out.println(solution);
			}
			
                        // If the target String is found then finish the evolution
			if (solution.equals(TARGET)) {
				numEvolutions = i;
				break;
			}
			
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
			}
		}
		
		solution = this.getSolution(genotype.getFittestChromosome());
		System.out.println(solution);
		System.out.println("Needed [" + numEvolutions + "] evolutions for this");
	}
	
        // Transform the integer chromosomes into Strings
	private String getSolution(IChromosome a_subject) {
		StringBuffer solution = new StringBuffer();
		
		Gene[] genes = a_subject.getGenes();
		for (int i = 0; i < genes.length; i++) {
			String allele = (String) genes[i].getAllele();
			solution.append(allele);
		}
		
		return solution.toString();
	}
	
	
	public static void main(String[] args) throws Exception {
		new GaHelloWorld();
	}
}
