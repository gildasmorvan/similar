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
package fr.univ_artois.lgi2a.similar.microkernel.libs.probes;

import fr.univ_artois.lgi2a.similar.microkernel.IProbe;
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;

/**
 * This probe slows down the simulation so that its execution speed matches real time (in the simulation), or a specific factor 
 * F times quicker than real time.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 */
public abstract class AbstractRealTimeMatcherProbe  implements IProbe {
	/**
	 * The number of milliseconds in a second.
	 */
	protected static final int MILLISECONDS_IN_SECONDS = (int) Math.pow( 10, 3 );
	/**
	 * The number of nanoseconds in a second.
	 */
	protected static final int NANOSECONDS_IN_SECONDS = (int) Math.pow( 10, 9 );
	
	/**
	 * The acceleration factor of the simulation. 1 means that the time of the simulation has to match real time. A higher value means that 
	 * the simulation has to go faster than real time. A lower value means that the simulation has to go slower than real time.
	 */
	private double accelerationFactor;
	
	/**
	 * The time stamp of the last measure made by this probe.
	 */
	private SimulationTimeStamp previousMeasureSimulationTime;
	/**
	 * The time in milliseconds since the last measure made by this probe.
	 */
	private long previousMeasureRealTime;
	
	/**
	 * Builds a probe slowing down the simulation so that its execution speed matches a specific factor <code>accelerationFactor</code> times 
	 * quicker than real time.
	 * @param accelerationFactor The acceleration factor of the simulation. 1 means that the time of the simulation has to match real time. 
	 * A higher value means that the simulation has to go faster than real time. A lower value means that the simulation has to go slower 
	 * than real time. This value has to be strictly positive.
	 */
	public AbstractRealTimeMatcherProbe(
		double accelerationFactor
	) {
		if( accelerationFactor <= 0 ) {
			throw new IllegalArgumentException( "The acceleration factor cannot be negative or zero (was " + accelerationFactor + ")." );
		}
		this.accelerationFactor = accelerationFactor;
	}
	
	/**
	 * Builds a probe slowing down the simulation so that its execution speed matches real time.
	 */
	public AbstractRealTimeMatcherProbe() {
		this(1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtInitialTimes(
		SimulationTimeStamp initialTime,
		ISimulationEngine engine
	) {
		this.previousMeasureSimulationTime = initialTime;
		this.previousMeasureRealTime = System.nanoTime( );
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtPartialConsistentTime(
		SimulationTimeStamp halfConsistentTime,
		ISimulationEngine engine
	) {
		// First compute the simulation time elapsed between the two times.
		// This value is multiplied by 10^9 since its unit are seconds.
		double simulationTimeSinceLastMeasure = this.getTimeElapsedBetween( this.previousMeasureSimulationTime, halfConsistentTime ) * NANOSECONDS_IN_SECONDS;
		// Then compute the real time elapsed since the last display
		double newMeasureTime = System.nanoTime( );
		double realTimeSinceLastMeasure = newMeasureTime - this.previousMeasureRealTime;
		// Check if the simulation has to be slowed down.
		double difference = simulationTimeSinceLastMeasure / this.accelerationFactor - realTimeSinceLastMeasure;
		if( difference > 0 ) {
			try {
				long waitingTime = (long) ( difference * MILLISECONDS_IN_SECONDS / NANOSECONDS_IN_SECONDS );
				Thread.sleep( waitingTime );
			} catch ( InterruptedException cause ) {
				Thread.currentThread().interrupt();
			}
		}
		// Update the values read during the next observation.
		// This updates occurs every 1 second of simulation time.
		// This trick is necessary, since otherwise, the manipulated values are so small that the waiting time is not computed appropriately.
		if( simulationTimeSinceLastMeasure > NANOSECONDS_IN_SECONDS ) {
			this.previousMeasureRealTime = (long) newMeasureTime;
			this.previousMeasureSimulationTime = halfConsistentTime;
		}
	}
	
	/**
	 * Gets the time elapsed between two simulation time stamps, in seconds.
	 * @param time1 The first time stamp of the comparison.
	 * @param time2 The second time stamp of the comparison. We necessarily have time2 superior or equal to time1.
	 * @return The time elapsed between two simulation time stamps, in seconds.
	 */
	protected abstract double getTimeElapsedBetween(
		SimulationTimeStamp time1,
		SimulationTimeStamp time2
	);
	
}

