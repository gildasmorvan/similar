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
package fr.univ_artois.lgi2a.similar.extendedkernel.examples.bubblechamber.model.influences.toexternal;

import fr.univ_artois.lgi2a.similar.extendedkernel.examples.bubblechamber.model.agents.cannon.external.AgtCannonPLSInExternal;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.influences.RegularInfluence;

/**
 * This influence is sent by a "Cannon" in the "External" level whenever it fires a new
 * particle in the environment.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class RIFireParticle extends RegularInfluence {
	/**
	 * The category of this influence.
	 */
	public static final String CATEGORY = "Fire particle";
	
	/**
	 * The particle being fired in the bubble chamber by the cannon.
	 */
	public final IAgent4Engine particle;
	/**
	 * The cannon that fired the particle.
	 */
	public final AgtCannonPLSInExternal cannon;
	
	/**
	 * Builds an initialized instance of this influence.
	 * @throws IllegalArgumentException If either <code>cannon</code> or 
	 * <code>particle</code> are <code>null</code>.
	 * @param timeLowerBound The lower bound of the transitory period during which this influence was created.
	 * @param timeUpperBound The upper bound of the transitory period during which this influence was created.
	 */
	public RIFireParticle( 
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound,
			AgtCannonPLSInExternal cannon,
			IAgent4Engine particle
	) {
		super(
			CATEGORY, 
			BubbleChamberLevelList.EXTERNAL,
			timeLowerBound, 
			timeUpperBound
		);
		if( particle == null | cannon == null ){
			throw new IllegalArgumentException( "The arguments cannot be null." );
		}
		this.particle = particle;
		this.cannon = cannon;
	}
}
