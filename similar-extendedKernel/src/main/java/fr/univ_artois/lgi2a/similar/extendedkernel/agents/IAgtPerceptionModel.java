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
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;

/**
 * Models the perception process used by an agent to produce perceived data from a specific level.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public interface IAgtPerceptionModel {
	/**
	 * Gets the level from which the perception is made.
	 * @return The level from which the perception is made.
	 */
	LevelIdentifier getLevel( );

	/**
	 * Creates the data perceived by an agent located in the level identified by {@link IAgtPerceptionModel#getLevel()}.
	 * <p>
	 * 	This method corresponds to the application perception<sub>a, ]t,t+dt<sub>l</sub>[, l</sub> of this agent.
	 * </p>
	 * @param timeLowerBound Is the lower bound of the transitory period of the level identified by <code>level</code> from which
	 * the perception is made by this agent (<i>i.e.</i> "t" in the notations).
	 * @param timeUpperBound Is the upper bound of the transitory period of the level identified by <code>level</code> from which
	 * the perception is made by this agent (<i>i.e.</i> t+dt<sub>l</sub> in the notations).
	 * @param publicLocalStates All the public local states of the agent.
	 * @param privateLocalState The private local state of the agent in the level from which perception is made (<i>i.e.</i> 
	 * &phi;<sub>a</sub><sup>-</sup>( t, <code>level</code> ) in the notations).
	 * @param dynamicStates The dynamic state of the various levels that can be perceived from the 
	 * level <code>level</code>. This value has previously been disambiguated by a heuristic defined in the simulation engine.
	 * @return The data being perceived by the agent from the level identified by <code>level</code>, for the transitory period 
	 * ]<code>timeLowerBound</code>, <code>timeUpperBound</code>[.
	 */
	IPerceivedData perceive( 
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound,
			Map<LevelIdentifier, ILocalStateOfAgent> publicLocalStates,
			ILocalStateOfAgent privateLocalState,
			IPublicDynamicStateMap dynamicStates
	);
}
