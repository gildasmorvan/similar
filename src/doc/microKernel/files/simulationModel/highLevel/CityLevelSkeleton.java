package fr.lgi2a.mysimulation.model.levels.city;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import fr.lgi2a.mysimulation.model.levels.MyAwesomeLevelList;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.dynamicstate.TransitoryPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.influences.IInfluence;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.levels.ILevel;

/**
 * The model of the "City" level.
 */
public class CityLevelSkeleton implements ILevel {
    /**
     * The unique identifier of the level.
     */
    private final LevelIdentifier identifier;
    /**
     * The out neighborhood of this level in the perception relation graph 
     * (<i>i.e.</i> the levels that can be perceived from this level).
     */
    private Set<LevelIdentifier> perceptionRelations;
    /**
     * The out neighborhood of this level in the influence relation graph 
     * (<i>i.e.</i> the levels that can be influenced by this level).
     */
    private Set<LevelIdentifier> influenceRelations;
    /**
     * The most recent consistent state of this level.
     */
    private ConsistentPublicLocalDynamicState mostRecentConsistentState;
    /**
     * The most recent transitory state of this level.
     */
    private TransitoryPublicLocalDynamicState mostRecentTransitoryState;
    
    /**
     * Builds an half-initialized level. This constructor defines the value of:
     * <ul>
     *     <li>The identifier of the level.</li>
     *     <li>The time stamp of the last consistent dynamic state of the level.</li>
     * </ul>
     * To complete the initialization of the level, the following elements have to 
     * be specified in the last consistent state of the level:
     * <ul>
     *     <li>The initial public local state of the environment;</li>
     *     <li>The public local state of the agents initially lying in the level have 
     *             to be added;</li>
     *     <li>The persistent influences initially lying in the level have to be 
     *             added.</li>
     * </ul>
     * @param initialTime The initial time stamp of the simulation.
     */
    public CityLevelSkeleton( SimulationTimeStamp initialTime ) {
        this.identifier = MyAwesomeLevelList.CITY;
        this.perceptionRelations = this.buildPerceptionRelations( );
        this.influenceRelations = this.buildInfluenceRelations( );
        this.mostRecentConsistentState = new ConsistentPublicLocalDynamicState(
                initialTime, 
                this.getIdentifier()
        );
        this.mostRecentTransitoryState = new TransitoryPublicLocalDynamicState(
            this.mostRecentConsistentState
        );
    }
    
    /**
     * Builds the perception relation graph of this level.
     */
    private final Set<LevelIdentifier> buildPerceptionRelations( ) {
        Set<LevelIdentifier> result = new HashSet<LevelIdentifier>();
        // First add the identifier of this level to the graph.
        result.add( this.getIdentifier() );
        // Then add to the set the identifier of the perceptible levels.
        result.add( MyAwesomeLevelList.SLUMBS );
        return result;
    }
    
    /**
     * Builds the influence relation graph of this level.
     */
    private final Set<LevelIdentifier> buildInfluenceRelations( ) {
        Set<LevelIdentifier> result = new HashSet<LevelIdentifier>();
        // First add the identifier of this level to the graph.
        result.add( this.getIdentifier() );
        // Then add to the set the identifier of the influenceable levels.
        result.add( MyAwesomeLevelList.SLUMBS );
        result.add( MyAwesomeLevelList.COUNTRY );
        return result;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public LevelIdentifier getIdentifier() {
        return this.identifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<LevelIdentifier> getPerceptibleLevels() {
        return this.perceptionRelations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<LevelIdentifier> getInfluenceableLevels() {
        return this.influenceRelations;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConsistentPublicLocalDynamicState getLastConsistentState() {
        return this.mostRecentConsistentState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TransitoryPublicLocalDynamicState getLastTransitoryState() {
        return this.mostRecentTransitoryState;
    }
    
    
    // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
    // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
    // //                                       //  //
    // //    The remaining methods are managed in a later step of the   //  //
    // //   simulation design process.                    //  //
    // //                                       //  //
    // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
    // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //

    /**
     * 
     * 
     * {@inheritDoc}
     * @param currentTime
     * @return
     */
    @Override
    public SimulationTimeStamp getNextTime(SimulationTimeStamp currentTime) {
        throw new UnsupportedOperationException( 
            "This operation currently has no specification." 
        );
    }

    /**
     * {@inheritDoc}
     */
    public void makeRegularReaction(
            SimulationTimeStamp transitoryTimeMin,
            SimulationTimeStamp transitoryTimeMax,
            ConsistentPublicLocalDynamicState consistentState,
            Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
            InfluencesMap remainingInfluences
    ) {
        throw new UnsupportedOperationException( 
            "This operation currently has no specification." 
        );
    }

    /**
     * {@inheritDoc}
     */
    public void makeSystemReaction(
        SimulationTimeStamp transitoryTimeMin,
        SimulationTimeStamp transitoryTimeMax,
        ConsistentPublicLocalDynamicState consistentState,
        Collection<IInfluence> systemInfluencesToManage,
        boolean happensBeforeRegularReaction,
        InfluencesMap newInfluencesToProcess
    ) {
        throw new UnsupportedOperationException( 
            "This operation currently has no specification." 
        );
    }
}