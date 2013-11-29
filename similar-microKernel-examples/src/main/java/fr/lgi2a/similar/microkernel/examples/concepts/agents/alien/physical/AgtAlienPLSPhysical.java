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
package fr.lgi2a.similar.microkernel.examples.concepts.agents.alien.physical;

import fr.lgi2a.similar.microkernel.IAgent;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationLevelIdentifiers;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.citizen.physical.AgtCitizenPLSPhysical;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.Cities;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractPublicLocalStateOfAgent;

/**
 * Models the public local state of a 'Alien' agent for the 'physical' level.
 * 
 * <h1>Naming convention</h1>
 * In the name of the class:
 * <ul>
 * 	<li>"Agt" stands for "Agent"</li>
 * 	<li>"PLS" stands for "Public Local State"</li>
 * </ul>
 * These rules were defined to reduce the size of the name of the class.
 * 
 * <h1>Public local state of an agent in the SIMILAR API suite.</h1>
 * <p>
 * 	In the micro-kernel of SIMILAR, the public local state of an agent is implemented as an 
 * 	instance of either the {@link fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent} interface, or of the {@link AbstractPublicLocalStateOfAgent} 
 * 	abstract class.
 * 	In this example, we use the abstract class which is easier to implement.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 *
 */
public class AgtAlienPLSPhysical extends AbstractPublicLocalStateOfAgent {
	/**
	 * The initial completion rate of an experiment.
	 */
	private static final int INITIAL_COMPLETIONRATE = 0;
	/**
	 * The completion rate of the experiment when it has finished.
	 */
	private static final int FINISHED_COMPLETIONRATE = 100;
	
	/**
	 * The location of the alien in the 'physical' level.
	 */
	private Cities location;
	/**
	 * An integer value between 0 and 100 (included) telling the completion rate of the experiments 
	 * currently being performed by the 'alien' on a 'citizen'.
	 */
	private int experimentCompletionRate;
	/**
	 * The 'citizen' on which the experiment is made.
	 */
	private AgtCitizenPLSPhysical experimentSubject;

	/**
	 * Builds an instance of the public local state of a 'Alien' agent in the 'physical' level.
	 * This constructor sets the completion rate of the experiment to 0%.
	 * @param owner The 'Alien' agent to which this public local state belongs.
	 * @param location The location of the alien in the 'physical' level.
	 * @param experimentSubject The 'citizen' on which the experiment is made.
	 */
	public AgtAlienPLSPhysical( IAgent owner, Cities location, AgtCitizenPLSPhysical experimentSubject ) {
		// The super constructor requires the identifier of the level for which this public
		// local state is defined.
		super( ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL, owner );
		// Initialize the location of the agent.
		this.location = location;
		// Initialize the completion rate of the experiment.
		this.experimentCompletionRate = INITIAL_COMPLETIONRATE;
		// Initialize the subject of the experiment.
		this.experimentSubject = experimentSubject;
	}

	/**
	 * Gets the location of the citizen in the 'physical' level.
	 * @return The location of the citizen in the 'physical' level.
	 */
	public Cities getLocation( ) {
		return this.location;
	}

	/**
	 * Sets the location of the alien in the 'physical' level.
	 * @param location The location of the alien in the 'physical' level.
	 */
	public void setLocation( Cities location ){
		this.location = location;
	}

	/**
	 * Increases the completion rate of the experiment being performed by the alien.
	 * @param increment An integer value between 0 and 100 (included) telling the increment of the completion rate of 
	 * the experiments currently being performed by the 'alien' on a 'citizen'.
	 */
	public void increaseExperimentCompletionRate( int increment ) {
		this.experimentCompletionRate += increment;
	}
	
	/**
	 * Tells if the 'alien' has already started its experiments.
	 * @return <code>true</code> if the alien has already started its experiments.
	 */
	public boolean hasStartedExperiments( ) {
		return this.experimentCompletionRate > INITIAL_COMPLETIONRATE;
	}
	
	/**
	 * Tells if the 'alien' has finished its experiments.
	 * @return <code>true</code> if the alien has finished its experiments.
	 */
	public boolean hasFinishedExperiments( ) {
		return this.experimentCompletionRate >= FINISHED_COMPLETIONRATE;
	}
	
	/**
	 * Gets the 'citizen' on which the experiment is made.
	 * @return The 'citizen' on which the experiment is made.
	 */
	public AgtCitizenPLSPhysical getExperimentSubject( ) {
		return this.experimentSubject;
	}
}
