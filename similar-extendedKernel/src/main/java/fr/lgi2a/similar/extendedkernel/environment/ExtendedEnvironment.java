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
package fr.lgi2a.similar.extendedkernel.environment;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.libs.abstractimpl.AbstractEnvironment;

/**
 * Models an environment in the extended kernel.
 * <p>
 * 	On the opposite of the micro-kernel, where environment classes had to be created, this class is self-sufficient.
 * 	The operational code of the natural action processes is defined in a separate 
 * 	class. The environment class only stores a reference to this class.
 * </p>
 * <h2>Benefits</h2>
 * This property has a huge benefit: 
 * <ul>
 * 	<li>
 * 		The behavior of the environment can evolve at runtime.
 * 	</li>
 * </ul>
 * <h2>Environment specification</h2>
 * <p>
 * 	The specification of such environments requires two operations:
 * <ul>
 * 	<li>
 * 		The definition of the initial local states of the environment, using the 
 * 		{@link AbstractEnvironment#includeNewLevel(LevelIdentifier, ILocalStateOfEnvironment, ILocalStateOfEnvironment)} method.
 * 	</li>
 * 	<li>
 * 		The specification of the behavior of the environment in each level, using the 
 * 		{@link ExtendedEnvironment#specifyBehaviorForLevel(LevelIdentifier, IEnvNaturalModel)} method.
 * 	</li>
 * </ul>
 * <h2>Introspection and intercession</h2>
 * <p>
 * 	Environment introspection is achieved with the following methods:
 * 	<ul>
 * 		<li>{@link ExtendedEnvironment#getNaturalModelForLevel(LevelIdentifier)}</li>
 * 	</ul>
 * <p>
 * 	Environment intercession is achieved with the following methods:
 * 	<ul>
 * 		<li>{@link ExtendedEnvironment#specifyBehaviorForLevel(LevelIdentifier, IEnvNaturalModel)}</li>
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
	 * 
	 * The environment has then to be initialized using the following methods:
	 * <ul>
	 * 	<li>
	 * 		The definition of the initial local states of the environment, using the 
	 * 		{@link AbstractEnvironment#includeNewLevel(LevelIdentifier, ILocalStateOfEnvironment, ILocalStateOfEnvironment)} method.
	 * 	</li>
	 * 	<li>
	 * 		The specification of the behavior of the environment in each level, using the 
	 * 		{@link ExtendedEnvironment#specifyBehaviorForLevel(LevelIdentifier, IEnvNaturalModel)} method.
	 * 	</li>
	 * </ul>
	 */
	public ExtendedEnvironment( ) {
		this.naturalActionModels = new HashMap<LevelIdentifier, IEnvNaturalModel>( );
	}

	//
	//
	//
	// Extended environment related methods
	//
	//
	//
	
	/**
	 * Gets the model of the natural action of the environment from that level.
	 * @param levelId The identifier of the level.
	 * @return The model of the natural action of the environment from that level.
	 * @throws NoSuchElementException If no natural model is defined for that 
	 * environment for the level identified by <code>levelId</code>.
	 */
	public IEnvNaturalModel getNaturalModelForLevel(
		LevelIdentifier levelId
	){
		IEnvNaturalModel model = this.naturalActionModels.get( levelId );
		if( model == null ){
			throw new NoSuchElementException(
				"The natural model of the environment is not specified for " +
				"the level '" + levelId + "'."
			);
		} else {
			return model;
		}
	}
	
	/**
	 * Specifies the behavior of the environment from the specified level.
	 * @param levelId The identifier of the level for which a behavior is defined.
	 * @param naturalMdl The model of the natural action of the environment from that level.
	 * @throws IllegalArgumentException If an argument is <code>null</code> or has an invalid value.
	 */
	public void specifyBehaviorForLevel(
		LevelIdentifier levelId,
		IEnvNaturalModel naturalMdl
	){
		if( levelId == null || naturalMdl == null ){
			throw new IllegalArgumentException(
				"The arguments cannot be null."
			);
		} else if( ! naturalMdl.getLevel().equals( levelId ) ){
			throw new IllegalArgumentException(
				"The level of the natural model '" + naturalMdl.getLevel() + "' has " +
				"to match the value of the argument 'levelId'."
			);
		}
		this.naturalActionModels.put( levelId, naturalMdl );
	}
	
	//
	//
	//
	// Micro-kernel agent related methods
	//
	//
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void natural(
		LevelIdentifier level,
		SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound,
		Map<LevelIdentifier, ILocalStateOfEnvironment> publicLocalStates,
		ILocalStateOfEnvironment privateLocalState,
		IPublicDynamicStateMap dynamicStates,
		InfluencesMap producedInfluences
	) {
		this.getNaturalModelForLevel( level ).natural(
			timeLowerBound, 
			timeUpperBound, 
			publicLocalStates, 
			privateLocalState, 
			dynamicStates, 
			producedInfluences
		);
	}
}
