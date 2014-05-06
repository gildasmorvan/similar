package fr.lgi2a.similar.extendedkernel.examples.lambdaenergylife;

import java.awt.Color;

import fr.lgi2a.similar.extendedkernel.examples.lambdaenergylife.initializations.LambdaEnergyLifeSimulationModel;
import fr.lgi2a.similar.extendedkernel.examples.lambdaenergylife.model.LambdaEnergyLifeParameters;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.agents.cell.AgtCellFactory;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.probes.LambdaGameOfLifeHistorySwingView;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.probes.MacroStateProbe;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.tools.RandomValueFactory;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.tools.randomstrategies.SecureRandomBasedRandomValuesGenerator;
import fr.lgi2a.similar.extendedkernel.simulationmodel.AbstractExtendedSimulationModel;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.libs.engines.EngineMonothreadedDefaultdisambiguation;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeExceptionPrinter;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeExecutionTracker;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeImageSwingJFrame;

public class LambdaEnergyMain {
	
	/**
	 * The main java method being run.
	 * @param args The command line arguments.
	 */
	/**
	 * @param args
	 */
	public static void main(
			String[] args
	) {
		// Create the parameters used in this simulation.
		LambdaEnergyLifeParameters parameters = new LambdaEnergyLifeParameters( );
		parameters.gridWidth = 23;
		parameters.gridHeight = 23;
		parameters.stillLifeThreshold = 511;
		parameters.energy = 3.0/(parameters.gridWidth*parameters.gridWidth);
		parameters.xTorus = false;
		parameters.yTorus = false;
		parameters.lambda = 0.5;
		parameters.finalTime = new SimulationTimeStamp(100000);
		// Initialize the random numbers generator.
		
		RandomValueFactory.setStrategy(
		//	new JavaRandomBasedRandomValuesGenerator( parameters.seed )
				new SecureRandomBasedRandomValuesGenerator( parameters.seed)
		);
		// Register the parameters to the agent factories.
		AgtCellFactory.setParameters( parameters );
	
		// Create the simulation engine that will run simulations
		ISimulationEngine engine = new EngineMonothreadedDefaultdisambiguation( );
		// Create the probes that will listen to the execution of the simulation.
		engine.addProbe( 
				"Error printer", 
				new ProbeExceptionPrinter( )
		);
		engine.addProbe(
				"Trace printer", 
				new ProbeExecutionTracker( System.err, false )
		);
		engine.addProbe(
				"Macro state",
				new MacroStateProbe( System.out )
		);
		
		engine.addProbe(
				"Evolution displayer", 
				new ProbeImageSwingJFrame( 
					"Game of life evolution", 											// The name of the frame
					new LambdaGameOfLifeHistorySwingView( Color.WHITE, parameters.gridWidth, parameters.gridHeight, parameters.initialTime), 						// The background color of the frame
					//new LambdaGameOfLifeSwingView( Color.WHITE ), 
					ProbeImageSwingJFrame.ClosingManagementStrategy.ABORT_SIMULATION, 	// The simulation will abort if the frame is closed
					null																// The frame is resized automatically
				)
		);
//		engine.addProbe(
//			"Exporting as image", 
//			new LambdaGameOfLifeLastStateExporter( "SimulationFinalState.png" )
//		);
		// Create the simulation model being used.
		AbstractExtendedSimulationModel simulationModel = new LambdaEnergyLifeSimulationModel(
				parameters
		);
		// Run the simulation.
		engine.runNewSimulation( simulationModel );
	}

}
