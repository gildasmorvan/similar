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
package fr.lgi2a.similar.microkernel.libs.abstractimplementation;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import fr.lgi2a.similar.microkernel.I_Environment;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.states.I_PublicLocalState;
import fr.lgi2a.similar.microkernel.states.dynamicstate.I_Modifiable_PublicLocalDynamicState;

/**
 * An abstract implementation of the {@link I_Environment} interface, providing a default behavior to the generic methods.
 * It also provides a method to set the level-related information of the environment.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public abstract class AbstractEnvironment implements I_Environment {
	/**
	 * The public local states of the environment in the various levels of the simulation.
	 */
	private Map<LevelIdentifier,I_PublicLocalState> publicLocalStates;
	
	/**
	 * Builds an environment for a simulation containing no levels.
	 * Levels are then added using the {@link AbstractEnvironment#includeNewLevel(LevelIdentifier, I_PublicLocalState)} method.
	 */
	public AbstractEnvironment( ) {
		this.publicLocalStates = new HashMap<LevelIdentifier, I_PublicLocalState>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public I_PublicLocalState getPublicLocalState( LevelIdentifier level ) throws NoSuchElementException {
		I_PublicLocalState result = this.publicLocalStates.get( level );
		if( result == null ){
			throw new NoSuchElementException( "No public local state is defined in the environment for the level '" + level + "'." );
		}
		return result;
	}
	
	/**
	 * Introduces the level-related data of the environment for a new level.
	 * <p>
	 * 	Note that the public local state also has to be added to the first consistent dynamic state of the level:
	 * 	the {@link I_Modifiable_PublicLocalDynamicState#setPublicLocalStateOfEnvironment(I_PublicLocalState)} method has to
	 * 	be called during the initialization of the simulation for the initial consistent state.
	 * </p>
	 * @param level The level for which data are added.
	 * @param publicLocalState The public local state of the environment for that level.
	 * @throws IllegalArgumentException If an argument is <code>null</code>, or if the level is already present in the environment.
	 */
	public void includeNewLevel( LevelIdentifier level, I_PublicLocalState publicLocalState ){
		if( level == null ){
			throw new IllegalArgumentException( "The 'level' argument cannot be null." );
		} else if( publicLocalState == null ){
			throw new IllegalArgumentException( "The 'publicLocalState' argument cannot be null." );
		} else if( this.publicLocalStates.containsKey( level ) ){
			throw new IllegalArgumentException( "The level '" + level + "' is already defined for this environment." );
		}
		this.publicLocalStates.put( level, publicLocalState );
	}
}
