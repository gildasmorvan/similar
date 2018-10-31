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
package fr.univ_artois.lgi2a.similar.microkernel.dynamicstate;

import java.util.Collection;

import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;
import fr.univ_artois.lgi2a.similar.microkernel.influences.IInfluence;


/**
 * Models a modifiable public local dynamic state.
 * 
 * <p>
 * 	This interface is separated from the {@link IPublicLocalDynamicState} to prevent users from modifying the content
 * 	of a dynamic state during the behavior phase of the agent (or the natural phase of the environment).
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public interface IModifiablePublicLocalDynamicState extends IPublicLocalDynamicState {	
	/**
	 * Sets the value of the public local state of the environment in this dynamic state.
	 * @param publicLocalState The public local state of the environment.
	 * @throws IllegalArgumentException If the argument is <code>null</code>.
	 */
	void setPublicLocalStateOfEnvironment( ILocalStateOfEnvironment publicLocalState );
	
	/**
	 * Adds the public local state of an agent to this public local dynamic state.
	 * <p>
	 * 	This method does nothing if the public local state is already in this dynamic state.
	 * </p>
	 * @param publicLocalState The added public local state of the agent.
	 * @throws IllegalArgumentException If the argument is <code>null</code>.
	 */
	void addPublicLocalStateOfAgent( ILocalStateOfAgent publicLocalState );
	
	/**
	 * Removes the public local state of an agent from this public local dynamic state.
	 * @param publicLocalState The removed public local state of the agent.
	 * @throws IllegalArgumentException If the argument is <code>null</code>.
	 */
	void removePublicLocalStateOfAgent( ILocalStateOfAgent publicLocalState );
	
	/**
	 * Adds an influence to this public local dynamic state.
	 * @param influence The influence to add to this public local dynamic state.
	 * @throws IllegalArgumentException If the argument is <code>null</code>.
	 */
	void addInfluence( IInfluence influence );
	
	/**
	 * Sets the state dynamics of this public local dynamic state as a copy of the specified value.
	 * @param toCopy The set of influences that have to be contained in the state dynamics of this local dynamic state.
	 * If <code>null</code>, this method sets the state dynamics of this dynamic state to an empty set.
	 * @throws IllegalArgumentException If the argument is <code>null</code>.
	 */
	void setStateDynamicsAsCopyOf( Collection<IInfluence> toCopy );

	/**
	 * Remove all the system influences of this dynamic state.
	 */
	void clearSystemInfluences( );
	
	/**
	 * Remove all the regular influences of this dynamic state.
	 */
	void clearRegularInfluences( );
}
