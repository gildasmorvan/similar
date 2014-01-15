package fr.lgi2a.mysimulation.model.levels;

import fr.lgi2a.similar.microkernel.LevelIdentifier;

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
	 * The identifier of the "slumbs" level.
	 */
	public static final LevelIdentifier SLUMBS = new LevelIdentifier( "slumbs" );

	/**
	 * The identifier of the "country" level.
	 */
	public static final LevelIdentifier COUNTRY = new LevelIdentifier( "country" );

	/**
	 * The identifier of the "seashore" level.
	 */
	public static final LevelIdentifier SEASHORE = new LevelIdentifier( "seashore" );
}