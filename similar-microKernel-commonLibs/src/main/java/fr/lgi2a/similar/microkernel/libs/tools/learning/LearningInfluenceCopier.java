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
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddPublicLocalStateToDynamicState;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemovePublicLocalStateFromDynamicState;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.AbstractLearningAgent;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.LearningPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.influence.ILearningInfluence;

/**
 * This class defines a method creating a copy of an influence.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public final class LearningInfluenceCopier {
	/**
	 * Builds the text of the {@link IllegalArgumentException} thrown if the user employs an inappropriate class in his model.
	 * @param itemDescription The description of the element of the model having the wrong class.
	 * @param expectedClass The excepted class.
	 * @param usedClass The class used by the user.
	 * @return The text of the {@link IllegalArgumentException} thrown if the user employs an inappropriate class in his model.
	 */
	private static String buildWrongClassExceptionText( String itemDescription, Class<?> expectedClass, Class<?> usedClass ) {
		return "The " + itemDescription + " have to be instances of the '" + expectedClass.getSimpleName() + "' class (found '" + usedClass.getSimpleName() + "').";
	}
	/**
	 * This constructor is never used.
	 */
	private LearningInfluenceCopier( ){ }
	
	/**
	 * Creates a copy of an influence.
	 * @param influence The influence to copy.
	 * @return The copy of the influence.
	 * @throws IllegalArgumentException If the influence is a system influence that is not managed by 
	 * the "learning" simulation or if it is not an influence defined for the "learning" simulation.
	 */
	public static IInfluence copyInfluence(
			IInfluence influence
	) {
		if( influence.isSystem() ){
			return copySystemInfluence( influence );
		} else if( influence instanceof ILearningInfluence ) {
			ILearningInfluence castedInfluence = (ILearningInfluence) influence;
			return castedInfluence.createCopy();
		} else {
			throw new IllegalArgumentException( "Agents can only produce system influences or instances of " +
					"the '" + ILearningInfluence.class.getSimpleName() + "' interface (was '" + influence.getClass().getSimpleName() + "')." );
		}
	}
	
	/**
	 * Creates a copy of a system influence.
	 * @param castedInfluence The system influence to copy.
	 * @return A copy of the influence.
	 */
	private static IInfluence copySystemInfluence( IInfluence systemInfluence ) {
		if( systemInfluence instanceof SystemInfluenceAddAgent ){
			return copyInfluence( (SystemInfluenceAddAgent) systemInfluence );
		} else if( systemInfluence instanceof SystemInfluenceAddPublicLocalStateToDynamicState ){
			return copyInfluence( (SystemInfluenceAddPublicLocalStateToDynamicState) systemInfluence );
		} else if( systemInfluence instanceof SystemInfluenceRemoveAgent ){
			return copyInfluence( (SystemInfluenceRemoveAgent) systemInfluence );
		} else if( systemInfluence instanceof SystemInfluenceRemovePublicLocalStateFromDynamicState ){
			return copyInfluence( (SystemInfluenceRemovePublicLocalStateFromDynamicState) systemInfluence );
		} else {
			throw new IllegalArgumentException( "System influences of the '" + systemInfluence.getClass().getSimpleName() + "' class are not supported by this model. " +
					"If you wish to support them, then tell in this method how to clone them." );
		}
	}
	
	/**
	 * Creates a copy of a system influence from the class {@link SystemInfluenceAddAgent}.
	 * @param castedInfluence The influence to copy.
	 * @return A copy of the influence.
	 */
	private static SystemInfluenceAddAgent copyInfluence( SystemInfluenceAddAgent castedInfluence ){
		if( ! ( castedInfluence.getAgent() instanceof AbstractLearningAgent ) ){
			throw new IllegalArgumentException( buildWrongClassExceptionText( 
					"agents", 
					AbstractLearningAgent.class, 
					castedInfluence.getAgent().getClass() 
			) );
		}
		AbstractLearningAgent agentCopy = ((AbstractLearningAgent) castedInfluence.getAgent() ).createCopy();
		return new SystemInfluenceAddAgent( castedInfluence.getTargetLevel(), agentCopy );
	}
	
	/**
	 * Creates a copy of a system influence from the class {@link SystemInfluenceAddPublicLocalStateToDynamicState}.
	 * @param castedInfluence The influence to copy.
	 * @return A copy of the influence.
	 */
	private static SystemInfluenceAddPublicLocalStateToDynamicState copyInfluence( SystemInfluenceAddPublicLocalStateToDynamicState castedInfluence ){
		if( ! ( castedInfluence.getPublicLocalState() instanceof LearningPublicLocalStateOfAgent ) ){
			throw new IllegalArgumentException( buildWrongClassExceptionText( 
					"public local states of agents", 
					LearningPublicLocalStateOfAgent.class, 
					castedInfluence.getPublicLocalState().getClass() 
			) );
		}
		LearningPublicLocalStateOfAgent castedState = (LearningPublicLocalStateOfAgent) castedInfluence.getPublicLocalState();
		return new SystemInfluenceAddPublicLocalStateToDynamicState( castedState.getLevel(), castedState.createCopy() );
	}
	
	/**
	 * Creates a copy of a system influence from the class {@link SystemInfluenceRemoveAgent}.
	 * @param castedInfluence The influence to copy.
	 * @return A copy of the influence.
	 */
	private static SystemInfluenceRemoveAgent copyInfluence( SystemInfluenceRemoveAgent castedInfluence ){
		if( ! ( castedInfluence.getAgent() instanceof AbstractLearningAgent ) ){
			throw new IllegalArgumentException( buildWrongClassExceptionText( 
					"agents", 
					AbstractLearningAgent.class, 
					castedInfluence.getAgent().getClass() 
			) );}
		AbstractLearningAgent agentCopy = ((AbstractLearningAgent) castedInfluence.getAgent() ).createCopy();
		return new SystemInfluenceRemoveAgent( castedInfluence.getTargetLevel(), agentCopy.getPublicLocalState( castedInfluence.getTargetLevel() ) );
	}
	
	/**
	 * Creates a copy of a system influence from the class {@link SystemInfluenceRemovePublicLocalStateFromDynamicState}.
	 * @param castedInfluence The influence to copy.
	 * @return A copy of the influence.
	 */
	private static SystemInfluenceRemovePublicLocalStateFromDynamicState copyInfluence( SystemInfluenceRemovePublicLocalStateFromDynamicState castedInfluence ){
		if( ! ( castedInfluence.getPublicLocalState() instanceof LearningPublicLocalStateOfAgent ) ){
			throw new IllegalArgumentException( buildWrongClassExceptionText( 
					"public local states of agents", 
					LearningPublicLocalStateOfAgent.class, 
					castedInfluence.getPublicLocalState().getClass() 
			) );
		}
		LearningPublicLocalStateOfAgent castedState = (LearningPublicLocalStateOfAgent) castedInfluence.getPublicLocalState();
		return new SystemInfluenceRemovePublicLocalStateFromDynamicState( castedState.getLevel(), castedState.createCopy() );
	}
}
