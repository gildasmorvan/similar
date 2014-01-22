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

import fr.lgi2a.similar.microkernel.examples.concepts.agents.citizen.physical.AgtCitizenPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.Cities;
import fr.lgi2a.similar.microkernel.examples.concepts.level.ConceptsSimulationLevelIdentifiers;
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
 * 	instance of either the {@link fr.lgi2a.similar.microkernel.IPublicLocalState} interface, or of the {@link AbstractPublicLocalState} 
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
	 * The reports written by citizen on the Internet stating that they were guinea pigs.
	 * The posts are ordered by citizen.
	 */
	private Map<AgtCitizenPLSPhysical, Set<PostOnConspiracyForum>> postsPerCitizen;
	
	/**
	 * Builds an instance of the public local state of the environment in the 'social' level.
	 * This instance initially contains no samples.
	 * @param tvBroadcastedThresholdForStrangePhysicalManifestations The initial number of strange physical manifestation over which a citizen can consider 
	 * that an alien experiment was performed on him/her. This value is being broadcasted on television.
	 */
	public EnvPLSSocial( int tvBroadcastedThresholdForStrangePhysicalManifestations ) {
		// The super constructor requires the identifier of the level for which this public
		// local state is defined.
		super( ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL );
		//
		// Initialize the public local state of the environment.
		//
		this.tvBroadcastedThresholdForStrangePhysicalManifestations = tvBroadcastedThresholdForStrangePhysicalManifestations;
		// Set the initial value of the reports written on the Internet.
		this.postsPerCities = new HashMap<Cities, Set<PostOnConspiracyForum>>( );
		for( Cities city : Cities.values() ){
			this.postsPerCities.put( city, new LinkedHashSet<PostOnConspiracyForum>() );
		}
		this.postsPerCitizen = new HashMap<AgtCitizenPLSPhysical, Set<PostOnConspiracyForum>>( );
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
	 * Adds a post to the Internet.
	 * @param post The post to add.
	 */
	public void addPost( PostOnConspiracyForum post ){
		this.postsPerCities.get( post.getCity() ).add( post );
		Set<PostOnConspiracyForum> posts  = this.postsPerCitizen.get( post.getAuthor() );
		if( posts == null ){
			posts = new LinkedHashSet<PostOnConspiracyForum>( );
			this.postsPerCitizen.put( post.getAuthor(), posts );
		}
		posts.add( post );
	}
	
	/**
	 * Gets the reports written by a citizen on the Internet stating that it was a guinea pigs.
	 * @param citizen The citizen which posts are listed.
	 * @return The list of posts on the Internet stating that a citizen was a guinea pig.
	 * Can be <code>null</code>.
	 */
	public Set<PostOnConspiracyForum> getPostsFor( AgtCitizenPLSPhysical citizen ) {
		return this.postsPerCitizen.get( citizen );
	}
	
	/**
	 * Removes all the posts of a citizen that were made on the Internet to report alien experiments.
	 * @param citizen The citizen which posts are removed.
	 */
	public void removeAllPostsOfCitizen( AgtCitizenPLSPhysical citizen ) {
		Set<PostOnConspiracyForum> posts = this.postsPerCitizen.get( citizen );
		if( posts != null ){
			for( PostOnConspiracyForum post : posts ){
				this.postsPerCities.get( post.getCity() ).remove( post );
			}
			posts.clear( );
		}
	}
	
	/**
	 * Removes all the posts that were made on the Internet to report alien experiments.
	 */
	public void removeAllPostsFromTheInternet( ) {
		this.postsPerCitizen.clear();
		for( Cities city : Cities.values() ){
			this.postsPerCities.get( city ).clear();
		}
	}
}
