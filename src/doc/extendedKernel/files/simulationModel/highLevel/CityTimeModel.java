package fr.lgi2a.mysimulation.model.levels.city;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.levels.ITimeModel;

/**
 * Implementation of the time evolution model of the 
 * "City" level.
 */
public class CityTimeModel implements ITimeModel {
	/**
	 * The period of the time model.
	 */
	private final int period;
	
	/**
	 * Builds an initialized instance of this time model.
	 * @param period The period of the time model.
	 * @throws IllegalArgumentException if the period is 
	 * negative or <code>null</code>.
	 */
	public CityTimeModel(
			int period
	){
		if( period <= 0 ){
			throw new IllegalArgumentException(
				"The period cannot be negative or zero."
			);
		} else {
			this.period = period;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimulationTimeStamp getNextTime(
		SimulationTimeStamp currentTime
	) {
		return new SimulationTimeStamp( currentTime, this.period );
	}
	
}
