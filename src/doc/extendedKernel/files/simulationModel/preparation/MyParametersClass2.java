package fr.univ_artois.lgi2a.mysimulation.model;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractSimulationParameters;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;

/**
 * Models the parameters used in this simulation.
 */
public class MyParametersClass2 extends AbstractSimulationParameters {
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
	public MyParametersClass2( 
		SimulationTimeStamp initialTime
	) {
		super( initialTime );
		this.initialNumberOfAgents = 350;
		this.agentInitialSpeed = 25;
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