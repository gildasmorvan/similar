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
package fr.lgi2a.similar.microKernel.states.dynamicStates;

import java.util.HashSet;
import java.util.Set;

import fr.lgi2a.similar.microKernel.I_Influence;
import fr.lgi2a.similar.microKernel.LevelIdentifier;
import fr.lgi2a.similar.microKernel.SimulationTimeStamp;
import fr.lgi2a.similar.microKernel.states.I_PublicLocalState;
import fr.lgi2a.similar.microKernel.states.I_PublicLocalStateOfAgent;

/**
 * Models the public dynamic state of a level when the level is in a transitory phase, <i>i.e.</i> 
 * when the environment and the agents in the level are evaluating their behavior between two consistent states.
 * 
 * <h1>Correspondence with theory</h1>
 * <p>
 * 	TODO : formal notation
 * </p>
 * 
 * <h1>Usage</h1>
 * <p>
 * 	This state is a working copy of the last consistent state of the simulation, where the behavior of agents or 
 * 	of the environment can add influences to the state dynamics of the public local dynamic state of the level.
 * 	These influences are used during the next reaction phase of the level to determine the next consistent public local 
 * 	dynamic state of the level.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class Transitory_PublicLocalDynamicState implements I_Modifiable_PublicLocalDynamicState {
	/**
	 * The last consistent public local dynamic state of the level.
	 * <p>
	 * 	TODO : formal notation
	 * </p>
	 */
	private Consistent_PublicLocalDynamicState lastConsistentDynamicState;
	
	/**
	 * The next time when the simulation will be in a consistent state.
	 * <p>
	 * 	TODO : formal notation
	 * </p>
	 */
	private SimulationTimeStamp nextConsistentTime;

	/**
	 * The transitory dynamics of the transitory public local dynamic state, <i>i.e.</i> the influences that have to 
	 * be managed by the next reaction of the level.
	 * <p>
	 * 	TODO : formal notation
	 * </p>
	 */
	private Set<I_Influence> stateTransitoryDynamics;
	
	/**
	 * A subset of the transitory dynamics containing only the system influences.
	 */
	private Set<I_Influence> stateTransitoryDynamics_systemInfluences;
	
	/**
	 * A subset of the transitory dynamics containing only the non-system influences.
	 */
	private Set<I_Influence> stateTransitoryDynamics_regularInfluences;
	
	/**
	 * Builds a transitory public local dynamic state modeling the state of the level for a time <i>t</i> between the time stamp of 
	 * the last consistent dynamic state, and the time stamp of the next consistent dynamic state.
	 * @param lastConsistentDynamicState The last consistent dynamic state preceding this transitory dynamic state.
	 * @param nextConsistentTime The next time stamp when the public local dynamic state of the level will be consistent.
	 * @throws IllegalArgumentException If an argument of this constructor is <code>null</code>.
	 */
	public Transitory_PublicLocalDynamicState( 
			Consistent_PublicLocalDynamicState lastConsistentDynamicState,
			SimulationTimeStamp nextConsistentTime
	) throws IllegalArgumentException {
		if( lastConsistentDynamicState == null ){
			throw new IllegalArgumentException( "The 'lastConsistentDynamicState' argument cannot be null." );
		} else if( nextConsistentTime == null ) {
			throw new IllegalArgumentException( "The 'nextConsistentTime' argument cannot be null." );
		}
		this.lastConsistentDynamicState = lastConsistentDynamicState;
		this.nextConsistentTime = nextConsistentTime;
		this.stateTransitoryDynamics = new HashSet<I_Influence>();
		this.stateTransitoryDynamics_systemInfluences = new HashSet<I_Influence>();
		this.stateTransitoryDynamics_regularInfluences = new HashSet<I_Influence>();
		// Add to the transitory dynamics all the influences that remained in the state dynamics of the last consistent dynamic state.
		for( I_Influence influence : this.lastConsistentDynamicState.getStateDynamics() ){
			this.stateTransitoryDynamics.add( influence );
			if( influence.isSystem() ){
				this.stateTransitoryDynamics_systemInfluences.add( influence );
			} else {
				this.stateTransitoryDynamics_regularInfluences.add( influence );
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LevelIdentifier getLevel() {
		return this.lastConsistentDynamicState.getLevel();
	}

	/**
	 * Gets the lower bound of the time range for which this transitory state is defined.
	 * <p>
	 * 	TODO : formal notation
	 * 	models <code>t</code> in the ]t, t+dt<sub>l</sub>[ range.
	 * </p>
	 * @return The time stamp modeling the lower bound of the time range for which this transitory state is defined.. 	
	 */
	@Override
	public SimulationTimeStamp getTime() {
		return this.nextConsistentTime;
	}

	/**
	 * Gets the higher bound of the time range for which this transitory state is defined, <i>i.e.</i> the next time when the level 
	 * will be in a consistent and observable state.
	 * <p>
	 * 	TODO : formal notation
	 * 	models <code>t+dt<sub>l</sub></code> in the ]t, t+dt<sub>l</sub>[ range.
	 * </p>
	 * @return The time stamp modeling the higher bound of the time range for which this transitory state is defined.
	 */
	public SimulationTimeStamp getNextTime() {
		return this.nextConsistentTime;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public I_PublicLocalState getPublicLocalStateOfEnvironment() {
		return this.lastConsistentDynamicState.getPublicLocalStateOfEnvironment();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<I_PublicLocalStateOfAgent> getPublicLocalStateOfAgents() {
		return this.lastConsistentDynamicState.getPublicLocalStateOfAgents();
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<I_Influence> getStateDynamics() {
		return this.stateTransitoryDynamics;
	}

	/**
	 * Gets the system influences contained in the state transitory dynamics.
	 * @return The system influences of the value returned by the {@link Transitory_PublicLocalDynamicState#getStateDynamics()} method.
	 */
	public Set<I_Influence> getSystemInfluencesOfStateDynamics() {
		return this.stateTransitoryDynamics_systemInfluences;
	}

	/**
	 * Gets the non-system influences contained in the state transitory dynamics.
	 * @return The non-system influences of the value returned by the 
	 * {@link Transitory_PublicLocalDynamicState#getStateDynamics()} method.
	 */
	public Set<I_Influence> getRegularInfluencesOfStateDynamics() {
		return this.stateTransitoryDynamics_regularInfluences;
	}

	/**
	 * This operation is not supported in transitory states.
	 */
	public void setTime(SimulationTimeStamp time) throws IllegalArgumentException {
		throw new UnsupportedOperationException( "Transitory states do not allow this operation." );
	}

	/**
	 * This operation is not supported in transitory states.
	 */
	@Override
	public void setPublicLocalStateOfEnvironment(
			I_PublicLocalState publicLocalState
	) throws IllegalArgumentException {
		throw new UnsupportedOperationException( "Transitory states do not allow this operation." );
	}

	/**
	 * This operation is not supported in transitory states.
	 */
	@Override
	public void addPublicLocalStateOfAgent(
			I_PublicLocalStateOfAgent publicLocalState
	) throws IllegalArgumentException {
		throw new UnsupportedOperationException( "Transitory states do not allow this operation." );
	}

	/**
	 * This operation is not supported in transitory states.
	 */
	@Override
	public void removePublicLocalStateOfAgent(
			I_PublicLocalStateOfAgent publicLocalState
	) throws IllegalArgumentException {
		throw new UnsupportedOperationException( "Transitory states do not allow this operation." );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addInfluence( I_Influence influence ) throws IllegalArgumentException {
		if( influence == null ) {
			throw new IllegalArgumentException( "The 'influence' argument cannot be null." );
		}
		this.stateTransitoryDynamics.add( influence );
	}

	/**
	 * This operation is not supported in transitory states.
	 */
	@Override
	public void setStateDynamicsAsCopyOf( Set<I_Influence> toCopy ) throws IllegalArgumentException {
		throw new UnsupportedOperationException( "Transitory states do not allow this operation." );
	}

	/**
	 * The last consistent public local dynamic state of the level.
	 * <p>
	 * 	TODO : formal notation
	 * </p>
	 */
	public Consistent_PublicLocalDynamicState getLastConsistentDynamicState( ) {
		return this.lastConsistentDynamicState;
	}
}
