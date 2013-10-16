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
package fr.lgi2a.similar.microKernel;

import java.util.Set;

/**
 * Models a simulation engine, <i>i.e.</i> the object moving the simulation through time.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public interface I_SimulationEngine {
	/**
	 * Adds a probe to this simulation engine.
	 * @param identifier An unique identifier for the probe.
	 * @param probe The probe to add to this simulation engine.
	 */
	void addProbe( String identifier, I_Probe probe );
	
	/**
	 * Removes a probe from the simulation engine.
	 * @param identifier The identifier of the probe to remove.
	 * @return The removed probe, <code>null</code> if no probe having the provided identifier was registered to this engine.
	 */
	I_Probe removeProbe( String identifier );
	
	/**
	 * Lists the identifier of all the probes that are registered to this engine.
	 * @return
	 */
	Set<String> getProbesIdentifiers( );
	
	/**
	 * Initializes and then runs completely a simulation.
	 * <p>
	 * 	This method has the responsibility to call the appropriate methods of the probes at the different moments 
	 * 	of the simulation.
	 * </p>
	 * @param simulationModel The simulation model running the simulation.
     * @throws RuntimeException if an unexpected error caused the shutdown of the simulation engine.
	 */
	void runNewSimulation( I_SimulationModel simulationModel ) throws RuntimeException;
}
