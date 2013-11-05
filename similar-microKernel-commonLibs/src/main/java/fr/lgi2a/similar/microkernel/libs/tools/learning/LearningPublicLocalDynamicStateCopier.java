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
package fr.lgi2a.similar.microkernel.libs.tools.learning;

import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.IPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.LearningPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.LearningPublicLocalStateOfEnvironment;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.influence.ILearningInfluence;

/**
 * This class defines a method copying the content of a dynamic state into another dynamic state.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public final class LearningPublicLocalDynamicStateCopier {
	/**
	 * This constructor is never used.
	 */
	private LearningPublicLocalDynamicStateCopier( ) { }
	
	/**
	 * Creates a copy of a public local dynamic state of the "learning" simulation.
	 * @param localDynamicState The public local dynamic state to copy.
	 * @return The copy of the public local dynamic state.
	 * @throws IllegalArgumentException If the argument is <code>null</code> or if it contains elements that are not within 
	 * the specification of the "learning" simulation.
	 */
	public static IPublicLocalDynamicState createCopy( IPublicLocalDynamicState localDynamicState ) {
		if( localDynamicState instanceof ConsistentPublicLocalDynamicState ){
			return createCopy( (ConsistentPublicLocalDynamicState) localDynamicState );
		} else {
			throw new UnsupportedOperationException( "Cannot manage the copy of a dynamic state from the " +
					"class '" + localDynamicState.getClass().getSimpleName() + "'" );
		}
	}
	
	/**
	 * Creates a copy of a consistent dynamic state of the "learning" simulation.
	 * @param toCopy The consistent dynamic state to copy.
	 * @return The copy of the consistent dynamic state.
	 * @throws IllegalArgumentException If the argument is <code>null</code> or if it contains elements that are not within 
	 * the specification of the "learning" simulation.
	 */
	public static ConsistentPublicLocalDynamicState createCopy( 
			ConsistentPublicLocalDynamicState toCopy 
	) {
		if( toCopy == null ){
			throw new IllegalArgumentException( "The 'toCopy' argument cannot be null." );
		}
		ConsistentPublicLocalDynamicState result = new ConsistentPublicLocalDynamicState(
				new SimulationTimeStamp( toCopy.getTime() ),
				toCopy.getLevel()
		);
		// Copy the public local state of the environment
		if( toCopy.getPublicLocalStateOfEnvironment() == null ){
			throw new IllegalArgumentException( "The public local state of the environment at the level '" +
					toCopy.getLevel() + "' in the consistent dynamic state at the time '" +
					toCopy.getTime() + "' was null."  );
		} else if( ! (toCopy.getPublicLocalStateOfEnvironment() instanceof LearningPublicLocalStateOfEnvironment) ){
			throw new IllegalArgumentException( "The public local state of the environment at the level '" +
					toCopy.getLevel() + "' in the consistent dynamic state at the time '" +
					toCopy.getTime() + "' is not an instance of the class '" + LearningPublicLocalStateOfEnvironment.class.getSimpleName() + "'."  );
		} else {
			result.setPublicLocalStateOfEnvironment( ( (LearningPublicLocalStateOfEnvironment) toCopy.getPublicLocalStateOfEnvironment() ).createCopy() );
		}
		// Copy the public local state of the agents
		for( IPublicLocalStateOfAgent agentPublicLocalState : toCopy.getPublicLocalStateOfAgents() ){
			if( agentPublicLocalState instanceof LearningPublicLocalStateOfAgent ){
				LearningPublicLocalStateOfAgent castedState = (LearningPublicLocalStateOfAgent) agentPublicLocalState;
				result.addPublicLocalStateOfAgent( castedState.createCopy() );
			} else {
				throw new IllegalArgumentException( "The public local state of an agent is not an instance of the class '" + LearningPublicLocalStateOfAgent.class.getSimpleName() + "'." );
			}
		}
		// Copy the state dynamics
		for( IInfluence influence : toCopy.getStateDynamics() ) {
			if( influence instanceof ILearningInfluence ){
				ILearningInfluence castedInfluence = (ILearningInfluence) influence;
				result.addInfluence( castedInfluence.createCopy() );
			} else {
				result.addInfluence( LearningInfluenceCopier.copyInfluence( influence ) );
			}
		}
		return result;
	}
}
