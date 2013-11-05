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
package fr.lgi2a.similar.microkernel.examples.concepts;

import java.util.Random;

/**
 * This class defines the random number generator used in the simulation.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class ConceptsSimulationRandom {
	/**
	 * The random number generator used in this class.
	 */
	private static Random randomInstance;
	
	/**
	 * Static block providing a default random number generator.
	 */
	static {
		initializeRandomizer( 10 );
	}
	
	protected ConceptsSimulationRandom( ){ }
	
	/**
	 * Initializes the random number generator using a specific seed.
	 * @param seed The seed to use.
	 */
	public static void initializeRandomizer( long seed ) {
		randomInstance = new Random( seed );
	}
	
	/**
	 * Gets a random number between 0 (included) and 1 (excluded).
	 * @return A random number between 0 (included) and 1 (excluded).
	 */
	public static double random( ) {
		return randomInstance.nextDouble();
	}
	
	/**
	 * Gets a random integer value between 0 (included) and the specified higher bound (excluded).
	 * @param higherBound The higher bound used for random integer generation.
	 * @return a random integer value between 0 (included) and <code>higherBound</code> (excluded).
	 */
	public static int randomInt( int higherBound ) {
		return randomInstance.nextInt( higherBound );
	}
}
