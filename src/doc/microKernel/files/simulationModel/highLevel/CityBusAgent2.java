package fr.lgi2a.mysimulation.model.agents.citybus;

import java.util.Map;

import fr.lgi2a.mysimulation.model.agents.MyAgentCategoriesList;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.libs.abstractimpl.AbstractAgent;

/**
 * The "City bus" agent.
 */
public class CityBusAgent2 extends AbstractAgent {
  /**
    * Builds a "City bus" agent without initializing the global state, the
    * public and private local states.
    * The setter of these elements have to be called manually.
    */
   public CityBusAgent2( ) {
       super( 
          MyAgentCategoriesList.CITY_BUS 
       );
   }
   
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //
   // // 
   // //   The remaining methods are managed in a later step of the simulation 
   // //   design process. 
   // //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //

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
   ) {
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
