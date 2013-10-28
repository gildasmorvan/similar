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
package fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.operations;

import fr.lgi2a.similar.microKernel.LevelIdentifier;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.Learning_PublicLocalDynamicStateCopier;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.Learning_PerceivedDataOfAgent;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.Learning_PublicLocalStateOfAgent;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.Learning_EngineOperation;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.Learning_EngineOperationType;
import fr.lgi2a.similar.microKernel.states.I_PublicLocalDynamicState;
import fr.lgi2a.similar.microKernel.states.dynamicStates.Consistent_PublicLocalDynamicState;
import fr.lgi2a.similar.microKernel.states.dynamicStates.map.DynamicState_Map;
import fr.lgi2a.similar.microKernel.states.dynamicStates.map.I_DynamicState_Map;

/**
 * Models the operation performed by the simulation engine when it asks an agent to perceive from a level.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class Learning_EngineOperation_Perception implements Learning_EngineOperation {
	/**
	 * The level for which perception was made.
	 */
	private LevelIdentifier level;
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
	 * Models the data that were perceived, <i>i.e.</i> the result of the 'perceive' method call.
	 */
	private Learning_PerceivedDataOfAgent methodResult;
	
	/**
	 * Builds a partially initialized object modeling a call to the 'perceive' method of an agent, for a specific level.
	 * To achieve completion, calls to the {@link Learning_EngineOperation_Perception#addObservableDynamicState(I_PublicLocalDynamicState)} method
	 * has to be called for each perceptible level from the specified level.
	 * @param level The level from which the perception is made.
	 * @param agentPublicLocalState The public local state of the agent in the level from which perception is made.
	 * @param methodResult The result of the call to the 'perceive' method.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public Learning_EngineOperation_Perception(
			LevelIdentifier level,
			Learning_PublicLocalStateOfAgent agentPublicLocalState,
			Learning_PerceivedDataOfAgent methodResult
	) throws IllegalArgumentException {
		if( level == null ){
			throw new IllegalArgumentException( "The 'level' argument cannot be null." );
		} else if( agentPublicLocalState == null ){
			throw new IllegalArgumentException( "The 'agentPublicLocalState' argument cannot be null." );
		} else if( methodResult == null ){
			throw new IllegalArgumentException( "The 'methodResult' argument cannot be null." );
		}
		this.level = level;
		this.agentPublicLocalState = agentPublicLocalState.createCopy();
		this.levelsPublicLocalObservableDynamicState = new DynamicState_Map();
		this.methodResult = methodResult.createCopy();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Learning_EngineOperationType getOperationType() {
		return Learning_EngineOperationType.PERCEPTION;
	}
	
	/**
	 * Gets the level for which perception was made.
	 * @return The level for which perception was made.
	 */
	public LevelIdentifier getLevel( ) {
		return this.level;
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
			this.levelsPublicLocalObservableDynamicState.put( Learning_PublicLocalDynamicStateCopier.createCopy( original ) );
		} else {
			throw new IllegalArgumentException( "The observable dynamic state of the level '" + dynamicState.getLevel() + "' has to be " +
					"an instance of the '" + Consistent_PublicLocalDynamicState.class.getSimpleName() + "' class (consistent state)." );
		}
	}
	
	/**
	 * Gets the data that were perceived, <i>i.e.</i> the result of the 'perceive' method call.
	 * @return The data that were perceived, <i>i.e.</i> the result of the 'perceive' method call.
	 */
	public Learning_PerceivedDataOfAgent getMethodResult( ){
		return this.methodResult;
	}
}
