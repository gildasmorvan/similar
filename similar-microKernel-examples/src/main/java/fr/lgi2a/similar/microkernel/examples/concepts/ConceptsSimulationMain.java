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
package fr.lgi2a.similar.microkernel.examples.concepts;

import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.ISimulationModel;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.editorinchief.AgtFactoryEditorInChief;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.TimeOfTheDay;
import fr.lgi2a.similar.microkernel.examples.concepts.probes.ProbeAliensNumber;
import fr.lgi2a.similar.microkernel.examples.concepts.probes.ProbeCitizenParanoia;
import fr.lgi2a.similar.microkernel.examples.concepts.probes.ProbeExperimentsNumber;
import fr.lgi2a.similar.microkernel.examples.concepts.probes.ProbePostsNumber;
import fr.lgi2a.similar.microkernel.examples.concepts.probes.ProbeTVBroadcastedValue;
import fr.lgi2a.similar.microkernel.libs.engines.MonoThreadedDefaultDisambiguationSimulationEngine;
import fr.lgi2a.similar.microkernel.libs.probes.ProbeExceptionPrinter;

/**
 * The main class of the simulation, building and running the simulation.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class ConceptsSimulationMain {
	/**
	 * Set the constructor of this class as not accessible.
	 */
	protected ConceptsSimulationMain( ) { }

	/**
	 * The main method called when the java program is performed.
	 * @param args The command line arguments of the main method.
	 * In this simulation, no arguments are used.
	 */
	public static void main(String[] args) {
		// The execution of a simulation is a 4-step process:
		//
		// 1 - Create the simulation engine where the simulations are performed.
		// A simulation engine is an object embedding all the procedural features moving the simulation
		// through time until the final time is reached. This engine has various implementations,
		// using more or less efficient and more or less simple algorithms and hardware features (e.g.
		// using mono threaded implementations, multi-core implementations, GPGPU, etc.).
		ISimulationEngine engine = new MonoThreadedDefaultDisambiguationSimulationEngine( );
		//
		// 2 - Create the model of the simulation to perform.
		//
		// Start the simulation the daytime following the time stamp with the identifier -1.
		SimulationTimeStamp initialTime = ConceptsSimulationTimeInterpretationModel.getInstance().getNext( new SimulationTimeStamp( -1 ), TimeOfTheDay.DAY );
		// End the simulation when it reaches the daytime of the 90th day following the start of the simulation.
		SimulationTimeStamp finalTime = new SimulationTimeStamp( initialTime.getIdentifier() + 90 * TimeOfTheDay.values().length );
		// Create the parameters used in the simulation model
		// In this simulation, we will use the default parameters defined in the constructor with no arguments.
		ConceptsSimulationParameters parameters = new ConceptsSimulationParameters( );
		// Link these parameters to the agent factories, to identify the default value they can use
		AgtFactoryEditorInChief.instance().setParameters( parameters );
		// Create the simulation model.
		ISimulationModel model = new ConceptsSimulationModel(
				initialTime, 
				finalTime, 
				parameters
		);
		//
		// 3 - Create the probes observing the results of the simulation.
		//
		// Add a probe from the common libs. This probe displays an error on the standard error output if an exception
		// is thrown during the execution of the simulation.
		engine.addProbe( "Error printer", new ProbeExceptionPrinter( ) );
		// Add a probe defined for this simulation to print the average paranoia level of the citizen.
		engine.addProbe( "Average paranoia", new ProbeCitizenParanoia( ) );
		// Add a probe defined for this simulation to print the threshold being broadcasted by the TV.
		engine.addProbe( "TV broadcast", new ProbeTVBroadcastedValue( ) );
		// Add a probe defined for this simulation to print the number of aliens in the simulation.
		engine.addProbe( "Aliens number", new ProbeAliensNumber( ) );
		// Add a probe defined for this simulation to print the number of experiments currently being performed in the simulation.
		engine.addProbe( "Experiments number", new ProbeExperimentsNumber( ) );
		// Add a probe defined for this simulation to print the number of posts on the Internet.
		engine.addProbe( "Posts number", new ProbePostsNumber( ) );
		//
		// 4 - Run the simulation
		engine.runNewSimulation( model );
	}
}
