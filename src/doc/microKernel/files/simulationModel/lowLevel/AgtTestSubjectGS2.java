package fr.univ_artois.lgi2a.underpressure.model.agents.testsubject;

import fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState;

/**
 * The global state of a 'Test subject' agent.
 */
public class AgtTestSubjectGS2 implements IGlobalState {
   /**
    * Builds an initialized instance of the global state.
    * @param owner The agent owning this global state.
    * @param initialStressDegree The initial stress degree of the agent.
    * @param individualNumberStressThreshold The threshold of individuals 
    * number in the 'physical' level over which 
    * the agent becomes fully stressed.
    * @param preferredNumberOfInteractions The optimal number of interactions 
    * the agent can undergo in the social level.
    */
   public AgtTestSubjectGS2(
      double initialStressDegree,
      int individualNumberStressThreshold,
      int preferredNumberOfInteractions
   ){
      this.stressDegree = initialStressDegree;
      this.individualNumberStressThreshold = individualNumberStressThreshold;
      this.preferredNumberOfInteractions = preferredNumberOfInteractions;
   }

   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  // 
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
   // // 
   // //   Agent-specific methods of the global state
   // //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
   
   /**
    * The stress degree of the agent.
    */
   private double stressDegree;
   
   /**
    * Gets the current stress degree of the agent.
    * @return The current stress degree of the agent.
    */
   public double getStressDegree( ){
      return this.stressDegree;
   }
   
   /**
    * Sets the stress degree of the agent to a new value.
    * @param stressDegree The new stress degree of the agent.
    */
   public void setStressDegree( double stressDegree ) {
      this.stressDegree = stressDegree;
   }

   /**
    * The threshold of individuals number in the 'physical' level over which 
    * the agent becomes fully stressed.
    */
   private final int individualNumberStressThreshold;
  
   /**
    * Gets the threshold of individuals number in the 'physical' level over which 
    * the agent becomes fully stressed.
    * @return The threshold of individuals number in the 'physical' level over which 
    * the agent becomes fully stressed.
    */
   public int getIndividualNumberStressThreshold( ){
      return this.individualNumberStressThreshold;
   }
   
   /**
    * The optimal number of interactions the agent can undergo in the social level.
    * This value determines when the agent undergoes the least stress because of interactions.
    */
   private final int preferredNumberOfInteractions;
   
   /**
    * Gets the optimal number of interactions the agent can undergo 
    * in the social level.
    * This value determines when the agent undergoes the least stress 
    * because of interactions.
    * @return The optimal number of interactions the agent can undergo 
    * in the social level.
    */
   public int getPreferredNumberOfInteractions( ){
	   return this.preferredNumberOfInteractions;
   }
}
