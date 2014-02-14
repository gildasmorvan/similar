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
package fr.lgi2a.similar.extendedkernel.examples.bubblechamber.model.environment.chamber;

import java.awt.geom.Point2D;
import java.util.Map;

import fr.lgi2a.similar.extendedkernel.examples.bubblechamber.model.agents.BubbleChamberAgentCategoriesList;
import fr.lgi2a.similar.extendedkernel.examples.bubblechamber.model.agents.bubble.AgtBubbleFactory;
import fr.lgi2a.similar.extendedkernel.examples.bubblechamber.model.agents.particle.chamber.AgtParticlePLSInChamber;
import fr.lgi2a.similar.extendedkernel.examples.bubblechamber.model.influences.tochamber.RIUpdateParticlesSpatialStateInChamber;
import fr.lgi2a.similar.extendedkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractEnvNaturalModel;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;

/**
 * The model of the natural action of the environment from the "Chamber" level.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class EnvNaturalInChamber extends AbstractEnvNaturalModel {

	/**
	 * Creates a bare instance of a natural action model, using a specific level identifier.
	 * @param levelIdentifier The identifier of the level for which this natural action model is defined.
	 */
	public EnvNaturalInChamber(	) {
		super( BubbleChamberLevelList.CHAMBER );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void natural(
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound,
		Map<LevelIdentifier, ILocalStateOfEnvironment> publicLocalStates,
		ILocalStateOfEnvironment privateLocalState,
		IPublicDynamicStateMap dynamicStates,
		InfluencesMap producedInfluences
	) {
		// The natural action of the environment has two roles in this
		// simulation:
		// 		1) Ask the reaction to update the location and velocity of 
		// 		the particles according to their location, velocity and 
		//		acceleration.
		//		2) Ask the reaction to add a bubble agent where each particle
		//		lies.
		
		// First create the influence asking for the update.
		RIUpdateParticlesSpatialStateInChamber updateInfluence = new RIUpdateParticlesSpatialStateInChamber(
			timeLowerBound,
			timeUpperBound
		);
		// Add this influence to the produced influences (the particles are registered
		// to the influence later).
		producedInfluences.add( updateInfluence );
		// Then get the dynamic state of the "Chamber" level, to list the 
		// particles
		IPublicLocalDynamicState dynamicState = dynamicStates.get( 
			BubbleChamberLevelList.CHAMBER 
		);
		// Search for particles among the agents lying in that state.
		for( ILocalStateOfAgent state : dynamicState.getPublicLocalStateOfAgents() ){
			// Check if the agent is a particle.
			if( state.getCategoryOfAgent().isA( BubbleChamberAgentCategoriesList.PARTICLE ) ){
				// Cast the public local state into the appropriate type.
				AgtParticlePLSInChamber castedState = (AgtParticlePLSInChamber) state;
				// Add the particle to the particles to update.
				updateInfluence.addParticleToUpdate( castedState );
				// Get the location of the particle.
				Point2D location = castedState.getLocation();
				// Create a "bubble" agent at that location.
				IAgent4Engine bubbleAgent = AgtBubbleFactory.generate(
						location.getX(), 
						location.getY()
				);
				// Add the "Bubble" agent to the simulation.
				SystemInfluenceAddAgent addInfluence = new SystemInfluenceAddAgent( 
						BubbleChamberLevelList.CHAMBER, 
						timeUpperBound,
						timeLowerBound,
						bubbleAgent
				);
				producedInfluences.add( addInfluence );
			}
		}
	}
}
