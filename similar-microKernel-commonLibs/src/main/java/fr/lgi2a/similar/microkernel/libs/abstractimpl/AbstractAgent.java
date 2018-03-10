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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import fr.lgi2a.similar.microkernel.AgentCategory;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.agents.IAgent;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;

/**
 * An abstract implementation of the {@link IAgent} and {@link IAgent4Engine} interfaces, 
 * providing a default behavior to the generic methods.
 * <p>
 * 	This class also provides a method to set the level-related information of the 
 * 	agent (public & private local states) as well as its global state.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public abstract class AbstractAgent implements IAgent4Engine {
	
	/**
	 * The category of the agent.
	 */
	private final AgentCategory category;
	/**
	 * The global state of the agent.
	 */
	private IGlobalState globalState;
	/**
	 * The public local state of the agent in the levels where it lies.
	 */
	private Map<LevelIdentifier,ILocalStateOfAgent> publicLocalStates;
	/**
	 * The private local state of the agent in the levels where it lies.
	 */
	private Map<LevelIdentifier,ILocalStateOfAgent> privateLocalStates;
	/**
	 * The last perceived data of the agent.
	 */
	private Map<LevelIdentifier, IPerceivedData> lastPerceivedData;
	
	/**
	 * Creates a bare instance of an agent, using a specific category.
	 * The agent has then to be initialized by calls to the {@link AbstractAgent#initializeGlobalState(IGlobalState)} and
	 * {@link AbstractAgent#includeNewLevel(LevelIdentifier, ILocalStateOfAgent, ILocalStateOfAgent)} methods.
	 * @param category The category of the agent.
	 * <p>
	 * 	This value can be the name of this class, or any other string representation modeling the equivalence 
	 * 	class of the agent.
	 * </p>
	 * <p>
	 * 	<b>Examples:</b>
	 * </p>
	 * <ul>
	 * 	<li>Car</li>
	 * 	<li>Prey</li>
	 * 	<li>Water drop</li>
	 * </ul>
	 * @throws IllegalArgumentException if the argument is <code>null</code>.
	 */
	public AbstractAgent( 
			AgentCategory category 
	) {
		if( category == null ){
			throw new IllegalArgumentException( buildNullArgumentExceptionText( "category" ) );
		}
		this.category = category;
		this.publicLocalStates = new LinkedHashMap<>();
		this.privateLocalStates = new LinkedHashMap<>();
		this.lastPerceivedData = new LinkedHashMap<>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public AgentCategory getCategory() {
		return this.category;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IGlobalState getGlobalState() {
		return this.globalState;
	}

	/**
	 * Sets the value of the initial global state of the agent.
	 * @param initialGlobalState The value of the initial global state of the agent.
	 * This state cannot be <code>null</code>.
	 * @throws IllegalArgumentException If the argument is <code>null</code>.
	 */
	public void initializeGlobalState( 
			IGlobalState initialGlobalState 
	) {
		if( initialGlobalState == null ){
			throw new IllegalArgumentException( buildNullArgumentExceptionText( "initialGlobalState" ) );
		}
		this.globalState = initialGlobalState;
	}

	/**
	 * {@inheritDoc}
	 */
	public Set<LevelIdentifier> getLevels() {
		return this.publicLocalStates.keySet();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILocalStateOfAgent getPublicLocalState(
			LevelIdentifier levelId
	) {
		if( levelId == null ){
			throw new IllegalArgumentException( buildNullArgumentExceptionText( "levelId" ) );
		}
		ILocalStateOfAgent result = this.publicLocalStates.get( levelId );
		if( result == null ){
			throw new NoSuchElementException( "The agent does not define a public local " +
					"state for the level '" + levelId + "'." );
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<LevelIdentifier, ILocalStateOfAgent> getPublicLocalStates() {
		return this.publicLocalStates;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ILocalStateOfAgent getPrivateLocalState(
			LevelIdentifier levelId
	) {
		if( levelId == null ){
			throw new IllegalArgumentException( buildNullArgumentExceptionText( "levelId" ) );
		}
		ILocalStateOfAgent result = this.privateLocalStates.get( levelId );
		if( result == null ){
			throw new NoSuchElementException( "The agent does not define a private local " +
					"state for the level '" + levelId + "'." );
		}
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void includeNewLevel( 
			LevelIdentifier levelIdentifier, 
			ILocalStateOfAgent publicLocalState,
			ILocalStateOfAgent privateLocalState 
	){
		if( levelIdentifier == null ){
			throw new IllegalArgumentException( buildNullArgumentExceptionText( "levelIdentifier" ) );
		} else if( publicLocalState == null ){
			throw new IllegalArgumentException( buildNullArgumentExceptionText( "publicLocalState" ) );
		} else if( privateLocalState == null ){
			throw new IllegalArgumentException( buildNullArgumentExceptionText( "privateLocalState" ) );
		}
		if( ! this.getLevels().contains( levelIdentifier ) ){
			this.publicLocalStates.put( levelIdentifier, publicLocalState );
			this.privateLocalStates.put( levelIdentifier, privateLocalState );
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void excludeFromLevel(
			LevelIdentifier levelIdentifier 
	) {
		if( levelIdentifier == null ){
			throw new IllegalArgumentException( buildNullArgumentExceptionText( "levelIdentifier" ) );
		}
		if( this.getLevels().contains( levelIdentifier ) ){
			this.publicLocalStates.remove( levelIdentifier );
			this.privateLocalStates.remove( levelIdentifier );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<LevelIdentifier, IPerceivedData> getPerceivedData() {
		return this.lastPerceivedData;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPerceivedData(
			IPerceivedData perceivedData
	) {
		if( perceivedData == null ){
			throw new IllegalArgumentException( buildNullArgumentExceptionText( "perceivedData" ) );
		} else if( perceivedData.getLevel() == null ){
			throw new IllegalArgumentException( buildNullArgumentExceptionText( "levelIden" ) );
		} 
		this.lastPerceivedData.put( perceivedData.getLevel(), perceivedData );
	}
	
	/**
	 * Builds the text of an exception stating that an argument cannot be <code>null</code>.
	 * @param argName The name of the argument.
	 * @return The text of an exception stating that an argument cannot be <code>null</code>.
	 */
	private static String buildNullArgumentExceptionText( String argName ) {
		return "The '" + argName + "' argument cannot be null.";
	}
}
