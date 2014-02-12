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

import fr.lgi2a.similar.microkernel.LevelIdentifier;

/**
 * Models an agent contained in the simulation, which specifications include engine optimization related methods.
 * 
 * <h1>Usage</h1>
 * <p>
 * 	This is the pragmatic interface of an agent instance, including 
 * 	implementation constraints.
 * 	In practice, the methods defined in this interface are used by the simulation engine. Therefore, 
 * 	simulation designers will only manipulate methods from the {@link IAgent} parent 
 * 	interface.
 * </p>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @see IAgent
 */
public interface IAgent4Engine extends IAgent {
	/**
	 * Include a new level in the specification of this agent.
	 * <p>
	 * 	If the agent already lies in the specified level when this method is called, then this method does nothing.
	 * </p>
	 * @param levelIdentifier The identifier of the specified level.
	 * @param publicLocalState The public local state of the agent in that level.
	 * @param privateLocalState The private local state of the agent in that level.
	 */
	public void includeNewLevel( 
			LevelIdentifier levelIdentifier, 
			ILocalStateOfAgent publicLocalState,
			ILocalStateOfAgent privateLocalState 
	);
	
	/**
	 * Removes a level from the specification of this agent.
	 * <h2>Usage and safety</h2>
	 * <p>
	 * 	A call to this method is safe as long as:
	 * </p>
	 * <ul>
	 * 	<li>
	 * 		It is made in the context of the reaction to the system influence removing the agent from
	 * 		the level identified by <code>levelIdentifier</code> (simulation engine).
	 *	</li>
	 * </ul>
	 * @param levelIdentifier The identifier of the level from which the agent has to be removed.
	 */
	void excludeFromLevel( 
			LevelIdentifier levelIdentifier 
	);
	
	/**
	 * Gets the data that were lastly perceived by the agent, for all the levels where it lies.
	 * <p>
	 * 	This method returns a map containing the identifier of a level "l" as a key, and
	 * 	the element p<sub>a</sub>(]t<sub>l</sub>,t<sub>l</sub>+dt<sub>l</sub>[, l) as a value, where
	 * 	]t<sub>l</sub>,t<sub>l</sub>+dt<sub>l</sub>[ is the most recent transitory period of the
	 * 	level "l" during the call of this method.
	 * </p>
	 * @return The data that were lastly perceived by the agent, for all the levels where it lies.
	 */
	Map<LevelIdentifier, IPerceivedData> getPerceivedData( );
	
	/**
	 * Sets the data that were lastly perceived by the agent from a specific level where it lies.
	 * <p>
	 * 	This method defines the value of an item of the map containing the identifier of a level "l" as a key, and
	 * 	the element p<sub>a</sub>(]t<sub>l</sub>,t<sub>l</sub>+dt<sub>l</sub>[, l) as a value, where
	 * 	]t<sub>l</sub>,t<sub>l</sub>+dt<sub>l</sub>[ is the most recent transitory period of the
	 * 	level "l" during the call of this method.
	 * </p>
	 * @param perceivedData The data that were lastly perceived by the agent from the <code>levelIdentifier</code> level.
	 */
	void setPerceivedData( 
			IPerceivedData perceivedData 
	);
	
	/**
	 * Gets the public local states of the agent.
	 * <p>
	 * 	This method returns all the values &phi;<sub>a</sub><sup>+</sup>(t, l) or 
	 * 	&phi;<sub>a</sub><sup>+</sup>(]t,t&prime;[, l)
	 * 	&isin;&Phi;<sub>a</sub><sup>+</sup>, where the value of ]t,t&prime;[ is
	 * 	defined by the context in which this method is called.
	 * </p>
	 * <h2>Usage and safety</h2>
	 * <p>
	 * 	This method has to be used only in the simulation engine.
	 * </p>
	 * @return The public local states of the agent.
	 */
	Map<LevelIdentifier, ILocalStateOfAgent> getPublicLocalStates( );
}
