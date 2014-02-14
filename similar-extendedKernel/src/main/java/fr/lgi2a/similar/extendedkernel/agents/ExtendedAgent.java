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
package fr.lgi2a.similar.extendedkernel.agents;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import fr.lgi2a.similar.microkernel.AgentCategory;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.libs.abstractimpl.AbstractAgent;

/**
 * Models an agent in the extended kernel.
 * <p>
 * 	On the opposite of the micro-kernel, where agent classes had to be created, this 
 * 	class is self-sufficient. The operational code of the perception, global state revision and 
 * 	decision processes is defined in separate classes. The agent class only stores a reference to these 
 * 	classes.
 * </p>
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
 * 	The specification of such agents requires four operations:
 * </p>
 * <ul>
 * 	<li>
 * 		The definition of the initial global state of the agent, using the 
 * 		{@link AbstractAgent#initializeGlobalState(IGlobalState)} method.
 * 	</li>
 * 	<li>
 * 		The specification of the global state revision model of the agent, using the 
 * 		{@link ExtendedAgent#specifyGlobalStateRevisionModel(IAgtGlobalStateRevisionModel)} method.
 * 	</li>
 * 	<li>
 * 		The specification of the levels where the agent initially lies, using the 
 * 		{@link ExtendedAgent#includeNewLevel(LevelIdentifier, ILocalStateOfAgent, ILocalStateOfAgent)} method.
 * 	</li>
 * 	<li>
 * 		The specification of the behavior of the agent in each level where <strong>it can lie</strong>, using the 
 * 		{@link ExtendedAgent#specifyBehaviorForLevel(LevelIdentifier, IAgtPerceptionModel, IAgtDecisionModel)} method.
 * 	</li>
 * </ul>
 * <h2>Introspection and intercession</h2>
 * <p>
 * 	Agent introspection is achieved with the following methods:
 * 	<ul>
 * 		<li>{@link ExtendedAgent#getPerceptionModel(LevelIdentifier)}</li>
 * 		<li>{@link ExtendedAgent#getGlobalStateRevisionModel()}</li>
 * 		<li>{@link ExtendedAgent#getDecisionModel(LevelIdentifier)}</li>
 * 	</ul>
 * <p>
 * 	Agent intercession is achieved with the following methods:
 * 	<ul>
 * 		<li>{@link ExtendedAgent#specifyBehaviorForLevel(LevelIdentifier, IAgtPerceptionModel, IAgtDecisionModel)}</li>
 * 		<li>{@link ExtendedAgent#specifyGlobalStateRevisionModel(IAgtGlobalStateRevisionModel)}</li>
 * 	</ul>
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class ExtendedAgent extends AbstractAgent {
	/**
	 * The perception models used by the agent.
	 */
	private Map<LevelIdentifier, IAgtPerceptionModel> perceptionModels;
	/**
	 * The global state revision model used by the agent.
	 */
	private IAgtGlobalStateRevisionModel globalStateRevisionModel;
	/**
	 * The decision models used by the agent.
	 */
	private Map<LevelIdentifier, IAgtDecisionModel> decisionModels;

	/**
	 * Creates a bare instance of an agent, using a specific category.
	 * 
	 * The agent has then to be initialized by calls to the following methods:
	 * <ul>
	 * 	<li>
	 * 		The definition of the initial global state of the agent, using the 
	 * 		{@link AbstractAgent#initializeGlobalState(IGlobalState)} method.
	 * 	</li>
	 * 	<li>
	 * 		The specification of the global state revision model of the agent, using the 
	 * 		{@link ExtendedAgent#specifyGlobalStateRevisionModel(IAgtGlobalStateRevisionModel)} method.
	 * 	</li>
	 * 	<li>
	 * 		The specification of the levels where the agent initially lies, using the 
	 * 		{@link ExtendedAgent#includeNewLevel(LevelIdentifier, ILocalStateOfAgent, ILocalStateOfAgent)} method.
	 * 	</li>
	 * 	<li>
	 * 		The specification of the behavior of the agent in each level where <strong>it can lie</strong>, using the 
	 * 		{@link ExtendedAgent#specifyBehaviorForLevel(LevelIdentifier, IAgtPerceptionModel, IAgtDecisionModel)} method.
	 * 	</li>
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
	public ExtendedAgent(
		AgentCategory category
	) {
		super(category);
		this.perceptionModels = new HashMap<LevelIdentifier, IAgtPerceptionModel>( );
		this.decisionModels = new HashMap<LevelIdentifier, IAgtDecisionModel>( );
	}

	//
	//
	//
	// Extended agent related methods
	//
	//
	//
	
	/**
	 * Gets the revision model of the global state of the agent.
	 * @return The revision model of the global state of the agent.
	 * @throws NoSuchElementException If no global state revision model is defined for that agent.
	 */
	public IAgtGlobalStateRevisionModel getGlobalStateRevisionModel( ){
		if( this.globalStateRevisionModel == null ){
			throw new NoSuchElementException(
				"The global state revision model of the agent '" + this + 
					"' is not specified."
			);
		}
		return this.globalStateRevisionModel;
	}
	
	/**
	 * Defines the revision model of the global state of the agent.
	 * @param revisionMdl The revision model of the global state of the agent.
	 * @throws IllegalArgumentException If the argument is <code>null</code>.
	 */
	public void specifyGlobalStateRevisionModel(
		IAgtGlobalStateRevisionModel revisionMdl
	){
		if( revisionMdl == null ){
			throw new IllegalArgumentException(
				"The argument cannot be null."
			);
		} else {
			this.globalStateRevisionModel = revisionMdl;
		}
	}
	
	/**
	 * Gets the perception model of the agent from that level.
	 * @param levelId The identifier of the level.
	 * @return The perception model of the agent from that level.
	 * @throws NoSuchElementException If no perception model is defined for that 
	 * agent for the level identified by <code>levelId</code>.
	 */
	public IAgtPerceptionModel getPerceptionModel( 
		LevelIdentifier levelId
	){
		IAgtPerceptionModel model = this.perceptionModels.get( levelId );
		if( model == null ){
			throw new NoSuchElementException(
				"The perception model of the agent '" + this + 
				"' is not specified for the level '" + levelId + "'."
			);
		} else {
			return model;
		}
	}
	
	/**
	 * Gets the decision model of the agent from that level.
	 * @param levelId The identifier of the level.
	 * @return The decision model of the agent from that level.
	 * @throws NoSuchElementException If no decision model is defined for that 
	 * agent for the level identified by <code>levelId</code>.
	 */
	public IAgtDecisionModel getDecisionModel( 
		LevelIdentifier levelId
	){
		IAgtDecisionModel model = this.decisionModels.get( levelId );
		if( model == null ){
			throw new NoSuchElementException(
				"The decision model of the agent '" + this + 
				"' is not specified for the level '" + levelId + "'."
			);
		} else {
			return model;
		}
	}

	/**
	 * Defines the behavior of an agent from a specific level of the simulation.
	 * <p>
	 * 	Note that this behavior will be used only if the agent is added to that level using the
	 *  appropriate system influences (or if the agent initially lies in that level).
	 * </p>
	 * @param levelId The identifier of the level for which a behavior is specified.
	 * @param perceptionMdl The perception model of the agent from that level.
	 * @param decisionMdl The decision model of the agent from that level.
	 * @throws IllegalArgumentException If an argument is <code>null</code> or has an 
	 * inappropriate value.
	 */
	public void specifyBehaviorForLevel(
		LevelIdentifier levelId,
		IAgtPerceptionModel perceptionMdl,
		IAgtDecisionModel decisionMdl
	){
		if( levelId == null || perceptionMdl == null | decisionMdl == null ){
			throw new IllegalArgumentException(
				"The arguments cannot be null."
			);
		} else if( ! perceptionMdl.getLevel().equals( levelId ) ){
			throw new IllegalArgumentException(
				"The level of the perception model '" + perceptionMdl.getLevel() + "' has " +
				"to match the value of the argument 'levelId'."
			);
			} else if( ! decisionMdl.getLevel().equals( levelId ) ){
				throw new IllegalArgumentException(
					"The level of the decision model '" + decisionMdl.getLevel() + "' has " +
					"to match the value of the argument 'levelId'."
				);
			}
		this.perceptionModels.put( levelId, perceptionMdl );
		this.decisionModels.put( levelId, decisionMdl );
	}
	
	/**
	 * Removes the behavior of the agent for the specified level.
	 * @param levelId The identifier of the level for which the behavior of the agent is removed.
	 */
	public void removeBehaviorForLevel(
		LevelIdentifier levelId
	){
		this.perceptionModels.remove( levelId );
		this.decisionModels.remove( levelId );
	}
	
	//
	//
	//
	// Micro-kernel agent related methods
	//
	//
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPerceivedData perceive(
		LevelIdentifier level,
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound,
		Map<LevelIdentifier, ILocalStateOfAgent> publicLocalStates,
		ILocalStateOfAgent privateLocalState,
		IPublicDynamicStateMap dynamicStates
	) {
		return this.getPerceptionModel( level ).perceive(
			timeLowerBound, 
			timeUpperBound, 
			publicLocalStates, 
			privateLocalState, 
			dynamicStates
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reviseGlobalState(
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound,
		Map<LevelIdentifier, IPerceivedData> perceivedData,
		IGlobalState globalState
	) {
		this.getGlobalStateRevisionModel().reviseGlobalState(
			timeLowerBound, 
			timeUpperBound, 
			perceivedData, 
			globalState
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decide(
		LevelIdentifier levelId,
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound, 
		IGlobalState globalState,
		ILocalStateOfAgent publicLocalState, 
		ILocalStateOfAgent privateLocalState, 
		IPerceivedData perceivedData,
		InfluencesMap producedInfluences
	) {
		this.getDecisionModel( levelId ).decide(
			timeLowerBound, 
			timeUpperBound, 
			globalState, 
			publicLocalState,
			privateLocalState, 
			perceivedData, 
			producedInfluences
		);
	}
}
