package fr.lgi2a.mysimulation.model.agents.citybus;

import fr.lgi2a.mysimulation.model.MyParametersClass;

/**
 * A factory method generating 'City bus' agents. 
 * Agents are generated using both arguments from the parameters class 
 * and context-dependent parameters.
 * 
 * This class can only be used if it has been initialized with a call to the 
 * <code>setParameters(...)</code> method.
 */
public class CityBusFactoryUsingDoubleCtx {
   /**
    * The instance of this singleton class. Use this field to call the generation 
    * methods of this factory.
    */
   private static final CityBusFactoryUsingDoubleCtx INSTANCE = 
                                                   new CityBusFactoryUsingDoubleCtx( );
   
   /**
    * Gets the instance of this singleton class. 
    * Use this field to call the generation methods of this factory.
    * @return The instance of this singleton class. 
    */
   public static CityBusFactoryUsingDoubleCtx instance( ) {
      return INSTANCE;
   }
   
   /**
    * The parameters class currently used in the simulation.
    */
   private MyParametersClass parameters;
   
   /**
    * Sets the parameters currently used to provide default values for some 
    * private or public local states of the 'City bus' agents with this factory.
    * @param parameters The parameters currently used to generate 'City bus' 
    * agents with this factory.
    */
   public void setParameters( MyParametersClass parameters ) {
      this.parameters = parameters;
   }
   
   /**
    * Generates a "City bus" agent using the ctx context-dependent parameter to
    * initialize the agent with a specific speed in the city level.
    * @param ctx The context dependent parameter used to initialize the agent.
    */
   public CityBusAgent generateAgent( 
         double ctx
   ) {
      if( this.parameters == null ) {
         throw new IllegalStateException( 
                  "The generation parameters have to be set before creating agents!" 
         );
      } else {
         /*
          *  First create the "empty" agent instance. Note that the constructor 
          *  can contain arguments used to initialize the private local state of
          *  the agent. This is not the case in this example.
          */
         CityBusAgent newAgent = new CityBusAgent( );
         /*
          * Then create its initial global memory state. Since this step is 
          * not yet presented, it is specified as a comment:
          */
         // IGlobalMemoryState newMemoryState = ...;
         // newAgent.initializeGlobalMemoryState( newMemoryState );
         /*
          * Then create the public local state of the agent in the levels where 
          * it lies.
          */
         /*
          * Initialize the public local state in the "City" level. Since this step is 
          * not yet presented, it is specified as a comment.
          */
         double doubleCtx = 2 * ctx;
         // IPublicLocalStateOfAgent stateInCityLevel = CONSTRUCTION OF A PUBLIC LOCAL 
         //                                          STATE USING THE VALUE OF doubleCtx;
         // newAgent.includeNewLevel( MyAwesomeLevelList.CITY, stateInCityLevel );
         /*
          * Initialize the public local state in the "Slumbs" level. Since this step is 
          * not yet presented, it is specified as a comment.
          */
         // IPublicLocalStateOfAgent stateInSlumbsLevel = CONSTRUCTION OF A 
         //                                                  PUBLIC LOCAL STATE;
         // newAgent.includeNewLevel( MyAwesomeLevelList.SLUMBS, stateInSlumbsLevel );
         return newAgent;
      }
   }
}
