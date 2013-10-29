/**
 * Copyright or � or Copr. LGI2A
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
package fr.lgi2a.similar.microkernel.generic.engines.tools;

import java.util.List;
import java.util.Map;

import fr.lgi2a.similar.microkernel.I_Level;
import fr.lgi2a.similar.microkernel.I_SimulationEngine;
import fr.lgi2a.similar.microkernel.I_SimulationModel;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;

/**
 * A simulation model where the value returned by each method is defined in the constructor.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class UnitTest_SimulationModel implements I_SimulationModel {
	/**
	 * The value returned by the {@link I_SimulationModel#getInitialTime()} method.
	 */
	private SimulationTimeStamp initialTime;
	/**
	 * The static value returned by the {@link I_SimulationModel#isFinalTimeOrAfter(SimulationTimeStamp)} method.
	 */
	private boolean finalTimeOrAfter;
	/**
	 * The static value returned by the {@link I_SimulationModel#generateLevels(SimulationTimeStamp)} method.
	 */
	private List<I_Level> generateLevels;
	/**
	 * The static value returned by the {@link I_SimulationModel#generateEnvironment(SimulationTimeStamp, Map)} method.
	 */
	private EnvironmentInitializationData generateEnvironment;
	/**
	 * The static value returned by the {@link I_SimulationModel#generateAgents(SimulationTimeStamp, Map)} method.
	 */
	private AgentInitializationData generateAgents;
	
	/**
	 * Creates a simulation model where the value returned by each method is defined in the constructor.
	 * @param initialTime The value returned by the {@link I_SimulationModel#getInitialTime()} method.
	 * @param nextTime The static value returned by the {@link I_SimulationModel#getNextTime(SimulationTimeStamp)} method.
	 * @param finalTimeOrAfter The static value returned by the {@link I_SimulationModel#isFinalTimeOrAfter(SimulationTimeStamp)} method.
	 * @param generateLevels The static value returned by the {@link I_SimulationModel#generateLevels(SimulationTimeStamp)} method.
	 * @param generateEnvironment The static value returned by the {@link I_SimulationModel#generateEnvironment(SimulationTimeStamp, Map)} method.
	 * @param generateAgents The static value returned by the {@link I_SimulationModel#generateAgents(SimulationTimeStamp, Map)} method.
	 */
	public UnitTest_SimulationModel( 
			SimulationTimeStamp initialTime,
			SimulationTimeStamp nextTime,
			boolean finalTimeOrAfter,
			List<I_Level> generateLevels,
			EnvironmentInitializationData generateEnvironment,
			AgentInitializationData generateAgents
	) {
		this.initialTime = initialTime;
		this.finalTimeOrAfter = finalTimeOrAfter;
		this.generateLevels = generateLevels;
		this.generateEnvironment = generateEnvironment;
		this.generateAgents = generateAgents;
	}
	
	/**
	 * Returns a value defined in the constructor of the object.
	 */
	@Override
	public SimulationTimeStamp getInitialTime() {
		return this.initialTime;
	}

	/**
	 * Returns a value defined in the constructor of the object.
	 */
	@Override
	public boolean isFinalTimeOrAfter( SimulationTimeStamp currentTime, I_SimulationEngine engine) {
		return this.finalTimeOrAfter;
	}

	/**
	 * Returns a value defined in the constructor of the object.
	 */
	@Override
	public List<I_Level> generateLevels(SimulationTimeStamp initialTime) {
		return this.generateLevels;
	}

	/**
	 * Returns a value defined in the constructor of the object.
	 */
	@Override
	public EnvironmentInitializationData generateEnvironment(
			SimulationTimeStamp initialTime,
			Map<LevelIdentifier, I_Level> levels
	) {
		return this.generateEnvironment;
	}

	/**
	 * Returns a value defined in the constructor of the object.
	 */
	@Override
	public AgentInitializationData generateAgents(
			SimulationTimeStamp initialTime,
			Map<LevelIdentifier, I_Level> levels
	) {
		return this.generateAgents;
	}
}
