package fr.lgi2a.wildlifesimulation.initializations;

import fr.lgi2a.similar.extendedkernel.simulationmodel.IEndCriterionModel;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;

/**
 * A simulation end criterion stopping the simulation as long as the
 * "Savannah" level contains no more agents.
 */
public class EndCriterionNoAgents implements IEndCriterionModel {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFinalTimeOrAfter(
			SimulationTimeStamp currentTime,
			ISimulationEngine engine
	) {
		return engine.getAgents( WildlifeLevelList.SAVANNAH ).isEmpty( );
	}
}