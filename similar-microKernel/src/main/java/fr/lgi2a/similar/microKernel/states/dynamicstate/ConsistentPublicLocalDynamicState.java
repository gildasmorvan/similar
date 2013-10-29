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
package fr.lgi2a.similar.microkernel.states.dynamicstate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.states.IPublicLocalState;
import fr.lgi2a.similar.microkernel.states.IPublicLocalStateOfAgent;

/**
 * Models a consistent public dynamic local state for a level <code>l</code> at a time <code>t</code>.
 * 
 * <h1>Correspondence with theory</h1>
 * <p>
 * 	TODO : formal notation
 * </p>
 * 
 * <h1>Usage</h1>
 * <p>
 * 	Such a public local dynamic state is valid only if the local state of the environment and of the agents are set, using the 
 * 	appropriate methods ( {@link IModifiablePublicLocalDynamicState#setPublicLocalStateOfEnvironment(IPublicLocalState)} and 
 * {@link IModifiablePublicLocalDynamicState#addPublicLocalStateOfAgent(IPublicLocalStateOfAgent)} ).
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class ConsistentPublicLocalDynamicState implements IModifiablePublicLocalDynamicState {
	/**
	 * The identifier of the level for which this dynamic state is defined.
	 */
	private LevelIdentifier level;
	
	/**
	 * The time for which this consistent public local dynamic state is defined.
	 * <p>
	 * 	TODO : formal notation
	 * </p>
	 */
	private SimulationTimeStamp time;
	
	//
	// The definition of the "properties" part of the dynamic state.
	//
	
	/**
	 * The public local state of the environment in this dynamic state.
	 * <p>
	 * 	TODO : formal notation
	 * </p>
	 */
	private IPublicLocalState publicLocalstateOfEnvironment;
	
	/**
	 * The public local state of the agents located in this level.
	 * <p>
	 * 	TODO : formal notation
	 * </p>
	 */
	private Set<IPublicLocalStateOfAgent> publicLocalStateOfAgents;
	
	/**
	 * Models the dynamics of this local dynamic state, <i>i.e.</i> the influences that are still active (being performed) when the level
	 * arrived into the consistent state.
	 */
	private Set<IInfluence> stateDynamics;
	/**
	 * Models the dynamics of this local dynamic state, <i>i.e.</i> the influences that are still active (being performed) when the level
	 * arrived into the consistent state.
	 * This field contains only system influences.
	 */
	private Set<IInfluence> stateDynamics_systemInfluences;
	/**
	 * Models the dynamics of this local dynamic state, <i>i.e.</i> the influences that are still active (being performed) when the level
	 * arrived into the consistent state.
	 * This field contains only regular influences.
	 */
	private Set<IInfluence> stateDynamics_regularInfluences;
	
	/**
	 * Builds a consistent public local state for a specific time stamp.
	 * This state still has to define the public local state of the environment, of the agents and the initial influences 
	 * lying into the state dynamics, using the appropriate setting methods.
	 * @param time The time stamp for which the public local dynamic state is defined.
	 * @param level The level of the public local dynamic state.
	 * @throws IllegalArgumentException If an argument is null.
	 */
	public ConsistentPublicLocalDynamicState( 
			SimulationTimeStamp time,
			LevelIdentifier level
	) throws IllegalArgumentException {
		this.setTime( time );
		if( level == null ){
			throw new IllegalArgumentException( "The 'level' argument cannot be null." );
		}
		this.level = level;
		this.stateDynamics_systemInfluences = new HashSet<IInfluence>();
		this.stateDynamics_regularInfluences = new HashSet<IInfluence>();
		this.stateDynamics = new ViewOnSetUnion<IInfluence>( this.stateDynamics_systemInfluences, this.stateDynamics_regularInfluences );
		this.publicLocalStateOfAgents = new HashSet<IPublicLocalStateOfAgent>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LevelIdentifier getLevel() {
		return this.level;
	}

	/**
	 * {@inheritDoc}
	 */
	public SimulationTimeStamp getTime() {
		return this.time;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setTime( SimulationTimeStamp time ) throws IllegalArgumentException {
		if( time == null ){
			throw new IllegalArgumentException( "The 'time' argument cannot be null." );
		}
		this.time = time;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPublicLocalState getPublicLocalStateOfEnvironment() {
		return this.publicLocalstateOfEnvironment;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<IPublicLocalStateOfAgent> getPublicLocalStateOfAgents() {
		return this.publicLocalStateOfAgents;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<IInfluence> getStateDynamics() {
		return this.stateDynamics;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<IInfluence> getSystemInfluencesOfStateDynamics() {
		return this.stateDynamics_regularInfluences;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<IInfluence> getRegularInfluencesOfStateDynamics() {
		return this.stateDynamics_systemInfluences;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPublicLocalStateOfEnvironment(
			IPublicLocalState publicLocalState
	) throws IllegalArgumentException {
		if( publicLocalState == null ){
			throw new IllegalArgumentException( "The 'publicLocalState' argument cannot be null." );
		}
		this.publicLocalstateOfEnvironment = publicLocalState;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addPublicLocalStateOfAgent(
			IPublicLocalStateOfAgent publicLocalState
	) throws IllegalArgumentException {
		if( publicLocalState == null ){
			throw new IllegalArgumentException( "The 'publicLocalState' argument cannot be null." );
		}
		this.publicLocalStateOfAgents.add( publicLocalState );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removePublicLocalStateOfAgent(
			IPublicLocalStateOfAgent publicLocalState
	) throws IllegalArgumentException {
		if( publicLocalState == null ){
			throw new IllegalArgumentException( "The 'publicLocalState' argument cannot be null." );
		}
		this.publicLocalStateOfAgents.remove( publicLocalState );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addInfluence( IInfluence influence ) throws IllegalArgumentException {
		if( influence == null ){
			throw new IllegalArgumentException( "The 'influence' argument cannot be null." );
		}
		if( influence.isSystem() ){
			this.stateDynamics_systemInfluences.add( influence );
		} else {
			this.stateDynamics_regularInfluences.add( influence );
			
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setStateDynamicsAsCopyOf( Collection<IInfluence> toCopy ) throws IllegalArgumentException {
		if( toCopy == null ){
			throw new IllegalArgumentException( "The 'toCopy' argument cannot be null." );
		}
		this.stateDynamics_regularInfluences.clear();
		this.stateDynamics_systemInfluences.clear();
		for( IInfluence influence : toCopy ){
			this.addInfluence( influence );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearSystemInfluences() {
		this.stateDynamics_systemInfluences.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clearRegularInfluences() {
		this.stateDynamics_regularInfluences.clear();
	}
}
