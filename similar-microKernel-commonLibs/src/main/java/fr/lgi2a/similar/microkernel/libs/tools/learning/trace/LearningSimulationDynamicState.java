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
package fr.lgi2a.similar.microkernel.libs.tools.learning.trace;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IAgent;
import fr.lgi2a.similar.microkernel.IDynamicStateMap;
import fr.lgi2a.similar.microkernel.IGlobalMemoryState;
import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.IPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.dynamicstate.TransitoryPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.dynamicstate.map.DynamicStateMap;
import fr.lgi2a.similar.microkernel.libs.tools.learning.LearningInfluenceCopier;
import fr.lgi2a.similar.microkernel.libs.tools.learning.LearningPublicLocalDynamicStateCopier;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.LearningGlobalMemoryState;

/**
 * Models the dynamic state of a simulation during consistent or half-consistent moments.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class LearningSimulationDynamicState {
	/**
	 * The global memory state of agents, in this dynamic state.
	 */
	private Map<IAgent,LearningGlobalMemoryState> agentGlobalMemoryStates;
	/**
	 * The data structure containing the dynamic state of the levels.
	 */
	private IDynamicStateMap localDynamicStates;
	
	/**
	 * Builds an initially empty dynamic state of the simulation.
	 * <p>
	 * 	The initialization is completed by calling the {@link LearningSimulationDynamicState#addGlobalMemoryState(IAgent)} method
	 * 	for each agent in the simulation, and the {@link LearningSimulationDynamicState#addLevelDynamicState(IPublicLocalDynamicState)} 
	 * 	method for each level in the simulation.
	 * </p>
	 */
	public LearningSimulationDynamicState(){
		this.agentGlobalMemoryStates = new LinkedHashMap<IAgent, LearningGlobalMemoryState>();
		this.localDynamicStates = new DynamicStateMap();
	}
	
	/**
	 * Gets the list of agents located in the simulation.
	 * @return The list of agents located in the simulation.
	 */
	public Set<IAgent> getAgents( ) {
		return this.agentGlobalMemoryStates.keySet();
	}
	
	/**
	 * Adds the global memory state of an agent to this dynamic state.
	 * @param agent The agent which memory state is copied and stored in this object.
	 * @throws IllegalArgumentException If an argument is <code>null</code> or if the global 
	 * memory state of the agent is not an instance of the {@link LearningGlobalMemoryState} class.
	 */
	public void addGlobalMemoryState( IAgent agent ) {
		if( agent == null ){
			throw new IllegalArgumentException( "The 'agent' argument cannot be null." );
		}
		IGlobalMemoryState memory = agent.getGlobalMemoryState();
		if( memory instanceof LearningGlobalMemoryState ){
			LearningGlobalMemoryState casted = (LearningGlobalMemoryState) memory;
			this.agentGlobalMemoryStates.put( agent, casted.createCopy() );
		} else {
			throw new IllegalArgumentException( "The global memory state of the agent has to be an instance of the " + 
						LearningGlobalMemoryState.class + " class." );
		}
	}
	
	/**
	 * Gets the global memory state of an agent in this dynamic state.
	 * @param agent The agent which memory state is got from this object.
	 * @throws IllegalArgumentException If the argument is <code>null</code>.
	 * @throws NoSuchElementException If the agent is not present in the simulation.
	 */
	public LearningGlobalMemoryState getGlobalMemoryState( IAgent agent ) throws NoSuchElementException {
		if( agent == null ){
			throw new IllegalArgumentException( "The 'agent' argument cannot be null." );
		}
		LearningGlobalMemoryState result = this.agentGlobalMemoryStates.get( agent );
		if( result != null ){
			return result;
		} else {
			throw new NoSuchElementException( "The agent " + agent + " is not in this dynamic state." );
		}
	}

	/**
	 * Gets the data structure containing the dynamic state of the levels.
	 * @return The data structure containing the dynamic state of the levels.
	 */
	public IDynamicStateMap getLocalDynamicStates(){
		return this.localDynamicStates;
	}
	
	/**
	 * Adds a copy of the public local dynamic state of a level to this dynamic state.
	 * @param levelDynamicState The public local dynamic state of a level of the simulation.
	 * @throws IllegalArgumentException If an argument is <code>null</code> or if the copy of the dynamic state failed.
	 */
	public void addLevelDynamicState( IPublicLocalDynamicState levelDynamicState ) {
		if( levelDynamicState == null ){
			throw new IllegalArgumentException( "The 'levelDynamicState' argument cannot be null." );
		}
		if( levelDynamicState instanceof ConsistentPublicLocalDynamicState ){
			ConsistentPublicLocalDynamicState castedState = (ConsistentPublicLocalDynamicState) levelDynamicState;
			this.localDynamicStates.put( LearningPublicLocalDynamicStateCopier.createCopy( castedState ) );
		} else if( levelDynamicState instanceof TransitoryPublicLocalDynamicState ){
			TransitoryPublicLocalDynamicState castedState = (TransitoryPublicLocalDynamicState) levelDynamicState;
			TransitoryPublicLocalDynamicState stateCopy = new TransitoryPublicLocalDynamicState(
					LearningPublicLocalDynamicStateCopier.createCopy( castedState.getLastConsistentDynamicState() ), 
					new SimulationTimeStamp( castedState.getNextTime() )
			);
			for( IInfluence influence : castedState.getStateDynamics() ){
				if( ! castedState.getLastConsistentDynamicState().getStateDynamics().contains( influence ) ){
					stateCopy.addInfluence( LearningInfluenceCopier.copyInfluence( influence ) );
				}
			}
			this.localDynamicStates.put( stateCopy );
		} else {
			throw new IllegalArgumentException( "The dynamic state class '" + levelDynamicState.getClass().getSimpleName()
					+ "' is not supported in this simulation." );
		}
	}
}
