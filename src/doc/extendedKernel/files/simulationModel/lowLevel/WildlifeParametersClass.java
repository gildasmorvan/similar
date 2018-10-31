package fr.univ_artois.lgi2a.wildlifesimulation.model;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractSimulationParameters;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;

/**
 * The simulation parameters of the 'wildlife' simulation.
 */
public class WildlifeParametersClass extends AbstractSimulationParameters {
	/**
	 * The perception radius used by the "Lion" agents to perceive the 
	 * "gazelle" agents from the "Savannah" level.
	 */
	public int gazellePercepRadius;
	/**
	 * The lower bound of the gazelle age threshold of the "Lion" agents.
	 * This threshold defines the age over which the gazelles are ignored 
	 * by the predation behavior of the lions.
	 */
	public long gazelleAgeThresholdMin;
	/**
	 * The higher bound of the gazelle age threshold of the "Lion" agents.
	 * This threshold defines the age over which the gazelles are ignored 
	 * by the predation behavior of the lions.
	 */
	public long gazelleAgeThresholdMax;

	/**
	 * Builds a parameters set containing default values.
	 * The values can be changed using the setters of the parameters.
	 * @param initialTime The initial time of the simulation.
	 */
	public WildlifeParametersClass( 
		SimulationTimeStamp initialTime
	) {
		super( initialTime );
		this.gazellePercepRadius = 500;
		this.gazelleAgeThresholdMin = 0;
		this.gazelleAgeThresholdMax = 5;
	}
}
