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
package fr.lgi2a.similar.microkernel.examples.concepts.agents.editorinchief.social;

import fr.lgi2a.similar.microkernel.IPerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationLevelIdentifiers;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractPerceivedDataOfAgent;

/**
 * Models the data being perceived by an 'Editor in chief' agent when it is perceiving the simulation from the 'Social' level.
 * 
 * <h1>Naming convention</h1>
 * In the name of the class:
 * <ul>
 * 	<li>"Agt" stands for "Agent"</li>
 * 	<li>"PDF" stands for "Perceived Data From the level"</li>
 * </ul>
 * These rules were defined to reduce the size of the name of the class.
 * 
 * <h1>Perceived data in the SIMILAR API suite.</h1>
 * <p>
 * 	In the micro-kernel of SIMILAR, the data perceived by agent are implemented as an 
 * 	instance of either the {@link IPerceivedDataOfAgent} interface or the {@link AbstractPerceivedDataOfAgent} 
 * 	abstract class.
 * 	In this example, we use the abstract class which is easier to implement.
 * </p>
 * 
 * <h1>Best practice</h1>
 * <p>
 * 	It is always possible to include a reference to the public local dynamic state of the perceptible levels
 * 	in the perceived data. Yet, this approach is not advised since it hides the data that are really necessary to build
 * 	the behavior of the agent. Instead, we uphold that this class has to contain as little data as possible. Moreover,
 * 	it can contain interpretations of the data contained in the perceptible levels rather than data directly extracted from 
 * 	that level.
 * 	For instance, if the behavior of the agent changes only depending on the number of other agents in a specific level, the 
 * 	perceived data should only contain the number of agents, and not a reference on the collection of agents lying in that level.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class AgtEditorInChiefPDFSocial extends AbstractPerceivedDataOfAgent {
	/**
	 * The total number of experiments on citizen witnessed by the posts on the Internet.
	 */
	private long totalNumberOfExperiments;
	/**
	 * The number of experiments on citizen in the city of the editor in chief 
	 * that are witnessed by the posts on the Internet.
	 */
	private long numberOfExperimentsInCityOfEditorInChief;

	/**
	 * Builds the data that were perceived by the 'Editor in chief' agent in preparation for
	 * its behavior from the 'Social' level.
	 * @param totalNumberOfExperiments The total number of experiments on citizen witnessed by the posts on the Internet.
	 * @param numberOfExperimentsInCityOfEditorInChief The number of experiments on citizen in the city of the editor in chief 
	 * that are witnessed by the posts on the Internet.
	 */
	public AgtEditorInChiefPDFSocial(
			long totalNumberOfExperiments,
			long numberOfExperimentsInCityOfEditorInChief
	) {
		// The super constructor requires the identifier of the level from which 
		// these data were perceived.
		super( ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL );
		this.totalNumberOfExperiments = totalNumberOfExperiments;
		this.numberOfExperimentsInCityOfEditorInChief = numberOfExperimentsInCityOfEditorInChief;
	}

	/**
	 * Gets the total number of experiments on citizen witnessed by the posts on the Internet.
	 * @return The total number of experiments on citizen witnessed by the posts on the Internet.
	 */
	public long getTotalNumberOfExperiments( ) {
		return this.totalNumberOfExperiments;
	}
	
	/**
	 * Gets the number of experiments on citizen in the city of the editor in chief 
	 * that are witnessed by the posts on the Internet.
	 * @return The number of experiments on citizen in the city of the editor in chief 
	 * that are witnessed by the posts on the Internet.
	 */
	public long getNumberOfExperimentsInCityOfEditorInChief( ) {
		return this.numberOfExperimentsInCityOfEditorInChief;
	}
}
