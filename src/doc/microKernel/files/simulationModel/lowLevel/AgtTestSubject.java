package fr.univ_artois.lgi2a.underpressure.model.agents.testsubject;

import java.util.Map;

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractAgent;
import fr.univ_artois.lgi2a.underpressure.model.agents.SimulationAgentCategories;
import fr.univ_artois.lgi2a.underpressure.model.agents.testsubject.physical.AgtTestSubjectPDFPhysical;
import fr.univ_artois.lgi2a.underpressure.model.agents.testsubject.social.AgtTestSubjectPDFSocialLevel;
import fr.univ_artois.lgi2a.underpressure.model.levels.SimulationLevels;

/**
 * Models an agent from the 'Test subject' category.
 */
public class AgtTestSubject extends AbstractAgent {
	/**
	 * Builds a "Test subject" agent without initializing the global, the public
	 * local and the private state of the agent.
	 * The setter of these elements have to be called manually.
	 */
	public AgtTestSubject( ) {
		super( SimulationAgentCategories.TEST_SUBJECT );
	}

	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
	// // 
	// //   Global state revision related method of the agent.
	// //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //

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
		AgtTestSubjectPDFSocialLevel socialPdf = (AgtTestSubjectPDFSocialLevel) 
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


	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
	// // 
	// //   Methods that are out of the scope of this example.
	// //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPerceivedData perceive(
		LevelIdentifier level,
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound,
		Map<LevelIdentifier, ILocalStateOfAgent> publicLocalStates,
		ILocalStateOfAgent privateLocalState,
		IPublicDynamicStateMap dynamicStates
	) {     
		throw new UnsupportedOperationException( 
			"This operation currently has no specification." 
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decide(
		LevelIdentifier levelId,
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound, 
		IGlobalState globalState,
		ILocalStateOfAgent publicLocalState,
		ILocalStateOfAgent privateLocalState, 
		IPerceivedData perceivedData,
		InfluencesMap producedInfluences
	) {
		throw new UnsupportedOperationException( 
			"This operation currently has no specification." 
		);
	}
}
