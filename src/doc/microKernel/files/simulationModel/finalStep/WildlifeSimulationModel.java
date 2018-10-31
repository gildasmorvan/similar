package fr.univ_artois.lgi2a.wildlifesimulation.initializations;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;
import fr.univ_artois.lgi2a.similar.microkernel.levels.ILevel;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractSimulationModel;
import fr.univ_artois.lgi2a.wildlifesimulation.model.WildlifeSimulationParam;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.gazelle.AgtGazelleFactory;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.lion.AgtLionFactory;
import fr.univ_artois.lgi2a.wildlifesimulation.model.environment.WildlifeEnvironment;
import fr.univ_artois.lgi2a.wildlifesimulation.model.environment.savannah.EnvHLSInSavannah;
import fr.univ_artois.lgi2a.wildlifesimulation.model.environment.savannah.EnvPLSInSavannah;
import fr.univ_artois.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;
import fr.univ_artois.lgi2a.wildlifesimulation.model.levels.savannah.SavannahLevel;
import fr.univ_artois.lgi2a.wildlifesimulation.model.levels.sky.SkyLevel;
import fr.univ_artois.lgi2a.wildlifesimulation.model.levels.underground.UndergroundLevel;
import fr.univ_artois.lgi2a.wildlifesimulation.tools.RandomValueFactory;

/**
 * Models an initialization profile of a Wildlife simulation containing only 
 * Lions and Gazelles.
 */
public class WildlifeSimulationModel extends AbstractSimulationModel {
	/**
	 * The parameters being used in this simulation.
	 */
	private WildlifeSimulationParam parameters;
	
	/**
	 * Trick method used to check the validity of the parameter 
	 * of the constructor before calling the super constructor.
	 * @param parameters The parameters of the simulation which validity is checked.
	 * @return The parameters of the simulation which validity is checked.
	 * @throws IllegalArgumentException If the parameters are invalid.
	 */
	private static WildlifeSimulationParam assertParameterValidity(
		WildlifeSimulationParam parameters
	){
		if( parameters == null ){
			throw new IllegalArgumentException(
				"The simulation parameters cannot be null."
			);
		}
		return parameters;
	}
	
	/**
	 * Builds a new instance of this simulation model initialization profile,
	 * using a specific instance of the parameters class.
	 * @param parameters The parameters class that will be used in the simulation.
	 * @throws IllegalArgumentException If the parameters are invalid.
	 */
	public WildlifeSimulationModel(
		WildlifeSimulationParam parameters
	) {
		super(
			assertParameterValidity( parameters ).initialTime
		);
		this.parameters = parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFinalTimeOrAfter(
		SimulationTimeStamp currentTime,
		ISimulationEngine engine
	) {
		return currentTime.compareTo( this.parameters.finalTime ) >= 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ILevel> generateLevels(
		SimulationTimeStamp initialTime
	) {
		List<ILevel> levels = new LinkedList<ILevel>( );
		SavannahLevel savannah = new SavannahLevel( 
			initialTime,
			this.parameters.eatBehaviorThresholdDistance
		);
		levels.add( savannah );
		SkyLevel sky = new SkyLevel( 
			initialTime
		);
		levels.add( sky );
		UndergroundLevel underground = new UndergroundLevel( 
			initialTime
		);
		levels.add( underground );
		return levels;
	}

	/**
	 * {@inheritDoc}
	 */
	public EnvironmentInitializationData generateEnvironment( 
			SimulationTimeStamp initialTime, 
			Map<LevelIdentifier, ILevel> levels 
	){
		WildlifeEnvironment environment = new WildlifeEnvironment( );
		// First initialize the environment by setting its public and private
		// local states in the various levels of the simulation.
		// 
		// Specify the "Savannah" level.
		ILocalStateOfEnvironment envPls = new EnvPLSInSavannah(
			this.parameters.initialHumidity,
			this.parameters.envWidth,
			this.parameters.envHeight
		);
		environment.includeNewLevel(
			WildlifeLevelList.SAVANNAH,
			envPls ,
			new EnvHLSInSavannah( )
		);
		// Then return the environment initialization data.
		return new EnvironmentInitializationData( environment  );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AgentInitializationData generateAgents(
		SimulationTimeStamp initialTime, 
		Map<LevelIdentifier, ILevel> levels
	) {
		AgentInitializationData result = new AgentInitializationData( );

		// Create the initial gazelle agents.
		for( 
			int gazelleId = 0; 
			gazelleId < this.parameters.initialNumOfGazelles; 
			gazelleId++ 
		){
			IAgent4Engine gazelle = AgtGazelleFactory.generate(
				RandomValueFactory.getStrategy().randomLocationIn(
					this.parameters.envWidth, 
					this.parameters.envHeight
				)
			);
			result.getAgents().add( gazelle );
		}
		// Create the initial lion agents.
		for( 
			int lionId = 0; 
			lionId < this.parameters.initialNumOfLions; 
			lionId++ 
		){
			IAgent4Engine lion = AgtLionFactory.generate(
				RandomValueFactory.getStrategy().randomLocationIn(
					this.parameters.envWidth, 
					this.parameters.envHeight
				)
			);
			result.getAgents().add( lion );
		}
		
		return result;
	}
}