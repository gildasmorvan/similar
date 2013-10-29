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
package fr.lgi2a.similar.microKernel.examples.oneLevelTwoAgentsTrace.levels;

import fr.lgi2a.similar.microKernel.examples.oneLevelTwoAgentsTrace.MyLevelIdentifiers;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.Learning_AbstractEnvironment;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.Learning_Level;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.SimulationExecutionTrace;

/**
 * Models the level 'level 1' as described in the specification of the "one level - two agents - trace" simulation.
 * <h1>Constraints</h1>
 * <p>
 * 	The level is an instance of the {@link Learning_AbstractEnvironment} class to ensure that the evolution of the environment 
 * 	can be tracked by the trace of the simulation.
 * </p>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class MyLevel1 extends Learning_Level {
	/**
	 * Builds a partially initialized instance of this class.
	 * The definition of the public local state of the environment is later defined automatically by the simulation model.
	 * @param initialTime The initial time of the simulation. This value is provided by the simulation model.
	 * @param trace The trace where the execution of the simulation is tracked.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public MyLevel1(
			SimulationTimeStamp initialTime, 
			SimulationExecutionTrace trace
	) throws IllegalArgumentException {
		super( initialTime, MyLevelIdentifiers.SIMULATION_LEVEL, trace );
		//
		// Define the perception relation graph of the level.
		//
		// In a mono-level simulation, no instruction is necessary.
		// Otherwise, calls to the addPerceptibleLevel(...) method have to be made.
		//
		// Define the influence relation graph of the level.
		//
		// In a mono-level simulation, no instruction is necessary.
		// Otherwise, calls to the addInfluenceableLevel(...) method have to be made.
		//
	}

	/**
	 * Defines how time evolves in this level.
	 * <p>
	 * 	In this case, we use a simple model where the identifier of the next time is equal to the identifier of the previous
	 * 	time plus one.
	 * </p>
	 */
	@Override
	public SimulationTimeStamp getNextTime( SimulationTimeStamp currentTime ) {
		return new SimulationTimeStamp( currentTime.getIdentifier() + 1 );
	}
}
