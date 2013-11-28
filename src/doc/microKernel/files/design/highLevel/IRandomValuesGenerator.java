package fr.lgi2a.mysimulation.model;

/**
 * Defines the random value generation methods that can be used in this simulation.
 */
public interface IRandomValuesGenerator {
	/**
	 * Gets a random number between 0 (included) and 1 (excluded).
	 * @return A random number between 0 (included) and 1 (excluded).
	 */
	double randomDouble( );

	/**
	 * Gets a random integer value between 0 (included) and the specified higher bound (excluded).
	 * @param higherBound The higher bound used for random integer generation.
	 * @return a random integer value between 0 (included) and <code>higherBound</code> (excluded).
	 */
	int randomInt( int higherBound );
}
