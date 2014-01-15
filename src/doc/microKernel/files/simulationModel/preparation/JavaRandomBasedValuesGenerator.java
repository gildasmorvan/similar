package fr.lgi2a.mysimulation.model;

import java.util.Random;

/**
 * A random value generation strategy based on the java.util.Random java class.
 * @see Random
 */
public class JavaRandomBasedValuesGenerator implements IRandomValuesGenerator  {
	/**
	 * The java.util.Random object generating random numbers in this strategy.
	 */
	private Random javaRandomHelper;

	/**
	 * Builds a random values generation strategy relying on the java Random class.
	 * @param seed The seed used to initialize the java random values generator.
	 */
	public JavaRandomBasedValuesGenerator ( long seed ) {
		this.javaRandomHelper = new Random( seed );
	}

	/**
	 * {@inheritdoc}
	 */
	public double randomDouble( ) {
		return this.javaRandomHelper.nextDouble( );
	}

	/**
	 * {@inheritdoc}
	 */
	public int randomInt( int higherBound ) {
		return this.javaRandomHelper.nextInt( higherBound );
	}
}