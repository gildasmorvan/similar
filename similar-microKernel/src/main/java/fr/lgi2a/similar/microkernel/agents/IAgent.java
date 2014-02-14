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
package fr.lgi2a.similar.microkernel.agents;

import java.util.Map;
import java.util.Set;

import fr.lgi2a.similar.microkernel.AgentCategory;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;

/**
 * Models an agent contained in the simulation.
 * <p>
 * 	An agent is characterized by a category, telling which kind of agent it models.
 * </p>
 * 
 * <h1>Correspondence with theory</h1>
 * <p>
 * 	An instance of this class models an agent a&isin;&#x1D538; from the simulation.
 * </p>
 * 
 * <h1>Usage</h1>
 * <p>
 * 	This is the ideal interface of an agent, from the perspective of a simulation designer.
 * 	Because of implementation constraints, agents 
 * 	have instead to implement the {@link IAgent4Engine} interface.
 * </p>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @see AgentCategory
 */
public interface IAgent {
	/**
	 * Gets the 'category' of agents to which this instance belongs to.
	 * <p>
	 * 	This method can return the name of this class, or any other string representation modeling the equivalence 
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
	 * @return The 'category' of the agent.
	 */
	AgentCategory getCategory( );
	
	/**
	 * Gets the collection of the levels where this agent lies, <i>i.e.</i> the levels where this agent 
	 * has a local state.
	 * <p>
	 * 	This method returns the levels in which the agent is defined, <i>i.e.</i> a list containing the identifier of 
	 * 	each level l&isin;&#x1D543; such that &phi;<sub>a</sub>(]t,t&prime;[, l) exists. The value of ]t,t&prime;[ is
	 * 	defined by the context in which this method is called.
	 * </p>
	 * <h2>Usage and safety</h2>
	 * <p>
	 * 	This method has to be used only in the simulation engine.
	 * </p>
	 * <p>
     * 	This method has to ensure that two consecutive iterations over this set always return its items in
     * 	the same order.
	 * </p>
	 * @return A collection containing the identifier of the levels where this agent lies.
	 */
	Set<LevelIdentifier> getLevels( );
	
	/**
	 * Gets the global state of the agent.
	 * <p>
	 * 	This method returns the value &mu;<sub>a</sub>(t) or &mu;<sub>a</sub>(]t,t&prime;[)&isin;&#x1D544;<sub>a</sub>, where 
	 * the value of ]t,t&prime;[ is defined by the context in which this method is called.
	 * </p>
	 * <h2>Usage and safety</h2>
	 * <p>
	 * 	This method has to be used only in the simulation engine.
	 * </p>
	 * @return The global state of the agent.
	 */
	IGlobalState getGlobalState( );
	
	/**
	 * Gets the public local state of the agent located in a specific level of the simulation.
	 * <p>
	 * 	This method returns the value &phi;<sub>a</sub><sup>+</sup>(t, <code>levelIdentifier</code>) or 
	 * 	&phi;<sub>a</sub><sup>+</sup>(]t,t&prime;[, <code>levelIdentifier</code>)
	 * 	&isin;&Phi;<sub>a</sub><sup>+</sup>, where the value of ]t,t&prime;[ is
	 * 	defined by the context in which this method is called.
	 * </p>
	 * <h2>Usage and safety</h2>
	 * <p>
	 * 	This method has to be used only in the simulation engine.
	 * </p>
	 * @param levelIdentifier The identifier of the level of the public local state;
	 * @return The public local state of the agent in the specified level.
	 * @throws java.util.NoSuchElementException If the agent does not define a public local state for the provided level identifier.
	 */
	ILocalStateOfAgent getPublicLocalState( 
			LevelIdentifier levelIdentifier 
	);
	
	/**
	 * Gets the private local state of the agent located in a specific level of the simulation.
	 * <p>
	 * 	This method returns the value &phi;<sub>a</sub><sup>-</sup>(t, <code>levelIdentifier</code>) or 
	 * 	&phi;<sub>a</sub><sup>-</sup>(]t,t&prime;[, <code>levelIdentifier</code>)
	 * 	&isin;&Phi;<sub>a</sub><sup>-</sup>, where the value of ]t,t&prime;[ is
	 * 	defined by the context in which this method is called.
	 * </p>
	 * <h2>Usage and safety</h2>
	 * <p>
	 * 	This method has to be used only in the simulation engine or in the reaction of the level identified by
	 * 	<code>levelIdentifier</code>.
	 * </p>
	 * @param levelIdentifier The identifier of the level of the private local state;
	 * @return The private local state of the agent in the specified level.
	 * @throws java.util.NoSuchElementException If the agent does not define a private local state for the provided level identifier.
	 */
	ILocalStateOfAgent getPrivateLocalState( 
			LevelIdentifier levelIdentifier 
	);
	
	/**
	 * Creates the data perceived by an agent located in a specific level.
	 * <p>
	 * 	This method corresponds to the application perception<sub>a, ]t,t+dt<sub>l</sub>[, l</sub> of this agent.
	 * </p>
	 * @param level The level from which perception is made (<i>i.e.</i> "l" in the notations).
	 * @param timeLowerBound Is the lower bound of the transitory period of the level identified by <code>level</code> from which
	 * the perception is made by this agent (<i>i.e.</i> "t" in the notations).
	 * @param timeUpperBound Is the upper bound of the transitory period of the level identified by <code>level</code> from which
	 * the perception is made by this agent (<i>i.e.</i> t+dt<sub>l</sub> in the notations).
	 * @param publicLocalStates All the public local states of the agent.
	 * @param privateLocalState The private local state of the agent in the level from which perception is made (<i>i.e.</i> 
	 * &phi;<sub>a</sub><sup>-</sup>( t, <code>level</code> ) in the notations).
	 * @param dynamicStates The dynamic state of the various levels that can be perceived from the 
	 * level <code>level</code>. This value has previously been disambiguated by a heuristic defined in the simulation engine.
	 * @return The data being perceived by the agent from the level identified by <code>level</code>, for the transitory period 
	 * ]<code>timeLowerBound</code>, <code>timeUpperBound</code>[.
	 */
	IPerceivedData perceive( 
			LevelIdentifier level,
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound,
			Map<LevelIdentifier, ILocalStateOfAgent> publicLocalStates,
			ILocalStateOfAgent privateLocalState,
			IPublicDynamicStateMap dynamicStates
	);
	
	/**
	 * Revises the content of the global state of the agent, using the previous value of its global state and
	 * the data that were lastly perceived by the agent.
	 * <p>
	 * 	This method corresponds to the application memoryRev<sub>a, ]t,t+dt[</sub> of this agent.
	 * </p>
	 * <p>
	 * 	As a side effect of this method call, the value &mu;<sub>a</sub>( t ) of the argument <code>globalState</code> is updated 
	 * 	to become &mu;<sub>a</sub>(]t,t+dt[)
	 * </p>
	 * @param timeLowerBound Is the lower bound of the transitory period for which the global state revision is made by this 
	 * agent (<i>i.e.</i> "t" in the notations).
	 * @param timeUpperBound Is the upper bound of the transitory period for which the global state revision is made by this 
	 * agent (<i>i.e.</i> "t+dt" in the notations).
	 * @param perceivedData The map containing the data that were lastly perceived from the various levels of the simulation.
	 * @param globalState The previous value of the global state of the agent being updated by this method call.
	 * @param perceivedData The map containing the data that were lastly perceived from the various levels of the simulation.
	 */
	void reviseGlobalState(
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound,
			Map<LevelIdentifier, IPerceivedData> perceivedData, 
			IGlobalState globalState 
	);
	
	/**
	 * Produces the influences resulting from the decisions of an agent from a specific level, for a specific transitory period.
	 * <p>
	 * 	This method corresponds to the application decision<sub>a, ]t,t+dt<sub>l</sub>[,l</sub> of this agent.
	 * </p>
	 * @param levelId The identifier of the level from which the decision is made (<i>i.e.</i> "l" in the notations).
	 * @param timeLowerBound Is the lower bound of the transitory period for which the decision is made by this 
	 * agent (<i>i.e.</i> "t" in the notations).
	 * @param timeUpperBound Is the upper bound of the transitory period for which the decision is made by this 
	 * agent (<i>i.e.</i> "t+dt" in the notations).
	 * @param globalState The revised global state of the agent when it made a decision (<i>i.e.</i> &mu;<sub>a</sub>(t+dt) in the 
	 * notations).
	 * @param publicLocalState  The public local state of the agent in the level from which decision is made (<i>i.e.</i> 
	 * &phi;<sub>a</sub><sup>+</sup>( t, <code>level</code> ) in the notations).
	 * @param privateLocalState The private local state of the agent in the level from which decision is made (<i>i.e.</i> 
	 * &phi;<sub>a</sub><sup>-</sup>( t, <code>level</code> ) in the notations).
	 * @param perceivedData The data that were perceived about the level identified by <code>levelId</code> and its perceptible levels.
	 * @param producedInfluences The map where the influences resulting from the decisions are stored.
	 */
	void decide(
			LevelIdentifier levelId,
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound,
			IGlobalState globalState,
			ILocalStateOfAgent publicLocalState,
			ILocalStateOfAgent privateLocalState,
			IPerceivedData perceivedData,
			InfluencesMap producedInfluences
	);
}
