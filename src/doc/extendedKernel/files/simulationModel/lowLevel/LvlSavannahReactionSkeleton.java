package fr.univ_artois.lgi2a.wildlifesimulation.model.levels.savannah;

import java.util.Collection;
import java.util.Set;

import fr.univ_artois.lgi2a.similar.extendedkernel.levels.ILevelReactionModel;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar.microkernel.influences.IInfluence;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;

/**
 * Models the user-side of the reaction of the "Savannah" level to the influence it 
 * received during its most recent transitory period.
 */
public class LvlSavannahReactionSkeleton implements ILevelReactionModel {
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
			"This method is not yet implemented."
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
				"This method is not yet implemented."
		);
	}
}
