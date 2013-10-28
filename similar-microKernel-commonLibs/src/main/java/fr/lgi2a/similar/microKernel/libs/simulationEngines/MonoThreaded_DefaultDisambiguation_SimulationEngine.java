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
package fr.lgi2a.similar.microKernel.libs.simulationEngines;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import fr.lgi2a.similar.microKernel.Exception_SimulationAborted;
import fr.lgi2a.similar.microKernel.I_Agent;
import fr.lgi2a.similar.microKernel.I_Environment;
import fr.lgi2a.similar.microKernel.I_Influence;
import fr.lgi2a.similar.microKernel.I_Level;
import fr.lgi2a.similar.microKernel.I_Probe;
import fr.lgi2a.similar.microKernel.I_SimulationModel;
import fr.lgi2a.similar.microKernel.I_SimulationModel.AgentInitializationData;
import fr.lgi2a.similar.microKernel.I_SimulationModel.EnvironmentInitializationData;
import fr.lgi2a.similar.microKernel.LevelIdentifier;
import fr.lgi2a.similar.microKernel.SimulationTimeStamp;
import fr.lgi2a.similar.microKernel.agentBehavior.I_PerceivedDataOfAgent;
import fr.lgi2a.similar.microKernel.agentBehavior.InfluencesMap;
import fr.lgi2a.similar.microKernel.influences.systemInfluences.SystemInfluence_AddAgent;
import fr.lgi2a.similar.microKernel.influences.systemInfluences.SystemInfluence_AddPublicLocalStateToDynamicState;
import fr.lgi2a.similar.microKernel.influences.systemInfluences.SystemInfluence_RemoveAgent;
import fr.lgi2a.similar.microKernel.influences.systemInfluences.SystemInfluence_RemovePublicLocalStateFromDynamicState;
import fr.lgi2a.similar.microKernel.libs.abstractImplementations.AbstractSimulationEngine;
import fr.lgi2a.similar.microKernel.libs.genericImplementations.EmptyPerceivedDataOfAgent;
import fr.lgi2a.similar.microKernel.states.I_PublicLocalDynamicState;
import fr.lgi2a.similar.microKernel.states.I_PublicLocalState;
import fr.lgi2a.similar.microKernel.states.I_PublicLocalStateOfAgent;
import fr.lgi2a.similar.microKernel.states.dynamicStates.Consistent_PublicLocalDynamicState;
import fr.lgi2a.similar.microKernel.states.dynamicStates.I_Modifiable_PublicLocalDynamicState;
import fr.lgi2a.similar.microKernel.states.dynamicStates.Transitory_PublicLocalDynamicState;
import fr.lgi2a.similar.microKernel.states.dynamicStates.map.DynamicState_FilteredMap;
import fr.lgi2a.similar.microKernel.states.dynamicStates.map.DynamicState_Map;
import fr.lgi2a.similar.microKernel.states.dynamicStates.map.I_DynamicState_Map;

/**
 * Models a simulation engine running simulations using a mono-threaded approach.
 * <p>
 * 	For efficiency reasons, this simulation engine does not 
 * 	clone the public local state of agents or of the environment during the computation of the reaction.
 *	It directly manipulates these states instead.
 * </p>
 * <p>
 * 	This simulation engine also defines the disambiguation operator such that the disambiguation of a transitory local dynamic state
 * 	is equal to the last consistent state of the local dynamic state.
 * </p>
 * <h2>Note</h2>
 * <p>
 * 	Some optimizations were made based on the above-mentioned assumptions. The algorithms used in this class do not exactly implement
 * 	the general algorithm of SIMILAR, to favor the execution of more efficient simulations: the algorithm performs less calls to the 
 * 	perception method and to the disambiguation operator.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class MonoThreaded_DefaultDisambiguation_SimulationEngine extends AbstractSimulationEngine {
	/**
	 * Models the public local dynamic states of the simulation being forwarded to the probes to 
	 * display the content of the simulation.
	 * This map contains either consistent dynamic states (if the current time stamp of the simulation is equal to
	 * the current time stamp of the level) or transitory dynamic state (if the level is currently between two of its
	 * time stamps).
	 */
	private I_DynamicState_Map currentSimulationDynamicState;
	/**
	 * A map associating a level to its identifier.
	 */
	private Map<LevelIdentifier, I_Level> levels;
	/**
	 * A map associating the agents lying in each level of the simulation to the identifier of the level.
	 */
	private Map<LevelIdentifier, Set<I_Agent>> agents;
	/**
	 * The environment where the simulation takes place.
	 */
	private I_Environment environment;
	/**
	 * Stores the last consistent dynamic state of each level of the simulation.
	 */
	private I_DynamicState_Map lastConsistentDynamicStates;
	/**
	 * Stores the current transitory dynamic state of each level of the simulation.
	 */
	private I_DynamicState_Map transitoryDynamicStates;
	/**
	 * A boolean flag telling if the simulation has to be aborted or not.
	 */
	private boolean abortFlag;
	
	/**
	 * Builds a mono-threaded simulation engine.
	 */
	public MonoThreaded_DefaultDisambiguation_SimulationEngine( ) {
		this.levels = new HashMap<LevelIdentifier, I_Level>( );
		this.agents = new HashMap<LevelIdentifier, Set<I_Agent>>();
		this.abortFlag = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public I_DynamicState_Map getSimulationDynamicStates() {
		return this.currentSimulationDynamicState;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<I_Agent> getAgents() {
		Set<I_Agent> agents = new HashSet<I_Agent>();
		for( LevelIdentifier level : this.getLevels() ){
			agents.addAll( this.getAgents( level ) );
		}
		return agents;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<LevelIdentifier> getLevels( ) {
		return this.levels.keySet( );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<I_Agent> getAgents(
			LevelIdentifier level
	) throws NoSuchElementException {
		Set<I_Agent> agents = this.agents.get( level );
		if( agents == null ){
			throw new NoSuchElementException( "The simulation does not contain the level '" + level + "'." );
		}
		return agents;
	}

	/**
	 * Disambiguates a public local dynamic state, <i>i.e.</i> transforms a transitory state into a fully observable state.
	 * <p>
	 * 	This operation returns the last consistent state of the level.
	 * </p>
	 * <p>
	 * 	TODO formal notation
	 * </p>
	 * @param transitoryDynamicState The transitory state for which a disambiguation is computed.
	 * @return the observable dynamic state corresponding to the disambiguation of the transitory dynamic state.
	 */
	@Override
	public I_PublicLocalDynamicState disambiguation(
			Transitory_PublicLocalDynamicState transitoryDynamicState
	) {
		return transitoryDynamicState.getLastConsistentDynamicState();
	}

	/**
	 * {@inheritDoc}
	 */
	public void requestSimulationAbortion( ) {
		this.abortFlag = true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void runNewSimulation(
			I_SimulationModel simulationModel
	) throws RuntimeException, Exception_SimulationAborted {
		// First check that the simulation model is not null.
		if( simulationModel == null ) {
			throw new IllegalArgumentException( "The 'simulationModel' argument cannot be null." );
		}
		// Prepare the observation made by the probes
		for( I_Probe probe : this.probes.values() ){
			probe.prepareObservation();
		}
		SimulationTimeStamp currentTime = simulationModel.getInitialTime();
		try {
			if( currentTime == null ){
				throw new IllegalStateException( "The 'initial time' defined in the simulation model cannot be null. " );
			}
			// Initialize the simulation
			this.initializeSimulation( simulationModel );
			// Tell the probes to observe the state of the simulation
			for( I_Probe probe : this.probes.values() ){
				probe.observeAtInitialTimes( simulationModel.getInitialTime(), this );
			}
			// Run the simulation
			SimulationTimeStamp finalTime = this.performSimulation( simulationModel );
			// The probes observe the partly consistent dynamic state of the simulation
			for( I_Probe probe : this.probes.values() ){
				probe.observeAtFinalTime( finalTime, this );
			}
		} catch( Exception_SimulationAborted a ) {
			// Case where the simulation was aborted by the user.
			for( I_Probe probe : this.probes.values() ){
				probe.reactToAbortion(
						currentTime, 
						this
				);
			}
		} catch( Throwable t ) {
			// In this case, both the simulation engine and the simulation model are in an
			// invalid state because of the error.
			// The simulation is stopped, and the probes are told to process the error.
			for( I_Probe probe : this.probes.values() ){
				probe.reactToError(
						"The simulation stopped because of an error.", 
						t
				);
			}
		} finally {
			// Close the resources that were used by the probes
			for( I_Probe probe : this.probes.values() ){
				probe.endObservation();
			}
		}
	}
	
	/**
	 * Initializes the simulation that will run.
	 * @param simulationModel The simulation model used to build the simulation being run with this engine.
	 * @throws IllegalArgumentException if: 
	 * <ul>
	 * 	<li>No level was defined for this simulation.</li>
	 * 	<li>The name of a level is <code>null</code>.</li>
	 * 	<li>Two levels have the same name.</li>
	 * 	<li>The environment of the simulation is <code>null</code></li>
	 * </ul>
	 * @throws IllegalStateException If the simulation model contains errors.
	 */
	protected void initializeSimulation( I_SimulationModel simulationModel ) throws IllegalStateException {
		// First reset the data that were used in the previous simulations.
		this.agents.clear();
		this.lastConsistentDynamicStates = new DynamicState_Map( );
		this.transitoryDynamicStates = new DynamicState_Map( );
		this.currentSimulationDynamicState = new DynamicState_Map( );
		this.levels.clear();
		this.abortFlag = false;
		//
		// Then generate the list of the levels of the simulation.
		//
		SimulationTimeStamp initialTime = simulationModel.getInitialTime();
		List<I_Level> createdLevels = simulationModel.generateLevels( initialTime );
		// Check that the list is valid.
		if( createdLevels == null ){
			throw new IllegalStateException( "The simulation model has to provide a valid list of levels. The list was null." );
		} else if( createdLevels.isEmpty( ) ){
			throw new IllegalStateException( "The simulation model has to contain at least one level." );
		} else {
			for( I_Level generatedLevel : createdLevels ){
				if( generatedLevel == null ){
					throw new IllegalStateException( "The list of levels cannot contain the null element." );
				}
				LevelIdentifier levelId = generatedLevel.getIdentifier( );
				if( levelId == null ){
					throw new IllegalStateException( "The identifier of a level cannot be null." );
				} else if( this.levels.containsKey( levelId ) ){
					throw new IllegalStateException( "Two levels share the same identifier '" + levelId + "'." );
				}
				this.levels.put( levelId, generatedLevel );
			}
		}
		//
		// Initialize the content of each level.
		//
		for( I_Level level : this.levels.values() ){
			LevelIdentifier levelId = level.getIdentifier( );
			this.agents.put( levelId, new LinkedHashSet<I_Agent>( ) );
			this.lastConsistentDynamicStates.put( level.getLastConsistentPublicLocalDynamicState() );
			this.transitoryDynamicStates.put(
					new Transitory_PublicLocalDynamicState( 
							level.getLastConsistentPublicLocalDynamicState(), 
							level.getNextTime( initialTime ) 
					)
			);
			this.currentSimulationDynamicState.put( level.getLastConsistentPublicLocalDynamicState() );
		}
		//
		// Manage the creation of the environment, the agents and the initial influences.
		//
		// Create the environment where the simulation will take place.
		InfluencesMap influencesProducedByEnvironment = this.createEnvironment( simulationModel, initialTime );
		// Then create the agents in the initial state of the simulation.		
		InfluencesMap influencesProducedByAgents = this.createAgents( simulationModel, initialTime );
		// Add the initial influences to the initial state dynamics of the public local dynamic state of the levels.
		this.dispatchInitialInfluences( influencesProducedByEnvironment, influencesProducedByAgents );
		//
		// Finally memorize the current state of the simulation as the sum of all the initial consistent states of the levels.
		// This value will be revised after each reaction.
		//
		for( I_Level level : this.levels.values() ){
			this.currentSimulationDynamicState.put( this.lastConsistentDynamicStates.get( level.getIdentifier() ) );
		}
	}
	
	/**
	 * Creates the environment of the simulation.
	 * @param simulationModel The model of the simulation.
	 * @param initialTime The initial time of the simulation.
	 * @return The influences that are initially in the simulation because of the environment.
	 * @throws IllegalStateException If the simulation model contains errors.
	 */
	private InfluencesMap createEnvironment( 
			I_SimulationModel simulationModel, 
			SimulationTimeStamp initialTime 
	) throws IllegalStateException {
		//
		// Ask the model to create a new environment for the simulation.
		//
		EnvironmentInitializationData generationData = simulationModel.generateEnvironment( initialTime, this.levels );
		// Check that the data seem valid
		if( generationData == null ){
			throw new IllegalStateException( "The value returned by the generateEnvironment(...) method of the " +
					"simulation model cannot be null." );
		}
		if( generationData.environment == null ){
			throw new IllegalStateException( "The environment of the simulation cannot be null. " +
					"Check the value returned by the generateEnvironment(...) method of the simulation model." );
		}
		this.environment = generationData.environment;
		//
		// Store the physical state of the environment in every level of the simulation.
		//
		for( I_Level level : this.levels.values() ){
			I_PublicLocalState environmentLocalState = this.environment.getPublicLocalState( level.getIdentifier() );
			if( environmentLocalState == null ){
				throw new IllegalStateException( "The public local state of the environment in the level '" + level.getIdentifier() + "'" +
						" cannot be null." );
			}
			level.getLastConsistentPublicLocalDynamicState().setPublicLocalStateOfEnvironment( 
					environmentLocalState
			);
		}
		// Return the initial influences defined during the generation of the environment.
		return generationData.influences;
	}
	
	/**
	 * Creates the agents of the simulation.
	 * @param simulationModel The model of the simulation.
	 * @param initialTime The initial time of the simulation.
	 * @return The influences that are initially in the simulation because of the agents.
	 */
	private InfluencesMap createAgents( 
			I_SimulationModel simulationModel, 
			SimulationTimeStamp initialTime 
	) {
		//
		// Ask the model to create the agents initially lying in the simulation.
		//
		AgentInitializationData generationData = simulationModel.generateAgents( initialTime, this.levels );
		// Check that the data seem valid
		if( generationData == null ){
			throw new IllegalStateException( "The value returned by the generateAgents(...) method of the " +
					"simulation model cannot be null." );
		}
		if( generationData.agents == null ){
			throw new IllegalStateException( "The environment of the simulation cannot be null. " +
					"Check the value returned by the generateAgents(...) method of the simulation model." );
		}
		//
		// Add the agents to the agents list. Also add the public local state of the agents to the appropriate levels.
		//
		Set<I_Agent> agents = generationData.agents;
		for( I_Agent agent : agents ){
			for( LevelIdentifier levelId : agent.getLevels() ){
				if( levelId == null || ! this.levels.containsKey( levelId ) ){
					throw new IllegalStateException( "The agent '" + agent.getCategory() + "' from the class '" + agent.getClass( ) + "' " +
							"specified a public local state for a not existing level named '" + levelId + "'." );
				}
				// Add the agent to the list of agents in that level.
				this.agents.get( levelId ).add( agent );
				// Add the public local state of the agent to the public local dynamic state of the level.
				I_Modifiable_PublicLocalDynamicState levelState = (I_Modifiable_PublicLocalDynamicState) 
						this.lastConsistentDynamicStates.get( levelId );
				I_PublicLocalStateOfAgent agentState = agent.getPublicLocalState( levelId );
				if( agentState == null ){
					throw new IllegalStateException( "The agent '" + agent.getCategory() + "' from the class '" + agent.getClass( ) + "' " +
							" defines a null public local state for the level '" + levelId + "'." );
				} else {
					levelState.addPublicLocalStateOfAgent( agentState );
				}
			}
		}
		// Return the initial influences defined during the generation of the agents.
		return generationData.influences;
	}
	
	/**
	 * Adds to the initial state dynamics of the levels the influences that were created during the initialization of the 
	 * environment and of the agents.
	 * @param influencesProducedByEnvironment The influences that are initially in the simulation because of the environment.
	 * @param influencesProducedByAgents The influences that are initially in the simulation because of the agents.
	 */
	private void dispatchInitialInfluences(
			InfluencesMap influencesProducedByEnvironment, 
			InfluencesMap influencesProducedByAgents
	) {
		for( LevelIdentifier levelId : this.levels.keySet( ) ){
			if( ! influencesProducedByEnvironment.isEmpty( levelId ) ){
				for( I_Influence influence : influencesProducedByEnvironment.getInfluencesForLevel( levelId ) ){
					Consistent_PublicLocalDynamicState state = 
							(Consistent_PublicLocalDynamicState) this.lastConsistentDynamicStates.get( levelId );
					state.addInfluence( influence );
				}
			}
			if( ! influencesProducedByAgents.isEmpty( levelId ) ){
				for( I_Influence influence : influencesProducedByAgents.getInfluencesForLevel( levelId ) ){
					Consistent_PublicLocalDynamicState state = 
							(Consistent_PublicLocalDynamicState) this.lastConsistentDynamicStates.get( levelId );
					state.addInfluence( influence );
				}
			}
		}
	}
	
	/**
	 * Runs the simulation, so that it will go from its initial state to its final state.
	 * @param simulationModel The model of the simulation.
	 * @return The final time stamp of the simulation.
	 * @throws Exception_SimulationAborted If the simulation was aborted by the user.
	 * @throws Throwable If an exception was caught while running the simulation.
	 */
	protected SimulationTimeStamp performSimulation( 
			I_SimulationModel simulationModel 
	) throws Throwable, Exception_SimulationAborted {
		// Iterate over all the time stamps of the simulation
		SimulationTimeStamp lastPartlyConsistentStateTimestamp = simulationModel.getInitialTime();
		//
		// Handle the first step of the simulation: no reaction has to be computed for the initial time.
		//
		if( this.isSimulationContinuing( lastPartlyConsistentStateTimestamp, simulationModel ) ) {
			// Ask all the agents to perceive
			Set<I_Agent> agentHavingToReviseMemory = this.perceptionPhase( 
					lastPartlyConsistentStateTimestamp, 
					this.levels.values() 
			);
			// Then revise their memory.
			this.memorizationPhase( agentHavingToReviseMemory );
			// Then trigger the decision of agents / natural action of the environment.
			this.naturalAndDecisionPhase( 
					lastPartlyConsistentStateTimestamp, 
					this.levels.values() 
			);
		}
		//
		// Then take into consideration the reaction phases.
		//
		while( this.isSimulationContinuing( lastPartlyConsistentStateTimestamp, simulationModel ) ){
			//
			// First perform the reaction for the levels which transitory period has met its end.
			// As a result of this operation, the value of the lastPartlyConsistentStateTimestamp
			// changes.
			//
			lastPartlyConsistentStateTimestamp = this.reactionPhase( lastPartlyConsistentStateTimestamp );
			// Then trigger the observation of the simulation.
			for( I_Probe probe : this.probes.values() ){
				probe.observeAtPartialConsistentTime( lastPartlyConsistentStateTimestamp, this );
			}
			// Then check if it models the ending time of the simulation.
			if( this.isSimulationContinuing( lastPartlyConsistentStateTimestamp, simulationModel ) ){
				// The simulation does not stop: perception, memory revision, natural and decision have to be performed.
				// Ask the appropriate agents to perceive.
				Set<I_Level> levelsWherePerceptionNaturalAndDesicionAreMade = this.buildLp( lastPartlyConsistentStateTimestamp );
				Set<I_Agent> agentHavingToReviseMemory = this.perceptionPhase( 
						lastPartlyConsistentStateTimestamp, 
						levelsWherePerceptionNaturalAndDesicionAreMade
				);
				// Then revise their memory.
				this.memorizationPhase( agentHavingToReviseMemory );
				// Then trigger the decision of agents / natural action of the environment.
				this.naturalAndDecisionPhase( 
						lastPartlyConsistentStateTimestamp, 
						levelsWherePerceptionNaturalAndDesicionAreMade
				);
			}
		}
		//
		// Finally update the dynamic state of the simulation, now that the ending time is reached:
		//	 + All transitory states become consistent states.
		// 	 + The state dynamics of the transitory states becomes the state dynamics of the consistent states.
		//
		InfluencesMap influencesAtEnd = new InfluencesMap( );
		for( I_Level level : this.levels.values() ){
			// Store in a temporary variable all the influences that were lying in the level.
			Transitory_PublicLocalDynamicState transitoryState = (Transitory_PublicLocalDynamicState)
					this.transitoryDynamicStates.get( level.getIdentifier() );
			Set<I_Influence> dynamics = transitoryState.getStateDynamics();
			for( I_Influence influence : dynamics ){
				influencesAtEnd.add( influence );
			}
			// Change the current time of the last consistent dynamic state of the level to the final time of the simulation.
			Consistent_PublicLocalDynamicState consistentState = 
					(Consistent_PublicLocalDynamicState) this.lastConsistentDynamicStates.get( level.getIdentifier() );
			consistentState.setTime( lastPartlyConsistentStateTimestamp );
		}
		// Store the state dynamics of the levels at the end of the simulation and update the set describing the current dynamic state
		// of the simulation.
		for( I_Level level : this.levels.values() ){
			Consistent_PublicLocalDynamicState consistentState = 
					(Consistent_PublicLocalDynamicState) this.lastConsistentDynamicStates.get( level.getIdentifier() );
			consistentState.setStateDynamicsAsCopyOf( influencesAtEnd.getInfluencesForLevel( level.getIdentifier() ) );
			this.currentSimulationDynamicState.put( consistentState );
		}
		return lastPartlyConsistentStateTimestamp;
	}
	
	/**
	 * Checks if the simulation has to continue or not.
	 * @param lastPartlyConsistentStateTimestamp The last time stamp for which the dynamic state of the simulation was partly-consistent.
	 * @param simulationModel The simulation model used in this simulation.
	 * @return <code>true</code> if the simulation is continuing.
	 */
	private boolean isSimulationContinuing( 
			SimulationTimeStamp lastPartlyConsistentStateTimestamp,
			I_SimulationModel simulationModel 
	) {
		return ! this.abortFlag && ! simulationModel.isFinalTimeOrAfter( lastPartlyConsistentStateTimestamp, this );
	}

	/**
	 * Builds the list of the levels that are currently in a consistent state. These levels are consequently beginning a new
	 * transitory phase (if the simulation did not end), and have to ask agents to perceive.
	 * @param currentTime The current simulation time.
	 * @return The list of levels that are currently in a consistent state.
	 */
	protected Set<I_Level> buildLp( SimulationTimeStamp currentTime ){
		Set<I_Level> lP = new LinkedHashSet<I_Level>( );
		for( I_Level level : this.levels.values( ) ){
			if( level.getLastConsistentPublicLocalDynamicState().getTime().equals( currentTime ) ){
				lP.add( level );
			}
		}
		return lP;
	}
	
	/**
	 * Tell the agents of the simulation located in specific levels to perceive.
	 * @param currentTime The value of the time stamp slightly after which the perception takes place.
	 * @param lP The levels where the perception has to be made.
	 * @return The set of agents that perceived during this phase. It contains the agent that will have to revise their memory.
	 */
	protected Set<I_Agent> perceptionPhase( 
			SimulationTimeStamp currentTime, 
			Collection<I_Level> lP 
	) {
		//
		// Initialize the set returned by this method.
		//
		Set<I_Agent> result = new LinkedHashSet<I_Agent>( );
		//
		// Iterate over the levels to trigger the perception of the agents.
		//
		for( I_Level level : lP ){
			LevelIdentifier levelId = level.getIdentifier();
			I_DynamicState_Map perceptibleLocalDynamicStates = new DynamicState_FilteredMap(
					this.lastConsistentDynamicStates, 
					level.getPerceptibleLevels()
			);
			for( I_Agent agent : this.agents.get( levelId ) ){
				// Perform the perception
				I_PerceivedDataOfAgent perceivedData = agent.perceive(
						levelId, 
						agent.getPublicLocalState( levelId ), 
						perceptibleLocalDynamicStates
				);
				if( perceivedData == null ){
					throw new IllegalStateException( "The perceived data cannot be null. Prefer using an instance of " +
							"the '" + EmptyPerceivedDataOfAgent.class.getSimpleName() + "' class." );
				}
				// Store these data in the agent
				agent.setPerceivedData( levelId, perceivedData );
				// Include the agent into the ones that have to perceive
				result.add( agent );
			}
		}
		return result;
	}
	
	/**
	 * Performs the memory revision phase of the simulation for a specific set of agents.
	 * @param agentsHavingToMemorize The set of agents that have to memorize.
	 */
	protected void memorizationPhase( Set<I_Agent> agentsHavingToMemorize ){
		for( I_Agent agent : agentsHavingToMemorize ){
			agent.reviseMemory( agent.getPerceivedData( ), agent.getGlobalMemoryState() );
		}
	}
	
	/**
	 * Performs the natural and decision phase for a specific set of levels.
	 * @param currentTime The current simulation time.
	 * @param lI The levels for which a decision has to be made.
	 */
	protected void naturalAndDecisionPhase( 
			SimulationTimeStamp currentTime,
			Collection<I_Level> lI
	){
		InfluencesMap producedInfluences = new InfluencesMap();
		for( I_Level level : lI ){
			LevelIdentifier levelId = level.getIdentifier( );
			I_DynamicState_Map perceptibleLocalDynamicStates = new DynamicState_FilteredMap(
					this.lastConsistentDynamicStates, 
					level.getPerceptibleLevels()
			);
			//
			// Perform the natural action of the environment and include the produced influences into 
			// the transitory state of the appropriate levels.
			//
			this.environment.natural(
					levelId, 
					perceptibleLocalDynamicStates, 
					producedInfluences
			);
			//
			// Perform the decisions of the agents and include the produced influences into 
			// the transitory state of the appropriate levels.
			//
			for( I_Agent agent : this.agents.get( levelId ) ) {
				agent.decide(
						levelId, 
						agent.getGlobalMemoryState(), 
						agent.getPerceivedData( levelId ), 
						producedInfluences
				);
			}
			// Include the influences produced by the environment and the agents to the transitory dynamic state of the levels.
			this.includeInfluencesInTransitoryStates( producedInfluences );
		}
	}
	
	/**
	 * Includes a map of influences into the transitory state of the levels.
	 * @param influences The influences to include into the transitory state of the levels.
	 */
	private void includeInfluencesInTransitoryStates( InfluencesMap influences ) {
		if( influences != null ){
			for( LevelIdentifier levelId : influences.getDefinedKeys() ){
				I_PublicLocalDynamicState rawDynamicState = this.transitoryDynamicStates.get( levelId );
				I_Modifiable_PublicLocalDynamicState castedDynamicState = (I_Modifiable_PublicLocalDynamicState) rawDynamicState;
				for( I_Influence influence : influences.getInfluencesForLevel( levelId ) ){
					castedDynamicState.addInfluence( influence );
				}
			}
		}
	}

	/**
	 * Builds the list of levels where a reaction has to be computed.
	 * @param lastConsistentTime The last time stamp when the simulation was in a partly-consistent state.
	 * @return The list of levels where a reaction has to be computed.
	 */
	protected Set<I_Level> buildLr( SimulationTimeStamp lastConsistentTime ){
		Set<I_Level> lR = new LinkedHashSet<I_Level>( );
		SimulationTimeStamp nextTime = null;
		for( I_Level level : this.levels.values( ) ){
			LevelIdentifier levelId =  level.getIdentifier();
			Transitory_PublicLocalDynamicState transitoryState = 
					(Transitory_PublicLocalDynamicState) this.transitoryDynamicStates.get( levelId );
			if( nextTime == null ){
				nextTime = transitoryState.getNextTime();
			}
			if( transitoryState.getNextTime().equals( nextTime ) ){
				lR.add( level );
			} else if( transitoryState.getNextTime().compareTo( nextTime ) < 0 ) {
				lR.clear();
				lR.add( level );
				nextTime = transitoryState.getNextTime();
			}
		}
		return lR;
	}
	
	/**
	 * Performs the reaction for the levels which transitory period has met its end.
	 * <h2>Side effects</h2>
	 * <p>
	 * 	As a result of this operation, the last time the simulation was in a partly-consistent state changes: it becomes equal 
	 * 	to the higher bound of the time range of the transitory period (<i>i.e.</i> to t+dt).
	 * </p>
	 * @param lastConsistentTime The last time stamp when the simulation was in a partly-consistent state.
	 * @return The higher bound of the time range of the transitory periods for which a reaction was computed.
	 */
	private SimulationTimeStamp reactionPhase( SimulationTimeStamp lastConsistentTime ) {
		Set<I_Level> levelsWhereReactionIsComputed = this.buildLr( lastConsistentTime );
		SimulationTimeStamp newLastConsistentTime = null;
		//
		// First get the value of the new last consistent time of the simulation (the
		// value is returned by this method).
		//
		if( ! levelsWhereReactionIsComputed.isEmpty() ){
			LevelIdentifier levelId = levelsWhereReactionIsComputed.iterator().next().getIdentifier();
			Transitory_PublicLocalDynamicState transitoryState = 
					(Transitory_PublicLocalDynamicState) this.transitoryDynamicStates.get( levelId );
			newLastConsistentTime = transitoryState.getNextTime();
		} else {
			throw new IllegalStateException( "Failed to determine the next time stamp of the simulation." );
		}
		//
		// Then perform a first loop where the reaction to system influences is handled before the reaction to
		// the regular influences.
		//
		InfluencesMap managedInfluences = this.systemReactionToSystemInfluences( levelsWhereReactionIsComputed );
		// Perform the user-defined reaction to these influences.
		this.userReactionToSystemInfluences(
				levelsWhereReactionIsComputed,
				managedInfluences, 
				true
		);
		//
		// Then perform the user-defined reaction to the regular influences of the levels where a reaction is computed.
		//
		this.userReactionToRegularInfluences( levelsWhereReactionIsComputed );
		//
		// Then perform a second loop where the reaction to system influences is handled adter the reaction to
		// the regular influences.
		//
		managedInfluences = this.systemReactionToSystemInfluences( levelsWhereReactionIsComputed );
		// Perform the user-defined reaction to these influences.
		this.userReactionToSystemInfluences(
				levelsWhereReactionIsComputed,
				managedInfluences, 
				false
		);
		//
		// Update the time and next time of the consistent and transitory states that computed a reaction.
		// Also sets the state dynamics of the consistent states as equal to the content of the transitory
		//
		for( I_Level level : levelsWhereReactionIsComputed ){
			LevelIdentifier levelId = level.getIdentifier();
			Consistent_PublicLocalDynamicState consistentState = 
					(Consistent_PublicLocalDynamicState) this.lastConsistentDynamicStates.get( levelId );
			Transitory_PublicLocalDynamicState transitoryState = 
					(Transitory_PublicLocalDynamicState) this.transitoryDynamicStates.get( levelId );
			// Update the time and next time of the last consistent and current transitory states.
			SimulationTimeStamp transitoryStateNewLowerBound = transitoryState.getNextTime();
			consistentState.setTime( transitoryStateNewLowerBound );
			SimulationTimeStamp transitoryStateNewHigherBound = level.getNextTime( transitoryStateNewLowerBound );
			transitoryState.setNextTime( transitoryStateNewHigherBound );
			// Move the influences contained into the transitory state into the consistent state.
			transitoryState.moveInfluencesToConsistentState( );
		}
		//
		// Change the value of the current dynamic state of the simulation.
		// It becomes equal to a combination of transitory states and consistent states
		//
		for( I_Level level : this.levels.values() ){
			if( levelsWhereReactionIsComputed.contains( level ) ){
				// A reaction was computed for this state.
				// Thus, the public dynamic state of the level seen by the probes is the consistent state.
				this.currentSimulationDynamicState.put( this.lastConsistentDynamicStates.get( level.getIdentifier() ) );
			} else {
				// No reaction was computed for this state.
				// Thus, the public dynamic state of the level seen by the probes is the transitory state.
				this.currentSimulationDynamicState.put( this.transitoryDynamicStates.get( level.getIdentifier() ) );
			}
		}
		// Finally return the new last time the simulation was partly-consistent.
		return newLastConsistentTime;
	}
	
	/**
	 * Performs a system reaction for the system influences that can be found in the levels contained in <code>lR</code>.
	 * <p>
	 * 	As a side effect, this method removes the system influences from the transitory state of the levels of <code>lR</code>.
	 * </p>
	 * @param lR The set containing all the levels having to perform a reaction.
	 * @return The system influences that were managed during the call of this method.
	 */
	private InfluencesMap systemReactionToSystemInfluences( Set<I_Level> lR ) {
		//
		// Create a map that will contain the influences that were managed during the call of this method.
		//
		InfluencesMap influencesManagedDuringSystemReaction = new InfluencesMap( );
		//
		// Create a map containing the influences to process in the next loop of the system reaction.
		//
		// At the end of each loop, the system influences contained in the state dynamics of transitory dynamic state
		// of the levels of 'lR' are removed (since they were processed by the loop).
		// Then, the system influences contained in the 'influences to process' map are put into the state dynamics of 
		// the public transitory dynamic state of the levels of the simulation (since they have yet to be processed by the loop).
		//
		// During a loop, the system reaction to system influences can produce other system influences (for instance
		// processing an 'add agent' influence will generate 'add public local state to level' influences). These influences
		// are added during the loop to the 'influences to process' map.
		//
		InfluencesMap influencesToProcessInNextLoop = new InfluencesMap( );
		do { 
			// Reset the content of the influences to process in the next loop (they are present in the transitory 
			// dynamic state of the levels).
			influencesToProcessInNextLoop.clear();
			//
			// Then process the system influences contained in the transitory state of the levels.
			//
			for( I_Level level : lR ){
				LevelIdentifier levelId = level.getIdentifier();
				Transitory_PublicLocalDynamicState transitoryState = 
						(Transitory_PublicLocalDynamicState) this.transitoryDynamicStates.get( levelId );
				for( I_Influence systemInfluence : transitoryState.getSystemInfluencesOfStateDynamics() ){
					// Handle the reaction to the influence.
					Set<I_Influence> producedInfluences = this.reactToSystemInfluence( systemInfluence );
					// If the reaction produced other influences, add them to the influence to process.
					if( producedInfluences != null ){
						for( I_Influence producedInfluence : producedInfluences ){
							influencesToProcessInNextLoop.add( producedInfluence );
						}
					}
				}
			}
			//
			// Then remove all system influences from the transitory dynamic states of the levels contained in lR, and
			// then dispatch all the influences contained in the 'influencesToProcessInNextLoop' map into the transitory dynamic
			// state of their corresponding levels.
			//
			for( LevelIdentifier levelId : influencesToProcessInNextLoop.getDefinedKeys() ){
				Transitory_PublicLocalDynamicState transitoryDynamicState = 
						(Transitory_PublicLocalDynamicState) this.transitoryDynamicStates.get( levelId );
				// First remove all the system influence if they were managed by the current loop in this
				// system reaction (i.e. if the level belongs to lR).
				if( lR.contains( this.levels.get( levelId ) ) ){
					transitoryDynamicState.clearSystemInfluences();
				}
				// Then include all the influences aimed at this level that were produced during the current loop.
				for( I_Influence influence : influencesToProcessInNextLoop.getInfluencesForLevel( levelId ) ){
					transitoryDynamicState.addInfluence( influence );
				}
			}
			// Continue the iteration if the 'influencesToProcessInNextLoop' map is not empty.
		} while( ! influencesToProcessInNextLoop.isEmpty() );
		
		return influencesManagedDuringSystemReaction;
	}
	
	/**
	 * Performs the operations in reaction to a specific system influence.
	 * @param systemInfluence The system influence for which a system reaction is computed.
	 * @return The influences that were produced in reaction to this influence. 
	 * <code>null</code> if no influences were produced.
	 */
	private Set<I_Influence> reactToSystemInfluence( I_Influence systemInfluence ){
		Set<I_Influence> producedInfluences = null;
		if( systemInfluence.getCategory().equals( SystemInfluence_AddAgent.CATEGORY ) ){
			//
			// Manage the system influence telling to add an agent into the simulation.
			//
			producedInfluences = new LinkedHashSet<I_Influence>( );
			SystemInfluence_AddAgent castedInfluence = (SystemInfluence_AddAgent) systemInfluence;
			I_Agent agentToAdd = castedInfluence.getAgent( );
			for( LevelIdentifier levelId : agentToAdd.getLevels() ){
				producedInfluences.add( new SystemInfluence_AddPublicLocalStateToDynamicState( 
						levelId,
						agentToAdd.getPublicLocalState( levelId ) 
				) );
			}
		} else if( systemInfluence.getCategory().equals( SystemInfluence_AddPublicLocalStateToDynamicState.CATEGORY ) ) {
			//
			// Manage the influence telling that the public local state of an agent is added in a level.
			//
			SystemInfluence_AddPublicLocalStateToDynamicState castedInfluence = 
					(SystemInfluence_AddPublicLocalStateToDynamicState) systemInfluence;
			I_Level level = this.levels.get( castedInfluence.getTargetLevel() );
			I_PublicLocalStateOfAgent addedLocalState = castedInfluence.getPublicLocalState();
			// Check the existence of the level where the public local state is added.
			if( level == null ){
				throw new IllegalStateException( "The influence '" + systemInfluence.getCategory() + "' tried to add the public " +
						"local state of an agent '" + addedLocalState.getOwner().getCategory() + "' to the " +
						"non-existing level '" + level + "'." );
			}
			// Add the public local state to the level.
			// The following instruction is equivalent to using the lastConsistentDynamicStates map.
			Consistent_PublicLocalDynamicState levelConsistentState = level.getLastConsistentPublicLocalDynamicState();
			levelConsistentState.addPublicLocalStateOfAgent( addedLocalState );
			// Add the agent to the list of agents contained in the level
			this.agents.get( level.getIdentifier() ).add( addedLocalState.getOwner() );
		} else if( systemInfluence.getCategory().equals( SystemInfluence_RemoveAgent.CATEGORY ) ){
			//
			// Manage the system influence telling to delete an agent from the simulation.
			//
			producedInfluences = new LinkedHashSet<I_Influence>( );
			SystemInfluence_RemoveAgent castedInfluence = (SystemInfluence_RemoveAgent) systemInfluence;
			I_Agent agentToRemove = castedInfluence.getAgent( );
			for( LevelIdentifier levelId : agentToRemove.getLevels() ){
				producedInfluences.add( new SystemInfluence_RemovePublicLocalStateFromDynamicState( 
						levelId,
						agentToRemove.getPublicLocalState( levelId ) 
				) );
			}
		} else if( systemInfluence.getCategory().equals( SystemInfluence_RemovePublicLocalStateFromDynamicState.CATEGORY ) ) {
			//
			// Manage the influence telling that the physical state of an agent disappears from a level.
			//
			SystemInfluence_RemovePublicLocalStateFromDynamicState castedInfluence = 
					(SystemInfluence_RemovePublicLocalStateFromDynamicState) systemInfluence;
			I_Level level = this.levels.get( castedInfluence.getTargetLevel() );
			I_PublicLocalStateOfAgent removedLocalState = castedInfluence.getPublicLocalState();
			// Check the existence of the level from which the public local state is removed.
			if( level == null ){
				throw new IllegalStateException( "The influence '" + systemInfluence.getCategory() + "' tried to remove the public " +
						"local state of an agent '" + removedLocalState.getOwner().getCategory() + "' from the " +
						"non-existing level '" + level + "'." );
			}
			// Remove the public local state from the level.
			// The following instruction is equivalent to using the lastConsistentDynamicStates map.
			Consistent_PublicLocalDynamicState levelConsistentState = level.getLastConsistentPublicLocalDynamicState();
			levelConsistentState.removePublicLocalStateOfAgent( removedLocalState );
			// Remove the agent from the list of agents contained in the level
			this.agents.get( level.getIdentifier() ).remove( removedLocalState.getOwner() );
		} else {
			throw new UnsupportedOperationException( "The system influence '" + systemInfluence.getCategory() + "' cannot " +
					"be managed by the '" + this.getClass().getSimpleName() + "' simulation engine." );
		}
		return producedInfluences;
	}
	
	/**
	 * Performs a user-defined reaction for the system influences that were managed during the system reaction.
	 * <p>
	 * 	As a side-effect, this method adds the influences produced by the user reaction into the transitory state.
	 * </p>
	 * @param lR The set containing all the levels having to perform a reaction.
	 * @param influencesManagedDuringSystemReaction The system influences that were managed during the system reaction
	 * to the system influences.
	 * @param beforeRegularReaction <code>true</code> if this method call happens before the user-defined reaction to regular influences.
	 */
	private void userReactionToSystemInfluences( 
			Set<I_Level> lR,
			InfluencesMap influencesManagedDuringSystemReaction,
			boolean beforeRegularReaction
	) {
		Set<I_Influence> newInfluencesToProcess = new LinkedHashSet<I_Influence>();
		//
		// Perform the user defined reaction in each level.
		//
		for( I_Level level : lR ){
			LevelIdentifier levelId = level.getIdentifier();
			Transitory_PublicLocalDynamicState transitoryState = 
					(Transitory_PublicLocalDynamicState) this.transitoryDynamicStates.get( levelId );
			level.makeSystemReaction(
					transitoryState.getTime(), 
					transitoryState.getNextTime(), 
					transitoryState.getLastConsistentDynamicState(), 
					influencesManagedDuringSystemReaction.getInfluencesForLevel( levelId ), 
					beforeRegularReaction, 
					newInfluencesToProcess
			);
		}
		//
		// Then add the produced influences to the transitory states
		//
		for( I_Influence influence : newInfluencesToProcess ){
			I_PublicLocalDynamicState rawDynamicState = this.transitoryDynamicStates.get( influence.getTargetLevel() );
			Transitory_PublicLocalDynamicState castedDynamicState = (Transitory_PublicLocalDynamicState) rawDynamicState;
			castedDynamicState.addInfluence( influence );
		}
	}
	
	/**
	 * Performs a user-defined reaction for the regular influences from the transitory dynamic state of the levels computing a reaction.
	 * <p>
	 * 	As a side-effect, this method removes the processed regular influences from the transitory state and the consistent state.
	 * 	The persisting regular influences still remain in the transitory states.
	 * </p>
	 * @param lR The levels where a reaction has to be computed.
	 */
	private void userReactionToRegularInfluences( 
			Set<I_Level> lR
	) {
		Set<I_Influence> influencesPersistingAfterUserReaction = new LinkedHashSet<I_Influence>( );
		for( I_Level level : lR ){
			LevelIdentifier levelId = level.getIdentifier( );
			Transitory_PublicLocalDynamicState transitoryState =
					(Transitory_PublicLocalDynamicState) this.transitoryDynamicStates.get( levelId );
			// Perform the user-defined reaction to the influences.
			level.makeRegularReaction(
					transitoryState.getTime(), 
					transitoryState.getNextTime(),
					transitoryState.getLastConsistentDynamicState(), 
					transitoryState.getRegularInfluencesOfStateDynamics(), 
					influencesPersistingAfterUserReaction
			);
			// Then reinitialize the regular influences contained in the state dynamics of the
			// consistent and transitory states to an empty set.
			transitoryState.clearRegularInfluences();
		}
		//
		// Add the persisting influences to the transitory dynamic state of the various levels.
		//
		for( I_Influence influence : influencesPersistingAfterUserReaction ){
			I_PublicLocalDynamicState rawDynamicState = this.transitoryDynamicStates.get( influence.getTargetLevel() );
			Transitory_PublicLocalDynamicState castedDynamicState = (Transitory_PublicLocalDynamicState) rawDynamicState;
			castedDynamicState.addInfluence( influence );
		}
	}
}
