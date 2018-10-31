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
package fr.univ_artois.lgi2a.similar.extendedkernel.examples.bubblechamber.model.agents.cannon.external;

import fr.univ_artois.lgi2a.similar.extendedkernel.examples.bubblechamber.model.ArithmeticParameters;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.bubblechamber.model.agents.particle.AgtParticleFactory;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.bubblechamber.model.influences.toexternal.RIFireParticle;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;

/**
 * The decision model of the "Cannon" agent from the "External" level.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class AgtCannonDecisionInExternal extends AbstractAgtDecisionModel {

	/**
	 * Builds an initialized instance of this behavior.
	 * @param levelIdentifier The identifier of the level for which this decision model is defined.
	 */
	public AgtCannonDecisionInExternal(	) {
		super( BubbleChamberLevelList.EXTERNAL );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decide(
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound, 
			IGlobalState globalState,
			ILocalStateOfAgent publicLocalState,
			ILocalStateOfAgent privateLocalState, 
			IPerceivedData perceivedData,
			InfluencesMap producedInfluences
	) {
		// Get the current temperature of the cannon.
		AgtCannonPLSInExternal castedPublicLocalState = (AgtCannonPLSInExternal) publicLocalState;
		double temperature = castedPublicLocalState.getTemperature();
		// Get the overheat temperature of the cannon.
		AgtCannonHLSInExternal castedPrivateLocalState = (AgtCannonHLSInExternal) privateLocalState;
		double overheatTemperature = castedPrivateLocalState.getOverheatTemperature();
		// Check if it is not overheating.
		if( temperature - overheatTemperature < - ArithmeticParameters.DOUBLE_PRECISION ){
			// If the cannon is not overheating, then fire a new particle.
			// First create the particle.
			IAgent4Engine particleAgt = AgtParticleFactory.generate(
					castedPublicLocalState.getEntryPointInChamber().getX(), 
					castedPublicLocalState.getEntryPointInChamber().getY(), 
					castedPublicLocalState.getDirection().getX() * castedPublicLocalState.getPower(),
					castedPublicLocalState.getDirection().getY() * castedPublicLocalState.getPower()
			);
			// Then fire it in the simulation.
			RIFireParticle influence = new RIFireParticle( 
					timeLowerBound,
					timeUpperBound,
					castedPublicLocalState, 
					particleAgt
			);
			producedInfluences.add( influence );
		}
	}
}
