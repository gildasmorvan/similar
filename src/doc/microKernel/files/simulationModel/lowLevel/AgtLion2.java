package fr.lgi2a.wildlifesimulation.model.agents.lion;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.lgi2a.similar.microkernel.IDynamicStateMap;
import fr.lgi2a.similar.microkernel.IGlobalMemoryState;
import fr.lgi2a.similar.microkernel.IPerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.InfluencesMap;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractAgent;
import fr.lgi2a.wildlifesimulation.model.agents.WildlifeAgentCategoriesList;
import fr.lgi2a.wildlifesimulation.model.agents.gazelle.savannah.AgtGazellePLSInSavannahLevel;
import fr.lgi2a.wildlifesimulation.model.agents.lion.savannah.AgtLionPDFSavannah;
import fr.lgi2a.wildlifesimulation.model.agents.lion.savannah.AgtLionPLSInSavannahLevel;
import fr.lgi2a.wildlifesimulation.model.influences.tosavannah.RISavannahEat;
import fr.lgi2a.wildlifesimulation.model.influences.tosavannah.RISavannahMove;
import fr.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;
import fr.lgi2a.wildlifesimulation.tools.RandomValueFactory;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;

/**
 * Models an agent from the 'Lion' category.
 */
public class AgtLion2 extends AbstractAgent {
    /**
     * Builds a "Lion" agent without initializing the global state and the public
     * local state of the agent.
     * The setter of these elements have to be called manually.
     * @param precision The precision of the double comparisons.
     * @param eatDistanceThreshold The threshold distance under which a lion can 
     * eat a prey without moving.
     * @throws IllegalArgumentException If the <code>gazellePerceptionRadius</code>
     * argument is negative.
     */
    public AgtLion2( 
        double precision,
        double eatDistanceThreshold
    ) {
        super( WildlifeAgentCategoriesList.LION );
        this.precision = precision;
        this.eatDistanceThreshold = eatDistanceThreshold;
    }
    
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // // 
   // //   Private local state of the agent. 
   // //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   
   /**
    * The precision of the double comparisons.
    */
   private final double precision;
   /**
    * The threshold distance under which a lion can eat a prey without moving.
    */
   private double eatDistanceThreshold;
    
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // // 
   // //   Decision related method of the agent. 
   // //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //

    /**
     * {@inheritDoc}
     */
    @Override
    public void decide(
        LevelIdentifier level, 
	SimulationTimeStamp time,
        IGlobalMemoryState memoryState,
        IPerceivedDataOfAgent perceivedData,
        InfluencesMap producedInfluences
    ) {
        if( level.equals( WildlifeLevelList.SAVANNAH ) ){
                this.decideFromSavannah(
			time,
                        memoryState, 
                        perceivedData, 
                        producedInfluences
                );
        } else {
                throw new IllegalStateException(
                    "The 'Lion' agetn cannot perceive from the level '" + level + "'"
                );
        }
   }
    
   /**
    * Models the decision from the Savannah level.
    */
   public void decideFromSavannah(
	SimulationTimeStamp time,
        IGlobalMemoryState memoryState,
        IPerceivedDataOfAgent perceivedData,
        InfluencesMap producedInfluences
   ){
        // First cast the perceived data into the appropriate type
        AgtLionPDFSavannah castedPData = (AgtLionPDFSavannah) perceivedData;
        // Then get the public local of this agent in the savannah level 
	// (to get its position)
        IPublicLocalStateOfAgent localState = this.getPublicLocalState( 
	   WildlifeLevelList.SAVANNAH 
	);
        AgtLionPLSInSavannahLevel castedLocalState = 
	   (AgtLionPLSInSavannahLevel) localState;
        Point2D.Double location = castedLocalState.getCoordinates();
        // Then get the list of the closest preys
        double closestDistance = Double.MAX_VALUE;
        List<AgtGazellePLSInSavannahLevel> closestPreys = 
	   new ArrayList<AgtGazellePLSInSavannahLevel>();
        AgtGazellePLSInSavannahLevel gazelle = null;
        for( 
            Iterator<IPublicLocalStateOfAgent> it = castedPData.nearbyPreysIterator();
            it.hasNext();
            gazelle = (AgtGazellePLSInSavannahLevel) it.next()
        ){
            double distanceFromGazelle = location.distance( gazelle.getCoordinates() );
            double difference = distanceFromGazelle - closestDistance;
            if( difference <= - this.precision ){
                closestPreys.clear();
                closestPreys.add( gazelle );
                closestDistance = distanceFromGazelle;
            } else if( difference < this.precision ){
                closestPreys.add( gazelle );
            }
        }
        // If there is at least one prey, choose the closest prey at random.
        if( ! closestPreys.isEmpty() ) {
            AgtGazellePLSInSavannahLevel selectedPrey = 
                    RandomValueFactory.getStrategy().randomElement( closestPreys );
            if( closestDistance - this.eatDistanceThreshold < this.precision ){
                // The prey is close enough to be eaten.
                RISavannahEat influence = new RISavannahEat( 
		    castedLocalState, 
		    selectedPrey 
		);
                producedInfluences.add( influence );
            } else {
                // The prey is too far away to be eaten: it has to move.
                RISavannahMove influence = new RISavannahMove( 
                        castedLocalState, 
                        selectedPrey.getCoordinates().getX(),
                        selectedPrey.getCoordinates().getY()
                );
                producedInfluences.add( influence );
            }
        }
   }

       
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // // 
   // //   The remaining methods are out of the scope of the example.
   // //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //

    /**
     * {@inheritDoc}
     */
    @Override
    public IPerceivedDataOfAgent perceive(
            LevelIdentifier level,
	    SimulationTimeStamp time,
            IPublicLocalStateOfAgent publicLocalStateInLevel,
            IDynamicStateMap levelsPublicLocalObservableDynamicState
    ) {
      throw new UnsupportedOperationException( 
              "This operation currently has no specification." 
      );
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void reviseMemory(
            SimulationTimeStamp time,
            Map<LevelIdentifier, 
            IPerceivedDataOfAgent> perceivedData,
            IGlobalMemoryState memoryState
    ) {
          throw new UnsupportedOperationException( 
                  "This operation currently has no specification." 
            );
    }
}
