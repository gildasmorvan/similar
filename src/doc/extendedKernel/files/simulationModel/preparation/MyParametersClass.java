package fr.univ_artois.lgi2a.mysimulation.model;

import fr.univ_artois.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;

/**
 * Models the parameters used in this simulation.
 */
public class MyParametersClass implements ISimulationParameters {
	/**
	 * Defines the initial time of the simulation.
	 */
	private SimulationTimeStamp initialTime;
	/**
	 * Defines the initial number of agents.
	 */
	private int initialNumberOfAgents;
	/**
	 * Defines the initial speed of an agent.
	 */
	private double agentInitialSpeed;

	/**
	 * Builds a parameters set containing default values.
	 * The values can be changed using the setters of the parameters.
	 * @param initialTime The initial time of the simulation.
	 */
	public MyParametersClass( 
		SimulationTimeStamp initialTime
	) {
		if( initialTime == null ){
			throw new IllegalArgumentException(
				"The initial time cannot be null."
			);
		}
		this.initialNumberOfAgents = 350;
		this.agentInitialSpeed = 25;
		this.initialTime = initialTime;
	}
	
	/**
	 * {@inheritdoc}
	 */
	@Override
	public SimulationTimeStamp getInitialTime(){
		return this.initialTime;
	}

	/**
	 * Gets the initial number of agents.
	 * @return The initial number of agents.
	 */
	public int getInitialNumberOfAgents( ){
		return this.initialNumberOfAgents;
	}

	/**
	 * Sets the initial number of agents.
	 * @param initialNumberOfAgents The initial number of agents.
	 */
	public void setInitialNumberOfAgents( int initialNumberOfAgents ){
		this.initialNumberOfAgents = initialNumberOfAgents ;
	}

	/**
	 * Gets the initial speed of an agent.
	 * @return The initial speed of an agent.
	 */
	public double getAgentInitialSpeed( ) {
		return this.agentInitialSpeed;
	}

	/**
	 * Sets the initial speed of an agent.
	 * @param agentInitialSpeed The initial speed of an agent.
	 */
	public void setAgentInitialSpeed( double agentInitialSpeed ){
		this.agentInitialSpeed  = agentInitialSpeed ;
	}
}