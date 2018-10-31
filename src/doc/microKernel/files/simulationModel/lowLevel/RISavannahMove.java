package fr.univ_artois.lgi2a.wildlifesimulation.model.influences.tosavannah;

import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.influences.RegularInfluence;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.generic.savannah.IAgtPLSInSavannah;
import fr.univ_artois.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;

/**
 * An influence sent by an agent from the "Savannah" level to move towards a direction.
 */
public class RISavannahMove extends RegularInfluence {
	/**
	 * The category of the influence, used as a unique identifier in 
	 * the reaction of the 'Savannah' level to determine the nature of the influence.
	 */
	public static final String CATEGORY = "";
	
	/**
	 * The agent moving in the simulation.
	 */
	private IAgtPLSInSavannah agent;
	/**
	 * The x coordinate of the desired destination of the agent.
	 */
	private double desiredX;
	/**
	 * The y coordinate of the desired destination of the agent.
	 */
	private double desiredY;

	/**
	 * Builds an initialized instance of this influence.
	 * @param timeLowerBound The lower bound of the transitory period during which this influence was created.
	 * @param timeUpperBound The upper bound of the transitory period during which this influence was created.
	 * @param agent The agent moving in the simulation.
	 * @param desiredX The x coordinate of the desired destination of the agent.
	 * @param desiredY The y coordinate of the desired destination of the agent.
	 * @throws IllegalArgumentException If one of the arguments is <code>null</code>.
	 */
	public RISavannahMove(
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound,
			IAgtPLSInSavannah agent,
			double desiredX,
			double desiredY
	) {
		super(
				CATEGORY, 
				WildlifeLevelList.SAVANNAH, 
				timeLowerBound, 
				timeUpperBound
		);
		if( agent == null ){
			throw new IllegalArgumentException(
				"The arguments cannot be null."
			);
		} else {
			this.agent = agent;
			this.desiredX = desiredX;
			this.desiredY = desiredY;
		}
	}
	
	/**
	 * Gets the agent moving in the simulation.
	 * @return The agent moving in the simulation.
	 */
	public IAgtPLSInSavannah getAgent( ){
		return this.agent;
	}
	
	/**
	 * Gets the x coordinate of the desired destination of the agent.
	 * @return The x coordinate of the desired destination of the agent.
	 */
	public double getDesiredX(){
		return this.desiredX;
	}
	
	/**
	 * Gets the y coordinate of the desired destination of the agent.
	 * @return The y coordinate of the desired destination of the agent.
	 */
	public double getDesiredY(){
		return this.desiredY;
	}
}
