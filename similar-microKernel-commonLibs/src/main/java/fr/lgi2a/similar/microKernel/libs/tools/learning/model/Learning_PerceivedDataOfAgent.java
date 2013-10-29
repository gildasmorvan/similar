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

import fr.lgi2a.similar.microKernel.libs.abstractImplementations.AbstractPerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.states.I_PublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.states.dynamicstate.Consistent_PublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.states.dynamicstate.map.DynamicState_Map;
import fr.lgi2a.similar.microkernel.states.dynamicstate.map.I_DynamicState_Map;

/**
 * Models the data being perceived by an agent in the "learning" simulation.
 * An instance of this class contains the parameters that were provided in the 'perceive' method of the agent.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class Learning_PerceivedDataOfAgent extends AbstractPerceivedDataOfAgent {
	/**
	 * A counter used to generate unique identification numbers in this class.
	 */
	private static long identifierCounter = 0;
	
	/**
	 * Stores a unique identifier for this object.
	 */
	private long identifier;
	/**
	 * The public local state of the agent in the level from which perception was made.
	 * This field models one element of the arguments of the 'perceive' method call.
	 */
	private Learning_PublicLocalStateOfAgent agentPublicLocalState;
	/**
	 * The observable public local dynamic state of the levels that are perceptible from the level where perception is made.
	 * This field models one element of the arguments of the 'perceive' method call.
	 */
	private I_DynamicState_Map levelsPublicLocalObservableDynamicState;
	
	/**
	 * Builds partially-initialized data perceived by an agent from a specific level.
	 * To achieve completion, calls to the {@link Learning_PerceivedDataOfAgent#addObservableDynamicState(I_PublicLocalDynamicState)} method
	 * has to be called for each perceptible level from the specified level.
	 * @param levelIdentifier The identifier of the level by which the data were perceived.
	 * @param agentPublicLocalState The public local state of the agent that was provided as a parameter of the 'perceive' method.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public Learning_PerceivedDataOfAgent(
			LevelIdentifier levelIdentifier,
			Learning_PublicLocalStateOfAgent agentPublicLocalState
	) throws IllegalArgumentException {
		super(levelIdentifier);
		this.identifier = ++identifierCounter;
		if( agentPublicLocalState == null ){
			throw new IllegalArgumentException( "The 'agentPublicLocalState' argument cannot be null." );
		}
		this.agentPublicLocalState = agentPublicLocalState.createCopy();
		this.levelsPublicLocalObservableDynamicState = new DynamicState_Map();
	}
	
	/**
	 * Gets the unique identifier for this object.
	 * @return The unique identifier for this object.
	 */
	public long getIdentifier( ) {
		return this.identifier;
	}
	
	/**
	 * Creates a copy of this object.
	 * @return A copy of this object.
	 */
	public Learning_PerceivedDataOfAgent createCopy( ){
		Learning_PerceivedDataOfAgent copy = new Learning_PerceivedDataOfAgent( 
				this.getLevel(),
				this.agentPublicLocalState.createCopy()
		);
		copy.identifier = this.identifier;
		for( LevelIdentifier level : this.levelsPublicLocalObservableDynamicState.keySet() ){
			I_PublicLocalDynamicState dynamicState = this.levelsPublicLocalObservableDynamicState.get( level );
			copy.addObservableDynamicState( Learning_PublicLocalDynamicStateCopier.createCopy( dynamicState ) );
		}
		return copy;
	}
	
	/**
	 * Adds an observable dynamic state to the parameters of the call to the 'perceive' method.
	 * @param dynamicState The observable dynamic state to add to the parameters of the method call.
	 * @throws IllegalArgumentException If the argument is <code>null</code> or if it is not an observable 
	 * transitory state of the "learning" simulation or a consistent state.
	 */
	public void addObservableDynamicState( I_PublicLocalDynamicState dynamicState ) throws IllegalArgumentException {
		if( dynamicState == null ){
			throw new IllegalArgumentException( "The 'dynamicState' argument cannot be null." );
		}
		if( dynamicState instanceof Consistent_PublicLocalDynamicState ){
			Consistent_PublicLocalDynamicState original = (Consistent_PublicLocalDynamicState) dynamicState;
			this.levelsPublicLocalObservableDynamicState.put( original );
		} else {
			throw new IllegalArgumentException( "The observable dynamic state of the level '" + dynamicState.getLevel() + "' has " +
					"to be an instance of the '" + Consistent_PublicLocalDynamicState.class.getSimpleName() + "' class (consistent " +
					"state)." );
		}
	}

	/**
	 * Gets the public local state of the agent in the level from which perception was made.
	 * This field models one element of the arguments of the 'perceive' method call.
	 * @return The public local state of the agent in the level from which perception was made.
	 */
	public Learning_PublicLocalStateOfAgent getAgentPublicLocalState( ) {
		return this.agentPublicLocalState;
	}
	
	/**
	 * Gets the observable public local dynamic state of the levels that are perceptible from the level where perception is made.
	 * This field models one element of the arguments of the 'perceive' method call.
	 * @return The observable public local dynamic state of the levels that are perceptible from the level where perception is made.
	 */
	public I_DynamicState_Map getLevelsPublicLocalObservableDynamicState( ) {
		return this.levelsPublicLocalObservableDynamicState;
	}
}
