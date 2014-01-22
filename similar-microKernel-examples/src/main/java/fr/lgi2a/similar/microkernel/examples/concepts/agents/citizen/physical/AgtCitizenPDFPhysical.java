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
package fr.lgi2a.similar.microkernel.examples.concepts.agents.citizen.physical;

import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.TimeOfTheDay;
import fr.lgi2a.similar.microkernel.examples.concepts.level.ConceptsSimulationLevelIdentifiers;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractPerceivedDataOfAgent;

/**
 * Models the data being perceived by a 'Citizen' agent when it is perceiving the simulation from the 'Physical' level.
 * 
 * <h1>Naming convention</h1>
 * In the name of the class:
 * <ul>
 * 	<li>"Agt" stands for "Agent"</li>
 * 	<li>"PDF" stands for "Perceived Data From the level"</li>
 * </ul>
 * These rules were defined to reduce the size of the name of the class.
 * 
 * <h1>Perceived data in the SIMILAR API suite.</h1>
 * <p>
 * 	In the micro-kernel of SIMILAR, the data perceived by agent are implemented as an 
 * 	instance of either the {@link fr.lgi2a.similar.microkernel.IPerceivedDataOfAgent} interface or the {@link AbstractPerceivedDataOfAgent} 
 * 	abstract class.
 * 	In this example, we use the abstract class which is easier to implement.
 * </p>
 * 
 * <h1>Best practice</h1>
 * <p>
 * 	It is always possible to include a reference to the public local dynamic state of the perceptible levels
 * 	in the perceived data. Yet, this approach is not advised since it hides the data that are really necessary to build
 * 	the behavior of the agent. Instead, we uphold that this class has to contain as little data as possible. Moreover,
 * 	it can contain interpretations of the data contained in the perceptible levels rather than data directly extracted from 
 * 	that level.
 * 	For instance, if the behavior of the agent changes only depending on the number of other agents in a specific level, the 
 * 	perceived data should only contain the number of agents, and not a reference on the collection of agents lying in that level.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class AgtCitizenPDFPhysical extends AbstractPerceivedDataOfAgent {
	/**
	 * The number of strange physical manifestation over which a citizen can consider that an alien experiment 
	 * was performed on him/her.
	 */
	private int strangePhysicalManifestationsThreshold;
	/**
	 * The current number of strange physical manifestations of the agent.
	 */
	private int currentStrangePhysicalManifestations;
	/**
	 * The time of the day when the perception was made.
	 */
	private TimeOfTheDay currentTimeOfTheDay;

	/**
	 * Builds the data that were perceived by the 'Citizen' agent in preparation for
	 * its behavior from the 'Physical' level.
	 */
	public AgtCitizenPDFPhysical(
			int strangePhysicalManifestationsThreshold,
			int currentStrangePhysicalManifestations,
			TimeOfTheDay currentTimeOfTheDay
	) {
		super( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL );
		this.strangePhysicalManifestationsThreshold = strangePhysicalManifestationsThreshold;
		this.currentStrangePhysicalManifestations = currentStrangePhysicalManifestations;
		this.currentTimeOfTheDay = currentTimeOfTheDay;
	}

	/**
	 * Gets the number of strange physical manifestation over which a citizen can consider that an alien experiment 
	 * was performed on him/her.
	 * @return The number of strange physical manifestation over which a citizen can consider that an alien experiment 
	 * was performed on him/her.
	 */
	public int getStrangePhysicalManifestationsThreshold( ) {
		return this.strangePhysicalManifestationsThreshold;
	}
	
	/**
	 * Gets the current number of strange physical manifestations of the agent.
	 * @return The current number of strange physical manifestations of the agent.
	 */
	public int getCurrentStrangePhysicalManifestations( ) {
		return this.currentStrangePhysicalManifestations;
	}
	
	/**
	 * Gets the time of the day when the perception was made.
	 * @return The time of the day when the perception was made.
	 */
	public TimeOfTheDay getCurrentTimeOfTheDay( ) {
		return this.currentTimeOfTheDay;
	}
}
