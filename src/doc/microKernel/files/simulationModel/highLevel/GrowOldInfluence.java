package fr.lgi2a.wildlifesimulation.model.influences.tosavannah;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.influences.RegularInfluence;
import fr.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;

/**
 * An influence sent by the environment to trigger the aging of the agents.
 */
public class GrowOldInfluence extends RegularInfluence {
	/**
	 * The category of the influence, used as a unique identifier in 
	 * the reaction of the 'Savannah' level to determine the nature of the influence.
	 */
	public static final String CATEGORY = "Grow old";
	
	/**
	 * Builds an instance of this influence created during the transitory 
	 * period <code>] timeLowerBound, timeUpperBound [</code>.
	 * @param timeLowerBound The lower bound of the transitory period 
	 * during which this influence was created.
	 * @param timeUpperBound The upper bound of the transitory period 
	 * during which this influence was created.
	 */
	public GrowOldInfluence(
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound
	) {
		super(
				GrowOldInfluence.CATEGORY, 
				WildlifeLevelList.SAVANNAH,
				timeLowerBound,
				timeUpperBound
		);
	}
}
