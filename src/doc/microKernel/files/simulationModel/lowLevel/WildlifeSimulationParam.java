package fr.lgi2a.wildlifesimulation.model;

/**
 * The simulation parameters of the 'wildlife' simulation.
 */
public class WildlifeSimulationParam {
	/**
	 * The perception radius used by the "Lion" agents to perceive the 
	 * "gazelle" agents from the "Savannah" level.
	 */
	public int gazellePercepRadius = 500;
	/**
	 * The lower bound of the gazelle age threshold of the "Lion" agents.
	 * This threshold defines the age over which the gazelles are ignored 
	 * by the predation behavior of the lions.
	 */
	public long gazelleAgeThresholdMin = 0;
	/**
	 * The higher bound of the gazelle age threshold of the "Lion" agents.
	 * This threshold defines the age over which the gazelles are ignored 
	 * by the predation behavior of the lions.
	 */
	public long gazelleAgeThresholdMax = 5;
}
