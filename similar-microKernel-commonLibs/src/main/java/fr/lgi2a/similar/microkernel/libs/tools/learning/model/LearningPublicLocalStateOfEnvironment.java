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
package fr.lgi2a.similar.microkernel.libs.tools.learning.model;

import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractPublicLocalState;

/**
 * Models the public local state of the environment in a specific level of the "learning" simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class LearningPublicLocalStateOfEnvironment extends AbstractPublicLocalState {
	/**
	 * Stores the number of modifications that were applied to this state since the beginning of the simulation.
	 */
	private long revisionNumber;

	/**
	 * Builds an initialized public local state for a specific level the environment in the "learning" simulation.
	 * @param levelIdentifier The identifier of the level where this public local state is defined.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public LearningPublicLocalStateOfEnvironment(
			LevelIdentifier levelIdentifier
	) {
		super(levelIdentifier);
		this.revisionNumber = 0;
	}

	/**
	 * Gets the number of modifications that were applied to this state since the beginning of the simulation.
	 * @return The number of modifications that were applied to this state since the beginning of the simulation.
	 */
	public long getRevisionNumber(){
		return this.revisionNumber;
	}
	
	/**
	 * Revise the content of this state once.
	 * @return This instance, including a revision of its content.
	 */
	public LearningPublicLocalStateOfEnvironment revise(){
		this.revisionNumber++;
		return this;
	}
	
	/**
	 * Creates a copy of this public local state of the environment.
	 * @return A copy of this public local state.
	 */
	public LearningPublicLocalStateOfEnvironment createCopy(){
		LearningPublicLocalStateOfEnvironment copy = new LearningPublicLocalStateOfEnvironment( this.getLevel() );
		copy.revisionNumber = this.revisionNumber;
		return copy;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString( ) {
		return "Env - PublicLocalState ( " + this.getLevel() + " ) - #" + this.getRevisionNumber();
	}
}
