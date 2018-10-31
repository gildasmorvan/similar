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
 * The system influence sent to a level when the reaction of that level has to remove an agent from the level.
 * This influence implies that the public and the private local state of the agent also have to be removed from 
 * the agent itself.
 * 
 * <h1>Usage</h1>
 * <p>
 * 	This influence is sent to the levels from which the agent is being removed, to exclude 
 * 	the local states of the agent from the dynamic state of these levels as well as from the specifications 
 * 	of the agent.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class SystemInfluenceRemoveAgentFromLevel extends SystemInfluence {
	/**
	 * The category of this influence.
	 */
	public static final String CATEGORY = "System influence - Remove agent from a level";

	/**
	 * The public local state of the agent being removed from the level.
	 */
	private ILocalStateOfAgent4Engine agent;
	
	
	
	/**
	 * Builds a 'Remove agent from a level' system influence, removing an agent from the 
	 * dynamic state of a specific level during the next reaction of that level.
	 * @param timeLowerBound The lower bound of the transitory period during which this influence was created.
	 * @param timeUpperBound The upper bound of the transitory period during which this influence was created.
	 * @param publicLocalState The public local state identifying both the agent and the level from which it has to
	 * be removed.
	 * @throws IllegalArgumentException If the public local state is <code>null</code>.
	 */
	public SystemInfluenceRemoveAgentFromLevel( 
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound, 
			ILocalStateOfAgent4Engine publicLocalState 
	) {
		super( 
			CATEGORY, 
			checkParameterValidity( publicLocalState ).getLevel(),
			timeLowerBound,
			timeUpperBound
		);
		this.agent = publicLocalState;
	}
	
	/**
	 * Builds a 'Remove agent from a level' system influence, removing an agent from the 
	 * dynamic state of a specific level during the next reaction of that level.
	 * @param timeLowerBound The lower bound of the transitory period during which this influence was created.
	 * @param timeUpperBound The upper bound of the transitory period during which this influence was created.
	 * @param agent The agent being removed from the level having the identifier <code>levelId</code>.
	 * @param levelId The identifier of the level from which the agent is removed.
	 */
	public SystemInfluenceRemoveAgentFromLevel(
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound, 
			IAgent4Engine agent,
			LevelIdentifier levelId
	){
		this(
			timeLowerBound,
			timeUpperBound,
			(ILocalStateOfAgent4Engine) agent.getPublicLocalState( levelId )
		);
	}
	
	/**
	 * Checks the validity of the parameters of the constructor.
	 * @param publicLocalState The second parameter of the constructor.
	 * @return The value of <code>publicLocalState</code>. This returned value is a trick allowing the integration of this tests 
	 * in the parameters of the super constructor call.
	 */
	private static ILocalStateOfAgent4Engine checkParameterValidity( 
			ILocalStateOfAgent publicLocalState
	) {
		if( publicLocalState == null ){
			throw new IllegalArgumentException( "The 'publicLocalState' argument cannot be null." );
		} else if( ! ( publicLocalState instanceof ILocalStateOfAgent4Engine ) ) {
			throw new IllegalArgumentException( "The 'publicLocalState' argument cannot has to be an instance of the" +
					ILocalStateOfAgent4Engine.class.getSimpleName() + " interface (found " + publicLocalState.getClass() + ")" );
		} else {
			return (ILocalStateOfAgent4Engine) publicLocalState;
		}
	}
	
	/**
	 * Gets the public local state of the agent being removed from the level.
	 * @return The public local state of the agent being removed from the level.
	 */
	public ILocalStateOfAgent4Engine getAgentLocalState( ){
		return this.agent;
	}

	/**
	 * Uses the category, the target level and the owner of the public local state of the influence 
	 * to build a printable version of this object.
	 * @return The concatenation of the category, the target level and the added owner of the public local state of the influence.
	 */
	@Override
	public String toString(){
		return super.toString() + ", with owner " + this.agent;
	}
}
