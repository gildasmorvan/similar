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
package fr.lgi2a.wildlifesimulation.model.influences.tosavannah;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.influences.RegularInfluence;
import fr.lgi2a.wildlifesimulation.model.agents.gazelle.savannah.AgtGazellePLSInSavannahLevel;
import fr.lgi2a.wildlifesimulation.model.agents.lion.savannah.AgtLionPLSInSavannahLevel;
import fr.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;

/**
 * An influence sent by a lion agent to eat a gazelle agent.
 */
public class RISavannahEat extends RegularInfluence {
	/**
	 * The category of the influence, used as a unique identifier in 
	 * the reaction of the 'Savannah' level to determine the nature of the influence.
	 */
	public static final String CATEGORY = "Eat";

	/**
	 * The predator eating a prey.
	 */
	private AgtLionPLSInSavannahLevel predator;

	/**
	 * The prey being eaten by the predator.
	 */
	private AgtGazellePLSInSavannahLevel prey;
	
	/**
	 * Builds an initialized instance of this influence.
	 * @param timeLowerBound The lower bound of the transitory period during which this influence was created.
	 * @param timeUpperBound The upper bound of the transitory period during which this influence was created.
	 * @param predator The predator eating a prey.
	 * @param prey The prey being eaten by the predator.
	 * @throws IllegalArgumentException If one of the arguments is <code>null</code>.
	 */
	public RISavannahEat(
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound,
			AgtLionPLSInSavannahLevel predator, 
			AgtGazellePLSInSavannahLevel prey 
	) {
		super(
				CATEGORY, 
				WildlifeLevelList.SAVANNAH, 
				timeLowerBound, 
				timeUpperBound
		);
		if( predator == null | prey == null ){
			throw new IllegalArgumentException(
				"The arguments cannot be null."
			);
		} else {
			this.predator = predator;
			this.prey = prey;
		}
	}

	/**
	 * Gets the predator eating a prey.
	 * @return The predator eating a prey.
	 */
	public AgtLionPLSInSavannahLevel getPredator( ){
		return this.predator;
	}

	/**
	 * Gets the prey being eaten by the predator.
	 * @return The prey being eaten by the predator.
	 */
	public AgtGazellePLSInSavannahLevel getPrey(){
		return this.prey;
	}
}
