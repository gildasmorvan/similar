package fr.univ_artois.lgi2a.wildlifesimulation.model.agents.lion;

import java.awt.geom.Point2D;

import fr.univ_artois.lgi2a.similar.microkernel.libs.generic.EmptyGlobalState;
import fr.univ_artois.lgi2a.wildlifesimulation.model.WildlifeSimulationParam;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.lion.savannah.AgtLionHLSInSavannahLevel;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.lion.savannah.AgtLionPLSInSavannahLevel;
import fr.univ_artois.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;
import fr.univ_artois.lgi2a.wildlifesimulation.tools.RandomValueFactory;

/**
 * This factory creates a default agent from the "Lion" category.
 */
public class AgtLionFactory {
    /**
     * The parameters that are used in this agent factory.
     */
    private static WildlifeSimulationParam PARAMETERS = null;
    
    /**
     * Gets the parameters used in this agent factory.
     * @return The parameters used in this agent factory.
     * @throws IllegalArgumentException If the parameters are not set.
     */
    public static WildlifeSimulationParam getParameters() {
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
        WildlifeSimulationParam parameters
    ){
        PARAMETERS = parameters;
    }
    
    /**
     * Generates a lion having:
     * <ul>
     *     <li>
     *         An age equal to <code>0</code>
     *     </li>
     *     <li>
     *         An initial location equal to 
     *         <code>initialLocation</code>
     *     </li>
     *     <li>
     *         A random gazelle age threshold between 
     *         {@link WildlifeSimulationParam}#gazelleAgeThresholdMin and 
     *         {@link WildlifeSimulationParam}#gazelleAgeThresholdMax
     *     </li>
     *     <li>
     *         A gazelle perception range equal to
     *         {@link WildlifeSimulationParam}#gazellePercepRadius
     *     </li>
     * </ul>
     * @param initialLocation The initial location of the agent.
     * @return A newly created and initialized "Lion" agent.
     */
    public static AgtLion generate( 
            Point2D initialLocation
    ) {
        // First create the agent instance and initialize its private local state.
        // This implies the identification of the gazelle age threshold of the agent.
        long gazelleAgeThreshold = RandomValueFactory.getStrategy().randomLong(
                getParameters().gazelleAgeThresholdMin, 
                getParameters().gazelleAgeThresholdMax
        );
        AgtLion newAgent = new AgtLion( );
        // Then create its initial global state.
        newAgent.initializeGlobalState(
            new EmptyGlobalState( )
        );
        // Finally create the public and private local states and add them to the agents.
        AgtLionPLSInSavannahLevel plsInSavannah = new AgtLionPLSInSavannahLevel(
                newAgent, 
                initialLocation.getX(), 
                initialLocation.getY(), 
                0
        );
        AgtLionHLSInSavannahLevel hlsInSavannah = new AgtLionHLSInSavannahLevel(
        		newAgent, 
        		getParameters().gazellePercepRadius, 
        		gazelleAgeThreshold
        );
        newAgent.includeNewLevel( 
        		WildlifeLevelList.SAVANNAH, 
        		plsInSavannah ,
        		hlsInSavannah
    	);
        return newAgent;
    }
}
