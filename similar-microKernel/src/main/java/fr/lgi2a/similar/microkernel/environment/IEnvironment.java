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
package fr.lgi2a.similar.microkernel.environment;

import java.util.Map;

import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;

/**
 * Models the environment of the simulation.
 * 
 * <h1>Correspondence with theory</h1>
 * <p>
 * 	An instance of this interface models the environment of the simulation.
 * </p>
 * 
 * <h1>Usage</h1>
 * <p>
 * 	This is the ideal interface of an environment, from the perspective of a simulation designer.
 * 	Because of implementation constraints, the environment 
 * 	has instead to implement the {@link IEnvironment4Engine} interface.
 * </p>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public interface IEnvironment {
	/**
	 * Gets the public local state of the environment for a specific level.
	 * <p>
	 * 	This method returns the value &phi;<sub>&omega;</sub><sup>+</sup>(t, <code>levelIdentifier</code>) or 
	 * 	&phi;<sub>&omega;</sub><sup>+</sup>(]t,t&prime;[, <code>levelIdentifier</code>)
	 * 	&isin;&Phi;<sub>&omega;</sub><sup>+</sup>, where the value of ]t,t&prime;[ is
	 * 	defined by the context in which this method is called.
	 * </p>
	 * @param level The level of the public local state of the environment.
	 * @return The public local state of the environment for a specific level.
	 * @throws java.util.NoSuchElementException If no public local state was defined for the specified level.
	 */
	ILocalStateOfEnvironment getPublicLocalState( LevelIdentifier level );
	
	/**
	 * Gets the private local state of the environment for a specific level.
	 * <p>
	 * 	This method returns the value &phi;<sub>&omega;</sub><sup>-</sup>(t, <code>levelIdentifier</code>) or 
	 * 	&phi;<sub>&omega;</sub><sup>-</sup>(]t,t&prime;[, <code>levelIdentifier</code>)
	 * 	&isin;&Phi;<sub>&omega;</sub><sup>-</sup>, where the value of ]t,t&prime;[ is
	 * 	defined by the context in which this method is called.
	 * </p>
	 * @param level The level of the private local state of the environment.
	 * @return The private local state of the environment for a specific level.
	 * @throws java.util.NoSuchElementException If no private local state was defined for the specified level.
	 */
	ILocalStateOfEnvironment getPrivateLocalState( LevelIdentifier level );
	
	/**
	 * Models the natural action of the environment on the simulation, from a specific level.
	 * <p>
	 * 	This method models the application natural<sub>]t,t+dt<sub>l</sub>[,l</sub> from the theory of SIMILAR.
	 * </p>
	 * @param level The level from which the natural action of the environment is made (<i>i.e.</i> "l" in the notations).
	 * @param timeLowerBound Is the lower bound of the transitory period of the level identified by <code>level</code> from which
	 * the natural action of the environment is made (<i>i.e.</i> "t" in the notations).
	 * @param timeUpperBound Is the upper bound of the transitory period of the level identified by <code>level</code> from which
	 * the natural action of the environment is made (<i>i.e.</i> t+dt<sub>l</sub> in the notations).
	 * @param publicLocalStates All the public local states of the environment.
	 * @param privateLocalState The private local state of the environment in the level from which perception is made (<i>i.e.</i> 
	 * &phi;<sub>&omega;</sub><sup>-</sup>( t, <code>level</code> ) in the notations).
	 * @param dynamicStates The dynamic state of the various levels that can be perceived from the 
	 * level <code>level</code>. This value has previously been disambiguated by a heuristic defined in the simulation engine.
	 * @param producedInfluences The map where the influences resulting from the natural action are stored.
	 */
	void natural(
		LevelIdentifier level,
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound,
		Map<LevelIdentifier, ILocalStateOfEnvironment> publicLocalStates,
		ILocalStateOfAgent privateLocalState,
		IPublicDynamicStateMap dynamicStates,
		InfluencesMap producedInfluences
	);
}
