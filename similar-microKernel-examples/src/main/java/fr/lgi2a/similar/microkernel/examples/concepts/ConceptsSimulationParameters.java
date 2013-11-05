/**
 * Copyright or © or Copr. LGI2A
 * 
 * LGI2A - Laboratoire de Genie Informatique et d'Automatique de l'Artois - EA 3926 
 * Faculte des Sciences Appliquees
 * Technoparc Futura
 * 62400 - BETHUNE Cedex
 * http://www.lgi2a.univ-artois.fr/
 * 
 * Email: gildas.morvan@univ-artois.fr
 * 
 * Contributors:
 * 	Gildas MORVAN (creator of the IRM4MLS formalism)
 * 	Yoann KUBERA (designer, architect and developer of SIMILAR)
 * 
 * This software is a computer program whose purpose is to support the
 * implementation of multi-agent-based simulations using the formerly named
 * IRM4MLS meta-model. This software defines an API to implement such 
 * simulations, and also provides usage examples.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.lgi2a.similar.microkernel.examples.concepts;

import java.util.HashMap;
import java.util.Map;


/**
 * Models the parameters used in this simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class ConceptsSimulationParameters {
	/**
	 * Defines the speed of an alien experiment.
	 * The unit is a percentage (within range ]0,100]) per reaction.
	 */
	private int alienExperimentSpeed;
	/**
	 * Models how much cities an alien is able to scrutinize each time it perceives from the 'Space' level.
	 * <p>
	 * 	If no citizen could be found after looking at <code>alienCitiesPerPerception</code> cities, then the alien failed to find
	 * 	a citizen to perform experiments that time.
	 * </p>
	 */
	private int alienCitiesPerPerception;
	/**
	 * Models the efficiency of an alien in its experiments. This value has to be 
	 * between 0 (completely inefficient) to 1 (fully efficient). It will be used as a multiplication factor when 
	 * the reaction will determine how much the experiment of the alien advanced.
	 */
	private double alienExperimentsEfficiency;
	/**
	 * The apparition rate of strange physical manifestations on the body of citizen when an alien performs 
	 * an experiment on them.
	 * <p>
	 * 	This value has to be between 0 (included) and 1 (excluded) and models a percentage.
	 * </p>
	 */
	private double citizenStrangePhysicalManifestationsApparitionRate;
	/**
	 * The data structure modeling how the time evolves in the 'Space' level. To simulate a unpredictable evolution, the evolution of time is based 
	 * on a map associating an increment value I(x) to the values x from an interval [0,N] of integers. The identifier of the time stamp following 
	 * a time stamp t is computed using the formula: <i>id(t+dt) = id(t) + I( t mod N )</i>.
	 */
	private Map<Long,Long> timeEvolutionDescriptorOfSpaceLevel;
	/**
	 * The number of strange physical manifestation over which a citizen can consider that an alien experiment 
	 * was performed on him/her. This value is being broadcasted on television and corresponds to the value 
	 * advised by the FBI.
	 */
	private int fbiAdvisedThreshold;
	/**
	 * The threshold of citizens posts number before a citizen is sent to lobotomy by the FBI.
	 */
	private int fbiThresholdBeforeCitizenLobotomy;
	/**
	 * Models the efficiency of the FBI to capture alien. This value has to be 
	 * between 0 (completely inefficient) to 1 (fully efficient). It determines the success chances of
	 * a capture influence sent by the FBI.
	 */
	private double fbiCaptureEfficiency;
	/**
	 * The number of experiments being reported in the city of the editor in chief before it becomes paranoid and 
	 * broadcasts values that are not advised by the FBI.
	 */
	private int editorInChiefParanoiaThreshold;
	/**
	 * Models the range where the number of citizen initially lying in a city is included. The bounds of the interval are included.
	 * This interval is used during the generation of the simulation.
	 */
	private int[] rangeOfCitizenPerCity;
	/**
	 * The number of aliens initially in the simulation.
	 */
	private int aliensNumber;
	
	/**
	 * Builds a parameters set containing default values.
	 * The values can be changed using the setters of the parameters.
	 */
	public ConceptsSimulationParameters( ) {
		this.alienExperimentSpeed = 25;
		this.alienCitiesPerPerception = 2;
		this.alienExperimentsEfficiency = 0.5;
		this.citizenStrangePhysicalManifestationsApparitionRate = 0.5;
		this.timeEvolutionDescriptorOfSpaceLevel = new HashMap<Long, Long>();
		this.timeEvolutionDescriptorOfSpaceLevel.put( 0l, 2l );
		this.timeEvolutionDescriptorOfSpaceLevel.put( 1l, 3l );
		this.timeEvolutionDescriptorOfSpaceLevel.put( 2l, 1l );
		this.timeEvolutionDescriptorOfSpaceLevel.put( 3l, 4l );
		this.timeEvolutionDescriptorOfSpaceLevel.put( 4l, 3l );
		this.fbiAdvisedThreshold = 4;
		this.fbiThresholdBeforeCitizenLobotomy = 15;
		this.fbiCaptureEfficiency = 0.05;
		this.editorInChiefParanoiaThreshold = 10;
		this.rangeOfCitizenPerCity = new int[]{ 0, 6 };
		this.aliensNumber = 10;
	}

	/**
	 * Gets the speed of an alien experiment.
	 * The unit is a percentage (within range ]0,100]) per reaction.
	 * @return The speed of an alien experiment.
	 */
	public int getAlienExperimentSpeed() {
		return alienExperimentSpeed;
	}

	/**
	 * Sets the speed of an alien experiment.
	 * The unit is a percentage (within range ]0,100]) per reaction.
	 * @param alienExperimentSpeed The speed of an alien experiment.
	 */
	public void setAlienExperimentSpeed(int alienExperimentSpeed) {
		this.alienExperimentSpeed = alienExperimentSpeed;
	}

	/**
	 * Gets how much cities an alien is able to scrutinize each time it perceives from the 'Space' level.
	 * <p>
	 * 	If no citizen could be found after looking at <code>alienCitiesPerPerception</code> cities, then the alien failed to find
	 * 	a citizen to perform experiments that time.
	 * </p>
	 * @return How much cities an alien is able to scrutinize each time it perceives from the 'Space' level.
	 */
	public int getAlienCitiesPerPerception() {
		return alienCitiesPerPerception;
	}

	/**
	 * Sets how much cities an alien is able to scrutinize each time it perceives from the 'Space' level.
	 * <p>
	 * 	If no citizen could be found after looking at <code>alienCitiesPerPerception</code> cities, then the alien failed to find
	 * 	a citizen to perform experiments that time.
	 * </p>
	 * @param alienCitiesPerPerception How much cities an alien is able to scrutinize each time it perceives from the 'Space' level.
	 */
	public void setAlienCitiesPerPerception(int alienCitiesPerPerception) {
		this.alienCitiesPerPerception = alienCitiesPerPerception;
	}

	/**
	 * Gets the efficiency of an alien in its experiments. This value has to be 
	 * between 0 (completely inefficient) to 1 (fully efficient). It will be used as a multiplication factor when 
	 * the reaction will determine how much the experiment of the alien advanced.
	 * @return The efficiency of an alien in its experiments.
	 */
	public double getAlienExperimentsEfficiency() {
		return alienExperimentsEfficiency;
	}

	/**
	 * Sets the efficiency of an alien in its experiments. This value has to be 
	 * between 0 (completely inefficient) to 1 (fully efficient). It will be used as a multiplication factor when 
	 * the reaction will determine how much the experiment of the alien advanced.
	 * @param alienExperimentsEfficiency The efficiency of an alien in its experiments.
	 */
	public void setAlienExperimentsEfficiency(double alienExperimentsEfficiency) {
		this.alienExperimentsEfficiency = alienExperimentsEfficiency;
	}

	/**
	 * Gets the apparition rate of strange physical manifestations on the body of citizen when an alien performs 
	 * an experiment on them.
	 * <p>
	 * 	This value has to be between 0 (included) and 1 (excluded) and models a percentage.
	 * </p>
	 * @return The apparition rate of strange physical manifestations on the body of citizen when an alien performs 
	 * an experiment on them.
	 */
	public double getCitizenStrangePhysicalManifestationsApparitionRate() {
		return citizenStrangePhysicalManifestationsApparitionRate;
	}

	/**
	 * Sets the apparition rate of strange physical manifestations on the body of citizen when an alien performs 
	 * an experiment on them.
	 * <p>
	 * 	This value has to be between 0 (included) and 1 (excluded) and models a percentage.
	 * </p>
	 * @param citizenStrangePhysicalManifestationsApparitionRate The apparition rate of strange physical manifestations on the body of citizen when an alien performs 
	 * an experiment on them.
	 */
	public void setCitizenStrangePhysicalManifestationsApparitionRate(
			double citizenStrangePhysicalManifestationsApparitionRate) {
		this.citizenStrangePhysicalManifestationsApparitionRate = citizenStrangePhysicalManifestationsApparitionRate;
	}

	/**
	 * Gets how the times of the level 'space' moves with a non-uniform pattern. This pattern models 
	 * that the actions of aliens cannot be foretold.
	 * This map uses as keys the long values contained in the interval [0,N-1], where N is the number of keys in the map.
	 * The values model the increment applied between a current time and the next time.
	 * The identifier of the current time is converted into a value contained in the range [0,N] using the modulo operator.
	 * @return The evolution pattern of the time in the 'Space' level.
	 */
	public Map<Long, Long> getTimeEvolutionDescriptorOfSpaceLevel() {
		return timeEvolutionDescriptorOfSpaceLevel;
	}

	/**
	 * Sets how the times of the level 'space' moves with a non-uniform pattern. This pattern models 
	 * that the actions of aliens cannot be foretold.
	 * This map uses as keys the long values contained in the interval [0,N-1], where N is the number of keys in the map.
	 * The values model the increment applied between a current time and the next time.
	 * The identifier of the current time is converted into a value contained in the range [0,N] using the modulo operator.
	 * @param timeEvolutionDescriptorOfSpaceLevel The evolution pattern of the time in the 'Space' level.
	 */
	public void setTimeEvolutionDescriptorOfSpaceLevel(
			Map<Long, Long> timeEvolutionDescriptorOfSpaceLevel) {
		this.timeEvolutionDescriptorOfSpaceLevel = timeEvolutionDescriptorOfSpaceLevel;
	}

	/**
	 * Gets the number of strange physical manifestation over which a citizen can consider that an alien experiment 
	 * was performed on him/her. This value is being broadcasted on television and corresponds to the value 
	 * advised by the FBI.
	 * @return The number of strange physical manifestation over which a citizen can consider that an alien experiment 
	 * was performed on him/her.
	 */
	public int getFbiAdvisedThreshold() {
		return fbiAdvisedThreshold;
	}

	/**
	 * Sets the number of strange physical manifestation over which a citizen can consider that an alien experiment 
	 * was performed on him/her. This value is being broadcasted on television and corresponds to the value 
	 * advised by the FBI.
	 * @param fbiAdvisedThreshold The number of strange physical manifestation over which a citizen can consider that an alien experiment 
	 * was performed on him/her.
	 */
	public void setFbiAdvisedThreshold(int fbiAdvisedThreshold) {
		this.fbiAdvisedThreshold = fbiAdvisedThreshold;
	}

	/**
	 * Gets the threshold of citizens posts number before a citizen is sent to lobotomy by the FBI.
	 * @return The threshold of citizens posts number before a citizen is sent to lobotomy by the FBI.
	 */
	public int getFbiThresholdBeforeCitizenLobotomy() {
		return fbiThresholdBeforeCitizenLobotomy;
	}

	/**
	 * Sets the threshold of citizens posts number before a citizen is sent to lobotomy by the FBI.
	 * @param fbiThresholdBeforeCitizenLobotomy The threshold of citizens posts number before a citizen is sent to lobotomy by the FBI.
	 */
	public void setFbiThresholdBeforeCitizenLobotomy(
			int fbiThresholdBeforeCitizenLobotomy) {
		this.fbiThresholdBeforeCitizenLobotomy = fbiThresholdBeforeCitizenLobotomy;
	}

	/**
	 * Gets the efficiency of the FBI to capture alien. This value has to be 
	 * between 0 (completely inefficient) to 1 (fully efficient). It determines the success chances of
	 * a capture influence sent by the FBI.
	 * @return The efficiency of the FBI to capture alien.
	 */
	public double getFbiCaptureEfficiency() {
		return fbiCaptureEfficiency;
	}

	/**
	 * Sets the efficiency of the FBI to capture alien. This value has to be 
	 * between 0 (completely inefficient) to 1 (fully efficient). It determines the success chances of
	 * a capture influence sent by the FBI.
	 * @param fbiCaptureEfficiency The efficiency of the FBI to capture alien.
	 */
	public void setFbiCaptureEfficiency(double fbiCaptureEfficiency) {
		this.fbiCaptureEfficiency = fbiCaptureEfficiency;
	}

	/**
	 * Gets the number of experiments being reported in the city of the editor in chief before it becomes paranoid and 
	 * broadcasts values that are not advised by the FBI.
	 * @return The number of experiments being reported in the city of the editor in chief before it becomes paranoid and 
	 * broadcasts values that are not advised by the FBI.
	 */
	public int getEditorInChiefParanoiaThreshold() {
		return editorInChiefParanoiaThreshold;
	}

	/**
	 * Sets the number of experiments being reported in the city of the editor in chief before it becomes paranoid and 
	 * broadcasts values that are not advised by the FBI.
	 * @param editorInChiefParanoiaThreshold The number of experiments being reported in the city of the editor in chief before it becomes paranoid and 
	 * broadcasts values that are not advised by the FBI.
	 */
	public void setEditorInChiefParanoiaThreshold(int editorInChiefParanoiaThreshold) {
		this.editorInChiefParanoiaThreshold = editorInChiefParanoiaThreshold;
	}

	/**
	 * Gets the range where the number of citizen initially lying in a city is included. The bounds of the interval are included.
	 * This interval is used during the generation of the simulation.
	 * @return The range where the number of citizen initially lying in a city is included.
	 */
	public int[] getRangeOfCitizenPerCity() {
		return rangeOfCitizenPerCity;
	}

	/**
	 * Sets the range where the number of citizen initially lying in a city is included. The bounds of the interval are included.
	 * This interval is used during the generation of the simulation.
	 * @param citizenPerCityRange The range where the number of citizen initially lying in a city is included.
	 */
	public void setRangeOfCitizenPerCity(int[] citizenPerCityRange) {
		this.rangeOfCitizenPerCity = citizenPerCityRange;
	}

	/**
	 * Gets the number of aliens initially in the simulation.
	 * @return The number of aliens initially in the simulation.
	 */
	public int getAliensNumber() {
		return aliensNumber;
	}

	/**
	 * Sets the number of aliens initially in the simulation.
	 * @param aliensNumber The number of aliens initially in the simulation.
	 */
	public void setAliensNumber(int aliensNumber) {
		this.aliensNumber = aliensNumber;
	}
}
