/**
 * Copyright or � or Copr. LGI2A
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
package fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.particle;

import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.BubbleChamberParameters;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.particle.chamber.AgtParticlePLSInChamber;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.univ_artois.lgi2a.similar.microkernel.libs.generic.EmptyGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.libs.generic.EmptyLocalStateOfAgent;

/**
 * The factory creating instances of agents the "Particle" category.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class AgtParticleFactory {
    /**
     * The parameters that are used in this agent factory.
     */
    private static BubbleChamberParameters PARAMETERS = null;
	
    /**
     * Gets the parameters used in this agent factory.
     * @return The parameters used in this agent factory.
     * @throws IllegalArgumentException If the parameters are not set.
     */
    public static BubbleChamberParameters getParameters() {
        if( PARAMETERS == null ){
            throw new IllegalArgumentException( 
                "The parameters are not set." 
            );
        } else {
            return PARAMETERS;
        }
    }
    
    /**
     * Sets the parameters used in this agent factory.
     * @param parameters The new parameters used in this agent factory.
     */
    public static void setParameters(
    		BubbleChamberParameters parameters
    ){
        PARAMETERS = parameters;
    }
	
	/**
	 * Generates a new agent of the "Particle" category, having a null acceleration.
	 * @param initialX The initial x coordinate of the particle in the "chamber" level.
	 * @param initialY The initial y coordinate of the particle in the "chamber" level.
	 * @param initialVelocityAlongX The initial x velocity of the particle in the "chamber" level.
	 * @param initialVelocityAlongY The initial y velocity of the particle in the "chamber" level.
	 * @return The newly created instance.
	 */
	public static AgtParticle generate(
			double initialX,
			double initialY, 
			double initialVelocityAlongX,
			double initialVelocityAlongY
	){
		AgtParticle result = new AgtParticle( );
		// Define the initial global state of the agent.
		result.initializeGlobalState( new EmptyGlobalState( ) );
		// Define the local states of the agent in the "Chamber" level.
		result.includeNewLevel(
				BubbleChamberLevelList.CHAMBER,
				new AgtParticlePLSInChamber(
					result, 
					initialX, 
					initialY, 
					initialVelocityAlongX, 
					initialVelocityAlongY, 
					0, 
					0
				),
				new EmptyLocalStateOfAgent(
					BubbleChamberLevelList.CHAMBER, 
					result
				)
		);
		return result;
	}
}
