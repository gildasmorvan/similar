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
package fr.univ_artois.lgi2a.similar.microkernel;

import java.util.Map;
import java.util.Set;

import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.TransitoryPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar.microkernel.environment.IEnvironment4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.levels.ILevel;

/**
 * Models a simulation engine, <i>i.e.</i> the object moving the simulation through time.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 * 
 */
public interface ISimulationEngine {
	/**
	 * Adds a probe to this simulation engine.
	 * @param identifier An unique identifier for the probe.
	 * @param probe The probe to add to this simulation engine.
	 * @throws IllegalArgumentException If the arguments are <code>null</code>, or if a probe is already defined for this identifier.
	 */
	void addProbe( String identifier, IProbe probe );
	
	/**
	 * Removes a probe from the simulation engine.
	 * @param identifier The identifier of the probe to remove.
	 * @return The removed probe, <code>null</code> if no probe having the provided identifier was registered to this engine.
	 * @throws IllegalArgumentException If the arguments are <code>null</code>.
	 */
	IProbe removeProbe( String identifier );
	
	/**
	 * Lists the identifier of all the probes that are registered to this engine.
	 * @return The identifier of all the probes that are registered to this engine.
	 */
	Set<String> getProbesIdentifiers( );
	
	/**
	 * Requests the abortion of the simulation currently running with this simulation engine.
	 * In response, the simulation engine will stop the simulation once the simulation reaches a partly-consistent state.
	 */
	void requestSimulationAbortion( );
	
	/**
	 * Initializes and then runs completely a simulation.
	 * <p>
	 * 	This method has the responsibility to call the appropriate methods of the probes at the different moments 
	 * 	of the simulation.
	 * </p>
	 * @param simulationModel The simulation model running the simulation.
	 * @throws IllegalArgumentException If the arguments are <code>null</code>.
     * @throws ExceptionSimulationAborted if the simulation has ended because it was aborted by the user.
	 */
	void runNewSimulation( ISimulationModel simulationModel );
	
	/**
	 * Gets the current dynamic states of the simulation.
	 * <h2>Usage</h2>
	 * <p>
	 * 	This method is used in probes to read the data about the simulation, when the simulation reaches a time stamp.
	 * </p>
	 * @return The dynamic state of the simulation, containing either consistent states (the current time stamp of the simulation is equal
	 * to the time stamp of the level) or transitory states (in the other case).
	 */
	IPublicDynamicStateMap getSimulationDynamicStates( );
	
	/**
	 * Gets the set of all the agents lying in the simulation.
	 * <h2>Usage</h2>
	 * <p>
	 * 	This method is used in probes to read the data about the simulation, when the simulation reaches a time stamp.
	 * </p>
	 * <p>
     * 	This method has to ensure that two consecutive iterations over this set always return its items in
     * 	the same order.
	 * </p>
	 * @return The set of all the agents lying in the simulation.
	 */
	Set<IAgent4Engine> getAgents( );
	
	/**
	 * Gets the set of level identifiers contained in the simulation.
	 * <p>
     * 	This method has to ensure that two consecutive iterations over this set always return its items in
     * 	the same order.
	 * </p>
	 * @return The list of levels contained in the simulation.
	 */
	Set<LevelIdentifier> getLevelIdentifiers( );
	
	/**
	 * Gets the list of levels contained in the simulation.
	 * <p>
     * 	This method has to ensure that two consecutive iterations over this set always return its items in
     * 	the same order.
	 * </p>
	 * @return The list of levels contained in the simulation.
	 */
	Map<LevelIdentifier, ILevel> getLevels( );
	
	/**
	 * Gets the set of all the agents lying in a specific level of the simulation.
	 * <h2>Usage</h2>
	 * <p>
	 * 	This method is used in probes to read the data about the simulation, when the simulation reaches a time stamp.
	 * </p>
	 * <p>
     * 	This method has to ensure that two consecutive iterations over this set always return its items in
     * 	the same order.
	 * </p>
	 * @param level The levels where to get the agents.
	 * @return The set of all the agents lying in a specific level of in the simulation.
	 * @throws java.util.NoSuchElementException If no such level was defined for the simulation.
	 */
	Set<IAgent4Engine> getAgents( LevelIdentifier level );
	
	/**
	 * Gets the environment the simulation.
	 * <h2>Usage</h2>
	 * <p>
	 * 	This method is used in probes to read the data about the simulation, when the simulation reaches a time stamp.
	 * </p>
	 * @return The environment of the simulation.
	 */
	IEnvironment4Engine getEnvironment( );
	
	/**
	 * Disambiguates a public local dynamic state, <i>i.e.</i> transforms a transitory state into a fully observable state.
	 * <p>
	 * 	This operation can introduce biases since it provides an estimation of the real state of a level, using the information 
	 * 	stored into a transitory dynamic state.
	 * </p>
	 * @param transitoryDynamicState The transitory state for which a disambiguation is computed.
	 * @return the observable dynamic state corresponding to the disambiguation of the transitory dynamic state.
	 */
	ConsistentPublicLocalDynamicState disambiguation( TransitoryPublicLocalDynamicState transitoryDynamicState );
}
