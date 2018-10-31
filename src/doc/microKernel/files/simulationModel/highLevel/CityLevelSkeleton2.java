package fr.univ_artois.lgi2a.mysimulation.model.levels.city;

import java.util.Collection;
import java.util.Set;

import fr.univ_artois.lgi2a.mysimulation.model.levels.MyAwesomeLevelList;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar.microkernel.influences.IInfluence;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractLevel;

/**
 * The model of the "City" level.
 */
public class CityLevelSkeleton2 extends AbstractLevel {    
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
    public CityLevelSkeleton2( SimulationTimeStamp initialTime ) {
        super( initialTime, MyAwesomeLevelList.CITY );
        this.buildPerceptionRelations( );
        this.buildInfluenceRelations( );
    }
    
    /**
     * Builds the perception relation graph of this level.
     */
    private final void buildPerceptionRelations( ) {
        // The identifier of this level is already in the graph.
        // Thus, add to the set the identifier of the perceptible levels.
        this.addPerceptibleLevel( MyAwesomeLevelList.SLUMBS );
    }
    
    /**
     * Builds the influence relation graph of this level.
     */
    private final void buildInfluenceRelations( ) {

        // The identifier of this level is already in the graph.
        // Thus, add to the set the identifier of the influenceable levels.
        this.addInfluenceableLevel( MyAwesomeLevelList.SLUMBS );
        this.addInfluenceableLevel( MyAwesomeLevelList.COUNTRY );
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
     * {@inheritDoc}
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
    @Override
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
    @Override
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