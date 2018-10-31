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
 * 		  hassane.abouaissa@univ-artois.fr
 * 
 * Contributors:
 * 	Hassane ABOUAISSA (designer)
 * 	Gildas MORVAN (designer, creator of the IRM4MLS formalism)
 * 	Yoann KUBERA (designer, architect and developer of SIMILAR)
 * 
 * This software is a computer program whose purpose is run road traffic
 * simulations using a dynamic hybrid approach.
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
package fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.influences.tomicro;

import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.agents.cell.micro.AgtCellPLSInMicroLevel;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.levels.LambdaLifeLevelList;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.influences.RegularInfluence;

/**
 * An influence sent by the cells to change their state.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 */
public class NextStateInfluence extends RegularInfluence {
	/**
	 * The category of the influence, used as a unique identifier in 
	 * the reaction of the 'micro' level to determine the nature of the influence.
	 */
	public static final String CATEGORY = "next state";
	/**
	 * The wanted next state.
	 */
	private boolean nextState;
	/**
	 * The public local state that is going to change
	 */
	private AgtCellPLSInMicroLevel target;
	
	/**
	 * Builds an instance of this influence created during the transitory 
	 * period <code>] timeLowerBound, timeUpperBound [</code>.
	 * @param timeLowerBound The lower bound of the transitory period 
	 * during which this influence was created.
	 * @param timeUpperBound The upper bound of the transitory period 
	 * during which this influence was created.
	 * @param nextState the next state of the cell
	 * @param target the target public local state of the influence
	 */
	public NextStateInfluence(
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound, 
			boolean nextState, 
			AgtCellPLSInMicroLevel target
	) {
		super(
			CATEGORY, 
			LambdaLifeLevelList.MICRO, 
			timeLowerBound, 
			timeUpperBound
		);
		this.nextState = nextState;
		this.target = target;
	}

	/**
	 * Gets the wanted next state.
	 * @return The wanted next state.
	 */
	public boolean getNextState( ){
		return this.nextState;
	}
	
	/**
	 * Gets the public local state that is going to change.
	 * @return The public local state that is going to change.
	 */
	public AgtCellPLSInMicroLevel getTarget( ) {
		return this.target;
	}
}
