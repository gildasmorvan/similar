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

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.ITimeModel;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractLevel;

/**
 * Models a level in the extended kernel.
 * On the opposite of the micro-kernel, where level classes had to be created, this class is self-sufficient.
 * The operational code of the reaction processes and of the time model is defined in a separate 
 * class. The level class only stores a reference to this class.
 * <h2>Benefits</h2>
 * This property has a huge benefit: 
 * <ul>
 * 	<li>
 * 		The behavior of the level can evolve at runtime.
 * 	</li>
 * </ul>
 * <h2>Level specification</h2>
 * <p>
 * 	The specification of such levels requires two operations:
 * </p>
 * <ul>
 * 	<li>
 * 		The initialization of the dynamic state using the following methods:
 * 		<ul>
 * 			<li>{@link fr.lgi2a.similar.microkernel.IModifiablePublicLocalDynamicState#setPublicLocalStateOfEnvironment(fr.lgi2a.similar.microkernel.IPublicLocalState)}</li>
 * 			<li>{@link fr.lgi2a.similar.microkernel.IModifiablePublicLocalDynamicState#setStateDynamicsAsCopyOf(java.util.Collection)}</li>
 * 			<li>{@link fr.lgi2a.similar.microkernel.IModifiablePublicLocalDynamicState#addPublicLocalStateOfAgent(fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent)}</li>
 * 		</ul>
 * 	</li>
 * 	<li>
 * 		The specification of the reaction model of the level, using the 
 * 		{@link ExtendedLevel#setReactionModel(ILevelReactionModel)} method.
 * 	</li>
 * </ul>
 * <h2>Introspection and intercession</h2>
 * <p>
 * 	Level introspection is achieved with the following methods:
 * 	<ul>
 * 		<li>{@link ExtendedLevel#getTimeModel()}</li>
 * 		<li>{@link ExtendedLevel#getReactionModel()}</li>
 * 	</ul>
 * <p>
 * 	Level intercession is achieved with the following methods:
 * 	<ul>
 * 		<li>{@link ExtendedLevel#setReactionModel(ILevelReactionModel)}</li>
 * 	</ul>
 * </p>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class ExtendedLevel extends AbstractLevel {
	/**
	 * The time model used by this level.
	 */
	private ITimeModel timeModel;
	/**
	 * The reaction model used by the level.
	 */
	private ILevelReactionModel reactionModel;
	
	/**
	 * Builds an initialized instance of level having a specific initial time, a specific identifier, a perception and influence 
	 * relation graph containing only an edge between this level and itself, a specific time model, an empty initial consistent 
	 * dynamic state and no reaction model.
	 * <p>
	 * 	The dynamic state has to be initialized using the following methods:
	 * </p>
	 * <ul>
	 * <li>{@link fr.lgi2a.similar.microkernel.IModifiablePublicLocalDynamicState#setPublicLocalStateOfEnvironment(fr.lgi2a.similar.microkernel.IPublicLocalState)}</li>
	 * <li>{@link fr.lgi2a.similar.microkernel.IModifiablePublicLocalDynamicState#setStateDynamicsAsCopyOf(java.util.Collection)}</li>
	 * <li>{@link fr.lgi2a.similar.microkernel.IModifiablePublicLocalDynamicState#addPublicLocalStateOfAgent(fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent)}</li>
	 * </ul>
	 * <p>
	 * 	The reaction model is defined using the following method:
	 * </p>
	 * <ul>
	 * 	<li>{@link ExtendedLevel#setReactionModel(ILevelReactionModel)}</li>
	 * </ul>
	 * @param initialTime The initial time stamp of the level.
	 * @param identifier The identifier of the level.
	 * @param timeModel The time model used by this level.
	 * @throws IllegalArgumentException if an argument is <code>null</code>.
	 */
	public ExtendedLevel(
			SimulationTimeStamp initialTime,
			LevelIdentifier identifier,
			ITimeModel timeModel
	) {
		super(initialTime, identifier);
		if( timeModel == null ){
			throw new IllegalArgumentException( "The 'timeModel' of the level '" + identifier + "' cannot be null." );
		}
		this.timeModel = timeModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimulationTimeStamp getNextTime( SimulationTimeStamp currentTime ) {
		return this.timeModel.getNextTime( currentTime );
	}

	/**
	 * Gets the time model used by this level.
	 * @return The time model used by this level.
	 */
	public ITimeModel getTimeModel( ) {
		return this.timeModel;
	}

	/**
	 * Gets the reaction model used by the level.
	 * @return The reaction model used by the level.
	 * @throws NoSuchElementException If no reaction model is defined for this level.
	 */
	public ILevelReactionModel getReactionModel( ) {
		if( this.reactionModel == null ){
			throw new NoSuchElementException( "No reaction model is defined for the level '" + this.getIdentifier() + "'." );
		}
		return this.reactionModel;
	}

	/**
	 * Sets the reaction model used by the level.
	 * @param newReactionModel The new reaction model used by the level.
	 */
	public void setReactionModel( ILevelReactionModel newReactionModel ){
		if( newReactionModel == null ){
			throw new IllegalArgumentException( "The reaction model of the level '" + this.getIdentifier() + "' cannot be null." );
		}
		this.reactionModel = newReactionModel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeRegularReaction(
			SimulationTimeStamp previousConsistentStateTime,
			SimulationTimeStamp newConsistentStateTime,
			ConsistentPublicLocalDynamicState consistentState,
			Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
			Set<IInfluence> remainingInfluences
	) {
		this.getReactionModel().makeRegularReaction(
				previousConsistentStateTime, 
				newConsistentStateTime, 
				consistentState, 
				regularInfluencesOftransitoryStateDynamics, 
				remainingInfluences
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void makeSystemReaction(
			SimulationTimeStamp previousConsistentStateTime,
			SimulationTimeStamp newConsistentStateTime,
			ConsistentPublicLocalDynamicState consistentState,
			Collection<IInfluence> systemInfluencesToManage,
			boolean happensBeforeRegularReaction,
			Collection<IInfluence> newInfluencesToProcess
	) {
		this.getReactionModel().makeSystemReaction(
				previousConsistentStateTime, 
				newConsistentStateTime, 
				consistentState, 
				systemInfluencesToManage, 
				happensBeforeRegularReaction, 
				newInfluencesToProcess
		);
	}
}
