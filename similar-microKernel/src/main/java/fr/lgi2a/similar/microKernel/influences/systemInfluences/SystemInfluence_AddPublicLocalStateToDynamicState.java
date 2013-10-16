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
package fr.lgi2a.similar.microKernel.influences.systemInfluences;

import fr.lgi2a.similar.microKernel.I_Influence;
import fr.lgi2a.similar.microKernel.LevelIdentifier;
import fr.lgi2a.similar.microKernel.influences.SystemInfluence;
import fr.lgi2a.similar.microKernel.states.I_PublicLocalStateOfAgent;

/**
 * The system influence sent to a level when the reaction of that level has to add the public local state of an agent into the 
 * public dynamic state of the level.
 * 
 * <h1>Usage</h1>
 * <p>
 * 	When an agent is added to the simulation, this influence is sent to the levels where the agent resides, to include 
 * 	the public local state of the agent into the dynamic state of these levels.
 * </p>
 * <p>
 * 	This influence has to be generated only by the simulation engine, in reaction to an {@link SystemInfluence_AddAgent} influence.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class SystemInfluence_AddPublicLocalStateToDynamicState extends SystemInfluence {
	/**
	 * The category of this influence.
	 */
	public static final String CATEGORY = "System influence - Add public local state agent";
	
	/**
	 * The public local state to add to the public dynamic local state of the level.
	 */
	private I_PublicLocalStateOfAgent publicLocalState;
	
	/**
	 * Builds an 'Add the public local state of an agent' system influence, adding the local state of a specific agent to the 
	 * dynamic state of a specific level during the next reaction of that level.
	 * @param targetLevel The target level of the influence, as defined in {@link I_Influence#getTargetLevel()}.
	 * @param publicLocalState The public local state to add to the public dynamic local state of the level.
	 * @throws IllegalArgumentException If the target level or the public local state are <code>null</code>.
	 */
	public SystemInfluence_AddPublicLocalStateToDynamicState( 
			LevelIdentifier targetLevel, 
			I_PublicLocalStateOfAgent publicLocalState 
	) throws IllegalArgumentException {
		super( CATEGORY, targetLevel );
		if( publicLocalState == null ){
			throw new IllegalArgumentException( "The 'publicLocalState' argument cannot be null." );
		}
		this.publicLocalState = publicLocalState;
	}

	
	/**
	 * Gets the public local state to add to the public dynamic local state of the level.
	 * @return The public local state to add to the public dynamic local state of the level.
	 */
	public I_PublicLocalStateOfAgent getPublicLocalState( ) {
		return this.publicLocalState;
	}

	/**
	 * Uses the category, the target level and the owner of the public local state of the influence 
	 * to build a printable version of this object.
	 * @return The concatenation of the category, the target level and the added owner of the public local state of the influence.
	 */
	public String toString(){
		return super.toString() + ", with owner " + this.publicLocalState;
	}
}
