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

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the static parameters and the initial values of the different elements of this model.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class ConceptsSimulationParameters {
	/**
	 * Defines the completion rate of the experiment performed by an alien, that is achieved when a reaction is computed i nthe 'physical' level.
	 */
	public static final double EXPERIMENT_COMPLETION_RATE_PER_REACTION = 25;
	/**
	 * This default parameter tells how much cities an alien is able to scrutinize each time it perceives from the 'space' level.
	 * <p>
	 * 	If no citizen could be found after looking at <code>CITIES_PERCEPTIBLE_BY_ALIEN_PER_PERCEPTION</code> cities, then the alien failed to find
	 * 	a citizen to perform experiments that time.
	 * </p>
	 */
	public static final int CITIES_PERCEPTIBLE_BY_ALIEN_PER_PERCEPTION = 2;
	/**
	 * Defines the default apparition rate of strange physical manifestations on the body of citizen when an alien performs 
	 * an experiment on them.
	 * <p>
	 * 	This value has to be between 0 (included) and 1 (excluded).
	 * </p>
	 */
	public static final double STRANGE_PHYSICAL_MANIFESTATIONS_APPARITION_RATE = 0.50;
	
	/**
	 * The map defining how the times of the level 'space' moves with a non-uniform pattern. This pattern models 
	 * that the actions of aliens cannot be foretold.
	 * This map uses as keys the long values contained in the interval [0,N] where N is the number of keys.
	 * The values model the increment applied between a current time and the next time.
	 * The identifier of the current time is converted into a value contained in the range [0,N] using the modulo operator.
	 */
	public static final Map<Long,Long> TIME_EVOLUTION_DESCRIPTOR_OF_SPACE_LEVEL = createTimeEvolutionDescriptorOfSpaceLevel( );
	
	/**
	 * Creates the value of the {@link ConceptsSimulationParameters#TIME_EVOLUTION_DESCRIPTOR_OF_SPACE_LEVEL} field of this class.
	 * @return The value of the {@link ConceptsSimulationParameters#TIME_EVOLUTION_DESCRIPTOR_OF_SPACE_LEVEL} field of this class.
	 */
	private static Map<Long,Long> createTimeEvolutionDescriptorOfSpaceLevel( ) {
		Map<Long,Long> result = new HashMap<Long,Long>( );
		result.put( 0l, 2l );
		result.put( 1l, 3l );
		result.put( 2l, 1l );
		result.put( 3l, 4l );
		result.put( 4l, 3l );
		return result;
	}
	
	/**
	 * The number of strange physical manifestation over which a citizen can consider that an alien experiment 
	 * was performed on him/her. This value is being broadcasted on television and corresponds to the value 
	 * advised by the FBI.
	 */
	public static final int THRESHOLD_FOR_STRANGE_PHYSICAL_MANIFESTATION_ADVISED_BY_FBI = 4;
	
	/**
	 * The number of abduction being reported in the city of the editor in chief before it becomes paranoid and 
	 * broadcasts values that are not advised by the FBI.
	 */
	public static final long EDITOR_IN_CHIEF_PARANOIA_THRESHOLD = 10;
	
	/**
	 * The threshold of citizens posts number before a citizen is sent to lobotomy by the FBI.
	 */
	public static final int CITIZEN_POSTS_THRESHOLD_BEFORE_LOBOTOMY = 15;
	
	/**
	 * Models a range of citizen initially lying in a city. The bounds of the interval are included.
	 * This interval is used during the generation of the simulation.
	 */
	public static final int[] DEFAULT_RANGE_OF_CITIZENS_PER_CITY = new int[]{ 0, 6 };
	
	/**
	 * The default number of aliens in the simulation.
	 */
	public static final int DEFAULT_NUMBER_OF_ALIENS = 10;
	
	/**
	 * Models the default efficiency of the alien in its experiments. This value has to be 
	 * between 0 (completely inefficient) to 1 (fully efficient). It will be used as a multiplication factor when 
	 * the reaction will determine how much the experiment of the alien advanced.
	 */
	public static final double DEFAULT_ALIEN_EFFICIENCY_IN_EXPERIMENTS = 0.5;
	
	/**
	 * Models the default efficiency of the FBI to capture alien. This value has to be 
	 * between 0 (completely inefficient) to 1 (fully efficient). It determines the success chances of
	 * a capture influence sent by the FBI.
	 */
	public static final double DEFAULT_FBI_EFFICIENCY_IN_CAPTURE = 0.05;
}
