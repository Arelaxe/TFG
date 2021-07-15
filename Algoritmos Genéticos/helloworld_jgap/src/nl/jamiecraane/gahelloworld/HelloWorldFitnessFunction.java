package nl.jamiecraane.gahelloworld;

import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;

/**
 * Fitness function for the Hello World example.
 */
public class HelloWorldFitnessFunction extends FitnessFunction {
	private String target;

	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * Compares the String formed by the specified subject with the target String.
	 * The errors is the numeric difference between the characters in the target String
	 * and the String from a_subject.
	 * Returns the squere from the errors.
	 */
	@Override
	protected double evaluate(IChromosome a_subject) {
		int errors = 0;

		Gene[] genes = a_subject.getGenes();
		for (int i = 0; i < genes.length; i++) {
			String allele = (String) genes[i].getAllele();
			int diff = Math.abs((int) allele.charAt(0) - (int) this.target.charAt(i));
			errors += diff * diff;
		}

		return errors;
	}

}
