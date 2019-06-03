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
package fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife.probes;

import java.io.PrintStream;

import fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife.model.agents.LambdaLifeAgentList;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife.model.agents.cell.micro.AgtCellPLSInMicroLevel;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife.model.environment.micro.EnvPLSInMicroLevel;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife.model.levels.LambdaLifeLevelList;
import fr.univ_artois.lgi2a.similar.microkernel.IProbe;
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;

/**
 * A probe printing in a stream the number of alive cells over time.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 */
public class MacroStateProbe implements IProbe {
	/**
	 * The stream where the data are written.
	 */
	private PrintStream target;
	
	/**
	 * Creates an instance of this probe writing in a specific print stream.
	 * @param target The stream where the data are written.
	 * @throws IllegalArgumentException If the <code>target</code> is <code>null</code>.
	 */
	public MacroStateProbe( PrintStream target ) {
		if( target == null ){
			throw new IllegalArgumentException( 
				"The argument cannot be null." 
			);
		}
		this.target = target;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepareObservation() {
		this.target.println("#Time\tDensity\tEnergy");		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtInitialTimes(
		SimulationTimeStamp initialTimestamp,
		ISimulationEngine simulationEngine
	) {
		this.displayDensityEnergy( initialTimestamp,  simulationEngine );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtPartialConsistentTime(
		SimulationTimeStamp timestamp,
		ISimulationEngine simulationEngine
	) {
		this.displayDensityEnergy( timestamp,  simulationEngine );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtFinalTime(
		SimulationTimeStamp finalTimestamp,
		ISimulationEngine simulationEngine
	) {
		this.displayDensityEnergy( finalTimestamp,  simulationEngine );

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endObservation() {
		this.target.flush();
	}

	/**
	 * {@inheritDoc}
	 */
	private void displayDensityEnergy(
			SimulationTimeStamp timestamp,
			ISimulationEngine simulationEngine
	){
		
		// Get the public local state of the environment in the "Micro" level.

		IPublicLocalDynamicState microState = simulationEngine.getSimulationDynamicStates().get( 
				LambdaLifeLevelList.MICRO
		);
		ILocalStateOfEnvironment rawEnPls = microState.getPublicLocalStateOfEnvironment();
		EnvPLSInMicroLevel envPls = (EnvPLSInMicroLevel) rawEnPls;
		int livingCells = 0, changingStateCells = 0;
		for( ILocalStateOfAgent agtState : microState.getPublicLocalStateOfAgents() ){
			if( agtState.getCategoryOfAgent().isA( LambdaLifeAgentList.CELL) ){
				AgtCellPLSInMicroLevel castedAgtState = (AgtCellPLSInMicroLevel) agtState;
				// Compute the number of neighbors
				int neighbors = 0;
				for( int dx = -1; dx <= 1; dx++ ){
					for( int dy = -1; dy <= 1; dy++ ){
						if( dx != 0 || dy != 0 ){
							AgtCellPLSInMicroLevel neighbor = envPls.getCellAt( castedAgtState.getX() + dx, castedAgtState.getY() + dy );
							if( neighbor != null && neighbor.isAlive() ){
								neighbors++;
							}
						}
					}
				}
				if(castedAgtState.isAlive() ) {
					livingCells++;
					if( neighbors != 2 && neighbors != 3 ){
						changingStateCells++;
					}
				}
				else {
					if( neighbors == 3 ) {
						changingStateCells++;
					}
				}
			}
		}
		this.target.println( 
			timestamp.getIdentifier() + 
			"\t"+ ( (double) livingCells)/(envPls.getHeight()*envPls.getWidth())+
			"\t"+ ((double) changingStateCells)/(envPls.getHeight()*envPls.getWidth())		
		);
	}
}
