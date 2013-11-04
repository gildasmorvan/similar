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
package fr.lgi2a.similar.microkernel.examples.concepts.probes;

import java.util.Set;

import fr.lgi2a.similar.microkernel.IAgent;
import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.alien.AgtAlien;

/**
 * This observation probe displays the number of aliens in the simulation.
 * 
 * <h1>Probes in the SIMILAR API suite.</h1>
 * <p>
 * 	A probe is an object observing the dynamic state of the simulation during a consistent (or half-consistent) state
 * 	of the simulation. It processes and displays the data of the simulation. The observation can be made about everything in the simulation:
 * 	public local states, private local states, global memory states, etc.
 * </p>
 * <p>
 * 	In the micro-kernel of SIMILAR, a probe is implemented as an 
 * 	instance of the {@link IProbe} interface.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class ProbeAliensNumber implements IProbe {
	/**
	 * To facilitate the identification of the source of on screen text, we prefix in this example
	 * the id of the probe to all the messages it prints.
	 */
	public static final String PROBEID = "[Probe:Alien Nb] ";
	
	/**
	 * This observation method is called before the initialization of a new simulation.
	 * It is used to prepare the observation, for instance by showing a GUI Frame, opening a stream towards a file, etc.
	 */
	@Override
	public void prepareObservation() {
		System.out.println( PROBEID + "Creating a new simulation." );
	}

	/**
	 * This method is called after the initialization of a new simulation, but before the execution of the simulation.
	 * It is used to display the initial data of the simulation.
	 */
	@Override
	public void observeAtInitialTimes(
			SimulationTimeStamp initialTimestamp,
			ISimulationEngine simulationEngine
	) {
		System.out.println( PROBEID + "Initial " + initialTimestamp + ":\t" + this.getAlienNumberMessage( simulationEngine.getAgents() ) );
	}
	
	/**
	 * Converts the list of agents of the simulation into a string displaying information about 
	 * the number of aliens.
	 * @param agentsOfSimulation The agents currently lying in the simulation.
	 * @return A string displaying information about the number of aliens.
	 */
	private String getAlienNumberMessage( 
			Set<IAgent> agentsOfSimulation
	) {
		int alienNumber = 0;
		for( IAgent agent : agentsOfSimulation ){
			if( AgtAlien.CATEGORY.equals( agent.getCategory() ) ){
				alienNumber++;
			}
		}
		return "Number of aliens: " + alienNumber;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtPartialConsistentTime(
			SimulationTimeStamp timestamp,
			ISimulationEngine simulationEngine
	) {
		System.out.println( PROBEID + timestamp + ":\t" + this.getAlienNumberMessage( simulationEngine.getAgents() ) );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtFinalTime(
			SimulationTimeStamp finalTimestamp,
			ISimulationEngine simulationEngine
	) {
		System.out.println( PROBEID  + "Final " + finalTimestamp + ":\t" + this.getAlienNumberMessage( simulationEngine.getAgents() ) );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToError(
			String errorMessage, 
			Throwable cause
	) {
		// Does nothing in this probe.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToAbortion(
			SimulationTimeStamp timestamp,
			ISimulationEngine simulationEngine
	) {
		System.out.println( PROBEID  + " Aborted at " + timestamp + " where:\t" + this.getAlienNumberMessage( simulationEngine.getAgents() ) );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endObservation() {
		System.out.println( PROBEID + "Simulation ended" );
	}
}
