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
package fr.lgi2a.similar.microkernel.libs.tools.learning;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.lgi2a.similar.microkernel.ILevel;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractSimulationModel;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.AbstractLearningAgent;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.AbstractLearningEnvironment;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.AbstractLearningLevel;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.LearningPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.LearningPublicLocalStateOfEnvironment;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.SimulationExecutionTrace;

/**
 * This simulation model is designed to help users to understand the algorithm used to run a simulation.
 * It builds a full trace of the operations performed during the execution of a simulation.
 * 
 * <h1>Usage</h1>
 * <p>
 * 	TODO
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public abstract class AbstractLearningSimulationModel extends AbstractSimulationModel {
	/**
	 * The final time stamp of the simulation.
	 */
	private final SimulationTimeStamp finalTime;
	/**
	 * The trace of the execution of the current simulation.
	 */
	private SimulationExecutionTrace trace;

	/**
	 * Builds an instance of an abstract simulation model, having a specific time stamp as initial time.
	 * @param initialTime The initial time stamp of this simulation model.
	 * @param finalTime The final time stamp of this simulation model, <i>i.e.</i> the time preceding the computation 
	 * of the last reaction of the simulation.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public AbstractLearningSimulationModel(
			SimulationTimeStamp initialTime,
			SimulationTimeStamp finalTime
	){
		super( initialTime );
		if( finalTime == null ){
			throw new IllegalArgumentException( "The 'finalTime' argumen cannot be null." );
		}
		this.finalTime = finalTime;
		this.trace = new SimulationExecutionTrace( );
	}
	
	/**
	 * The trace of the execution of the current simulation.
	 */
	public SimulationExecutionTrace getTrace(){
		return this.trace;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<ILevel> generateLevels( SimulationTimeStamp initialTime ) {
		this.trace.clearTrace();
		List<AbstractLearningLevel> castedLevels = this.generateCastedLevels( initialTime, this.trace );
		List<ILevel> result = new LinkedList<ILevel>();
		result.addAll( castedLevels );
		return result;
	}
	
	/**
	 * Generates the bare levels of the simulation. These levels contain no agents and define no environment.
	 * <p>
	 * 	The instances have to define:
	 * </p>
	 * <ul>
	 * 	<li>the identifier of the level, as an argument of the constructor</li>
	 * 	<li>how the model moves through time, by overriding the <code>getNextTime( ... )</code> method </li>
	 * 	<li>optionally a custom reaction to system influences, by overriding the <code>produceNewInfluencesDuringRegularReaction(...)</code> method</li>
	 * </ul>
	 * @param initialTime The initial time of the simulation. This argument has to be forwarded to the constructor of the levels.
	 * @param trace The object where the trace of the simulation will be registered. This argument has to be forwarded to the constructor of the levels.
	 * @return The bare levels of the simulation.
	 */
	protected abstract List<AbstractLearningLevel> generateCastedLevels( SimulationTimeStamp initialTime, SimulationExecutionTrace trace );

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final EnvironmentInitializationData generateEnvironment(
			SimulationTimeStamp initialTime,
			Map<LevelIdentifier, ILevel> levels
	) {
		AbstractLearningEnvironment environment = this.createEnvironment( this.trace );
		for( LevelIdentifier levelId : levels.keySet() ){
			environment.includeNewLevel( levelId, new LearningPublicLocalStateOfEnvironment( levelId ) );
		}
		return new EnvironmentInitializationData( environment );
	}
	
	/**
	 * Creates a new instance of the environment where the simulation takes place.
	 * The main role of this method is to create an environment defining its natural action over time.
	 * The creation and addition of public local states is handled automatically by this abstract class.
	 * @param trace The object where the trace of the simulation will be registered.
	 * @return A new instance of the environment where the simulation takes place.
	 */
	protected abstract AbstractLearningEnvironment createEnvironment( SimulationExecutionTrace trace );

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AgentInitializationData generateAgents(
			SimulationTimeStamp initialTime,
			Map<LevelIdentifier, ILevel> levels
	) {
		AgentInitializationData result = new AgentInitializationData( );
		for( AbstractLearningAgent agent : this.createAgents( this.trace ) ){
			result.agents.add( agent );
		}
		return result;
	}
	
	/**
	 * Creates the list of agents that will lie in the simulation.
	 * During the creation of the agents, this method is responsible for:
	 * <ul>
	 * 	<li>
	 * 		Determine the levels where the agent lies, using the 
	 * 		{@link AbstractLearningAgent#includeNewLevel(LevelIdentifier, fr.lgi2a.similar.microKernel.states.I_PublicLocalStateOfAgent)} method
	 * 		with an instance of the {@link LearningPublicLocalStateOfAgent} class as public local state.
	 * 	</li>
	 * 	<li>
	 * 		Determine the behavior of the decision operation of the agent.
	 * 	</li>
	 * </ul>
	 * @param trace The object where the trace of the simulation will be registered.
	 * @return The list of agents that will lie in the simulation.
	 */
	protected abstract List<AbstractLearningAgent> createAgents( SimulationExecutionTrace trace ); 

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFinalTimeOrAfter(
			SimulationTimeStamp currentTime,
			ISimulationEngine engine
	) {
		return this.finalTime.compareTo( currentTime ) <= 0;
	}
}
