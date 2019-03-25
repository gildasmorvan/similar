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
package fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.environment;

import java.awt.geom.Point2D;
import java.util.Map;

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.BubbleChamberAgentCategoriesList;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.bubble.AgtBubble;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.bubble.AgtBubbleFactory;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.cannon.external.AgtCannonPLSInExternal;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.particle.chamber.AgtParticlePLSInChamber;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.environment.external.EnvPLSInExternal;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.influences.tochamber.RIUpdateParticlesSpatialStateInChamber;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.influences.toexternal.RICoolDown;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.tools.RandomValueFactory;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.univ_artois.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractEnvironment;

/**
 * The environment used in the "Bubble chamber" simulation.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 */
public class BubbleChamberEnvironment extends AbstractEnvironment {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void natural(
		LevelIdentifier level,
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound,
		Map<LevelIdentifier, ILocalStateOfEnvironment> publicLocalStates,
		ILocalStateOfEnvironment privateLocalState,
		IPublicDynamicStateMap dynamicStates,
		InfluencesMap producedInfluences
	) {
		if( level.equals( BubbleChamberLevelList.CHAMBER ) ){
			this.naturalForChamberLevel(
				timeLowerBound,
				timeUpperBound, 
				publicLocalStates,
				privateLocalState,
				dynamicStates,
				producedInfluences
			);
		} else if( level.equals( BubbleChamberLevelList.EXTERNAL ) ) {
			this.naturalForExternalLevel(
				timeLowerBound,
				timeUpperBound, 
				publicLocalStates,
				privateLocalState,
				dynamicStates,
				producedInfluences
			);
		} else {
			throw new UnsupportedOperationException( 
				"The '" + level + "' does not belong to the " +
				"initial specification of the 'bubble chamber' simulation." 
			);
		}
	}
	
	/**
	 * The natural action of the environment for the "Chamber" level.
	 */
	public void naturalForChamberLevel(
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
		
		// Search for bubbles to remove from the chamber.
		for( ILocalStateOfAgent state : dynamicState.getPublicLocalStateOfAgents() ){
			// Check if the agent is a bubble.
			if( state.getCategoryOfAgent().isA( BubbleChamberAgentCategoriesList.BUBBLE ) ){
				if(RandomValueFactory.getStrategy().randomDouble(0, 1) < 0.001) {
					SystemInfluenceRemoveAgent removeAgent = new SystemInfluenceRemoveAgent(
							BubbleChamberLevelList.CHAMBER, 
							timeUpperBound,
							timeLowerBound,
							state
						);
					producedInfluences.add(removeAgent);
					
				}
			}
		}
		
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
				AgtBubble bubbleAgent = AgtBubbleFactory.generate(
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
	
	/**
	 * The natural action of the environment for the "External" level.
	 */
	public void naturalForExternalLevel(
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound,
		Map<LevelIdentifier, ILocalStateOfEnvironment> publicLocalStates,
		ILocalStateOfEnvironment privateLocalState,
		IPublicDynamicStateMap dynamicStates,
		InfluencesMap producedInfluences
	) {
		// First get the ambient external temperature of the environment from
		// the public local state of the environment in the external level.
		EnvPLSInExternal castedState = (EnvPLSInExternal) publicLocalStates.get( 
			BubbleChamberLevelList.EXTERNAL 
		);
		double ambientTemperature = castedState.getAmbientTemperature();
		// Create the influence cooling down cannons
		RICoolDown influence = new RICoolDown( 
			timeLowerBound,
			timeUpperBound,
			ambientTemperature
		);
		// Then find the cannons and add them to the influence.
		IPublicLocalDynamicState extLvlState = dynamicStates.get( BubbleChamberLevelList.EXTERNAL );
		for( ILocalStateOfAgent agtState : extLvlState.getPublicLocalStateOfAgents() ) {
			if( agtState.getCategoryOfAgent().isA( BubbleChamberAgentCategoriesList.CANNON ) ){
				influence.cannon.add( (AgtCannonPLSInExternal) agtState );
			}
		}
		// Add the influence to the results.
		producedInfluences.add( influence );
	}
}
