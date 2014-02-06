package fr.lgi2a.mysimulation.model.agents.citybus;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import fr.lgi2a.mysimulation.model.agents.MyAgentCategoriesList;
import fr.lgi2a.similar.microkernel.AgentCategory;
import fr.lgi2a.similar.microkernel.IAgent;
import fr.lgi2a.similar.microkernel.IDynamicStateMap;
import fr.lgi2a.similar.microkernel.IGlobalMemoryState;
import fr.lgi2a.similar.microkernel.IPerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.InfluencesMap;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;

/**
 * The "City bus" agent.
 */
public class CityBusAgent implements IAgent {
   /**
    * The global memory state of the agent.
    */
   private IGlobalMemoryState globalMemoryState;
   /**
    * The public local state of the agent in the levels where it lies.
    */
   private Map<LevelIdentifier,IPublicLocalStateOfAgent> publicLocalStates;
   /**
    * The last perceived data of the agent.
    */
   private Map<LevelIdentifier, IPerceivedDataOfAgent> lastPerceivedData;
   
   /**
    * Builds a "City bus" agent without initializing the global memory state and the
    * public local states.
    * The setter of these elements have to be called manually.
    */
   public CityBusAgent( ) {
      this.publicLocalStates = new HashMap<LevelIdentifier, IPublicLocalStateOfAgent>();
      this.lastPerceivedData = new HashMap<LevelIdentifier, IPerceivedDataOfAgent>();
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
         IPublicLocalStateOfAgent publicLocalState 
   ){
      if( levelIdentifier == null ){
         throw new IllegalArgumentException( "The arguments cannot be null." );
      } else if( publicLocalState == null ){
         throw new IllegalArgumentException( "The arguments cannot be null." );
      }
      if( ! this.getLevels().contains( levelIdentifier ) ){
         this.publicLocalStates.put( levelIdentifier, publicLocalState );
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
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public IPublicLocalStateOfAgent getPublicLocalState(
         LevelIdentifier levelId
   ) throws NoSuchElementException {
      if( levelId == null ){
         throw new IllegalArgumentException( "The arguments cannot be null." );
      }
      IPublicLocalStateOfAgent result = this.publicLocalStates.get( levelId );
      if( result == null ){
         throw new NoSuchElementException( "The agent does not define a public local " + 
               "state for the level '" + levelId + "'." );
      }
      return result;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public IGlobalMemoryState getGlobalMemoryState() {
      return this.globalMemoryState;
   }

   /**
    * Sets the value of the initial memory state of the agent.
    * @param newState The value of the initial memory state of the agent.
    * This memory state cannot be <code>null</code>.
    */
   public void initializeGlobalMemoryState( IGlobalMemoryState initialMemoryState ) {
      if( initialMemoryState == null ){
         throw new IllegalArgumentException( "The arguments cannot be null." );
      }
      this.globalMemoryState = initialMemoryState;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Map<LevelIdentifier, IPerceivedDataOfAgent> getPerceivedData() {
      return this.lastPerceivedData;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public IPerceivedDataOfAgent getPerceivedData(
         LevelIdentifier levelIdent
   ) throws NoSuchElementException {
      if( levelIdent == null ){
         throw new IllegalArgumentException( "The arguments cannot be null." );
      }
      IPerceivedDataOfAgent result = this.lastPerceivedData.get( levelIdent );
      if( result == null ){
         throw new NoSuchElementException( "No perceived data were defined "+
               "for the level '" + levelIdent + "'." );
      }
      return result;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setPerceivedData(
         LevelIdentifier levelIden,
         IPerceivedDataOfAgent perceivedData
   ) {
      if( levelIden == null ){
         throw new IllegalArgumentException( "The arguments cannot be null." );
      } else if( perceivedData == null ){
         throw new IllegalArgumentException( "The arguments cannot be null." );
      }
      this.lastPerceivedData.put( levelIden, perceivedData );
   }
   
   
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
   // // 
   // //   The remaining methods are managed in a later step of the simulation 
   // //   design process. 
   // //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
   

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
   public void reviseMemory(
         SimulationTimeStamp time,
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
