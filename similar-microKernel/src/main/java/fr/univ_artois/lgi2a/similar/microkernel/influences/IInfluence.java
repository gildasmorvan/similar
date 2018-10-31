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
 * Models an influence produced by the agents, the environment or the reaction to modify the dynamic state of the simulation.
 * <p>
 * 	An influence is characterized by a category, telling which kind of influence it models. This category is especially used during
 * 	the reaction phase to identify which type of influence it is, without having to perform a class test (<code>instanceof</code>).
 * </p>
 * 
 * <h1>Correspondence with theory</h1>
 * <p>
 * 	An instance of this class models an influence i&isin;&#x1D540;.
 * </p>
 * 
 * <h1>Usage</h1>
 * <p>
 * 	To facilitate the use of influences, it is advised to define a public and static field defining the category of the influence.
 * 	This way, there won't be any misspelling  during the reaction phase of the simulation, when these categories are 
 * 	used to identify the influences.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public interface IInfluence {
	/**
	 * Gets the category of the influence, <i>i.e.</i> a name identifying the type of the influence.
	 * <p>
	 * 	<b>Examples:</p>
	 * </p>
	 * <ul>
	 * 	<li>Accelerate</li>
	 * 	<li>Eat prey</li>
	 * 	<li>Crouch</li>
	 * </ul>
	 * @return The category of the influence, as defined in the public static field declared in the concrete influence class.
	 */
	String getCategory( );
	
	/**
	 * Gets the identifier of the level whose reaction will process this influence.
	 * @return The identifier of the level whose reaction will process this influence.
	 */
	LevelIdentifier getTargetLevel();
	
	/**
	 * Checks if this influence is a system influence, and has to be managed by the simulation engine.
	 * @return <code>true</code> if the influence is a system influence.
	 */
	boolean isSystem();
	
	/**
	 * Gets the lower bound of the transitory period during which this influence was created.
	 * @return The lower bound of the transitory period during which this influence was created.
	 */
	SimulationTimeStamp getTimeLowerBound( );
	
	/**
	 * Gets the upper bound of the transitory period during which this influence was created.
	 * @return The upper bound of the transitory period during which this influence was created.
	 */
	SimulationTimeStamp getTimeUpperBound( );
}
