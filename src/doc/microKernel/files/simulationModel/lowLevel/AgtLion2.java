package fr.univ_artois.lgi2a.wildlifesimulation.model.agents.lion;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractAgent;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.WildlifeAgentCategoriesList;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.gazelle.savannah.AgtGazellePLSInSavannahLevel;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.lion.savannah.AgtLionHLSInSavannahLevel2;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.lion.savannah.AgtLionPDFSavannah;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.lion.savannah.AgtLionPLSInSavannahLevel;
import fr.univ_artois.lgi2a.wildlifesimulation.model.influences.tosavannah.RISavannahEat;
import fr.univ_artois.lgi2a.wildlifesimulation.model.influences.tosavannah.RISavannahMove;
import fr.univ_artois.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;
import fr.univ_artois.lgi2a.wildlifesimulation.tools.RandomValueFactory;

/**
 * Models an agent from the 'Lion' category.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class AgtLion2 extends AbstractAgent {
   /**
    * Builds a "Lion" agent without initializing the global state and the public
    * local state of the agent.
    * The setter of these elements have to be called manually.
    */
   public AgtLion2( ) {
      super( WildlifeAgentCategoriesList.LION );
   }

   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // // 
   // //   Decision related method of the agent. 
   // //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //

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
        if( levelId.equals( WildlifeLevelList.SAVANNAH ) ){
            this.decideFromSavannah(
                  timeLowerBound,
                  timeUpperBound, 
                  globalState,
                  publicLocalState,
                  privateLocalState, 
                  perceivedData,
                  producedInfluences
            );
       } else {
               throw new IllegalStateException(
                   "The 'Lion' agetn cannot perceive from the level '" + levelId + "'"
               );
       }
   }

   /**
    * Models the decision from the "Savannah" level.
    */
   private void decideFromSavannah(
      SimulationTimeStamp timeLowerBound,
      SimulationTimeStamp timeUpperBound, 
      IGlobalState globalState,
      ILocalStateOfAgent publicLocalState,
      ILocalStateOfAgent privateLocalState, 
      IPerceivedData perceivedData,
      InfluencesMap producedInfluences
   ) {
      // First cast the perceived data into the appropriate type
      AgtLionPDFSavannah castedPData = (AgtLionPDFSavannah) perceivedData;
      // Then cast in the appropriate type the public local state of this agent 
      // in the Savannah level and get the location of the agent.
      AgtLionPLSInSavannahLevel castedPublicLocalState = 
            (AgtLionPLSInSavannahLevel) publicLocalState;
        Point2D.Double location = castedPublicLocalState.getCoordinates();
        // Then get the list of the closest preys
        double closestDistance = Double.MAX_VALUE;
        List<AgtGazellePLSInSavannahLevel> closestPreys = 
              new ArrayList<AgtGazellePLSInSavannahLevel>();
        AgtGazellePLSInSavannahLevel gazelle = null;
        for( 
            Iterator<ILocalStateOfAgent> it = castedPData.nearbyPreysIterator();
            it.hasNext();
            gazelle = (AgtGazellePLSInSavannahLevel) it.next()
        ){
            double distanceFromGazelle = location.distance( gazelle.getCoordinates() );
            if( distanceFromGazelle < closestDistance ){
                closestPreys.clear();
                closestPreys.add( gazelle );
                closestDistance = distanceFromGazelle;
            } else if( distanceFromGazelle == closestDistance ){
                closestPreys.add( gazelle );
            }
        }
        // Cast in the appropriate type the private local state of this agent
        // in the Savannah level.
        AgtLionHLSInSavannahLevel2 castedPrivateState = 
              (AgtLionHLSInSavannahLevel2) privateLocalState;
        // If there is at least one prey, choose the closest prey at random.
        if( ! closestPreys.isEmpty() ) {
            AgtGazellePLSInSavannahLevel selectedPrey = 
                    RandomValueFactory.getStrategy().randomElement( closestPreys );
            if( closestDistance < castedPrivateState.getEatDistanceThreshold() ){
                // The prey is close enough to be eaten.
               RISavannahEat influence = new RISavannahEat(
                 timeLowerBound, 
                 timeUpperBound, 
                 castedPublicLocalState, 
                 selectedPrey
               );
               producedInfluences.add( influence );
            } else {
                // The prey is too far away to be eaten: the lion has to move.
               RISavannahMove influence = new RISavannahMove(
                     timeLowerBound, 
                     timeUpperBound, 
                     castedPublicLocalState, 
                        selectedPrey.getCoordinates().getX(),
                        selectedPrey.getCoordinates().getY()
               );
                producedInfluences.add( influence );
            }
        }
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
   public IPerceivedData perceive( 
      LevelIdentifier level,
      SimulationTimeStamp timeLowerBound,
      SimulationTimeStamp timeUpperBound,
      Map<LevelIdentifier, ILocalStateOfAgent> publicLocalStates,
      ILocalStateOfAgent privateLocalState,
      IPublicDynamicStateMap dynamicStates
   ){
      throw new UnsupportedOperationException( 
         "This operation currently has no specification." 
      );
   }


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
}
