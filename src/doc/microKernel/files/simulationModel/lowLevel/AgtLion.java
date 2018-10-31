package fr.univ_artois.lgi2a.wildlifesimulation.model.agents.lion;

import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Set;

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractAgent;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.WildlifeAgentCategoriesList;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.gazelle.savannah.AgtGazellePLSInSavannahLevel;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.lion.savannah.AgtLionHLSInSavannahLevel;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.lion.savannah.AgtLionPDFSavannah;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.lion.savannah.AgtLionPLSInSavannahLevel;
import fr.univ_artois.lgi2a.wildlifesimulation.model.environment.savannah.PLSEnvInSavannahLevel;
import fr.univ_artois.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;

/**
 * Models an agent from the 'Lion' category.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class AgtLion extends AbstractAgent {
   /**
    * Builds a "Lion" agent without initializing the global, the public
    * local and the private local state of the agent.
    * The setter of these elements have to be called manually.
    */
   public AgtLion( ) {
      super( WildlifeAgentCategoriesList.LION );
   }
   
      // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
      // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
      // // 
      // //   Perception related method of the agent. 
      // //
      // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
      // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //

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
   ){
      // Determine the level from which the perception is made,
      // and forward the call to the appropriate method.
      if( level.equals( WildlifeLevelList.SAVANNAH ) ){
         return this.perceiveFromSavannah(
               timeLowerBound, 
               timeUpperBound,
               publicLocalStates, 
               privateLocalState,
               dynamicStates
         );
      } else {
         throw new IllegalStateException(
            "The 'Lion' agetn cannot perceive from the level '" + level + "'"
         );
      }
   }
   
   /**
    * Models the perception process preceding a decision from the "Savannah" level.
    */
   private AgtLionPDFSavannah perceiveFromSavannah( 
         SimulationTimeStamp timeLowerBound,
         SimulationTimeStamp timeUpperBound,
         Map<LevelIdentifier, ILocalStateOfAgent> publicLocalStates,
         ILocalStateOfAgent privateLocalState,
         IPublicDynamicStateMap dynamicStates
   ){
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
      
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // // 
   // //   The remaining methods are managed in a later step of the simulation 
   // //   design process. 
   // //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
         

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
      throw new UnsupportedOperationException( 
              "This operation currently has no specification." 
        );
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
      throw new UnsupportedOperationException( 
              "This operation currently has no specification." 
        );
   }
}
