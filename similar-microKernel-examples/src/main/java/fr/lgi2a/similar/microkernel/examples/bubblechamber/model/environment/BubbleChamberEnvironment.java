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
package fr.lgi2a.similar.microkernel.examples.bubblechamber.model.environment;

import java.awt.geom.Point2D;

import fr.lgi2a.similar.microkernel.IDynamicStateMap;
import fr.lgi2a.similar.microkernel.IPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.InfluencesMap;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.BubbleChamberAgentCategoriesList;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.bubble.AgtBubble;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.bubble.AgtBubbleFactory;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.cannon.external.AgtCannonPLSInExternal;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.particle.chamber.AgtParticlePLSInChamber;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.environment.external.EnvPLSInExternal;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.influences.tochamber.RIUpdateParticlesSpatialStateInChamber;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.influences.toexternal.RICoolDown;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractEnvironment;

/**
 * The environment used in the "Bubble chamber" simulation.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class BubbleChamberEnvironment extends AbstractEnvironment {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void natural(
		LevelIdentifier level,
		SimulationTimeStamp time,
		IDynamicStateMap levelsPublicLocalObservableDynamicState,
		InfluencesMap producedInfluences
	) {
		if( level.equals( BubbleChamberLevelList.CHAMBER ) ){
			this.naturalForChamberLevel(
					time,
					levelsPublicLocalObservableDynamicState, 
					producedInfluences
			);
		} else if( level.equals( BubbleChamberLevelList.EXTERNAL ) ) {
			this.naturalForExternalLevel(
					levelsPublicLocalObservableDynamicState, 
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
		SimulationTimeStamp time,
		IDynamicStateMap levelsPublicLocalObservableDynamicState,
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
		RIUpdateParticlesSpatialStateInChamber updateInfluence = new RIUpdateParticlesSpatialStateInChamber();
		// Add this influence to the produced influences (the particles are registered
		// to the influence later).
		producedInfluences.add( updateInfluence );
		// Then get the dynamic state of the "Chamber" level, to list the 
		// particles
		IPublicLocalDynamicState dynamicState = 
				levelsPublicLocalObservableDynamicState.get( BubbleChamberLevelList.CHAMBER );
		// Search for particles among the agents lying in that state.
		for( IPublicLocalStateOfAgent state : dynamicState.getPublicLocalStateOfAgents() ){
			// Check if the agent is a particle.
			if( state.getOwner().getCategory().equals( BubbleChamberAgentCategoriesList.PARTICLE ) ){
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
		IDynamicStateMap levelsPublicLocalObservableDynamicState,
		InfluencesMap producedInfluences
	) {
		// First get the ambient external temperature of the environment
		IPublicLocalDynamicState dstate = levelsPublicLocalObservableDynamicState.get( BubbleChamberLevelList.EXTERNAL );
		EnvPLSInExternal castedState = (EnvPLSInExternal) dstate.getPublicLocalStateOfEnvironment();
		double ambientTemperature = castedState.getAmbientTemperature();
		// Create the influence cooling down cannons
		RICoolDown influence = new RICoolDown( ambientTemperature );
		// Then find the cannons and add them to the influence.
		for( IPublicLocalStateOfAgent agtState : dstate.getPublicLocalStateOfAgents() ) {
			if( agtState.getOwner().getCategory().equals( BubbleChamberAgentCategoriesList.CANNON ) ){
				influence.cannon.add( (AgtCannonPLSInExternal) agtState );
			}
		}
		// Add the influence to the results.
		producedInfluences.add( influence );
	}
}
