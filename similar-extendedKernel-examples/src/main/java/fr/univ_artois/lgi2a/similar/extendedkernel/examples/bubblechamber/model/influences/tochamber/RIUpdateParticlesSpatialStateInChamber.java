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
package fr.univ_artois.lgi2a.similar.extendedkernel.examples.bubblechamber.model.influences.tochamber;

import java.util.HashSet;
import java.util.Set;

import fr.univ_artois.lgi2a.similar.extendedkernel.examples.bubblechamber.model.agents.particle.chamber.AgtParticlePLSInChamber;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.influences.RegularInfluence;

/**
 * This influence is sent by the environment to the "Chamber" level to trigger the computation 
 * of the new acceleration, speed and location of the "Particle" agents.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class RIUpdateParticlesSpatialStateInChamber extends RegularInfluence {
	/**
	 * The category of this influence.
	 */
	public static final String CATEGORY = "Update particles spatial state";
	
	/**
	 * The particles which state has to be updated.
	 */
	private Set<AgtParticlePLSInChamber> particles;
	
	/**
	 * Builds an initialized instance of this influence.
	 * @param timeLowerBound The lower bound of the transitory period during which this influence was created.
	 * @param timeUpperBound The upper bound of the transitory period during which this influence was created.
	 */
	public RIUpdateParticlesSpatialStateInChamber(
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound
	) {
		super(
			CATEGORY, 
			BubbleChamberLevelList.CHAMBER,
			timeLowerBound, 
			timeUpperBound
		);
		this.particles = new HashSet<AgtParticlePLSInChamber>();
	}

	/**
	 * Gets the particles which state has to be updated.
	 * @return The particles which state has to be updated.
	 */
	public Set<AgtParticlePLSInChamber> getParticlesToUpdate(){
		return this.particles;
	}
	
	/**
	 * Adds a particle to update in reaction to this influence.
	 * @param particle The particle that has to be udpated.
	 */
	public void addParticleToUpdate( AgtParticlePLSInChamber particle ) {
		this.particles.add( particle );
	}
}
