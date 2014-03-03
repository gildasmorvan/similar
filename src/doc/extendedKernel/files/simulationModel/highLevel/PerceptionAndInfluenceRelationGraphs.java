package fr.lgi2a.mysimulation.model.levels;

import fr.lgi2a.similar.extendedkernel.levels.ExtendedLevel;

/**
 * This class models the perception and the influence relation graphs of the simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class PerceptionAndInfluenceRelationGraphs {
   /**
    * Builds the influence and perception relation graph for the specified level.
    * @param level The level for which the perception and influence relation graph is built.
    */
   public static void buildGraphOfLevel(
      ExtendedLevel level
   ){
      if(
         level.getIdentifier().equals( MyAwesomeLevelList.CITY )
      ){
         // By default, the "city" level is perceptible and influenceable
         // by agents in the "city" level.
         level.addInfluenceableLevel( MyAwesomeLevelList.SLUMS );
         level.addInfluenceableLevel( MyAwesomeLevelList.COUNTRY );
         level.addInfluenceableLevel( MyAwesomeLevelList.SEASHORE );
      } else if(
         level.getIdentifier().equals( MyAwesomeLevelList.SLUMS )
      ){
         // By default, the "slumbs" level is perceptible and influenceable
         // by agents in the "slumbs" level.
         level.addPerceptibleLevel( MyAwesomeLevelList.CITY );
         level.addInfluenceableLevel( MyAwesomeLevelList.CITY );
      } else if(
         level.getIdentifier().equals( MyAwesomeLevelList.COUNTRY )
      ){
         // By default, the "country" level is perceptible and influenceable
         // by agents in the "country" level.
         level.addPerceptibleLevel( MyAwesomeLevelList.SLUMS );
         level.addPerceptibleLevel( MyAwesomeLevelList.COUNTRY );
         level.addPerceptibleLevel( MyAwesomeLevelList.SEASHORE );
         level.addInfluenceableLevel( MyAwesomeLevelList.SEASHORE );
      } else if(
         level.getIdentifier().equals( MyAwesomeLevelList.SEASHORE )
      ){
         // By default, the "seashore" level is perceptible and influenceable
         // by agents in the "seashore" level.
         level.addPerceptibleLevel( MyAwesomeLevelList.SLUMS );
         level.addPerceptibleLevel( MyAwesomeLevelList.COUNTRY );
         level.addPerceptibleLevel( MyAwesomeLevelList.SEASHORE );
         level.addInfluenceableLevel( MyAwesomeLevelList.COUNTRY );
      }
   }
}
