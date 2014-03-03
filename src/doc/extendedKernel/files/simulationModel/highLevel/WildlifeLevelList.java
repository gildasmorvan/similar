package fr.lgi2a.wildlifesimulation.model.levels;

import fr.lgi2a.similar.microkernel.LevelIdentifier;

/**
 * The list of levels of a wildlife simulation.
 */
public class WildlifeLevelList {
	/**
	 * This constructor is unused since this class only defines static values.
	 * It is declared as protected to prevent the instantiation of this class while 
	 * supporting inheritance.
	 */
	protected WildlifeLevelList ( ){ }

	/**
	 * The identifier of the "Savannah" level.
	 */
	public static final LevelIdentifier SAVANNAH = new LevelIdentifier( "Savannah" );

	/**
	 * The identifier of the "Sky" level.
	 */
	public static final LevelIdentifier SKY = new LevelIdentifier( "Sky" );
	
	/**
	 * The identifier of the "Underground" level.
	 */
	public static final LevelIdentifier UNDERGROUND = new LevelIdentifier( "Underground" );
}