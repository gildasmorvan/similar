package fr.univ_artois.lgi2a.wildlifesimulation.model.influences.tosavannah;

import java.util.Set;

import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.influences.RegularInfluence;
import fr.univ_artois.lgi2a.wildlifesimulation.model.agents.generic.savannah.IAgtPLSInSavannah;
import fr.univ_artois.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;

/**
 * An influence sent by the environment to trigger the aging of the agents.
 */
public class RISavannahGrowOlder extends RegularInfluence {
	/**
	 * The category of the influence, used as a unique identifier in 
	 * the reaction of the 'Savannah' level to determine the nature of the influence.
	 */
	public static final String CATEGORY = "Grow older";
	
	/**
	 * The public local state of the agents growing older.
	 */
	private Set<IAgtPLSInSavannah> agents;
	
	/**
	 * Builds an instance of this influence created during the transitory 
	 * period <code>] timeLowerBound, timeUpperBound [</code>.
	 * @param timeLowerBound The lower bound of the transitory period during which this influence was created.
	 * @param timeUpperBound The upper bound of the transitory period during which this influence was created.
	 */
	public RISavannahGrowOlder(
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound
	) {
		super(
				RISavannahGrowOlder.CATEGORY, 
				WildlifeLevelList.SAVANNAH,
				timeLowerBound,
				timeUpperBound
		);
	}
	
	/**
	 * Gets the public local state of the agents growing older.
	 * @return the public local state of the agents growing older.
	 */
	public Set<IAgtPLSInSavannah> getAgent( ){
		return this.agents;
	}
	
	/**
	 * Tells that an agent has to age in reaction to this influence.
	 * @param agent The agent added to this influence.
	 */
	public void addAgent(
			IAgtPLSInSavannah agent
	){
		this.agents.add( agent );
	}
}
