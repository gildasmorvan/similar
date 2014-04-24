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
package fr.lgi2a.similar.extendedkernel.examples.bubblechamber.initializations;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.lgi2a.similar.extendedkernel.environment.ExtendedEnvironment;
import fr.lgi2a.similar.extendedkernel.examples.bubblechamber.model.BubbleChamberParameters;
import fr.lgi2a.similar.extendedkernel.examples.bubblechamber.model.agents.cannon.AgtCannonFactory;
import fr.lgi2a.similar.extendedkernel.examples.bubblechamber.model.environment.chamber.EnvNaturalInChamber;
import fr.lgi2a.similar.extendedkernel.examples.bubblechamber.model.environment.chamber.EnvPLSInChamber;
import fr.lgi2a.similar.extendedkernel.examples.bubblechamber.model.environment.external.EnvNaturalInExternal;
import fr.lgi2a.similar.extendedkernel.examples.bubblechamber.model.environment.external.EnvPLSInExternal;
import fr.lgi2a.similar.extendedkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.lgi2a.similar.extendedkernel.examples.bubblechamber.model.levels.PerceptionAndInfluenceRelationGraphs;
import fr.lgi2a.similar.extendedkernel.examples.bubblechamber.model.levels.chamber.ChamberLvlReactionModel;
import fr.lgi2a.similar.extendedkernel.examples.bubblechamber.model.levels.external.ExternalLvlReactionModel;
import fr.lgi2a.similar.extendedkernel.examples.bubblechamber.tools.RandomValueFactory;
import fr.lgi2a.similar.extendedkernel.levels.ExtendedLevel;
import fr.lgi2a.similar.extendedkernel.libs.endcriterion.TimeBasedEndCriterion;
import fr.lgi2a.similar.extendedkernel.libs.timemodel.PeriodicTimeModel;
import fr.lgi2a.similar.extendedkernel.simulationmodel.AbstractExtendedSimulationModel;
import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar.microkernel.libs.generic.EmptyLocalStateOfEnvironment;

/**
 * The simulation model of the "Bubble chamber" simulation.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class BubbleChamberSimulation extends AbstractExtendedSimulationModel {
	/**
	 * The parameters being used in the simulation.
	 */
	private BubbleChamberParameters parameters;
	
	/**
	 * Builds a new model for the "Bubble chamber" simulation.
	 * @param initialTime The initial time of the simulation.
	 * @param finalTime The final time of the simulation.
	 * @throws IllegalArgumentException If the <code>parameters</code> or the <code>finalTime</code>
	 * arguments are <code>null</code>.
	 */
	public BubbleChamberSimulation(
			BubbleChamberParameters parameters
	) {
		super(
			parameters,
			new TimeBasedEndCriterion( 
				parameters.finalTime
			)
		);
		this.parameters = parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ILevel> generateLevels(
			ISimulationParameters parameters
	) {
		List<ILevel> result = new LinkedList<ILevel>();
		BubbleChamberParameters castedParameters = (BubbleChamberParameters) parameters;
		
		// Create the "external" level of the simulation.
		ExtendedLevel external = new ExtendedLevel(
			castedParameters.getInitialTime(), 
			BubbleChamberLevelList.EXTERNAL, 
			new PeriodicTimeModel( 
				1, 
				0, 
				castedParameters.getInitialTime()
			),
			new ExternalLvlReactionModel(
				castedParameters.powerToTemperatureConversionRatio
			)
		);
		PerceptionAndInfluenceRelationGraphs.buildGraphOfLevel( external );
		result.add( external );
		
		// Create the "chamber" level of the simulation.
		ExtendedLevel chamber = new ExtendedLevel(
			castedParameters.getInitialTime(), 
			BubbleChamberLevelList.CHAMBER, 
			new PeriodicTimeModel( 
				1, 
				0, 
				castedParameters.getInitialTime()
			),
			new ChamberLvlReactionModel( )
		);
		PerceptionAndInfluenceRelationGraphs.buildGraphOfLevel( chamber );
		result.add( chamber );
		
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EnvironmentInitializationData generateEnvironment(
			ISimulationParameters parameters, 
			Map<LevelIdentifier, ILevel> levels
	) {
		// Create the environment.
		ExtendedEnvironment environment = new ExtendedEnvironment( );
		// Define the initial behavior of the environment for each level.
		environment.specifyBehaviorForLevel(
				BubbleChamberLevelList.EXTERNAL, 
				new EnvNaturalInExternal( )
		);
		environment.specifyBehaviorForLevel(
				BubbleChamberLevelList.CHAMBER, 
				new EnvNaturalInChamber( )
		);
		// Set the initial local state of the environment for each level.
		environment.includeNewLevel(
				BubbleChamberLevelList.CHAMBER, 
				new EnvPLSInChamber( this.parameters.chamberBounds ),
				new EmptyLocalStateOfEnvironment( BubbleChamberLevelList.CHAMBER )
		);
		environment.includeNewLevel(
				BubbleChamberLevelList.EXTERNAL, 
				new EnvPLSInExternal( this.parameters.initialAmbientTemperature ),
				new EmptyLocalStateOfEnvironment( BubbleChamberLevelList.EXTERNAL )
		);
		
		return new EnvironmentInitializationData( environment );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AgentInitializationData generateAgents(
			ISimulationParameters parameters, 
			Map<LevelIdentifier, ILevel> levels
	) {
		AgentInitializationData result = new AgentInitializationData();
		
		// Create the agents initially lying in the simulation.
		double initialPower = RandomValueFactory.getStrategy().randomDouble(
				this.parameters.cannonPowerRange[ 0 ], 
				this.parameters.cannonPowerRange[ 1 ]
		);
		IAgent4Engine cannon = AgtCannonFactory.generate(
				this.parameters.chamberBounds.getX(), 
				this.parameters.chamberBounds.getCenterY(), 
				1.0, 
				0.0, 
				initialPower, 
				this.parameters.initialAmbientTemperature
		);
		result.getAgents().add( cannon );
		
		return result;
	}

}
