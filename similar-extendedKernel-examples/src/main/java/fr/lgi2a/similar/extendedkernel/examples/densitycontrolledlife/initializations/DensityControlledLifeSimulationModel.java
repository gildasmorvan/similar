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
package fr.lgi2a.similar.extendedkernel.examples.densitycontrolledlife.initializations;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.lgi2a.similar.extendedkernel.environment.ExtendedEnvironment;
import fr.lgi2a.similar.extendedkernel.examples.densitycontrolledlife.model.DensityControlledLifeParameters;
import fr.lgi2a.similar.extendedkernel.examples.densitycontrolledlife.model.agents.cellcluster.AgtCellClusterFactory;
import fr.lgi2a.similar.extendedkernel.examples.densitycontrolledlife.model.levels.DensityControlledLifeLevelList;
import fr.lgi2a.similar.extendedkernel.examples.densitycontrolledlife.model.levels.PerceptionAndInfluenceRelationGraphs;
import fr.lgi2a.similar.extendedkernel.examples.densitycontrolledlife.model.levels.micro.LvlMicroDensityControlledReaction;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.initializations.LambdaLifeSimulationModel;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.environment.micro.EnvPLSInMicroLevel;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.levels.LambdaLifeLevelList;
import fr.lgi2a.similar.extendedkernel.levels.ExtendedLevel;
import fr.lgi2a.similar.extendedkernel.levels.ILevelReactionModel;
import fr.lgi2a.similar.extendedkernel.libs.generic.EmptyEnvNaturalModel;
import fr.lgi2a.similar.extendedkernel.libs.generic.EmptyLevelReactionModel;
import fr.lgi2a.similar.extendedkernel.libs.timemodel.PeriodicTimeModel;
import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar.microkernel.levels.ITimeModel;
import fr.lgi2a.similar.microkernel.libs.generic.EmptyLocalStateOfEnvironment;

/**
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class DensityControlledLifeSimulationModel extends
		LambdaLifeSimulationModel {

	/**
	 * @param simulationParameters
	 */
	public DensityControlledLifeSimulationModel(
			ISimulationParameters simulationParameters) {
		super(simulationParameters);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<ILevel> generateLevels(
			ISimulationParameters simulationParameters
	) {

		SimulationTimeStamp initialTime = simulationParameters.getInitialTime( );
		List<ILevel> levels = new LinkedList<ILevel>( );
		/* 
		 * Create the "Micro" level.
		 */
		ITimeModel microTimeModel = new PeriodicTimeModel(1, 0, initialTime);
		ILevelReactionModel reactionModel = new LvlMicroDensityControlledReaction();
		ExtendedLevel micro = new ExtendedLevel(
				initialTime, 
				LambdaLifeLevelList.MICRO, 
				microTimeModel, 
				reactionModel
		);
		levels.add( micro );
		
		/* 
		 * Create the "Meso" level.
		 */
		ExtendedLevel meso = new ExtendedLevel(
				initialTime, 
				DensityControlledLifeLevelList.MESO, 
				microTimeModel, 
				new EmptyLevelReactionModel()
		);
		levels.add( meso );
		
		for(ILevel level : levels ) {
			PerceptionAndInfluenceRelationGraphs.buildGraphOfLevel( (ExtendedLevel) level );
		}
		

		return levels;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected EnvironmentInitializationData generateEnvironment(
			ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels
	) {
		DensityControlledLifeParameters castedParameters = 
				(DensityControlledLifeParameters) simulationParameters;
		
		// Create the environment
		ExtendedEnvironment environment = new ExtendedEnvironment();
		
		// Define its public and private local states in the "Micro" level.
		ILocalStateOfEnvironment plsInMicro = new EnvPLSInMicroLevel( 
			castedParameters.gridWidth, 
			castedParameters.gridHeight, 
			castedParameters.xTorus, 
			castedParameters.yTorus 
		);
		ILocalStateOfEnvironment hlsInMicro = new EmptyLocalStateOfEnvironment(
				DensityControlledLifeLevelList.MICRO
		);
		environment.includeNewLevel(
				DensityControlledLifeLevelList.MICRO,
				plsInMicro,
				hlsInMicro
		);
		// Specify the behavior of the environment in the "Micro" level.
		environment.specifyBehaviorForLevel(
				DensityControlledLifeLevelList.MICRO,
				new EmptyEnvNaturalModel( DensityControlledLifeLevelList.MICRO )
		);
		
		// Define its public and private local states in the "Meso" level.
		ILocalStateOfEnvironment plsInMeso = new EmptyLocalStateOfEnvironment(
				DensityControlledLifeLevelList.MESO
		);
		
		ILocalStateOfEnvironment hlsInMeso = new EmptyLocalStateOfEnvironment(
				DensityControlledLifeLevelList.MESO
		);
		
		environment.includeNewLevel(
				DensityControlledLifeLevelList.MESO,
				plsInMeso,
				hlsInMeso
		);
		
		// Specify the behavior of the environment in the "Meso" level.
		environment.specifyBehaviorForLevel(
			DensityControlledLifeLevelList.MESO,
			new EmptyEnvNaturalModel( DensityControlledLifeLevelList.MESO )
		);
		
		return new EnvironmentInitializationData( environment );
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AgentInitializationData generateAgents(
			ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels
	) {
		DensityControlledLifeParameters castedParameters = 
				(DensityControlledLifeParameters) simulationParameters;
		AgentInitializationData result = super.generateAgents(simulationParameters, levels);
		for( int x = 0; x < castedParameters.gridWidth; x += castedParameters.xlength ) {
			for( int y = 0; y < castedParameters.gridHeight; y+=castedParameters.ylength ) {
				// Create the cellCluster located at (x, y)
				IAgent4Engine cellCluster = AgtCellClusterFactory.generate( x, y);
				result.getAgents().add( cellCluster );
			}
		}
		
		return result;
	}

}
