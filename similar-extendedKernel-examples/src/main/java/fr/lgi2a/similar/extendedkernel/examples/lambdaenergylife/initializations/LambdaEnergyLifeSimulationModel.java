package fr.lgi2a.similar.extendedkernel.examples.lambdaenergylife.initializations;

import java.util.LinkedList;
import java.util.List;

import fr.lgi2a.similar.extendedkernel.examples.lambdaenergylife.model.LambdaEnergyLifeParameters;
import fr.lgi2a.similar.extendedkernel.examples.lambdaenergylife.model.levels.micro.LvlMicroEnergyReaction;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.initializations.LambdaLifeSimulationModel;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.levels.LambdaLifeLevelList;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.levels.PerceptionAndInfluenceRelationGraphs;
import fr.lgi2a.similar.extendedkernel.levels.ExtendedLevel;
import fr.lgi2a.similar.extendedkernel.levels.ILevelReactionModel;
import fr.lgi2a.similar.extendedkernel.libs.timemodel.PeriodicTimeModel;
import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar.microkernel.levels.ITimeModel;

public class LambdaEnergyLifeSimulationModel extends LambdaLifeSimulationModel {

	public LambdaEnergyLifeSimulationModel(
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
		LambdaEnergyLifeParameters castedParameters = 
				(LambdaEnergyLifeParameters) simulationParameters;
		SimulationTimeStamp initialTime = simulationParameters.getInitialTime( );
		List<ILevel> levels = new LinkedList<ILevel>( );
		/* 
		 * Create the "Micro" level.
		 */
		ITimeModel timeModel = new PeriodicTimeModel(1, 0, initialTime);
		ILevelReactionModel reactionModel = new LvlMicroEnergyReaction(
			castedParameters.lambda,
			castedParameters.energy,
			castedParameters.gridHeight*castedParameters.gridWidth
		);
		ExtendedLevel micro = new ExtendedLevel(
				initialTime, 
				LambdaLifeLevelList.MICRO, 
				timeModel, 
				reactionModel
		);
		PerceptionAndInfluenceRelationGraphs.buildGraphOfLevel( micro );
		levels.add( micro );

		return levels;
	}

}
