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

/**
 * The object identifying one level involved in a simulation.
 * 
 * <h1>Usage</h1>
 * <p>
 * 	To facilitate the access to level identifiers, it is recommended to create 
 * 	a class containing the identifier of each level as a static value. This avoids
 * 	any misspelling when referring to a level.
 * </p>
 * <h2>Example</h2>
 * <p>
 * 	The following code shows how to create the level identifiers for a simulation of the road traffic,
 * 	containing a "Traffic" level where vehicles move in the infrastructure of the road network, and a 
 * 	"GPS" level where the GPS tells vehicles how they can navigate.
 * </p>
<pre>
public class LevelsOfRoadTrafficSimulation {
	public static final LevelIdentifier TRAFFICLEVEL = new LevelIdentifier( "Traffic level" );
	public static final LevelIdentifier GPSLEVEL = new LevelIdentifier( "GPS level" );
}
</pre>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public final class LevelIdentifier {
	/**
	 * The string identifier of the level.
	 */
	private final String identifier;
	
	/**
	 * Builds an instance of this class using a specific value for the level identifier.
	 * @param identifier The identifier of the level. This value should be unique.
	 * @throws IllegalArgumentException If <code>identifier</code> is <code>null</code>.
	 */
	public LevelIdentifier( String identifier ) throws IllegalArgumentException {
		if( identifier == null ){
			throw new IllegalArgumentException( "The first argument cannot be null." );
		}
		this.identifier = identifier;
	}
	
	/**
	 * Gets a printable version of the level identifier.
	 * <p>
	 * 	This identifier is used in the various simulation traces defined in the common libraries.
	 * </p>
	 * @return A string representation of the level identifier.
	 */
	@Override
	public String toString() {
		return this.identifier;
	}
	
	/**
	 * Check if this level identifier is equal to another level identifier.
	 * @param o The other object used to check equality.
	 * @return <code>true</code> if the two objects are equal, <i>i.e.</i> if they are both 
	 * level identifiers having the same string identifier.
	 */
	@Override
	public boolean equals( Object o ) {
		if( o == null ){
			return false;
		} else {
			if( ! ( o instanceof LevelIdentifier )  ){
				return false;
			} else {
				LevelIdentifier otherIdentifier = (LevelIdentifier) o;
				return this.identifier.equals( otherIdentifier.identifier );
			}
		}
	}
	
	/**
	 * Gets the hash code of this object.
	 * @return The hash code of this object.
	 */
	@Override
	public int hashCode(){
		return this.identifier.hashCode();
	}
}
