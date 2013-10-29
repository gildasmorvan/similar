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
package fr.lgi2a.similar.microKernel.examples.oneLevelTwoAgentsTrace.environment;

import java.util.LinkedHashSet;
import java.util.Set;

import fr.lgi2a.similar.microKernel.examples.oneLevelTwoAgentsTrace.MyLevelIdentifiers;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.Learning_AbstractEnvironment;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.Learning_PublicLocalStateOfAgent;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.Learning_PublicLocalStateOfEnvironment;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.influence.Learning_Influence_AgentPublicLocalStateUpdate;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.influence.Learning_Influence_EnvironmentPublicLocalStateUpdate;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.SimulationExecutionTrace;
import fr.lgi2a.similar.microkernel.I_Influence;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.states.I_PublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.states.I_PublicLocalState;
import fr.lgi2a.similar.microkernel.states.I_PublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.states.dynamicstate.map.I_DynamicState_Map;

/**
 * Models the environment as described in the specification of the "one level - two agents - trace" simulation.
 * <h1>Constraints</h1>
 * <p>
 * 	The environment is an instance of the {@link Learning_AbstractEnvironment} class to ensure that the evolution of the environment 
 * 	can be tracked by the trace of the simulation.
 * </p>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class MyEnvironment extends Learning_AbstractEnvironment {
	/**
	 * Builds an initialized instance of the environment.
	 * @param trace The trace where the execution of the simulation is tracked.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public MyEnvironment(SimulationExecutionTrace trace)
			throws IllegalArgumentException {
		super(trace);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Set<I_Influence> produceInfluencesOfNatural(
			LevelIdentifier level,
			I_DynamicState_Map levelsPublicLocalObservableDynamicState
	) {
		// Create the set of influences that will be returned by the natural action of the environment.
		//
		// This method can only produce either system influences or influences from the following classes of the common libs:
		//		- Learning_Influence_AgentPublicLocalStateUpdate
		//		- Learning_Influence_EnvironmentPublicLocalStateUpdate
		//
		Set<I_Influence> producedInfluences = new LinkedHashSet<I_Influence>( );
		//
		// Iterate over the public local state of the agent, and tell that the environment requests their modification.
		//
		// First get the set containing the public local state of the agents in the 'level 1' level.
		I_PublicLocalDynamicState levelPublicLocalDynamicState = levelsPublicLocalObservableDynamicState.get( MyLevelIdentifiers.SIMULATION_LEVEL );
		for( I_PublicLocalStateOfAgent agentPublicLocalState : levelPublicLocalDynamicState.getPublicLocalStateOfAgents() ){
			// Cast the public local state of the agent in the appropriate class.
			Learning_PublicLocalStateOfAgent castedAgentState = (Learning_PublicLocalStateOfAgent) agentPublicLocalState;
			// Then create the influence telling that the environment requests the modification of this state.
			Learning_Influence_AgentPublicLocalStateUpdate influence = new Learning_Influence_AgentPublicLocalStateUpdate(
					castedAgentState.getLevel(),
					castedAgentState
			);
			// Add the influence to the returned influences.
			producedInfluences.add( influence );
		}
		//
		// Add an influence updating the value of the public local state of the environment.
		//
		I_PublicLocalState environmentPublicLocalState = levelPublicLocalDynamicState.getPublicLocalStateOfEnvironment();
		// Cast the public local state of the environment in the appropriate class.
		Learning_PublicLocalStateOfEnvironment castedEnvironmentState = (Learning_PublicLocalStateOfEnvironment) environmentPublicLocalState;
		// Then create the influence telling that the environment requests the modification of this state.
		Learning_Influence_EnvironmentPublicLocalStateUpdate envInfluence = new Learning_Influence_EnvironmentPublicLocalStateUpdate(
				level,
				castedEnvironmentState
		);
		// Add the influence to the returned influences.
		producedInfluences.add( envInfluence );
		//
		// Return the influences that were produced.
		//
		return producedInfluences;
	}
}
