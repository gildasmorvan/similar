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
package fr.lgi2a.similar.microkernel.libs.tools.learning.model;

import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluence_AddAgent;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluence_AddPublicLocalStateToDynamicState;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluence_RemoveAgent;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluence_RemovePublicLocalStateFromDynamicState;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.influence.I_LearningInfluence;

/**
 * This class defines a method creating a copy of an influence.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class Learning_InfluenceCopier {
	/**
	 * Creates a copy of an influence.
	 * @param influence The influence to copy.
	 * @return The copy of the influence.
	 * @throws IllegalArgumentException If the influence is a system influence that is not managed by 
	 * the "learning" simulation or if it is not an influence defined for the "learning" simulation.
	 */
	public static IInfluence copyInfluence(
			IInfluence influence
	) throws IllegalArgumentException {
		if( influence.isSystem() ){
			if( influence instanceof SystemInfluence_AddAgent ){
				SystemInfluence_AddAgent castedInfluence = (SystemInfluence_AddAgent) influence;
				if( ! ( castedInfluence.getAgent() instanceof Learning_AbstractAgent ) ){
					throw new IllegalArgumentException( "The agents have to be instances of the '" + Learning_AbstractAgent.class.getSimpleName() + "' class." );
				}
				Learning_AbstractAgent agentCopy = ((Learning_AbstractAgent) castedInfluence.getAgent() ).createCopy();
				return new SystemInfluence_AddAgent( castedInfluence.getTargetLevel(), agentCopy );
			} else if( influence instanceof SystemInfluence_AddPublicLocalStateToDynamicState ){
				SystemInfluence_AddPublicLocalStateToDynamicState castedInfluence = (SystemInfluence_AddPublicLocalStateToDynamicState) influence;
				if( ! ( castedInfluence.getPublicLocalState() instanceof Learning_PublicLocalStateOfAgent ) ){
					throw new IllegalArgumentException( "The public local states of agents have to be instances of the '" + Learning_PublicLocalStateOfAgent.class.getSimpleName() + "' class." );
				}
				Learning_PublicLocalStateOfAgent castedState = (Learning_PublicLocalStateOfAgent) castedInfluence.getPublicLocalState();
				return new SystemInfluence_AddPublicLocalStateToDynamicState( castedState.getLevel(), castedState.createCopy() );
			} else if( influence instanceof SystemInfluence_RemoveAgent ){
				SystemInfluence_RemoveAgent castedInfluence = (SystemInfluence_RemoveAgent) influence;
				if( ! ( castedInfluence.getAgent() instanceof Learning_AbstractAgent ) ){
					throw new IllegalArgumentException( "The agents have to be instances of the '" + Learning_AbstractAgent.class.getSimpleName() + "' class." );
				}
				Learning_AbstractAgent agentCopy = ((Learning_AbstractAgent) castedInfluence.getAgent() ).createCopy();
				return new SystemInfluence_RemoveAgent( castedInfluence.getTargetLevel(), agentCopy.getPublicLocalState( castedInfluence.getTargetLevel() ) );
			} else if( influence instanceof SystemInfluence_RemovePublicLocalStateFromDynamicState ){
				SystemInfluence_RemovePublicLocalStateFromDynamicState castedInfluence = (SystemInfluence_RemovePublicLocalStateFromDynamicState) influence;
				if( ! ( castedInfluence.getPublicLocalState() instanceof Learning_PublicLocalStateOfAgent ) ){
					throw new IllegalArgumentException( "The public local states of agents have to be instances of the '" + Learning_PublicLocalStateOfAgent.class.getSimpleName() + "' class." );
				}
				Learning_PublicLocalStateOfAgent castedState = (Learning_PublicLocalStateOfAgent) castedInfluence.getPublicLocalState();
				return new SystemInfluence_RemovePublicLocalStateFromDynamicState( castedState.getLevel(), castedState.createCopy() );
			} else {
				throw new IllegalArgumentException( "System influences of the '" + influence.getClass().getSimpleName() + "' class are not supported by this model. " +
						"If you wish to support them, then tell in this method how to clone them." );
			}
		} else if( influence instanceof I_LearningInfluence ) {
			I_LearningInfluence castedInfluence = (I_LearningInfluence) influence;
			return castedInfluence.createCopy();
		} else {
			throw new IllegalArgumentException( "Agents can only produce system influences or instances of " +
					"the '" + I_LearningInfluence.class.getSimpleName() + "' interface (was '" + influence.getClass().getSimpleName() + "')." );
		}
	}
}
