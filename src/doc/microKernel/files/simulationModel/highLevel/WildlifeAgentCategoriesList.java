package fr.lgi2a.wildlifesimulation.model.agents;

import fr.lgi2a.similar.microkernel.AgentCategory;

/**
 * The list of levels of a wildlife simulation.
 */
public class WildlifeAgentCategoriesList {
	/**
	 * This constructor is unused since this class only defines static values.
	 * It is declared as protected to prevent the instantiation of this class while 
	 * supporting inheritance.
	 */
	protected WildlifeAgentCategoriesList ( ){ }

	/**
	 * The "Lion" agent category.
	 */
	public static final AgentCategory LION = new AgentCategory( "Lion" );

	/**
	 * The "Vulture" agent category.
	 */
	public static final AgentCategory VULTURE = new AgentCategory( "Vulture" );

	/**
	 * The "Gazelle" agent category.
	 */
	public static final AgentCategory GAZELLE = new AgentCategory( "Gazelle" );

	/**
	 * The "Poacher" agent category.
	 */
	public static final AgentCategory POACHER = new AgentCategory( "Poacher" );

	/**
	 * The "Tourist" agent category.
	 */
	public static final AgentCategory TOURIST = new AgentCategory( "Tourist" );
}