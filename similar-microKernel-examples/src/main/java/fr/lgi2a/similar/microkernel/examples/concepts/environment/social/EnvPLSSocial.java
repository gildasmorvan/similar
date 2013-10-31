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
package fr.lgi2a.similar.microkernel.examples.concepts.environment.social;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IPublicLocalState;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationLevelIdentifiers;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationParameters;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.Cities;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractPublicLocalState;

/**
 * Models the public local state of the environment for the 'social' level.
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
 * 	This public local state models the media (the Internet and a TV program).
 * 	It contains two types of information:
 * </p>
 * <ul>
 * 	<li>
 * 		An TV program dedicated to UFO investigation. This program broadcasts the number of 
 * 		strange physical manifestation over which a citizen can consider that an alien experiment 
 * 		was performed on him/her.
 * 	</li>
 * 	<li>
 * 		A blog where citizen can tell that they were abducted by aliens. This blog contains a post (an article) 
 * 		for each report made by a citizen.
 * 	</li>
 * </ul>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class EnvPLSSocial extends AbstractPublicLocalState {
	/**
	 * The number of strange physical manifestation over which a citizen can consider that an alien experiment 
	 * was performed on him/her. This value is being broadcasted on television.
	 */
	private int tvBroadcastedThresholdForStrangePhysicalManifestations;
	/**
	 * The reports written by citizen on the Internet stating that they were guinea pigs.
	 * The posts are ordered by cities.
	 */
	private Map<Cities, Set<PostOnConspiracyForum>> postsPerCities;
	
	/**
	 * Builds an instance of the public local state of the environment in the 'social' level.
	 * This instance initially contains no samples.
	 */
	public EnvPLSSocial( ) throws IllegalArgumentException {
		// The super constructor requires the identifier of the level for which this public
		// local state is defined.
		super( ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL );
		// Set the initial value as the one advised by the FBI.
		this.tvBroadcastedThresholdForStrangePhysicalManifestations = 
				ConceptsSimulationParameters.THRESHOLD_FOR_STRANGE_PHYSICAL_MANIFESTATION_ADVISED_BY_FBI;
		// Set the initial value of the reports written on the Internet.
		this.postsPerCities = new HashMap<Cities, Set<PostOnConspiracyForum>>( );
		for( Cities city : Cities.values() ){
			this.postsPerCities.put( city, new LinkedHashSet<PostOnConspiracyForum>() );
		}
	}
	
	/**
	 * Gets the number of strange physical manifestation over which a citizen can consider that an alien experiment 
	 * was performed on him/her. This value is being broadcasted on television.
	 * @return The number of strange physical manifestation over which a citizen can consider that an alien experiment 
	 * was performed on him/her
	 */
	public int getTvBroadcastedThresholdForStrangePhysicalManifestations( ) {
		return this.tvBroadcastedThresholdForStrangePhysicalManifestations;
	}

	/**
	 * Sets the number of strange physical manifestation over which a citizen can consider that an alien experiment was 
	 * performed on him/her. This value is being broadcasted on television.
	 * @param tvBroadcastedThresholdForStrangePhysicalManifestations The number of strange physical manifestation over which a 
	 * citizen can consider that an alien experiment was performed on him/her.
	 */
	public void setTvBroadcastedThresholdForStrangePhysicalManifestations( 
			int tvBroadcastedThresholdForStrangePhysicalManifestations 
	) {
		this.tvBroadcastedThresholdForStrangePhysicalManifestations = tvBroadcastedThresholdForStrangePhysicalManifestations;
	}
	

	/**
	 * Gets the reports written by citizen on the Internet stating that they were guinea pigs in a specific city.
	 * @param city The city where the citizen were guinea pigs.
	 * @return The list of posts on the Internet stating that a citizen was a guinea pig.
	 */
	public Set<PostOnConspiracyForum> getPostsFor( Cities city ) {
		return this.postsPerCities.get( city );
	}
	
	/**
	 * Removes all the posts that were made on the Internet to report alien experiments.
	 */
	public void removeAllPostsFromTheInternet( ) {
		for( Cities city : Cities.values() ){
			this.postsPerCities.get( city ).clear();
		}
	}
}
