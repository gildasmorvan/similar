package fr.univ_artois.lgi2a.mysimulation.model.agents.citybus;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import fr.univ_artois.lgi2a.mysimulation.model.agents.MyAgentCategoriesList;
import fr.univ_artois.lgi2a.similar.microkernel.AgentCategory;
import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;

/**
 * The "City bus" agent.
 */
public class CityBusAgent implements IAgent4Engine {
   /**
    * The global state of the agent.
    */
   private IGlobalState globalMemoryState;
   /**
    * The public local state of the agent in the levels where it lies.
    */
   private Map<LevelIdentifier,ILocalStateOfAgent> publicLocalStates;
   /**
    * The private local state of the agent in the levels where it lies.
    */
   private Map<LevelIdentifier,ILocalStateOfAgent> privateLocalStates;
   /**
    * The last perceived data of the agent.
    */
   private Map<LevelIdentifier, IPerceivedData> lastPerceivedData;
   
   /**
    * Builds a "City bus" agent without initializing the global state, the
    * public and private local states.
    * The setter of these elements have to be called manually.
    */
   public CityBusAgent( ) {
      this.publicLocalStates = new LinkedHashMap<LevelIdentifier, ILocalStateOfAgent>();
      this.privateLocalStates = new LinkedHashMap<LevelIdentifier, ILocalStateOfAgent>();
      this.lastPerceivedData = new LinkedHashMap<LevelIdentifier, IPerceivedData>();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public AgentCategory getCategory() {
      return MyAgentCategoriesList.CITY_BUS;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Set<LevelIdentifier> getLevels() {
      return this.publicLocalStates.keySet();
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public void includeNewLevel( 
         LevelIdentifier levelIdentifier, 
         ILocalStateOfAgent publicLocalState, 
         ILocalStateOfAgent privateLocalState 
   ){
      if( 
         levelIdentifier == null || 
         publicLocalState == null || 
         privateLocalState == null 
      ){
         throw new IllegalArgumentException( "The arguments cannot be null." );
      }
      if( ! this.getLevels().contains( levelIdentifier ) ){
          this.publicLocalStates.put( levelIdentifier, publicLocalState );
          this.privateLocalStates.put( levelIdentifier, privateLocalState );
      }
   }

   /**
    * {@inheritDoc}
    */
   public void excludeFromLevel( LevelIdentifier levelIdentifier ) {
      if( levelIdentifier == null ){
         throw new IllegalArgumentException( "The arguments cannot be null." );
      }
      if( this.getLevels().contains( levelIdentifier ) ){
          this.publicLocalStates.remove( levelIdentifier );
          this.privateLocalStates.remove( levelIdentifier );
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ILocalStateOfAgent getPublicLocalState(
         LevelIdentifier levelId
   ) throws NoSuchElementException {
      if( levelId == null ){
         throw new IllegalArgumentException( "The arguments cannot be null." );
      }
      ILocalStateOfAgent result = this.publicLocalStates.get( levelId );
      if( result == null ){
         throw new NoSuchElementException( "The agent does not define a " + 
               "public local state for the level '" + levelId + "'." );
      }
      return result;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Map<LevelIdentifier, ILocalStateOfAgent> getPublicLocalStates() {
      return this.publicLocalStates;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ILocalStateOfAgent getPrivateLocalState(
         LevelIdentifier levelId
   ) throws NoSuchElementException {
      if( levelId == null ){
         throw new IllegalArgumentException( "The arguments cannot be null." );
      }
      ILocalStateOfAgent result = this.privateLocalStates.get( levelId );
      if( result == null ){
         throw new NoSuchElementException( "The agent does not define a " + 
               "private local state for the level '" + levelId + "'." );
      }
      return result;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public IGlobalState getGlobalState() {
      return this.globalMemoryState;
   }

   /**
    * Sets the value of the initial global state of the agent.
    * @param newState The value of the initial global state of the agent.
    * This global state cannot be <code>null</code>.
    */
   public void initializeGlobalState( IGlobalState initialGlobalState ) {
      if( initialGlobalState == null ){
         throw new IllegalArgumentException( "The arguments cannot be null." );
      }
      this.globalMemoryState = initialGlobalState;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Map<LevelIdentifier, IPerceivedData> getPerceivedData() {
      return this.lastPerceivedData;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setPerceivedData(
         IPerceivedData perceivedData
   ) {
      if( perceivedData == null ){
         throw new IllegalArgumentException( "The arguments cannot be null." );
      }
      this.lastPerceivedData.put( perceivedData.getLevel(), perceivedData );
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
