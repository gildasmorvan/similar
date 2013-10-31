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
package fr.lgi2a.similar.microkernel.examples.concepts.environment.physical;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IPublicLocalState;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationLevelIdentifiers;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.alien.physical.AgtAlienPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.citizen.physical.AgtCitizenPLSPhysical;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractPublicLocalState;

/**
 * Models the public local state of the environment for the 'physical' level.
 * 
 * <h1>Naming convention</h1>
 * In the name of the class:
 * <ul>
 * 	<li>"Env" stands for "Environment"</li>
 * 	<li>"PLS" stands for "Public Local State"</li>
 * </ul>
 * These rules were defined to reduce the size of the name of the class.
 * 
 * <h1>Public local state of the environment in the SIMILAR API suite.</h1>
 * <p>
 * 	In the micro-kernel of SIMILAR, the public local state of the environment is implemented as an 
 * 	instance of either the {@link IPublicLocalState} interface, or of the {@link AbstractPublicLocalState} 
 * 	abstract class.
 * 	In this example, we use the abstract class which is easier to implement.
 * </p>
 * 
 * <h1>Usage</h1>
 * <p>
 * 	This public local state stores the association between a location (city) and the agents lying at that position.
 * 	This facilitates the perception process of all the agents.
 * </p>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class EnvPLSPhysical extends AbstractPublicLocalState {
	/**
	 * The data structure summarizing the location of all the citizen of the 'physical' level.
	 */
	private Map<Cities,Set<AgtCitizenPLSPhysical>> citizenLocation;
	/**
	 * The data structure summarizing the location of all the aliens of the 'physical' level.
	 */
	private Map<Cities,Set<AgtAlienPLSPhysical>> alienLocation;
	/**
	 * Models the current time of the day in the 'physical' level.
	 */
	private TimeOfTheDay currentTimeOfTheDay;
	
	/**
	 * Builds an instance of the public local state of the environment in the 'physical' level.
	 * This instance initially contains no citizens. Citizens have to be added using the appropriate 
	 * method of this class.
	 * @param initialTime The initial time of the day of this simulation.
	 */
	public EnvPLSPhysical( TimeOfTheDay initialTime ) throws IllegalArgumentException {
		// The super constructor requires the identifier of the level for which this public
		// local state is defined.
		super( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL );
		// Initialize the time of the day.
		this.currentTimeOfTheDay = initialTime;
		// Create an initially empty citizen and alien location map.
		this.citizenLocation = new HashMap<Cities,Set<AgtCitizenPLSPhysical>>( );
		this.alienLocation = new HashMap<Cities,Set<AgtAlienPLSPhysical>>();
		// Include an empty set for each possible city of the simulation.
		for( Cities city : Cities.values() ) {
			// The sets used in this map are instances of the linked hash set class to
			// favor simulation reproducibility (since the iteration order over hash sets is 
			// not "deterministic").
			Set<AgtCitizenPLSPhysical> citizensAtLocation = new LinkedHashSet<AgtCitizenPLSPhysical>();
			this.citizenLocation.put( city, citizensAtLocation );
			Set<AgtAlienPLSPhysical> aliensAtLocation = new LinkedHashSet<AgtAlienPLSPhysical>();
			this.alienLocation.put( city, aliensAtLocation );
		}
	}
	
	/**
	 * Adds a citizen at a specific location of the public local state of the environment in the 'physical' level.
	 * <p>
	 * 	Note that a call to this method does not change the 'location' field of the public local state of the 'citizen'.
	 * 	This operation has also to be done manually.
	 * </p>
	 * @param citizenPLS The public local state of the 'citizen' agent to add to this level.
	 * @param location The location where the citizen is placed in the public local state of the environment.
	 */
	public void addCitizen( AgtCitizenPLSPhysical citizenPLS, Cities location ) {
		this.citizenLocation.get( location ).add( citizenPLS );
	}
	
	/**
	 * Gets the set of citizens located in a specific city.
	 * @param location The location where to search for citizen.
	 * @return The set of citizens located in a specific city.
	 */
	public Set<AgtCitizenPLSPhysical> getCitizensAt( Cities location ) {
		return this.citizenLocation.get( location );
	}
	
	/**
	 * Adds an alien at a specific location of the public local state of the environment in the 'physical' level.
	 * <p>
	 * 	Note that a call to this method does not change the 'location' field of the public local state of the 'alien'.
	 * 	This operation has also to be done manually.
	 * </p>
	 * @param alienPLS The public local state of the 'alien' agent to add to this level.
	 * @param location The location where the alien is placed in the public local state of the environment.
	 */
	public void addAlien( AgtAlienPLSPhysical alienPLS, Cities location ) {
		this.alienLocation.get( location ).add( alienPLS );
	}
	
	/**
	 * Gets the set of aliens located in a specific city.
	 * @param location The location where to search for aliens.
	 * @return The set of aliens located in a specific city.
	 */
	public Set<AgtAlienPLSPhysical> getAliensAt( Cities location ) {
		return this.alienLocation.get( location );
	}
	
	/**
	 * Removes an alien from the public local state of the environment in the 'physical' level.
	 * @param toRemove The public local state of the alien to remove from the public 
	 * local state of the environment in the 'physical' level.
	 */
	public void removeAlien( AgtAlienPLSPhysical toRemove ) {
		this.alienLocation.remove( toRemove );
	}

	/**
	 * Gets the current time of the day in the 'physical' level.
	 * @return The current time of the day in the 'physical' level.
	 */
	public TimeOfTheDay getCurrentTimeOfTheDay( ) {
		return this.currentTimeOfTheDay;
	}

	/**
	 * Sets the current time of the day in the 'physical' level.
	 * @param The new current time of the day in the 'physical' level.
	 */
	public void setCurrentTimeOfTheDay( TimeOfTheDay currentTimeOfTheDay ){
		this.currentTimeOfTheDay = currentTimeOfTheDay;
	}
}
