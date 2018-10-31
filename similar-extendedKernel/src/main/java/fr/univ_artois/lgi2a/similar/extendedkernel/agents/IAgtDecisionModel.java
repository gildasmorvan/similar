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

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;

/**
 * Models the decision process used by an agent to make decision from a specific level.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public interface IAgtDecisionModel {
	/**
	 * Gets the level from which the decision is made.
	 * @return The level from which the decision is made.
	 */
	LevelIdentifier getLevel( );
	
	/**
	 * Produces the influences resulting from the decisions of an agent from the level identified by
	 * {@link IAgtDecisionModel#getLevel()}, for a specific transitory period.
	 * <p>
	 * 	This method corresponds to the application decision<sub>a, ]t,t+dt<sub>l</sub>[,l</sub> of this agent.
	 * </p>
	 * @param timeLowerBound Is the lower bound of the transitory period for which the decision is made by this 
	 * agent (<i>i.e.</i> "t" in the notations).
	 * @param timeUpperBound Is the upper bound of the transitory period for which the decision is made by this 
	 * agent (<i>i.e.</i> "t+dt" in the notations).
	 * @param globalState The revised global state of the agent when it made a decision (<i>i.e.</i> &mu;<sub>a</sub>(t+dt) in the 
	 * notations).
	 * @param publicLocalState The public local state of the agent in the level from which decision is made (<i>i.e.</i> 
	 * &phi;<sub>a</sub><sup>+</sup>( t, <code>level</code> ) in the notations).
	 * @param privateLocalState The private local state of the agent in the level from which decision is made (<i>i.e.</i> 
	 * &phi;<sub>a</sub><sup>-</sup>( t, <code>level</code> ) in the notations).
	 * @param perceivedData The data that were perceived about the level identified by <code>levelId</code> and its perceptible levels.
	 * @param producedInfluences The map where the influences resulting from the decisions are stored.
	 */
	void decide(
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound,
			IGlobalState globalState,
			ILocalStateOfAgent publicLocalState,
			ILocalStateOfAgent privateLocalState,
			IPerceivedData perceivedData,
			InfluencesMap producedInfluences
	);
}
