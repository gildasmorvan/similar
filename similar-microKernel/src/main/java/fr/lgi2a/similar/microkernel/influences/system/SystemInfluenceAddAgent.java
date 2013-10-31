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
package fr.lgi2a.similar.microkernel.influences.system;

import fr.lgi2a.similar.microkernel.IAgent;
import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.influences.SystemInfluence;

/**
 * The system influence sent to a level when the reaction of that level has to insert a new agent into the simulation 
 * and make appear it public local state into the public dynamic state of the levels.
 * 
 * <h1>Usage</h1>
 * <p>
 * 	The agent has to be fully initialized before being added using this influence.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public final class SystemInfluenceAddAgent extends SystemInfluence {
	/**
	 * The category of this influence.
	 */
	public static final String CATEGORY = "System influence - Add agent";

	/**
	 * The agent to add to the simulation.
	 */
	private IAgent agent;
	
	/**
	 * Builds an 'Add agent' system influence adding a specific agent to the simulation during the next reaction of a specific level.
	 * @param targetLevel The target level as described in {@link IInfluence#getTargetLevel()}
	 * @param agent The agent to add to the simulation.
	 * @throws IllegalArgumentException If the target level or the agent are <code>null</code>.
	 */
	public SystemInfluenceAddAgent( LevelIdentifier targetLevel, IAgent agent ) throws IllegalArgumentException {
		super( CATEGORY, targetLevel );
		if( agent == null ){
			throw new IllegalArgumentException( "The 'agent' argument cannot be null." );
		}
		this.agent = agent;
	}

	/**
	 * Gets the agent to add to the simulation.
	 * @return The agent to add to the simulation.
	 */
	public IAgent getAgent(){
		return this.agent;
	}

	/**
	 * Uses the category, the target level and the added agent of the influence to build a printable version of this object.
	 * @return The concatenation of the category, the target level and the added agent of the influence.
	 */
	public String toString(){
		return super.toString() + ", adding " + this.agent.toString();
	}
}
