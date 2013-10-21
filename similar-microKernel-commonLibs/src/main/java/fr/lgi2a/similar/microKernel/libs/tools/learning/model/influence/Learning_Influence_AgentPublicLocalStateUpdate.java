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
package fr.lgi2a.similar.microKernel.libs.tools.learning.model.influence;

import fr.lgi2a.similar.microKernel.LevelIdentifier;
import fr.lgi2a.similar.microKernel.influences.RegularInfluence;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.Learning_PublicLocalStateOfAgent;
import fr.lgi2a.similar.microKernel.states.I_PublicLocalStateOfAgent;

/**
 * An influence sent by an agent or by the environment to update the public local state of an agent in a specific level.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class Learning_Influence_AgentPublicLocalStateUpdate extends RegularInfluence implements I_LearningInfluence {
	/**
	 * The category of this influence.
	 */
	public static final String CATEGORY = "Agent public local state update";
	
	/**
	 * A counter providing unique identifiers for the influence.
	 */
	private static long influenceCounter = 0;
	
	/**
	 * The unique identifier of the influence.
	 */
	private long influenceId;
	
	/**
	 * The public local state modified in reaction to this influence.
	 */
	private Learning_PublicLocalStateOfAgent publicLocalStateOfAgent;
	
	/**
	 * Builds a regular influence from the 'Update public local state of agent' category.
	 * @param targetLevel The level containing the public local state of the agent to update.
	 * @param publicLocalStateOfAgent The public local state modified in reaction to this influence.
	 * @throws IllegalArgumentException If one of the arguments is <code>null</code> or if the public local 
	 * state of the agent has not the appropriate class.
	 */
	public Learning_Influence_AgentPublicLocalStateUpdate(
			LevelIdentifier targetLevel,
			I_PublicLocalStateOfAgent publicLocalStateOfAgent
	) throws IllegalArgumentException {
		super( CATEGORY, targetLevel );
		this.influenceId = ++influenceCounter;
		if( !( publicLocalStateOfAgent instanceof Learning_PublicLocalStateOfAgent ) ){
			throw new IllegalArgumentException( "The physical state of the agent has to be from " +
					"the '" + Learning_PublicLocalStateOfAgent.class.getSimpleName() + "' class." );
		}
		this.publicLocalStateOfAgent = (Learning_PublicLocalStateOfAgent) publicLocalStateOfAgent;
	}
	
	/**
	 * Builds a copy of an 'Update public local state of agent' influence.
	 * @param toCopy The influence to copy.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public Learning_Influence_AgentPublicLocalStateUpdate(
			Learning_Influence_AgentPublicLocalStateUpdate toCopy
	) throws IllegalArgumentException {
		super( CATEGORY, toCopy.getTargetLevel() );
		this.influenceId = toCopy.influenceId;
		this.publicLocalStateOfAgent = toCopy.publicLocalStateOfAgent.createCopy();
	}

	/**
	 * Gets the public local state modified in reaction to this influence.
	 * @return The public local state modified in reaction to this influence.
	 */
	public Learning_PublicLocalStateOfAgent getPublicLocalStateOfAgent( ) {
		return this.publicLocalStateOfAgent;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return super.toString() + "#" + this.influenceId + ", updating " + publicLocalStateOfAgent.getOwner();
	}

	/**
	 * {@inheritDoc}
	 * @see fr.lgi2a.similar.microKernel.libs.tools.learning.model.influence.I_LearningInfluence#createCopy()
	 */
	public I_LearningInfluence createCopy() {
		return new Learning_Influence_AgentPublicLocalStateUpdate( this );
	}
}
