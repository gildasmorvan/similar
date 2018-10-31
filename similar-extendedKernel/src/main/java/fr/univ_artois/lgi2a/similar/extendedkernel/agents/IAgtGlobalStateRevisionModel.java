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
package fr.univ_artois.lgi2a.similar.extendedkernel.agents;

import java.util.Map;

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData;

/**
 * Models the global state revision process used by an agent to update its global state.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public interface IAgtGlobalStateRevisionModel {
	/**
	 * Revises the content of the global state of the agent, using the previous value of its global state and
	 * the data that were lastly perceived by the agent.
	 * <p>
	 * 	This method corresponds to the application memoryRev<sub>a, ]t,t+dt[</sub> of this agent.
	 * </p>
	 * <p>
	 * 	As a side effect of this method call, the value &mu;<sub>a</sub>( t ) of the argument <code>globalState</code> is updated 
	 * 	to become &mu;<sub>a</sub>(]t,t+dt[)
	 * </p>
	 * @param timeLowerBound Is the lower bound of the transitory period for which the global state revision is made by this 
	 * agent (<i>i.e.</i> "t" in the notations).
	 * @param timeUpperBound Is the upper bound of the transitory period for which the global state revision is made by this 
	 * agent (<i>i.e.</i> "t+dt" in the notations).
	 * @param perceivedData The map containing the data that were lastly perceived from the various levels of the simulation.
	 * @param globalState The previous value of the global state of the agent being updated by this method call.
	 */
	void reviseGlobalState(
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound,
			Map<LevelIdentifier, IPerceivedData> perceivedData, 
			IGlobalState globalState 
	);
}
