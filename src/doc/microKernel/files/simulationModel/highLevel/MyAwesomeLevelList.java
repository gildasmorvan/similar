package fr.univ_artois.lgi2a.mysimulation.model.levels;

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;

/**
 * The list of levels of my awesome simulation.
 */
public class MyAwesomeLevelList {
	/**
	 * This constructor is unused since this class only defines static values.
	 * It is declared as protected to prevent the instanciation of this class while 
	 * supporting inheritance.
	 */
	protected MyAwesomeLevelList ( ){ }

	/**
	 * The identifier of the "city" level.
	 */
	public static final LevelIdentifier CITY = new LevelIdentifier( "city" );

	/**
	 * The identifier of the "slums" level.
	 */
	public static final LevelIdentifier SLUMS = new LevelIdentifier( "slums" );

	/**
	 * The identifier of the "country" level.
	 */
	public static final LevelIdentifier COUNTRY = new LevelIdentifier( "country" );

	/**
	 * The identifier of the "seashore" level.
	 */
	public static final LevelIdentifier SEASHORE = new LevelIdentifier( "seashore" );
}