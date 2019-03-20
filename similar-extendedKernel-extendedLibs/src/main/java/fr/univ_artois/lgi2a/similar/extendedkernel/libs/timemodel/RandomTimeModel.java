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
package fr.univ_artois.lgi2a.similar.extendedkernel.libs.timemodel;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.levels.ITimeModel;

/**
 * A time model using a period and a phase shift to determine the next time stamp.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public final class RandomTimeModel implements ITimeModel {
	/**
	 * The min period of the time model.
	 */
	private int minPeriod;
	/**
	 * The max period of the time model.
	 */
	private int maxPeriod;

	/**
	 * Builds a periodic time model having a specific period and phase shift.
	 * @param minPeriod The min period of the time model. This value has to be strictly positive.
	 * @param maxPeriod The max period of the time model. This value has to be strictly inferior to minPeriod.
	 * @throws IllegalArgumentException If an argument is invalid.
	 */
	public RandomTimeModel( 
		int minPeriod,
		int maxPeriod
	) {
		checkConstructorArgumentsValidity(
			minPeriod, 
			maxPeriod
		);
		this.minPeriod = minPeriod;
		this.maxPeriod = maxPeriod;
	}
	
	/**
	 * Checks the validity of the arguments of the constructor.
	 * @param minPeriod The min period of the time model. This value has to be strictly positive.
	 * @param maxPeriod The max period of the time model. This value has to be strictly > to minPeriod.
	 * @throws IllegalArgumentException If an argument is invalid.
	 */
	private static void checkConstructorArgumentsValidity(
			long minPeriod,
			long maxPeriod
	){
		if( minPeriod <= 0 ){
			throw new IllegalArgumentException( "The min period has to be a strictly positive value (was '" + minPeriod + "')." );
		} else if( maxPeriod <= minPeriod ){
			throw new IllegalArgumentException( "The max period has to be a positive value (was '" + maxPeriod + "')." );
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimulationTimeStamp getNextTime( SimulationTimeStamp currentTime ) {
		// If the simulation engine works appropriately, this method will be called either
		// if currentTime is the initial time, or if the identifier of the currentTime has the form
		return new SimulationTimeStamp(
			currentTime.getIdentifier() + (long) (this.minPeriod + 1+ PRNG.randomInt(this.maxPeriod - this.minPeriod))
		);
	}
}
