package fr.univ_artois.lgi2a.wildlifesimulation.model.levels.savannah;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar.microkernel.influences.IInfluence;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractLevel;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.gazelle.savannah.AgtGazellePLSInSavannahLevel;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.generic.savannah.IAgtPLSInSavannah;
import fr.univ_artois.lgi2a.wildlifesimulation.model.environment.savannah.PLSEnvInSavannahLevel;
import fr.univ_artois.lgi2a.wildlifesimulation.model.influences.tosavannah.RISavannahEat;
import fr.univ_artois.lgi2a.wildlifesimulation.model.influences.tosavannah.RISavannahGrowOlder;
import fr.univ_artois.lgi2a.wildlifesimulation.model.influences.tosavannah.RISavannahMove;
import fr.univ_artois.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;
import fr.univ_artois.lgi2a.wildlifesimulation.tools.RandomValueFactory;

/**
 * Models the "Savannah" level.
 */
public class SavannahLevel2 extends AbstractLevel {
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
    * @param eatBehaviorThresholdDistance Models the maximal distance that 
    * can separate a predator from its prey for a "eat" behavior.
    */
   public SavannahLevel2(
      SimulationTimeStamp initialTime,
      double eatBehaviorThresholdDistance
   ) {
      super(
         initialTime, 
         WildlifeLevelList.SAVANNAH
      );
      this.addPerceptibleLevel(
         WildlifeLevelList.SKY
      );
      this.eatBehaviorThresholdDistance = eatBehaviorThresholdDistance;
   }
   
   // //   // //   // //   // //   // //   // //   // //
   //
   //   Definition of the parameters of the reaction.
   //
   // //   // //   // //   // //   // //   // //   // //
   
   /**
    * Models the maximal distance that can separate a predator
    * from its prey for a "eat" behavior.
    */
   private double eatBehaviorThresholdDistance;
   
   // //   // //   // //   // //   // //   // //   // //
   //
   //   Definition of the reaction.
   //
   // //   // //   // //   // //   // //   // //   // //

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
      // Get the public local state of the environment.
      PLSEnvInSavannahLevel envPLS = 
            (PLSEnvInSavannahLevel) consistentState.getPublicLocalStateOfEnvironment();
      // Create the data structure facilitating the management of 
      // the collisions between influences.
      // This data structure stores a map associating to a gazelle agent 
      //       all the eat influences targeting it;
      Map<AgtGazellePLSInSavannahLevel,Set<RISavannahEat>> eatInfluences =
            new LinkedHashMap<AgtGazellePLSInSavannahLevel,Set<RISavannahEat>>( );
      // Then react to the influences causing no collisions, and store in the data
      // structure the ones causing collisions.
      for( IInfluence influence : regularInfluencesOftransitoryStateDynamics ){
         if( influence.getCategory().equals( RISavannahGrowOlder.CATEGORY ) ){
            // Directly handle the side effects of the "Grow older" influences.
            this.manageGrowOlder(
               transitoryTimeMin, 
               transitoryTimeMax, 
               (RISavannahGrowOlder) influence
            );
         } else if( influence.getCategory().equals( RISavannahMove.CATEGORY ) ){
            // Directly handle the side effects of the "Move" influences.
            this.manageMove(
               (RISavannahMove) influence,
               envPLS
            );
         } else if( influence.getCategory().equals( RISavannahEat.CATEGORY ) ){
            // Delay the management of the side effects of the "Eat" influences.
            RISavannahEat castedInfluence = (RISavannahEat) influence;
            Set<RISavannahEat> eatInfluencesSet = eatInfluences.get( 
                  castedInfluence.getPrey() 
            );
            if( eatInfluencesSet == null ){
               eatInfluencesSet = new LinkedHashSet<RISavannahEat>();
               eatInfluences.put( castedInfluence.getPrey() , eatInfluencesSet );
            }
            eatInfluencesSet.add( castedInfluence );
         }
      }
      // Finally manage each collision of influences.
      for( Set<RISavannahEat> eatInfluencesSet : eatInfluences.values() ){
         // Manage the collision of a set of "eat" influences having the same
         // prey.
         this.manageCollision(
            transitoryTimeMin,
            transitoryTimeMax,
            eatInfluencesSet, 
            remainingInfluences 
         );
      }
   }
   
   /**
    * Manages the individual reaction to the "Grow older" influence.
    * @param transitoryTimeMin The lower bound of the transitory period of 
    * the level for which this reaction is performed.
    * @param transitoryTimeMax The upper bound of the transitory period of 
    * the level for which this reaction is performed.
    * @param influence The influence in question.
    */
   private void manageGrowOlder(
      SimulationTimeStamp transitoryTimeMin,
      SimulationTimeStamp transitoryTimeMax,
      RISavannahGrowOlder influence
   ){
      for( IAgtPLSInSavannah agent : influence.getAgent() ){
         agent.setAge(
            agent.getAge() + transitoryTimeMax.compareTo( transitoryTimeMin )
         );
      }
   }
   
   /**
    * Manages the individual reaction to the "Move" influence.
    * @param influence The influence in question.
    * @param envPls The public local state of the environment, 
    * containing the grid topology.
    */
   private void manageMove(
      RISavannahMove influence,
      PLSEnvInSavannahLevel envPls
   ){
      double newX = -1;
      double newY = -1;
      double oldX = influence.getAgent().getCoordinates().getX();
      double oldY = influence.getAgent().getCoordinates().getY();
      double distance = influence.getAgent().getCoordinates().distance(
         influence.getDesiredX(),
         influence.getDesiredY()
      );
      // First check if the distance between the agent
      // and its destination is lower than 1. If true,
      // then the agent arrived at its destination.
      if( distance > 1 ){
         newX = influence.getDesiredX();
         newY = influence.getDesiredY();
      } else {
         // First identify the x and y components of the 
         // vector going from the location of the agent to
         // the destination.
         newX = influence.getDesiredX() - oldX;
         newY = influence.getDesiredY() - oldY;
         // Reduce the length of the vector to a unitary one.
         newX = newX / distance;
         newY = newY / distance;
         // Add the current coordinates of the agent to convert
         // the vector into the new location of the agent.
         newX += oldX;
         newY += oldY;
      }
      // Finally update the public local state of the agent and the
      // topology of the environment.
      envPls.removeAgent(influence.getAgent(), oldX, oldY);
      influence.getAgent().setCoordinates(newX, newY);
      envPls.addAgent(influence.getAgent(), newX, newY);
   }
   
   /**
    * Manages the collision between "eat" influences.
    * @param transitoryTimeMin The lower bound of the transitory period of the 
    * level for which this reaction is performed.
    * @param transitoryTimeMax The upper bound of the transitory period of the 
    * level for which this reaction is performed.
    * @param eatInfluencesSet A set of "eat" influences having the same prey.
    * @param remainingInfluences Where to put the influences that were created 
    * as a side effect of this reaction.
    */
   private void manageCollision(
      SimulationTimeStamp transitoryTimeMin,
      SimulationTimeStamp transitoryTimeMax,
      Set<RISavannahEat> eatInfluencesSet,
      InfluencesMap remainingInfluences
   ){
      // When this method is called, the location of all the agents
      // has been updated in reaction to the "move" influences.
      // The first step in this collision management method is to eliminate
      // the eat influences where the prey managed to get away from the predator.
      Iterator<RISavannahEat> it = eatInfluencesSet.iterator();
      while( it.hasNext() ){
         RISavannahEat influence = it.next();
         // Get the distance between the predator and the prey.
         double distance = influence.getPredator().getCoordinates().distance(
            influence.getPrey().getCoordinates()
         );
         if( distance > this.eatBehaviorThresholdDistance ){
            // The prey escaped from the predator.
            it.remove();
         }
      }
      // Then, if more than one influence remains, we select the predator eating 
      // the prey at random.
      int influencesNumber = eatInfluencesSet.size();
      if( influencesNumber > 1 ){
         RISavannahEat influence = RandomValueFactory.getStrategy().randomElement( 
        		 eatInfluencesSet 
        );
         this.manageEat(
               transitoryTimeMin, 
               transitoryTimeMax,
               influence, 
               remainingInfluences
         );
      } else if( influencesNumber == 1 ) {
         this.manageEat(
            transitoryTimeMin, 
            transitoryTimeMax,
            eatInfluencesSet.iterator().next(), 
            remainingInfluences
         );
      }
   }
   
   /**
    * Manages the individual reaction to the "Move" influence.
    * @param transitoryTimeMin The lower bound of the transitory period 
    * of the level for which this reaction is performed.
    * @param transitoryTimeMax The upper bound of the transitory period 
    * of the level for which this reaction is performed.
    * @param influence The influence in question.
    * @param remainingInfluences Where to put the influences that were created 
    * as a side effect of this reaction.
    */
   private void manageEat(
      SimulationTimeStamp transitoryTimeMin,
      SimulationTimeStamp transitoryTimeMax,
      RISavannahEat influence,
      InfluencesMap remainingInfluences
   ){
      // In this case, the eat influence removes the prey from the simulation.
      SystemInfluenceRemoveAgent newInfluence = new SystemInfluenceRemoveAgent(
         WildlifeLevelList.SAVANNAH, 
         transitoryTimeMin, 
         transitoryTimeMax, 
         influence.getPrey()
      );
      remainingInfluences.add( newInfluence );
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
   public SimulationTimeStamp getNextTime(SimulationTimeStamp currentTime) {
      throw new UnsupportedOperationException(
         "This method is out of the scope of this example."
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
      throw new UnsupportedOperationException(
         "This method is out of the scope of this example."
      );
   }
}
