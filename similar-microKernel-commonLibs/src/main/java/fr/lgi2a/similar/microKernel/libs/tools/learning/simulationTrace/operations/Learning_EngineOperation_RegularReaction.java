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

import java.util.LinkedHashSet;
import java.util.Set;

import fr.lgi2a.similar.microKernel.I_Influence;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.Learning_ConsistentDynamicStateCopier;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.Learning_InfluenceCopier;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.Learning_EngineOperation;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.Learning_EngineOperationType;
import fr.lgi2a.similar.microKernel.states.dynamicStates.Consistent_PublicLocalDynamicState;

/**
 * Models the operation performed by the simulation engine when it asks a level to perform its reaction.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class Learning_EngineOperation_RegularReaction implements Learning_EngineOperation {
	/**
	 * The 'new consistent state' argument provided as a parameter to the 'reaction' method call.
	 */
	private Consistent_PublicLocalDynamicState newConsistentStateAtBeginning;
	/**
	 * The value of the 'new consistent state' argument at the end of the 'reaction' method call.
	 */
	private Consistent_PublicLocalDynamicState newConsistentStateAtEnd;
	/**
	 * The 'regularInfluencesOftransitoryStateDynamics' argument of the 'reaction' method call.
	 */
	private Set<I_Influence> regularInfluencesOftransitoryStateDynamicsArgument;
	/**
	 * The influences that were produced during the 'reaction' method call.
	 */
	private Set<I_Influence> producedInfluences;
	
	/**
	 * Builds a partially initialized model of a call to the 'reaction' method call of a level.
	 * The initialization is completed by calls to the:
	 * <ul>
	 * 	<li>
	 * 		{@link Learning_EngineOperation_RegularReaction#addArgumentInfluence(I_Influence)} for each influence located in the 
	 * 		'regularInfluencesOftransitoryStateDynamics' argument of the 'reaction' method call.
	 *	</li>
	 * 	<li>
	 * 		{@link Learning_EngineOperation_RegularReaction#addProducedInfluence(I_Influence)} for each influence located in the 
	 * 		influences produced by the reaction.
	 *	</li>
	 *	<li>
	 *		{@link Learning_EngineOperation_RegularReaction#setNewConsistentStateAtEnd(Consistent_PublicLocalDynamicState)} to store the
	 *		value of the new consistent state of the level after the reaction.
	 *	</li>
	 * </ul>
	 * @param newConsistentStateAtBeginning The 'new consistent state' argument provided as a parameter to the 'reaction' method call.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public Learning_EngineOperation_RegularReaction(
			Consistent_PublicLocalDynamicState newConsistentStateAtBeginning
	) throws IllegalArgumentException {
		if( newConsistentStateAtBeginning == null ){
			throw new IllegalArgumentException( "The 'newConsistentStateAtBeginning' argument cannot be null." );
		}
		if( newConsistentStateAtEnd == null ){
			throw new IllegalArgumentException( "The 'newConsistentStateAtEnd' argument cannot be null." );
		}
		this.newConsistentStateAtBeginning = Learning_ConsistentDynamicStateCopier.createCopy( newConsistentStateAtBeginning );
		this.regularInfluencesOftransitoryStateDynamicsArgument = new LinkedHashSet<I_Influence>();
		this.producedInfluences = new LinkedHashSet<I_Influence>();
	}
	
	/**
	 * {@inheritDoc}
	 * @see fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.Learning_EngineOperation#getOperationType()
	 */
	@Override
	public Learning_EngineOperationType getOperationType() {
		return Learning_EngineOperationType.REGULARREACTION;
	}

	/**
	 * Gets the 'new consistent state' argument provided as a parameter to the 'reaction' method call.
	 * @return The 'new consistent state' argument provided as a parameter to the 'reaction' method call.
	 */
	public Consistent_PublicLocalDynamicState getNewConsistentStateAtBeginning( ) {
		return this.newConsistentStateAtBeginning;
	}
	
	/**
	 * Gets the value of the 'new consistent state' argument at the end of the 'reaction' method call.
	 * @return The value of the 'new consistent state' argument at the end of the 'reaction' method call.
	 */
	public Consistent_PublicLocalDynamicState getNewConsistentStateAtEnd( ) {
		return this.newConsistentStateAtEnd;
	}
	/**
	 * Sets the value of the 'new consistent state' argument at the end of the 'reaction' method call.
	 * @param newConsistentStateAtEnd The value of the 'new consistent state' argument at the end of the 'reaction' method call.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public void setNewConsistentStateAtEnd(Consistent_PublicLocalDynamicState newConsistentStateAtEnd ) throws IllegalArgumentException {
		this.newConsistentStateAtEnd = Learning_ConsistentDynamicStateCopier.createCopy( newConsistentStateAtEnd );
	}
	
	/**
	 * Gets the 'regularInfluencesOftransitoryStateDynamics' argument of the 'reaction' method call.
	 * @return The 'regularInfluencesOftransitoryStateDynamics' argument of the 'reaction' method call.
	 */
	public Set<I_Influence> getRegularInfluencesOftransitoryStateDynamicsArgument( ) {
		return this.regularInfluencesOftransitoryStateDynamicsArgument;
	}
	
	/**
	 * Gets the influences that were produced during the 'reaction' method call.
	 * @return The influences that were produced during the 'reaction' method call.
	 */
	public Set<I_Influence> getProducedInfluences( ) {
		return this.producedInfluences;
	}
	
	/**
	 * Adds a copy of an influence to the set of influences contained in the 'regularInfluencesOftransitoryStateDynamics' argument 
	 * of the 'reaction' method call being modeled. 
	 * @param influence The influence to add.
	 * @throws IllegalArgumentException If an argument is <code>null</code> or if the influence is not supported.
	 */
	public void addArgumentInfluence(
			I_Influence influence
	) throws IllegalArgumentException {
		if( influence == null ){
			throw new IllegalArgumentException( "The 'influence' argument cannot be null." );
		}
		this.regularInfluencesOftransitoryStateDynamicsArgument.add( Learning_InfluenceCopier.copyInfluence( influence ) );
	}
	
	/**
	 * Adds a copy of an influence to the set of influences generated during the 'reaction' method call being modeled. 
	 * @param influence The influence to add.
	 * @throws IllegalArgumentException If an argument is <code>null</code> or if the influence is not supported.
	 */
	public void addProducedInfluence(
			I_Influence influence
	) throws IllegalArgumentException {
		if( influence == null ){
			throw new IllegalArgumentException( "The 'influence' argument cannot be null." );
		}
		this.producedInfluences.add( Learning_InfluenceCopier.copyInfluence( influence ) );
	}
}
