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
package fr.lgi2a.similar.extendedkernel.examples.densitycontrolledlife.model.agents.cellcluster;

import fr.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.lgi2a.similar.extendedkernel.examples.densitycontrolledlife.model.DensityControlledLifeParameters;
import fr.lgi2a.similar.extendedkernel.examples.densitycontrolledlife.model.agents.DensityControlledLifeAgentList;
import fr.lgi2a.similar.extendedkernel.examples.densitycontrolledlife.model.agents.cellcluster.meso.AgtCellClusterDecisionFromMeso;
import fr.lgi2a.similar.extendedkernel.examples.densitycontrolledlife.model.agents.cellcluster.meso.AgtCellClusterPLSInMeso;
import fr.lgi2a.similar.extendedkernel.examples.densitycontrolledlife.model.agents.cellcluster.meso.AgtCellClusterPerceptionFromMeso;
import fr.lgi2a.similar.extendedkernel.examples.densitycontrolledlife.model.levels.DensityControlledLifeLevelList;
import fr.lgi2a.similar.extendedkernel.libs.generic.IdentityAgtGlobalStateRevisionModel;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent4Engine;
import fr.lgi2a.similar.microkernel.libs.generic.EmptyGlobalState;
import fr.lgi2a.similar.microkernel.libs.generic.EmptyLocalStateOfAgent;

/**
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class AgtCellClusterFactory {
	
	
	/**
     * The parameters that are used in this agent factory.
     */
    private static DensityControlledLifeParameters PARAMETERS = null;
    /**
     * Gets the parameters used in this agent factory.
     * @return The parameters used in this agent factory.
     * @throws IllegalArgumentException If the parameters are not set.
     */
    public static DensityControlledLifeParameters getParameters() {
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
     * @param parameters
     */
    public static void setParameters(
    		DensityControlledLifeParameters parameters
    ){
        PARAMETERS = parameters;
    }
    
    /**
     * Generates a cell located at a specific place of the grid. 
     * @param x The x axis coordinate of the agent.
     * @param y The y axis coordinate of the agent.
     * @return A newly created and initialized "cell" agent.
     */
    public static ExtendedAgent generate( int x, int y) {
    	// First create the agent instance.
        ExtendedAgent newAgent = new ExtendedAgent( DensityControlledLifeAgentList.CELL_CLUSTER );
        // Then create its initial global state.
        newAgent.initializeGlobalState(new EmptyGlobalState( ));
        // Then create the public and private local states in the "meso" level and add them to the agents.
        AgtCellClusterPLSInMeso plsInMeso = new AgtCellClusterPLSInMeso(
    		newAgent, 
    		PARAMETERS.kP,
    		PARAMETERS.expectedDensity,
        	x, 
        	y,
        	PARAMETERS.xlength,
        	PARAMETERS.ylength
        );
        ILocalStateOfAgent4Engine hlsInMeso = new EmptyLocalStateOfAgent(
        		DensityControlledLifeLevelList.MESO, 
    		newAgent
    	);
        newAgent.includeNewLevel( 
        		DensityControlledLifeLevelList.MESO, 
        		plsInMeso,
        		hlsInMeso
    	);
        // Finally specify its behavior.
        // Specify its global state revision model.
        newAgent.specifyGlobalStateRevisionModel(
        		new IdentityAgtGlobalStateRevisionModel( )
        );
        // Specify its behavior from the "micro" level.
        newAgent.specifyBehaviorForLevel(
        		DensityControlledLifeLevelList.MESO, 
    		new AgtCellClusterPerceptionFromMeso( ), 
    		new AgtCellClusterDecisionFromMeso( )
        );
        return newAgent;
    }

}
