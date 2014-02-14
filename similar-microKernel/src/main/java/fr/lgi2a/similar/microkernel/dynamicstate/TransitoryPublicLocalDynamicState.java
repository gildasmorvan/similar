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
package fr.lgi2a.similar.microkernel.dynamicstate;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;
import fr.lgi2a.similar.microkernel.influences.IInfluence;

/**
 * Models the public dynamic state of a level when the level is in a transitory phase, <i>i.e.</i> 
 * when the environment and the agents in the level are evaluating their behavior between two consistent states.
 * 
 * <h1>Correspondence with theory</h1>
 * <p>
 * 	An instance of this class models the public local dynamic state &delta;(]t, t+dt<sub>l</sub>[, l) of a level "l" 
 * 	for the transitory period ]t, t+dt<sub>l</sub>[.
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
public class TransitoryPublicLocalDynamicState implements IModifiablePublicLocalDynamicState {
	/**
	 * The text of the exception thrown when an operation is not supported by instances of this class.
	 */
	private static final String FORBIDDEN_OPERATION_TEXT = "Transitory states do not allow this operation.";
	
	/**
	 * The last consistent public local dynamic state of the level.
	 * <p>
	 * 	If &delta;(]t, t+dt<sub>l</sub>[, l) is this transitory state, then this field models &delta;(t, l)
	 * </p>
	 */
	private ConsistentPublicLocalDynamicState lastConsistentDynamicState;
	
	/**
	 * The upper bound of the transitory period of this state.
	 */
	private SimulationTimeStamp timeUpperBound;
	
	/**
	 * The state dynamics of the transitory public local dynamic state, <i>i.e.</i> both the influences that were added during the 
	 * transitory phase of the level and the influences that were lying in the state dynamics of the last consistent dynamic state.
	 */
	private ViewOnSetUnion<IInfluence> allInfluencesOfStateTransitoryDynamics;
	
	/**
	 * A subset of the transitory dynamics of the transitory public local dynamic state containing only the system influences.
	 */
	private Set<IInfluence> stateTransitoryDynamicsSystemInfluences;
	
	/**
	 * A subset of the state dynamics of the transitory public local dynamic state containing only the system influences.
	 */
	private ViewOnSetUnion<IInfluence> allInfluencesOfStateTransitoryDynamicsSystemInfluences;
	
	/**
	 * A subset of the transitory dynamics of the transitory public local dynamic state containing only the non-system influences.
	 */
	private Set<IInfluence> stateTransitoryDynamicsRegularInfluences;
	
	/**
	 * A subset of the state dynamics of the transitory public local dynamic state containing only the non-system influences.
	 */
	private ViewOnSetUnion<IInfluence> allInfluencesOfStateTransitoryDynamicsRegularInfluences;
	
	/**
	 * Builds a transitory public local dynamic state modeling the state of the level for a time <i>t</i> between the time stamp of 
	 * the last consistent dynamic state, and the time stamp of the next consistent dynamic state.
	 * @param lastConsistentDynamicState The last consistent dynamic state preceding this transitory dynamic state.
	 * If &delta;(]t, t+dt<sub>l</sub>[, l) is this transitory state, then this field models &delta;(t, l)
	 * @param timeUpperBound The upper bound of the transitory period of this state.
	 * @throws IllegalArgumentException If an argument of this constructor is <code>null</code>.
	 */
	public TransitoryPublicLocalDynamicState( 
			ConsistentPublicLocalDynamicState lastConsistentDynamicState,
			SimulationTimeStamp timeUpperBound
	) {
		if( lastConsistentDynamicState == null ){
			throw new IllegalArgumentException( "The 'lastConsistentDynamicState' argument cannot be null." );
		} else if( timeUpperBound == null ) {
			throw new IllegalArgumentException( "The 'timeUpperBound' argument cannot be null." );
		}
		this.lastConsistentDynamicState = lastConsistentDynamicState;
		this.timeUpperBound = timeUpperBound;
		this.stateTransitoryDynamicsSystemInfluences = new LinkedHashSet<IInfluence>();
		this.allInfluencesOfStateTransitoryDynamicsSystemInfluences = new ViewOnSetUnion<>( 
				this.lastConsistentDynamicState.getSystemInfluencesOfStateDynamics(), 
				this.stateTransitoryDynamicsSystemInfluences
		);
		this.stateTransitoryDynamicsRegularInfluences = new LinkedHashSet<IInfluence>();
		this.allInfluencesOfStateTransitoryDynamicsRegularInfluences = new ViewOnSetUnion<>( 
				this.lastConsistentDynamicState.getRegularInfluencesOfStateDynamics(), 
				this.stateTransitoryDynamicsRegularInfluences 
		);
		this.allInfluencesOfStateTransitoryDynamics = new ViewOnSetUnion<>( 
				this.allInfluencesOfStateTransitoryDynamicsSystemInfluences, 
				this.allInfluencesOfStateTransitoryDynamicsRegularInfluences 
		);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LevelIdentifier getLevel() {
		return this.lastConsistentDynamicState.getLevel();
	}
	
	/**
	 * Gets the lower bound of the transitory period of this state.
	 * <p>
	 * 	This method returns the value "t" within the expression p<sub>a</sub>(]t,t&prime;[, l);
	 * </p>
	 * @return The lower bound of the transitory period of this state.
	 */
	public SimulationTimeStamp getTransitoryPeriodMin( ){
		return this.lastConsistentDynamicState.getTime( );
	}
	
	/**
	 * Gets the upper bound of the transitory period of this state.
	 * <p>
	 * 	This method returns the value "t&prime;" within the expression p<sub>a</sub>(]t,t&prime;[, l);
	 * </p>
	 * @return The upper bound of the transitory period of this state.
	 */
	public SimulationTimeStamp getTransitoryPeriodMax( ){
		return this.timeUpperBound;
	}

	/**
	 * Sets the higher bound of the time range for which this transitory state is defined, <i>i.e.</i> the next time when the level 
	 * will be in a consistent and observable state.
	 * <p>
	 * 	This method sets the value <code>t+dt<sub>l</sub></code> in the ]t, t+dt<sub>l</sub>[ transitory period.
	 * </p>
	 * @param time The time stamp modeling the higher bound of the time range for which this transitory state is defined.
	 * @throws IllegalArgumentException If an argument is <code>null</code> or is lower or equal to the current next time.
	 */
	public void setTransitoryPeriodMax( SimulationTimeStamp time ) {
		if( time == null ){
			throw new IllegalArgumentException( "The 'time' argument cannot be null." );
		} else if( time.compareTo( this.getTransitoryPeriodMin() ) <= 0 ){
			throw new IllegalArgumentException( "The time following the value '" + this.getTransitoryPeriodMin() + 
					"' in the time model of the level '" + this.getLevel() + "' has to be strictly greater than '" +
							this.getTransitoryPeriodMin() + ".' (was '" + time + "')." );
		}
		this.timeUpperBound = time;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILocalStateOfEnvironment getPublicLocalStateOfEnvironment() {
		return this.lastConsistentDynamicState.getPublicLocalStateOfEnvironment();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<ILocalStateOfAgent> getPublicLocalStateOfAgents() {
		return this.lastConsistentDynamicState.getPublicLocalStateOfAgents();
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<IInfluence> getStateDynamics() {
		return this.allInfluencesOfStateTransitoryDynamics;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<IInfluence> getSystemInfluencesOfStateDynamics() {
		return this.allInfluencesOfStateTransitoryDynamicsSystemInfluences;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<IInfluence> getRegularInfluencesOfStateDynamics() {
		return this.allInfluencesOfStateTransitoryDynamicsRegularInfluences;
	}

	/**
	 * This operation is not supported in transitory states.
	 */
	@Override
	public void setPublicLocalStateOfEnvironment(
			ILocalStateOfEnvironment publicLocalState
	) {
		throw new UnsupportedOperationException( FORBIDDEN_OPERATION_TEXT );
	}

	/**
	 * This operation is not supported in transitory states.
	 */
	@Override
	public void addPublicLocalStateOfAgent(
			ILocalStateOfAgent publicLocalState
	) {
		throw new UnsupportedOperationException( FORBIDDEN_OPERATION_TEXT );
	}

	/**
	 * This operation is not supported in transitory states.
	 */
	@Override
	public void removePublicLocalStateOfAgent(
			ILocalStateOfAgent publicLocalState
	) {
		throw new UnsupportedOperationException( FORBIDDEN_OPERATION_TEXT );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addInfluence( IInfluence influence ) {
		if( influence == null ) {
			throw new IllegalArgumentException( "The 'influence' argument cannot be null." );
		}
		if( influence.isSystem() ){
			this.stateTransitoryDynamicsSystemInfluences.add( influence );
		} else {
			this.stateTransitoryDynamicsRegularInfluences.add( influence );
		}
	}

	/**
	 * This operation is not supported in transitory states.
	 */
	@Override
	public void setStateDynamicsAsCopyOf( Collection<IInfluence> toCopy ) {
		throw new UnsupportedOperationException( FORBIDDEN_OPERATION_TEXT );
	}

	/**
	 * Gets the last consistent public local dynamic state of the level.
	 * <p>
	 * 	If &delta;(]t, t+dt<sub>l</sub>[, l) is this transitory state, then this method returns &delta;(t, l)
	 * </p>
	 * @return The last consistent public local dynamic state of the level.
	 */
	public ConsistentPublicLocalDynamicState getLastConsistentDynamicState( ) {
		return this.lastConsistentDynamicState;
	}

	/**
	 * Remove all the system influences of this dynamic state.
	 * <p>
	 * 	Warning: this method remove the system influences from both the transitory state and the consistent dynamic state it is
	 * 	attached to.
	 * </p>
	 */
	@Override
	public void clearSystemInfluences() {
		this.allInfluencesOfStateTransitoryDynamicsSystemInfluences.clear( );
	}

	/**
	 * Remove all the regular influences of this dynamic state.
	 * <p>
	 * 	Warning: this method remove the regular influences from both the transitory state and the consistent dynamic state it is
	 * 	attached to.
	 * </p>
	 */
	@Override
	public void clearRegularInfluences() {
		this.allInfluencesOfStateTransitoryDynamicsRegularInfluences.clear( );
	}
	
	/**
	 * Moves the influences contained in this transitory state into the consistent dynamic state it is attached to.
	 */
	public void moveInfluencesToConsistentState( ) {
		// Copy the system influences.
		Iterator<IInfluence> systemInfluenceIterator = this.stateTransitoryDynamicsSystemInfluences.iterator();
		while( systemInfluenceIterator.hasNext() ){
			IInfluence systemInfluence = systemInfluenceIterator.next();
			// Copy the influence into the consistent state.
			this.lastConsistentDynamicState.addInfluence( systemInfluence );
			// Remove the influence from this transitory state.
			systemInfluenceIterator.remove();
		}
		// Copy the regular influences.
		Iterator<IInfluence> regularInfluenceIterator = this.stateTransitoryDynamicsRegularInfluences.iterator();
		while( regularInfluenceIterator.hasNext() ){
			IInfluence regularInfluence = regularInfluenceIterator.next();
			// Copy the influence into the consistent state.
			this.lastConsistentDynamicState.addInfluence( regularInfluence );
			// Remove the influence from this transitory state.
			regularInfluenceIterator.remove();
		}
	}
}
