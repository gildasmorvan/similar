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
package fr.lgi2a.similar.microkernel.examples.concepts.influences.toSocial;

import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationLevelIdentifiers;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.editorinchief.AgtEditorInChief;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.editorinchief.social.AgtEditorInChiefPLSSocial;
import fr.lgi2a.similar.microkernel.influences.RegularInfluence;

/**
 * Models the influence sent by the 'FBI' to replace an 'Editor in chief' that is not following their 
 * directives.
 * 
 * <h1>Naming convention</h1>
 * In the name of the class:
 * <ul>
 * 	<li>"RI" stands for "Regular Influence"</li>
 * </ul>
 * These rules were defined to reduce the size of the name of the class.
 * 
 * <h1>Regular influences in the SIMILAR API suite.</h1>
 * <p>
 * 	In the micro-kernel of SIMILAR, the regular influences are implemented as an 
 * 	instance of either the {@link IInfluence} interface or the {@link RegularInfluence} 
 * 	abstract class.
 * 	In this example, we use the abstract class which is easier to implement.
 * </p>
 * 
 * <h1>Best practice</h1>
 * <p>
 * 	The category of the influence is a unique identifier for the influence. It is defined to avoid
 * 	using <code>instanceof</code> instructions in the reaction of the level to identify the nature of the
 * 	influence and the operations that have to be performed in reaction to the influence.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class RISocialReplaceEditorInChief extends RegularInfluence {
	/**
	 * The category of the influence, used as a unique identifier in the reaction of the 'social' 
	 * level to determine the nature of the influence.
	 */
	public static final String CATEGORY = "Change editor in chief";
	
	/**
	 * The editor in chief to change.
	 */
	private AgtEditorInChiefPLSSocial editorInChiefToReplace;
	/**
	 * The new editor in chief of the TV broadcast.
	 */
	private AgtEditorInChief newEditorInChief;
	
	/**
	 * Builds a regular influence modeling the will of the 'FBI' to change the editor 
	 * in chief of the TV channel.
	 * @param agtEditorInChiefPLSSocial The editor in chief to change.
	 * @param newEditorInChief The new editor in chief of the TV broadcast.
	 */
	public RISocialReplaceEditorInChief( 
			AgtEditorInChiefPLSSocial agtEditorInChiefPLSSocial,
			AgtEditorInChief newEditorInChief
	) throws IllegalArgumentException {
		super( CATEGORY, ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL );
		this.editorInChiefToReplace = agtEditorInChiefPLSSocial;
		this.newEditorInChief = newEditorInChief;
	}

	/**
	 * Gets the editor in chief to change.
	 * @return The editor in chief to change.
	 */
	public AgtEditorInChiefPLSSocial getEditorInChiefToReplace( ) {
		return this.editorInChiefToReplace;
	}
	
	/**
	 * Gets the new editor in chief of the TV broadcast.
	 * @return The new editor in chief of the TV broadcast.
	 */
	public AgtEditorInChief getNewEditorInChief( ) {
		return this.newEditorInChief;
	}
}
