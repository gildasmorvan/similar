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
package fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.cannon;

import java.util.Map;

import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.ArithmeticParameters;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.BubbleChamberAgentCategoriesList;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.cannon.external.AgtCannonHLSInExternal;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.cannon.external.AgtCannonPLSInExternal;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.particle.AgtParticle;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.particle.AgtParticleFactory;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.influences.toexternal.RIFireParticle;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.influences.toexternal.RIMoveCannon;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.libs.abstractimpl.AbstractAgent;
import fr.lgi2a.similar.microkernel.libs.generic.EmptyPerceivedData;

/**
 * Models a "Cannon" agent of the simulation.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 */
public class AgtCannon extends AbstractAgent {
	/**
	 * Builds a agent instance without initializing the global state and the public
	 * local state of the agent.
	 * The setter of these elements have to be called manually.
	 */
	public AgtCannon( ) {
		super( BubbleChamberAgentCategoriesList.CANNON );
	}
	
	//
	//
	// Behavior
	//
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPerceivedData perceive(
		LevelIdentifier level,
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound,
		Map<LevelIdentifier, ILocalStateOfAgent> publicLocalStates,
		ILocalStateOfAgent privateLocalState,
		IPublicDynamicStateMap dynamicStates
	) {
		return new EmptyPerceivedData(
			level, 
			timeLowerBound, 
			timeUpperBound
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reviseGlobalState(
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound,
		Map<LevelIdentifier, IPerceivedData> perceivedData,
		IGlobalState globalState
	) {
		// Does nothing in this case.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decide(
			LevelIdentifier levelId,
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound, 
			IGlobalState globalState,
			ILocalStateOfAgent publicLocalState, 
			ILocalStateOfAgent privateLocalState, 
			IPerceivedData perceivedData,
			InfluencesMap producedInfluences
	) {
		if( levelId.equals( BubbleChamberLevelList.EXTERNAL ) ){
			this.decideFromExternalLevel(
					timeLowerBound,
					timeUpperBound,
					globalState, 
					(AgtCannonPLSInExternal) publicLocalState,
					(AgtCannonHLSInExternal) privateLocalState,
					perceivedData, 
					producedInfluences
			);
		} else {
			throw new UnsupportedOperationException(
				"The agent does not define a decision process " +
				"for the '" + levelId + "' level."
			);
		}
	}

	/**
	 * Models the decision process of the agent from the 
	 * "External" level of the simulation.
	 */
	public void decideFromExternalLevel(
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound, 
			IGlobalState globalState,
			AgtCannonPLSInExternal publicLocalState, 
			AgtCannonHLSInExternal privateLocalState, 
			IPerceivedData perceivedData,
			InfluencesMap producedInfluences
	) {
		//Move the cannon
		RIMoveCannon moveInfluence = new RIMoveCannon(
				timeLowerBound,
				timeUpperBound,
				publicLocalState
			);
		producedInfluences.add(moveInfluence);
		// Get the current temperature of the cannon.
		double temperature = publicLocalState.getTemperature();
		// Get the overheat temperature of the cannon.
		double overheatTemperature = privateLocalState.getOverheatTemperature();
		// Check if it is not overheating.
		if( temperature - overheatTemperature < - ArithmeticParameters.DOUBLE_PRECISION ){
			// If the cannon is not overheating, then fire a new particle.
			// First create the particle.
			AgtParticle particleAgt = AgtParticleFactory.generate(
					publicLocalState.getEntryPointInChamber().getX(), 
					publicLocalState.getEntryPointInChamber().getY(), 
					publicLocalState.getDirection().getX() * publicLocalState.getPower(),
					publicLocalState.getDirection().getY() * publicLocalState.getPower()
			);
			// Then fire it in the simulation.
			RIFireParticle influence = new RIFireParticle( 
					timeLowerBound,
					timeUpperBound,
					publicLocalState, 
					particleAgt
			);
			producedInfluences.add( influence );
		}
	}
}
