package fr.lgi2a.underpressure.model.agents.testsubject;

import java.util.Map;

import fr.lgi2a.similar.extendedkernel.agents.IAgtGlobalStateRevisionModel;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.lgi2a.underpressure.model.agents.testsubject.physical.AgtTestSubjectPDFPhysical;
import fr.lgi2a.underpressure.model.agents.testsubject.social.AgtTestSubjectPDFSocial;
import fr.lgi2a.underpressure.model.levels.SimulationLevels;

/**
 * The global state revision model of a 'Test subject' agent.
 */
public class AgtTestSubjectGSRevisionModel implements IAgtGlobalStateRevisionModel {
    /**
     * {@inheritDoc}
     */
    @Override
    public void reviseGlobalState(
            SimulationTimeStamp timeLowerBound,
            SimulationTimeStamp timeUpperBound,
            Map<LevelIdentifier, IPerceivedData> perceivedData,
            IGlobalState globalState
    ) {
        // First cast the global state into the appropriate class.
        AgtTestSubjectGS2 castedGlobalState = (AgtTestSubjectGS2) globalState;
        // Then get the number of individuals from the perceived data from the 
        // 'physical' level
        AgtTestSubjectPDFPhysical physicalPdf = (AgtTestSubjectPDFPhysical) 
                perceivedData.get( SimulationLevels.PHYSICAL );
        int indivNum = physicalPdf.getIndividualNum( );
        // Compute the first part of the stress
        double physicalStress = Math.min( 
                indivNum, 
                castedGlobalState.getIndividualNumberStressThreshold() 
        );
        physicalStress /= castedGlobalState.getIndividualNumberStressThreshold();
        // Get the number of social interactions from the perceived data from the 
        // 'social' level
        AgtTestSubjectPDFSocial socialPdf = (AgtTestSubjectPDFSocial) 
            perceivedData.get( SimulationLevels.SOCIAL );
        int interactionsNum = socialPdf.getInteractionNum();
        // Compute the second part of the stress
        double exponent = - Math.pow(
            interactionsNum - castedGlobalState.getPreferredNumberOfInteractions(), 
            2
        );
        double socialStress = 1 - Math.exp( exponent / 2 );
        // Finally set the new stress value of the global state.
        castedGlobalState.setStressDegree(
            (
                ( 1 + physicalStress ) * ( 1 + socialStress ) - 1
            ) / 3
        );
    }
}
