/**
 * Copyright or � or Copr. LGI2A
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
package fr.lgi2a.similar.microkernel.generic.engines.tools;

import java.util.LinkedHashSet;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IDynamicStateMap;
import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.IPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.AbstractLearningAgent;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.LearningGlobalMemoryState;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.LearningPerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.LearningPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.influence.LearningInfluenceAgentPublicLocalStateUpdate;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.influence.LearningInfluenceEnvironmentPublicLocalStateUpdate;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.SimulationExecutionTrace;

/**
 * The concrete agent class used in the functional test of the simulation engine.
 * Agents of this class produce a modification influence for the public local state of the environment and of all the agents it can perceive.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class Test_Agent extends AbstractLearningAgent {
	/**
	 * Builds an initialized instance of this agent class.
	 * @param category The category of the agent. To make the trace of the simulation explicit,
	 * it is advised to use a different category for each agent instance.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public Test_Agent(
			String category, 
			SimulationExecutionTrace trace
	) throws IllegalArgumentException {
		super(category, trace);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AbstractLearningAgent createCopy() {
		Test_Agent copy = new Test_Agent( this.getCategory(), this.getTrace() );
		copy.initializeGlobalMemoryState( ((LearningGlobalMemoryState)this.getGlobalMemoryState()).createCopy() );
		for( LevelIdentifier levelId : this.getLevels() ){
			copy.includeNewLevel(
					levelId, 
					((LearningPublicLocalStateOfAgent)this.getPublicLocalState( levelId ) ).createCopy()
			);
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
		Set<IInfluence> influences = new LinkedHashSet<IInfluence>();
		IDynamicStateMap localDynamicStates = perceivedData.getLevelsPublicLocalObservableDynamicState();
		for( LevelIdentifier levelId : localDynamicStates.keySet() ){
			IPublicLocalDynamicState localDynamicState = localDynamicStates.get( levelId );
			influences.add(
					new LearningInfluenceEnvironmentPublicLocalStateUpdate( levelId, localDynamicState.getPublicLocalStateOfEnvironment() )
			);
			for( IPublicLocalStateOfAgent agentState : localDynamicState.getPublicLocalStateOfAgents() ){
				influences.add(
						new LearningInfluenceAgentPublicLocalStateUpdate( levelId, agentState )
				);
			}
		}
		return influences;
	}

}