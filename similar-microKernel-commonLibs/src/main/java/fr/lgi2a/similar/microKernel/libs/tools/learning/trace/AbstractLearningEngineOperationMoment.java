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
package fr.lgi2a.similar.microkernel.libs.tools.learning.trace;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;

/**
 * Models a moment of the simulation, <i>i.e.</i> either when the simulation engine performed an operation or at a time stamp.
 * It can be slightly before a time stamp, slightly after a time stamp or exactly at the time stamp.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public abstract class AbstractLearningEngineOperationMoment implements Comparable<AbstractLearningEngineOperationMoment> {
	/**
	 * The time stamp relatively to which this moment of the simulation is expressed.
	 */
	private SimulationTimeStamp timestamp;
	
	/**
	 * Builds a moment that will be expressed relatively to a specific time stamp.
	 * @param timestamp The time stamp relatively to which this moment of the simulation is expressed.
	 * @throws IllegalArgumentException If the argument is <code>null</code>.
	 */
	public AbstractLearningEngineOperationMoment(
			SimulationTimeStamp timestamp
	) throws IllegalArgumentException {
		if( timestamp == null ){
			throw new IllegalArgumentException( "The 'timestamp' argument cannot be null." );
		}
		this.timestamp = new SimulationTimeStamp( timestamp );
	}
	
	/**
	 * Gets the time stamp relatively to which this moment of the simulation is expressed.
	 * @return The time stamp relatively to which this moment of the simulation is expressed.
	 */
	public SimulationTimeStamp getTimestamp(){
		return this.timestamp;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public abstract boolean equals( Object other );
	
	/**
	 * {@inheritDoc}
	 */
	public abstract int hashCode( );
	
	/**
	 * {@inheritDoc}
	 */
	public abstract String toString( );
	
	/**
	 * Models a moment happening slightly before a time stamp.
	 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
	 */
	public static class Learning_EngineOperationMoment_Before extends AbstractLearningEngineOperationMoment {
		/**
		 * Builds a moment happening slightly before a time stamp.
		 * @param timestamp The time stamp relatively to which this moment of the simulation is expressed.
		 * @throws IllegalArgumentException If the argument is <code>null</code>.
		 */
		public Learning_EngineOperationMoment_Before(
				SimulationTimeStamp timestamp
		) throws IllegalArgumentException {
			super(timestamp);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compareTo(AbstractLearningEngineOperationMoment o) {
			int comparison = this.getTimestamp().compareTo( o.getTimestamp() );
			if( comparison != 0 ){
				return comparison;
			} else {
				if( o instanceof Learning_EngineOperationMoment_Before ) {
					return 0;
				} else if( o instanceof Learning_EngineOperationMoment_After ) {
					return -1;
				} else {
					throw new UnsupportedOperationException( "An instance of the '" + this.getClass().getSimpleName() + "' class cannot be " +
							"compared with an instance of the '" + o.getClass().getSimpleName() + "' class." );
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals( Object other ) {
			if( other == null || ! ( other instanceof Learning_EngineOperationMoment_Before ) ){
				return false;
			} else {
				return this.compareTo( (Learning_EngineOperationMoment_Before) other ) == 0;
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			// This hashCode method is built using the hashCode method generated by eclipse for a class
			// having a long and a boolean field. The boolean field models "before" or "after".
			final int prime = 31;
			int result = 1;
			result = prime * result + 1237;
			result = prime * result + (int) (this.getTimestamp().getIdentifier() ^ (this.getTimestamp().getIdentifier() >>> 32));
			return result;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return "Slightly before " + this.getTimestamp();
		}
	}
	
	/**
	 * Models a moment happening slightly after a time stamp.
	 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
	 */
	public static class Learning_EngineOperationMoment_After extends AbstractLearningEngineOperationMoment {
		/**
		 * Builds a moment happening slightly after a time stamp.
		 * @param timestamp The time stamp relatively to which this moment of the simulation is expressed.
		 * @throws IllegalArgumentException If the argument is <code>null</code>.
		 */
		public Learning_EngineOperationMoment_After(
				SimulationTimeStamp timestamp
		) throws IllegalArgumentException {
			super(timestamp);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int compareTo( AbstractLearningEngineOperationMoment o ) {
			int comparison = this.getTimestamp().compareTo( o.getTimestamp() );
			if( comparison != 0 ){
				return comparison;
			} else {
				if( o instanceof Learning_EngineOperationMoment_After ) {
					return 0;
				} else if( o instanceof Learning_EngineOperationMoment_Before ) {
					return 1;
				} else {
					throw new UnsupportedOperationException( "An instance of the '" + this.getClass().getSimpleName() + "' class cannot be " +
							"compared with an instance of the '" + o.getClass().getSimpleName() + "' class." );
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals( Object other ) {
			if( other == null || ! ( other instanceof Learning_EngineOperationMoment_After ) ){
				return false;
			} else {
				return this.compareTo( (Learning_EngineOperationMoment_After) other ) == 0;
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			// This hashCode method is built using the hashCode method generated by eclipse for a class
			// having a long and a boolean field. The boolean field models "before" or "after".
			final int prime = 31;
			int result = 1;
			result = prime * result + 1231;
			result = prime * result + (int) (this.getTimestamp().getIdentifier() ^ (this.getTimestamp().getIdentifier() >>> 32));
			return result;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return "Slightly after " + this.getTimestamp();
		}
	}
}
