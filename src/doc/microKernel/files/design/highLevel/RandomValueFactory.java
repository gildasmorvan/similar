package fr.lgi2a.mysimulation.model;

/**
 * The random values factory used in the simulation.
 * <p>
 *	By default, this factory uses a strategy based on a java.util.Random instance using the seed "1000".
 * </p>
 */
public class RandomValueFactory {
	/**
	 * The random values generation strategy currently used in the simulation.
	 * The default strategy is based on a java.util.Random instance using the seed "1000".
	 */
	private static IRandomValuesGenerator INSTANCE = new JavaRandomBasedValuesGenerator( 1000 );

	/**
	 * Sets the random value generation strategy used in the simulation.
	 * @param strategy The random value generation strategy used in the simulation.
	 */
	public static void setStrategy( IRandomValuesGenerator  strategy ) {
		if( strategy != null ) {
			INSTANCE = strategy ;
		}
	}

	/**
	 * Gets the random value generation strategy used in the simulation.
	 */
	public IRandomValuesGenerator getStrategy( ) {
		return INSTANCE;
	}
}