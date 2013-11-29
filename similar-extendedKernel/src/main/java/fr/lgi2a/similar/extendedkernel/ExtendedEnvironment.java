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
package fr.lgi2a.similar.extendedkernel;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import fr.lgi2a.similar.microkernel.IDynamicStateMap;
import fr.lgi2a.similar.microkernel.IPublicLocalState;
import fr.lgi2a.similar.microkernel.InfluencesMap;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractEnvironment;

/**
 * Models an environment in the extended kernel.
 * On the opposite of the micro-kernel, where environment classes had to be created, this class is self-sufficient.
 * The operational code of the natural action processes is defined in a separate 
 * class. The environment class only stores a reference to this class.
 * <h2>Benefits</h2>
 * This property has a huge benefit: 
 * <ul>
 * 	<li>
 * 		The behavior of the environment can evolve at runtime.
 * 	</li>
 * </ul>
 * <h2>Environment specification</h2>
 * <p>
 * 	The specification of such environments requires one operation:
 * </p>
 * <ul>
 * 	<li>
 * 		The specification of the levels where the environment lies, using the 
 * 		{@link ExtendedEnvironment#includeNewLevel(LevelIdentifier, IPublicLocalState, IEnvNaturalModel)} method.
 * 	</li>
 * </ul>
 * <h2>Introspection and intercession</h2>
 * <p>
 * 	Environment introspection is achieved with the following methods:
 * 	<ul>
 * 		<li>{@link ExtendedEnvironment#getNaturalActionModel(LevelIdentifier)}</li>
 * 	</ul>
 * <p>
 * 	Environment intercession is achieved with the following methods:
 * 	<ul>
 * 		<li>{@link ExtendedEnvironment#changeNaturalActionModel(LevelIdentifier, IEnvNaturalModel)}</li>
 * 	</ul>
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class ExtendedEnvironment extends AbstractEnvironment {
	/**
	 * The natural action models used by the environment.
	 */
	private Map<LevelIdentifier,IEnvNaturalModel> naturalActionModels;
	
	/**
	 * Builds an environment for a simulation containing no levels.
	 * Levels are then added using the {@link ExtendedEnvironment#includeNewLevel(LevelIdentifier, IPublicLocalState, IEnvNaturalModel)} method.
	 */
	public ExtendedEnvironment( ) {
		this.naturalActionModels = new HashMap<LevelIdentifier, IEnvNaturalModel>( );
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void includeNewLevel( LevelIdentifier levelId, IPublicLocalState state ){
		throw new UnsupportedOperationException( "Use the 'includeNewLevel(LevelIdentifier, IPublicLocalState, IEnvNaturalModel)' method instead." );
	}
	
	/**
	 * Introduces the level-related data of the environment for a new level.
	 * <p>
	 * 	Note that the public local state also has to be added to the first consistent dynamic state of the level:
	 * 	the {@link fr.lgi2a.similar.microkernel.IModifiablePublicLocalDynamicState#setPublicLocalStateOfEnvironment(IPublicLocalState)} method has to
	 * 	be called during the initialization of the simulation for the initial consistent state.
	 * </p>
	 * @param levelId The level for which data are added.
	 * @param state The public local state of the environment for that level.
	 * @param naturalActionModel The natural action model of the environment for that level.
	 * @throws IllegalArgumentException If an argument is <code>null</code>, or if the level is already present in the environment.
	 */
	public void includeNewLevel( 
			LevelIdentifier levelId, 
			IPublicLocalState state, 
			IEnvNaturalModel naturalActionModel 
	){
		this.checkNaturalActionModelValidity( levelId, naturalActionModel );
		super.includeNewLevel( levelId, state );
		this.naturalActionModels.put( levelId, naturalActionModel );
	}
	
	/**
	 * Checks that a natural action model is defined for the appropriate level.
	 * @param levelIdentifier The expected level of the natural action model.
	 * @param naturalModel The natural action model.
	 * @throws IllegalArgumentException If the natural action model is not defined for that level.
	 */
	private void checkNaturalActionModelValidity( LevelIdentifier levelIdentifier, IEnvNaturalModel naturalModel ) {
		if( ! levelIdentifier.equals( naturalModel.getLevel() ) ){
			throw new IllegalArgumentException( 
					"The level of a perception model is not appropriate for the environemnt. " +
					"Expecting '" + levelIdentifier + "' but found '" + naturalModel.getLevel() +"'" 
			);
		}
	}
	
	/**
	 * Gets the perception model used by the agent for a specific level.
	 * @param levelId The identifier of the level of the perception model.
	 * @return The perception model of the agent for the level <code>levelId</code>.
	 * @throws NoSuchElementException If no perception model is defined for that level.
	 */
	public IEnvNaturalModel getNaturalActionModel( LevelIdentifier levelId ) {
		IEnvNaturalModel result = this.naturalActionModels.get( levelId );
		if( result == null ){
			throw new NoSuchElementException( "No natural action model is defined for the " +
					"level '" + levelId + "'." );
		}
		return result;
	}
	
	/**
	 * Changes the natural action model of an environment for a new model.
	 * @param levelId The level for which the natural action model is changed.
	 * @param newModel The new natural action model for the <code>levelId</code> level.
	 * @throws IllegalArgumentException If the <code>newModel</code> argument is <code>null</code>, is incompatible 
	 * with the <code>levelId</code> level or if the environment does not reside in that level.
	 */
	public void changeNaturalActionModel( LevelIdentifier levelId, IEnvNaturalModel newModel ) {
		if( ! this.naturalActionModels.containsKey( levelId ) ) {
			throw new IllegalStateException( "The agent '" +  this + "' does not lie in the '" + levelId + "' level." );
		}
		this.checkNaturalActionModelValidity( levelId, newModel );
		this.naturalActionModels.put( levelId, newModel );
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void natural(
			LevelIdentifier level,
			IDynamicStateMap levelsPublicLocalObservableDynamicState,
			InfluencesMap producedInfluences
	) {
		IEnvNaturalModel naturalActionModel = this.naturalActionModels.get( level );
		if( naturalActionModel == null ){
			throw new IllegalStateException( "No natural action model is defined for the level '" + level + "'." );
		}
		naturalActionModel.natural( levelsPublicLocalObservableDynamicState, producedInfluences );
	}
}
