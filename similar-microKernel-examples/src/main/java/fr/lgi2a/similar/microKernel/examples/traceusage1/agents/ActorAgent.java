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

import java.util.LinkedHashSet;
import java.util.Set;

import fr.lgi2a.similar.microkernel.I_Influence;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.examples.traceusage1.MyLevelIdentifiers;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.Learning_AbstractAgent;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.Learning_GlobalMemoryState;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.Learning_PerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.Learning_PublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.influence.Learning_Influence_AgentPublicLocalStateUpdate;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.SimulationExecutionTrace;
import fr.lgi2a.similar.microkernel.states.I_PublicLocalStateOfAgent;

/**
 * Models the agents of the 'actor' category, as described in the specification of the "one level - two agents - trace" simulation.
 * <h1>Constraints</h1>
 * <p>
 * 	The agent is an instance of the {@link Learning_AbstractAgent} class to ensure that the evolution of the agent 
 * 	can be tracked by the trace of the simulation.
 * </p>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class ActorAgent extends Learning_AbstractAgent {
	/**
	 * The category of this agent class, <i>i.e.</i> a unique identifier.
	 */
	public static final String CATEGORY = "Actor";
	
	/**
	 * Builds an initialized instance of the 'Actor' agent category.
	 * @param trace The trace where the execution of the simulation is tracked.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public ActorAgent(
			SimulationExecutionTrace trace
	) throws IllegalArgumentException {
		super( CATEGORY, trace);
		// Initialize the global memory state of the agent.
		// This state has to be an instance of the Learning_GlobalMemoryState class from the common libs to ensure that the evolution of that
		// state can be tracked by the trace of the simulation.
		this.initializeGlobalMemoryState( new Learning_GlobalMemoryState( this ) );
		// Create the public local state of the agent in the various levels where it lies.
		// This state has to be an instance of the Learning_PublicLocalStateOfAgent class from the common libs to ensure that the evolution of that
		// state can be tracked by the trace of the simulation.
		LevelIdentifier levelId = MyLevelIdentifiers.SIMULATION_LEVEL;
		this.includeNewLevel( levelId, new Learning_PublicLocalStateOfAgent( levelId, this ) );
	}

	/**
	 * Note: this method is only necessary in a simulation using the tracing method of the "learning" library of the common libs.
	 * {@inheritDoc}
	 */
	@Override
	public Learning_AbstractAgent createCopy() {
		// Create the agent embedding the copy of this instance.
		ActorAgent copy = new ActorAgent( this.getTrace() );
		// Create a copy of the global memory state of the agent.
		Learning_GlobalMemoryState castedGlobalMemoryState = (Learning_GlobalMemoryState) this.getGlobalMemoryState();
		copy.initializeGlobalMemoryState( castedGlobalMemoryState.createCopy() );
		// Create the copy of the public local states of the agent.
		// Iterate over the set of levels where the agent lies.
		for( LevelIdentifier levelId : this.getLevels() ){
			// Get the public local state of the agent in the level of the iteration.
			I_PublicLocalStateOfAgent agentState = this.getPublicLocalState( levelId );
			Learning_PublicLocalStateOfAgent castedAgentState = (Learning_PublicLocalStateOfAgent) agentState;
			// Add a copy of this state to the copy of the agent.
			copy.includeNewLevel( levelId, castedAgentState.createCopy() );
		}
		return copy;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Set<I_Influence> produceInfluencesOfDecision(
			LevelIdentifier level, Learning_GlobalMemoryState memoryState,
			Learning_PerceivedDataOfAgent perceivedData
	) {
		// Create the set of influences that will be returned by the decision of the agent.
		//
		// This method can only produce either system influences or influences from the following classes of the common libs:
		//		- Learning_Influence_AgentPublicLocalStateUpdate
		//		- Learning_Influence_EnvironmentPublicLocalStateUpdate
		//
		Set<I_Influence> producedInfluences = new LinkedHashSet<I_Influence>( );
		//
		// During decision, the agent chooses to perform a modification of its own state.
		// Thus, the agent produces an influence. In the context of this simulation, the produced influence
		// is an instance of the Learning_Influence_AgentPublicLocalStateUpdate class from the common libs.
		//
		// Get the level where the influence will be processed.
		LevelIdentifier targetLevel = MyLevelIdentifiers.SIMULATION_LEVEL;
		// Get the updated public local state of the agent.
		I_PublicLocalStateOfAgent stateToUpdate = this.getPublicLocalState( targetLevel );
		Learning_Influence_AgentPublicLocalStateUpdate influence = new Learning_Influence_AgentPublicLocalStateUpdate( 
				targetLevel, 
				stateToUpdate 
		);
		producedInfluences.add( influence );
		// Return the influences that were produced.
		return producedInfluences;
	}
}
