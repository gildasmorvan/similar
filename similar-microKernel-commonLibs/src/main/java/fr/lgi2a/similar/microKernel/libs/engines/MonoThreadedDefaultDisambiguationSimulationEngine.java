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
package fr.lgi2a.similar.microkernel.libs.engines;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import fr.lgi2a.similar.microkernel.ExceptionSimulationAborted;
import fr.lgi2a.similar.microkernel.IAgent;
import fr.lgi2a.similar.microkernel.IEnvironment;
import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.ILevel;
import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationModel;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.ISimulationModel.AgentInitializationData;
import fr.lgi2a.similar.microkernel.ISimulationModel.EnvironmentInitializationData;
import fr.lgi2a.similar.microkernel.agentbehavior.IPerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.agentbehavior.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluence_AddAgent;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluence_AddPublicLocalStateToDynamicState;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluence_RemoveAgent;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluence_RemovePublicLocalStateFromDynamicState;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractSimulationEngine;
import fr.lgi2a.similar.microkernel.libs.generic.EmptyPerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.states.IPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.states.IPublicLocalState;
import fr.lgi2a.similar.microkernel.states.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.states.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.states.dynamicstate.IModifiablePublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.states.dynamicstate.TransitoryPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.states.dynamicstate.map.DynamicStateFilteredMap;
import fr.lgi2a.similar.microkernel.states.dynamicstate.map.DynamicStateMap;
import fr.lgi2a.similar.microkernel.states.dynamicstate.map.IDynamicStateMap;

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
public class MonoThreadedDefaultDisambiguationSimulationEngine extends AbstractSimulationEngine {
	/**
	 * Models the public local dynamic states of the simulation being forwarded to the probes to 
	 * display the content of the simulation.
	 * This map contains either consistent dynamic states (if the current time stamp of the simulation is equal to
	 * the current time stamp of the level) or transitory dynamic state (if the level is currently between two of its
	 * time stamps).
	 */
	private IDynamicStateMap currentSimulationDynamicState;
	/**
	 * A map associating a level to its identifier.
	 */
	private Map<LevelIdentifier, ILevel> levels;
	/**
	 * A map associating the agents lying in each level of the simulation to the identifier of the level.
	 */
	private Map<LevelIdentifier, Set<IAgent>> agents;
	/**
	 * The environment where the simulation takes place.
	 */
	private IEnvironment environment;
	/**
	 * Stores the last consistent dynamic state of each level of the simulation.
	 */
	private IDynamicStateMap lastConsistentDynamicStates;
	/**
	 * Stores the current transitory dynamic state of each level of the simulation.
	 */
	private IDynamicStateMap transitoryDynamicStates;
	/**
	 * A boolean flag telling if the simulation has to be aborted or not.
	 */
	private boolean abortFlag;
	
	/**
	 * Builds a mono-threaded simulation engine.
	 */
	public MonoThreadedDefaultDisambiguationSimulationEngine( ) {
		this.levels = new HashMap<LevelIdentifier, ILevel>( );
		this.agents = new HashMap<LevelIdentifier, Set<IAgent>>();
		this.abortFlag = false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDynamicStateMap getSimulationDynamicStates() {
		return this.currentSimulationDynamicState;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<IAgent> getAgents() {
		Set<IAgent> agents = new HashSet<IAgent>();
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
	public Set<IAgent> getAgents(
			LevelIdentifier level
	) throws NoSuchElementException {
		Set<IAgent> agents = this.agents.get( level );
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
	public IPublicLocalDynamicState disambiguation(
			TransitoryPublicLocalDynamicState transitoryDynamicState
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
			ISimulationModel simulationModel
	) throws RuntimeException, ExceptionSimulationAborted {
		// First check that the simulation model is not null.
		if( simulationModel == null ) {
			throw new IllegalArgumentException( "The 'simulationModel' argument cannot be null." );
		}
		// Prepare the observation made by the probes
		for( IProbe probe : this.probes.values() ){
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
			for( IProbe probe : this.probes.values() ){
				probe.observeAtInitialTimes( simulationModel.getInitialTime(), this );
			}
			// Run the simulation
			SimulationTimeStamp finalTime = this.performSimulation( simulationModel );
			// The probes observe the partly consistent dynamic state of the simulation
			for( IProbe probe : this.probes.values() ){
				probe.observeAtFinalTime( finalTime, this );
			}
		} catch( ExceptionSimulationAborted a ) {
			// Case where the simulation was aborted by the user.
			for( IProbe probe : this.probes.values() ){
				probe.reactToAbortion(
						currentTime, 
						this
				);
			}
		} catch( Exception t ) {
			// In this case, both the simulation engine and the simulation model are in an
			// invalid state because of the error.
			// The simulation is stopped, and the probes are told to process the error.
			for( IProbe probe : this.probes.values() ){
				probe.reactToError(
						"The simulation stopped because of an error.", 
						t
				);
			}
		} finally {
			// Close the resources that were used by the probes
			for( IProbe probe : this.probes.values() ){
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
	protected void initializeSimulation( ISimulationModel simulationModel ) throws IllegalStateException {
		// First reset the data that were used in the previous simulations.
		this.agents.clear();
		this.lastConsistentDynamicStates = new DynamicStateMap( );
		this.transitoryDynamicStates = new DynamicStateMap( );
		this.currentSimulationDynamicState = new DynamicStateMap( );
		this.levels.clear();
		this.abortFlag = false;
		//
		// Then generate the list of the levels of the simulation.
		//
		SimulationTimeStamp initialTime = simulationModel.getInitialTime();
		List<ILevel> createdLevels = simulationModel.generateLevels( initialTime );
		// Check that the list is valid.
		if( createdLevels == null ){
			throw new IllegalStateException( "The simulation model has to provide a valid list of levels. The list was null." );
		} else if( createdLevels.isEmpty( ) ){
			throw new IllegalStateException( "The simulation model has to contain at least one level." );
		} else {
			for( ILevel generatedLevel : createdLevels ){
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
		for( ILevel level : this.levels.values() ){
			LevelIdentifier levelId = level.getIdentifier( );
			this.agents.put( levelId, new LinkedHashSet<IAgent>( ) );
			this.lastConsistentDynamicStates.put( level.getLastConsistentPublicLocalDynamicState() );
			this.transitoryDynamicStates.put(
					new TransitoryPublicLocalDynamicState( 
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
		for( ILevel level : this.levels.values() ){
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
			ISimulationModel simulationModel, 
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
		for( ILevel level : this.levels.values() ){
			IPublicLocalState environmentLocalState = this.environment.getPublicLocalState( level.getIdentifier() );
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
			ISimulationModel simulationModel, 
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
		Set<IAgent> agents = generationData.agents;
		for( IAgent agent : agents ){
			for( LevelIdentifier levelId : agent.getLevels() ){
				if( levelId == null || ! this.levels.containsKey( levelId ) ){
					throw new IllegalStateException( "The agent '" + agent.getCategory() + "' from the class '" + agent.getClass( ) + "' " +
							"specified a public local state for a not existing level named '" + levelId + "'." );
				}
				// Add the agent to the list of agents in that level.
				this.agents.get( levelId ).add( agent );
				// Add the public local state of the agent to the public local dynamic state of the level.
				IModifiablePublicLocalDynamicState levelState = (IModifiablePublicLocalDynamicState) 
						this.lastConsistentDynamicStates.get( levelId );
				IPublicLocalStateOfAgent agentState = agent.getPublicLocalState( levelId );
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
				for( IInfluence influence : influencesProducedByEnvironment.getInfluencesForLevel( levelId ) ){
					ConsistentPublicLocalDynamicState state = 
							(ConsistentPublicLocalDynamicState) this.lastConsistentDynamicStates.get( levelId );
					state.addInfluence( influence );
				}
			}
			if( ! influencesProducedByAgents.isEmpty( levelId ) ){
				for( IInfluence influence : influencesProducedByAgents.getInfluencesForLevel( levelId ) ){
					ConsistentPublicLocalDynamicState state = 
							(ConsistentPublicLocalDynamicState) this.lastConsistentDynamicStates.get( levelId );
					state.addInfluence( influence );
				}
			}
		}
	}
	
	/**
	 * Runs the simulation, so that it will go from its initial state to its final state.
	 * @param simulationModel The model of the simulation.
	 * @return The final time stamp of the simulation.
	 * @throws ExceptionSimulationAborted If the simulation was aborted by the user.
	 * @throws Exception If an exception was caught while running the simulation.
	 */
	protected SimulationTimeStamp performSimulation( 
			ISimulationModel simulationModel 
	) throws ExceptionSimulationAborted {
		// Iterate over all the time stamps of the simulation
		SimulationTimeStamp lastPartlyConsistentStateTimestamp = simulationModel.getInitialTime();
		//
		// Handle the first step of the simulation: no reaction has to be computed for the initial time.
		//
		if( this.isSimulationContinuing( lastPartlyConsistentStateTimestamp, simulationModel ) ) {
			// Ask all the agents to perceive
			Set<IAgent> agentHavingToReviseMemory = this.perceptionPhase( 
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
			for( IProbe probe : this.probes.values() ){
				probe.observeAtPartialConsistentTime( lastPartlyConsistentStateTimestamp, this );
			}
			// Then check if it models the ending time of the simulation.
			if( this.isSimulationContinuing( lastPartlyConsistentStateTimestamp, simulationModel ) ){
				// The simulation does not stop: perception, memory revision, natural and decision have to be performed.
				// Ask the appropriate agents to perceive.
				Set<ILevel> levelsWherePerceptionNaturalAndDesicionAreMade = this.buildLp( lastPartlyConsistentStateTimestamp );
				Set<IAgent> agentHavingToReviseMemory = this.perceptionPhase( 
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
		for( ILevel level : this.levels.values() ){
			// Store in a temporary variable all the influences that were lying in the level.
			TransitoryPublicLocalDynamicState transitoryState = (TransitoryPublicLocalDynamicState)
					this.transitoryDynamicStates.get( level.getIdentifier() );
			Set<IInfluence> dynamics = transitoryState.getStateDynamics();
			for( IInfluence influence : dynamics ){
				influencesAtEnd.add( influence );
			}
			// Change the current time of the last consistent dynamic state of the level to the final time of the simulation.
			ConsistentPublicLocalDynamicState consistentState = 
					(ConsistentPublicLocalDynamicState) this.lastConsistentDynamicStates.get( level.getIdentifier() );
			consistentState.setTime( lastPartlyConsistentStateTimestamp );
		}
		// Store the state dynamics of the levels at the end of the simulation and update the set describing the current dynamic state
		// of the simulation.
		for( ILevel level : this.levels.values() ){
			ConsistentPublicLocalDynamicState consistentState = 
					(ConsistentPublicLocalDynamicState) this.lastConsistentDynamicStates.get( level.getIdentifier() );
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
			ISimulationModel simulationModel 
	) {
		return ! this.abortFlag && ! simulationModel.isFinalTimeOrAfter( lastPartlyConsistentStateTimestamp, this );
	}

	/**
	 * Builds the list of the levels that are currently in a consistent state. These levels are consequently beginning a new
	 * transitory phase (if the simulation did not end), and have to ask agents to perceive.
	 * @param currentTime The current simulation time.
	 * @return The list of levels that are currently in a consistent state.
	 */
	protected Set<ILevel> buildLp( SimulationTimeStamp currentTime ){
		Set<ILevel> lP = new LinkedHashSet<ILevel>( );
		for( ILevel level : this.levels.values( ) ){
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
	protected Set<IAgent> perceptionPhase( 
			SimulationTimeStamp currentTime, 
			Collection<ILevel> lP 
	) {
		//
		// Initialize the set returned by this method.
		//
		Set<IAgent> result = new LinkedHashSet<IAgent>( );
		//
		// Iterate over the levels to trigger the perception of the agents.
		//
		for( ILevel level : lP ){
			LevelIdentifier levelId = level.getIdentifier();
			IDynamicStateMap perceptibleLocalDynamicStates = new DynamicStateFilteredMap(
					this.lastConsistentDynamicStates, 
					level.getPerceptibleLevels()
			);
			for( IAgent agent : this.agents.get( levelId ) ){
				// Perform the perception
				IPerceivedDataOfAgent perceivedData = agent.perceive(
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
	protected void memorizationPhase( Set<IAgent> agentsHavingToMemorize ){
		for( IAgent agent : agentsHavingToMemorize ){
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
			Collection<ILevel> lI
	){
		InfluencesMap producedInfluences = new InfluencesMap();
		for( ILevel level : lI ){
			LevelIdentifier levelId = level.getIdentifier( );
			IDynamicStateMap perceptibleLocalDynamicStates = new DynamicStateFilteredMap(
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
			for( IAgent agent : this.agents.get( levelId ) ) {
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
				IPublicLocalDynamicState rawDynamicState = this.transitoryDynamicStates.get( levelId );
				IModifiablePublicLocalDynamicState castedDynamicState = (IModifiablePublicLocalDynamicState) rawDynamicState;
				for( IInfluence influence : influences.getInfluencesForLevel( levelId ) ){
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
	protected Set<ILevel> buildLr( SimulationTimeStamp lastConsistentTime ){
		Set<ILevel> lR = new LinkedHashSet<ILevel>( );
		SimulationTimeStamp nextTime = null;
		for( ILevel level : this.levels.values( ) ){
			LevelIdentifier levelId =  level.getIdentifier();
			TransitoryPublicLocalDynamicState transitoryState = 
					(TransitoryPublicLocalDynamicState) this.transitoryDynamicStates.get( levelId );
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
		Set<ILevel> levelsWhereReactionIsComputed = this.buildLr( lastConsistentTime );
		SimulationTimeStamp newLastConsistentTime = null;
		//
		// First get the value of the new last consistent time of the simulation (the
		// value is returned by this method).
		//
		if( ! levelsWhereReactionIsComputed.isEmpty() ){
			LevelIdentifier levelId = levelsWhereReactionIsComputed.iterator().next().getIdentifier();
			TransitoryPublicLocalDynamicState transitoryState = 
					(TransitoryPublicLocalDynamicState) this.transitoryDynamicStates.get( levelId );
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
		for( ILevel level : levelsWhereReactionIsComputed ){
			LevelIdentifier levelId = level.getIdentifier();
			ConsistentPublicLocalDynamicState consistentState = 
					(ConsistentPublicLocalDynamicState) this.lastConsistentDynamicStates.get( levelId );
			TransitoryPublicLocalDynamicState transitoryState = 
					(TransitoryPublicLocalDynamicState) this.transitoryDynamicStates.get( levelId );
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
		for( ILevel level : this.levels.values() ){
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
	private InfluencesMap systemReactionToSystemInfluences( Set<ILevel> lR ) {
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
			for( ILevel level : lR ){
				LevelIdentifier levelId = level.getIdentifier();
				TransitoryPublicLocalDynamicState transitoryState = 
						(TransitoryPublicLocalDynamicState) this.transitoryDynamicStates.get( levelId );
				for( IInfluence systemInfluence : transitoryState.getSystemInfluencesOfStateDynamics() ){
					// Handle the reaction to the influence.
					Set<IInfluence> producedInfluences = this.reactToSystemInfluence( systemInfluence );
					// If the reaction produced other influences, add them to the influence to process.
					if( producedInfluences != null ){
						for( IInfluence producedInfluence : producedInfluences ){
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
				TransitoryPublicLocalDynamicState transitoryDynamicState = 
						(TransitoryPublicLocalDynamicState) this.transitoryDynamicStates.get( levelId );
				// First remove all the system influence if they were managed by the current loop in this
				// system reaction (i.e. if the level belongs to lR).
				if( lR.contains( this.levels.get( levelId ) ) ){
					transitoryDynamicState.clearSystemInfluences();
				}
				// Then include all the influences aimed at this level that were produced during the current loop.
				for( IInfluence influence : influencesToProcessInNextLoop.getInfluencesForLevel( levelId ) ){
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
	private Set<IInfluence> reactToSystemInfluence( IInfluence systemInfluence ){
		Set<IInfluence> producedInfluences = null;
		if( systemInfluence.getCategory().equals( SystemInfluence_AddAgent.CATEGORY ) ){
			//
			// Manage the system influence telling to add an agent into the simulation.
			//
			producedInfluences = new LinkedHashSet<IInfluence>( );
			SystemInfluence_AddAgent castedInfluence = (SystemInfluence_AddAgent) systemInfluence;
			IAgent agentToAdd = castedInfluence.getAgent( );
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
			ILevel level = this.levels.get( castedInfluence.getTargetLevel() );
			IPublicLocalStateOfAgent addedLocalState = castedInfluence.getPublicLocalState();
			// Check the existence of the level where the public local state is added.
			if( level == null ){
				throw new IllegalStateException( "The influence '" + systemInfluence.getCategory() + "' tried to add the public " +
						"local state of an agent '" + addedLocalState.getOwner().getCategory() + "' to the " +
						"non-existing level '" + level + "'." );
			}
			// Add the public local state to the level.
			// The following instruction is equivalent to using the lastConsistentDynamicStates map.
			ConsistentPublicLocalDynamicState levelConsistentState = level.getLastConsistentPublicLocalDynamicState();
			levelConsistentState.addPublicLocalStateOfAgent( addedLocalState );
			// Add the agent to the list of agents contained in the level
			this.agents.get( level.getIdentifier() ).add( addedLocalState.getOwner() );
		} else if( systemInfluence.getCategory().equals( SystemInfluence_RemoveAgent.CATEGORY ) ){
			//
			// Manage the system influence telling to delete an agent from the simulation.
			//
			producedInfluences = new LinkedHashSet<IInfluence>( );
			SystemInfluence_RemoveAgent castedInfluence = (SystemInfluence_RemoveAgent) systemInfluence;
			IAgent agentToRemove = castedInfluence.getAgent( );
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
			ILevel level = this.levels.get( castedInfluence.getTargetLevel() );
			IPublicLocalStateOfAgent removedLocalState = castedInfluence.getPublicLocalState();
			// Check the existence of the level from which the public local state is removed.
			if( level == null ){
				throw new IllegalStateException( "The influence '" + systemInfluence.getCategory() + "' tried to remove the public " +
						"local state of an agent '" + removedLocalState.getOwner().getCategory() + "' from the " +
						"non-existing level '" + level + "'." );
			}
			// Remove the public local state from the level.
			// The following instruction is equivalent to using the lastConsistentDynamicStates map.
			ConsistentPublicLocalDynamicState levelConsistentState = level.getLastConsistentPublicLocalDynamicState();
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
			Set<ILevel> lR,
			InfluencesMap influencesManagedDuringSystemReaction,
			boolean beforeRegularReaction
	) {
		Set<IInfluence> newInfluencesToProcess = new LinkedHashSet<IInfluence>();
		//
		// Perform the user defined reaction in each level.
		//
		for( ILevel level : lR ){
			LevelIdentifier levelId = level.getIdentifier();
			TransitoryPublicLocalDynamicState transitoryState = 
					(TransitoryPublicLocalDynamicState) this.transitoryDynamicStates.get( levelId );
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
		for( IInfluence influence : newInfluencesToProcess ){
			IPublicLocalDynamicState rawDynamicState = this.transitoryDynamicStates.get( influence.getTargetLevel() );
			TransitoryPublicLocalDynamicState castedDynamicState = (TransitoryPublicLocalDynamicState) rawDynamicState;
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
			Set<ILevel> lR
	) {
		Set<IInfluence> influencesPersistingAfterUserReaction = new LinkedHashSet<IInfluence>( );
		for( ILevel level : lR ){
			LevelIdentifier levelId = level.getIdentifier( );
			TransitoryPublicLocalDynamicState transitoryState =
					(TransitoryPublicLocalDynamicState) this.transitoryDynamicStates.get( levelId );
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
		for( IInfluence influence : influencesPersistingAfterUserReaction ){
			IPublicLocalDynamicState rawDynamicState = this.transitoryDynamicStates.get( influence.getTargetLevel() );
			TransitoryPublicLocalDynamicState castedDynamicState = (TransitoryPublicLocalDynamicState) rawDynamicState;
			castedDynamicState.addInfluence( influence );
		}
	}
}
