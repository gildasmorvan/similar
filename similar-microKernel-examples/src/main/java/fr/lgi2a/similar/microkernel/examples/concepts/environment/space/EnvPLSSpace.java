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
package fr.lgi2a.similar.microkernel.examples.concepts.environment.space;

import java.util.LinkedHashSet;
import java.util.Set;

import fr.lgi2a.similar.microkernel.examples.concepts.agents.citizen.physical.AgtCitizenPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.level.ConceptsSimulationLevelIdentifiers;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractPublicLocalState;

/**
 * Models the public local state of the environment for the 'space' level.
 * 
 * <h1>Naming convention</h1>
 * In the name of the class:
 * <ul>
 * 	<li>"Env" stands for "Environment"</li>
 * 	<li>"PLS" stands for "Public Local State"</li>
 * </ul>
 * These rules were defined to reduce the size of the name of the class.
 * 
 * <h1>Public local state of the environment in the SIMILAR API suite.</h1>
 * <p>
 * 	In the micro-kernel of SIMILAR, the public local state of the environment is implemented as an 
 * 	instance of either the {@link fr.lgi2a.similar.microkernel.IPublicLocalState} interface, or of the {@link AbstractPublicLocalState} 
 * 	abstract class.
 * 	In this example, we use the abstract class which is easier to implement.
 * </p>
 * 
 * <h1>Usage</h1>
 * <p>
 * 	This public local state models the mothership of the aliens.
 * 	Its public local state contains a sample of each citizen on which an experiment was performed.
 *	It also tracks the total number of data samples that were sent to the mothership.
 * </p>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class EnvPLSSpace extends AbstractPublicLocalState {
	/**
	 * The samples that were collected by the 'aliens' up to now.
	 */
	private Set<AgtCitizenPLSPhysical> samples;
	/**
	 * The total number of samples that were sent to the mothership.
	 */
	private long samplesNumber;

	/**
	 * Builds an instance of the public local state of the environment in the 'space' level.
	 * This instance initially contains no samples.
	 */
	public EnvPLSSpace( ) {
		// The super constructor requires the identifier of the level for which this public
		// local state is defined.
		super( ConceptsSimulationLevelIdentifiers.SPACE_LEVEL );
		this.samples = new LinkedHashSet<AgtCitizenPLSPhysical>( );
		this.samplesNumber = 0;
	}

	/**
	 * Gets the samples that were collected by the 'aliens' up to now.
	 * @return The samples that were collected by the 'aliens' up to now.
	 */
	public Set<AgtCitizenPLSPhysical> getSamples( ) {
		return this.samples;
	}
	
	/**
	 * Adds a sample to the samples that were collected by the 'aliens' up to now.
	 * @param sample The sample to add.
	 */
	public void addSample( AgtCitizenPLSPhysical sample ) {
		this.samples.add( sample );
		this.samplesNumber++;
	}
	
	/**
	 * Gets the total number of samples that were sent to the mothership.
	 * @return The total number of samples that were sent to the mothership.
	 */
	public long getSamplesNumber( ) {
		return this.samplesNumber;
	}
}
