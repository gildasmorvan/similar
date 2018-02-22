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

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationModel;
import fr.lgi2a.similar.microkernel.ISimulationModel.AgentInitializationData;
import fr.lgi2a.similar.microkernel.ISimulationModel.EnvironmentInitializationData;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.dynamicstate.IModifiablePublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.lgi2a.similar.microkernel.dynamicstate.TransitoryPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.environment.IEnvironment4Engine;
import fr.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;
import fr.lgi2a.similar.microkernel.influences.IInfluence;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgentToLevel;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar.microkernel.libs.abstractimpl.AbstractSimulationEngine;
import fr.lgi2a.similar.microkernel.libs.disambiguation.DisambiguationOperatorReturningLastConsistentState;

/**
 * An abstract implementation of the {@link fr.lgi2a.similar.microkernel.ISimulationEngine} interface, providing algorithms to the initialization
 * process of the simulation, using a single threaded approach: the initialization of the levels is made in sequence.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public abstract class AbstractSimulationEngineWithInitialization extends AbstractSimulationEngine {
	/**
	 * Models the public local dynamic states of the simulation being forwarded to the probes to 
	 * display the content of the simulation.
	 * This map contains either consistent dynamic states (if the current time stamp of the simulation is equal to
	 * the current time stamp of the level) or transitory dynamic state (if the level is currently between two of its
	 * time stamps).
	 */
	private DynamicStateMap currentSimulationDynamicState;
	/**
	 * A map associating a level to its identifier.
	 */
	private LinkedHashMap<LevelIdentifier, ILevel> levels;
	/**
	 * A map associating the agents lying in each level of the simulation to the identifier of the level.
	 */
	private LinkedHashMap<LevelIdentifier, LinkedHashSet<IAgent4Engine>> agents;
	/**
	 * The environment where the simulation takes place.
	 */
	private IEnvironment4Engine environment;
	/**
	 * A boolean flag telling if the simulation has to be aborted or not.
	 */
	private boolean abortFlag;
	
	/**
	 * Tells if the simulation has to be aborted or not.
	 * @return <code>true</code> if the user requested the abortion of the simulation.
	 */
	public boolean isAbortionRequested( ) {
		return this.abortFlag;
	}
	
	/**
	 * Builds an instance of this simulation engine.
	 */
	public AbstractSimulationEngineWithInitialization( ) {
		this.levels = new LinkedHashMap<LevelIdentifier, ILevel>( );
		this.agents = new LinkedHashMap<LevelIdentifier, LinkedHashSet<IAgent4Engine>>();
		this.resetData( );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<IAgent4Engine> getAgents() {
		Set<IAgent4Engine> agentsSet = new LinkedHashSet<IAgent4Engine>();
		for( LevelIdentifier level : this.getLevels() ){
			agentsSet.addAll( this.getAgents( level ) );
		}
		return agentsSet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<LevelIdentifier> getLevels() {
		return this.levels.keySet( );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<IAgent4Engine> getAgents(
			LevelIdentifier level
	) {
		Set<IAgent4Engine> agentsSet = this.agents.get( level );
		if( agentsSet == null ){
			throw new NoSuchElementException( "The simulation does not contain the level '" + level + "'." );
		}
		return agentsSet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IEnvironment4Engine getEnvironment( ){
		return this.environment;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConsistentPublicLocalDynamicState disambiguation(
			TransitoryPublicLocalDynamicState transitoryDynamicState
	) {
		return DisambiguationOperatorReturningLastConsistentState.disambiguation( transitoryDynamicState );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPublicDynamicStateMap getSimulationDynamicStates() {
		return this.currentSimulationDynamicState;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void requestSimulationAbortion() {
		this.abortFlag = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void runNewSimulation( ISimulationModel simulationModel ) {
		// First check that the simulation model is not null.
		this.checkArgumentValidity( simulationModel );
		// Prepare the observation made by the probes
		for( IProbe probe : this.getProbes() ){
			probe.prepareObservation();
		}
		SimulationTimeStamp currentTime = simulationModel.getInitialTime();
		try {
			if( currentTime == null ){
				throw new IllegalStateException( "The 'initial time' defined in the simulation model cannot be null. " );
			}
			this.runNewSimulationWithoutCheckingErrors( simulationModel );
		} catch( ExceptionSimulationAborted a ) {
			// Case where the simulation was aborted by the user.
			for( IProbe probe : this.getProbes() ){
				probe.reactToAbortion(
						a.getLastConsistentTimeBeforeAbortion(), 
						this
				);
			}
		} catch( Exception t ) {
			// In this case, both the simulation engine and the simulation model are in an
			// invalid state because of the error.
			// The simulation is stopped, and the probes are told to process the error.
			for( IProbe probe : this.getProbes() ){
				probe.reactToError(
						"The simulation stopped because of an error.", 
						t
				);
			}
		} finally {
			// Close the resources that were used by the probes
			for( IProbe probe : this.getProbes() ){
				probe.endObservation();
			}
		}
	}
	
	/**
	 * Checks the validity of the argument of the {@link AbstractSimulationEngineWithInitialization#runNewSimulation(ISimulationModel)} method.
	 * @param simulationModel The argument which validity is checked.
	 */
	private void checkArgumentValidity( 
			ISimulationModel simulationModel 
	) {
		if( simulationModel == null ) {
			throw new IllegalArgumentException( "The 'simulationModel' argument cannot be null." );
		}
	}
	
	/**
	 * Performs the same task than the {@link AbstractSimulationEngineWithInitialization#runNewSimulation(ISimulationModel)} method, 
	 * but without checking errors.
	 * @param simulationModel The simulation model used to generate a new simulation.
	 */
	public void runNewSimulationWithoutCheckingErrors(
			ISimulationModel simulationModel
	) {
		// Initialize the simulation
		this.initializeSimulation( simulationModel );
		// Tell the probes to observe the state of the simulation
		for( IProbe probe : this.getProbes() ){
			probe.observeAtInitialTimes( simulationModel.getInitialTime(), this );
		}
		// Run the simulation
		SimulationTimeStamp finalTime = this.performSimulation( simulationModel );
		// Update the dynamic state of the simulation, now that the ending time is reached:
		//	 + All transitory states become consistent states.
		// 	 + The state dynamics of the transitory states becomes the state dynamics of the consistent states.
		this.convertDynamicStatesToConsistent( 
			finalTime
		);
		// The probes observe the consistent dynamic state of the simulation at the final time.
		for( IProbe probe : this.getProbes() ){
			probe.observeAtFinalTime( finalTime, this );
		}
	}
	
	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//
	//
	//
	//
	//	SIMULATION INITIALIZATION RELATED METHODS
	//
	//
	//
	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//
	

	/**
	 * Initializes the simulation that will run.
	 * @param simulationModel The model of the simulation instance being run with this engine.
	 * @throws IllegalArgumentException if: 
	 * <ul>
	 * 	<li>No level was defined for this simulation.</li>
	 * 	<li>The name of a level is <code>null</code>.</li>
	 * 	<li>Two levels have the same name.</li>
	 * 	<li>The environment of the simulation is <code>null</code></li>
	 * </ul>
	 * @throws IllegalStateException If the simulation model contains errors.
	 */
	protected void initializeSimulation( 
			ISimulationModel simulationModel 
	) {
		//
		// First reset the data stored in this engine.
		//
		this.resetData();
		//
		// Then initialize the data stored in this engine
		//
		SimulationTimeStamp initialTime = simulationModel.getInitialTime();
		// Generate the list of the levels of the simulation.
		this.generateLevels( simulationModel, initialTime );
		// Create the environment where the simulation will take place, and
		// include its public local states to the dynamic state of the levels.
		InfluencesMap influencesProducedByEnvironment = this.createEnvironment( 
				simulationModel, 
				initialTime 
		);
		// Then create the agents in the initial state of the simulation.
		InfluencesMap influencesProducedByAgents = this.createAgents( simulationModel, initialTime );
		// Add the initial influences to the initial state dynamics of the public local dynamic state of the levels.
		this.dispatchInitialInfluences( influencesProducedByEnvironment, influencesProducedByAgents );
	}
	
	/**
	 * Resets the data stored inside this engine before running a new simulation.
	 */
	private void resetData( ) {
		this.abortFlag = false;
		this.agents.clear();
		this.levels.clear();
		this.currentSimulationDynamicState = new DynamicStateMap( );
	}
	
	/**
	 * Generates and memorizes into the engine the levels of the simulation.
	 * @param simulationModel The simulation model used to build the simulation being run with this engine. 
	 * This value is never equal to <code>null</code>.
	 * @param initialTime The initial time stamp of the simulation.
	 */
	private void generateLevels( 
			ISimulationModel simulationModel, 
			SimulationTimeStamp initialTime 
	) {
		List<ILevel> createdLevels = simulationModel.generateLevels( initialTime );
		// Check that the list is valid (not null, not empty).
		this.checkLevelsListValidity( createdLevels );
		// Add the levels to the engine while checking that each level is unique
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
			this.agents.put( levelId, new LinkedHashSet<IAgent4Engine>( ) );
			// Initialize the consistent and transitory states.
			generatedLevel.getLastConsistentState().setTime( initialTime );
			generatedLevel.getLastTransitoryState().setTransitoryPeriodMax( generatedLevel.getNextTime(
				initialTime
			) );
			// Tell that initially, the dynamic state of this level in the simulation is the consistent one
			// (it stays as such until the first simulation step is performed).
			this.currentSimulationDynamicState.put( generatedLevel.getLastConsistentState( ) );
		}
	}
	
	/**
	 * Checks the validity of the list of levels created by the {@link ISimulationModel#generateLevels(SimulationTimeStamp)} method.
	 * @param createdLevels The levels list that was created.
	 */
	private void checkLevelsListValidity( List<ILevel> createdLevels ){
		if( createdLevels == null ){
			throw new IllegalStateException( "The simulation model has to provide a valid list of levels. The list was null." );
		} else if( createdLevels.isEmpty( ) ){
			throw new IllegalStateException( "The simulation model has to contain at least one level." );
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
	) {
		//
		// Ask the model to create a new environment for the simulation.
		//
		EnvironmentInitializationData generationData = simulationModel.generateEnvironment( initialTime, this.levels );
		// Check that the data seem valid
		if( generationData == null ){
			throw new IllegalStateException( "The value returned by the generateEnvironment(...) method of the " +
					"simulation model cannot be null." );
		}
		if( generationData.getEnvironment() == null ){
			throw new IllegalStateException( "The environment of the simulation cannot be null. " +
					"Check the value returned by the generateEnvironment(...) method of the simulation model." );
		}
		this.environment = generationData.getEnvironment();
		//
		// Store the physical state of the environment in every level of the simulation.
		//
		for( ILevel level : this.levels.values() ){
			ILocalStateOfEnvironment environmentLocalState = this.environment.getPublicLocalState( level.getIdentifier() );
			if( environmentLocalState == null ){
				throw new IllegalStateException( "The public local state of the environment in the level '" + level.getIdentifier() + "'" +
						" cannot be null." );
			}
			level.getLastConsistentState().setPublicLocalStateOfEnvironment( 
					environmentLocalState
			);
		}
		// Return the initial influences defined during the generation of the environment.
		return generationData.getInfluences();
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
		if( generationData.getAgents() == null ){
			throw new IllegalStateException( "The environment of the simulation cannot be null. " +
					"Check the value returned by the generateAgents(...) method of the simulation model." );
		}
		//
		// Add the agents to the agents list. Also add the public local state of the agents to the appropriate levels.
		//
		Set<IAgent4Engine> agentsToAdd = generationData.getAgents();
		for( IAgent4Engine agent : agentsToAdd ){
			this.addAgentToSimulation( agent );
		}
		// Return the initial influences defined during the generation of the agents.
		return generationData.getInfluences( );
	}
	
	/**
	 * Adds an agent to this simulation.
	 * @param agent The agent to add to this simulation.
	 */
	private void addAgentToSimulation( IAgent4Engine agent ){
		for( LevelIdentifier levelId : agent.getLevels() ){
			if( levelId == null || ! this.levels.containsKey( levelId ) ){
				throw new IllegalStateException( "The agent '" + agent.getCategory() + "' from the class '" + agent.getClass( ) + "' " +
						"specified a public local state for a not existing level named '" + levelId + "'." );
			}
			// Add the agent to the list of agents in that level.
			this.agents.get( levelId ).add( agent );
			// Add the public local state of the agent to the public local dynamic state of the level.
			IModifiablePublicLocalDynamicState levelState = this.levels.get( levelId ).getLastConsistentState();
			ILocalStateOfAgent agentState = agent.getPublicLocalState( levelId );
			if( agentState == null ){
				throw new IllegalStateException( "The agent '" + agent.getCategory() + "' from the class '" + agent.getClass( ) + "' " +
						" defines a null public local state for the level '" + levelId + "'." );
			} else {
				levelState.addPublicLocalStateOfAgent( agentState );
				/* Custom reactions can be made when the public local state of the agent appears
				 * in a level. For instance, update the list of agents contained in a patch of the 
				 * environment. In the case of the initialization, agents are not added with a reaction:
				 * they are manually added by this method. Therefore, the customized user reaction to the
				 * addition of the agent in the level has to be triggered manually. */
				// TODO Ideally, the "createAgents" method should simply call a reaction to perform a clean
				// TODO addition of the agents.
				// Identify the level where the custom reaction takes place.
				ILevel level = this.levels.get( levelId );
				// Identify the time range of the reaction. Since we are initializing the simulation, they
				// are both the initial time of the simulation, which is also the time of the last consistent
				// state of the simulation.
				SimulationTimeStamp initialTime = level.getLastConsistentState().getTime();
				// Generate the list of influences that will be managed by the reaction. In this case, there
				// will be only one influence: a 
				List<IInfluence> influences = new LinkedList<IInfluence>( );
				influences.add( new SystemInfluenceAddAgentToLevel(
						initialTime, initialTime, 
						agent.getPublicLocalState( levelId ), 
						agent.getPrivateLocalState( levelId )
				) );
				// Create the object that will receive the influence generated by the user reaction.
				InfluencesMap userInfluences = new InfluencesMap( );
				// Perform the reaction.
				level.makeSystemReaction(
					initialTime, 
					initialTime, 
					level.getLastConsistentState(), 
					influences, 
					false, 
					userInfluences
				);
				// Finally include the generated influences into the transitory state of the simulation.
				for( LevelIdentifier lid : userInfluences.getDefinedKeys() ) {
					for( IInfluence influence : userInfluences.getInfluencesForLevel( lid ) ) {
						level.getLastTransitoryState().addInfluence(influence);
					}
				}
			}
		}
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
			ConsistentPublicLocalDynamicState state = this.levels.get( levelId ).getLastConsistentState();
			if( ! influencesProducedByEnvironment.isEmpty( levelId ) ){
				for( IInfluence influence : influencesProducedByEnvironment.getInfluencesForLevel( levelId ) ){
					state.addInfluence( influence );
				}
			}
			if( ! influencesProducedByAgents.isEmpty( levelId ) ){
				for( IInfluence influence : influencesProducedByAgents.getInfluencesForLevel( levelId ) ){
					state.addInfluence( influence );
				}
			}
		}
	}
	
	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//
	//
	//
	//
	//	SIMULATION EXECUTION RELATED METHODS
	//
	//
	//
	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//	//
	
	/**
	 * Runs the simulation, so that it will go from its initial state to its final state.
	 * @param simulationModel The model of the simulation.
	 * @return The final time stamp of the simulation.
	 * @throws ExceptionSimulationAborted If the simulation was aborted by the user.
	 * @throws Exception If an exception was caught while running the simulation.
	 */
	protected SimulationTimeStamp performSimulation( 
			ISimulationModel simulationModel 
	) {
		SimulationTimeStamp finalTime = this.performSimulation(
			simulationModel,
			this.currentSimulationDynamicState, 
			this.levels, 
			this.agents, 
			this.environment
		);
		if( this.isAbortionRequested() ){
			throw new ExceptionSimulationAborted( finalTime );
		}
		return finalTime;
	}
	
	/**
	 * Runs the simulation, so that it goes from its current initial state to its final state.
	 * <h2>Implementation instructions</h2>
	 * <p>
	 * 	This method has multiple roles along the execution of the simulation:
	 * </p>
	 * <ul>
	 * 	<li>
	 * 		Determine when the perception, global state revision and decision methods of the agents are called,
	 * 		using the value of the <code>levels</code>, <code>agents</code> and <code>environment</code> arguments.
	 * 	</li>
	 * 	<li>
	 * 		Determine when the natural method of the environment is called using the value of the <code>levels</code>, 
	 * 		<code>agents</code> and <code>environment</code> arguments.
	 * 	</li>
	 * 	<li>
	 * 		Determine when the reaction of a level is computed using the value of the <code>levels</code>, <code>agents</code> 
	 * 		and <code>environment</code> arguments.
	 * 	</li>
	 * 	<li>
	 * 		Determine how the reaction is computed:
	 * 		<ul>
	 * 			<li>
	 * 				Write how the system influences are managed: the addition of the public local state of an agent to a level implies the addition
	 * 				of the agent for that level in the <code>levels</code> argument.
	 * 			</li>
	 * 			<li>Include the produced influences either into the reaction of other levels, or in their transitory state dynamics</li>
	 * 		</ul>
	 * 	</li>
	 * 	<li>
	 * 		Update the content of the <code>levels</code>, <code>agents</code> and <code>environment</code>
	 * 		arguments according to the changes occurring during the execution of the simulation.
	 * 	</li>
	 * 	<li>
	 * 		When the simulation reaches an half-consistent state, this method has to update the value of the
	 * 		<code>currentSimulationDynamicState</code> argument to match the real state of the simulation:
	 * 		if a level is consistent, then its consistent state has to be put in <code>currentSimulationDynamicState</code>
	 * 		(see {@link ILevel#getLastConsistentState()}).
	 * 		If it is transitory, then its transitory state has to be put in <code>currentSimulationDynamicState</code> 
	 * 		(see {@link ILevel#getLastTransitoryState()});
	 * 	</li>
	 * 	<li>
	 * 		When the simulation reaches an half-consistent state, this method has the responsibility to call the 
	 * 		{@link IProbe#observeAtPartialConsistentTime(SimulationTimeStamp, fr.lgi2a.similar.microkernel.ISimulationEngine)}
	 * 		method of each probe of the simulation (after the operations described in the previous item of this list);
	 * 	</li>
	 * 	<li>
	 * 		After each reaction phase, check that the abortion flag is raised (see 
	 * 		{@link AbstractSimulationEngineWithInitialization#isAbortionRequested()}). If true, stop the execution of the simulation 
	 * 		and throw an {@link ExceptionSimulationAborted} exception.
	 * 	</li>
	 * </ul>
	 * @param simulationModel The model of the simulation being run.
	 * @param currentSimulationDynamicState Models the public local dynamic states of the simulation being forwarded to the probes to 
	 * display the content of the simulation.
	 * This map contains either consistent dynamic states (if the current time stamp of the simulation is equal to
	 * the current time stamp of the level) or transitory dynamic state (if the level is currently between two of its
	 * time stamps).
	 * @param levels A map associating a level to its identifier. This map initially contains the levels in their initial
	 * state.
	 * @param agents A map associating the agents lying in each level of the 
	 * simulation to the identifier of the level. This map contains the initial agents of the simulation, in their initial state.
	 * @param environment The environment where the simulation takes place. This environment is in its initial state.
	 * @return The final time stamp of the simulation.
	 */
	protected abstract SimulationTimeStamp performSimulation(
		ISimulationModel simulationModel,
		DynamicStateMap currentSimulationDynamicState,
		LinkedHashMap<LevelIdentifier, ILevel> levels,
		LinkedHashMap<LevelIdentifier, LinkedHashSet<IAgent4Engine>> agents,
		IEnvironment4Engine environment
	);
	
	/**
	 * Updates the dynamic state of the simulation, now that the ending time is reached:
	 * <ul>
	 * 	<li>All transitory states become consistent states</li>
	 * 	<li>The state dynamics of the transitory states becomes the state dynamics of the consistent states.</li>
	 * </ul>
	 * @param finalTime The final time of the simulation.
	 */
	private void convertDynamicStatesToConsistent( 
			SimulationTimeStamp finalTime
	) {
		InfluencesMap influencesAtEnd = new InfluencesMap( );
		// Store in a temporary variable all the influences that were lying in the transitory 
		// state dynamics of the levels.
		for( ILevel level : this.levels.values() ){
			TransitoryPublicLocalDynamicState transitoryState = level.getLastTransitoryState( );
			Set<IInfluence> dynamics = transitoryState.getStateDynamics();
			for( IInfluence influence : dynamics ){
				influencesAtEnd.add( influence );
			}
		}
		// Update the state dynamics of the transitory and consistent state
		for( ILevel level : this.levels.values() ){
			// Remove all the influences from the last transitory and last consistent state of that level.
			TransitoryPublicLocalDynamicState transitoryState = level.getLastTransitoryState();
			transitoryState.clearRegularInfluences( );
			transitoryState.clearSystemInfluences( );
			// Add the temporarily stored influences into the state dynamics of the consistent state of that level.
			ConsistentPublicLocalDynamicState consistentState = level.getLastConsistentState();
			consistentState.setStateDynamicsAsCopyOf( influencesAtEnd.getInfluencesForLevel( level.getIdentifier() ) );
			// Update the time stamp of the consistent state.
			consistentState.setTime( finalTime );
			// Update the time stamp of the transitory state.
			transitoryState.setTransitoryPeriodMax( new SimulationTimeStamp( Long.MAX_VALUE ) );
			// Tell that the last dynamic state of that level was the consistent one.
			this.currentSimulationDynamicState.put( level.getLastConsistentState() );
		}
	}
}
