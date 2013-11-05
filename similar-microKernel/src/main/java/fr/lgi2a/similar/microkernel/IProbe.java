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
package fr.lgi2a.similar.microkernel;


/**
 * Models an observation probe extracting data from the simulation when at least one level
 * becomes consistent.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public interface IProbe {
	/**
	 * Prepares the observation of a simulation.
	 * <p>
	 * 	This method is defined to open the streams or the other resources used during the observation of one simulation.
	 * </p>
	 */
	void prepareObservation( );
	
	/**
	 * Observes the state of the simulation when the initial time stamp is reached.
	 * @param initialTimestamp The initial time stamp of the simulation.
	 * @param simulationEngine The simulation engine embedding the currently running simulation and the current 
	 * dynamic state of the various levels of the simulation.
	 */
	void observeAtInitialTimes(
			SimulationTimeStamp initialTimestamp,
			ISimulationEngine simulationEngine
	);
	
	/**
	 * Observes the state of the simulation when at least one level is in a consistent state.
	 * @param timestamp The time stamp for which at least one level of the simulation is a consistent state.
	 * @param simulationEngine The simulation engine embedding the currently running simulation and the current 
	 * dynamic state of the various levels of the simulation.
	 */
	void observeAtPartialConsistentTime(
			SimulationTimeStamp timestamp,
			ISimulationEngine simulationEngine
	);
	
	/**
	 * Observes the state of the simulation when the final time stamp of the simulation is reached.
	 * @param finalTimestamp The final time stamp of the simulation.
	 * @param simulationEngine The simulation engine embedding the currently running simulation and the current 
	 * dynamic state of the various levels of the simulation.
	 */
	void observeAtFinalTime(
			SimulationTimeStamp finalTimestamp,
			ISimulationEngine simulationEngine
	);
	
	/**
	 * Reacts to an error thrown by the simulation engine.
	 * @param cause The cause of the error.
	 */
	void reactToError(
			String errorMessage,
			Throwable cause
	);
	
	/**
	 * Reacts to the abortion of a currently running simulation.
	 * @param timestamp The time stamp reached right after the moment when the abortion was requested.
	 * @param simulationEngine The simulation engine embedding the aborted simulation and the  
	 * dynamic state of the various levels of the simulation after abortion.
	 */
	void reactToAbortion( 
			SimulationTimeStamp timestamp,
			ISimulationEngine simulationEngine
	);
	/**
	 * Ends the observation of a simulation.
	 * <p>
	 * 	This method is defined to close the streams or the other resources used during the observation of one simulation.
	 * </p>
	 */
	void endObservation( );
}
