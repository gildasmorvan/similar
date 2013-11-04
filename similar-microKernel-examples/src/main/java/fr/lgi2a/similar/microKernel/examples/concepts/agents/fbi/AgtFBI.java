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
package fr.lgi2a.similar.microkernel.examples.concepts.agents.fbi;

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
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationParameters;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.alien.physical.AgtAlienPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.citizen.AgtCitizen;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.citizen.physical.AgtCitizenPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.editorinchief.AgtEditorInChief;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.editorinchief.social.AgtEditorInChiefPLSSocial;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.fbi.physical.AgtFBIPDFSocial;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.fbi.social.AgtFBIPDFPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.Cities;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.EnvPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.social.EnvPLSSocial;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.social.PostOnConspiracyForum;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.toPhysical.RIPhysicalCaptureAndDissectAlien;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.toPhysical.RIPhysicalRemoveAllStrangePhysicalManifestations;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.toSocial.RISocialChangeBroadcast;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.toSocial.RISocialRemoveAllPublications;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.toSocial.RISocialRemovePublications;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.toSocial.RISocialReplaceEditorInChief;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractAgent;
import fr.lgi2a.similar.microkernel.libs.generic.EmptyGlobalMemoryState;
import fr.lgi2a.similar.microkernel.libs.generic.EmptyPublicLocalStateOfAgent;

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
public class AgtFBI extends AbstractAgent {
	/**
	 * The category of this agent class (telling that the agent is an 'FBI' agent).
	 * <p>
	 * 	This category is defined as a static value to facilitate the identification of the nature of the
	 * 	agents for instance when data about the agent are printed on screen.
	 * </p>
	 */
	public static final String CATEGORY = "FBI";
	
	/**
	 * Builds the global memory state of the FBI agents.
	 * <p>
	 * 	The private local state of the agent in the 'physical' state contains an initially empty posts database.
	 * </p>
	 */
	public AgtFBI( ) {
		// The super constructor requires the definition of the category of the agent.
		super( CATEGORY );
		//
		// Define the initial global memory state of the agent.
		//
		// No specific data are required in that state for the 'alien' agent. Thus, instead of defining a new class,
		// we use the generic class EmptyGlobalMemoryState defined in the common libs of SIMILAR.
		// This class models an empty global memory state for an agent.
		this.initializeGlobalMemoryState( new EmptyGlobalMemoryState( this ) );
		//
		// Tell that this agent is initially in the 'Physical' level.
		//
		// No specific data about this agent have be perceptible in the 'Physical' level. Thus, instead of defining a new class,
		// we use the generic class EmptyPublicLocalStateOfAgent defined in the common libs of SIMILAR.
		// This class models an empty public local state for an agent.
		IPublicLocalStateOfAgent stateInPhysicalLevel = new EmptyPublicLocalStateOfAgent( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL, this );
		// Specify that the agent lies is initially present in the 'Physical' level.
		this.includeNewLevel( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL, stateInPhysicalLevel );
		//
		// Tell that this agent is initially in the 'Social' level.
		//
		// No specific data about this agent have be perceptible in the 'Social' level. Thus, instead of defining a new class,
		// we use the generic class EmptyPublicLocalStateOfAgent defined in the common libs of SIMILAR.
		// This class models an empty public local state for an agent.
		IPublicLocalStateOfAgent stateInSocialLevel = new EmptyPublicLocalStateOfAgent( ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL, this );
		// Specify that the agent lies is initially present in the 'Social' level.
		this.includeNewLevel( ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL, stateInSocialLevel );
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
		if( ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL.equals( levelId ) ){
			// Case where the perception is made from the 'Social' level.
			// Dispatch to the appropriate method.
			return this.perceptionForSocialLevel( levelsPublicLocalObservableDynamicState );
		} else if( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL.equals( levelId ) ) {
			// Case where the perception is made from the 'Physical' level.
			// Dispatch to the appropriate method.
			return this.perceptionForPhysicalLevel( levelsPublicLocalObservableDynamicState );
		} else {
			// This case is out of the bounds of the behavior of the agent.
			// Consequently, we throw an exception telling that this case should not happen
			// in an appropriate simulation.
			throw new UnsupportedOperationException( "Cannot perceive from the level '" + levelId + "'." );
		}
	}
	

	/**
	 * Produce the perceived data of the agent in the case when the perception is made from the 'Social' level.
	 * @param levelsPublicLocalObservableDynamicState The public dynamic state of the levels that can be perceived from 
	 * the 'space' level.
	 * @return The data that were perceived by the 'FBI' for its decisions from the 'Social' level.
	 */
	private IPerceivedDataOfAgent perceptionForSocialLevel(
			IDynamicStateMap levelsPublicLocalObservableDynamicState
	) {
		IPublicLocalDynamicState socialDynamicState = levelsPublicLocalObservableDynamicState.get( 
				ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL 
		);
		EnvPLSSocial envState = (EnvPLSSocial) socialDynamicState.getPublicLocalStateOfEnvironment();
		// Find the editor in chief of the TV broadcast.
		AgtEditorInChiefPLSSocial editorInChief = null;
		for( IPublicLocalStateOfAgent agentState : socialDynamicState.getPublicLocalStateOfAgents() ){
			if( AgtEditorInChief.CATEGORY.equals( agentState.getOwner().getCategory() ) ){
				editorInChief = (AgtEditorInChiefPLSSocial) agentState;
			}
		}
		// Find the city having the least posts.
		Cities selectedCity = null;
		int numberOfPostsInSelectedCity = Integer.MAX_VALUE;
		for( Cities city : Cities.values() ){
			int numberOfPostsInCity = envState.getPostsFor( city ).size();
			if( numberOfPostsInCity < numberOfPostsInSelectedCity ){
				selectedCity = city;
				numberOfPostsInSelectedCity = numberOfPostsInCity;
			}
		}
		return new AgtFBIPDFSocial(
				editorInChief, 
				envState.getTvBroadcastedThresholdForStrangePhysicalManifestations(),
				selectedCity
		);
	}
	
	/**
	 * The threshold of citizens posts number before a citizen is sent to lobotomy by the FBI.
	 */
	private int citizenPostsThresholdBeforeLobotomy = ConceptsSimulationParameters.CITIZEN_POSTS_THRESHOLD_BEFORE_LOBOTOMY;
	
	/**
	 * Produce the perceived data of the agent in the case when the perception is made from the 'physical' level.
	 * @param levelsPublicLocalObservableDynamicState The public dynamic state of the levels that can be perceived from 
	 * the 'space' level.
	 * @return The data that were perceived by the 'FBI' for its decisions from the 'physical' level.
	 */
	private IPerceivedDataOfAgent perceptionForPhysicalLevel(
			IDynamicStateMap levelsPublicLocalObservableDynamicState
	) {
		// First get the state of environment in the 'social' level  and the 'physical' level.
		IPublicLocalDynamicState dynamicState = levelsPublicLocalObservableDynamicState.get( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL );
		EnvPLSPhysical envPhysical = (EnvPLSPhysical) dynamicState.getPublicLocalStateOfEnvironment();
		dynamicState = levelsPublicLocalObservableDynamicState.get( ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL );
		EnvPLSSocial envSocial = (EnvPLSSocial) dynamicState.getPublicLocalStateOfEnvironment();
		// Choose the city having the most posts about alien experiments.
		int postsOfCurrentCity = 0;
		Cities selectedCity = null;
		for( Cities city : Cities.values() ){
			int postsOfCity = envSocial.getPostsFor( city ).size();
			if( postsOfCity > postsOfCurrentCity ) {
				postsOfCurrentCity = postsOfCity;
				selectedCity = city;
			}
		}
		AgtFBIPDFPhysical perceivedData = new AgtFBIPDFPhysical( 
				selectedCity, 
				envPhysical.getCurrentTimeOfTheDay() 
		);
		// Add the aliens located in that city.
		Set<AgtAlienPLSPhysical> aliens = envPhysical.getAliensAt( selectedCity );
		if( aliens != null ){
			for( AgtAlienPLSPhysical alien : aliens ){
				perceivedData.addAlienInSearchedCity( alien );
			}
		}
		// Find dangerous citizens.
		for( IPublicLocalStateOfAgent agentState : levelsPublicLocalObservableDynamicState.get( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL ).getPublicLocalStateOfAgents() ){
			// Check if the agent is a citizen.
			if( AgtCitizen.CATEGORY.equals( agentState.getOwner().getCategory() ) ){
				// If the agent is a citizen, find the number of posts that were written by him.
				AgtCitizenPLSPhysical citizenState = (AgtCitizenPLSPhysical) agentState;
				Set<PostOnConspiracyForum> posts = envSocial.getPostsFor( citizenState );
				if( posts != null ){
					if( posts.size() > this.citizenPostsThresholdBeforeLobotomy ){
						perceivedData.addDangerousCitizen( citizenState, envSocial.getPostsFor( citizenState ) );
					}
				}
			}
		}
		return perceivedData;
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
		if( ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL.equals( levelId ) ){
			// Case where the decision is made from the 'Social' level.
			// Dispatch to the appropriate method.
			this.decideFromSocialLevel( perceivedData, producedInfluences );
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
	 * The number of strange physical manifestation over which a citizen can consider that an alien experiment 
	 * was performed on him/her. This value is being broadcasted on television and corresponds to the value 
	 * advised by the FBI.
	 * <p>
	 * 	This value is part of the private local state of the agent. Since it purely belongs to the inner working of the behavior of the 
	 * 	agent from the 'social' level, it is declared here rather than in the public local state or the global memory state of the agent.
	 * </p>
	 */
	private int thresholdForStrangePhysicalManifestationsAdvisedByFBI = 
			ConceptsSimulationParameters.THRESHOLD_FOR_STRANGE_PHYSICAL_MANIFESTATION_ADVISED_BY_FBI;

	/**
	 * Produce the influences resulting from the decisions made from the 'Social' level.
	 * <p>
	 * 	From that level, the FBI replaces 'Editor in chiefs' if they do not follow the directives of the FBI any more.
	 * 	If it does so, the FBI also changes the broadcasted value to the recommended one. It also removes all the posts on the
	 * 	Internet to cut off the panic in the media.
	 * </p>
	 * @param perceivedData The data that were perceived from the 'Social' level, containing the candidates for the experiments.
	 * @param producedInfluences The map where the influences produced by the decisions of this agent are put.
	 */
	private void decideFromSocialLevel( 
			IPerceivedDataOfAgent perceivedData,
			InfluencesMap producedInfluences
	) {
		AgtFBIPDFSocial castedData = (AgtFBIPDFSocial) perceivedData;
		if( castedData.getBroadcastedValue() != this.thresholdForStrangePhysicalManifestationsAdvisedByFBI ){
			RISocialReplaceEditorInChief replaceInfluence = new RISocialReplaceEditorInChief( 
					castedData.getEditorInChief(), 
					new AgtEditorInChief( castedData.getCityWithLessPosts() ) 
			);
			producedInfluences.add( replaceInfluence );
			RISocialChangeBroadcast changeInfluence = new RISocialChangeBroadcast( 
					this.thresholdForStrangePhysicalManifestationsAdvisedByFBI, 
					this.getCategory() 
			);
			producedInfluences.add( changeInfluence );
			RISocialRemoveAllPublications removeAllInfluence = new RISocialRemoveAllPublications( );
			producedInfluences.add( removeAllInfluence );
		}
	}
	
	/**
	 * Produce the influences resulting from the decisions made from the 'Physical' level.
	 * <p>
	 * 	From that level, the FBI agent:
	 * </p>
	 * <ul>
	 * 	<li>Chases dangerous citizens and lobotomize them during the day.</li>
	 * 	<li>Chases aliens during the night.</li>
	 * </ul>
	 * @param perceivedData The data that were perceived from the 'Physical' level, containing the candidates for the experiments.
	 * @param producedInfluences The map where the influences produced by the decisions of this agent are put.
	 */
	private void decideFromPhysicalLevel(
			IPerceivedDataOfAgent perceivedData,
			InfluencesMap producedInfluences
	){
		AgtFBIPDFPhysical castedData = (AgtFBIPDFPhysical) perceivedData;
		switch( castedData.getCurrentTime() ){
		case DAY:
			// The FBI lobotomizes all the dangerous citizen (remove all strange physical manifestations, and remove all their posts on the Internet)
			for( AgtCitizenPLSPhysical dangerousCitizen : castedData.getDangerousCitizens() ) {
				// First tell that the strange physical manifestations have to be removed.
				RIPhysicalRemoveAllStrangePhysicalManifestations removeManifestationsInfluence = new RIPhysicalRemoveAllStrangePhysicalManifestations(
						dangerousCitizen
				);
				producedInfluences.add( removeManifestationsInfluence );
				// Then tell that all the posts of that citizen have to be removed from the Internet.
				RISocialRemovePublications removePostsInfluence = new RISocialRemovePublications( dangerousCitizen );
				producedInfluences.add( removePostsInfluence );
			}
			break;
		case EVENING:
			// The FBI does nothing in the evening.
			break;
		case NIGHT:
			// The FBI chases the aliens it perceived.
			if( castedData.getAliensInSearchedCity() != null ){
				for( AgtAlienPLSPhysical alien : castedData.getAliensInSearchedCity() ){
					// Produce an influence telling that the FBI tries to capture the alien.
					RIPhysicalCaptureAndDissectAlien captureInfluence = new RIPhysicalCaptureAndDissectAlien( alien );
					producedInfluences.add( captureInfluence );
				}
			}
			break;
		default:
			// This case is normally never reached.
			throw new UnsupportedOperationException( "The '" + castedData.getCurrentTime() + "' time of the day is not supported." );
		}
	}
}
