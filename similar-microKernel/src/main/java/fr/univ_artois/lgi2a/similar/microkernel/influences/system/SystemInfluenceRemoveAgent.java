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
package fr.univ_artois.lgi2a.similar.microkernel.influences.system;

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.influences.SystemInfluence;

/**
 * The system influence sent to a level when the reaction of that level has to remove the agent from the simulation, 
 * and make disappear its public local state from the public dynamic state of the levels.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public final class SystemInfluenceRemoveAgent extends SystemInfluence {
	/**
	 * The category of this influence.
	 */
	public static final String CATEGORY = "System influence - Remove agent";

	/**
	 * The agent to remove from the simulation.
	 */
	private IAgent4Engine agent;
	
	/**
	 * Builds a 'Remove agent' system influence removing a specific agent from the simulation during the next reaction of a specific level.
	 * @param targetLevel The target level as described in {@link fr.univ_artois.lgi2a.similar.microkernel.influences.IInfluence#getTargetLevel()}
	 * @param publicLocalStateOfAgent A public local state of the agent to remove from the simulation
	 * @param timeLowerBound The lower bound of the transitory period during which this influence was created.
	 * @param timeUpperBound The upper bound of the transitory period during which this influence was created.
	 * @throws IllegalArgumentException If the target level or the agent are <code>null</code>.
	 */
	public SystemInfluenceRemoveAgent( 
			LevelIdentifier targetLevel, 
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound, 
			ILocalStateOfAgent publicLocalStateOfAgent 
	) {
		super( 
				CATEGORY, 
				targetLevel,
				timeLowerBound,
				timeUpperBound
		);
		if( publicLocalStateOfAgent == null ){
			throw new IllegalArgumentException( "The 'publicLocalStateOfAgent' argument cannot be null." );
		} else if( ! ( publicLocalStateOfAgent instanceof ILocalStateOfAgent4Engine ) ) {
			throw new IllegalArgumentException( "The 'publicLocalStateOfAgent' argument has to be an instance of the " + 
						ILocalStateOfAgent4Engine.class.getSimpleName() + " interface (found type " + publicLocalStateOfAgent.getClass()
						+ ")." );
		}
		this.agent = ((ILocalStateOfAgent4Engine)publicLocalStateOfAgent).getOwner();
	}

	/**
	 * Gets the agent to remove from the simulation.
	 * @return The agent to remove from the simulation.
	 */
	public IAgent4Engine getAgent(){
		return this.agent;
	}

	/**
	 * Uses the category, the target level and the removed agent of the influence to build a printable version of this object.
	 * @return The concatenation of the category, the target level and the removed agent of the influence.
	 */
	@Override
	public String toString(){
		return super.toString() + ", removing " + this.agent.toString();
	}
}
