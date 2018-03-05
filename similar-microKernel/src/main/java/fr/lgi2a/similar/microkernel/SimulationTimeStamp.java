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
package fr.lgi2a.similar.microkernel;

import java.io.Serializable;

/**
 * Models a time stamp (<i>i.e.</i> the beginning of a time step) of the simulation.
 * 
 * <h1>Correspondence with theory</h1>
 * <p>
 * 	In the theory of similar, an instance of this class models an element from 
 * 	the &#x1D54B; set of time stamps.
 * </p>
 * 
 * <h1>Usage</h1>
 * <p>
 * 	Each time stamp has a unique identifier, modeled as a <code>long</code> value. 
 *	This identifier is used to order time stamps the ones relatively to the others.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public final class SimulationTimeStamp implements Comparable<SimulationTimeStamp>, Serializable, Cloneable {
	/**
	 * The serialization ID of this class.
	 */
	private static final long serialVersionUID = 5067374981031091877L;
	
	/**
	 * A value used in the computation of the hashcode.
	 */
	private static final int SHIFT_VALUE = 32;
	
	/**
	 * The unique identifier of this time stamp.
	 */
	private final long identifier;
	
	/**
	 * Builds a time stamp having a specific identifier.
	 * @param identifier The identifier of the time stamp.
	 */
	public SimulationTimeStamp( long identifier ) {
		this.identifier = identifier;
	}
	
	/**
	 * Builds a time stamp as a copy of another time stamp.
	 * @param toCopy The time stamp to copy.
	 * @throws IllegalArgumentException If the argument is <code>null</code>.
	 */
	public SimulationTimeStamp( SimulationTimeStamp toCopy ) {
		if( toCopy == null ){
			throw new IllegalArgumentException( "The 'toCopy' argument cannot be null." );
		}
		this.identifier = toCopy.identifier;
	}
	
	/**
	 * Builds a time stamp which identifier is defined relatively to the identifier 
	 * of another time stamp.
	 * @param reference The time stamp used as a reference to create the identifier 
	 * of the new time stamp.
	 * @param shift The value added to the identifier of the reference to obtain the 
	 * identifier of the new time stamp.
	 * @throws IllegalArgumentException If the argument is <code>null</code>.
	 * @throws ArithmeticException If the identifier of the new time stamp exceeds 
	 * the capacity of the {@link Long} type.
	 */
	public SimulationTimeStamp( 
			SimulationTimeStamp reference,
			int shift
	) {
		if( reference == null ){
			throw new IllegalArgumentException( "The 'toCopy' argument cannot be null." );
		}
		if( 
				( shift > 0 && Long.MAX_VALUE - reference.identifier > shift ) 
				||
				( shift < 0 && reference.identifier - Long.MIN_VALUE > - shift )
		){
			// The capacity of the "long" type is exceeded.
			throw new ArithmeticException( 
				"The identifier of the new time stamp exceeds the capacity " +
				"of Long values." 
			);
		} else {
			this.identifier = reference.identifier + shift;
		}
	}
	
	/**
	 * Gets the unique identifier of this time stamp.
	 * @return The unique identifier of this time stamp.
	 */
	public long getIdentifier( ) {
		return this.identifier;
	}

	/**
	 * Compares the identifier of this time stamp to the identifier of another time stamp.
	 * @param otherTimeStamp The other time stamp used in the comparison.
	 * @return the difference between the identifier of this time stamp and the identifier of the parameter:
	 * <ul>
	 * 	<li>0 if the identifiers are identical</li>
	 * 	<li>a negative value if the identifier of this time stamp is lesser than the identifier of the time stamp 
	 * 	of the parameter</li>
	 * 	<li>a positive value if the identifier of this time stamp is greater than the identifier of the time stamp 
	 * 	of the parameter</li>
	 * </ul>
	 * If the difference goes out of the bounds of the {@link Integer} type, the value is either set to {@link Integer#MAX_VALUE}
	 * or to {@link Integer#MIN_VALUE}.
	 * @throws IllegalArgumentException If the parameter is <code>null</code>.
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo( SimulationTimeStamp otherTimeStamp ) {
		if( otherTimeStamp == null ) {
			throw new IllegalArgumentException( "The first parameter of this method cannot be null." );
		} else {
			long difference = this.identifier - otherTimeStamp.identifier;
			if( difference > Integer.MAX_VALUE || difference < Integer.MIN_VALUE) {
				throw new InvalidIntegerValueException();
			} else {
				return (int) difference;
			}
		}
	}

	/**
	 * Returns a hash code value for this object. This hash code is built using the <code>identifier</code> field
	 * of this class.
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		// This code was automatically generated using the eclipse framework.
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) ( this.identifier ^ ( this.identifier >>> SHIFT_VALUE ) );
		return result;
	}

	/**
	 * Returns <code>true</code> if this object models the same time stamp than the parameter.
	 * Two time stamps are identical if their identifiers are equal.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		// This code was automatically generated using the eclipse framework.
		if ( this == obj ) {
			return true;
		} else if ( obj == null ) {
			return false;
		} else if ( this.getClass() != obj.getClass() ) {
			return false;
		} else {
			SimulationTimeStamp other = (SimulationTimeStamp) obj;
			return this.identifier == other.identifier;
		}
	}
	
	/**
	 * Creates a copy of this object, having the same identifier.
	 * @return A copy of this object.
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		return new SimulationTimeStamp( this.identifier );
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString( ) {
		return "t(" + this.identifier + ")";
	}
}
