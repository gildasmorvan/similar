/**
 * Copyright or © or Copr. LGI2A
 * 
 * LGI2A - Laboratoire de Genie Informatique et d'Automatique de l'Artois - EA 3926 
 * Faculte des Sciences Appliquees
 * Technoparc Futura
 * 62400 - BETHUNE Cedex
 * http://www.lgi2a.univ-artois.fr/
 * 
 * Email: gildas.morvan@univ-artois.fr
 * 
 * Contributors:
 * 	Gildas MORVAN (creator of the IRM4MLS formalism)
 * 	Yoann KUBERA (designer, architect and developer of SIMILAR)
 * 
 * This software is a computer program whose purpose is to support the
 * implementation of multi-agent-based simulations using the formerly named
 * IRM4MLS meta-model. This software defines an API to implement such 
 * simulations, and also provides usage examples.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.lgi2a.similar.extendedkernel.libs.time;

import fr.lgi2a.similar.microkernel.ITimeModel;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;

/**
 * A time model using a period and a phase shift to determine the next time stamp.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public final class PeriodicTimeModel implements ITimeModel {
	/**
	 * The period of the time model. This value has to be strictly positive.
	 */
	private long period;
	/**
	 * The phase shift of the time model.
	 */
	private long phaseShift;
	/**
	 * The initial time of the simulation.
	 */
	private SimulationTimeStamp initialTime;
	
	/**
	 * Builds a periodic time model having a specific period and phase shift.
	 * @param period The period of the time model. This value has to be strictly positive.
	 * @param phaseShift The phase shift of the time model.
	 * @param initialTime The initial time stamp of the simulation. This value cannot be <code>null</code>.
	 * @throws IllegalArgumentException If an argument is invalid.
	 */
	public PeriodicTimeModel( 
			long period,
			long phaseShift,
			SimulationTimeStamp initialTime
	) {
		this.checkConstructorArgumentsValidity(
				period, 
				initialTime
		);
		this.period = period;
		this.phaseShift = phaseShift;
		this.initialTime = initialTime;
	}
	
	/**
	 * Checks the validity of the arguments of the constructor.
	 * @param period The period of the time model. This value has to be strictly positive.
	 * @param initialTime The initial time stamp of the simulation. This value cannot be <code>null</code>.
	 * @throws IllegalArgumentException If an argument is invalid.
	 */
	private void checkConstructorArgumentsValidity(
			long period,
			SimulationTimeStamp initialTime
	){
		if( period <= 0 ){
			throw new IllegalArgumentException( "The period has to be a strictly positive value (was '" + period + "')." );
		}
		if( initialTime == null ){
			throw new IllegalArgumentException( "The initial time cannot be null." );
		}
	}
	
	/**
	 * Gets the period of the time model. This value has to be strictly positive.
	 * @return The period of the time model. This value has to be strictly positive.
	 */
	public long getPeriod( ) {
		return this.period;
	}
	
	/**
	 * Gets the phase shift of the time model.
	 * @return The phase shift of the time model.
	 */
	public long getPhaseShift( ){
		return this.phaseShift;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimulationTimeStamp getNextTime( SimulationTimeStamp currentTime ) {
		// If the simulation engine works appropriately, this method will be called either
		// if currentTime is the initial time, or if the identifier of the currentTime has the form
		// initialTimeId + phaseShift + N * period.
		if( currentTime.equals( this.initialTime ) ){
			return new SimulationTimeStamp( currentTime.getIdentifier() + this.phaseShift );
		} else {
			// Case where The currentTime has the form "initialTimeId + phaseShift + N * period"
			return new SimulationTimeStamp( currentTime.getIdentifier() + this.period );
		}
	}
}
