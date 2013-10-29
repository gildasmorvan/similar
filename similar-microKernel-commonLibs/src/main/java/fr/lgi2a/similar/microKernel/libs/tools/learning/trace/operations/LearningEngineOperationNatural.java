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
package fr.lgi2a.similar.microkernel.libs.tools.learning.trace.operations;

import java.lang.reflect.Method;

import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.agentbehavior.InfluencesMap;
import fr.lgi2a.similar.microkernel.libs.tools.learning.LearningInfluenceCopier;
import fr.lgi2a.similar.microkernel.libs.tools.learning.LearningPublicLocalDynamicStateCopier;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.ILearningEngineOperation;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.LearningEngineOperationType;
import fr.lgi2a.similar.microkernel.states.IPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.states.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.states.dynamicstate.map.DynamicStateMap;
import fr.lgi2a.similar.microkernel.states.dynamicstate.map.IDynamicStateMap;

/**
 * Models the operation performed by the simulation engine when it asks the environment to perform its natural reaction from a level.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class LearningEngineOperationNatural implements ILearningEngineOperation {
	/**
	 * The level for which perception was made.
	 */
	private LevelIdentifier level;
	/**
	 * The observable public local dynamic state of the levels that are perceptible from the level where perception is made.
	 * This field models one element of the arguments of the 'natural' method call.
	 */
	private IDynamicStateMap levelsPublicLocalObservableDynamicState;
	/**
	 * The influences that were produced by the natural action of the environment.
	 */
	private InfluencesMap producedInfluences;
	
	
	/**
	 * {@inheritDoc}
	 * @see fr.lgi2a.similar.microkernel.libs.tools.learning.trace.ILearningEngineOperation#getOperationType()
	 */
	@Override
	public LearningEngineOperationType getOperationType() {
		return LearningEngineOperationType.NATURAL;
	}
	
	/**
	 * Builds a partially initialized object modeling a call to the 'natural' method of the environment from a specific level.
	 * The initialization is completed by:
	 * <ul>
	 * 	<li>Calling the {@link LearningEngineOperationNatural#addObservableDynamicState(IPublicLocalDynamicState)}{@link Method} method for each perceptible level.</li>
	 * 	<li>Calling the {@link LearningEngineOperationNatural#addInfluence(IInfluence)} method for each influence that was produced by the natural action of the environment.</li>
	 * </ul>
	 * @param level The level from which the 'natural' method is called.
	 * @throws IllegalArgumentException If the argument is <code>null</code>.
	 */
	public LearningEngineOperationNatural(
			LevelIdentifier level
	) throws IllegalArgumentException {
		if( level == null ){
			throw new IllegalArgumentException( "The 'level' argument cannot be null." );
		}
		this.level = level;
		this.levelsPublicLocalObservableDynamicState = new DynamicStateMap( );
		this.producedInfluences = new InfluencesMap();
	}
	
	/**
	 * Adds an observable dynamic state to the parameters of the call to the 'perceive' method.
	 * @param dynamicState The observable dynamic state to add to the parameters of the method call.
	 * @throws IllegalArgumentException If the argument is <code>null</code> or if it is not an observable 
	 * transitory state of the "learning" simulation or a consistent state.
	 */
	public void addObservableDynamicState( IPublicLocalDynamicState dynamicState ) throws IllegalArgumentException {
		if( dynamicState == null ){
			throw new IllegalArgumentException( "The 'dynamicState' argument cannot be null." );
		}
		if( dynamicState instanceof ConsistentPublicLocalDynamicState ){
			ConsistentPublicLocalDynamicState original = (ConsistentPublicLocalDynamicState) dynamicState;
			this.levelsPublicLocalObservableDynamicState.put( LearningPublicLocalDynamicStateCopier.createCopy( original ) );
		} else {
			throw new IllegalArgumentException( "The observable dynamic state of the level '" + dynamicState.getLevel() + "' has to be an instance " +
					"of the '" + ConsistentPublicLocalDynamicState.class.getSimpleName() + "' class (consistent state)." );
		}
	}
	
	/**
	 * Adds an influence to the produced influence map argument of this 'natural' method call model.
	 * @param influence The influence to add.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public void addInfluence( 
			IInfluence influence
	) throws IllegalArgumentException {
		if( influence == null ){
			throw new IllegalArgumentException( "The 'influence' argument cannot be null." );
		}
		this.producedInfluences.add( LearningInfluenceCopier.copyInfluence( influence ) );
	}
	
	/**
	 * Gets the level for which perception was made.
	 * @return The level for which perception was made.
	 */
	public LevelIdentifier getLevel( ) {
		return this.level;
	}
	
	/**
	 * Gets the observable public local dynamic state of the levels that are perceptible from the level where perception is made.
	 * This field models one element of the arguments of the 'natural' method call.
	 * @return The observable public local dynamic state of the levels that are perceptible from the level where perception is made.
	 */
	public IDynamicStateMap getLevelsPublicLocalObservableDynamicState( ) {
		return this.levelsPublicLocalObservableDynamicState;
	}

	/**
	 * Gets the influences that were produced by the natural action of the environment.
	 * @return The influences that were produced by the natural action of the environment.
	 */
	public InfluencesMap getProducedInfluences( ) {
		return this.producedInfluences;
	}
}
