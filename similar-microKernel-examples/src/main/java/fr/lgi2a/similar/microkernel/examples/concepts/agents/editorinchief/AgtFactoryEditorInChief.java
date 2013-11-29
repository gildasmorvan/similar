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

import fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationLevelIdentifiers;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationParameters;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.editorinchief.social.AgtEditorInChiefPLSSocial;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.Cities;
import fr.lgi2a.similar.microkernel.libs.generic.EmptyGlobalMemoryState;

/**
 * A factory method generating 'Editor in chief' agents. Agents are generated using both explicit parameters and default parameters.
 * 
 * <h1>Best practices</h1>
 * <p>
 * 	The generation of agents during simulation implies the specification of the parameters 
 * 	used for their initialization. Yet, giving an explicit value inside the behavior of the agent creating the 
 * 	new agent might not be relevant (for instance, there is no reason that the 'FBI' agent defines explicitly the 'paranoiaThreshold'
 *  private local state of the 'Editor in chief' agent it is creating). To avoid these irrelevances, these parameters can be defined either in 
 *  a default constructor (dirty yet easy method) or with a factory (clean but more complex approach).
 * </p>
 * <p>
 * 	This class illustrates the use of a factory to create agents having some default parameters and some explicit parameters.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class AgtFactoryEditorInChief {
	/**
	 * The instance of this singleton class. Use this field to call the generation methods of this factory.
	 */
	private static final AgtFactoryEditorInChief INSTANCE = new AgtFactoryEditorInChief( );
	
	/**
	 * Gets the instance of this singleton class. Use this field to call the generation methods of this factory.
	 * @return The instance of this singleton class. Use this field to call the generation methods of this factory.
	 */
	public static AgtFactoryEditorInChief instance( ) {
		return INSTANCE;
	}
	
	/**
	 * The parameters currently used to generate 'editor in chief' agents with this factory.
	 */
	private ConceptsSimulationParameters parameters;
	
	/**
	 * Sets the parameters currently used to provide default values for some private local states of the 'editor in chief' agents with this factory.
	 * @param parameters The parameters currently used to generate 'editor in chief' agents with this factory.
	 */
	public void setParameters( ConceptsSimulationParameters parameters ) {
		this.parameters = parameters;
	}
	
	/**
	 * Generates an editor in chief agent living in a specific city and broadcasting a specific threshold for strange physical manifestations.
	 * @param address The address of the editor in chief agent.
	 * @param thresholdForStrangePhysicalManifestationsAdvisedByFBI The thresold for strange physical manifestations (expressed 
	 * in a number of manifestations to reach to believe that experiments were made on a citizen).
	 * @return The new and initialized editor in chief agent.
	 */
	public AgtEditorInChief generateAgent( 
			Cities address,
			int thresholdForStrangePhysicalManifestationsAdvisedByFBI
	) {
		if( this.parameters == null ) {
			throw new IllegalStateException( "The generation parameters have to be set before creating agents!" );
		} else {
			AgtEditorInChief result = new AgtEditorInChief(
					thresholdForStrangePhysicalManifestationsAdvisedByFBI, 
					this.parameters.getEditorInChiefParanoiaThreshold()
			);
			//
			// Define the initial global memory state of the agent.
			//
			// No specific data are required in that state for the 'Editor in chief' agent. Thus, instead of defining a new class,
			// we use the generic class EmptyGlobalMemoryState defined in the common libs of SIMILAR.
			// This class models an empty global memory state for an agent.
			result.initializeGlobalMemoryState( new EmptyGlobalMemoryState( result ) );
			//
			// Tell that this agent is initially in the 'social' level.
			//
			IPublicLocalStateOfAgent stateInSocialLevel = new AgtEditorInChiefPLSSocial( result, address );
			// Specify that the agent lies is initially present in the 'social' level.
			result.includeNewLevel( ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL, stateInSocialLevel );
			return result;
		}
	}
}
