package fr.lgi2a.mysimulation.model.environment;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.lgi2a.similar.microkernel.environment.IEnvironment4Engine;
import fr.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;

/**
 * The model of the environment in the simulation.
 */
public class EnvironmentSkeleton implements IEnvironment4Engine {
   /**
    * The public local states of the environment in the various levels of the 
    * simulation.
    */
   private Map<LevelIdentifier,ILocalStateOfEnvironment> publicLocalStates;
   /**
    * The private local states of the environment in the various levels of the 
    * simulation.
    */
   private Map<LevelIdentifier,ILocalStateOfEnvironment> privateLocalStates;
   
   /**
    * Builds an environment for a simulation containing no levels.
    * Levels are then added using the 
    * {@link EnvironmentSkeleton#includeNewLevel(LevelIdentifier, IPublicLocalState)} 
    * method.
    */
   public EnvironmentSkeleton( ) {
      this.publicLocalStates = new LinkedHashMap<LevelIdentifier,ILocalStateOfEnvironment>();
      this.privateLocalStates = new LinkedHashMap<LevelIdentifier,ILocalStateOfEnvironment>();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ILocalStateOfEnvironment getPublicLocalState( 
      LevelIdentifier level 
   ) throws NoSuchElementException {
      ILocalStateOfEnvironment result = this.publicLocalStates.get( level );
      if( result == null ){
         throw new NoSuchElementException( 
            "No public local state is defined in the environment " +
            "for the level '" +   level + "'." 
         );
      }
      return result;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Map<LevelIdentifier, ILocalStateOfEnvironment> getPublicLocalStates() {
      return this.publicLocalStates;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ILocalStateOfEnvironment getPrivateLocalState(
      LevelIdentifier level 
   ) throws NoSuchElementException {
      ILocalStateOfEnvironment result = this.privateLocalStates.get( level );
      if( result == null ){
         throw new NoSuchElementException( 
            "No private local state is defined in the environment " +
            "for the level '" +   level + "'." 
         );
      }
      return result;
   }
   
   /**
    * Introduces the level-related data of the environment for a new level.
    * @param level The level for which data are added.
    * @param publicLocalState The public local state of the environment for 
    * that level.
    * @param privateLocalState The private local state of the environment for 
    * that level.
    * @throws IllegalArgumentException If an argument is <code>null</code>, 
    * or if the level is already present in the environment.
    */
   public void includeNewLevel( 
      LevelIdentifier level, 
      ILocalStateOfEnvironment publicLocalState , 
      ILocalStateOfEnvironment privateLocalState 
   ){
      if( level == null || publicLocalState == null){
         throw new IllegalArgumentException( "An argument is null." );
      } else if( this.publicLocalStates.containsKey( level ) ){
         throw new IllegalArgumentException( 
            "The level '" + level + "' is already defined " + 
            "for this environment." 
         );
      }
      this.publicLocalStates.put( level, publicLocalState );
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
