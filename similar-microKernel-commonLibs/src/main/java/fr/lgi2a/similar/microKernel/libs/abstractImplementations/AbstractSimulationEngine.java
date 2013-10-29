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
package fr.lgi2a.similar.microKernel.libs.abstractImplementations;

import java.util.LinkedHashMap;
import java.util.Set;

import fr.lgi2a.similar.microkernel.I_Probe;
import fr.lgi2a.similar.microkernel.I_SimulationEngine;

/**
 * An abstract implementation of the {@link I_SimulationEngine} interface, providing a default behavior to the probe list-related methods.
 * This class ensures that the iteration order over probes is the insertion order of the probes.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public abstract class AbstractSimulationEngine implements I_SimulationEngine {
	/**
	 * A map containing the probes. This map ensures that the iteration order over probes is the insertion order of the probes.
	 */
	protected LinkedHashMap<String,I_Probe> probes;

	/**
	 * Builds an instance of this abstract simulation engine, containing no probes.
	 */
	public AbstractSimulationEngine(  ) {
		this.probes = new LinkedHashMap<String,I_Probe>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addProbe( String identifier, I_Probe probe ) throws IllegalArgumentException {
		if( identifier == null ){
			throw new IllegalArgumentException( "The 'identifier' argument cannot be null." );
		} else if( probe == null ){
			throw new IllegalArgumentException( "The 'probe' argument cannot be null." );
		} else if( this.probes.containsKey( identifier ) ){
			throw new IllegalArgumentException( "A probe using the '" + identifier + "' identifier already exists." );
		}
		this.probes.put( identifier, probe );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public I_Probe removeProbe( String identifier ) {
		if( identifier == null ){
			throw new IllegalArgumentException( "The 'identifier' argument cannot be null." );
		}
		I_Probe removedProbe = this.probes.remove( identifier );
		return removedProbe;
	}

	/**
	 * Lists the identifier of all the probes that are registered to this engine.
	 * The set returned by this method is ordered, and returns the identifier of the probes in the order they were inserted.
	 * @return The identifier of all the probes that are registered to this engine.
	 */
	@Override
	public Set<String> getProbesIdentifiers() {
		return this.probes.keySet();
	}
}
