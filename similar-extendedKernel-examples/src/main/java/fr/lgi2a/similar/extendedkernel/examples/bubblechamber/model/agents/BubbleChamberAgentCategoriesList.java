package fr.lgi2a.similar.extendedkernel.examples.bubblechamber.model.agents;

import fr.lgi2a.similar.microkernel.AgentCategory;

/**
 * The list of levels of a "Bubble chamber" simulation.
 */
public class BubbleChamberAgentCategoriesList {
	/**
	 * This constructor is unused since this class only defines static values.
	 * It is declared as protected to prevent the instantiation of this class while 
	 * supporting inheritance.
	 */
	protected BubbleChamberAgentCategoriesList ( ){ }

	/**
	 * The "Particle" agent category.
	 */
	public static final AgentCategory PARTICLE = new AgentCategory( "Particle" );
	
	/**
	 * The "Bubble" agent category.
	 */
	public static final AgentCategory BUBBLE = new AgentCategory( "Bubble" );

	/**
	 * The "Cannon" agent category.
	 */
	public static final AgentCategory CANNON = new AgentCategory( "Cannon" );
}