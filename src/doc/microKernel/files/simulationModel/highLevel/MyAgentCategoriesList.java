package fr.lgi2a.mysimulation.model.agents;

import fr.lgi2a.similar.microkernel.AgentCategory;

/**
 * The list of levels of my awesome simulation.
 */
public class MyAgentCategoriesList {
	/**
	 * This constructor is unused since this class only defines static values.
	 * It is declared as protected to prevent the instanciation of this class while 
	 * supporting inheritance.
	 */
	protected MyAgentCategoriesList ( ){ }

	/**
	 * The "car" agent category.
	 */
	public static final AgentCategory CAR = new AgentCategory( "Car" );

	/**
	 * The "pedestrian" agent category.
	 */
	public static final AgentCategory PEDESTRIAN = new AgentCategory( "Pedestrian" );

	/**
	 * The "city bus" agent category.
	 */
	public static final AgentCategory CITY_BUS = new AgentCategory( "City bus" );

	/**
	 * The "market" agent category.
	 */
	public static final AgentCategory MARKET = new AgentCategory( "Market" );
}