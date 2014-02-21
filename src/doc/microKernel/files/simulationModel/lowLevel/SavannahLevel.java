package fr.lgi2a.wildlifesimulation.model.levels.savannah;

import java.util.Collection;
import java.util.Set;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent4Engine;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.influences.IInfluence;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgentToLevel;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgentFromLevel;
import fr.lgi2a.similar.microkernel.libs.abstractimpl.AbstractLevel;
import fr.lgi2a.wildlifesimulation.model.agents.WildlifeAgentCategoriesList;
import fr.lgi2a.wildlifesimulation.model.agents.gazelle.savannah.AgtGazellePLSInSavannahLevel;
import fr.lgi2a.wildlifesimulation.model.agents.lion.savannah.AgtLionPLSInSavannahLevel;
import fr.lgi2a.wildlifesimulation.model.environment.savannah.PLSEnvInSavannahLevel;
import fr.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;

/**
 * Models the "Savannah" level.
 */
public class SavannahLevel extends AbstractLevel {
   /**
    * Builds an initialized instance of the level having:
    * <ul>
    *    <li>
    *       a perception relation graph containing an edge between 
    *       this level and itself and between this level and the "Sky" 
    *       level;
    *    </li>
    *    <li>
    *       an influence relation graph containing only an edge between 
    *       this level and itself;
    *   </li>
    *    <li>
    *       an initial consistent dynamic state where the public local state 
    *       of the environment is not yet defined
    *   </li>
    * </ul>
    * @param initialTime The initial time stamp of the level.
    */
   public SavannahLevel(
      SimulationTimeStamp initialTime
   ) {
      super(
         initialTime, 
         WildlifeLevelList.SAVANNAH
      );
      this.addPerceptibleLevel(
         WildlifeLevelList.SKY
      );
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void makeSystemReaction(
      SimulationTimeStamp transitoryTimeMin,
      SimulationTimeStamp transitoryTimeMax,
      ConsistentPublicLocalDynamicState consistentState,
      Collection<IInfluence> systemInfluencesToManage,
      boolean happensBeforeRegularReaction,
      InfluencesMap newInfluencesToProcess
   ) {
      // First get the public local state of the environment in the "Savannah"
      // level.
      PLSEnvInSavannahLevel envLocalState = 
            (PLSEnvInSavannahLevel) consistentState.getPublicLocalStateOfEnvironment();
      // Then handle the user-defined reaction to the system influences.
      for( IInfluence influence : systemInfluencesToManage ){
        if( influence.getCategory().equals( 
               SystemInfluenceAddAgentToLevel.CATEGORY
        ) ) {
            this.manageSystemInfluence(
               (SystemInfluenceAddAgentToLevel) influence, 
               envLocalState
            );
        } else if( influence.getCategory().equals( 
               SystemInfluenceRemoveAgentFromLevel.CATEGORY
        ) ) {
            this.manageSystemInfluence(
               (SystemInfluenceRemoveAgentFromLevel) influence, 
               envLocalState
            );
        }
      }
   }
   
   /**
    * Manages the user-defined reaction to the 
    * {@link SystemInfluenceAddAgentToLevel} system influence.
    * @param influence The system influence managed by this method.
    * @param envLocalState The public local state of the environment
    * in the "Savannah" level.
    */
   private void manageSystemInfluence(
      SystemInfluenceAddAgentToLevel influence,
      PLSEnvInSavannahLevel envLocalState
   ) {
      ILocalStateOfAgent4Engine addedAgent = influence.getPublicLocalState();
      // Get the coordinates of the agent if it is a gazelle or a lion.
      if( 
         addedAgent.getCategoryOfAgent().isA( WildlifeAgentCategoriesList.GAZELLE ) ||
         addedAgent.getCategoryOfAgent().isA( WildlifeAgentCategoriesList.LION )
      ){
         double x = -1;
         double y = -1;
         if( addedAgent.getCategoryOfAgent().isA( WildlifeAgentCategoriesList.GAZELLE ) ){
            AgtGazellePLSInSavannahLevel publicLocalState = 
                  (AgtGazellePLSInSavannahLevel) addedAgent;
            x = publicLocalState.getCoordinates().getX();
            y = publicLocalState.getCoordinates().getY();
         } else {
            AgtLionPLSInSavannahLevel publicLocalState = 
                  (AgtLionPLSInSavannahLevel) addedAgent;
            x = publicLocalState.getCoordinates().getX();
            y = publicLocalState.getCoordinates().getY();
         }
         // Add the agent to the grid.
         envLocalState.addAgent(
            addedAgent, 
            x, 
            y
         );
      }
   }
   
   /**
    * Manages the user-defined reaction to the 
    * {@link SystemInfluenceRemoveAgentFromLevel} system influence.
    * @param influence The system influence managed by this method.
    * @param envLocalState The public local state of the environment
    * in the "Savannah" level.
    */
   private void manageSystemInfluence(
      SystemInfluenceRemoveAgentFromLevel influence,
      PLSEnvInSavannahLevel envLocalState
   ) {
      ILocalStateOfAgent removedAgent = influence.getAgentLocalState();
      // Check if the agent is a Lion or a gazelle.
      if( 
         removedAgent.getCategoryOfAgent().isA( WildlifeAgentCategoriesList.GAZELLE ) ||
         removedAgent.getCategoryOfAgent().isA( WildlifeAgentCategoriesList.LION )
      ){
         // Get the coordinates of the agent.
         double x = -1;
         double y = -1;
         if( removedAgent.getCategoryOfAgent().isA( WildlifeAgentCategoriesList.GAZELLE ) ){
            AgtGazellePLSInSavannahLevel publicLocalState = 
                  (AgtGazellePLSInSavannahLevel) removedAgent;
            x = publicLocalState.getCoordinates().getX();
            y = publicLocalState.getCoordinates().getY();
         } else {
            AgtLionPLSInSavannahLevel publicLocalState = 
                  (AgtLionPLSInSavannahLevel) removedAgent;
            x = publicLocalState.getCoordinates().getX();
            y = publicLocalState.getCoordinates().getY();
         }
         envLocalState.removeAgent(removedAgent, x, y);
      }
   }
   
   // //   // //   // //   // //   // //   // //   // //
   //
   //   Methods that are out of the scope of this example.
   //
   // //   // //   // //   // //   // //   // //   // //

   /**
    * {@inheritDoc}
    */
   @Override
   public SimulationTimeStamp getNextTime(
      SimulationTimeStamp currentTime
   ) {
      throw new UnsupportedOperationException(
         "This method is out of the scope of this example."
      );
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void makeRegularReaction(
      SimulationTimeStamp transitoryTimeMin,
      SimulationTimeStamp transitoryTimeMax,
      ConsistentPublicLocalDynamicState consistentState,
      Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
      InfluencesMap remainingInfluences
   ) {
      throw new UnsupportedOperationException(
         "This method is out of the scope of this example."
      );
   }
}
