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
package fr.lgi2a.similar.microKernel;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import fr.lgi2a.similar.microKernel.agentBehavior.I_PerceivedDataOfAgent;
import fr.lgi2a.similar.microKernel.agentBehavior.InfluencesMap;
import fr.lgi2a.similar.microKernel.states.I_GlobalMemoryState;
import fr.lgi2a.similar.microKernel.states.I_PublicLocalDynamicState;
import fr.lgi2a.similar.microKernel.states.I_PublicLocalStateOfAgent;
import fr.lgi2a.similar.microKernel.states.dynamicStates.Transitory_PublicLocalDynamicState;
import fr.lgi2a.similar.microKernel.states.dynamicStates.map.I_DynamicState_Map;

/**
 * Models an agent contained in the simulation.
 * <p>
 * 	An agent is characterized by a category, telling which kind of agent it models.
 * </p>
 * 
 * <h1>Correspondence with theory</h1>
 * <p>
 * 	TODO formal notation
 * </p>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public interface I_Agent {
	/**
	 * Gets the 'category' of the agent. This method identifies the category of agents to which this instance belongs to.
	 * <p>
	 * 	This method can return the name of this class, or any other string representation modeling the equivalence 
	 * 	class of the agent.
	 * </p>
	 * <p>
	 * 	<b>Examples:</b>
	 * </p>
	 * <ul>
	 * 	<li>Car</li>
	 * 	<li>Prey</li>
	 * 	<li>Water drop</li>
	 * </ul>
	 * @return The 'category' of the agent.
	 */
	String getCategory( );
	
	/**
	 * Gets the collection of the levels where this agent lies, <i>i.e.</i> the levels where this agent 
	 * has a local state.
	 * <p>
	 * 	TODO : formal notation
	 * </p>
	 * @return A collection containing the identifier of the levels where this agent lies.
	 */
	Set<LevelIdentifier> getLevels( );
	
	/**
	 * Gets the public local state of the agent located in a specific level of the simulation.
	 * <p>
	 * 	TODO : formal notation
	 * </p>
	 * @param levelIdentifier The identifier of the level of the public local state, TODO : formal notation.
	 * @return The public local state of the agent in the specified level.
	 * @throws NoSuchElementException If the agent does not define a public local state for the provided level identifier.
	 */
	I_PublicLocalStateOfAgent getPublicLocalState( LevelIdentifier levelIdentifier ) throws NoSuchElementException;
	
	/**
	 * Gets the global memory state of the agent.
	 * <p>
	 * 	TODO : formal notation
	 * </p>
	 * @return The global memory state of the agent.
	 */
	I_GlobalMemoryState getGlobalMemoryState();
	
	/**
	 * Revises the value of the global memory state of the agent to a new value.
	 * <p>
	 * 	TODO : formal notation
	 * </p>
	 * @param newState The new value of the global memory state.
	 */
	void setGlobalMemoryState( I_GlobalMemoryState newState );
	
	/**
	 * Gets the data that were lastly perceived by the agent, for all the levels where it lies.
	 * <p>
	 * 	TODO : formal notation
	 * </p>
	 * @return The data that were lastly perceived by the agent, for all the levels where it lies.
	 */
	Map<LevelIdentifier, I_PerceivedDataOfAgent> getPerceivedData( );
	
	/**
	 * Gets the data that were lastly perceived by the agent from a specific level where it lies.
	 * <p>
	 * 	TODO : formal notation
	 * </p>
	 * @param levelIdentifier The identifier of the level from which the data were perceived.
	 * @return The data that were lastly perceived by the agent from the <code>levelIdentifier</code> level.
	 */
	I_PerceivedDataOfAgent getPerceivedData( LevelIdentifier levelIdentifier ) throws NoSuchElementException;
	
	/**
	 * Sets the data that were lastly perceived by the agent from a specific level where it lies.
	 * <p>
	 * 	TODO : formal notation
	 * </p>
	 * @param levelIdentifier The identifier of the level from which the data were perceived.
	 * @param perceivedData The data that were lastly perceived by the agent from the <code>levelIdentifier</code> level.
	 */
	void setPerceivedData( LevelIdentifier levelIdentifier, I_PerceivedDataOfAgent perceivedData );
	
	/**
	 * Disambiguates a public local dynamic state, <i>i.e.</i> transforms a transitory state into a fully observable state.
	 * <p>
	 * 	This operation can introduce biases since it provides an estimation of the real state of a level, using the information 
	 * 	stored into a transitory dynamic state.
	 * </p>
	 * <p>
	 * 	TODO formal notation
	 * </p>
	 * @param transitoryDynamicState The transitory state for which a disambiguation is computed.
	 * @return the observable dynamic state corresponding to the disambiguation of the transitory dynamic state.
	 */
	I_PublicLocalDynamicState disambiguation( Transitory_PublicLocalDynamicState transitoryDynamicState );
	
	/**
	 * Creates the data perceived by an agent located in a specific level.
	 * <p>
	 * 	TODO formal notation
	 * </p>
	 * @param level The level from which perception is made.
	 * @param publicLocalStateInLevel The public local state of the agent in the level from which perception is made.
	 * @param levelsPublicLocalObservableDynamicState The observable dynamic state of the various levels that can be perceived from the 
	 * level <code>level</code>.
	 * @return The data being perceived by an agent located in the <code>level</code> level.
	 */
	I_PerceivedDataOfAgent perceive( 
			LevelIdentifier level,
			I_PublicLocalStateOfAgent publicLocalStateInLevel,
			I_DynamicState_Map levelsPublicLocalObservableDynamicState
	);
	
	/**
	 * Revises the content of the global memory state of the agent, using the previous value of the global memory state and
	 * the data that were lastly perceived by the agent.
	 * <p>
	 * 	TODO formal notation
	 * </p>
	 * @param perceivedData The map containing the data that were lastly perceived from the various levels of the simulation.
	 * @param memoryState The previous value of the global memory state of the agent.
	 * @return The new value of the global memory state of the agent.
	 */
	I_GlobalMemoryState reviseMemory( Map<LevelIdentifier, I_PerceivedDataOfAgent> perceivedData, I_GlobalMemoryState memoryState );
	
	/**
	 * Produces the influences resulting from the decisions of an agent from a specific level.
	 * <p>
	 * 	TODO formal notation
	 * </p>
	 * @param level The level from which the decision is made.
	 * @param memoryState The global memory state of the agent when it made a decision.
	 * @param perceivedData The data that were perceived about the level and its perceptible levels.
	 * @param producedInfluences The map where the influences resulting from the decisions are stored.
	 */
	void decide(
			LevelIdentifier level,
			I_GlobalMemoryState memoryState,
			I_PerceivedDataOfAgent perceivedData,
			InfluencesMap producedInfluences
	);
}
