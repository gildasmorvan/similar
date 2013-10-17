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
package fr.lgi2a.similar.microKernel.libs.abstractImplementations;

import java.util.HashSet;
import java.util.Set;

import fr.lgi2a.similar.microKernel.I_Level;
import fr.lgi2a.similar.microKernel.LevelIdentifier;
import fr.lgi2a.similar.microKernel.SimulationTimeStamp;
import fr.lgi2a.similar.microKernel.states.dynamicStates.Consistent_PublicLocalDynamicState;
import fr.lgi2a.similar.microKernel.states.dynamicStates.I_Modifiable_PublicLocalDynamicState;

/**
 * An abstract implementation of the {@link I_Level} interface, providing a default behavior to most methods.
 * It also provides setters for the perception and the influence relation graph.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public abstract class AbstractLevel implements I_Level {
	/**
	 * The identifier of the level.
	 */
	private final LevelIdentifier identifier;
	/**
	 * The initial time of the level.
	 */
	private final SimulationTimeStamp initialTime;
	/**
	 * The last consistent public local dynamic state of this level.
	 */
	private Consistent_PublicLocalDynamicState lastConsistentPublicLocalDynamicState;
	/**
	 * The perceptible levels of this level.
	 */
	private Set<LevelIdentifier> perceptibleLevels;
	/**
	 * The influenceable levels of this level.
	 */
	private Set<LevelIdentifier> influenceableLevels;
	
	/**
	 * Builds an initialized instance of level having a specific initial time, a specific identifier, a perception and influence 
	 * relation graph containing only an edge between this level and itself and an empty initial consistent dynamic state.
	 * This dynamic state has to be initialized using the 
	 * {@link I_Modifiable_PublicLocalDynamicState#setPublicLocalStateOfEnvironment(fr.lgi2a.similar.microKernel.states.I_PublicLocalState)},
	 * {@link I_Modifiable_PublicLocalDynamicState#setStateDynamicsAsCopyOf(Set)} and {@link I_Modifiable_PublicLocalDynamicState#addPublicLocalStateOfAgent(fr.lgi2a.similar.microKernel.states.I_PublicLocalStateOfAgent)}
	 * methods.
	 * @param initialTime The initial time stamp of the level.
	 * @param identifier The identifier of the level.
	 * @throws IllegalArgumentException if an argument is <code>null</code>.
	 */
	public AbstractLevel(
			SimulationTimeStamp initialTime,
			LevelIdentifier identifier
	) throws IllegalArgumentException {
		if( initialTime == null ){
			throw new IllegalArgumentException( "The 'initialTime' argument cannot be null." );
		} else if( identifier == null ){
			throw new IllegalArgumentException( "The 'identifier' argument cannot be null." );
		}
		this.initialTime = initialTime;
		this.identifier = identifier;
		// Create the last consistent dynamic state of this level.
		this.lastConsistentPublicLocalDynamicState = new Consistent_PublicLocalDynamicState( this.initialTime );
		// Create the out neighborhood of the perception relation graph for this level.
		this.perceptibleLevels = new HashSet<LevelIdentifier>( );
		this.perceptibleLevels.add( identifier );
		// Create the out neighborhood of the influence relation graph for this level.
		this.influenceableLevels = new HashSet<LevelIdentifier>( );
		this.influenceableLevels.add( identifier );
	}

	/**
	 * Gets the initial time of this time model, as defined in the constructor of the level.
	 * @return The initial time of this model.
	 */
	@Override
	public SimulationTimeStamp getInitialTime() {
		return this.initialTime;
	}

	/**
	 * Gets the identifier of this level, as defined in the constructor of the level.
	 * @return The identifier of this level.
	 */
	@Override
	public LevelIdentifier getIdentifier() {
		return this.identifier;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Consistent_PublicLocalDynamicState getLastConsistentPublicLocalDynamicState() {
		return this.lastConsistentPublicLocalDynamicState;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setLastConsistentPublicLocalDynamicState(
			Consistent_PublicLocalDynamicState lastConsistentState
	) {
		if( lastConsistentState == null ){
			throw new IllegalArgumentException( "The 'lastConsistentState' argument cannot be null." );
		}
		this.lastConsistentPublicLocalDynamicState = lastConsistentState;
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
	public void addPerceptibleLevel( LevelIdentifier perceptibleLevel ) throws IllegalArgumentException {
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
	public void addInfluenceableLevel( LevelIdentifier influenceableLevel ) throws IllegalArgumentException {
		if( influenceableLevel == null ){
			throw new IllegalArgumentException( "The 'influenceableLevel' argument cannot be null." );
		}
		this.influenceableLevels.add( influenceableLevel );
	}
}
