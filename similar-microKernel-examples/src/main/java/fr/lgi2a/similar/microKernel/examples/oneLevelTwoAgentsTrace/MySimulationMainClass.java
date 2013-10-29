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
package fr.lgi2a.similar.microKernel.examples.oneLevelTwoAgentsTrace;

import fr.lgi2a.similar.microKernel.libs.simulationEngines.MonoThreaded_DefaultDisambiguation_SimulationEngine;
import fr.lgi2a.similar.microKernel.libs.tools.learning.LearningSimilar_SimulationModel;
import fr.lgi2a.similar.microKernel.libs.tools.learning.Learning_TracePrinter;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.Learning_Probe;
import fr.lgi2a.similar.microkernel.I_SimulationEngine;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;

/**
 * Models the main class of the "one level - two agents - trace" simulation.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class MySimulationMainClass {
	/**
	 * The main method of the "one level - two agents - trace" simulation.
	 * @param args The command line arguments of the program.
	 */
	public static void main( String[] args ) {
		// Create the simulation engine running the simulation.
		I_SimulationEngine engine = new MonoThreaded_DefaultDisambiguation_SimulationEngine( );
		// Define the initial and final time of the simulation.
		SimulationTimeStamp initialTime = new SimulationTimeStamp( 11 );
		SimulationTimeStamp finalTime = new SimulationTimeStamp( 15 );
		// Create the simulation model of the simulation we are running.
		LearningSimilar_SimulationModel model = new MySimulationModel(
				initialTime,
				finalTime
		);
		// Tell the simulation engine that it has to memorize the trace of the dynamic state of the simulation
		// defined by this model. This is necessary to obtain a viable trace of the simulation. Otherwise, only
		// the operations like perception/memory revision/decision/natural and reaction are memorized in the trace.
		engine.addProbe( "Dynamic state tracer", new Learning_Probe( model.getTrace() ) );
		// Run the simulation
		engine.runNewSimulation( model );
		// Then display the trace of the simulation on the standard output.
		// The interpretation of the trace is described in the documentation of the "learning" simulation in the common libs.
		Learning_TracePrinter.printTrace( model.getTrace() );
	}
}
