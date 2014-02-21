package fr.lgi2a.similar.microkernel.examples.bubblechamber.initializations;

import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.libs.engines.EngineMonothreadedDefaultdisambiguation;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeExceptionPrinter;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeExecutionTracker;

import fr.lgi2a.similar.microkernel.examples.bubblechamber.initializations.BubbleChamberSimulation;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.BubbleChamberParameters;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.bubble.AgtBubbleFactory;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.cannon.AgtCannonFactory;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.particle.AgtParticleFactory;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.probes.ProbePrintingParticleLocationOverTime;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.tools.RandomValueFactory;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.tools.randomstrategies.JavaRandomBasedValuesGenerator;

/**
 * Runs a bubble chamber simulation.
 */
public class BubbleChamberMain {
   /**
    * The main method of this java class.
    */
   public static void main(String[] args) {
      // 1 - Create an instance of the simulation parameters class, 
      // and set the value of its content;
      BubbleChamberParameters parameters = createParameters( );
      // 2 - Initialize the random number generator, using a seed 
      // declared in the simulation parameters;
      RandomValueFactory.setStrategy(
         new JavaRandomBasedValuesGenerator( parameters.randomSeed )
      );
      // 3 - Bind the simulation parameters with the agent factories 
      // being used in the simulation;
      bindFactoriesWithParameters( parameters );
      // 4 - Create an instance of the simulation model initialization 
      // profile that will be used to run the simulation;
      BubbleChamberSimulation simulationModel = new BubbleChamberSimulation(
         parameters
      );
      // 5 - Create an instance of the simulation engine that will 
      // run the simulation;
      ISimulationEngine engine = new EngineMonothreadedDefaultdisambiguation( );
      // 6 - Create the instance of each observation probe used 
      // in the simulation;
      // 7 - Bind the probes to the simulation engine;
      createAndBindProbes( engine );
      // 8 - Run a simulation on the created engine using the simulation 
      // model initialization profile.
      engine.runNewSimulation( simulationModel );
   }
   
   /**
    * Creates the parameters used in the simulation.
    * @return The initialized parameters used in the simulation.
    */
   private static BubbleChamberParameters createParameters( ) {
      BubbleChamberParameters result = new BubbleChamberParameters( );
      result.initialTime = new SimulationTimeStamp( 0 );
      result.finalTime = new SimulationTimeStamp( 500 );
      result.randomSeed = 10000;
      return result;
   }
   
   /**
    * Binds the parameters class with the agent factories used in
    * the simulation.
    * @param parameters The simulation parameters class being used.
    */
   private static void bindFactoriesWithParameters(
      BubbleChamberParameters parameters
   ){
      AgtBubbleFactory.setParameters( parameters );
      AgtCannonFactory.setParameters( parameters );
      AgtParticleFactory.setParameters( parameters );
   }
   
   /**
    * Creates the probes used in the simulation and binds them
    * to the simulation engine.
    * @param engine 
    */
   private static void createAndBindProbes(
      ISimulationEngine engine
   ){
      engine.addProbe( 
            "Error printer", 
            new ProbeExceptionPrinter( )
      );
      engine.addProbe(
            "Trace printer", 
            new ProbeExecutionTracker( System.err, false )
      );
      engine.addProbe(
            "Particle location",
            new ProbePrintingParticleLocationOverTime( )
      );
   }
}