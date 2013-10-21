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
package fr.lgi2a.similar.microKernel.libs.tools.learning.model;

import fr.lgi2a.similar.microKernel.I_Influence;
import fr.lgi2a.similar.microKernel.SimulationTimeStamp;
import fr.lgi2a.similar.microKernel.states.dynamicStates.Transitory_PublicLocalDynamicState;

/**
 * The disambiguation of a transitory public local dynamic state of a level.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class Learning_ObservableTransitoryState extends Transitory_PublicLocalDynamicState {
	/**
	 * Checks the validity of the 'transitoryDynamicState' argument of the constructor of this class.
	 * @param transitoryDynamicState The 'transitoryDynamicState' argument of the constructor of this class.
	 * @throws IllegalArgumentException If the argument is <code>null</code>.
	 */
	private static Transitory_PublicLocalDynamicState checkValidity( Transitory_PublicLocalDynamicState transitoryDynamicState ) throws IllegalArgumentException {
		if( transitoryDynamicState == null ){
			throw new IllegalArgumentException( "The 'transitoryDynamicState' argument cannot be null." );
		}
		return transitoryDynamicState;
	}
	
	/**
	 * Builds a transitory public local dynamic state modeling the state of the level for a time <i>t</i> between the time stamp of 
	 * the last consistent dynamic state, and the time stamp of the next consistent dynamic state.
	 * @param lastConsistentDynamicState The last consistent dynamic state preceding this transitory dynamic state.
	 * @param nextConsistentTime The next time stamp when the public local dynamic state of the level will be consistent.
	 * @throws IllegalArgumentException If an argument of this constructor is <code>null</code> or if an influence cannot be managed by this model.
	 */
	public Learning_ObservableTransitoryState(
			Transitory_PublicLocalDynamicState transitoryDynamicState
	) throws IllegalArgumentException {
		super( checkValidity( transitoryDynamicState ).getLastConsistentDynamicState(), transitoryDynamicState.getNextTime() );
		for( I_Influence influence : transitoryDynamicState.getSystemInfluencesOfStateDynamics() ){
			if( ! transitoryDynamicState.getLastConsistentDynamicState().getStateDynamics().contains( influence ) ){
				this.addInfluence( influence );
			}
		}
	}
	
	/**
	 * Copies the content of an observable transitory state into this object.
	 * @param toCopy The transitory state to copy.
	 */
	private Learning_ObservableTransitoryState(
			Learning_ObservableTransitoryState toCopy
	) {
		super( 
				Learning_ConsistentDynamicStateCopier.createCopy( toCopy.getLastConsistentDynamicState() ),
				new SimulationTimeStamp( toCopy.getNextTime() )
		);
		for( I_Influence influence : toCopy.getSystemInfluencesOfStateDynamics() ){
			if( ! toCopy.getLastConsistentDynamicState().getStateDynamics().contains( influence ) ){
				this.addInfluence( Learning_InfluenceCopier.copyInfluence( influence ) );
			}
		}
	}
	
	/**
	 * Creates a copy of this observable transitory public local dynamic state.
	 * @return A copy of this observable transitory public local dynamic state.
	 */
	public Learning_ObservableTransitoryState createCopy( ){
		return new Learning_ObservableTransitoryState( this );
	}
}
