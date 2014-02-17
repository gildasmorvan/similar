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
package fr.lgi2a.similar.microkernel.libs.abstractimpl;

import fr.lgi2a.similar.microkernel.AgentCategory;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.agents.IAgent;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent4Engine;

/**
 * An abstract implementation of the {@link ILocalStateOfAgent} and {@link ILocalStateOfAgent4Engine} interfaces, 
 * providing a default behavior to their methods.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class AbstractLocalStateOfAgent extends AbstractLocalState implements ILocalStateOfAgent, ILocalStateOfAgent4Engine {
	/**
	 * The agent owning this local state.
	 */
	private final IAgent4Engine owner;
	
	/**
	 * Builds an initialized instance of this class.
	 * @param level The level for which this local state was defined.
	 * @param owner The agent owning this local state.
	 */
	protected AbstractLocalStateOfAgent(
			LevelIdentifier level,
			IAgent4Engine owner
	){
		super( level );
		if( owner == null ){
			throw new IllegalArgumentException( "The 'owner' argument cannot be null." );
		}
		this.owner = owner;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAgent4Engine getOwner() {
		return this.owner;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isOwnedBy( IAgent agent ) {
		return this.owner.equals( agent );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AgentCategory getCategoryOfAgent() {
		return this.owner.getCategory();
	}
}