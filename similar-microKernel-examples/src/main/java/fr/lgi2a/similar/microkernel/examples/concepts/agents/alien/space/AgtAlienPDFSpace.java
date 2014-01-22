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
package fr.lgi2a.similar.microkernel.examples.concepts.agents.alien.space;

import java.util.Set;

import fr.lgi2a.similar.microkernel.examples.concepts.agents.citizen.physical.AgtCitizenPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.Cities;
import fr.lgi2a.similar.microkernel.examples.concepts.level.ConceptsSimulationLevelIdentifiers;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractPerceivedDataOfAgent;

/**
 * Models the data being perceived by an 'Alien' agent when it is perceiving the simulation from the 'Space' level.
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
public class AgtAlienPDFSpace extends AbstractPerceivedDataOfAgent {	
	/**
	 * The location that was selected by the alien to perform the experiments.
	 */
	private Cities selectedLocation;
	/**
	 * The citizens that are lying at the specified location in the 'physical' level.
	 * These sad citizens are candidates for the experiments of the 'alien'.
	 */
	private Set<AgtCitizenPLSPhysical> candidatesForExperimentation;
	
	/**
	 * Builds the data that were perceived by the 'alien' agent in preparation for
	 * its behavior from the 'Space' level.
	 * This constructor defines a location and a selection of citizen that are candidate 
	 * for experimentation.
	 * @param selectedLocation The city where the experiments will be made.
	 * @param citizensAtSelectedLocation The citizens that are located at that position.
	 */
	public AgtAlienPDFSpace( 
			Cities selectedLocation, 
			Set<AgtCitizenPLSPhysical> citizensAtSelectedLocation 
	) {
		// The super constructor requires the identifier of the level from which 
		// these data were perceived.
		super( ConceptsSimulationLevelIdentifiers.SPACE_LEVEL );
		this.selectedLocation = selectedLocation;
		this.candidatesForExperimentation = citizensAtSelectedLocation;
	}
	
	/**
	 * Builds the data that were perceived by the 'alien' agent if it found no candidate for
	 * experimentation.
	 */
	public AgtAlienPDFSpace( ) {
		// The super constructor requires the identifier of the level from which 
		// these data were perceived.
		super( ConceptsSimulationLevelIdentifiers.SPACE_LEVEL );
		this.selectedLocation = null;
		this.candidatesForExperimentation = null;
	}
	
	/**
	 * Tells if the alien found candidates for experimentations in the 'physical' level of the simulation.
	 * @return <code>true</code> if the alien found candidates for experimentations in the 'physical' level of the simulation.
	 */
	public boolean hasFoundCandidatesForExperimentation( ) {
		return this.selectedLocation != null;
	}

	/**
	 * Gets the location that was selected by the alien to perform the experiments.
	 * @return The location that was selected by the alien to perform the experiments.
	 * <code>null</code> if no candidate for experimentation was found.
	 */
	public Cities getSelectedLocation( ) {
		return this.selectedLocation;
	}
	
	/**
	 * Gets the citizens that are lying at the specified location in the 'physical' level.
	 * These sad citizens are candidates for the experiments of the 'alien'.
	 * @return The citizens that are lying at the specified location in the 'physical' level.
	 * <code>null</code> if no candidate for experimentation was found.
	 */
	public Set<AgtCitizenPLSPhysical> getCandidatesForExperimentation( ) {
		return this.candidatesForExperimentation;
	}
}
