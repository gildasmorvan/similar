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
package fr.lgi2a.similar.microkernel.libs.tools.learning.trace.operations;

import fr.lgi2a.similar.microkernel.IGlobalMemoryState;
import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.IPerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.InfluencesMap;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.libs.tools.learning.LearningInfluenceCopier;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.LearningGlobalMemoryState;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.LearningPerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.ILearningEngineOperation;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.LearningEngineOperationType;

/**
 * Models the operation performed by the simulation engine when it asks an agent to decide from a level.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class LearningEngineOperationDecision implements ILearningEngineOperation {
	/**
	 * The level provided as a parameter of the 'decision' method call of the agent.
	 */
	private LevelIdentifier level;
	/**
	 * The global memory state provided as a parameter of the 'decision' method call of the agent.
	 */
	private LearningGlobalMemoryState memoryState;
	/**
	 * The perceived data provided as a parameter of the 'decision' method call of the agent.
	 */
	private LearningPerceivedDataOfAgent perceivedData;
	/**
	 * The influences that were produced by the decision of the agent.
	 */
	private InfluencesMap producedInfluences;
	
	/**
	 * Builds a partially initialized object modeling a method call to the 'decision' method of an agent.
	 * The initialization of this instance is completed by calls to the {@link LearningEngineOperationDecision#addInfluence(IInfluence)} for each
	 * influence located in the produced influences of the 'decision' method call.
	 * @param level The level provided as a parameter of the 'decision' method call of the agent.
	 * @param memoryState The global memory state provided as a parameter of the 'decision' method call of the agent.
	 * @param perceivedData The perceived data provided as a parameter of the 'decision' method call of the agent.
	 * @throws IllegalArgumentException If an argument is <code>null</code> or if the arguments are not elements 
	 * from the "learning" model.
	 */
	public LearningEngineOperationDecision(
			LevelIdentifier level,
			IGlobalMemoryState memoryState,
			IPerceivedDataOfAgent perceivedData
	) {
		if( level == null ){
			throw new IllegalArgumentException( "The 'level' argument cannot be null." );
		} else if( memoryState == null ){
			throw new IllegalArgumentException( "The 'memoryState' argument cannot be null." );
		} else if( perceivedData == null ){
			throw new IllegalArgumentException( "The 'perceivedData' argument cannot be null." );
		}
		this.level = level;
		if( !( memoryState instanceof LearningGlobalMemoryState ) ){
			throw new IllegalArgumentException( "The memory state of the agent has to be from the '" + LearningGlobalMemoryState.class + "' class." );
		} else {
			LearningGlobalMemoryState castedState = (LearningGlobalMemoryState) memoryState;
			this.memoryState = castedState.createCopy();
		}
		if( !( perceivedData instanceof LearningPerceivedDataOfAgent ) ){
			throw new IllegalArgumentException( "The perceived data of the agent has to be from the '" + LearningPerceivedDataOfAgent.class + "' class." );
		} else {
			LearningPerceivedDataOfAgent castedData = (LearningPerceivedDataOfAgent) perceivedData;
			this.perceivedData = castedData.createCopy();
		}
		this.producedInfluences = new InfluencesMap();
	}
	
	/**
	 * {@inheritDoc}
	 * @see fr.lgi2a.similar.microkernel.libs.tools.learning.trace.ILearningEngineOperation#getOperationType()
	 */
	@Override
	public LearningEngineOperationType getOperationType() {
		return LearningEngineOperationType.DECISION;
	}
	
	/**
	 * Gets the level provided as a parameter of the 'decision' method call of the agent.
	 * @return The level provided as a parameter of the 'decision' method call of the agent.
	 */
	public LevelIdentifier getLevel( ) {
		return this.level;
	}
	
	/**
	 * Gets the global memory state provided as a parameter of the 'decision' method call of the agent.
	 * @return The global memory state provided as a parameter of the 'decision' method call of the agent.
	 */
	public LearningGlobalMemoryState getMemoryState( ) {
		return this.memoryState;
	}
	
	/**
	 * Gets the perceived data provided as a parameter of the 'decision' method call of the agent.
	 * @return The perceived data provided as a parameter of the 'decision' method call of the agent.
	 */
	public LearningPerceivedDataOfAgent getPerceivedData( ) {
		return this.perceivedData;
	}
	
	/**
	 * Gets the influences that were produced by the decision of the agent.
	 * @return The influences that were produced by the decision of the agent.
	 */
	public InfluencesMap getProducedInfluences( ) {
		return this.producedInfluences;
	}
	
	/**
	 * Adds an influence to the produced influence map argument of this 'decision' method call model.
	 * @param influence The influence to add.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public void addInfluence( 
			IInfluence influence
	) {
		if( influence == null ){
			throw new IllegalArgumentException( "The 'influence' argument cannot be null." );
		}
		this.producedInfluences.add( LearningInfluenceCopier.copyInfluence( influence ) );
	}
}
