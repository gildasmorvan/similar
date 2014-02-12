package fr.lgi2a.similar.microkernel.examples.bubblechamber.model.levels;

import fr.lgi2a.similar.microkernel.LevelIdentifier;

/**
 * The list of levels of a "bubble chamber" simulation.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class BubbleChamberLevelList {
	/**
	 * This constructor is unused since this class only defines static values.
	 * It is declared as protected to prevent the instantiation of this class while 
	 * supporting inheritance.
	 */
	protected BubbleChamberLevelList ( ){ }

	/**
	 * The identifier of the "Chamber" level.
	 */
	public static final LevelIdentifier CHAMBER = new LevelIdentifier( "Chamber" );
	
	/**
	 * The identifier of the "External" level.
	 */
	public static final LevelIdentifier EXTERNAL = new LevelIdentifier( "External" );
}