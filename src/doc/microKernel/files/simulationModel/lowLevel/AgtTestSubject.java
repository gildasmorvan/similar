package fr.lgi2a.underpressure.model.agents.testsubject;

import java.util.Map;

import fr.lgi2a.similar.microkernel.IDynamicStateMap;
import fr.lgi2a.similar.microkernel.IGlobalMemoryState;
import fr.lgi2a.similar.microkernel.IPerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.InfluencesMap;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractAgent;
import fr.lgi2a.underpressure.model.agents.SimulationAgentCategories;
import fr.lgi2a.underpressure.model.agents.testsubject.physical.AgtTestSubjectPDFPhysical;
import fr.lgi2a.underpressure.model.agents.testsubject.social.AgtTestSubjectPDFSocialLevel;
import fr.lgi2a.underpressure.model.levels.SimulationLevels;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;

/**
 * Models an agent from the 'Test subject' category.
 */
public class AgtTestSubject extends AbstractAgent {
   /**
    * Builds a "Test subject" agent without initializing the global state and the public
    * local state of the agent.
    * The setter of these elements have to be called manually.
    * @param initialIndividualNumberStressThreshold The initial value of the 
    * threshold of individuals number in the 'physical' level over which 
    * the agent becomes fully stressed.
    */
   public AgtTestSubject( 
   	int initialIndividualNumberStressThreshold 
   ) {
   	super( SimulationAgentCategories.TEST_SUBJECT );
   	this.individualNumberStressThreshold = initialIndividualNumberStressThreshold;
   }
	
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // // 
   // //   Private local state of the agent. 
   // //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
	
   /**
    * The threshold of individuals number in the 'physical' level over which 
    * the agent becomes fully stressed.
    */
   private final int individualNumberStressThreshold;
	
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // // 
   // //   Global state revision related method of the agent.
   // //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
	
   /**
    * {@inheritDoc}
    */
   @Override
   public void reviseMemory(
           SimulationTimeStamp time,
   	   Map<LevelIdentifier, IPerceivedDataOfAgent> perceivedData,
   	   IGlobalMemoryState globalState
   ) {
      // First cast the global state into the appropriate class.
      AgtTestSubjectGS castedGlobalState = (AgtTestSubjectGS) globalState;
      // Then get the number of individuals from the perceived data from the 
      // 'physical' level
      AgtTestSubjectPDFPhysical physicalPdf = (AgtTestSubjectPDFPhysical) 
   		perceivedData.get( SimulationLevels.PHYSICAL );
      int indivNum = physicalPdf.getIndividualNum();
      // Compute the first part of the stress
      double physicalStress = Math.min( indivNum, this.individualNumberStressThreshold );
      physicalStress /= this.individualNumberStressThreshold;
      // Get the number of social interactions from the perceived data from the 
      // 'social' level
      AgtTestSubjectPDFSocialLevel socialPdf = (AgtTestSubjectPDFSocialLevel) 
      		perceivedData.get( SimulationLevels.SOCIAL
      );
      int interactionsNum = socialPdf.getInteractionNum();
      // Compute the second part of the stress
      double socialStress = 1 - Math.exp( - Math.pow(interactionsNum - 3, 2) / 2 );
      // Finally set the new stress value of the global state.
      castedGlobalState.setStressDegree(
         (
            ( 1 + physicalStress ) * ( 1 + socialStress ) - 1
         ) / 3
      );
   }

	   
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // // 
   // //   Methods that are out of the scope of this example.
   // //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
	
   /**
     * {@inheritDoc}
     */
   @Override
   public IPerceivedDataOfAgent perceive(
      LevelIdentifier level,
      SimulationTimeStamp time,
      IPublicLocalStateOfAgent publicLocalStateInLevel,
      IDynamicStateMap levelsPublicLocalObservableDynamicState
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
	 LevelIdentifier level, 
         SimulationTimeStamp time,
         IGlobalMemoryState memoryState,
         IPerceivedDataOfAgent perceivedData,
         InfluencesMap producedInfluences
   ) {
      throw new UnsupportedOperationException( 
            "This operation currently has no specification." 
      );
   }	
}
