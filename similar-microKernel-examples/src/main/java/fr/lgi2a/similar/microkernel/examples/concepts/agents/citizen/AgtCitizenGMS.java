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
package fr.lgi2a.similar.microkernel.examples.concepts.agents.citizen;

import fr.lgi2a.similar.microkernel.IAgent;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractGlobalMemoryState;

/**
 * Models the global memory state of a 'Citizen' agent.
 * 
 * <h1>Naming convention</h1>
 * In the name of the class:
 * <ul>
 * 	<li>"Agt" stands for "Agent"</li>
 * 	<li>"GMS" stands for "Global Memory State"</li>
 * </ul>
 * These rules were defined to reduce the size of the name of the class.
 * 
 * <h1>Global memory states in the SIMILAR API suite.</h1>
 * <p>
 * 	In the micro-kernel of SIMILAR, the global memory state of an agent is implemented as an 
 * 	instance of either the {@link fr.lgi2a.similar.microkernel.IGlobalMemoryState} interface, or of the {@link AbstractGlobalMemoryState} 
 * 	abstract class.
 * 	In this example, we use the abstract class which is easier to implement.
 * </p>
 * 
 * <h1>Best practices</h1>
 * <p>
 * 	The identification of the data that should be in the global memory state and the data that
 * 	should be in the private local state of the agent is a hard task. Indeed, the simple rule 'if the 
 * 	data is used in more than one level and cannot be perceived, then the data belongs to the global memory state' 
 * 	is not appropriate in the general case:
 * </p>
 * <ul>
 * 	<li>
 * 		The same data can be used in the bahavior of the agent in different levels using different forms. 
 * 		For instance a the total number of agents (for a level), 
 * 		and the number of agents per category (for the other). In this case, it becomes difficult to tell if the number of agents per category should be
 * 		placed in a global dynamic state and if the total number of agents should be computed using the memorized data, or if the data should
 * 		appear in the private local state of the different levels of the agent.
 * 	</li>
 * </ul>
 * <p>
 * 	In the end, it is up to the user to determine the information to include in the global dynamic state. 
 * 	The global memory state can include beliefs as well as plans, or only beliefs or cross level plans, etc.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class AgtCitizenGMS extends AbstractGlobalMemoryState {
	/**
	 * Models the paranoia level of the agent, on a scale going from 0 (not paranoid) to a positive value (the higher 
	 * it is, the more paranoid the agent is).
	 */
	private int paranoiaLevel;
	
	/**
	 * Builds a global memory state where the paranoia level is set to 0.
	 * @param owner The agent owning this global memory state.
	 */
	public AgtCitizenGMS(IAgent owner) {
		super(owner);
		// Sets the initial paranoia level to 0.
		this.paranoiaLevel = 0;
	}

	/**
	 * Gets the paranoia level of the agent, on a scale going from 0 (not paranoid) to a positive value (the higher 
	 * it is, the more paranoid the agent is).
	 * @return The paranoia level of the agent, on a scale going from 0 (not paranoid) to a positive value (the higher 
	 * it is, the more paranoid the agent is).
	 */
	public int getParanoiaLevel( ) {
		return this.paranoiaLevel;
	}

	/**
	 * Sets the paranoia level of the agent, on a scale going from 0 (not paranoid) to a positive value (the higher 
	 * it is, the more paranoid the agent is).
	 * @param paranoiaLevel The paranoia level of the agent, on a scale going from 0 (not paranoid) to a positive value (the higher 
	 * it is, the more paranoid the agent is).
	 */
	public void setParanoiaLevel( int paranoiaLevel ) {
		this.paranoiaLevel = paranoiaLevel;
	}
}
