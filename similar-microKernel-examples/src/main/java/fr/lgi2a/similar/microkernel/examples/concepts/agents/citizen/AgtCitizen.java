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
package fr.lgi2a.similar.microkernel.examples.concepts.agents.citizen;

import java.util.Map;

import fr.lgi2a.similar.microkernel.IAgent;
import fr.lgi2a.similar.microkernel.IDynamicStateMap;
import fr.lgi2a.similar.microkernel.IGlobalMemoryState;
import fr.lgi2a.similar.microkernel.IPerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.InfluencesMap;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationLevelIdentifiers;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationRandom;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.citizen.physical.AgtCitizenPDFPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.citizen.physical.AgtCitizenPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.Cities;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.EnvPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.social.EnvPLSSocial;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.social.PostOnConspiracyForum;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.tophysical.RIPhysicalGoToWork;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.tosocial.RISocialPublishExperimentReport;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractAgent;

/**
 * Models instances of 'Citizen' agents.
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
public class AgtCitizen extends AbstractAgent {
	/**
	 * The category of this agent class (telling that the agent is an 'Citizen' agent).
	 * <p>
	 * 	This category is defined as a static value to facilitate the identification of the nature of the
	 * 	agents for instance when data about the agent are printed on screen.
	 * </p>
	 */
	public static final String CATEGORY = "Citizen";
	
	/**
	 * Builds a 'Citizen' agent initially lying in the 'physical' level.
	 * @param address The address of the citizen in the 'physical' level.
	 */
	public AgtCitizen( Cities address ) {
		// The super constructor requires the definition of the category of the agent.
		super( CATEGORY );
		//
		// Define the initial global memory state of the agent.
		//
		this.initializeGlobalMemoryState( new AgtCitizenGMS( this ) );
		//
		// Tell that this agent is initially in the 'physical' level.
		//
		this.includeNewLevel( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL, new AgtCitizenPLSPhysical( this, address ) );
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
	 * @param publicLocalStateInLevel The public local state of this agent in the level which identifier is <code>levelId</code>.
	 * @param levelsPublicLocalObservableDynamicState The public dynamic state of the levels that can be perceived from 
	 * the level having the identifier <code>levelId</code>.
	 */
	@Override
	public IPerceivedDataOfAgent perceive(
			LevelIdentifier levelId,
			IPublicLocalStateOfAgent publicLocalStateInLevel,
			IDynamicStateMap levelsPublicLocalObservableDynamicState
	) {
		if( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL.equals( levelId ) ) {
			// Case where the perception is made from the 'Physical' level.
			//
			// First cast the public local state of the agent in the 'physical' level and the public
			// local state of the environment in the 'social' level.
			AgtCitizenPLSPhysical agtStatePhysical = (AgtCitizenPLSPhysical) publicLocalStateInLevel;
			EnvPLSSocial envStateSocial = (EnvPLSSocial) levelsPublicLocalObservableDynamicState.get( 
					ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL 
				).getPublicLocalStateOfEnvironment();
			EnvPLSPhysical envStatePhysical = (EnvPLSPhysical) levelsPublicLocalObservableDynamicState.get( 
					ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL 
				).getPublicLocalStateOfEnvironment();
			return new AgtCitizenPDFPhysical(
					envStateSocial.getTvBroadcastedThresholdForStrangePhysicalManifestations(), 
					agtStatePhysical.getNumberOfStrangePhysicalManifestations(), 
					envStatePhysical.getCurrentTimeOfTheDay()
			); 
		} else {
			// This case is out of the bounds of the behavior of the agent.
			// Consequently, we throw an exception telling that this case should not happen
			// in an appropriate simulation.
			throw new UnsupportedOperationException( "Cannot perceive from the level '" + levelId + "'." );
		}
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
	 * @param perceivedData The last data that were perceived by the agent from the various levels where it lies.
	 * @param memoryState The revised memory state, which value is changed by this method call.
	 */
	@Override
	public void reviseMemory(
			Map<LevelIdentifier, IPerceivedDataOfAgent> perceivedData,
			IGlobalMemoryState memoryState
	) { 
		// First cast the perceived data in an exploitable format.
		AgtCitizenPDFPhysical castedDataFromPhysical = (AgtCitizenPDFPhysical) perceivedData.get( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL );
		// Cast the revised memory state in an exploitable format.
		AgtCitizenGMS castedMemory = (AgtCitizenGMS) memoryState;
		// Set the paranoia level to numberOfMarks/broadcastedThreshold.
		castedMemory.setParanoiaLevel( castedDataFromPhysical.getCurrentStrangePhysicalManifestations() / castedDataFromPhysical.getStrangePhysicalManifestationsThreshold() );
	}

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
		if( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL.equals( levelId ) ) {
			// Case where the decision is made from the 'Physical' level.
			// Dispatch to the appropriate method.
			this.decideFromPhysicalLevel( memoryState, perceivedData, producedInfluences );
		} else {
			// This case is out of the bounds of the behavior of the agent.
			// Consequently, we throw an exception telling that this case should not happen
			// in an appropriate simulation.
			throw new UnsupportedOperationException( "Cannot decide from the level '" + levelId + "'." );
		}
	}
	
	/**
	 * Produce the influences resulting from the decisions made from the 'physical' level.
	 * <p>
	 * 	From that level, the proceeds its experiments unless the experiments have finished.
	 * 	Once finished, the alien leaves the earth and sends an experiment report to its mothership.
	 * </p>
	 * @param memoryState The memory state of the citizen, defining its current mindset.
	 * @param perceivedData The data that were perceived from the 'space' level, containing the candidates for the experiments.
	 * @param producedInfluences The map where the influences produced by the decisions of this agent are put.
	 */
	private void decideFromPhysicalLevel(
			IGlobalMemoryState memoryState,
			IPerceivedDataOfAgent perceivedData,
			InfluencesMap producedInfluences
	){
		// First cast the argument into the appropriate format.
		AgtCitizenGMS castedMemory = (AgtCitizenGMS) memoryState;
		AgtCitizenPDFPhysical castedPerceivedData = (AgtCitizenPDFPhysical) perceivedData;
		// The behavior of the citizen agent depends of the time of the day:
		//	- During the day, it either goes to work (normal behavior)  or stay sheltered (paranoid)
		//	- During the evening, it reports experiments on the Internet (either because the citizen believes
		//		it was a guinea pig of aliens or because it is paranoid)
		//	- During the night, it does nothing.
		switch( castedPerceivedData.getCurrentTimeOfTheDay() ){
		case DAY:
			// If the paranoia level of the citizen reaches at least 1, then it does not
			// go to work and stays sheltered at home.
			if( castedMemory.getParanoiaLevel() == 0 ) {
				// The agent is not paranoid. Thus, it goes to work.
				//
				// Create and add the influence to the influences produced by the decision.
				RIPhysicalGoToWork goToWorkInfluence = new RIPhysicalGoToWork( (AgtCitizenPLSPhysical) this.getPublicLocalState( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL ) );
				producedInfluences.add( goToWorkInfluence );
			}
			break;
		case EVENING:
			// During the evening, a paranoid agent reports experiments depending on its paranoia level.
			// At a level 1, it reports an experiment on himself in its city.
			// Starting from level 2, it also reports experiments on himself in other (random) cities.
			//
			// First get the public local state of the agent in the 'physical' level.
			AgtCitizenPLSPhysical citizenState = (AgtCitizenPLSPhysical) this.getPublicLocalState( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL );
			// Create the posts and request their addition to the Internet.
			if( castedMemory.getParanoiaLevel() >= 1 ){
				// Then create and add the influence to the influences produced by the decision.
				RISocialPublishExperimentReport postInfluence = new RISocialPublishExperimentReport( 
						new PostOnConspiracyForum( citizenState, citizenState.getAddress() )
				);
				producedInfluences.add( postInfluence );
			}
			for( int postsToPublish = 2; postsToPublish <= castedMemory.getParanoiaLevel(); postsToPublish++ ){
				// Get a city at random.
				int cityIndex = ConceptsSimulationRandom.randomInt( Cities.values().length );
				Cities randomCity = Cities.values()[ cityIndex ];
				// Then create a fake experiment report in that city.
				PostOnConspiracyForum post = new PostOnConspiracyForum( citizenState, randomCity );
				// Then request its addition to the Internet.
				//
				// Create and add the influence to the influences produced by the decision.
				RISocialPublishExperimentReport postInfluence = new RISocialPublishExperimentReport( post );
				producedInfluences.add( postInfluence );
			}
			break;
		case NIGHT:
			// During the night, the citizen does nothing.
			break;
		default:
			// This case is normally never reached.
			throw new UnsupportedOperationException( "The '" + castedPerceivedData.getCurrentTimeOfTheDay() + "' time of the day is not supported." );
		}
	}
}
