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
package fr.lgi2a.similar.microkernel.libs.tools.engine;

import java.util.NoSuchElementException;
import java.util.Set;

import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;

/**
 * This class defines a sub set of a dynamic state map.
 * 
 * <h1>Usage</h1>
 * <p>
 * 	This map is used to facilitate the creation of the map used as an argument of the perception and natural methods of 
 * 	the agents and the environment. It avoids the duplication of the map data.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public final class DynamicStateFilteredMap implements IPublicDynamicStateMap {
	/**
	 * The over map of this map.
	 */
	private IPublicDynamicStateMap parent;
	
	/**
	 * The set of keys of the over map that have to be included to the keys of this masked map.
	 */
	private Set<LevelIdentifier> filter;
	
	/**
	 * Builds a sub set of an existing map of dynamic states.
	 * @param parent The over map of this map.
	 * @param filter The set of keys to include in this map.
	 * @throws IllegalArgumentException If an argument is <code>null</code> or if the filter is not a subset of the keys of the parent.
	 */
	public DynamicStateFilteredMap( 
			IPublicDynamicStateMap parent, 
			Set<LevelIdentifier> filter 
	) {
		if( filter == null ){
			throw new IllegalArgumentException( "The 'filter' argument cannot be null." );
		}
		this.filter = filter;
		this.changeParent( parent );
	}

	/**
	 * Changes the over map of this map.
	 * @param parent The over map of this map.
	 * @throws IllegalArgumentException If an argument is <code>null</code> or if the filter is not a subset of the keys of the parent.
	 */
	public void changeParent( IPublicDynamicStateMap parent ) {
		if( parent == null ){
			throw new IllegalArgumentException( "The 'parent' argument cannot be null." );
		} else if( ! parent.keySet().containsAll( filter ) ){
			throw new IllegalArgumentException( "The 'filter' argument is not a subset of the keys of the 'parent' argument." );
		} else {
			this.parent = parent;
		}
	}

	/**
	 * Gets the levels which dynamic state is contained in this filtered map.
	 * @return The identifier of the levels contained in this filtered map.
	 */
	@Override
	public Set<LevelIdentifier> keySet() {
		return this.filter;
	}

	/**
	 * Gets the dynamic state of a level contained in this filtered map.
	 * @param level The level of the dynamic state.
	 * @return The dynamic state of the level.
	 * @throws IllegalArgumentException If the argument is <code>null</code>.
	 * @throws NoSuchElementException If the <code>level</code> level is not in this filtered map.
	 */
	@Override
	public IPublicLocalDynamicState get(
		LevelIdentifier level
	) {
		if( this.filter.contains( level ) ){
			return this.parent.get( level );
		} else {
			throw new NoSuchElementException( "The '" + level + "' level is not in this filtered map." );
		}
	}

	/**
	 * This operation is not supported in this class.
	 */
	@Override
	public void put(
			IPublicLocalDynamicState state
	) {
		throw new UnsupportedOperationException( "This operation is not supported in this class." );
	}
}