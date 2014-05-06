package fr.lgi2a.similar.extendedkernel.examples.lambdalife.initializations;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.lgi2a.similar.extendedkernel.environment.ExtendedEnvironment;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.LambdaLifeParameters;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.agents.cell.AgtCellFactory;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.agents.cell.micro.AgtCellPLSInMicroLevel;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.environment.micro.EnvPLSInMicroLevel;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.levels.LambdaLifeLevelList;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.levels.PerceptionAndInfluenceRelationGraphs;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.levels.micro.LvlMicroReaction;
import fr.lgi2a.similar.extendedkernel.levels.ExtendedLevel;
import fr.lgi2a.similar.extendedkernel.levels.ILevelReactionModel;
import fr.lgi2a.similar.extendedkernel.libs.endcriterion.EndCriterionDisjunction;
import fr.lgi2a.similar.extendedkernel.libs.endcriterion.TimeBasedEndCriterion;
import fr.lgi2a.similar.extendedkernel.libs.generic.EmptyEnvNaturalModel;
import fr.lgi2a.similar.extendedkernel.libs.timemodel.PeriodicTimeModel;
import fr.lgi2a.similar.extendedkernel.simulationmodel.AbstractExtendedSimulationModel;
import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar.microkernel.levels.ITimeModel;
import fr.lgi2a.similar.microkernel.libs.generic.EmptyLocalStateOfEnvironment;

/**
 * Models the default configuration of the simulation for a lambda game of life.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 */
public class LambdaLifeSimulationModel extends AbstractExtendedSimulationModel {
	/**
	 * Trick method used to check the validity of the parameter 
	 * of the constructor before calling the super constructor.
	 * @param parameters The parameters of the simulation which validity is checked.
	 * @return The parameters of the simulation which validity is checked.
	 * @throws IllegalArgumentException If the parameters are invalid.
	 */
	private static LambdaLifeParameters assertParameterValidity(
			ISimulationParameters parameters
	){
		if( parameters == null ){
			throw new IllegalArgumentException(
				"The simulation parameters cannot be null."
			);
		} else if( ! (parameters instanceof LambdaLifeParameters) ){
			throw new IllegalArgumentException(
				"The simulation parameters have to implement the " +
					LambdaLifeParameters.class.getSimpleName() + " class."
			);
		}
		return (LambdaLifeParameters) parameters;
	}

	/**
	 * Extracts the final time of the simulation from the simulation parameters.
	 * @param parameters The simulation parameters.
	 * @return The final time of the simulation.
	 */
	private static SimulationTimeStamp getFinalTimeFromParameters(
			ISimulationParameters parameters
	){
		LambdaLifeParameters castedParams = (LambdaLifeParameters) parameters;
		return castedParams.finalTime;
	}

	/**
	 * Extracts the still lives threshold from the simulation parameters.
	 * @param parameters The simulation parameters.
	 * @return The still lives threshold.
	 */
	private static int getStillLivesThresholdFromParameters(
			ISimulationParameters parameters
	){
		LambdaLifeParameters castedParams = (LambdaLifeParameters) parameters;
		return castedParams.stillLifeThreshold;
	}

	/**
	 * Builds an initialized instance of this simulation model.
	 * @param simulationParameters The parameters used with this simulation model.
	 */
	public LambdaLifeSimulationModel(
			ISimulationParameters simulationParameters
	) {
		super( 
			assertParameterValidity( simulationParameters ), 
			new EndCriterionDisjunction( 
				new TimeBasedEndCriterion(
					getFinalTimeFromParameters(simulationParameters)
				),
				new StillLifeEndCriterion(
					getStillLivesThresholdFromParameters(simulationParameters)
				)
			)
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<ILevel> generateLevels(
			ISimulationParameters simulationParameters
	) {
		LambdaLifeParameters castedParameters = 
				(LambdaLifeParameters) simulationParameters;
		SimulationTimeStamp initialTime = simulationParameters.getInitialTime( );
		List<ILevel> levels = new LinkedList<ILevel>( );
		/* 
		 * Create the "Micro" level.
		 */
		ITimeModel timeModel = new PeriodicTimeModel(1, 0, initialTime);
		ILevelReactionModel reactionModel = new LvlMicroReaction(
			castedParameters.lambda
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected EnvironmentInitializationData generateEnvironment(
			ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels
	) {
		LambdaLifeParameters castedParameters = 
				(LambdaLifeParameters) simulationParameters;
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
			LambdaLifeLevelList.MICRO
		);
		environment.includeNewLevel(
				LambdaLifeLevelList.MICRO,
				plsInMicro,
				hlsInMicro
		);
		// Specify the behavior of the environment in the "Micro" level.
		environment.specifyBehaviorForLevel(
				LambdaLifeLevelList.MICRO,
				new EmptyEnvNaturalModel( LambdaLifeLevelList.MICRO )
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
		LambdaLifeParameters castedParameters = (LambdaLifeParameters) simulationParameters;
		AgentInitializationData result = new AgentInitializationData( );
		// Get the local state of the environment
		EnvPLSInMicroLevel envPls = (EnvPLSInMicroLevel) 
				levels.get( LambdaLifeLevelList.MICRO ).getLastConsistentState().getPublicLocalStateOfEnvironment( );
		// Create the cell agents
		for( int x = 0; x < castedParameters.gridWidth; x++ ) {
			for( int y = 0; y < castedParameters.gridHeight; y++ ) {
				// Create the cell located at (x, y)
				IAgent4Engine cell = AgtCellFactory.generate( x, y );
				result.getAgents().add( cell );
				// Register the agent in the grid of the environment.
				envPls.setCellAt( (AgtCellPLSInMicroLevel) cell.getPublicLocalState( LambdaLifeLevelList.MICRO ) );
			}
		}
		return result;
	}
}