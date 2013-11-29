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
package fr.lgi2a.similar.extendedkernel;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import fr.lgi2a.similar.microkernel.IDynamicStateMap;
import fr.lgi2a.similar.microkernel.IGlobalMemoryState;
import fr.lgi2a.similar.microkernel.IPerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.InfluencesMap;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractAgent;

/**
 * Models an agent in the extended kernel.
 * On the opposite of the micro-kernel, where agent classes had to be created, this class is self-sufficient.
 * The operational code of the perception, memory revision and decision processes is defined in separate 
 * classes. The agent class only stores a reference to these classes.
 * <h2>Benefits</h2>
 * This property has huge benefits: 
 * <ul>
 * 	<li>
 * 		The behavior of the agent can be changed modularly: for instance, 
 * 		a modification in the decision process won't modify the class where 
 * 		the memory revision process is defined;
 * 	</li>
 * 	<li>
 * 		The behavior of the agent can evolve at runtime.
 * 	</li>
 * <h2>Agent specification</h2>
 * <p>
 * 	The specification of such agents requires three operations:
 * </p>
 * <ul>
 * 	<li>
 * 		The definition of the initial global memory state of the agent, using the 
 * 		{@link AbstractAgent#initializeGlobalMemoryState(IGlobalMemoryState)} method.
 * 	</li>
 * 	<li>
 * 		The specification of the memory revision model of the agent, using the 
 * 		{@link ExtendedAgent#setMemoryRevisionModel(IAgtMemoryRevisionModel)} method.
 * 	</li>
 * 	<li>
 * 		The specification of the levels where the agent lies, using the 
 * 		{@link ExtendedAgent#includeNewLevel(LevelIdentifier, IPublicLocalStateOfAgent, IAgtPerceptionModel, IAgtDecisionModel)} method.
 * 	</li>
 * </ul>
 * <h2>Introspection and intercession</h2>
 * <p>
 * 	Agent introspection is achieved with the following methods:
 * 	<ul>
 * 		<li>{@link ExtendedAgent#getPerceptionModel(LevelIdentifier)}</li>
 * 		<li>{@link ExtendedAgent#getMemoryRevisionModel()}</li>
 * 		<li>{@link ExtendedAgent#getDecisionModel(LevelIdentifier)}</li>
 * 	</ul>
 * <p>
 * 	Agent intercession is achieved with the following methods:
 * 	<ul>
 * 		<li>{@link ExtendedAgent#changePerceptionModel(LevelIdentifier, IAgtPerceptionModel)}</li>
 * 		<li>{@link ExtendedAgent#setMemoryRevisionModel(IAgtMemoryRevisionModel)}</li>
 * 		<li>{@link ExtendedAgent#changeDecisionModel(LevelIdentifier, IAgtDecisionModel)}</li>
 * 	</ul>
 * </p>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class ExtendedAgent extends AbstractAgent {
	/**
	 * The perception models used by the agent.
	 */
	private Map<LevelIdentifier, IAgtPerceptionModel> perceptionModels;
	/**
	 * The memory revision model used by the agent.
	 */
	private IAgtMemoryRevisionModel memoryRevisionModel;
	/**
	 * The decision models used by the agent.
	 */
	private Map<LevelIdentifier, IAgtDecisionModel> decisionModels;
	
	/**
	 * Creates a bare instance of an agent, using a specific category.
	 * The agent has then to be initialized by calls to the following methods:
	 * <ul>
	 * 	<li>{@link AbstractAgent#initializeGlobalMemoryState(IGlobalMemoryState)}</li>
	 * 	<li>{@link ExtendedAgent#setMemoryRevisionModel(IAgtMemoryRevisionModel)}</li>
	 * 	<li>{@link ExtendedAgent#includeNewLevel(LevelIdentifier, IPublicLocalStateOfAgent, IAgtPerceptionModel, IAgtDecisionModel)}</li>
	 * </ul>
	 * @param category The category of the agent.
	 * <p>
	 * 	This value can be the name of this class, or any other string representation modeling the equivalence 
	 * 	class of the agent.
	 * </p>
	 * <p>
	 * 	<b>Examples:</b>
	 * </p>
	 * <ul>
	 * 	<li>Car</li>
	 * 	<li>Prey</li>
	 * 	<li>Water drop</li>
	 * </ul>
	 * @throws IllegalArgumentException if the argument is <code>null</code>.
	 */
	public ExtendedAgent( String category ) {
		super( category );
		this.perceptionModels = new HashMap<LevelIdentifier, IAgtPerceptionModel>( );
		this.decisionModels = new HashMap<LevelIdentifier, IAgtDecisionModel>( );
	}

	/**
	 * This method is not suitable in this class. Use instead the
	 * {@link ExtendedAgent#includeNewLevel(LevelIdentifier, IPublicLocalStateOfAgent, IAgtPerceptionModel, IAgtDecisionModel)} method.
	 */
	public void includeNewLevel( 
			LevelIdentifier levelIdentifier, 
			IPublicLocalStateOfAgent publicLocalState
	){
		throw new UnsupportedOperationException( "Use the 'includeNewLevel(LevelIdentifier, IPublicLocalStateOfAgent, IAgtPerceptionModel, IAgtDecisionModel)' method instead." );
	}

	/**
	 * Include a new level in the specification of this agent.
	 * <p>
	 * 	If the agent already lies in the specified level when this method is called, then this method does nothing.
	 * </p>
	 * @param levelIdentifier The identifier of the specified level.
	 * @param publicLocalState The public local state of the agent in that level.
	 * @param perceptionModel The perception model used by this agent from the new level.
	 * @param decisionModel The decision model used by this agent from the new level.
	 */
	public void includeNewLevel( 
			LevelIdentifier levelIdentifier, 
			IPublicLocalStateOfAgent publicLocalState,
			IAgtPerceptionModel perceptionModel,
			IAgtDecisionModel decisionModel
	){
		this.checkPerceptionModelValidity( levelIdentifier, perceptionModel );
		this.checkDecisionModelValidity( levelIdentifier, decisionModel );
		super.includeNewLevel( levelIdentifier, publicLocalState );
		this.perceptionModels.put( levelIdentifier, perceptionModel );
		this.decisionModels.put( levelIdentifier, decisionModel );
	}
	
	/**
	 * Checks that the perception model is defined for the appropriate level.
	 * @param levelIdentifier The expected level of the perception model.
	 * @param perceptionModel The perception model.
	 * @throws IllegalArgumentException If the perception model is not defined for that level.
	 */
	private void checkPerceptionModelValidity( LevelIdentifier levelIdentifier, IAgtPerceptionModel perceptionModel ) {
		if( ! levelIdentifier.equals( perceptionModel.getLevel() ) ){
			throw new IllegalArgumentException( 
					"The level of a perception model is not appropriate for the agent '" + this.getCategory() + "'. " +
					"Expecting '" + levelIdentifier + "' but found '" + perceptionModel.getLevel() +"'" 
			);
		}
	}
	
	/**
	 * Checks that the decision model is defined for the appropriate level.
	 * @param levelIdentifier The expected level of the decision model.
	 * @param decisionModel The decision model.
	 * @throws IllegalArgumentException If the decision model is not defined for that level.
	 */
	private void checkDecisionModelValidity( LevelIdentifier levelIdentifier, IAgtDecisionModel decisionModel ) {
		if( ! levelIdentifier.equals( decisionModel.getLevel() ) ){
			throw new IllegalArgumentException( 
					"The level of a decision model is not appropriate for the agent '" + this.getCategory() + "'. " +
					"Expecting '" + levelIdentifier + "' but found '" + decisionModel.getLevel() +"'" 
			);
		}
	}

	/**
	 * Gets the memory revision model used by the agent.
	 * @return The memory revision model used by the agent.
	 * @throws NoSuchElementException If no memory revision model is defined for that agent.
	 */
	public IAgtMemoryRevisionModel getMemoryRevisionModel( ) {
		if( this.memoryRevisionModel == null ) {
			throw new NoSuchElementException( "No memory revision model is defined for the " +
					"agent '" + this.getCategory() + "'." );
		}
		return this.memoryRevisionModel;
	}

	/**
	 * Sets the memory revision model used by the agent.
	 * @param memoryRevisionModel The memory revision model used by the agent.
	 */
	public void setMemoryRevisionModel( IAgtMemoryRevisionModel memoryRevisionModel ) {
		if( memoryRevisionModel == null ){
			throw new IllegalArgumentException( "The argument cannot be null." );
		}
		this.memoryRevisionModel = memoryRevisionModel;
	}
	
	/**
	 * Gets the perception model used by the agent for a specific level.
	 * @param levelId The identifier of the level of the perception model.
	 * @return The perception model of the agent for the level <code>levelId</code>.
	 * @throws NoSuchElementException If no perception model is defined for that level.
	 */
	public IAgtPerceptionModel getPerceptionModel( LevelIdentifier levelId ) {
		IAgtPerceptionModel result = this.perceptionModels.get( levelId );
		if( result == null ){
			throw new NoSuchElementException( "No perception model is defined for the " +
					"level '" + levelId + "' for the agent " + this.getCategory() );
		}
		return result;
	}
	
	/**
	 * Changes the perception model of an agent for a new model.
	 * @param levelId The level for which the perception model is changed.
	 * @param newModel The new perception model for the <code>levelId</code> level.
	 * @throws IllegalArgumentException If the <code>newModel</code> argument is <code>null</code>, is incompatible 
	 * with the <code>levelId</code> level or if the agent does not reside in that level.
	 */
	public void changePerceptionModel( LevelIdentifier levelId, IAgtPerceptionModel newModel ) {
		if( ! this.getLevels().contains( levelId ) ) {
			throw new IllegalStateException( "The agent '" +  this + "' does not lie in the '" + levelId + "' level." );
		}
		this.checkPerceptionModelValidity( levelId, newModel );
		this.perceptionModels.put( levelId, newModel );
	}
	
	/**
	 * Gets the decision model used by the agent for a specific level.
	 * @param levelId The identifier of the level of the decision model.
	 * @return The decision model of the agent for the level <code>levelId</code>.
	 * @throws NoSuchElementException If no decision model is defined for that level.
	 */
	public IAgtDecisionModel getDecisionModel( LevelIdentifier levelId ) {
		IAgtDecisionModel result = this.decisionModels.get( levelId );
		if( result == null ){
			throw new NoSuchElementException( "No decision model is defined for the " +
					"level '" + levelId + "' for the agent " + this.getCategory() );
		}
		return result;
	}
	
	/**
	 * Changes the decision model of an agent for a new model.
	 * @param levelId The level for which the decision model is changed.
	 * @param newModel The new decision model for the <code>levelId</code> level.
	 * @throws IllegalArgumentException If the <code>newModel</code> argument is <code>null</code>, is incompatible 
	 * with the <code>levelId</code> level or if the agent does not reside in that level.
	 */
	public void changeDecisionModel( LevelIdentifier levelId, IAgtDecisionModel newModel ) {
		if( ! this.getLevels().contains( levelId ) ) {
			throw new IllegalStateException( "The agent '" +  this + "' does not lie in the '" + levelId + "' level." );
		}
		this.checkDecisionModelValidity( levelId, newModel );
		this.decisionModels.put( levelId, newModel );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPerceivedDataOfAgent perceive(
			LevelIdentifier level,
			IPublicLocalStateOfAgent publicLocalStateInLevel,
			IDynamicStateMap levelsPublicLocalObservableDynamicState
	) {
		IAgtPerceptionModel perceptionModel = this.perceptionModels.get( level );
		if( perceptionModel == null ){
			throw new IllegalStateException( "No perception model is defined for the level '" + level + "' for the agent   " + this.getCategory() );
		} else {
			return perceptionModel.perceive(
					publicLocalStateInLevel, 
					levelsPublicLocalObservableDynamicState
			);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reviseMemory(
			Map<LevelIdentifier, IPerceivedDataOfAgent> perceivedData,
			IGlobalMemoryState memoryState
	) {
		if( this.memoryRevisionModel != null ){
			this.memoryRevisionModel.reviseMemory( perceivedData, memoryState );
		} else {
			throw new IllegalStateException( "No memory revision model was defined for the agent '" + this.getCategory() + "'" );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decide(
			LevelIdentifier level, 
			IGlobalMemoryState memoryState,
			IPerceivedDataOfAgent perceivedData,
			InfluencesMap producedInfluences
	) {
		IAgtDecisionModel perceptionModel = this.decisionModels.get( level );
		if( perceptionModel == null ){
			throw new IllegalStateException( "No decision model is defined for the level '" + level + "'' for the agent  " + this.getCategory() );
		} else {
			perceptionModel.decide( memoryState, perceivedData, producedInfluences );
		}
	}

}
