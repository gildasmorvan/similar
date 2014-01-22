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
package fr.lgi2a.similar.microkernel.examples.traceusage1.agents;

import java.util.Set;

import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.examples.traceusage1.levels.MyLevelIdentifiers;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.AbstractLearningAgent;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.LearningGlobalMemoryState;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.LearningPerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.LearningPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.SimulationExecutionTrace;

/**
 * Models agents from the 'procrastinator' category, as described in the specification of the "one level - two agents - trace" simulation.
 * <h1>Constraints</h1>
 * <p>
 * 	The agent is an instance of the {@link AbstractLearningAgent} class to ensure that the evolution of the agent 
 * 	can be tracked by the trace of the simulation.
 * </p>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class ProcrastinatorAgent extends AbstractLearningAgent {
	/**
	 * The category of this agent class, <i>i.e.</i> a unique identifier.
	 */
	public static final String CATEGORY = "Procrastinator";
	
	/**
	 * Builds an initialized instance of the 'procrastinator' agent category.
	 * @param trace The trace where the execution of the simulation is tracked.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public ProcrastinatorAgent(
			SimulationExecutionTrace trace
	) {
		super( TraceUsage1AgentCategories.PROCRASTINATOR, trace);
		// Initialize the global memory state of the agent.
		// This state has to be an instance of the Learning_GlobalMemoryState class from the common libs to ensure that the evolution of that
		// state can be tracked by the trace of the simulation.
		this.initializeGlobalMemoryState( new LearningGlobalMemoryState( this ) );
		// Create the public local state of the agent in the various levels where it lies.
		// This state has to be an instance of the Learning_PublicLocalStateOfAgent class from the common libs to ensure that the evolution of that
		// state can be tracked by the trace of the simulation.
		LevelIdentifier levelId = MyLevelIdentifiers.SIMULATION_LEVEL;
		this.includeNewLevel( levelId, new LearningPublicLocalStateOfAgent( levelId, this ) );
	}

	/**
	 * Note: this method is only necessary in a simulation using the tracing method of the "learning" library of the common libs.
	 * {@inheritDoc}
	 */
	@Override
	public AbstractLearningAgent createCopy() {
		// Create the agent embedding the copy of this instance.
		ProcrastinatorAgent copy = new ProcrastinatorAgent( this.getTrace() );
		// Create a copy of the global memory state of the agent.
		LearningGlobalMemoryState castedGlobalMemoryState = (LearningGlobalMemoryState) this.getGlobalMemoryState();
		copy.initializeGlobalMemoryState( castedGlobalMemoryState.createCopy() );
		// Create the copy of the public local states of the agent.
		// Iterate over the set of levels where the agent lies.
		for( LevelIdentifier levelId : this.getLevels() ){
			// Get the public local state of the agent in the level of the iteration.
			IPublicLocalStateOfAgent agentState = this.getPublicLocalState( levelId );
			LearningPublicLocalStateOfAgent castedAgentState = (LearningPublicLocalStateOfAgent) agentState;
			// Add a copy of this state to the copy of the agent.
			copy.includeNewLevel( levelId, castedAgentState.createCopy() );
		}
		return copy;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Set<IInfluence> produceInfluencesOfDecision(
			LevelIdentifier level, LearningGlobalMemoryState memoryState,
			LearningPerceivedDataOfAgent perceivedData
	) {
		// Create the set of influences that will be returned by this method call.
		//
		// This method can only produce either system influences or influences from the following classes of the common libs:
		//		- Learning_Influence_AgentPublicLocalStateUpdate
		//		- Learning_Influence_EnvironmentPublicLocalStateUpdate
		//
		// This agent does nothing.
		// Thus, this method can return null.
		return null;
	}
}
