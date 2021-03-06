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

import java.util.LinkedHashSet;
import java.util.Set;

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.TransitoryPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar.microkernel.levels.ILevel;

/**
 * An abstract implementation of the {@link ILevel} interface, 
 * providing a default behavior to most methods.
 * <p>
 * 	It also provides setters for the perception and the influence relation graph.
 * </p>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public abstract class AbstractLevel implements ILevel {
	/**
	 * The identifier of the level.
	 */
	private final LevelIdentifier identifier;
	/**
	 * The last consistent public local dynamic state of this level.
	 */
	private ConsistentPublicLocalDynamicState lastConsistentPublicLocalDynamicState;
	/**
	 * The last transitory public local dynamic state of this level.
	 */
	private TransitoryPublicLocalDynamicState lastTransitoryPublicLocalDynamicState;
	/**
	 * The perceptible levels of this level.
	 */
	private Set<LevelIdentifier> perceptibleLevels;
	/**
	 * The influenceable levels of this level.
	 */
	private Set<LevelIdentifier> influenceableLevels;

	/**
	 * Builds an initialized instance of level having a specific identifier, a perception and influence 
	 * relation graph containing only an edge between this level and itself and an empty initial consistent dynamic state.
	 * This dynamic state has to be initialized using the following method on the last consistent state of the level:
	 * <ul>
	 * 	<li>{@link ConsistentPublicLocalDynamicState#setPublicLocalStateOfEnvironment(fr.univ_artois.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment)}</li>
	 * </ul>
	 * @param initialTime The initial time stamp of the level.
	 * @param identifier The identifier of the level.
	 * @throws IllegalArgumentException if an argument is <code>null</code>.
	 */
	protected AbstractLevel(
			SimulationTimeStamp initialTime,
			LevelIdentifier identifier
	){
		if( identifier == null ){
			throw new IllegalArgumentException( "The 'identifier' argument cannot be null." );
		}
		this.identifier = identifier;
		// Create the out neighborhood of the perception relation graph for this level.
		this.perceptibleLevels = new LinkedHashSet<>( );
		this.perceptibleLevels.add( identifier );
		// Create the out neighborhood of the influence relation graph for this level.
		this.influenceableLevels = new LinkedHashSet<>( );
		this.influenceableLevels.add( identifier );
		// Create a partially initialized instance of the dynamic state of the level.
		this.initializeStates(
			new ConsistentPublicLocalDynamicState(
				initialTime,
				identifier
			)
		);
	}

	/**
	 * Gets the identifier of this level, as defined in the constructor of the level.
	 * @return The identifier of this level.
	 */
	@Override
	public final LevelIdentifier getIdentifier() {
		return this.identifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ConsistentPublicLocalDynamicState getLastConsistentState() {
		if( this.lastConsistentPublicLocalDynamicState == null ){
			 throw new IllegalStateException( 
					 "The state of the level " + this.getIdentifier() + " is not initialized." 
			);
		}
		return this.lastConsistentPublicLocalDynamicState;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TransitoryPublicLocalDynamicState getLastTransitoryState() {
		if( this.lastConsistentPublicLocalDynamicState == null ){
			 throw new IllegalStateException( 
					 "The state of the level " + this.getIdentifier() + " is not initialized." 
			);
		}
		return this.lastTransitoryPublicLocalDynamicState;
	}

	/**
	 * Sets the initial value of the last consistent and the last transitory 
	 * public state of the level.
	 * @param lastConsistentState The initial value of the last consistent state of this level.
	 */
	public final void initializeStates( 
			ConsistentPublicLocalDynamicState lastConsistentState 
	){
		if( lastConsistentState == null ){
			throw new IllegalArgumentException( "The 'lastConsistentState' argument cannot be null." );
		} else if( lastConsistentState.getTime() == null ) {
			throw new IllegalArgumentException( "The 'getTime()' method of the 'lastConsistentState' argument cannot return null." );
		}
		this.lastConsistentPublicLocalDynamicState = lastConsistentState;
		this.lastTransitoryPublicLocalDynamicState = new TransitoryPublicLocalDynamicState(
				this.lastConsistentPublicLocalDynamicState, 
				new SimulationTimeStamp( Long.MAX_VALUE )
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<LevelIdentifier> getPerceptibleLevels() {
		return this.perceptibleLevels;
	}
	
	/**
	 * Adds a perceptible level by this level.
	 * @param perceptibleLevel The level becoming perceptible by this level.
	 * @throws IllegalArgumentException If the argument is <code>null</code>.
	 */
	public void addPerceptibleLevel( LevelIdentifier perceptibleLevel ) {
		if( perceptibleLevel == null ){
			throw new IllegalArgumentException( "The 'perceptibleLevel' argument cannot be null." );
		}
		this.perceptibleLevels.add( perceptibleLevel );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<LevelIdentifier> getInfluenceableLevels() {
		return this.influenceableLevels;
	}
	
	/**
	 * Adds a level to the levels that can be influenced by this level.
	 * @param influenceableLevel The level becoming influenceable by this level.
	 * @throws IllegalArgumentException If the argument is <code>null</code>.
	 */
	public void addInfluenceableLevel( LevelIdentifier influenceableLevel ) {
		if( influenceableLevel == null ){
			throw new IllegalArgumentException( "The 'influenceableLevel' argument cannot be null." );
		}
		this.influenceableLevels.add( influenceableLevel );
	}
}
