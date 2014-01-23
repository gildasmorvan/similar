package fr.lgi2a.mysimulation.model.agents.citybus;

import java.util.Map;

import fr.lgi2a.mysimulation.model.agents.MyAgentCategoriesList;
import fr.lgi2a.similar.microkernel.IDynamicStateMap;
import fr.lgi2a.similar.microkernel.IGlobalMemoryState;
import fr.lgi2a.similar.microkernel.IPerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.InfluencesMap;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractAgent;

/**
 * The "City bus" agent.
 */
public class CityBusAgent2 extends AbstractAgent {
  /**
    * Builds a "City bus" agent without initializing the global memory state and the
    * public local states.
    * The setter of these elements have to be called manually.
    */
   public CityBusAgent2( ) {
       super( MyAgentCategoriesList.CITY_BUS );
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
   public IPerceivedDataOfAgent perceive(
         LevelIdentifier level,
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
   public void reviseMemory(
         Map<LevelIdentifier, IPerceivedDataOfAgent> perceivedData,
         IGlobalMemoryState memoryState
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
         IGlobalMemoryState memoryState,
         IPerceivedDataOfAgent perceivedData,
         InfluencesMap producedInfluences
   ) {
      throw new UnsupportedOperationException( 
            "This operation currently has no specification." 
      );
   }
}
