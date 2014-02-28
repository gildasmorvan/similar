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
package fr.lgi2a.similar.microkernel.libs.tools.engine;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationModel;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent4Engine;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.lgi2a.similar.microkernel.dynamicstate.TransitoryPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.environment.IEnvironment4Engine;
import fr.lgi2a.similar.microkernel.influences.IInfluence;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgentToLevel;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgentFromLevel;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar.microkernel.libs.generic.EmptyPerceivedData;

/**
 * A simulation engine using a mono-threaded simulation engine.
 * <p>
 * 	This engine supports the following system influences:
 * </p>
 * <ul>
 * 	<li> {@link SystemInfluenceAddAgent} </li>
 * 	<li> {@link SystemInfluenceAddAgentToLevel} </li>
 * 	<li> {@link SystemInfluenceRemoveAgent} </li>
 * 	<li> {@link SystemInfluenceRemoveAgentFromLevel} </li>
 * </ul>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public abstract class AbstractMonothreadedEngine extends AbstractSimulationEngineWithInitialization {	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected SimulationTimeStamp performSimulation(
			ISimulationModel simulationModel,
			DynamicStateMap currentSimulationDynamicState,
			LinkedHashMap<LevelIdentifier, ILevel> levels,
			LinkedHashMap<LevelIdentifier, LinkedHashSet<IAgent4Engine>> agents,
			IEnvironment4Engine environment
	) {
		SimulationTimeStamp lastPartlyConsistentStateTimestamp = simulationModel.getInitialTime();
		//
		// Handle the first step of the simulation: no reaction has to be computed since no transitory
		// period is ending.
		// We only start a new transitory period.
		//
		this.behaviorPhaseOfAgentsAndEnvironment(
				simulationModel, 
				lastPartlyConsistentStateTimestamp, 
				currentSimulationDynamicState, 
				levels.values(), 
				levels, 
				agents,
				environment
		);
		//
		// Then perform the simulation normally.
		//
		while( this.isSimulationContinuing( 
				lastPartlyConsistentStateTimestamp, 
				simulationModel 
		) ){
			/*	
			 * Perform the reaction of the levels which transitory period has met its end.
			 *	As a result of this operation:
			 *		* The content of the consistent and transitory states of the above-mentioned levels
			 *			is updated (especially their time or time range);
			 *		* The content of the agents argument (telling which agents are contained in each levels)
			 *			is updated according to the processed system reactions;
			 *		* The content of the currentSimulationDynamicState argument changes to match the
			 *			state of the simulation at the half-consistent time following the reaction.
			 *		* The value of lastPartlyConsistentStateTimestamp changes. 
			 */
			SimulationTimeStamp nextPartlyConsistentStateTimestamp = this.getNextTime( levels.values() );
			Collection<ILevel> levelsEndingTransitoryPeriod = this.identifyLevelsEndingTransitoryPeriod(
				nextPartlyConsistentStateTimestamp, 
				levels
			);
			this.reactionPhase( 
				nextPartlyConsistentStateTimestamp,
				levelsEndingTransitoryPeriod,
				levels,
				agents,
				currentSimulationDynamicState
			);
			lastPartlyConsistentStateTimestamp = nextPartlyConsistentStateTimestamp;
			// The simulation is now in an half-consistent state: the observation of the simulation is triggered.
			for( IProbe probe : this.getProbes() ){
				probe.observeAtPartialConsistentTime( lastPartlyConsistentStateTimestamp, this );
			}
			// Then trigger the behavior of the agents and of the environment for the transitory period
			// following current the half-consistent state.
			// Before this, we have to build the disambiguation of the dynamic state of the simulation.
			DynamicStateMap disambiguatedSimulationDynamicState = this.buildDynamicStateDisambiguation(
					currentSimulationDynamicState
			);
			Collection<ILevel> levelsStartingNewTransitoryPeriod = this.identifyLevelsStartingNewTransitoryPeriod(
				lastPartlyConsistentStateTimestamp, 
				levels
			);
			this.behaviorPhaseOfAgentsAndEnvironment(
				simulationModel,
				lastPartlyConsistentStateTimestamp, 
				disambiguatedSimulationDynamicState, 
				levelsStartingNewTransitoryPeriod, 
				levels,
				agents,
				environment
			);
		}
		return lastPartlyConsistentStateTimestamp;
	}
	
	/**
	 * Checks if the simulation has to continue or not.
	 * @param lastPartlyConsistentStateTimestamp The last time stamp for which 
	 * the dynamic state of the simulation was partly-consistent.
	 * @param simulationModel The simulation model used in this simulation.
	 * @return <code>true</code> if the simulation is continuing.
	 */
	private boolean isSimulationContinuing( 
			SimulationTimeStamp lastPartlyConsistentStateTimestamp,
			ISimulationModel simulationModel 
	) {
		return ! this.isAbortionRequested() && 
				! simulationModel.isFinalTimeOrAfter( 
						lastPartlyConsistentStateTimestamp, 
						this 
				);
	}
	
	/**
	 * This method identifies the next time new transitory periods will start. This value is identified using 
	 * the upper bound of the current transitory state of each level.
	 * @param levels The levels of the simulation.
	 * @return The next time new transitory periods will start.
	 */
	private SimulationTimeStamp getNextTime(
		Collection<ILevel> levels
	) {
		SimulationTimeStamp result = null;
		for( ILevel level : levels ){
			SimulationTimeStamp levelTime = level.getLastTransitoryState().getTransitoryPeriodMax();
			if( result == null ){
				result = levelTime;
			} else if( levelTime.compareTo( result ) < 0 ) {
				result = levelTime;
			}
		}
		return result;
	}
	
	/**
	 * Builds a disambiguated version of an half consistent state of the simulation.
	 * @param currentSimulationHalfConsistentState The half-consistent state of the simulation.
	 * @return A disambiguation of the half-consistent state of the simulation.
	 */
	protected abstract DynamicStateMap buildDynamicStateDisambiguation(
		DynamicStateMap currentSimulationHalfConsistentState
	);
	
	/**
	 * Identifies the levels starting a new transitory period at the time <code>halfConsistentTime</code>.
	 * @param halfConsistentTime The current half consistent time of the simulation.
	 * @param levels A map containing the levels of the simulation.
	 * @return The levels that are currently starting a new transitory period.
	 */
	private Collection<ILevel> identifyLevelsStartingNewTransitoryPeriod(
		SimulationTimeStamp halfConsistentTime,
		LinkedHashMap<LevelIdentifier, ILevel> levels
	){
		Collection<ILevel> result = new LinkedHashSet<ILevel>();
		for( ILevel level : levels.values() ){
			if( level.getLastTransitoryState().getTransitoryPeriodMin().equals( halfConsistentTime ) ){
				result.add( level );
			}
		}
		return result;
	}
	
	/**
	 * Identifies the levels ending their transitory period at the half-consistent time 
	 * <code>halfConsistentTime</code>.
	 * @param halfConsistentTime The current half consistent time of the simulation.
	 * @param levels A map containing the levels of the simulation.
	 * @return The levels ending their transitory period at the half-consistent time 
	 * <code>halfConsistentTime</code>.
	 */
	private Collection<ILevel> identifyLevelsEndingTransitoryPeriod(
		SimulationTimeStamp halfConsistentTime,
		LinkedHashMap<LevelIdentifier, ILevel> levels
	){
		Collection<ILevel> result = new LinkedHashSet<ILevel>();
		for( ILevel level : levels.values() ){
			if( level.getLastTransitoryState().getTransitoryPeriodMax().equals( halfConsistentTime ) ){
				result.add( level );
			}
		}
		return result;
	}
	
	/**
	 * Performs once the behavior of the agents and the natural action 
	 * of the environment for the specified set of levels.
	 * @param simulationModel The model of the simulation being run.
	 * @param lastPartlyConsistentStateTimestamp The last half-consistent time 
	 * preceding the transitory phase for which the behaviors are computed.
	 * @param disambiguatedSimulationDynamicState The disambiguated dynamic state of 
	 * the simulation.
	 * @param levelsStartingNewTransitoryPeriod The levels of the simulation for which the behavior phase
	 * has to be performed.
	 * @param levels The levels of the simulation.
	 * @param agents The agents lying in the various levels of the simulation.
	 */
	private void behaviorPhaseOfAgentsAndEnvironment(
		ISimulationModel simulationModel,
		SimulationTimeStamp lastPartlyConsistentStateTimestamp,
		DynamicStateMap disambiguatedSimulationDynamicState,
		Collection<ILevel> levelsStartingNewTransitoryPeriod,
		LinkedHashMap<LevelIdentifier, ILevel> levels,
		LinkedHashMap<LevelIdentifier, LinkedHashSet<IAgent4Engine>> agents,
		IEnvironment4Engine environment
	){
		if( this.isSimulationContinuing( 
				lastPartlyConsistentStateTimestamp, 
				simulationModel 
		) ) {
			// Tell the agents to perceive
			Set<IAgent4Engine> agentHavingToReviseGlobalState = this.perceptionPhase( 
				disambiguatedSimulationDynamicState,
				levelsStartingNewTransitoryPeriod,
				agents
			);
			// Then revise their global state.
			SimulationTimeStamp tPlusDt = this.getNextTime( levels.values() );
			this.globalStateRevisionPhase( 
				lastPartlyConsistentStateTimestamp,
				tPlusDt,
				agentHavingToReviseGlobalState 
			);
			InfluencesMap producedInfluencesDuringnewTransitoryPhases = new InfluencesMap();
			// Then trigger the natural action of the environment.
			this.naturalPhase( 
					levelsStartingNewTransitoryPeriod,
					disambiguatedSimulationDynamicState,
					environment,
					producedInfluencesDuringnewTransitoryPhases
			);
			// Trigger the decision of the agents
			this.decisionPhase( 
					levelsStartingNewTransitoryPeriod,
					agents,
					producedInfluencesDuringnewTransitoryPhases
			);
			// Then include the produced influences into the state dynamics of the
			// transitory dynamic states.
			for( LevelIdentifier levelId : producedInfluencesDuringnewTransitoryPhases.getDefinedKeys() ){
				List<IInfluence> influences = producedInfluencesDuringnewTransitoryPhases.getInfluencesForLevel( levelId );
				for( IInfluence influence : influences ){
					levels.get( levelId ).getLastTransitoryState().addInfluence( influence );
				}
			}
		}
	}

	/**
	 * Tell the agents of the simulation located in specific levels to perceive.
	 * @param disambiguatedSimulationDynamicState The disambiguated dynamic state of 
	 * the simulation.
	 * @param levelsStartingNewTransitoryPeriod The identifier of the levels starting a new transitory period.
	 * @param agents The agents lying in the various levels of the simulation.
	 * @return The set of agents that perceived during this phase. 
	 * It contains the agent that will have to revise their global state.
	 */
	private Set<IAgent4Engine> perceptionPhase( 
			DynamicStateMap disambiguatedSimulationDynamicState,
			Collection<ILevel> levelsStartingNewTransitoryPeriod,
			LinkedHashMap<LevelIdentifier, LinkedHashSet<IAgent4Engine>> agents
	) {
		//
		// Initialize the set returned by this method.
		//
		Set<IAgent4Engine> result = new LinkedHashSet<IAgent4Engine>( );
		//
		// Iterate over the levels to trigger the perception of the agents.
		//
		for( ILevel level : levelsStartingNewTransitoryPeriod ){
			LevelIdentifier levelId = level.getIdentifier( );
			// Hide the dynamic state of the not perceptible levels.
			IPublicDynamicStateMap perceptibleLocalDynamicStates = new DynamicStateFilteredMap(
					disambiguatedSimulationDynamicState, 
					level.getPerceptibleLevels()
			);
			SimulationTimeStamp transitoryPeriodMin = level.getLastTransitoryState().getTransitoryPeriodMin();
			SimulationTimeStamp transitoryPeriodMax = level.getLastTransitoryState().getTransitoryPeriodMax();
			for( IAgent4Engine agent : agents.get( levelId ) ){
				// Perform the perception for this agent
				IPerceivedData perceivedData = agent.perceive(
						levelId, 
						transitoryPeriodMin, 
						transitoryPeriodMax, 
						agent.getPublicLocalStates( ), 
						agent.getPrivateLocalState( levelId ), 
						perceptibleLocalDynamicStates
				);
				if( perceivedData == null ){
					throw new IllegalStateException( "The perceived data cannot be null. Prefer using an instance of " +
							"the '" + EmptyPerceivedData.class.getSimpleName() + "' class." );
				}
				// Store these data in the agent
				agent.setPerceivedData( 
						perceivedData 
				);
				// Include the agent into the ones that have to revise their global state.
				result.add( agent );
			}
		}
		return result;
	}
	
	/**
	 * Performs the global state revision phase for a specific set of agents.
	 * @param t The time stamp of the most recent half-consistent 
	 * state of the simulation (the beginning of the new transitory phase for which revision 
	 * is made). 
	 * @param tPlusDt The time stamp of the next half-consistent state of 
	 * the simulation.
	 * @param agents The agents having to revise their global state.
	 */
	private void globalStateRevisionPhase(
		SimulationTimeStamp t,
		SimulationTimeStamp tPlusDt,
		Collection<IAgent4Engine> agents
	){
		for( IAgent4Engine agent : agents ){
			agent.reviseGlobalState(
				t, 
				tPlusDt, 
				agent.getPerceivedData(), 
				agent.getGlobalState()
			);
		}
	}

	/**
	 * Performs the natural action phase for a specific set of levels.
	 * @param levelsStartingNewTransitoryPeriod The identifier of the levels starting a new transitory period.
	 * @param disambiguatedSimulationDynamicState The disambiguated dynamic state of 
	 * the simulation.
	 * @param environment The environment of the simulation.
	 * @param producedInfluences The data structure where to put the influences that were produced by the 
	 * environment.
	 */
	private void naturalPhase( 
		Collection<ILevel> levelsStartingNewTransitoryPeriod,
		DynamicStateMap disambiguatedSimulationDynamicState,
		IEnvironment4Engine environment,
		InfluencesMap producedInfluences
	){
		for( ILevel level : levelsStartingNewTransitoryPeriod ) {
			LevelIdentifier levelId = level.getIdentifier( );
			// Hide the dynamic state of the not perceptible levels.
			IPublicDynamicStateMap perceptibleLocalDynamicStates = new DynamicStateFilteredMap(
					disambiguatedSimulationDynamicState, 
					level.getPerceptibleLevels()
			);
			// Perform the natural action of the environment
			environment.natural(
				levelId, 
				level.getLastTransitoryState().getTransitoryPeriodMin(), 
				level.getLastTransitoryState().getTransitoryPeriodMax(), 
				environment.getPublicLocalStates( ), 
				environment.getPrivateLocalState( levelId ), 
				perceptibleLocalDynamicStates, 
				producedInfluences
			);
		}
	}

	/**
	 * Performs the decision phase of the agents lying in a specific set of levels.
	 * @param levelsStartingNewTransitoryPeriod The identifier of the levels starting a new transitory period.
	 * @param agents The agents of the simulation.
	 * @param producedInfluences The data structure where to put the influences that were produced by the 
	 * environment.
	 */
	private void decisionPhase( 
		Collection<ILevel> levelsStartingNewTransitoryPeriod,
		LinkedHashMap<LevelIdentifier, LinkedHashSet<IAgent4Engine>> agents,
		InfluencesMap producedInfluences
	){
		for( ILevel level : levelsStartingNewTransitoryPeriod ) {
			LevelIdentifier levelId = level.getIdentifier( );
			SimulationTimeStamp transitoryPeriodMin = level.getLastTransitoryState().getTransitoryPeriodMin();
			SimulationTimeStamp transitoryPeriodMax = level.getLastTransitoryState().getTransitoryPeriodMax();
			// Perform the decision of each agent lying in that level.
			for( IAgent4Engine agent : agents.get( levelId ) ){
				agent.decide(
					levelId, 
					transitoryPeriodMin, 
					transitoryPeriodMax, 
					agent.getGlobalState(), 
					agent.getPublicLocalState( levelId ), 
					agent.getPrivateLocalState( levelId ),
					agent.getPerceivedData().get( levelId ), 
					producedInfluences
				);
			}
		}
	}

	
	/**
	 * Performs the reaction for the levels which transitory period has met its end.
	 * <h2>Side effects</h2>
	 * This method updates:
	 * <ul>
	 * 	<li>
	 *		The state dynamics of the consistent and transitory state stored in the levels : after the reaction 
	 *		they will both contain the influences that persist despite the reaction.
	 *	</li>
	 * 	<li>
	 *		The public local states contained in the consistent and transitory state stored in the levels : after the reaction 
	 *		they will both contain the influences that persist despite the reaction.
	 *	</li>
	 *	<li>
	 *		The agents of the simulation, according to the "addition" and "removal" system influences contained in the 
	 *		transitory state of the levels performing a reaction.
	 *	</li>
	 *	<li>
	 *		The public local state of the agents is updated according to the reaction to the 
	 *		influences contained in the transitory state of the levels performing a reaction.
	 *	</li>
	 *	<li>
	 *		The public local state of the environment is updated according to the reaction to the 
	 *		influences contained in the transitory state of the levels performing a reaction.
	 *	</li>
	 * </ul>
	 * @param newHalfConsistentTimeAfterReaction The time stamp of the half-consistent state
	 * of the simulation reached after the computation of this reaction phase.
	 * @param levelsEndingTransitoryPeriod The levels for which a reaction is performed.
	 * @param levels The levels of the simulation.
	 * @param agents The agents of the simulation.
	 * @param currentSimulationHalfConsistentState The half-consistent state of the simulation.
	 */
	private void reactionPhase( 
		SimulationTimeStamp newHalfConsistentTimeAfterReaction,
		Collection<ILevel> levelsEndingTransitoryPeriod,
		LinkedHashMap<LevelIdentifier, ILevel> levels,
		LinkedHashMap<LevelIdentifier, LinkedHashSet<IAgent4Engine>> agents,
		DynamicStateMap currentSimulationHalfConsistentState
	) {
		// Perform a first loop where the reaction to system influences is handled before the reaction to
		// the regular influences.
		InfluencesMap managedInfluences = this.systemReactionToSystemInfluences( 
				levelsEndingTransitoryPeriod,
				levels,
				agents
		);
		// Perform the user-defined reaction to these influences.
		this.userReactionToSystemInfluences(
				levelsEndingTransitoryPeriod,
				levels,
				managedInfluences, 
				true
		);
		//
		// Then perform the user-defined reaction to the regular influences of the levels where a reaction is computed.
		//
		this.userReactionToRegularInfluences( 
			levelsEndingTransitoryPeriod,
			levels
		);
		//
		// Then perform a second loop where the reaction to system influences is handled after the reaction to
		// the regular influences.
		//
		managedInfluences = this.systemReactionToSystemInfluences( 
				levelsEndingTransitoryPeriod,
				levels,
				agents
		);
		// Perform the user-defined reaction to these influences.
		this.userReactionToSystemInfluences(
				levelsEndingTransitoryPeriod,
				levels,
				managedInfluences, 
				false
		);
		//
		// Update the time and next time of the consistent and transitory states that computed a reaction.
		// Also sets the state dynamics of the consistent states as equal to the content of the transitory
		// states.
		//
		for( ILevel level : levelsEndingTransitoryPeriod ){
			ConsistentPublicLocalDynamicState consistentState = level.getLastConsistentState();
			TransitoryPublicLocalDynamicState transitoryState = level.getLastTransitoryState();
			// Update the time and next time of the last consistent and current transitory states.
			SimulationTimeStamp levelLastConsistentTime = transitoryState.getTransitoryPeriodMax();
			SimulationTimeStamp levelNewTransitoryMax = level.getNextTime( levelLastConsistentTime );
			consistentState.setTime( levelLastConsistentTime );
			transitoryState.setTransitoryPeriodMax( levelNewTransitoryMax );
			// Move the influences contained into the transitory state into the consistent state.
			transitoryState.moveInfluencesToConsistentState( );
		}
		//
		// Change the value of the current dynamic state of the simulation.
		// It becomes equal to a combination of transitory states and consistent states
		//
		for( ILevel level : levels.values() ){
			if( level.getLastConsistentState().getTime().equals( newHalfConsistentTimeAfterReaction ) ){
				currentSimulationHalfConsistentState.put( level.getLastConsistentState() );
			} else {
				currentSimulationHalfConsistentState.put( level.getLastTransitoryState() );
			}
		}
	}
	
	/**
	 * Defines the side effects on the list of agents in the simulation, on 
	 * the dynamic states of the levels, on the agents and on the environment of the reaction
	 * to the system influences.
	 * <p>
	 * 	This method focuses on the generic consequences of the system influences.
	 * </p>
	 * <p>
	 * 	As a side effect, this method removes the system influences from the transitory state 
	 * of the levels of <code>levelsEndingTransitoryPeriod</code>.
	 * </p>
	 * @param levelsEndingTransitoryPeriod The levels for which a reaction is performed.
	 * @param levels The levels of the simulation.
	 * @param agents The agents of the simulation.
	 * @return The system influences that were managed during the call of this method.
	 */
	private InfluencesMap systemReactionToSystemInfluences(
		Collection<ILevel> levelsEndingTransitoryPeriod,
		LinkedHashMap<LevelIdentifier, ILevel> levels,
		LinkedHashMap<LevelIdentifier, LinkedHashSet<IAgent4Engine>> agents
	){
		InfluencesMap result = new InfluencesMap();
		// Create a map containing the influences to process in the next loop of the system reaction.
		InfluencesMap influencesToProcessInNextLoop = new InfluencesMap( );
		// This loop processes the system influences contained in the transitory state dynamics of
		// the levels computing a reaction, and puts into "influencesToProcessInNextLoop" the influences
		// that have to be processed in the next iteration of the loop.
		// Once the current iteration is over, the content of "influencesToProcessInNextLoop" is put into
		// the transitory state dynamics.
		// This loop ends when no more system influences are contained in the transitory state dynamics of
		// the levels computing a reaction.
		do { 
			// First dispatch all the influences contained in the 'influencesToProcessInNextLoop' map into the 
			// transitory dynamic state of their corresponding levels.
			//
			for( LevelIdentifier levelId : influencesToProcessInNextLoop.getDefinedKeys() ){
				TransitoryPublicLocalDynamicState transitoryDynamicState = levels.get( levelId ).getLastTransitoryState();
				// Include all the influences aimed at this level that were produced during the previous iteration.
				for( IInfluence influence : influencesToProcessInNextLoop.getInfluencesForLevel( levelId ) ){
					transitoryDynamicState.addInfluence( influence );
				}
			}
			// Then clear the content of "influencesToProcessInNextLoop".
			influencesToProcessInNextLoop.clear();
			// Process the system influences contained in the transitory state of the levels.
			for( ILevel level : levelsEndingTransitoryPeriod ){
				TransitoryPublicLocalDynamicState transitoryState = level.getLastTransitoryState();
				SimulationTimeStamp transitoryTimeMin = transitoryState.getTransitoryPeriodMin();
				SimulationTimeStamp transitoryTimeMax = transitoryState.getTransitoryPeriodMax();
				for( IInfluence systemInfluence : transitoryState.getSystemInfluencesOfStateDynamics() ){
					// Handle the reaction to the influence.
					this.reactToSystemInfluence(
						transitoryTimeMin,
						transitoryTimeMax,
						systemInfluence,
						levels,
						agents,
						influencesToProcessInNextLoop
					);
					// Include the influence into the set of influences that were processed by this method
					result.add( systemInfluence );
				}
				// Remove all system influences from the transitory dynamic state (they were handled).
				transitoryState.clearSystemInfluences();
			}
		} while( 
			! influencesToProcessInNextLoop.isEmpty()
		);
		return result;
	}

	
	/**
	 * Performs the operations in reaction to a specific system influence.
	 * @param transitoryTimeMin The lower bound of the transitory period of the level at which the influence
	 * is aimed.
	 * @param transitoryTimeMax The upper bound of the transitory period of the level at which the influence
	 * is aimed.
	 * @param systemInfluence The system influence for which a system reaction is computed.
	 * @param levels The levels of the simulation.
	 * @param agents The agents of the simulation.
	 * @param producedInfluences The data structure where to add the influences that were 
	 * produced in reaction to this influence. 
	 */
	private void reactToSystemInfluence( 
			SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			IInfluence systemInfluence,
			LinkedHashMap<LevelIdentifier, ILevel> levels,
			LinkedHashMap<LevelIdentifier, LinkedHashSet<IAgent4Engine>> agents,
			InfluencesMap producedInfluences
	){
		// Dispatches the management to the appropriate methods.
		if( systemInfluence.getCategory().equals( SystemInfluenceAddAgent.CATEGORY ) ){
			this.manageSystemInfluence(
				transitoryTimeMin,
				transitoryTimeMax,
				(SystemInfluenceAddAgent) systemInfluence,
				producedInfluences
			);
		} else if( systemInfluence.getCategory().equals( SystemInfluenceRemoveAgent.CATEGORY ) ){
			this.manageSystemInfluence( 
				transitoryTimeMin,
				transitoryTimeMax,
				(SystemInfluenceRemoveAgent) systemInfluence,
				producedInfluences
			);
		} else if( systemInfluence.getCategory().equals( SystemInfluenceAddAgentToLevel.CATEGORY ) ){
			this.manageSystemInfluence( 
				(SystemInfluenceAddAgentToLevel) systemInfluence,
				levels,
				agents
			);
		} else if( systemInfluence.getCategory().equals( SystemInfluenceRemoveAgentFromLevel.CATEGORY ) ){
			this.manageSystemInfluence( 
				(SystemInfluenceRemoveAgentFromLevel) systemInfluence,
				levels,
				agents
			);
		} else {
			throw new UnsupportedOperationException( 
				"The system influence '" + systemInfluence.getCategory() + "' cannot " +
				"be managed by the '" + this.getClass().getSimpleName() + "' simulation engine." 
			);
		}
	}
	
	/**
	 * Manages the reaction to a {@link SystemInfluenceAddAgent} influence.
	 * @param transitoryTimeMin The lower bound of the transitory period of the level at which the influence
	 * is aimed.
	 * @param transitoryTimeMax The upper bound of the transitory period of the level at which the influence
	 * is aimed.
	 * @param systemInfluence The system influence for which a system reaction is computed.
	 * @param levels The levels of the simulation.
	 * @param agents The agents of the simulation.
	 * @param producedInfluences The data structure where to add the influences that were 
	 * produced in reaction to this influence. 
	 */
	private void manageSystemInfluence(
		SimulationTimeStamp transitoryTimeMin,
		SimulationTimeStamp transitoryTimeMax,
		SystemInfluenceAddAgent systemInfluence,
		InfluencesMap producedInfluences
	){
		IAgent4Engine agentToAdd = systemInfluence.getAgent();
		for( LevelIdentifier levelId : agentToAdd.getLevels() ){
			SystemInfluenceAddAgentToLevel subInfluence = new SystemInfluenceAddAgentToLevel(
				transitoryTimeMin, 
				transitoryTimeMax, 
				agentToAdd.getPublicLocalState( levelId ), 
				agentToAdd.getPrivateLocalState( levelId )
			);
			producedInfluences.add( subInfluence );
		}
	}
	
	/**
	 * Manages the reaction to a {@link SystemInfluenceRemoveAgent} influence.
	 * @param transitoryTimeMin The lower bound of the transitory period of the level at which the influence
	 * is aimed.
	 * @param transitoryTimeMax The upper bound of the transitory period of the level at which the influence
	 * is aimed.
	 * @param systemInfluence The system influence for which a system reaction is computed.
	 * @param levels The levels of the simulation.
	 * @param agents The agents of the simulation.
	 * @param producedInfluences The data structure where to add the influences that were 
	 * produced in reaction to this influence. 
	 */
	private void manageSystemInfluence(
		SimulationTimeStamp transitoryTimeMin,
		SimulationTimeStamp transitoryTimeMax,
		SystemInfluenceRemoveAgent systemInfluence,
		InfluencesMap producedInfluences
	){
		IAgent4Engine agentToRemove = systemInfluence.getAgent();
		for( LevelIdentifier levelId : agentToRemove.getLevels() ){
			SystemInfluenceRemoveAgentFromLevel subInfluence = new SystemInfluenceRemoveAgentFromLevel(
				transitoryTimeMin, 
				transitoryTimeMax, 
				agentToRemove,
				levelId
			);
			producedInfluences.add( subInfluence );
		}
	}
	
	/**
	 * Manages the reaction to a {@link SystemInfluenceAddAgentToLevel} influence.
	 * @param systemInfluence The system influence for which a system reaction is computed.
	 * @param levels The levels of the simulation.
	 * @param agents The agents of the simulation.
	 * @return The influences that were produced in reaction to this influence. 
	 */
	private void manageSystemInfluence(
		SystemInfluenceAddAgentToLevel systemInfluence,
		LinkedHashMap<LevelIdentifier, ILevel> levels,
		LinkedHashMap<LevelIdentifier, LinkedHashSet<IAgent4Engine>> agents
	){
		ILevel level = levels.get( systemInfluence.getTargetLevel() );
		ILocalStateOfAgent4Engine addedPublicLocalState = systemInfluence.getPublicLocalState();
		ILocalStateOfAgent4Engine addedPrivateLocalState = systemInfluence.getPrivateLocalState();
		IAgent4Engine addedAgent = systemInfluence.getPublicLocalState().getOwner();
		// Check the existence of the level where the public local state is added.
		if( level == null ){
			throw new IllegalStateException( 
				"The influence '" + systemInfluence.getCategory() + "' tried to add the public " +
				"local state of an agent '" + addedPublicLocalState.getCategoryOfAgent() + "' to the " +
				"non-existing level '" + level + "'." 
			);
		} else {
			// Add the public local state to the public local dynamic state of the level.
			level.getLastConsistentState().addPublicLocalStateOfAgent( 
				addedPublicLocalState
			);
			// Add the agent to the list of agents contained in the level.
			LevelIdentifier levelId = level.getIdentifier();
			agents.get( levelId ).add( addedAgent );
			// Add the local states to the agent.
			addedAgent.includeNewLevel(
				level.getIdentifier(), 
				addedPublicLocalState, 
				addedPrivateLocalState
			);
		}
	}
	
	/**
	 * Manages the reaction to a {@link SystemInfluenceRemoveAgentFromLevel} influence.
	 * @param systemInfluence The system influence for which a system reaction is computed.
	 * @param levels The levels of the simulation.
	 * @param agents The agents of the simulation.
	 * @return The influences that were produced in reaction to this influence. 
	 */
	private void manageSystemInfluence(
		SystemInfluenceRemoveAgentFromLevel systemInfluence,
		LinkedHashMap<LevelIdentifier, ILevel> levels,
		LinkedHashMap<LevelIdentifier, LinkedHashSet<IAgent4Engine>> agents
	){
		ILevel level = levels.get( systemInfluence.getTargetLevel() );
		ILocalStateOfAgent4Engine agentPublicLocalState = systemInfluence.getAgentLocalState();
		IAgent4Engine removedAgent = agentPublicLocalState.getOwner( );
		// Check the existence of the level from which the agent is removed.
		if( level == null ){
			throw new IllegalStateException( 
				"The influence '" + systemInfluence.getCategory() + "' tried to remove " +
				"an agent '" + removedAgent.getCategory() + "' from the " +
				"non-existing level '" + level + "'." 
			);
		} else {
			LevelIdentifier levelId = level.getIdentifier();
			// Remove the public local state of the agent from the public local dynamic state of the level.
			level.getLastConsistentState().removePublicLocalStateOfAgent(
				agentPublicLocalState
			);
			// Remove the agent from the list of agents contained in the level.
			agents.get( levelId ).remove( removedAgent );
			// Remove the local states of the agent.
			removedAgent.excludeFromLevel( levelId );
		}
	}
	
	/**
	 * Performs a user-defined reaction for the system influences that were managed during the system reaction.
	 * <p>
	 * 	As a side-effect, this method adds the influences produced by the user reaction into 
	 * 	the transitory state of the levels.
	 * </p>
	 * @param levelsEndingTransitoryPeriod The levels for which a reaction is performed.
	 * @param levels The levels of the simulation.
	 * @param influencesManagedDuringSystemReaction The system influences that were managed during the system reaction
	 * to the system influences.
	 * @param beforeRegularReaction <code>true</code> if this method is called before the user-defined reaction to regular influences.
	 */
	private void userReactionToSystemInfluences( 
			Collection<ILevel> levelsEndingTransitoryPeriod,
			LinkedHashMap<LevelIdentifier, ILevel> levels,
			InfluencesMap influencesManagedDuringSystemReaction,
			boolean beforeRegularReaction
	) {
		// Create the data structure where the influences produced by the user reaction are stored, before
		// dispatching them between the transitory dynamic state of the targeted levels.
		InfluencesMap userInfluences = new InfluencesMap( );
		// Run the user reactions.
		for( ILevel level : levelsEndingTransitoryPeriod ){
			TransitoryPublicLocalDynamicState transitoryState = level.getLastTransitoryState();
			SimulationTimeStamp transitoryTimeMin = transitoryState.getTransitoryPeriodMin();
			SimulationTimeStamp transitoryTimeMax = transitoryState.getTransitoryPeriodMax();
			level.makeSystemReaction(
				transitoryTimeMin, 
				transitoryTimeMax, 
				level.getLastConsistentState(), 
				influencesManagedDuringSystemReaction.getInfluencesForLevel( level.getIdentifier() ), 
				beforeRegularReaction, 
				userInfluences
			);
		}
		// Then include the produced influences to the transitory states
		for( LevelIdentifier levelId : userInfluences.getDefinedKeys() ) {
			ILevel level = levels.get( levelId );
			for( IInfluence influence : userInfluences.getInfluencesForLevel( levelId ) ){
				level.getLastTransitoryState().addInfluence( influence );
			}
		}
	}
	
	/**
	 * Performs a user-defined reaction for the regular influences from the transitory dynamic state of 
	 * the levels computing a reaction.
	 * <p>
	 * 	As a side-effect, this method removes the processed regular influences from the transitory state and the consistent state.
	 * 	The influences persisting after the reaction are located in the transitory state of the levels after the call to this method.
	 * </p>
	 * @param levelsEndingTransitoryPeriod The levels for which a reaction is performed.
	 * @param levels The levels of the simulation.
	 */
	private void userReactionToRegularInfluences( 
			Collection<ILevel> levelsEndingTransitoryPeriod,
			LinkedHashMap<LevelIdentifier, ILevel> levels
	) {
		// Create the data structure where the influences produced by the user reaction are stored, before
		// dispatching them between the transitory dynamic state of the targeted levels.
		InfluencesMap userInfluences = new InfluencesMap( );
		// Run the user reactions.
		for( ILevel level : levelsEndingTransitoryPeriod ){
			TransitoryPublicLocalDynamicState transitoryState = level.getLastTransitoryState();
			SimulationTimeStamp transitoryTimeMin = transitoryState.getTransitoryPeriodMin();
			SimulationTimeStamp transitoryTimeMax = transitoryState.getTransitoryPeriodMax();
			level.makeRegularReaction(
				transitoryTimeMin,
				transitoryTimeMax, 
				level.getLastConsistentState(), 
				level.getLastTransitoryState().getRegularInfluencesOfStateDynamics(), 
				userInfluences
			);
			// Remove the regular influences from the transitory state.
			transitoryState.clearRegularInfluences();
		}
		// Include the influences in the transitory state of the targeted levels.
		for( LevelIdentifier levelId : userInfluences.getDefinedKeys() ) {
			ILevel level = levels.get( levelId );
			TransitoryPublicLocalDynamicState transitoryState = level.getLastTransitoryState( );
			for( IInfluence influence : userInfluences.getInfluencesForLevel( levelId ) ){
				transitoryState.addInfluence( influence );
			}
		}
	}
}
