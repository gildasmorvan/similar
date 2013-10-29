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

import java.util.LinkedList;
import java.util.List;

import fr.lgi2a.similar.microKernel.examples.oneLevelTwoAgentsTrace.agents.ActorAgent;
import fr.lgi2a.similar.microKernel.examples.oneLevelTwoAgentsTrace.agents.ProcrastinatorAgent;
import fr.lgi2a.similar.microKernel.examples.oneLevelTwoAgentsTrace.environment.MyEnvironment;
import fr.lgi2a.similar.microKernel.examples.oneLevelTwoAgentsTrace.levels.MyLevel1;
import fr.lgi2a.similar.microKernel.libs.tools.learning.LearningSimilar_SimulationModel;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.Learning_AbstractAgent;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.Learning_AbstractEnvironment;
import fr.lgi2a.similar.microKernel.libs.tools.learning.model.Learning_Level;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.SimulationExecutionTrace;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;

/**
 * Models the simulation model of the "one level - two agents - trace" simulation.
 * <h1>Constraints</h1>
 * <p>
 * 	The simulation model is an instance of the {@link LearningSimilar_SimulationModel} class to ensure that the evolution of the environment 
 * 	can be tracked by the trace of the simulation.
 * </p>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class MySimulationModel extends LearningSimilar_SimulationModel {
	/**
	 * Builds an initialized "one level - two agents - trace" simulation.
	 * @param initialTime Defines the initial time stamp of the simulation.
	 * @param finalTime Defines the final time stamp of the simulation. No decision or natural action occur starting from this time stamp.
	 */
	public MySimulationModel(
			SimulationTimeStamp initialTime,
			SimulationTimeStamp finalTime
	) {
		super(initialTime, finalTime);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<Learning_Level> generateCastedLevels(
			SimulationTimeStamp initialTime, 
			SimulationExecutionTrace trace
	) {
		// Create the levels where the simulation takes place.
		//
		// Create the list containing all the levels of the simulation.
		List<Learning_Level> levelsList = new LinkedList<Learning_Level>();
		// Create the levels of the simulation
		MyLevel1 level = new MyLevel1( initialTime, trace );
		levelsList.add( level );
		//
		// Return the list of levels of the simulation.
		//
		return levelsList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Learning_AbstractEnvironment createEnvironment(
			SimulationExecutionTrace trace
	) {
		return new MyEnvironment( trace );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<Learning_AbstractAgent> createAgents(
			SimulationExecutionTrace trace
	) {
		// Create the agents initially lying in the simulation.
		//
		// Create the list containing all the agents of the simulation.
		List<Learning_AbstractAgent> agentsList = new LinkedList<Learning_AbstractAgent>( );
		// Create the agent of the 'actor' category.
		ActorAgent actor = new ActorAgent( trace );
		agentsList.add( actor );
		// Create the agent of the 'procrastinator' category.
		ProcrastinatorAgent procrastinator = new ProcrastinatorAgent( trace );
		agentsList.add( procrastinator );
		//
		// Return the list of initial agents of the simulation.
		//
		return agentsList;
	}

}
