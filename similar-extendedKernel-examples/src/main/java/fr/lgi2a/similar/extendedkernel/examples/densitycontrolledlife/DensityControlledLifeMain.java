/**
 * Copyright or © or Copr. LGI2A
 * 
 * LGI2A - Laboratoire de Genie Informatique et d'Automatique de l'Artois - EA 3926 
 * Faculte des Sciences Appliquees
 * Technoparc Futura
 * 62400 - BETHUNE Cedex
 * http://www.lgi2a.univ-artois.fr/
 * 
 * Email: gildas.morvan@univ-artois.fr
 * 
 * Contributors:
 * 	Gildas MORVAN (creator of the IRM4MLS formalism)
 * 	Yoann KUBERA (designer, architect and developer of SIMILAR)
 * 
 * This software is a computer program whose purpose is to support the
 * implementation of multi-agent-based simulations using the formerly named
 * IRM4MLS meta-model. This software defines an API to implement such 
 * simulations, and also provides usage examples.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.univ_artois.lgi2a.similar.extendedkernel.examples.densitycontrolledlife;

import java.awt.Color;

import fr.univ_artois.lgi2a.similar.extendedkernel.examples.densitycontrolledlife.initializations.DensityControlledLifeSimulationModel;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.densitycontrolledlife.model.DensityControlledLifeParameters;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.densitycontrolledlife.model.agents.cellcluster.AgtCellClusterFactory;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife.model.agents.cell.AgtCellFactory;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife.probes.LambdaGameOfLifeSwingView;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife.probes.MacroStateProbe;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife.tools.RandomValueFactory;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife.tools.randomstrategies.SecureRandomBasedRandomValuesGenerator;
import fr.univ_artois.lgi2a.similar.extendedkernel.simulationmodel.AbstractExtendedSimulationModel;
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.libs.engines.EngineMonothreadedDefaultdisambiguation;
import fr.univ_artois.lgi2a.similar.microkernel.libs.probes.ProbeExceptionPrinter;
import fr.univ_artois.lgi2a.similar.microkernel.libs.probes.ProbeExecutionTracker;
import fr.univ_artois.lgi2a.similar.microkernel.libs.probes.ProbeImageSwingJFrame;

/**
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class DensityControlledLifeMain {

	/**
	 * The main java method being run.
	 * @param args The command line arguments.
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create the parameters used in this simulation.
		DensityControlledLifeParameters parameters = new DensityControlledLifeParameters( );
		parameters.gridWidth = 60;
		parameters.gridHeight = 60;
		parameters.stillLifeThreshold = 60;
		parameters.expectedDensity = 0.08;
		parameters.kP = 10*parameters.expectedDensity;
		parameters.xlength = 30;
		parameters.ylength = 30;
		parameters.finalTime = new SimulationTimeStamp(10000);
		
		// Initialize the random numbers generator
		RandomValueFactory.setStrategy(
		//	new JavaRandomBasedRandomValuesGenerator( parameters.seed )
				new SecureRandomBasedRandomValuesGenerator( parameters.seed)
		);
		// Register the parameters to the agent factories.
		AgtCellFactory.setParameters( parameters );
		AgtCellClusterFactory.setParameters( parameters );
		
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
				"Macro State",
				new MacroStateProbe( System.out )
		);
		
		engine.addProbe(
				"Evolution displayer", 
				new ProbeImageSwingJFrame( 
					"Game of life evolution", 											// The name of the frame
					new LambdaGameOfLifeSwingView( Color.WHITE ), 						// The background color of the frame
					ProbeImageSwingJFrame.ClosingManagementStrategy.ABORT_SIMULATION, 	// The simulation will abort if the frame is closed
					null																// The frame is resized automatically
				)
		);
		
		// Create the simulation model being used.
		AbstractExtendedSimulationModel simulationModel = new DensityControlledLifeSimulationModel(
				parameters
		);
		// Run the simulation.
		engine.runNewSimulation( simulationModel );

	}

}
