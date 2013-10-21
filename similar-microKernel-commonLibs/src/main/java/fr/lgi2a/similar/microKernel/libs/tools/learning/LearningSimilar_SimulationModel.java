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
package fr.lgi2a.similar.microKernel.libs.tools.learning;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import fr.lgi2a.similar.microKernel.I_Level;
import fr.lgi2a.similar.microKernel.LevelIdentifier;
import fr.lgi2a.similar.microKernel.SimulationTimeStamp;
import fr.lgi2a.similar.microKernel.libs.abstractImplementations.AbstractSimulationModel;

/**
 * This simulation model is designed to help users to understand the algorithm used to run a simulation.
 * It builds a full trace of the operations performed during the execution of a simulation.
 * 
 * <h1>Usage</h1>
 * <p>
 * 	TODO
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class LearningSimilar_SimulationModel extends AbstractSimulationModel {
	/**
	 * The final time stamp of the simulation.
	 */
	private final SimulationTimeStamp finalTime;

	/**
	 * Builds an instance of an abstract simulation model, having a specific time stamp as initial time.
	 * @param initialTime The initial time stamp of this simulation model.
	 * @param finalTime The final time stamp of this simulation model, <i>i.e.</i> the time preceding the computation 
	 * of the last reaction of the simulation.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public LearningSimilar_SimulationModel(
			SimulationTimeStamp initialTime,
			SimulationTimeStamp finalTime
	){
		super( initialTime );
		if( finalTime == null ){
			throw new IllegalArgumentException( "The 'finalTime' argumen cannot be null." );
		}
		this.finalTime = finalTime;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimulationTimeStamp getNextTime( SimulationTimeStamp currentTime ) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFinalTimeOrAfter(
			SimulationTimeStamp currentTime
	) throws NoSuchElementException {
		return currentTime.compareTo( finalTime ) >= 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<I_Level> generateLevels( SimulationTimeStamp initialTime ) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EnvironmentInitializationData generateEnvironment(
			SimulationTimeStamp initialTime,
			Map<LevelIdentifier, I_Level> levels) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AgentInitializationData generateAgents(
			SimulationTimeStamp initialTime,
			Map<LevelIdentifier, I_Level> levels
	) {
		return null;
	}
}
