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
package fr.lgi2a.similar.microkernel.examples.concepts.agents.fbi.physical;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationLevelIdentifiers;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.alien.physical.AgtAlienPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.citizen.physical.AgtCitizenPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.Cities;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.TimeOfTheDay;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.social.PostOnConspiracyForum;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractPerceivedDataOfAgent;

/**
 * Models the data being perceived by an 'FBI' agent when it is perceiving the simulation from the 'Physical' level.
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
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class AgtFBIPDFPhysical extends AbstractPerceivedDataOfAgent {
	/**
	 * The citizens considered as dangerous by the FBI.
	 */
	private Set<AgtCitizenPLSPhysical> dangerousCitizens;
	/**
	 * The posts on the Internet of the citizens considered as dangerous by the FBI.
	 */
	private Map<AgtCitizenPLSPhysical,Set<PostOnConspiracyForum>> postsOfDangerousCitizens;
	/**
	 * The city that was chosen by the FBI to search for aliens.
	 */
	private Cities cityWhereToSearchForAliens;
	/**
	 * The list of aliens currently present in the city where the FBI is searching.
	 */
	private Collection<AgtAlienPLSPhysical> alienPhysicalState;
	/**
	 * The current time of the day.
	 */
	private TimeOfTheDay currentTime;
	
	/**
	 * Builds the data that were perceived by the 'FBI' agent in preparation for
	 * its behavior from the 'Physical' level.
	 * @param currentTime The current time of the day.
	 */
	public AgtFBIPDFPhysical( 
			TimeOfTheDay currentTime
	) {
		// The super constructor requires the identifier of the level from which 
		// these data were perceived.
		super( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL );
		this.alienPhysicalState = new LinkedHashSet<AgtAlienPLSPhysical>();
		this.dangerousCitizens = new LinkedHashSet<AgtCitizenPLSPhysical>( );
		this.postsOfDangerousCitizens = new HashMap<AgtCitizenPLSPhysical,Set<PostOnConspiracyForum>>();
		this.currentTime = currentTime;
	}

	/**
	 * Gets the citizens considered as dangerous by the FBI.
	 * @return The citizens considered as dangerous by the FBI.
	 */
	public Set<AgtCitizenPLSPhysical> getDangerousCitizens( ) {
		return this.dangerousCitizens;
	}
	
	/**
	 * Adds a dangerous citizen to the perceived data.
	 * @param citizen The citizen to add.
	 * @param posts The posts on the Internet of the dangerous citizen.
	 */
	public void addDangerousCitizen( AgtCitizenPLSPhysical citizen, Set<PostOnConspiracyForum> posts ) {
		this.dangerousCitizens.add( citizen );
		Set<PostOnConspiracyForum> postsOfCitizen = new LinkedHashSet<PostOnConspiracyForum>( );
		postsOfCitizen.addAll( posts );
		this.postsOfDangerousCitizens.put( citizen, postsOfCitizen );
	}

	/**
	 * Gets the posts on the Internet of the citizens considered as dangerous by the FBI.
	 * @param citizen The citizen for which the posts are retrieved.
	 * @return The posts on the Internet of the citizens considered as dangerous by the FBI.
	 */
	public Set<PostOnConspiracyForum> getPostsOfDangerousCitizen( AgtCitizenPLSPhysical citizen ) {
		return this.postsOfDangerousCitizens.get( citizen );
	}

	/**
	 * Gets the city that was chosen by the FBI to search for aliens.
	 * @return The city that was chosen by the FBI to search for aliens.
	 */
	public Cities getCityWhereToSearchForAliens( ) {
		return this.cityWhereToSearchForAliens;
	}
	
	/**
	 * Gets the list of aliens currently present in the city where the FBI is searching.
	 * @return The list of aliens currently present in the city where the FBI is searching.
	 */
	public Collection<AgtAlienPLSPhysical> getAliensInSearchedCity( ) {
		return this.alienPhysicalState;
	}
	
	/**
	 * Adds an alien to the list of aliens located in the city where the FBI is searching.
	 * @param alien The alien to add.
	 */
	public void addAlienInSearchedCity( AgtAlienPLSPhysical alien ) {
		this.alienPhysicalState.add( alien );
	}
	
	/**
	 * Gets the current time of the day.
	 * @return The current time of the day.
	 */
	public TimeOfTheDay getCurrentTime( ) {
		return this.currentTime;
	}
}
