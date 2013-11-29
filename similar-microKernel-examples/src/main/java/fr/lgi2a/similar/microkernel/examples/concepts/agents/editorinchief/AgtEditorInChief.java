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
package fr.lgi2a.similar.microkernel.examples.concepts.agents.editorinchief;

import java.util.Map;

import fr.lgi2a.similar.microkernel.IAgent;
import fr.lgi2a.similar.microkernel.IDynamicStateMap;
import fr.lgi2a.similar.microkernel.IGlobalMemoryState;
import fr.lgi2a.similar.microkernel.IPerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.IPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.InfluencesMap;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationLevelIdentifiers;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.editorinchief.social.AgtEditorInChiefPDFSocial;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.editorinchief.social.AgtEditorInChiefPLSSocial;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.Cities;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.social.EnvPLSSocial;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.tosocial.RISocialChangeBroadcast;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractAgent;

/**
 * Models instances of 'Editor in chief' agents.
 * 
 * <h1>Naming convention</h1>
 * In the name of the class:
 * <ul>
 * 	<li>"Agt" stands for "Agent"</li>
 * </ul>
 * These rules were defined to reduce the size of the name of the class.
 * 
 * <h1>Agents in the SIMILAR API suite.</h1>
 * <p>
 * 	In the micro-kernel of SIMILAR, an agent is implemented as an 
 * 	instance of either the {@link IAgent} interface, or of the {@link AbstractAgent} 
 * 	abstract class.
 * 	In this example, we use the abstract class which is easier to implement.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class AgtEditorInChief extends AbstractAgent {
	/**
	 * The category of this agent class (telling that the agent is an 'Editor in chief' agent).
	 * <p>
	 * 	This category is defined as a static value to facilitate the identification of the nature of the
	 * 	agents for instance when data about the agent are printed on screen.
	 * </p>
	 */
	public static final String CATEGORY = "Editor in chief";
	
	/**
	 * Builds an 'Editor in chief' agent having initially no public local states or global memory state.
	 * @param address The city where the editor in chief lives.
	 * @param thresholdForStrangePhysicalManifestationsAdvisedByFBI The number of strange physical manifestation over which a 
	 * citizen can consider that an alien experiment was performed on him/her. This value is being broadcasted on television 
	 * and corresponds to the value advised by the FBI.
	 * @param paranoiaThreshold The number of abduction being reported in the city of the editor in chief before it becomes paranoid and 
	 * broadcasts values that are not advised by the FBI.
	 */
	public AgtEditorInChief( 
			Cities address,
			int thresholdForStrangePhysicalManifestationsAdvisedByFBI,
			long paranoiaThreshold
	) {
		// The super constructor requires the definition of the category of the agent.
		super( CATEGORY );
		//
		// Define the initial private local states of the agent.
		//
		this.paranoiaThreshold = paranoiaThreshold;
		this.thresholdForStrangePhysicalManifestationsAdvisedByFBI = thresholdForStrangePhysicalManifestationsAdvisedByFBI;
	}

	// // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
	//
	//
	//	PERCEPTION RELATED METHODS
	//
	//
	//
	
	/**
	 * This method defines the data being perceived by the agent, depending on the level from which perception is made.
	 * @param levelId The identifier of the level from which the perception is made.
	 * @param publicLocalStateInLevel The public local state of this agent in the level which identifier is <code>levelId</code>.
	 * @param levelsPublicLocalObservableDynamicState The public dynamic state of the levels that can be perceived from 
	 * the level having the identifier <code>levelId</code>.
	 */
	@Override
	public IPerceivedDataOfAgent perceive(
			LevelIdentifier levelId,
			IPublicLocalStateOfAgent publicLocalStateInLevel,
			IDynamicStateMap levelsPublicLocalObservableDynamicState
	) {
		if( ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL.equals( levelId ) ) {
			// Case where the perception is made from the 'Social' level.
			//
			// Get the public local state of the environment in the social level.
			IPublicLocalDynamicState dynamicStateOfLevel = levelsPublicLocalObservableDynamicState.get( ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL );
			EnvPLSSocial envState = (EnvPLSSocial) dynamicStateOfLevel.getPublicLocalStateOfEnvironment();
			// Get the public local state of the editor in chief
			AgtEditorInChiefPLSSocial castedEditorState = (AgtEditorInChiefPLSSocial) publicLocalStateInLevel;
			// Then build the perceived data using the current data of the environment.
			long totalNumberOfExperiments = 0;
			for( Cities city : Cities.values() ){
				totalNumberOfExperiments += envState.getPostsFor( city ).size();
			}
			AgtEditorInChiefPDFSocial perceivedData = new AgtEditorInChiefPDFSocial(
					totalNumberOfExperiments, 
					envState.getPostsFor( castedEditorState.getAddress() ).size( )
			);
			return perceivedData;
		} else {
			// This case is out of the bounds of the behavior of the agent.
			// Consequently, we throw an exception telling that this case should not happen
			// in an appropriate simulation.
			throw new UnsupportedOperationException( "Cannot perceive from the level '" + levelId + "'." );
		}
	}

	// // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
	//
	//
	//	MEMORY REVISION RELATED METHODS
	//
	//
	//

	/**
	 * Defines how the agent uses the data that were perceived to revise the content of its
	 * global memory state.
	 * <p>
	 * 	Since this agent does not use the global memory state, this method does nothing.
	 * </p>
	 */
	@Override
	public void reviseMemory(
			Map<LevelIdentifier, IPerceivedDataOfAgent> perceivedData,
			IGlobalMemoryState memoryState
	) { }


	// // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
	//
	//
	//	DECISION RELATED METHODS
	//
	//
	//
	
	/**
	 * The number of strange physical manifestation over which a citizen can consider that an alien experiment 
	 * was performed on him/her. This value is being broadcasted on television and corresponds to the value 
	 * advised by the FBI.
	 * <p>
	 * 	This value is part of the private local state of the agent. Since it purely belongs to the inner working of the behavior of the 
	 * 	agent from the 'social' level, it is declared here rather than in the public local state or the global memory state of the agent.
	 * </p>
	 */
	private int thresholdForStrangePhysicalManifestationsAdvisedByFBI;
	/**
	 * The number of abduction being reported in the city of the editor in chief before it becomes paranoid and 
	 * broadcasts values that are not advised by the FBI.
	 * <p>
	 * 	This value is part of the private local state of the agent. Since it purely belongs to the inner working of the behavior of the 
	 * 	agent from the 'social' level, it is declared here rather than in the public local state or the global memory state of the agent.
	 * </p>
	 */
	private long paranoiaThreshold;
	
	/**
	 * This method defines the influences that are produced by the decisions made by this agent, when it performs a decision
	 * from a specific level.
	 * @param levelId The identifier of the level from which a decision is made.
	 * @param memoryState The global memory state of the agent when the decision is made.
	 * @param perceivedData The data that were lastly perceived from the <code>levelId</code> by the agent.
	 * @param producedInfluences The map where the influences produced by the decisions of this agent are put.
	 */
	@Override
	public void decide(
			LevelIdentifier levelId, 
			IGlobalMemoryState memoryState,
			IPerceivedDataOfAgent perceivedData,
			InfluencesMap producedInfluences
	) {
		if( ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL.equals( levelId ) ) {
			// Case where the decision is made from the 'Social' level.
			//
			// Convert the perceived data into the appropriate class.
			AgtEditorInChiefPDFSocial castedPerceivedData = (AgtEditorInChiefPDFSocial) perceivedData;
			if( castedPerceivedData.getNumberOfExperimentsInCityOfEditorInChief() > this.paranoiaThreshold ){
				// The editor in chief is paranoid and changes the broadcasted threshold for strange physical manifestations.
				// It reduces the threshold by an amount equal to the total number of abduction cases divided by the number of cities plus
				// the threshold (arbitrary computation).
				int broadCastedValue = (int) Math.max(
						1, 
						this.thresholdForStrangePhysicalManifestationsAdvisedByFBI - castedPerceivedData.getTotalNumberOfExperiments() / ( 
								castedPerceivedData.getNumberOfExperimentsInCityOfEditorInChief() + this.paranoiaThreshold
						)
				);
				// Create the influence that will change the broadcasted value.
				RISocialChangeBroadcast influence = new RISocialChangeBroadcast( broadCastedValue, this.getCategory() );
				producedInfluences.add( influence );
			} else {
				// The editor in chief acts normally and broadcasts the value advised by the FBI.
				// Create the influence that will change the broadcasted value.
				RISocialChangeBroadcast influence = new RISocialChangeBroadcast( 
						this.thresholdForStrangePhysicalManifestationsAdvisedByFBI, 
						this.getCategory() 
				);
				producedInfluences.add( influence );
			}
		} else {
			// This case is out of the bounds of the behavior of the agent.
			// Consequently, we throw an exception telling that this case should not happen
			// in an appropriate simulation.
			throw new UnsupportedOperationException( "Cannot decide from the level '" + levelId + "'." );
		}
	}
}
