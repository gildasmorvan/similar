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
package fr.univ_artois.lgi2a.similar.microkernel.influences;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;


/**
 * A data structure containing a collection of influences, ordered using the target level of the influences.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public final class InfluencesMap {
	/**
	 * The data structure storing the influences.
	 */
	private Map<LevelIdentifier, List<IInfluence>> influences;
	
	private boolean parallelSimulation = false;
	
	/**
	 * Builds an empty influences map.
	 */
	public InfluencesMap( ){
		this.influences = new LinkedHashMap<>();
	}
	
	/**
	 * Builds an empty influences map.
	 */
	public InfluencesMap(boolean parallelSimulation ){
		this.parallelSimulation = parallelSimulation;
		if(parallelSimulation) {
			this.influences = new ConcurrentHashMap<>();
		} else {
			this.influences = new LinkedHashMap<>();
		}
	}
	
	/**
	 * Builds an influences map as a copy of an existing influences map.
	 * @param toAdd The influences map to copy.
	 * @throws IllegalArgumentException if the argument is <code>null</code>.
	 */
	public InfluencesMap( InfluencesMap toAdd ) {
		this( );
		this.addAll( toAdd );
	}
	
	/**
	 * Builds an influences map initially containing a specific collection of influences.
	 * @param toAdd The influences to include into this influence map..
	 * @throws IllegalArgumentException if the argument is <code>null</code>.
	 */
	public InfluencesMap( Collection<IInfluence> toAdd ) {
		this( );
		this.addAll( toAdd );
	}
	
	/**
	 * Builds an influences map initially containing a specific array of influences.
	 * @param toAdd The influences to include into this influence map.
	 * @throws IllegalArgumentException if the argument is <code>null</code> or contains <code>null</code>.
	 */
	public InfluencesMap( IInfluence... toAdd ) {
		this( );
		for( IInfluence addedinfluence : toAdd ) {
			this.add( addedinfluence );
		}
	}
	
	/**
	 * Gets an unordered set containing the keys that are defined in this influences map.
	 * @return The unordered set containing the keys that are defined in this influences map.
	 */
	public Set<LevelIdentifier> getDefinedKeys(){
		return this.influences.keySet();
	}
	
	/**
	 * Tells whether if this map contains at least one influence or not.
	 * @return <code>true</code> if this map contains no influences.
	 */
	public boolean isEmpty( ) {
		for( LevelIdentifier targetLevel : this.getDefinedKeys() ){
			if( ! this.isEmpty( targetLevel ) ){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check if this map contains no influences targeted at a specific level.
	 * @param targetLevel The target level for which the 'is empty' test is performed.
	 * @return <code>true</code> if this map contains no influences targeted at the <code>targetLevel</code> level.
	 */
	public boolean isEmpty( LevelIdentifier targetLevel ) {
		List<IInfluence> result = this.influences.get( targetLevel );
		return result == null || result.isEmpty();
	}
	
	/**
	 * Gets the influences contained in this map that are targeted at a specific level.
	 * @param targetLevel The target level for which influences are fetched in this map.
	 * @return The influences contained in this map that are targeted at a specific level.
	 */
	public List<IInfluence> getInfluencesForLevel( LevelIdentifier targetLevel ) {
		List<IInfluence> result = this.influences.get( targetLevel );
		if( result == null ){
			if(parallelSimulation) {
				result = new CopyOnWriteArrayList<>();
			} else {
				result = new LinkedList<>( );
			}
			this.influences.put( targetLevel, result );
		}
		return result;
	}
	
	/**
	 * Adds an influence to this influence map.
	 * @param influence The influence to add to this influence map.
	 * @throws IllegalArgumentException If the <code>influence</code> parameter was <code>null</code>.
	 */
	public void add( IInfluence influence ) {
		if( influence == null ){
			throw new IllegalArgumentException( "The 'influence' argument cannot be null." );
		}
		List<IInfluence> influenceList = this.influences.get( influence.getTargetLevel() );
		if( influenceList == null ){
			if(parallelSimulation) {
				influenceList = new CopyOnWriteArrayList<>();
			} else {
				influenceList = new LinkedList<>( );
			}
			this.influences.put( influence.getTargetLevel(), influenceList );
		}
		influenceList.add( influence );
	}
	
	/**
	 * Adds all the influences contained in an influence map into this influence map.
	 * <p>
	 * 	Note that if this influence map was already containing elements of the added influences, these influence 
	 * 	will be present twice in this map.
	 * </p>
	 * @param toAdd The influence map which content has to be added to this influence map.
	 * @throws IllegalArgumentException If the <code>toAdd</code> argument is <code>null</code>.
	 */
	public void addAll( InfluencesMap toAdd ) {
		if( toAdd == null ){
			throw new IllegalArgumentException( "The 'toAdd' argument cannot be null." );
		}
		for( LevelIdentifier key : toAdd.getDefinedKeys() ){
			if( ! toAdd.isEmpty( key ) ){
				List<IInfluence> influenceList;
				if(parallelSimulation) {
					influenceList = new CopyOnWriteArrayList<>();
				} else {
					influenceList = new LinkedList<>( );
				}
				this.influences.put( key, influenceList );
				influenceList.addAll( toAdd.getInfluencesForLevel( key ) );
			}
		}
	}
	
	/**
	 * Adds all the influences contained in a collection of influences into this influence map.
	 * <p>
	 * 	Note that if this influence map was already containing elements of the added influences, these influence 
	 * 	will be present twice in this map.
	 * </p>
	 * @param toAdd The collection which content has to be added to this influence map.
	 * @throws IllegalArgumentException If the <code>toAdd</code> argument is <code>null</code>.
	 */
	public void addAll( Collection<IInfluence> toAdd ) {
		if( toAdd == null ){
			throw new IllegalArgumentException( "The 'toAdd' argument cannot be null." );
		}
		for( IInfluence influence : toAdd ){
			this.add( influence );
		}
	}
	
	/**
	 * Removes all of the mappings from this map. The map will be empty after this call returns
	 */
	public void clear( ) {
		this.influences.clear( );
	}
}
