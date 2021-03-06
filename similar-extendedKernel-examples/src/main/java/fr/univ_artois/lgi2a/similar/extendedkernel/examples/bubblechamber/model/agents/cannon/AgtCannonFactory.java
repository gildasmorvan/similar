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
package fr.univ_artois.lgi2a.similar.extendedkernel.examples.bubblechamber.model.agents.cannon;

import fr.univ_artois.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.bubblechamber.model.BubbleChamberParameters;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.bubblechamber.model.agents.BubbleChamberAgentCategoriesList;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.bubblechamber.model.agents.cannon.external.AgtCannonDecisionInExternal;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.bubblechamber.model.agents.cannon.external.AgtCannonHLSInExternal;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.bubblechamber.model.agents.cannon.external.AgtCannonPLSInExternal;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.generic.EmptyAgtPerceptionModel;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.generic.IdentityAgtGlobalStateRevisionModel;
import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.libs.generic.EmptyGlobalState;

/**
 * The factory creating instances of agents the "Bubble" category.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class AgtCannonFactory {
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
	 * Generates a new agent of the "Cannon" category.
	 * @param cannonEndX The x axis coordinate of the point where the cannon points to.
	 * @param cannonEndY The y axis coordinate of the point where the cannon points to.
	 * @param initialDirectionX The direction of the cannon along the x axis.
	 * @param initialDirectionY The direction of the cannon along the y axis.
	 * @param initialPower The initial power of the cannon.
	 * @param ambientTemperature The ambient temperature when the cannon was created.
	 * @return The newly created instance.
	 */
	public static ExtendedAgent generate(
		double cannonEndX,
		double cannonEndY,
		double initialDirectionX,
		double initialDirectionY,
		double initialPower,
		double ambientTemperature
	){
		ExtendedAgent cannon = new ExtendedAgent( 
			BubbleChamberAgentCategoriesList.CANNON
		);
		// Define the revision model of the global state.
		cannon.specifyGlobalStateRevisionModel(
			new IdentityAgtGlobalStateRevisionModel( )
		);
		// Specify the behavior of the agent in the various levels where it can lie.
		LevelIdentifier levelId = BubbleChamberLevelList.EXTERNAL;
		cannon.specifyBehaviorForLevel(
			levelId, 
			new EmptyAgtPerceptionModel( levelId ), 
			new AgtCannonDecisionInExternal( )
		);
		// Define the initial global state of the agent.
		cannon.initializeGlobalState( new EmptyGlobalState( ) );
		// Add the agent in the "External" level.
		cannon.includeNewLevel( 
				BubbleChamberLevelList.EXTERNAL, 
				new AgtCannonPLSInExternal(
						cannon, 
						cannonEndX, 
						cannonEndY, 
						initialDirectionX, 
						initialDirectionY, 
						initialPower, 
						ambientTemperature
				),
				new AgtCannonHLSInExternal(
						cannon, 
					getParameters().cannonOverheatTemperature
				)
		);
		return cannon;
	}
}
