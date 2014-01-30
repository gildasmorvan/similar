package fr.lgi2a.wildlifesimulation.model.influences;

import fr.lgi2a.similar.microkernel.LevelIdentifier;
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
	 * Builds an instance of this influence
	 */
	public GrowOldInfluence(
			String category, 
			LevelIdentifier targetLevel
	) {
		super(
				GrowOldInfluence.CATEGORY, 
				WildlifeLevelList.SAVANNAH
		);
	}
}
