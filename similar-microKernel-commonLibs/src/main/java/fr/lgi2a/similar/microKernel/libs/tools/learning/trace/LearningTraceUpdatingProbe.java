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
package fr.lgi2a.similar.microkernel.libs.tools.learning.trace;

import fr.lgi2a.similar.microkernel.IAgent;
import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.libs.tools.learning.LearningPublicLocalDynamicStateCopier;
import fr.lgi2a.similar.microkernel.states.IPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.states.dynamicstate.map.IDynamicStateMap;

/**
 * A probe registering to the simulation execution trace all the partly-consistent dynamic state of the simulation, 
 * <i>i.e.</i> the state of the simulation at each time stamp of the simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class LearningTraceUpdatingProbe implements IProbe {
	/**
	 * The object where the execution trace of the simulation is stored.
	 */
	private SimulationExecutionTrace trace;
	
	/**
	 * Creates an instance of the probe keeping the trace of the partly-consistent dynamic states.
	 * @param trace The object where the execution trace of the simulation is stored.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public LearningTraceUpdatingProbe(
			SimulationExecutionTrace trace
	) throws IllegalArgumentException {
		if( trace == null ) {
			throw new IllegalArgumentException( "The 'trace' argument cannot be null." );
		}
		this.trace = trace;
	}

	/**
	 * {@inheritDoc}
	 */
	public void prepareObservation() { }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtInitialTimes(
			SimulationTimeStamp initialTimestamp,
			ISimulationEngine simulationEngine
	) {
		this.includeDynamicStateInTrace( initialTimestamp, simulationEngine );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtPartialConsistentTime(
			SimulationTimeStamp timestamp,
			ISimulationEngine simulationEngine
	) {
		this.includeDynamicStateInTrace( timestamp, simulationEngine );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtFinalTime(
			SimulationTimeStamp finalTimestamp,
			ISimulationEngine simulationEngine
	) {
		LearningSimulationDynamicState dynamicState = new LearningSimulationDynamicState( );
		for( IAgent agent : simulationEngine.getAgents() ){
			dynamicState.addGlobalMemoryState( agent );
		}
		IDynamicStateMap publicLocalDynamicStates = simulationEngine.getSimulationDynamicStates();
		for( LevelIdentifier level : publicLocalDynamicStates.keySet() ) {
			IPublicLocalDynamicState localDynamicState = publicLocalDynamicStates.get( level );
			dynamicState.addLevelDynamicState( LearningPublicLocalDynamicStateCopier.createCopy( localDynamicState ) );
		}
		this.trace.setFinalDynamicState( new SimulationTimeStamp( finalTimestamp ), dynamicState );
		this.trace.setReasonOfEnd( LearningReasonOfSimulationEnd.END_CRITERION_REACHED );
	}
	
	/**
	 * Includes a partly-consistent dynamic state of the simulation into the simulation trace.
	 * @param timeStamp The time stamp when the simulation was in the specified state.
	 * @param simulationEngine The simulation engine where the simulation is running.
	 */
	private void includeDynamicStateInTrace( 
			SimulationTimeStamp timeStamp,
			ISimulationEngine simulationEngine
	) {
		LearningSimulationDynamicState dynamicState = new LearningSimulationDynamicState( );
		for( IAgent agent : simulationEngine.getAgents() ){
			dynamicState.addGlobalMemoryState( agent );
		}
		IDynamicStateMap publicLocalDynamicStates = simulationEngine.getSimulationDynamicStates();
		for( LevelIdentifier level : publicLocalDynamicStates.keySet() ) {
			IPublicLocalDynamicState localDynamicState = publicLocalDynamicStates.get( level );
			dynamicState.addLevelDynamicState( LearningPublicLocalDynamicStateCopier.createCopy( localDynamicState ) );
		}
		this.trace.addDynamicState(
				timeStamp, 
				dynamicState
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToError(
			String errorMessage, 
			Throwable cause
	) { 
		this.trace.setReasonOfEnd( new LearningReasonOfSimulationEnd.ExceptionCaught( errorMessage, cause ) );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToAbortion(
			SimulationTimeStamp timestamp,
			ISimulationEngine simulationEngine
	) { 
		this.trace.setReasonOfEnd( LearningReasonOfSimulationEnd.ABORTED );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endObservation() { }
}
