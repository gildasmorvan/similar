package fr.univ_artois.lgi2a.mysimulation.model.environment;

import java.util.Map;

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.univ_artois.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractEnvironment;

/**
 * The model of the environment in the simulation.
 */
public class EnvironmentSkeleton2 extends AbstractEnvironment {   
   /**
    * Builds an environment for a simulation containing no levels.
    * Levels are then added using the 
    * {@link EnvironmentSkeleton2#includeNewLevel(LevelIdentifier, ILocalStateOfEnvironment, ILocalStateOfEnvironment)} 
    * method.
    */
   public EnvironmentSkeleton2( ) {
	   super( );
      // Does nothing specific.
   }
   
   
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
   // //                                //  //
   // //   The remaining methods are managed in a later step of the   //  //
   // //   simulation design process.                //  //
   // //                                //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
   

   /**
    * {@inheritDoc}
    */
   @Override
   public void natural(
      LevelIdentifier level,
      SimulationTimeStamp timeLowerBound,
      SimulationTimeStamp timeUpperBound,
      Map<LevelIdentifier, ILocalStateOfEnvironment> publicLocalStates,
      ILocalStateOfEnvironment privateLocalState,
      IPublicDynamicStateMap dynamicStates,
      InfluencesMap producedInfluences
   ) {
      throw new UnsupportedOperationException( 
         "This operation currently has no specification." 
      );
   }
}
