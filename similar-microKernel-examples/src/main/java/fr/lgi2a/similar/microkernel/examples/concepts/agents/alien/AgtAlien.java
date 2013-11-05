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
package fr.lgi2a.similar.microkernel.examples.concepts.agents.alien;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IAgent;
import fr.lgi2a.similar.microkernel.IDynamicStateMap;
import fr.lgi2a.similar.microkernel.IGlobalMemoryState;
import fr.lgi2a.similar.microkernel.IPerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.IPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.InfluencesMap;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationLevelIdentifiers;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationRandom;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.alien.physical.AgtAlienPDFPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.alien.physical.AgtAlienPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.alien.space.AgtAlienPDFSpace;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.citizen.physical.AgtCitizenPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.Cities;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.EnvPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.toPhysical.RIPhysicalLandOnEarth;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.toPhysical.RIPhysicalPerformExperiment;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.toPhysical.RIPhysicalTakeOffFromEarth;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.toSpace.RISpaceSendExperimentReport;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractAgent;
import fr.lgi2a.similar.microkernel.libs.generic.EmptyGlobalMemoryState;
import fr.lgi2a.similar.microkernel.libs.generic.EmptyPublicLocalStateOfAgent;

/**
 * Models instances of 'Alien' agents.
 * 
 * <h1>Naming convention</h1>
 * In the name of the class:
 * <ul>
 * 	<li>"Agt" stands for "Agent"</li>
 * </ul>
 * These rules were defined to reduce the size of the name of the class.
 * 
 * <h1>Agents in the SIMILAR API suite.</h1>
 * <p>
 * 	In the micro-kernel of SIMILAR, an agent is implemented as an 
 * 	instance of either the {@link IAgent} interface, or of the {@link AbstractAgent} 
 * 	abstract class.
 * 	In this example, we use the abstract class which is easier to implement.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class AgtAlien extends AbstractAgent {
	/**
	 * The category of this agent class (telling that the agent is an 'Alien' agent).
	 * <p>
	 * 	This category is defined as a static value to facilitate the identification of the nature of the
	 * 	agents for instance when data about the agent are printed on screen.
	 * </p>
	 */
	public static final String CATEGORY = "Alien";
	
	/**
	 * Builds an 'Alien' agent initially lying in the 'space' level.
	 * @param perceptibleCitiesPerTime This parameter of the agent tells how much cities it is able to scrutinize each time it 
	 * perceives from the 'space' level.
	 * <p>
	 * 	If no citizen could be found after looking at <code>perceptibleCitiesPerTime</code> cities, then the alien failed to find
	 * 	a citizen to perform experiments that time.
	 * </p>
	 * @param efficiencyInExperiments Models the efficiency of the alien in its experiments. This value has to be 
	 * between 0 (completely inefficient) to 1 (fully efficient). It will be used as a multiplication factor when 
	 * the reaction will determine how much the experiment of the alien advanced.
	 */
	public AgtAlien( 
			int perceptibleCitiesPerTime,
			double efficiencyInExperiments
	) {
		// The super constructor requires the definition of the category of the agent.
		super( CATEGORY );
		//
		// Define the initial private local states of the agent.
		//
		this.perceptibleCitiesPerTime = perceptibleCitiesPerTime;
		this.efficiencyInExperiments = efficiencyInExperiments;
		//
		// Define the initial global memory state of the agent.
		//
		// No specific data are required in that state for the 'alien' agent. Thus, instead of defining a new class,
		// we use the generic class EmptyGlobalMemoryState defined in the common libs of SIMILAR.
		// This class models an empty global memory state for an agent.
		this.initializeGlobalMemoryState( new EmptyGlobalMemoryState( this ) );
		//
		// Tell that this agent is initially in the 'space' level.
		//
		// No specific data about this agent have be perceptible in the 'space' level. Thus, instead of defining a new class,
		// we use the generic class EmptyPublicLocalStateOfAgent defined in the common libs of SIMILAR.
		// This class models an empty public local state for an agent.
		IPublicLocalStateOfAgent stateInSpaceLevel = new EmptyPublicLocalStateOfAgent( ConceptsSimulationLevelIdentifiers.SPACE_LEVEL, this );
		// Specify that the agent lies is initially present in the 'space' level.
		this.includeNewLevel( ConceptsSimulationLevelIdentifiers.SPACE_LEVEL, stateInSpaceLevel );
	}

	// // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
	//
	//
	//	PERCEPTION RELATED METHODS
	//
	//
	//
	
	/**
	 * This method defines the data being perceived by the agent, depending on the level from which perception is made.
	 * @param levelId The identifier of the level from which the perception is made.
	 * @param The public local state of this agent in the level which identifier is <code>levelId</code>.
	 * @param levelsPublicLocalObservableDynamicState The public dynamic state of the levels that can be perceived from 
	 * the level having the identifier <code>levelId</code>.
	 */
	@Override
	public IPerceivedDataOfAgent perceive(
			LevelIdentifier levelId,
			IPublicLocalStateOfAgent publicLocalStateInLevel,
			IDynamicStateMap levelsPublicLocalObservableDynamicState
	) {
		// Dispatch the method call depending on the level from which perception is made.
		if( ConceptsSimulationLevelIdentifiers.SPACE_LEVEL.equals( levelId ) ){
			// Case where the perception is made from the 'Space' level.
			// Dispatch to the appropriate method.
			return this.perceptionForSpaceLevel( levelsPublicLocalObservableDynamicState );
		} else if( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL.equals( levelId ) ) {
			// Case where the perception is made from the 'Physical' level.
			// Dispatch to the appropriate method.
			return this.perceptionForPhysicalLevel( publicLocalStateInLevel );
		} else {
			// This case is out of the bounds of the behavior of the agent.
			// Consequently, we throw an exception telling that this case should not happen
			// in an appropriate simulation.
			throw new UnsupportedOperationException( "Cannot perceive from the level '" + levelId + "'." );
		}
	}

	/**
	 * This parameter of the agent tells how much cities it is able to scrutinize each time it perceives from the 'space' level.
	 * <p>
	 * 	If no citizen could be found after looking at <code>perceptibleCitiesPerTime</code> cities, then the alien failed to find
	 * 	a citizen to perform experiments that time.
	 * </p>
	 * <p>
	 * 	This value is part of the private local state of the agent. Since it purely belongs to the inner working of the behavior of the 
	 * 	agent from the 'space' level, it is declared here rather than in the public local state or the global memory state of the agent.
	 * </p>
	 */
	private int perceptibleCitiesPerTime;
	
	/**
	 * Produce the perceived data of the agent in the case when the perception is made from the 'space' level.
	 * <p>
	 * 	From that level, the 'alien' agent looks for a citizen at random in a random city from the 'physical' level.
	 * </p>
	 * @param levelsPublicLocalObservableDynamicState The public dynamic state of the levels that can be perceived from 
	 * the 'space' level.
	 * @return The data that were perceived by the 'alien' for its decisions from the 'space' level.
	 */
	private IPerceivedDataOfAgent perceptionForSpaceLevel(
			IDynamicStateMap levelsPublicLocalObservableDynamicState
	) {
		//
		// First get the public local state of the environment in the 'physical' state, where the
		// citizen are grouped by cities.
		//
		IPublicLocalDynamicState publicLocalDynamicStateOfPhysicalLevel = 
				levelsPublicLocalObservableDynamicState.get( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL );
		EnvPLSPhysical environmentInPhysicalLevel = (EnvPLSPhysical) publicLocalDynamicStateOfPhysicalLevel.getPublicLocalStateOfEnvironment();
		//
		// Then perceive inside that public local state.
		//
		// The data that were perceived by the agent. As long as they are null, the alien did not find a city containing 
		// at least one citizen.
		AgtAlienPDFSpace perceivedData = null;
		// Create a list from which cities are randomly picked until a city containing a citizen is chosen.
		List<Cities> cityCandidates = new LinkedList<Cities>();
		Collections.addAll( cityCandidates, Cities.values() );
		// The tries remaining before the alien fails to find a citizen.
		int remainingTries = this.perceptibleCitiesPerTime;
		while( perceivedData == null && remainingTries > 0 && ! cityCandidates.isEmpty() ) {
			int listIndex = ConceptsSimulationRandom.randomInt( cityCandidates.size() );
			Cities city = cityCandidates.remove( listIndex );
			Set<AgtCitizenPLSPhysical> citizensFromCity = environmentInPhysicalLevel.getCitizensAt( city );
			if( ! citizensFromCity.isEmpty() ){
				// In this case, the citizens are candidates for experimentations
				perceivedData = new AgtAlienPDFSpace( city, citizensFromCity );
			}
			remainingTries--;
		}
		if( perceivedData != null ){
			return perceivedData;
		} else {
			// The alien did not find eligible citizen for experiments.
			return new AgtAlienPDFSpace( );
		}
	}
	
	/**
	 * Produce the perceived data of the agent in the case when the perception is made from the 'physical' level.
	 * <p>
	 * 	From that level, the 'alien' agent perceives whether if it has finished its experiments or not.
	 * </p>
	 * @param The public local state of this agent in the 'physical' level.
	 * @return The data that were perceived by the 'alien' for its decisions from the 'physical' level.
	 */
	private IPerceivedDataOfAgent perceptionForPhysicalLevel(
			IPublicLocalStateOfAgent publicLocalStateInLevel
	) {
		// The 'alien' needs to perceive:
		//	- whether if its current experiment has finished or not.
		//	- who is its experimental subject.
		AgtAlienPLSPhysical castedStateInLevel = (AgtAlienPLSPhysical) publicLocalStateInLevel;
		return new AgtAlienPDFPhysical( 
				castedStateInLevel.hasStartedExperiments(),
				castedStateInLevel.hasFinishedExperiments(), 
				castedStateInLevel.getExperimentSubject() 
		);
	}

	// // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
	//
	//
	//	MEMORY REVISION RELATED METHODS
	//
	//
	//

	/**
	 * Defines how the agent uses the data that were perceived to revise the content of its
	 * global memory state.
	 * <p>
	 * 	Since this agent does not use the global memory state, this method does nothing.
	 * </p>
	 */
	@Override
	public void reviseMemory(
			Map<LevelIdentifier, IPerceivedDataOfAgent> perceivedData,
			IGlobalMemoryState memoryState
	) { }

	// // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
	//
	//
	//	DECISION RELATED METHODS
	//
	//
	//
	
	/**
	 * This method defines the influences that are produced by the decisions made by this agent, when it performs a decision
	 * from a specific level.
	 * @param levelId The identifier of the level from which a decision is made.
	 * @param memoryState The global memory state of the agent when the decision is made.
	 * @param perceivedData The data that were lastly perceived from the <code>levelId</code> by the agent.
	 * @param producedInfluences The map where the influences produced by the decisions of this agent are put.
	 */
	@Override
	public void decide(
			LevelIdentifier levelId, 
			IGlobalMemoryState memoryState,
			IPerceivedDataOfAgent perceivedData,
			InfluencesMap producedInfluences
	) {
		// Dispatch the method call depending on the level from which decision is made.
		if( ConceptsSimulationLevelIdentifiers.SPACE_LEVEL.equals( levelId ) ){
			// Case where the decision is made from the 'Space' level.
			// Dispatch to the appropriate method.
			this.decideFromSpaceLevel( perceivedData, producedInfluences );
		} else if( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL.equals( levelId ) ) {
			// Case where the decision is made from the 'Physical' level.
			// Dispatch to the appropriate method.
			this.decideFromPhysicalLevel( perceivedData, producedInfluences );
		} else {
			// This case is out of the bounds of the behavior of the agent.
			// Consequently, we throw an exception telling that this case should not happen
			// in an appropriate simulation.
			throw new UnsupportedOperationException( "Cannot decide from the level '" + levelId + "'." );
		}
	}

	/**
	 * Produce the influences resulting from the decisions made from the 'space' level.
	 * <p>
	 * 	From that level, the 'alien' picks a citizen from the perceived data and lands on earth to perform experiments on it.
	 * </p>
	 * @param perceivedData The data that were perceived from the 'space' level, containing the candidates for the experiments.
	 * @param producedInfluences The map where the influences produced by the decisions of this agent are put.
	 */
	private void decideFromSpaceLevel( 
			IPerceivedDataOfAgent perceivedData,
			InfluencesMap producedInfluences
	) {
		AgtAlienPDFSpace spaceData = (AgtAlienPDFSpace) perceivedData;
		// If the alien found test subjects, it chooses a test subject and lands on earth to perform the tests.
		if( spaceData.hasFoundCandidatesForExperimentation() ){
			// Get at random the index in the set of the experiment subject.
			int specimenIndexInCollection = ConceptsSimulationRandom.randomInt( spaceData.getCandidatesForExperimentation().size() );
			// Find the 'citizen' located at the specified index in the candidates collection.
			Iterator<AgtCitizenPLSPhysical> specimenIterator = spaceData.getCandidatesForExperimentation().iterator();
			while( specimenIndexInCollection != 0 && specimenIterator.hasNext() ){
				specimenIterator.next();
				specimenIndexInCollection--;
			}
			AgtCitizenPLSPhysical specimen = specimenIterator.next();
			// Then create and add the influence to the influences produced by the decision.
			RIPhysicalLandOnEarth influence = new  RIPhysicalLandOnEarth( this, specimen );
			producedInfluences.add( influence );
		}
	}
	
	/**
	 * Models the efficiency of the alien in its experiments.
	 * <p>
	 * 	This value has to be between 0 (completely inefficient) to 1 (fully efficient).
	 * 	It will be used as a multiplication factor when the reaction will determine how much the experiment of the 
	 * 	alien advanced.
	 * </p>
	 * <p>
	 * 	This value is part of the private local state of the agent. Since it purely belongs to the inner working of the behavior of the 
	 * 	agent from the 'physical' level, it is declared here rather than in the public local state or the global memory state of the agent.
	 * </p>
	 */
	private double efficiencyInExperiments;
	
	/**
	 * Produce the influences resulting from the decisions made from the 'physical' level.
	 * <p>
	 * 	From that level, the alien proceeds its experiments unless the experiments have finished.
	 * 	Once finished, the alien leaves the earth and sends an experiment report to its mothership.
	 * </p>
	 * @param perceivedData The data that were perceived from the 'physical' level, containing the candidates for the experiments.
	 * @param producedInfluences The map where the influences produced by the decisions of this agent are put.
	 */
	private void decideFromPhysicalLevel(
			IPerceivedDataOfAgent perceivedData,
			InfluencesMap producedInfluences
	){
		AgtAlienPDFPhysical physicalData = (AgtAlienPDFPhysical) perceivedData;
		// If the alien has not started its experiment, it has to start it.
		if( ! physicalData.hasStartedExperiments() ){
			// Create and add the 'perform experiment' influence to the influences produced by the decision.
			// This influence will persist in the state dynamics of the 'physical' level until the end of the experiment.
			RIPhysicalPerformExperiment experimentInfluence = new RIPhysicalPerformExperiment(
					(AgtAlienPLSPhysical) this.getPublicLocalState( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL ), 
					physicalData.getExperimentSubject(), 
					this.efficiencyInExperiments
			);
			producedInfluences.add( experimentInfluence );
		}
		// If the alien finished its experiment, it has to take off and send a report.
		if( physicalData.hasFinishedExperiments() ){
			// Create and add the 'take off' influence to the influences produced by the decision.
			RIPhysicalTakeOffFromEarth takeOffInfluence = new RIPhysicalTakeOffFromEarth( this );
			producedInfluences.add( takeOffInfluence );
			// Also create and add the 'report experiment to mothership' influence to the influences produced by the decision.
			RISpaceSendExperimentReport sendReportInfluence = new RISpaceSendExperimentReport( physicalData.getExperimentSubject() );
			producedInfluences.add( sendReportInfluence );
		}
	}
}
