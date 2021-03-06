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
package fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.environment.IEnvironment;
import fr.univ_artois.lgi2a.similar.microkernel.environment.IEnvironment4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;

/**
 * An abstract implementation of the {@link IEnvironment} and {@link IEnvironment4Engine} interfaces, 
 * providing a default behavior to the generic methods.
 * <p>
 * 	This class also provides a method to set the level-related information of the environment.
 * </p>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public abstract class AbstractEnvironment implements IEnvironment4Engine {
	/**
	 * The public local states of the environment in the various levels of the simulation.
	 */
	private Map<LevelIdentifier,ILocalStateOfEnvironment> publicLocalStates;
	/**
	 * The private local states of the environment in the various levels of the simulation.
	 */
	private Map<LevelIdentifier,ILocalStateOfEnvironment> privateLocalStates;
	
	/**
	 * Builds an environment for a simulation containing no levels.
	 * Levels are then added using the 
	 * {@link AbstractEnvironment#includeNewLevel(LevelIdentifier, ILocalStateOfEnvironment, ILocalStateOfEnvironment)} method.
	 */
	public AbstractEnvironment( ) {
		this.publicLocalStates = new LinkedHashMap<>();
		this.privateLocalStates = new LinkedHashMap<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILocalStateOfEnvironment getPublicLocalState( LevelIdentifier level ) {
		ILocalStateOfEnvironment result = this.publicLocalStates.get( level );
		if( result == null ){
			throw new NoSuchElementException( "No public local state is defined in the environment for the level '" + level + "'." );
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<LevelIdentifier, ILocalStateOfEnvironment> getPublicLocalStates() {
		return this.publicLocalStates;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILocalStateOfEnvironment getPrivateLocalState( LevelIdentifier level ) {
		ILocalStateOfEnvironment result = this.privateLocalStates.get( level );
		if( result == null ){
			throw new NoSuchElementException( "No private local state is defined in the environment for the level '" + level + "'." );
		}
		return result;
	}
	
	/**
	 * Introduces the level-related data of the environment for a new level.
	 * <p>
	 * 	Note that the public local state also has to be added to the first consistent public local dynamic state of the level:
	 * 	the {@link fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IModifiablePublicLocalDynamicState#setPublicLocalStateOfEnvironment(ILocalStateOfEnvironment)} method has to
	 * 	be called during the initialization of the simulation for the initial consistent state.
	 * </p>
	 * @param level The level for which data are added.
	 * @param publicLocalState The public local state of the environment for that level.
	 * @param privateLocalState The public local state of the environment for that level.
	 * @throws IllegalArgumentException If an argument is <code>null</code>, or if the level is already present in the environment.
	 */
	public void includeNewLevel( 
			LevelIdentifier level, 
			ILocalStateOfEnvironment publicLocalState,
			ILocalStateOfEnvironment privateLocalState 
	){
		if( level == null || publicLocalState == null || privateLocalState == null ){
			throw new IllegalArgumentException( "The arguments cannot be null." );
		} else if( ! level.equals( publicLocalState.getLevel())  || ! level.equals( privateLocalState.getLevel() ) ) {
			throw new IllegalArgumentException( "The 'getLevel()' method of the 'publicLocalState' and" +
					" 'privateLocalState' arguments have to be equal to the 'level' argument." );
		} else if( this.publicLocalStates.containsKey( level ) ){
			throw new IllegalArgumentException( "The level '" + level + "' is already defined for this environment." );
		}
		this.publicLocalStates.put( level, publicLocalState );
		this.privateLocalStates.put( level, privateLocalState );
	}
}
