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
package fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber;

import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.initializations.BubbleChamberSimulation;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.BubbleChamberParameters;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.bubble.AgtBubbleFactory;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.cannon.AgtCannonFactory;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.particle.AgtParticleFactory;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.probes.ChamberLevelSwingViewer;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.probes.ProbePrintingParticleLocationOverTime;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.tools.RandomValueFactory;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.tools.randomstrategies.JavaRandomBasedValuesGenerator;
import fr.univ_artois.lgi2a.similar.microkernel.libs.engines.EngineMonothreadedDefaultdisambiguation;
import fr.univ_artois.lgi2a.similar.microkernel.libs.probes.ProbeExceptionPrinter;
import fr.univ_artois.lgi2a.similar.microkernel.libs.probes.ProbeExecutionTracker;
import fr.univ_artois.lgi2a.similar.microkernel.libs.probes.ProbeImageSwingJFrame;


/**
 * The main class of the "Bubble chamber" simulation.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 */
public class BubbleChamberMain {
	/**
	 * The main method of the simulation.
	 * @param args The command line arguments.
	 */
	public static void main(
			String[] args
	) {
		// Initialize the random numbers generator.
		RandomValueFactory.setStrategy(
			new JavaRandomBasedValuesGenerator( 10000 )
		);
		// Create the parameters used in this simulation.
		BubbleChamberParameters parameters = new BubbleChamberParameters( );
		// Register the parameters to the agent factories.
		AgtBubbleFactory.setParameters( parameters );
		AgtCannonFactory.setParameters( parameters );
		AgtParticleFactory.setParameters( parameters );
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
				"Particle location",
				new ProbePrintingParticleLocationOverTime( System.out )
		);
		engine.addProbe(
				"Chamber level Swing viewer",
				new ProbeImageSwingJFrame( 
					"Chamber level", 													// The name of the frame
					new ChamberLevelSwingViewer(), 										
					ProbeImageSwingJFrame.ClosingManagementStrategy.ABORT_SIMULATION, 	// The simulation will abort if the frame is closed
					null																// The frame is resized automatically
				)
		);

		// Create the simulation model being used.
		BubbleChamberSimulation simulationModel = new BubbleChamberSimulation(
				new SimulationTimeStamp( 0 ), 
				new SimulationTimeStamp( 1000 ), 
				parameters
		);
		// Run the simulation.
		engine.runNewSimulation( simulationModel );
	}
}
