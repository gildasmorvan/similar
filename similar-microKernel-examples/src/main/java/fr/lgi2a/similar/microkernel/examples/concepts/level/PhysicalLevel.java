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
package fr.lgi2a.similar.microkernel.examples.concepts.level;

import java.util.Collection;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.ILevel;
import fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationLevelIdentifiers;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationRandom;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationTimeInterpretationModel;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.alien.AgtAlien;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.alien.physical.AgtAlienPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.citizen.AgtCitizen;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.citizen.physical.AgtCitizenPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.EnvPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.tophysical.RIPhysicalCaptureAndDissectAlien;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.tophysical.RIPhysicalGoToWork;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.tophysical.RIPhysicalLandOnEarth;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.tophysical.RIPhysicalPerformExperiment;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.tophysical.RIPhysicalRemoveAllStrangePhysicalManifestations;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.tophysical.RIPhysicalSetTimeOfTheDay;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.tophysical.RIPhysicalTakeOffFromEarth;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddPublicLocalStateToDynamicState;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemovePublicLocalStateFromDynamicState;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractLevel;
import fr.lgi2a.similar.microkernel.libs.generic.EmptyPublicLocalStateOfAgent;

/**
 * Models the 'Physical' level of the simulation.
 * 
 * <h1>Levels in the SIMILAR API suite.</h1>
 * <p>
 * 	In the micro-kernel of SIMILAR, a level is implemented as an 
 * 	instance of either the {@link ILevel} interface, or of the {@link AbstractLevel} 
 * 	abstract class.
 * 	In this example, we use the abstract class which is easier to implement.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class PhysicalLevel extends AbstractLevel {
	/**
	 * Builds an initialized instance of the 'physical' level.
	 * @param initialTime The initial time stamp of the simulation. This value is provided during 
	 * the initialization process of a simulation.
	 * @param experimentCompletionRatePerReaction The completion rate of the experiment performed by an alien achieved when a reaction is computed. This value is within the range ]0,100]. 
	 * It corresponds to the percentage of the experiment process completed after one reaction.
	 * @param strangePhysicalManifestationRate The apparition rate of strange physical manifestations on the body of citizen when an alien performs 
	 * an experiment on them. This value has to be between 0 (included) and 1 (excluded). The higher the value, the higher a strange physical manifestation
	 * will appear on a citizen during each reaction where the experiment is still performed.
	 * @param fbiCaptureEfficiency The efficiency of the FBI to capture alien. This value has to be 
	 * between 0 (completely inefficient) to 1 (fully efficient). It determines the success chances of
	 * a capture influence sent by the FBI.
	 */
	public PhysicalLevel(
			SimulationTimeStamp initialTime,
			double experimentCompletionRatePerReaction,
			double strangePhysicalManifestationRate,
			double fbiCaptureEfficiency
	) {
		super( initialTime, ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL );
		//
		// Build the perception relation graph of the level, i.e. identify the level that can be perceived
		// by the agents lying in this level.
		//
		// FBI agents and Citizen can perceive the media of the 'social' levels from the 'physical' level.
		this.addPerceptibleLevel( ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL );
		//
		// Build the influence relation graph of the level, i.e. identify the level that can be influenced
		// by the decisions of the agents (or the natural action of the environment) lying in this level.
		//
		// FBI agents and Citizen can modify the media of the 'social' levels from the 'physical' level (
		// change broadcasted values or write posts on the Internet).
		this.addInfluenceableLevel( ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL );
		// Aliens can send data reports to their mothership in the 'space' level at the end of their experiments from the
		// 'physical' level.
		this.addInfluenceableLevel( ConceptsSimulationLevelIdentifiers.SPACE_LEVEL );
		//
		// Initialize the parameters of the reaction of this level.
		//
		this.experimentCompletionRatePerReaction = experimentCompletionRatePerReaction;
		this.strangePhysicalManifestationRate = strangePhysicalManifestationRate;
		this.fbiCaptureEfficiency = fbiCaptureEfficiency;
	}

	// // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
	//
	//
	//	TIME MODEL RELATED METHOD
	//
	//
	//

	/**
	 * This method determines the next time when the next reaction will be performed for this level.
	 * @return The next time a reaction will be performed for this model.
	 */
	@Override
	public SimulationTimeStamp getNextTime( SimulationTimeStamp currentTime ) {
		// In this level, the times moves with a period of 1.
		return new SimulationTimeStamp( currentTime.getIdentifier() + 1 );
	}

	// // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
	//
	//
	//	METHOD RELATED TO THE REACTION TO SYSTEM INFLUENCES
	//
	//
	//

	/**
	 * Performs a user-defined reaction to system influences.
	 * <p>
	 * 	In SIMILAR, the reaction is the process moving the public local dynamic state of a level from its state
	 * 	at a specific time stamp to its state at its next time stamp.
	 * </p>
	 * <h1>Calls to this method</h1>
	 * This method is called twice during the reaction phase of a level:
	 * <ol>
	 * 	<li>
	 * 		After the reaction of the simulation engine to the system influences, but before the reaction to the regular influences
	 *	</li>
	 * 	<li>
	 * 		After the reaction of the simulation engine to the system influences that were produced by the reaction to the regular influences.
	 *	</li>
	 * </ol>
	 * <p>
	 * 	It provides to users the possibility to write a customized reaction to system influences.
	 * </p>
	 * @param previousConsistentStateTime The time stamp of the last time a reaction was computed for this level.
	 * @param newConsistentStateTime The time stamp of when this reaction is computed for this level.
	 * @param consistentState The public dynamic local state of the level being updated by the reaction to reach its new state.
	 * @param systemInfluencesToManage The system influences that were managed by the simulation engine during the current reaction right before the
	 * call to this method.
	 * @param happensBeforeRegularReaction <code>true</code> if this method is called in the first situation of 
	 * the enumeration (see the documentation of the method).
	 * @param newInfluencesToProcess The data structure where the influences that were produced by this user-defined reaction are put.
	 */
	@Override
	public void makeSystemReaction(
			SimulationTimeStamp previousConsistentStateTime,
			SimulationTimeStamp newConsistentStateTime,
			ConsistentPublicLocalDynamicState consistentState,
			Collection<IInfluence> systemInfluencesToManage,
			boolean happensBeforeRegularReaction,
			Collection<IInfluence> newInfluencesToProcess
	) {
		//
		// In the 'physical' level, we have to maintain the data structure summarizing the location of all the citizen and the aliens of 
		// the 'physical' level.
		// This means that each time the public local state of a 'citizen' or of an 'alien' agent is added to this level, this user-defined
		// reaction has to add the public local state to the public local state of the environment.
		// The same goes if such a state is removed from the level.
		//
		// First get a pointer on the public local state of the environment in this level.
		EnvPLSPhysical envPLS = (EnvPLSPhysical) consistentState.getPublicLocalStateOfEnvironment();
		// Then iterate over the system influences.
		for( IInfluence systemInfluence : systemInfluencesToManage ){
			// Check if the influence models the addition of the public local state of an agent.
			if( SystemInfluenceAddPublicLocalStateToDynamicState.CATEGORY.equals( systemInfluence.getCategory() ) ){
				// Convert the influence to the appropriate format.
				SystemInfluenceAddPublicLocalStateToDynamicState castedInfluence = (SystemInfluenceAddPublicLocalStateToDynamicState) systemInfluence;
				// Get the public local state that was added to the level.
				IPublicLocalStateOfAgent agentState = castedInfluence.getPublicLocalState();
				// If it is a citizen, add it to the 'citizen' data structure of the environment.
				if( agentState.getOwner().getCategory().equals( AgtCitizen.CATEGORY ) ){
					AgtCitizenPLSPhysical castedAgentState = (AgtCitizenPLSPhysical) agentState;
					envPLS.addCitizen( castedAgentState, castedAgentState.getAddress() );
				}
				// If it is an alien, add it to the 'alien' data structure of the environment.
				if( agentState.getOwner().getCategory().equals( AgtAlien.CATEGORY ) ){
					AgtAlienPLSPhysical castedAgentState = (AgtAlienPLSPhysical) agentState;
					envPLS.addAlien( castedAgentState, castedAgentState.getLocation() );
				}
			}
		}
	}

	// // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
	//
	//
	//	METHOD RELATED TO THE REACTION TO REGULAR INFLUENCES
	//
	//
	//

	/**
	 * Performs a user-defined reaction to the regular influences.
	 * <p>
	 * 	In SIMILAR, the reaction is the process moving the public local dynamic state of a level from its state
	 * 	at a specific time stamp to its state at its next time stamp.
	 * </p>
	 * <h1>Usage</h1>
	 * <p>
	 * 	This reaction reads the influences contained in the <code>regularInfluencesOftransitoryStateDynamics</code>
	 * 	argument and performs operations into the public local dynamic state of the level (<code>consistentState</code> 
	 * 	argument), or in the public local state of the environment or agents it contains.
	 * </p>
	 * <p>
	 * 	The influence can either be consumed by the reaction (they disappear from the simulation) or can persist after the reaction
	 *  (if the influence models an unfinished action when the reaction ends).
	 *  In the second case, the influence has to be included into the <code>remainingInfluences</code> set.
	 * </p>
	 * <p>
	 * 	If the reaction produces other influences, these influences have to be added to the <code>remainingInfluences</code> set.
	 * 	Note that the system influences aimed at levels currently performing their reaction are managed right after the end of the call 
	 * 	to this method. Otherwise, these influences are managed during the next reaction of the level they are aimed at.
	 * </p>
	 * @param previousConsistentStateTime The time stamp of the last time a reaction was computed for this level.
	 * @param newConsistentStateTime The time stamp of when this reaction is computed for this level.
	 * @param consistentState The public dynamic local state of the level being updated by the reaction to reach its new state.
	 * @param regularInfluencesOftransitoryStateDynamics The <b>regular</b> influences that have to be managed by this reaction to go from the 
	 * previous consistent state to the next consistent state of the level.
	 * @param remainingInfluences The set that will contain the influences that were produced by the user during the invocation of 
	 * this method, or the influences that persist after this reaction.
	 */
	@Override
	public void makeRegularReaction(
			SimulationTimeStamp previousConsistentStateTime,
			SimulationTimeStamp newConsistentStateTime,
			ConsistentPublicLocalDynamicState consistentState,
			Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
			Set<IInfluence> remainingInfluences
	) {
		for( IInfluence influence : regularInfluencesOftransitoryStateDynamics ){
			// Dispatch the management of the influence to the appropriate methods.
			if( RIPhysicalLandOnEarth.CATEGORY.equals( influence.getCategory() ) ){
				this.reactTo( 
						(RIPhysicalLandOnEarth) influence, 
						remainingInfluences 
				);
			} else if( RIPhysicalTakeOffFromEarth.CATEGORY.equals( influence.getCategory() ) ){
				this.reactTo( 
						(RIPhysicalTakeOffFromEarth) influence, 
						remainingInfluences 
				);
			} else if( RIPhysicalPerformExperiment.CATEGORY.equals( influence.getCategory() ) ){
				this.reactTo( 
						(RIPhysicalPerformExperiment) influence, 
						remainingInfluences 
				);
			} else if( RIPhysicalSetTimeOfTheDay.CATEGORY.equals( influence.getCategory() ) ){
				this.reactToSetTimeOfTheDay( 
						newConsistentStateTime, 
						consistentState
				);
			} else if( RIPhysicalCaptureAndDissectAlien.CATEGORY.equals( influence.getCategory() ) ){
				this.reactTo( 
						(RIPhysicalCaptureAndDissectAlien) influence, 
						remainingInfluences 
				);
			} else if( RIPhysicalRemoveAllStrangePhysicalManifestations.CATEGORY.equals( influence.getCategory() ) ){
				this.reactTo( 
						(RIPhysicalRemoveAllStrangePhysicalManifestations) influence
				);
			} else if( 	RIPhysicalGoToWork.CATEGORY.equals( influence.getCategory() ) ) {
				// This influence has currently no effect on the simulation.
				// In the future, it can be used to track the effect on the economy of the
				// alien experiments
			} else {
				// This case is out of the bounds of the behavior of the reaction.
				// Consequently, we throw an exception telling that this case should not happen
				// in an appropriate simulation.
				throw new UnsupportedOperationException( "Cannot manage the influence '" + influence.getCategory() + "' in the reaction " +
						"of the level '" + this.getIdentifier() + "'." );
			}
		}
	}
	
	/**
	 * Manages the reaction to a {@link RIPhysicalLandOnEarth} influence.
	 * @param influence The influence which reaction is managed by this method call.
	 * @param remainingInfluences The set that will contain the influences that were produced by the user during the invocation of 
	 * this method, or the influences that persist after this reaction.
	 */
	private void reactTo(
			RIPhysicalLandOnEarth influence,
			Set<IInfluence> remainingInfluences
	){
		//
		// In reaction to this influence, the 'alien' agent is removed from the 'space' level and added
		// to the 'physical' level.
		//
		// The agent is first added to the 'physical' level. This operation is done by creating the appropriate system influence.
		AgtCitizenPLSPhysical experimentSubject = influence.getExperimentSubject();
		AgtAlien alienAgent = influence.getAlienAgent();
		SystemInfluenceAddPublicLocalStateToDynamicState addInfluence = new SystemInfluenceAddPublicLocalStateToDynamicState( 
				ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL, 
				new AgtAlienPLSPhysical( alienAgent, experimentSubject.getAddress(), experimentSubject )
		);
		remainingInfluences.add( addInfluence );
		// The agent is then removed from the 'space' level. This operation is also done by creating the appropriate system influence.
		SystemInfluenceRemovePublicLocalStateFromDynamicState rmInfluence = new SystemInfluenceRemovePublicLocalStateFromDynamicState(
				ConceptsSimulationLevelIdentifiers.SPACE_LEVEL, 
				alienAgent.getPublicLocalState( ConceptsSimulationLevelIdentifiers.SPACE_LEVEL )
		);
		remainingInfluences.add( rmInfluence );
	}
	
	/**
	 * Manages the reaction to a {@link RIPhysicalTakeOffFromEarth} influence.
	 * @param influence The influence which reaction is managed by this method call.
	 * @param remainingInfluences The set that will contain the influences that were produced by the user during the invocation of 
	 * this method, or the influences that persist after this reaction.
	 */
	private void reactTo(
			RIPhysicalTakeOffFromEarth influence,
			Set<IInfluence> remainingInfluences
	){
		//
		// In reaction to this influence, the 'alien' agent is removed from the 'physical' level and added
		// to the 'space' level.
		//
		// The agent is first added to the 'space' level. This operation is done by creating the appropriate system influence.
		AgtAlien alienAgent = influence.getAlienAgent();
		SystemInfluenceAddPublicLocalStateToDynamicState addInfluence = new SystemInfluenceAddPublicLocalStateToDynamicState( 
				ConceptsSimulationLevelIdentifiers.SPACE_LEVEL, 
				new EmptyPublicLocalStateOfAgent( ConceptsSimulationLevelIdentifiers.SPACE_LEVEL, alienAgent )
		);
		remainingInfluences.add( addInfluence );
		// The agent is then removed from the 'physical' level. This operation is also done by creating the appropriate system influence.
		SystemInfluenceRemovePublicLocalStateFromDynamicState rmInfluence = new SystemInfluenceRemovePublicLocalStateFromDynamicState(
				ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL, 
				alienAgent.getPublicLocalState( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL )
		);
		remainingInfluences.add( rmInfluence );
	}
	
	/**
	 * Models the completion rate of the experiment performed by an alien achieved when a reaction is computed. This value is within the range ]0,100]. 
	 * It corresponds to the percentage of the experiment process completed after one reaction.
	 * <p>
	 * 	This value is a parameter of the reaction process of the level.
	 * </p>
	 */
	private double experimentCompletionRatePerReaction;
	/**
	 * Models the apparition rate of strange physical manifestations on the body of citizen when an alien performs 
	 * an experiment on them. This value has to be between 0 (included) and 1 (excluded). The higher the value, the higher a strange physical manifestation
	 * will appear on a citizen during each reaction where the experiment is still performed.
	 * <p>
	 * 	This value is a parameter of the reaction process of the level.
	 * </p>
	 */
	private double strangePhysicalManifestationRate;
	
	/**
	 * Manages the reaction to a {@link RIPhysicalPerformExperiment} influence.
	 * @param influence The influence which reaction is managed by this method call.
	 * @param remainingInfluences The set that will contain the influences that were produced by the user during the invocation of 
	 * this method, or the influences that persist after this reaction.
	 */
	private void reactTo(
			RIPhysicalPerformExperiment influence,
			Set<IInfluence> remainingInfluences
	){
		//
		// In reaction to this influence: 
		//	- the completion rate of the experiment is increased (public local state of the 'alien')
		//	- the number of strange physical manifestations of the 'citizen' might increase (public local state of the 'citizen').
		//	- if the experiment has not finished, the influence persists after the reaction.
		//
		// Increase the completion rate of the experiment.
		int addedValue = (int) Math.ceil( this.experimentCompletionRatePerReaction * influence.getEfficiencyInExperiments() );
		influence.getAlienPublicLocalState().increaseExperimentCompletionRate( addedValue );
		// Increase by one the strange physical manifestations of the 'citizen' if a random value is lower than their apparition rate.
		double randomValue = ConceptsSimulationRandom.random( );
		if( randomValue < this.strangePhysicalManifestationRate ){
			influence.getExperimentSubject().increaseNumberOfStrangePhysicalManifestations( 1 );
		}
		// Include the influence into the remaining influences if the experiment is not completed.
		if( ! influence.getAlienPublicLocalState().hasFinishedExperiments() ){
			remainingInfluences.add( influence );
		}
	}
	
	/**
	 * Manages the reaction to a {@link RIPhysicalSetTimeOfTheDay} influence.
	 * @param newConsistentStateTime The time stamp of when this reaction is computed for this level.
	 * @param consistentState The public dynamic local state of the level being updated by the reaction to reach its new state.
	 */
	private void reactToSetTimeOfTheDay(
			SimulationTimeStamp newConsistentStateTime,
			ConsistentPublicLocalDynamicState consistentState
	){
		//
		// In reaction to this influence, the current time of the day is updated.
		// to the 'space' level.
		//
		EnvPLSPhysical castedEnvState = (EnvPLSPhysical) consistentState.getPublicLocalStateOfEnvironment();
		castedEnvState.setCurrentTimeOfTheDay( ConceptsSimulationTimeInterpretationModel.getInstance().getTimeOfTheDay( newConsistentStateTime ) );
	}
	
	/**
	 * Models the efficiency of the FBI to capture alien. This value has to be 
	 * between 0 (completely inefficient) to 1 (fully efficient). It determines the success chances of
	 * a capture influence sent by the FBI.
	 * <p>
	 * 	This value is a parameter of the reaction process of the level.
	 * </p>
	 */
	private double fbiCaptureEfficiency;

	/**
	 * Manages the reaction to a {@link RIPhysicalCaptureAndDissectAlien} influence.
	 * @param influence The influence which reaction is managed by this method call.
	 * @param remainingInfluences The set that will contain the influences that were produced by the user during the invocation of 
	 * this method, or the influences that persist after this reaction.
	 */
	private void reactTo(
			RIPhysicalCaptureAndDissectAlien influence,
			Set<IInfluence> remainingInfluences
	){
		//
		// In reaction to this influence, the alien is removed from the simulation if the FBI is efficient enough.
		// This is determined by getting a random number and checking if it is higher than an efficiency threshold.
		//
		// First get a random number.
		double randomNumber = ConceptsSimulationRandom.random( );
		// Then check if the FBI is efficient enough to capture the alien.
		if( randomNumber < this.fbiCaptureEfficiency ){
			// The alien is removed from the simulation. This task is performed by using a system reaction.
			// Since we want to remove the agent before the end of this reaction, the system influence has to be managed in the system reaction
			// directly following this reaction to regular influences. This is achieved by aiming the influence at this level.
			SystemInfluenceRemoveAgent removeAgtInfluence = new SystemInfluenceRemoveAgent( this.getIdentifier(), influence.getAlien() );
			remainingInfluences.add( removeAgtInfluence );
		}
	}

	/**
	 * Manages the reaction to a {@link RIPhysicalRemoveAllStrangePhysicalManifestations} influence.
	 * @param influence The influence which reaction is managed by this method call.
	 */
	private void reactTo(
			RIPhysicalRemoveAllStrangePhysicalManifestations influence
	){
		//
		// In reaction to this influence, the number of strange physical manifestations on the body of the citizen are removed.
		//
		influence.getCitizen().resetStrangePhysicalManifestations( );
	}
}
