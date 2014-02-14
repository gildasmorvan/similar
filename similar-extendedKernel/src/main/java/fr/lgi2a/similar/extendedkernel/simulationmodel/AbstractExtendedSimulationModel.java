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
package fr.lgi2a.similar.extendedkernel.simulationmodel;

import java.util.List;
import java.util.Map;

import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar.microkernel.libs.abstractimpl.AbstractSimulationModel;

/**
 * Models a simulation model in the extended kernel. This model adds a feature compared to the common libs of the micro-kernel:
 * it separates the declaration of the simulation end criterion from the code of the simulation model.
 * <h2>Benefits</h2>
 * This property has a huge benefit: 
 * <ul>
 * 	<li>
 * 		Simulation end criteria libraries can be built and re-used in many simulations.
 * 	</li>
 * </ul>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public abstract class AbstractExtendedSimulationModel extends AbstractSimulationModel {
	/**
	 * The parameters currently being used in the simulation.
	 */
	private ISimulationParameters simulationParameters;
	/**
	 * The end criterion model used in this simulation model.
	 */
	private IEndCriterionModel endCriterionModel;
	
	/**
	 * Checks the validity of the parameters provided to the constructor.
	 * @param simulationParameters The execution parameters of the simulation.
	 * @param endCriterionModel The end criterion model used in this simulation model.
	 * @return The initial time stamp of the simulation.
	 */
	private static SimulationTimeStamp checkParametersValidity(
		ISimulationParameters simulationParameters,
		IEndCriterionModel endCriterionModel
	){

		if( simulationParameters == null ){
			throw new IllegalArgumentException( 
				"The 'simulationParameters' cannot be null." 
			);
		} else if( endCriterionModel == null ){
			throw new IllegalArgumentException( 
				"The 'endCriterion' cannot be null." 
			);
		} else {
			SimulationTimeStamp initialTime = simulationParameters.getInitialTime();
			return initialTime;
		}
	}
	
	/**
	 * Builds an instance of an abstract extended simulation model, having a specific time stamp as initial time and a specific
	 * ending criterion.
	 * @param simulationParameters The execution parameters of the simulation.
	 * @param endCriterionModel The end criterion model used in this simulation model.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public AbstractExtendedSimulationModel(
			ISimulationParameters simulationParameters,
			IEndCriterionModel endCriterionModel
			
	) {
		super( checkParametersValidity(
			simulationParameters, 
			endCriterionModel
		) );
		this.simulationParameters = simulationParameters;
		this.endCriterionModel = endCriterionModel;
	}
	
	/**
	 * Gets the end criterion model used in this simulation model.
	 * @return The end criterion model used in this simulation model.
	 */
	public IEndCriterionModel getEndCriterionModel( ){
		return this.endCriterionModel;
	}
	
	/**
	 * Gets the parameters currently being used in the simulation.
	 * @return The parameters currently being used in the simulation.
	 */
	public ISimulationParameters getSimulationParameters( ) {
		return this.simulationParameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isFinalTimeOrAfter(
			SimulationTimeStamp currentTime,
			ISimulationEngine engine
	) {
		return this.endCriterionModel.isFinalTimeOrAfter(
			currentTime, 
			engine
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final List<ILevel> generateLevels(
		SimulationTimeStamp initialTime
	) {
		return this.generateLevels(
			this.simulationParameters
		);
	}
	
	/**
	 * Generates the bare levels of the simulation. These levels contain no agents and define no environment.
	 * @param simulationParameters The execution parameters of the simulation.
	 * @return The bare levels of the simulation.
	 */
	protected abstract List<ILevel> generateLevels( 
		ISimulationParameters simulationParameters
	);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final EnvironmentInitializationData generateEnvironment(
		SimulationTimeStamp initialTime,
		Map<LevelIdentifier, ILevel> levels
	) {
		return this.generateEnvironment(
			this.simulationParameters,
			levels
		);
	}
	
	/**
	 * Generates the environment of the simulation. At this stage, no agent are generated in the simulation.
	 * <p>
	 * 	This method should set the public local state of the environment for each level of the simulation.
	 * </p>
	 * @param simulationParameters The execution parameters of the simulation.
	 * @param levels The levels of the simulation.
	 * @return The generated environment and the influences to put in the state dynamics of the initial 
	 * dynamic state of the levels.
	 */
	protected abstract EnvironmentInitializationData generateEnvironment( 
			ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels 
	);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final AgentInitializationData generateAgents(
			SimulationTimeStamp initialTime,
			Map<LevelIdentifier, ILevel> levels
	) {
		return this.generateAgents(
			this.simulationParameters, 
			levels
		);
	}
	
	/**
	 * Generates the agents of the simulation.
	 * <p>
	 * 	This method should only create the agents, without adding them in the levels. This task is performed by the simulation engine.
	 * </p>
	 * @param simulationParameters The execution parameters of the simulation.
	 * @param levels The levels of the simulation.
	 * @return The generated agents and the influences to put in the state dynamics of the initial 
	 * dynamic state of the levels.
	 */
	protected abstract AgentInitializationData generateAgents( 
			ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels 
	);
}
