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
package fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.operations;

import java.util.HashMap;
import java.util.Map;

import fr.lgi2a.similar.microKernel.libs.tools.learning.model.Learning_GlobalMemoryState;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.Learning_PerceivedDataOfAgent;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.Learning_EngineOperation;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.Learning_EngineOperationType;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.agentbehavior.I_PerceivedDataOfAgent;

/**
 * Models the operation performed by the simulation engine when it asks an agent to revise its memory.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class Learning_EngineOperation_ReviseMemory implements Learning_EngineOperation {
	/**
	 * The global memory state of the agent after the last reaction preceding the 'memory revision' method call.
	 */
	private Learning_GlobalMemoryState previousMemoryState;
	/**
	 * The global memory state returned by the 'memory revision' method call.
	 */
	private Learning_GlobalMemoryState methodResult;
	/**
	 * The last perceived data of the agent provided as a parameter of the 'memory revision' method call.
	 */
	private Map<LevelIdentifier,Learning_PerceivedDataOfAgent> perceivedData;
	
	/**
	 * Builds a partly-initialized model of a call to the 'memory revision' method of an agent.
	 * The complete initialization is achieved by making calls to the {@link Learning_EngineOperation_ReviseMemory#addPerceivedData(I_PerceivedDataOfAgent)} method 
	 * for each perceptible level by the agent.
	 * @param previousMemoryState The global memory state of the agent after the last reaction preceding the 'memory revision' method call. 
	 * @param methodResult The global memory state returned by the 'memory revision' method call.
	 * @throws IllegalArgumentException The last perceived data of the agent provided as a parameter of the 'memory revision' method call.
	 */
	public Learning_EngineOperation_ReviseMemory(
			Learning_GlobalMemoryState previousMemoryState,
			Learning_GlobalMemoryState methodResult
	) throws IllegalArgumentException {
		if( previousMemoryState == null ){
			throw new IllegalArgumentException( "The 'previousMemoryState' argument cannot be null." );
		} else if( methodResult == null ){
			throw new IllegalArgumentException( "The 'methodResult' argument cannot be null." );
		}
		this.previousMemoryState = previousMemoryState.createCopy();
		this.methodResult = methodResult.createCopy();
		this.perceivedData = new HashMap<LevelIdentifier,Learning_PerceivedDataOfAgent>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Learning_EngineOperationType getOperationType() {
		return Learning_EngineOperationType.MEMORIZATION;
	}

	/**
	 * Gets the global memory state of the agent after the last reaction preceding the 'memory revision' method call.
	 * @return The global memory state of the agent after the last reaction preceding the 'memory revision' method call.
	 */
	public Learning_GlobalMemoryState getPreviousMemoryState( ) {
		return this.previousMemoryState;
	}
	
	/**
	 * Gets the global memory state returned by the 'memory revision' method call.
	 * @return The global memory state returned by the 'memory revision' method call.
	 */
	public Learning_GlobalMemoryState getMethodResult( ) {
		return this.methodResult;
	}
	
	/**
	 * Gets the last perceived data of the agent provided as a parameter of the 'memory revision' method call.
	 * @return The last perceived data of the agent provided as a parameter of the 'memory revision' method call.
	 */
	public Map<LevelIdentifier,Learning_PerceivedDataOfAgent> getPerceivedData( ) {
		return this.perceivedData;
	}
	
	/**
	 * Adds perceived data to the arguments of the 'revise memory' method call modeled by this object.
	 * @param perceivedData The perceived data to add as an argument of the method call modeled by this object.
	 * @throws IllegalArgumentException If an argument is <code>null</code> or is not part of the "learning" simulation.
	 */
	public void addPerceivedData(
			I_PerceivedDataOfAgent perceivedData
	) throws IllegalArgumentException {
		if( perceivedData == null ){
			throw new IllegalArgumentException( "The 'perceivedData' argument cannot be null." );
		} else if( this.perceivedData.containsKey( perceivedData.getLevel() ) ){
			throw new IllegalArgumentException( "This object contains already perceived data for the level '" + perceivedData.getLevel() + "'." );
		} else if( perceivedData instanceof Learning_PerceivedDataOfAgent ){
			Learning_PerceivedDataOfAgent casted = (Learning_PerceivedDataOfAgent) perceivedData;
			this.perceivedData.put( casted.getLevel(), casted.createCopy() );
		} else {
			throw new IllegalArgumentException( "The 'perceivedData' argument has to be an instance of the '" + Learning_PerceivedDataOfAgent.class.getSimpleName() + "' class." );
		}
	}
}
