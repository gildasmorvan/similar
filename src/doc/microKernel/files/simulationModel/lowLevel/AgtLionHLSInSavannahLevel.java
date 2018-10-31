package fr.univ_artois.lgi2a.wildlifesimulation.model.agents.lion.savannah;

import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractLocalStateOfAgent;
import fr.univ_artois.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;

/**
 * The private local state of an agent from the "Lion" category in the 
  * level identified by "Savannah".
 */
public class AgtLionHLSInSavannahLevel extends AbstractLocalStateOfAgent {
   /**
    * Builds an initialized instance of this private local state.
    * @param owner The agent owning this public local state. This value cannot 
    * be <code>null</code>.
    * @throws IllegalArgumentException If an argument had an inappropriate value.
    */
   public AgtLionHLSInSavannahLevel(
      IAgent4Engine owner,
      int initialGazellePerceptionRadius,
      long initialGazelleAgeThreshold
   ){
      super(
         WildlifeLevelList.SAVANNAH,
         owner 
      );
      if( initialGazellePerceptionRadius < 0 ){
         throw new IllegalArgumentException(
            "The perception radius of Gazelles cannot be negative."
         );
      }
      this.gazelleAgeThreshold = initialGazelleAgeThreshold;
      this.gazellePerceptionRadius = initialGazellePerceptionRadius;
   }

   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //
   // // 
   // //   Declaration of the non-perceptible data of the agent
   // //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //
   
   /**
    * The perception radius where the gazelles can be seen.
    */
   private int gazellePerceptionRadius;
   
   /**
    * Gets the perception radius (in meters) where the gazelles can be seen.
    * @return The perception radius where the gazelles can be seen.
    */
   public int getGazellePerceptionRadius( ) {
      return this.gazellePerceptionRadius;
   }
   
   /**
    * The age threshold under which gazelles are preyed on.
    */
   private long gazelleAgeThreshold;
   
   /**
    * Gets the age threshold (in years) under which gazelles are preyed on.
    * @return The age threshold under which gazelles are preyed on.
    */
   public long getGazelleAgeThreshold( ){
      return this.gazelleAgeThreshold;
   }
}
