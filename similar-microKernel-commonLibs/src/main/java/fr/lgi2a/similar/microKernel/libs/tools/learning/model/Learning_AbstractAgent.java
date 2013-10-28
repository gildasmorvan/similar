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
package fr.lgi2a.similar.microKernel.libs.tools.learning.model;

import java.util.Map;
import java.util.Set;

import fr.lgi2a.similar.microKernel.I_Influence;
import fr.lgi2a.similar.microKernel.LevelIdentifier;
import fr.lgi2a.similar.microKernel.SimulationTimeStamp;
import fr.lgi2a.similar.microKernel.agentBehavior.I_PerceivedDataOfAgent;
import fr.lgi2a.similar.microKernel.agentBehavior.InfluencesMap;
import fr.lgi2a.similar.microKernel.influences.systemInfluences.SystemInfluence_AddAgent;
import fr.lgi2a.similar.microKernel.influences.systemInfluences.SystemInfluence_RemoveAgent;
import fr.lgi2a.similar.microKernel.libs.abstractImplementations.AbstractAgent;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.influence.Learning_Influence_AgentPublicLocalStateUpdate;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.influence.Learning_Influence_EnvironmentPublicLocalStateUpdate;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.Learning_EngineOperationMoment;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.SimulationExecutionTrace;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.operations.Learning_EngineOperation_Decision;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.operations.Learning_EngineOperation_Perception;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.operations.Learning_EngineOperation_ReviseMemory;
import fr.lgi2a.similar.microKernel.states.I_GlobalMemoryState;
import fr.lgi2a.similar.microKernel.states.I_PublicLocalStateOfAgent;
import fr.lgi2a.similar.microKernel.states.dynamicStates.map.I_DynamicState_Map;

/**
 * Models an agent in the 'learning' simulation.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public abstract class Learning_AbstractAgent extends AbstractAgent {
	/**
	 * The trace where the execution of the simulation is tracked.
	 */
	private SimulationExecutionTrace trace;
	
	/**
	 * Builds an initialized instance of this agent class.
	 * @param category The category of the agent. To make the trace of the simulation explicit,
	 * it is advised to use a different category for each agent instance.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public Learning_AbstractAgent( 
			String category, 
			SimulationExecutionTrace trace 
	) throws IllegalArgumentException {
		super(category);
		if( trace == null ){
			throw new IllegalArgumentException( "The 'trace' argument cannot be null." );
		}
		this.trace = trace;
	}
	
	/**
	 * Gets the trace where the execution of the simulation is tracked.
	 * @return The trace where the execution of the simulation is tracked.
	 */
	public SimulationExecutionTrace getTrace(){
		return this.trace;
	}
	
	/**
	 * Creates a copy of this agent.
	 * <p>
	 * 	Warning: The copy has to include a copy of:
	 * </p>
	 * <ul>
	 * 	<li>The global memory state of the agent</li>
	 * 	<li>The public local state of the agent</li>
	 * </ul>
	 * @return The copy of this agent.
	 * @see Learning_GlobalMemoryState#createCopy()
	 * @see Learning_PublicLocalStateOfAgent#createCopy()
	 */
	public abstract Learning_AbstractAgent createCopy( );

	/**
	 * {@inheritDoc}
	 * @see fr.lgi2a.similar.microKernel.I_Agent#perceive(fr.lgi2a.similar.microKernel.LevelIdentifier, fr.lgi2a.similar.microKernel.states.I_PublicLocalStateOfAgent, fr.lgi2a.similar.microKernel.states.dynamicStates.map.I_DynamicState_Map)
	 */
	@Override
	public I_PerceivedDataOfAgent perceive(
			LevelIdentifier level,
			I_PublicLocalStateOfAgent publicLocalStateInLevel,
			I_DynamicState_Map levelsPublicLocalObservableDynamicState
	) {
		if( ! ( publicLocalStateInLevel instanceof Learning_PublicLocalStateOfAgent ) ){
			 throw new IllegalArgumentException( "The public local state of agents have to be instances of the '" + Learning_PublicLocalStateOfAgent.class.getSimpleName() + "' class." );
		}
		Learning_PublicLocalStateOfAgent castedState = (Learning_PublicLocalStateOfAgent) publicLocalStateInLevel;
		Learning_PerceivedDataOfAgent result = new  Learning_PerceivedDataOfAgent(
				level, 
				castedState
		);
		for( LevelIdentifier perceptibleLevel : levelsPublicLocalObservableDynamicState.keySet() ){
			result.addObservableDynamicState( levelsPublicLocalObservableDynamicState.get( perceptibleLevel ) );
		}
		Learning_EngineOperation_Perception operation = new Learning_EngineOperation_Perception(
				level, 
				castedState, 
				result
		);
		for( LevelIdentifier perceptibleLevel : levelsPublicLocalObservableDynamicState.keySet() ){
			operation.addObservableDynamicState( levelsPublicLocalObservableDynamicState.get( perceptibleLevel ) );
		}
		this.trace.addEngineOperation(
				new Learning_EngineOperationMoment.Learning_EngineOperationMoment_After( levelsPublicLocalObservableDynamicState.get( level ).getTime() ), 
				operation
		);
		return result;
	}

	/**
	 * {@inheritDoc}
	 * @see fr.lgi2a.similar.microKernel.I_Agent#reviseMemory(java.util.Map, fr.lgi2a.similar.microKernel.states.I_GlobalMemoryState)
	 */
	@Override
	public void reviseMemory(
			Map<LevelIdentifier, I_PerceivedDataOfAgent> perceivedData,
			I_GlobalMemoryState memoryState
	) {
		if( ! (memoryState instanceof Learning_GlobalMemoryState) ){
			throw new IllegalArgumentException( "The global memory state of an agent has to be an instance of the '" + Learning_GlobalMemoryState.class.getSimpleName() + "' class." );
		}
		Learning_GlobalMemoryState castedMemory = (Learning_GlobalMemoryState) memoryState;
		Learning_GlobalMemoryState initialMemoryCopy = castedMemory.createCopy();
		//
		// The next instruction simulates a revision of the memory state
		//
		castedMemory.revise();
		//
		// The rest of these instructions are dedicated to the registration of this memory revision operation to the trace of the simulation.
		//
		Learning_EngineOperation_ReviseMemory operation = new Learning_EngineOperation_ReviseMemory( 
				initialMemoryCopy, 
				castedMemory 
		);
		// Compute the moment when the memory revision was made.
		// This loop also copies the perceived data into the operation.
		SimulationTimeStamp memorizationTime = null;
		for( LevelIdentifier level : perceivedData.keySet() ){
			I_PerceivedDataOfAgent rawData = perceivedData.get( level );
			if( ! ( rawData instanceof Learning_PerceivedDataOfAgent ) ){
				throw new IllegalArgumentException( "The perceived data of agents have to be instances of the '" + Learning_PerceivedDataOfAgent.class.getSimpleName() + "' class" );
			}
			Learning_PerceivedDataOfAgent castedData = (Learning_PerceivedDataOfAgent) rawData;
			SimulationTimeStamp levelTime = castedData.getLevelsPublicLocalObservableDynamicState().get( level ).getTime();
			if( memorizationTime == null || levelTime.compareTo( memorizationTime ) > 0 ){
				memorizationTime = levelTime;
			}
			operation.addPerceivedData( perceivedData.get( level ) );
		}
		this.trace.addEngineOperation(
				new Learning_EngineOperationMoment.Learning_EngineOperationMoment_After( memorizationTime ), 
				operation
		);
	}

	/**
	 * {@inheritDoc}
	 * @see fr.lgi2a.similar.microKernel.I_Agent#decide(fr.lgi2a.similar.microKernel.LevelIdentifier, fr.lgi2a.similar.microKernel.states.I_GlobalMemoryState, fr.lgi2a.similar.microKernel.agentBehavior.I_PerceivedDataOfAgent, fr.lgi2a.similar.microKernel.agentBehavior.InfluencesMap)
	 */
	@Override
	public void decide(
			LevelIdentifier level, 
			I_GlobalMemoryState memoryState,
			I_PerceivedDataOfAgent perceivedData,
			InfluencesMap producedInfluences
	) {
		if( ! ( memoryState instanceof Learning_GlobalMemoryState ) ){
			throw new IllegalArgumentException( "The 'memoryState' argument has to be an instance of the '" + Learning_GlobalMemoryState.class.getSimpleName() + "' class." );
		}
		if( ! ( perceivedData instanceof Learning_PerceivedDataOfAgent ) ){
			throw new IllegalArgumentException( "The 'perceivedData' argument has to be an instance of the '" + Learning_PerceivedDataOfAgent.class.getSimpleName() + "' class." );
		}
		Set<I_Influence> influences = this.produceInfluencesOfDecision( 
				level, 
				(Learning_GlobalMemoryState) memoryState, 
				(Learning_PerceivedDataOfAgent) perceivedData 
		);
		Learning_EngineOperation_Decision operation = new Learning_EngineOperation_Decision(
				level, 
				memoryState, 
				perceivedData
		);
		for( I_Influence influence : influences ){
			operation.addInfluence( influence );
			producedInfluences.add( influence );
		}
		SimulationTimeStamp levelTimestamp = ( ( Learning_PerceivedDataOfAgent ) perceivedData ).getLevelsPublicLocalObservableDynamicState().get( level ).getTime();
		this.trace.addEngineOperation(
				new Learning_EngineOperationMoment.Learning_EngineOperationMoment_After( levelTimestamp ), 
				operation
		);
	}
	
	/**
	 * An abstract method where users can define which influences are produced by the agent during its decision phase.
	 * The following influences can be produced:
	 * <ul>
	 * 	<li>{@link SystemInfluence_AddAgent}, with an instance of the {@link Learning_AbstractAgent} class.</li>
	 * 	<li>{@link SystemInfluence_RemoveAgent}, with a public local state of an agent located in the perceived data.</li>
	 * 	<li> 
	 * 		{@link Learning_Influence_AgentPublicLocalStateUpdate}, with a public local state of an agent located in the perceived data.
	 * 		During the reaction, this influence triggers a modification of the public local
	 * 		state, increasing its revision number. This operation models any modification 
	 * 		that can be made in the public local state of an agent.
	 * 	</li>
	 * <li> 
	 * 		{@link Learning_Influence_EnvironmentPublicLocalStateUpdate}, with a public local state of the environment 
	 * 		located in the perceived data.
	 * 		During the reaction, this influence triggers a modification of the public local
	 * 		state, increasing its revision number. This operation models any modification 
	 * 		that can be made in the public local state of the environment.
	 * 	</li>
	 * </ul>
	 * <p>
	 * 	Warning: do not change the values contained in the arguments !
	 * 	Otherwise, the produced trace will be invalid.
	 * </p>
	 * @param level The level from which perception is made.
	 * @param memoryState The global memory state of the agent when the decision is made.
	 * @param perceivedData The data that were perceived by the agent.
	 * @return The set of influences that are produced by the decision of the agent.
	 */
	protected abstract Set<I_Influence> produceInfluencesOfDecision(
			LevelIdentifier level, 
			Learning_GlobalMemoryState memoryState,
			Learning_PerceivedDataOfAgent perceivedData
	);
}
