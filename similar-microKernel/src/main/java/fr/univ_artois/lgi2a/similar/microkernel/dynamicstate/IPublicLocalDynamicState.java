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

import java.util.Set;

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;
import fr.univ_artois.lgi2a.similar.microkernel.influences.IInfluence;

/**
 * The parent interface of any public dynamic state of a level of the simulation.
 * 
 * <h1>Correspondence with theory</h1>
 * <p>
 * 	An instance of this interface models the public local dynamic state
 * 	&delta;(t, l) of a level "l" at a time "t", or the public local dynamic state 
 * 	&delta;(]t,t&prime;[, l) of a level "l" during a transitory period "]t,t&prime;[".
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public interface IPublicLocalDynamicState {
	/**
	 * Gets the level for which the public local dynamic state is defined.
	 * <p>
	 * 	It returns the identifier of "l" in the notation &delta;(t, l) or &delta;(]t,t&prime;[, l).
	 * </p>
	 * @return The level for which the public local dynamic state is defined.
	 */
	LevelIdentifier getLevel( );
	
	/**
	 * Gets the public local state of the environment contained in this public local dynamic state.
	 * <p>
	 * 	It returns the value &phi;<sub>&omega;</sub>(t, <code>getLevel( )</code>) or 
	 * 	&phi;<sub>&omega;</sub>(]t,t&prime;, <code>getLevel( )</code>).
	 * </p>
	 * @return The public local state of the environment contained in this public local dynamic state.
	 */
	ILocalStateOfEnvironment getPublicLocalStateOfEnvironment( );
	
	/**
	 * Gets the public local state of the agents lying in the level of this dynamic state.
	 * <p>
	 * 	It returns the values &phi;<sub>a</sub>(t, <code>getLevel( )</code>) (resp. &phi;<sub>a</sub>(]t,t&prime;, <code>getLevel( )</code>))
	 * 	for each agent lying in the level identified by <code>getLevel( )</code> at the time "t" (resp. during 
	 * 	the transitory period ]t,t&prime;[).
	 * </p>
	 * <p>
     * 	This method has to ensure that two consecutive iterations over this set always return its items in
     * 	the same order.
	 * </p>
	 * @return The public local state of the agents lying in the level of this dynamic state.
	 */
	Set<ILocalStateOfAgent> getPublicLocalStateOfAgents();
    
    /**
     * Gets the state dynamics of this public local dynamic state, <i>i.e.</i> the influences that are 
     * still active (being performed) when the level was in this state.
     * <p>
     * 		It returns the value &gamma;(t, <code>getLevel( )</code>) or 
     * 		&gamma;(]t,t&prime;[, <code>getLevel( )</code>).
     * </p>
     * <p>
     * 	This method has to ensure that two consecutive iterations over this set always return its items in
     * 	the same order.
     * </p>
     * @return The state dynamics of this public local dynamic state.
     */
	Set<IInfluence> getStateDynamics();
    
    /**
	 * Gets the system influences contained in the state dynamics.
	 * <p>
     * 	This method has to ensure that two consecutive iterations over this set always return its items in
     * 	the same order.
	 * </p>
	 * @return The system influences of the value returned by the {@link IPublicLocalDynamicState#getStateDynamics()} method.
	 */
	Set<IInfluence> getSystemInfluencesOfStateDynamics();
	
	/**
	 * Gets the non-system influences contained in the state dynamics.
	 * <p>
     * 	This method has to ensure that two consecutive iterations over this set always return its items in
     * 	the same order.
	 * </p>
	 * @return The non-system influences of the value returned by the 
	 * {@link IPublicLocalDynamicState#getStateDynamics()} method.
	 */
	Set<IInfluence> getRegularInfluencesOfStateDynamics();
}
