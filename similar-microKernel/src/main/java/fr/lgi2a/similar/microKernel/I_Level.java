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
package fr.lgi2a.similar.microKernel;

import java.util.Set;

import fr.lgi2a.similar.microKernel.states.I_PublicLocalDynamicState;
import fr.lgi2a.similar.microKernel.states.dynamicStates.Consistent_PublicLocalDynamicState;

/**
 * Models a level of the simulation.
 * 
 * <h1>Correspondence with theory</h1>
 * <p>
 * 	TODO formal notation
 * </p>
 * 
 * <h1>Usage</h1>
 * <p>
 * 	The reaction of a level is a process changing the value of the last consistent public local dynamic state of the level from 
 * 	a time stamp to the next time stamp of the level. This process is performed using the following steps:
 * </p>
 * <ol>
 * 	<li>Create a new consistent dynamic state of the level as a copy of the last consistent dynamic state.</li>
 * 	<li>Reset the state dynamics (remove all the influences it contains).</li>
 * 	<li>
 * 		The level asks the engine to perform the reaction to the system influences of contained in the 
 * 		state dynamics of the current transitory state
 *	</li>
 *	<li>
 *		The level then performs a user defined reaction to these system influences, for instance to update the public local state 
 *		of the environment when an influence added / removed the public local state of an agent into that level.
 *	</li>
 *	<li>
 *		If the engine or user reaction did generate new influences: 
 *		<ul>
 *			<li>The regular influences are added to the state dynamics of the current transitory state of the level</i>
 *			<li>The system influences are managed using the step 3 again</i>
 *			<li>
 *				If the influence is targeted at another level:
 *				<ul>
 *					<li>
 *						It is added to the state dynamics of the transitory state of the other level, if the transitory state of the 
 *						other level does not end at the same time than the transitory state of this level, <i>i.e.</i> if no reaction is
 *						currently being performed for that level.
 *					</li>
 *					<li>
 *						It is managed by the current reaction process of the other level otherwise.
 *					</li>
 *				</ul>
 *			</li>
 *		</ul>
 *	</li>
 *	<li>
 *		Then, the level performs a user defined reaction to the regular influences of the state dynamics of the current transitory 
 *		state of the level.
 *		The influences persisting after this reaction have to be added to the state dynamics of the new consistent public local dynamic 
 *		state of the level.
 *	</li>
 *	<li>
 *		If this user reaction did generate new influences:
 *		<ul>
 *			<li>The regular influences are added to the new consistent public local dynamic state of the level</i>
 *			<li>The system influences are managed using the following step</i>
 *			<li>
 *				If the influence is targeted at another level:
 *				<ul>
 *					<li>
 *						It is added to the state dynamics of the transitory state of the other level, if the transitory state of the 
 *						other level does not end at the same time than the transitory state of this level, <i>i.e.</i> if no reaction is
 *						currently being performed for that level.
 *					</li>
 *					<li>
 *						It is managed by the current reaction process of the other level otherwise.
 *					</li>
 *				</ul>
 *			</li>
 *		</ul>
 *	</li>
 * 	<li>
 * 		The level asks the engine to perform the reaction to the system influences that were added by the previous step.
 *	</li>
 *	<li>
 *		The level then performs a user defined reaction to these system influences, for instance to update the public local state 
 *		of the environment when an influence added / removed the public local state of an agent into that level.
 *	</li>
 *	<li>
 *		If the engine or user reaction did generate new influences: 
 *		<ul>
 *			<li>The regular influences are added to the new consistent public local dynamic state of the level</i>
 *			<li>The system influences are managed using the step 8 again</i>
 *			<li>
 *				If the influence is targeted at another level:
 *				<ul>
 *					<li>
 *						It is added to the state dynamics of the transitory state of the other level, if the transitory state of the 
 *						other level does not end at the same time than the transitory state of this level, <i>i.e.</i> if no reaction is
 *						currently being performed for that level.
 *					</li>
 *					<li>
 *						It is managed by the current reaction process of the other level otherwise.
 *					</li>
 *				</ul>
 *			</li>
 *		</ul>
 *	</li>
 * </ol>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public interface I_Level extends I_TimeModel {
	/**
	 * Gets the identifier of this level.
	 * <p>
	 * 	TODO formal notation
	 * </p>
	 * @return The identifier of this level.
	 */
	LevelIdentifier getIdentifier( );
	
	/**
	 * Gets the last consistent dynamic state of the level.
	 * <p>
	 * 	TODO formal notation
	 * </p>
	 * @return The last consistent dynamic state of the level.
	 */
	Consistent_PublicLocalDynamicState getLastConsistentPublicLocalDynamicState( );
	
	/**
	 * Sets the last consistent dynamic state of the level.
	 * <p>
	 * 	TODO formal notation
	 * </p>
	 * @param lastConsistentState The last consistent dynamic state of the level.
	 */
	void setLastConsistentPublicLocalDynamicState( Consistent_PublicLocalDynamicState lastConsistentState );

	/**
	 * Gets the levels that can be perceived by agents located in this level.
	 * <p>
	 * 	TODO formal notation
	 * </p>
	 * @return The levels that can be perceived by agents located in this level.
	 */
	Set<LevelIdentifier> getPerceptibleLevels( );

	/**
	 * Gets the levels that can be influenced by agents located in this level.
	 * <p>
	 * 	TODO formal notation
	 * </p>
	 * @return The levels that can be influenced by agents located in this level.
	 */
	Set<LevelIdentifier> getInfluenceableLevels( );
	
	/**
	 * Performs the reaction phase of the level using the current transitory state to build next consistent state
	 * of the simulation.
	 * <p>
	 * 	TODO formal notation
	 * </p>
	 * <h2>Usage</h2>
	 * <p>
	 * 	The <code>newConsistentState</code> argument of this method contains a copy of the last consistent state, 
	 * 	where the state dynamics are empty. The reaction has to add to this set all the influences that persist after 
	 * 	reaction. The other influences are consumed by the reaction.
	 * 	Note that this reaction can also produce other influences that might have to be handled either by the current reaction (for 
	 * 	instance the addition of new agents) or by the current/next reaction of other levels. Such influences have to be added to 
	 * 	the <code>newInfluencesToProcess</code> set.
	 * </p>
	 * <p>
	 * 	Note that for time and memory efficiency reasons, the public local state of the agents and of the environment of the new
	 * 	consistent state (<code>newConsistentState</code> argument) are references to the public local state of the agents and of 
	 * 	the environment of the last consistent state (<code>lastConsistentState</code>).
	 * </p>
	 * @param lastConsistentState The consistent state preceding the transitory phase ended by the reaction.
	 * @param newConsistentState The consistent state following the transitory phase ended by the reaction. This state is updated
	 * by the operations performed in this reaction.
	 * @param regularInfluencesOftransitoryStateDynamics The <b>regular</b> influences that have to be managed by this reaction to go from the 
	 * previous consistent state to the next consistent state of the level.
	 * @param newInfluencesToProcess The influences that were produced by this method and 
	 * that have to be handled either by the current reaction of the level (system influences) or handled in other levels 
	 * of the simulation. The regular influences added in the <code>newInfluencesToProcess</code> will be managed during the next reaction
	 * of this level.
	 */
	void makeRegularReaction(
			I_PublicLocalDynamicState lastConsistentState,
			Consistent_PublicLocalDynamicState newConsistentState,
			Set<I_Influence> regularInfluencesOftransitoryStateDynamics,
			Set<I_Influence> newInfluencesToProcess
	);
	
	/**
	 * Performs a reaction of the level to specific system influences.
	 * <p>
	 * 	TODO formal notation
	 * </p>
	 * <h2>Usage</h2>
	 * <p>
	 * 	This method is called whenever a system influence has to be managed either because it was present in the transitory 
	 * 	dynamic state, or because the reaction phase added new system influences that have to be managed.
	 * </p>
	 * <p>
	 * 	The system influences contained in the <code>systemInfluencesToManage</code> set were previously managed by the 
	 * 	simulation engine. Thus, this method only contains a level specific behavior (for instance updating the public local state of the
	 * 	environment in response to the apparition/disappearance of the public local state of an agent).
	 * </p>
	 * @param lastConsistentState The consistent state preceding the transitory phase ended by the reaction.
	 * @param newConsistentState The consistent state following the transitory phase ended by the reaction. This state is updated
	 * by the operations performed in this reaction.
	 * @param systemInfluencesToManage The system influences for which a special reaction might be necessary by the level.
	 * @param happensBeforeRegularReaction <code>true</code> if the reaction to the system influence happened before the call to
	 * the {@link I_Level#makeRegularReaction(I_PublicLocalDynamicState, Consistent_PublicLocalDynamicState, Set, Set)} method.
	 * @param newInfluencesToProcess The influences that were produced by this method and 
	 * that have to be handled either by the current reaction of the level (system influences) or handled in other levels 
	 * of the simulation. The regular influences that have to be managed by this level during the next reaction have to be added to the 
	 * state dynamics of the new consistent state. The regular influences that have to be managed by this level during the current reaction
	 * have to be added to this argument only if <code>happensBeforeRegularReaction</code> is <code>true</code>. Otherwise, the 
	 * influence is automatically added to the state dynamics of the new consistent state.
	 */
	void makeSystemReaction(
			I_PublicLocalDynamicState lastConsistentState,
			Consistent_PublicLocalDynamicState newConsistentState,
			Set<I_Influence> systemInfluencesToManage,
			boolean happensBeforeRegularReaction,
			Set<I_Influence> newInfluencesToProcess
	);
}
