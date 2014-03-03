package fr.lgi2a.wildlifesimulation.model.agents.lion.savannah;

import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Set;

import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtPerceptionModel;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.lgi2a.wildlifesimulation.model.agents.WildlifeAgentCategoriesList;
import fr.lgi2a.wildlifesimulation.model.agents.gazelle.savannah.AgtGazellePLSInSavannahLevel;
import fr.lgi2a.wildlifesimulation.model.agents.lion.savannah.AgtLionHLSInSavannahLevel;
import fr.lgi2a.wildlifesimulation.model.agents.lion.savannah.AgtLionPDFSavannah;
import fr.lgi2a.wildlifesimulation.model.agents.lion.savannah.AgtLionPLSInSavannahLevel;
import fr.lgi2a.wildlifesimulation.model.environment.savannah.PLSEnvInSavannahLevel;
import fr.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;

/**
 * The perception model of a "lion" agent from the "Savannah" level.
 */
public class AgtLionPerceptionFromSavannah2 extends AbstractAgtPerceptionModel {
   /**
    * Builds an instance of the perception model.
    */
   public AgtLionPerceptionFromSavannah2( ) {
      super( WildlifeAgentCategoriesList.LION );
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public IPerceivedData perceive(
         SimulationTimeStamp timeLowerBound,
         SimulationTimeStamp timeUpperBound,
         Map<LevelIdentifier, ILocalStateOfAgent> publicLocalStates,
         ILocalStateOfAgent privateLocalState,
         IPublicDynamicStateMap dynamicStates
   ) {
      // Create an empty perceived data model.
      AgtLionPDFSavannah perceivedData = new AgtLionPDFSavannah( 
            timeLowerBound,
            timeUpperBound
            );
      // First get the coordinates of the lion agent.
      AgtLionPLSInSavannahLevel lionStateInSavannah = (AgtLionPLSInSavannahLevel) 
            publicLocalStates.get( WildlifeLevelList.SAVANNAH );
      Point2D.Double lionCoordinates = lionStateInSavannah.getCoordinates();
      // Cast the private local state of the agent in the appropriate type
      AgtLionHLSInSavannahLevel castedPrivateState = 
            (AgtLionHLSInSavannahLevel) privateLocalState;
      // Find the nearby young Gazelles.
      int x = (int) Math.floor( lionCoordinates.getX() );
      int y = (int) Math.floor( lionCoordinates.getY() );
      IPublicLocalDynamicState savannahDynamicState = 
            dynamicStates.get( WildlifeLevelList.SAVANNAH );
      PLSEnvInSavannahLevel envStateInSavannah = (PLSEnvInSavannahLevel)
            savannahDynamicState.getPublicLocalStateOfEnvironment();
      for( 
            int dx = x - castedPrivateState.getGazellePerceptionRadius() / 2; 
            dx <= x + castedPrivateState.getGazellePerceptionRadius() / 2; 
            dx++ 
            ){
         for( 
               int dy = y - castedPrivateState.getGazellePerceptionRadius() / 2; 
               dy <= y + castedPrivateState.getGazellePerceptionRadius() / 2; 
               dy++ 
               ){
            // Get the agents located in the specified cell of the grid.
            try{
               Set<ILocalStateOfAgent> agentsInCell = 
                     envStateInSavannah.getAgentsLocatedAt(dx, dy);
               for( ILocalStateOfAgent agtState : agentsInCell ){
                  // Check if the agent is a gazelle
                  if( agtState.getCategoryOfAgent().isA( WildlifeAgentCategoriesList.GAZELLE ) ){
                     // Check the age of the gazelle
                     AgtGazellePLSInSavannahLevel gazelleState = 
                           (AgtGazellePLSInSavannahLevel) agtState;
                     if( gazelleState.getAge() < castedPrivateState.getGazelleAgeThreshold() ){
                        perceivedData.addNearbyPrey( gazelleState );
                     }
                  }
               }
            } catch( IllegalArgumentException e ) {
               // Case where the cell is not in the grid: do nothing.
            }
         }
      }
      return perceivedData;
   }

}
