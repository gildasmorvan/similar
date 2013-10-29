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

import java.util.Collection;
import java.util.Set;

import fr.lgi2a.similar.microKernel.libs.abstractImplementations.AbstractLevel;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.influence.I_LearningInfluence;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.influence.Learning_Influence_AgentPublicLocalStateUpdate;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.influence.Learning_Influence_EnvironmentPublicLocalStateUpdate;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.Learning_EngineOperationMoment;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.SimulationExecutionTrace;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.operations.Learning_EngineOperation_RegularReaction;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.operations.Learning_EngineOperation_SystemReaction;
import fr.lgi2a.similar.microkernel.I_Influence;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.states.dynamicstate.Consistent_PublicLocalDynamicState;

/**
 * Models a level in the 'learning' simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public abstract class Learning_Level extends AbstractLevel {
	/**
	 * The trace where the execution of the simulation is tracked.
	 */
	private SimulationExecutionTrace trace;
	
	/**
	 * Builds an almost initialized level for the 'learning' simulation.
	 * @param initialTime The initial time stamp of the level. It has to be equal 
	 * to the initial time stamp of the simulation.
	 * @param identifier The identifier of the level.
	 * @param trace The trace where the execution of the simulation is tracked.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public Learning_Level(
			SimulationTimeStamp initialTime,
			LevelIdentifier identifier,
			SimulationExecutionTrace trace
	) throws IllegalArgumentException {
		super(initialTime, identifier);
		if( trace == null ){
			throw new IllegalArgumentException( "The 'trace' argument cannot be null." );
		}
		this.trace = trace;
	}

	/**
	 * {@inheritDoc}
	 * @see fr.lgi2a.similar.microkernel.I_Level#makeRegularReaction(fr.lgi2a.similar.microkernel.SimulationTimeStamp, fr.lgi2a.similar.microkernel.SimulationTimeStamp, fr.lgi2a.similar.microkernel.states.dynamicstate.Consistent_PublicLocalDynamicState, java.util.Set, java.util.Set)
	 */
	@Override
	public void makeRegularReaction(
			SimulationTimeStamp previousConsistentStateTime,
			SimulationTimeStamp newConsistentStateTime,
			Consistent_PublicLocalDynamicState newConsistentState,
			Set<I_Influence> regularInfluencesOftransitoryStateDynamics,
			Set<I_Influence> newInfluencesToProcess
	) {
		Learning_EngineOperation_RegularReaction operation = new Learning_EngineOperation_RegularReaction(
				previousConsistentStateTime,
				newConsistentStateTime,
				newConsistentState
		);
		// Memorize the argument influences
		for( I_Influence influence : regularInfluencesOftransitoryStateDynamics ){
			operation.addArgumentInfluence( influence );
		}
		// Process the influences
		for( I_Influence influence : regularInfluencesOftransitoryStateDynamics ){
			if( influence instanceof Learning_Influence_AgentPublicLocalStateUpdate ){
				Learning_Influence_AgentPublicLocalStateUpdate casted = (Learning_Influence_AgentPublicLocalStateUpdate) influence;
				casted.getPublicLocalStateOfAgent().revise();
			} else if( influence instanceof Learning_Influence_EnvironmentPublicLocalStateUpdate ){
				Learning_Influence_EnvironmentPublicLocalStateUpdate casted = (Learning_Influence_EnvironmentPublicLocalStateUpdate) influence;
				casted.getPublicLocalStateOfEnvironment().revise();
			} else if( influence instanceof I_LearningInfluence ) {
				newConsistentState.addInfluence( influence );
			} else {
				throw new IllegalArgumentException( "The '" + influence.getClass().getSimpleName() + "' class of influence is not managed by this level." );
			}
		}
		// Include user defined influences
		this.produceNewInfluencesDuringRegularReaction(
				previousConsistentStateTime, 
				newConsistentStateTime, 
				newConsistentState, 
				regularInfluencesOftransitoryStateDynamics, 
				newInfluencesToProcess
		);
		// Memorize the value of the consistent state after the execution of this reaction.
		operation.setNewConsistentStateAtEnd( newConsistentState );
		this.trace.addEngineOperation(
				new Learning_EngineOperationMoment.Learning_EngineOperationMoment_Before( new SimulationTimeStamp( newConsistentStateTime ) ), 
				operation
		);
	}
	
	/**
	 * This method can be overridden by users to define a custom reaction to the regular influences.
	 * <h1>Usage</h1>
	 * <p>
	 * 	This method has to include into the <code>newInfluencesToProcess</code> set the influences produced by this method.
	 * 	This method shall not modify the value of the other arguments of this method.
	 * 	Moreover, the influences that can be produced are limited to instances of the following regular influences:
	 * </p>
	 * <ul>
	 * 	<li> {@link Learning_Influence_AgentPublicLocalStateUpdate} to notify the reaction that the public local state of an agent is modified once. </li>
	 * 	<li> {@link Learning_Influence_EnvironmentPublicLocalStateUpdate} to notify the reaction that the public local state of the environment is modified once. </li>
	 * </ul>
	 * <p>
	 * 	Note that the system influences will be immediately processed if a reaction is being performed for the level they are sent to.
	 * 	Otherwise, the influences will be processed during the next reaction of the level they are sent to.
	 * </p>
	 * @param previousConsistentStateTime The previous time stamp when the dynamic state of this level was consistent, 
	 * <i>i.e.</i> the starting time of the transitory phase being ended by this reaction (<code>t<sub>l</sub></code> in the description
	 * of the method).
	 * @param newConsistentStateTime The next time stamp when the dynamic state of this level will be consistent, 
	 * <i>i.e.</i> the ending time of the transitory phase being ended by this reaction (<code>t<sub>l</sub>+dt<sub>l</sub></code> in the description
	 * of the method).
	 * @param consistentState The consistent state being modified by this user reaction.
	 * The operations performed in this reaction participate in the transition of the consistent dynamic state 
	 * <code>&delta;<sub>l</sub>(t<sub>l</sub>)</code> into its new value <code>&delta;<sub>l</sub>(t<sub>l</sub>+dt<sub>l</sub>)</code>.
	 * @param regularInfluencesOftransitoryStateDynamics The <b>regular</b> influences that have to be managed by this reaction to go from the 
	 * previous consistent state to the next consistent state of the level.
	 * @param remainingInfluences The set that will contain the influences that were produced by the user during the invocation of 
	 * this method, or the influences that persist after this reaction.
	 */
	protected void produceNewInfluencesDuringRegularReaction(
			SimulationTimeStamp previousConsistentStateTime,
			SimulationTimeStamp newConsistentStateTime,
			Consistent_PublicLocalDynamicState newConsistentState,
			Set<I_Influence> regularInfluencesOftransitoryStateDynamics,
			Set<I_Influence> newInfluencesToProcess
	){ }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeSystemReaction(
			SimulationTimeStamp previousConsistentStateTime,
			SimulationTimeStamp newConsistentStateTime,
			Consistent_PublicLocalDynamicState newConsistentState,
			Collection<I_Influence> systemInfluencesToManage,
			boolean happensBeforeRegularReaction,
			Collection<I_Influence> newInfluencesToProcess
	) {
		Learning_EngineOperation_SystemReaction operation = new Learning_EngineOperation_SystemReaction(
				previousConsistentStateTime, 
				newConsistentStateTime, 
				happensBeforeRegularReaction, 
				newConsistentState
		);
		for( I_Influence influence : systemInfluencesToManage ){
			operation.addArgumentInfluence( influence );
		}
		operation.setNewConsistentStateAtEnd( newConsistentState );
		this.trace.addEngineOperation(
				new Learning_EngineOperationMoment.Learning_EngineOperationMoment_Before( new SimulationTimeStamp( newConsistentStateTime ) ), 
				operation
		);
	}

	/**
	 * {@inheritDoc}
	 * @see fr.lgi2a.similar.microkernel.I_TimeModel#getNextTime(fr.lgi2a.similar.microkernel.SimulationTimeStamp)
	 */
	@Override
	public abstract SimulationTimeStamp getNextTime( SimulationTimeStamp currentTime );

	/**
	 * Gets the trace where the execution of the simulation is tracked.
	 * @return The trace where the execution of the simulation is tracked.
	 */
	public SimulationExecutionTrace getTrace( ) {
		return this.trace;
	}
}
