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
package fr.univ_artois.lgi2a.similar.microkernel.influences;

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;

/**
 * Provides a default behavior of the generic methods of the {@link IInfluence} class.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public abstract class AbstractInfluence implements IInfluence {
	/**
	 * The category of the influence, as described in the {@link IInfluence#getCategory()} method.
	 */
	private final String category;
	/**
	 * The level where the reaction to the influence is managed, as described in the {@link IInfluence#getTargetLevel()} method.
	 */
	private final LevelIdentifier targetLevel;
	/**
	 * The lower bound of the transitory period during which this influence was created.
	 */
	private final SimulationTimeStamp timeLowerBound;
	/**
	 * The upper bound of the transitory period during which this influence was created.
	 */
	private final SimulationTimeStamp timeUpperBound;
	
	/**
	 * Builds an influence having a specific category and a specific target level.
	 * @param category The category of the influence, as described in the {@link IInfluence#getCategory()} method.
	 * @param targetLevel The identifier of the level whose reaction will process this influence.
	 * @param timeLowerBound The lower bound of the transitory period during which this influence was created.
	 * @param timeUpperBound The upper bound of the transitory period during which this influence was created.
	 * @throws IllegalArgumentException If one of the arguments is <code>null</code>.
	 */
	public AbstractInfluence( 
			String category, 
			LevelIdentifier targetLevel,
			SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound
	) {
		if( category == null ){
			throw new IllegalArgumentException( "The 'category' argument of this method cannot be null." );
		} else if( targetLevel == null ){
			throw new IllegalArgumentException( "The 'targetLevel' argument of this method cannot be null." );
		} else if( timeLowerBound == null ){
			throw new IllegalArgumentException( "The 'timeLowerBound' argument of this method cannot be null." );
		} else if( timeUpperBound == null ){
			throw new IllegalArgumentException( "The 'timeUpperBound' argument of this method cannot be null." );
		}
		this.category = category;
		this.targetLevel = targetLevel;
		this.timeLowerBound = timeLowerBound;
		this.timeUpperBound = timeUpperBound;
	}
	
	/**
	 * Returns the category defined in the constructor of this class.
	 * @see IInfluence#getCategory()
	 */
	@Override
	public String getCategory() {
		return this.category;
	}

	/**
	 * Returns the target level defined in the constructor of this class.
	 * @see IInfluence#getTargetLevel()
	 */
	@Override
	public LevelIdentifier getTargetLevel() {
		return this.targetLevel;
	}
	
	/**
	 * {@inheritDoc}}
	 */
	@Override
	public SimulationTimeStamp getTimeLowerBound( ){
		return this.timeLowerBound;
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public SimulationTimeStamp getTimeUpperBound( ){
		return this.timeUpperBound;
	}
	
	/**
	 * Uses the category and the target level of the influence to build a printable version of this object.
	 * @return The concatenation of the category and the target level of the influence.
	 */
	@Override
	public String toString(){
		return this.getCategory() + " (reaction in '" + this.getTargetLevel() + "')";
	}
}
