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
package fr.lgi2a.similar.extendedkernel.levels;

import java.util.Collection;
import java.util.Set;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.influences.IInfluence;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;

/**
 * Models the reaction process used by a level to react to the influences it received.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public interface ILevelReactionModel {	
	/**
	 * Performs a user-defined reaction to the regular influences that were lying in the most recent consistent dynamic state of 
	 * the level and to the influences that were added into the transitory dynamic state of the level.
	 * 
	 * <h2>Usage</h2>
	 * <p>
	 * 	<b>This method has to:</b>
	 * </p>
	 * <ul>
	 * 	<li>
	 * 		Update the local state of the environment and of the agents contained in the consistent dynamic state
	 *  	<code>consistentState</code> argument of this method according to the modifications depicted by the regular influences.
	 *  </li>
	 *  <li>
	 *  	Add to the <code>newInfluencesToProcess</code> set the influences persisting after the reaction.
	 *  	The influences that were consumed by the reaction are not added to this set.
	 *  </li>
	 *  <li>
	 *  	Add to the <code>newInfluencesToProcess</code> set the new influences that were produced by 
	 *  	this reaction (for instance the addition of an agent to the simulation).
	 *  	The system reactions will be processed after this user reaction to regular influences if the level they are aimed at
	 *  	is a level currently computing its reaction. Otherwise, the system influence is managed during the next reaction of the 
	 *  	targeted level.
	 *  	Moreover, the regular influences added in such a way will be processed during the next reaction of the level 
	 *  	they are aimed at.
	 *  </li>
	 * </ul>
	 * <p>
	 * 	<b>This method cannot:</b>
	 * </p>
	 * <ul>
	 * 	<li>
	 * 		Add influences directly into the consistent state of the simulation. If it does so, it puts the simulation into an 
	 * 		inconsistent state.
	 * 	</li>
	 * </ul>
	 * @param transitoryTimeMin The lower bound of the transitory period of the level for which this reaction is performed.
	 * @param transitoryTimeMax The upper bound of the transitory period of the level for which this reaction is performed.
	 * @param consistentState The consistent state being updated by this user reaction.
	 * @param regularInfluencesOftransitoryStateDynamics The <b>regular</b> influences that have to be managed by this reaction to go from the 
	 * previous consistent state to the next consistent state of the level.
	 * @param remainingInfluences The data structure that will contain the influences that were produced by the user during the invocation of 
	 * this method, or the influences that persist after this reaction.
	 */
	void makeRegularReaction(
			SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			ConsistentPublicLocalDynamicState consistentState,
			Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
			InfluencesMap remainingInfluences
	);
	
	/**
	 * Performs a user-defined reaction to the system influences that are lying in the most recent consistent dynamic state of 
	 * the level and to the influences that were added into the transitory dynamic state of the level.
	 * This method is called twice during the reaction phase of the level:
	 * <ul>
	 * 	<li>
	 * 		A first time right after the system reaction to the system influences happening at the beginning of the reaction phase.
	 * 		It gives the opportunity to perform a user-defined reaction to the system influences that were added to the transitory
	 * 		dynamic state of the level during the transitory period (for instance to add an agent to the topology of the environment).
	 * 	</li>
	 * 	<li>
	 * 		A second time right after the system reaction to the system influences happening after the user-defined reaction to the
	 * 		regular influences.
	 * 		It gives the opportunity to perform a user-defined reaction to the system influences that were created by the user during the
	 * 		user reaction to the regular influences.
	 * 	</li>
	 * </ul>
	 * <h2>Usage</h2>
	 * <p>
	 * 	<b>This method can:</b>
	 * </p>
	 * <ul>
	 * 	<li>
	 * 		Update the local state of the environment and of the agents contained in the consistent dynamic state
	 *  	<code>consistentState</code> argument of this method according  to the modifications depicted by the system influences.
	 *  	For instance, if the public local state of the environment defines an agent grid, this method can add the public local state 
	 *  	of an agent to the grid, in reaction to the 'add agent to level' system influence.
	 *  </li>
	 *  <li>
	 *  	Add to the <code>newInfluencesToProcess</code> set the new influences that were produced by 
	 *  	this reaction (for instance the addition of a new agent to the simulation).
	 *  	The influences added in such a way will be processed either during the next reaction of the level 
	 *  	they are aimed at (<code>happensBeforeRegularReaction</code> <code>false</code> or the level being targeted
	 *  	by the influence is not currently computing a reaction).
	 *  </li>
	 * </ul>
	 * <p>
	 * 	<b>This method cannot:</b>
	 * </p>
	 * <ul>
	 * 	<li>
	 * 		Add influences directly into the consistent state of the simulation. If it does so, it puts the simulation into an 
	 * 		inconsistent state.
	 * 	</li>
	 * </ul>
	 * @param transitoryTimeMin The lower bound of the transitory period of the level for which this reaction is performed.
	 * @param transitoryTimeMax The lower bound of the transitory period of the level for which this reaction is performed.
	 * @param consistentState The consistent state being updated by this user reaction. Note that this state already include the result
	 * of the system influences listed in the <code>systemInfluencesToManage</code> argument.
	 * @param systemInfluencesToManage The <b>system</b> influences that have to be managed by this reaction to go from the 
	 * previous consistent state to the next consistent state of the level.
	 * @param happensBeforeRegularReaction <code>true</code> if this user-defined system reaction is performed before the call to the
	 * {@link fr.lgi2a.similar.microkernel.levels.ILevel#makeRegularReaction(SimulationTimeStamp, SimulationTimeStamp, ConsistentPublicLocalDynamicState, Set, InfluencesMap)} method.
	 * @param newInfluencesToProcess The data structure where the influences resulting from this user reaction have to be added.
	 */
	void makeSystemReaction(
			SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			ConsistentPublicLocalDynamicState consistentState,
			Collection<IInfluence> systemInfluencesToManage,
			boolean happensBeforeRegularReaction,
			InfluencesMap newInfluencesToProcess
	);
}
