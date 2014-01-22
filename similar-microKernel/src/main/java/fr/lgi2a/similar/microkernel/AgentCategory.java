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
 * The object identifying the category of an agent involved in a simulation.
 * 
 * <h1>Usage</h1>
 * <p>
 * 	To facilitate the access to agent categories, it is recommended to create 
 * 	a class containing each agent category of the simulation as a static value. This avoids
 * 	any misspelling when referring to a level.
 * </p>
 * <h2>Example</h2>
 * <p>
 * 	The following code shows how to create the agent categories for a simulation of the road traffic,
 * 	containing a "Vehicle" and "Pedestrian" agent categories.
 * </p>
<pre>
public class CategoriesOfRoadTrafficSimulation {
	public static final AgentCategory VEHICLE = new AgentCategory( "Vehicle" );
	public static final AgentCategory PEDESTRIAN = new AgentCategory( "Pedestrian" );
}
</pre>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public final class AgentCategory {
	/**
	 * The string identifier of the category.
	 */
	private final String identifier;
	
	/**
	 * Builds an instance of this class using a specific value for the agent category.
	 * @param identifier The identifier of the agent category. This value should be unique.
	 * @throws IllegalArgumentException If <code>identifier</code> is <code>null</code>.
	 */
	public AgentCategory( String identifier ) {
		if( identifier == null ){
			throw new IllegalArgumentException( "The first argument cannot be null." );
		}
		this.identifier = identifier;
	}
	
	/**
	 * Gets a printable version of the agent category.
	 * <p>
	 * 	This identifier is used in the various simulation traces defined in the common libraries.
	 * </p>
	 * @return A string representation of the agent category.
	 */
	@Override
	public String toString() {
		return this.identifier;
	}
	
	/**
	 * Check if this agent category is equal to another agent category.
	 * @param o The other object used to check equality.
	 * @return <code>true</code> if the two objects are equal, <i>i.e.</i> if they are both 
	 * agent category having the same string identifier.
	 */
	@Override
	public boolean equals( Object o ) {
		if( o == null ){
			return false;
		} else {
			if( ! ( o instanceof AgentCategory )  ){
				return false;
			} else {
				AgentCategory otherIdentifier = (AgentCategory) o;
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
