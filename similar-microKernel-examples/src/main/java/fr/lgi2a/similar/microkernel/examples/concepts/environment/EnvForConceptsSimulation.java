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
package fr.lgi2a.similar.microkernel.examples.concepts.environment;

import fr.lgi2a.similar.microkernel.IDynamicStateMap;
import fr.lgi2a.similar.microkernel.InfluencesMap;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationLevelIdentifiers;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.EnvPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.TimeOfTheDay;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.social.EnvPLSSocial;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.space.EnvPLSSpace;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.tophysical.RIPhysicalSetTimeOfTheDay;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractEnvironment;

/**
 * Models the environment of this simulation.
 * 
 * <h1>Naming convention</h1>
 * In the name of the class:
 * <ul>
 * 	<li>"Env" stands for "Environment"</li>
 * </ul>
 * These rules were defined to reduce the size of the name of the class.
 * 
 * <h1>Environment in the SIMILAR API suite.</h1>
 * <p>
 * 	In the micro-kernel of SIMILAR, the environment is implemented as an 
 * 	instance of either the {@link fr.lgi2a.similar.microkernel.IEnvironment} interface, or of the {@link AbstractEnvironment} 
 * 	abstract class.
 * 	In this example, we use the abstract class which is easier to implement.
 * </p>
 * 
 * <h1>Usage</h1>
 * <p>
 * 	An environment has to define a public local state for each level of the simulation. In the {@link AbstractEnvironment} class, these 
 * 	level are specified using the {@link AbstractEnvironment#includeNewLevel(LevelIdentifier, fr.lgi2a.similar.microkernel.IPublicLocalState)} method.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class EnvForConceptsSimulation extends AbstractEnvironment {
	/**
	 * Builds an initialized instance of the environment, where a public local state is defined for the 'Physical', 'Social' and 'Space' levels.
	 * @param initialTime The time of the day of the initial time of the simulation.
	 * @param tvBroadcastedThresholdForStrangePhysicalManifestations The initial number of strange physical manifestation over which a citizen can consider 
	 * that an alien experiment was performed on him/her. This value is being broadcasted on television.
	 */
	public EnvForConceptsSimulation( 
			TimeOfTheDay initialTime,
			int tvBroadcastedThresholdForStrangePhysicalManifestations
		) {
		this.includeNewLevel( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL, new EnvPLSPhysical( initialTime ) );
		this.includeNewLevel( ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL, new EnvPLSSocial( tvBroadcastedThresholdForStrangePhysicalManifestations ) );
		this.includeNewLevel( ConceptsSimulationLevelIdentifiers.SPACE_LEVEL, new EnvPLSSpace( ) );
	}

	/**
	 * Models the natural action of the environment on the simulation, from a specific level.
	 * @param levelId The identifier of the level from which the natural action of the environment is made.
	 * @param levelsPublicLocalObservableDynamicState The public dynamic state of the levels that can be perceived from 
	 * the level having the identifier <code>levelId</code>.
	 * @param producedInfluences The map where the influences produced by the natural action of this environment are put.
	 */
	@Override
	public void natural(
			LevelIdentifier levelId,
			IDynamicStateMap levelsPublicLocalObservableDynamicState,
			InfluencesMap producedInfluences
	) {
		// Dispatch the method call depending on the level from which the natural action is made.
		if( ConceptsSimulationLevelIdentifiers.SPACE_LEVEL.equals( levelId ) ){
			// Case where the decision is made from the 'Space' level.
			// In this case, the environment does nothing.
		} else if( ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL.equals( levelId ) ){
			// Case where the decision is made from the 'Social' level.
			// In this case, the environment does nothing.
		} else if( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL.equals( levelId ) ) {
			// Case where the decision is made from the 'Physical' level.
			// Dispatch to the appropriate method.
			this.naturalFromPhysicalLevel( producedInfluences );
		} else {
			// This case is out of the bounds of the behavior of the agent.
			// Consequently, we throw an exception telling that this case should not happen
			// in an appropriate simulation.
			throw new UnsupportedOperationException( "Cannot decide from the level '" + levelId + "'." );
		}
	}
	
	/**
	 * Produce the influences resulting from the natural action of the environment from the 'physical' level.
	 * <p>
	 * 	From that level, the environment moves the day-evening-night cycle.
	 * </p>
	 * @param producedInfluences The map where the influences produced by the decisions of this agent are put.
	 */
	private void naturalFromPhysicalLevel(
			InfluencesMap producedInfluences
	){
		// Create the influence that will change the current time of the day of the environment to the 
		// appropriate value. The appropriate value is determined during the reaction.
		RIPhysicalSetTimeOfTheDay influence = new RIPhysicalSetTimeOfTheDay( );
		producedInfluences.add( influence );
	}
}
