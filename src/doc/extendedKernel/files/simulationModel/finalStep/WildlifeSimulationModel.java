package fr.univ_artois.lgi2a.wildlifesimulation.initializations;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.univ_artois.lgi2a.similar.extendedkernel.environment.ExtendedEnvironment;
import fr.univ_artois.lgi2a.similar.extendedkernel.levels.ExtendedLevel;
import fr.univ_artois.lgi2a.similar.extendedkernel.levels.ILevelReactionModel;
import fr.univ_artois.lgi2a.similar.extendedkernel.simulationmodel.AbstractExtendedSimulationModel;
import fr.univ_artois.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;
import fr.univ_artois.lgi2a.similar.microkernel.levels.ILevel;
import fr.univ_artois.lgi2a.similar.microkernel.levels.ITimeModel;
import fr.univ_artois.lgi2a.wildlifesimulation.model.WildlifeParametersClass;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.gazelle.AgtGazelleFactory;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.lion.AgtLionFactory;
import fr.univ_artois.lgi2a.wildlifesimulation.model.environment.savannah.EnvHLSInSavannah;
import fr.univ_artois.lgi2a.wildlifesimulation.model.environment.savannah.EnvNaturalInSavannah;
import fr.univ_artois.lgi2a.wildlifesimulation.model.environment.savannah.EnvPLSInSavannah;
import fr.univ_artois.lgi2a.wildlifesimulation.model.levels.PerceptionAndInfluenceRelationGraphs;
import fr.univ_artois.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;
import fr.univ_artois.lgi2a.wildlifesimulation.model.levels.savannah.LvlSavannahReaction;
import fr.univ_artois.lgi2a.wildlifesimulation.model.levels.savannah.LvlSavannahTimeModel;
import fr.univ_artois.lgi2a.wildlifesimulation.tools.RandomValueFactory;

/**
 * Models an initialization profile of a Wildlife simulation containing only 
 * Lions and Gazelles.
 */
public class WildlifeSimulationModel extends AbstractExtendedSimulationModel {
    /**
     * Trick method used to check the validity of the parameter 
     * of the constructor before calling the super constructor.
     * @param parameters The parameters of the simulation which validity is checked.
     * @return The parameters of the simulation which validity is checked.
     * @throws IllegalArgumentException If the parameters are invalid.
     */
    private static WildlifeParametersClass assertParameterValidity(
            ISimulationParameters parameters
    ){
        if( parameters == null ){
            throw new IllegalArgumentException(
                "The simulation parameters cannot be null."
            );
        } else if( ! (parameters instanceof WildlifeParametersClass) ){
            throw new IllegalArgumentException(
                "The simulation parameters have to implement the " +
                WildlifeParametersClass.class.getSimpleName() + " class."
            );
        }
        return (WildlifeParametersClass) parameters;
    }
    
    /**
     * Extracts the final time of the simulation from the simulation parameters.
     * @param parameters The simulation parameters.
     * @return The final time of the simulation.
     */
    private static SimulationTimeStamp getFinalTimeFromParameters(
        ISimulationParameters parameters
    ){
        WildlifeParametersClass castedParams = (WildlifeParametersClass) parameters;
        return castedParams.finalTime;
    }
    
    /**
     * Builds an initialized instance of this simulation model.
     * @param simulationParameters The parameters used with this simulation model.
     */
    public WildlifeSimulationModel(
        ISimulationParameters simulationParameters
    ) {
        super(
            assertParameterValidity( simulationParameters ), 
            new WildlifeEndCriterion(
                getFinalTimeFromParameters( simulationParameters )
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
        WildlifeParametersClass castedParameters = 
                (WildlifeParametersClass) simulationParameters;
        SimulationTimeStamp initialTime = simulationParameters.getInitialTime( );
        List<ILevel> levels = new LinkedList<ILevel>( );
        /* 
         * Create the "Savannah" level.
         */
        ITimeModel timeModel = new LvlSavannahTimeModel( );
        ILevelReactionModel reactionModel = new LvlSavannahReaction( 
            castedParameters.eatBehaviorThresholdDistance
        );
        ExtendedLevel savannah = new ExtendedLevel(
            initialTime, 
            WildlifeLevelList.SAVANNAH, 
            timeModel, 
            reactionModel
        );
        PerceptionAndInfluenceRelationGraphs.buildGraphOfLevel( savannah );
        levels.add( savannah );
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
        WildlifeParametersClass castedParameters = (WildlifeParametersClass) simulationParameters;
        ExtendedEnvironment environment = new ExtendedEnvironment( );
        /*
         * First initialize the environment by setting its public and private
         * local states in the various levels of the simulation.
         */
        // Specify the "Savannah" level.
        ILocalStateOfEnvironment envPls = new EnvPLSInSavannah(
            castedParameters.initialHumidity,
            castedParameters.envWidth,
            castedParameters.envHeight
        );
        environment.includeNewLevel(
            WildlifeLevelList.SAVANNAH,
            envPls ,
            new EnvHLSInSavannah( )
        );
        /*
         * Then specify the natural action of the environment for 
         * each of these levels.
         */
        environment.specifyBehaviorForLevel(
            WildlifeLevelList.SAVANNAH, 
            new EnvNaturalInSavannah( )
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
            WildlifeParametersClass castedParameters = (WildlifeParametersClass) simulationParameters;
            AgentInitializationData result = new AgentInitializationData( );
            // Create the initial gazelle agents.
            for( int gazelleId = 0; gazelleId < castedParameters.initialNumOfGazelles; gazelleId++ ){
                IAgent4Engine gazelle = AgtGazelleFactory.generate(
                    RandomValueFactory.getStrategy().randomLocationIn(
                        castedParameters.envWidth, 
                        castedParameters.envHeight
                    )
                );
                result.getAgents().add( gazelle );
            }
            // Create the initial lion agents.
            for( int lionId = 0; lionId < castedParameters.initialNumOfLions; lionId++ ){
                IAgent4Engine lion = AgtLionFactory.generate(
                    RandomValueFactory.getStrategy().randomLocationIn(
                        castedParameters.envWidth, 
                        castedParameters.envHeight
                    )
                );
                result.getAgents().add( lion );
            }
            return result;
        }
}
