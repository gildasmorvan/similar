package fr.univ_artois.lgi2a.wildlifesimulation.model.agents.lion.savannah;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.gazelle.savannah.AgtGazellePLSInSavannahLevel;
import fr.univ_artois.lgi2a.wildlifesimulation.model.influences.tosavannah.RISavannahEat;
import fr.univ_artois.lgi2a.wildlifesimulation.model.influences.tosavannah.RISavannahMove;
import fr.univ_artois.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;
import fr.univ_artois.lgi2a.wildlifesimulation.tools.RandomValueFactory;

/**
 * The decision model of a "Lion" agent from the "Savannah" level.
 */
public class AgtLionDecisionFromSavannah2 extends AbstractAgtDecisionModel {
    /**
     * Builds an initialized instance of this decision model.
     */
    public AgtLionDecisionFromSavannah2( ) {
        super( WildlifeLevelList.SAVANNAH );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void decide(
            SimulationTimeStamp timeLowerBound,
            SimulationTimeStamp timeUpperBound, IGlobalState globalState,
            ILocalStateOfAgent publicLocalState,
            ILocalStateOfAgent privateLocalState, 
            IPerceivedData perceivedData,
            InfluencesMap producedInfluences
    ) {
        // First cast the perceived data into the appropriate type
        AgtLionPDFSavannah castedPData = (AgtLionPDFSavannah) perceivedData;
        // Then cast in the appropriate type the public local state of this agent 
        // in the Savannah level and get the location of the agent.
        AgtLionPLSInSavannahLevel castedPublicLocalState = 
                (AgtLionPLSInSavannahLevel) publicLocalState;
        Point2D.Double location = castedPublicLocalState.getCoordinates();
        // Then get the list of the closest preys
        double closestDistance = Double.MAX_VALUE;
        List<AgtGazellePLSInSavannahLevel> closestPreys = 
                new ArrayList<AgtGazellePLSInSavannahLevel>();
        AgtGazellePLSInSavannahLevel gazelle = null;
        for( 
                Iterator<ILocalStateOfAgent> it = castedPData.nearbyPreysIterator();
                it.hasNext();
                gazelle = (AgtGazellePLSInSavannahLevel) it.next()
                ){
            double distanceFromGazelle = location.distance( gazelle.getCoordinates() );
            if( distanceFromGazelle < closestDistance ){
                closestPreys.clear();
                closestPreys.add( gazelle );
                closestDistance = distanceFromGazelle;
            } else if( distanceFromGazelle == closestDistance ){
                closestPreys.add( gazelle );
            }
        }
        // Cast in the appropriate type the private local state of this agent
        // in the Savannah level.
        AgtLionHLSInSavannahLevel castedPrivateState = 
                (AgtLionHLSInSavannahLevel) privateLocalState;
        // If there is at least one prey, choose the closest prey at random.
        if( ! closestPreys.isEmpty() ) {
            AgtGazellePLSInSavannahLevel selectedPrey = 
                    RandomValueFactory.getStrategy().randomElement( closestPreys );
            if( closestDistance < castedPrivateState.getEatDistanceThreshold() ){
                // The prey is close enough to be eaten.
                RISavannahEat influence = new RISavannahEat(
                    castedPublicLocalState, 
                    selectedPrey, 
                    timeLowerBound, 
                    timeUpperBound
                );
                producedInfluences.add( influence );
            } else {
                // The prey is too far away to be eaten: the lion has to move.
                RISavannahMove influence = new RISavannahMove(
                        castedPublicLocalState, 
                        selectedPrey.getCoordinates().getX(),
                        selectedPrey.getCoordinates().getY(),
                        timeLowerBound, 
                        timeUpperBound
                        );
                producedInfluences.add( influence );
            }
        }
    }
}
