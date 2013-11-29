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
package fr.lgi2a.similar.microkernel.libs.tools.learning.model;

import java.util.Set;

import fr.lgi2a.similar.microkernel.IDynamicStateMap;
import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.InfluencesMap;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractEnvironment;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.AbstractLearningEngineOperationMoment;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.SimulationExecutionTrace;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.operations.LearningEngineOperationNatural;

/**
 * Models the environment in the 'learning' simulation.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public abstract class AbstractLearningEnvironment extends AbstractEnvironment {
	/**
	 * The trace where the execution of the simulation is tracked.
	 */
	private SimulationExecutionTrace trace;
	
	/**
	 * Creates the environment of the 'learning' simulation.
	 * @param trace The trace where the execution of the simulation is tracked.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public AbstractLearningEnvironment(
			SimulationExecutionTrace trace
	) {
		if( trace == null ){
			throw new IllegalArgumentException( "The 'trace' argument cannot be null" );
		}
		this.trace = trace;
	}

	/**
	 * {@inheritDoc}
	 * @see fr.lgi2a.similar.microkernel.IEnvironment#natural(fr.lgi2a.similar.microkernel.LevelIdentifier, fr.lgi2a.similar.microkernel.IDynamicStateMap, fr.lgi2a.similar.microkernel.InfluencesMap)
	 */
	@Override
	public void natural(
			LevelIdentifier level,
			IDynamicStateMap levelsPublicLocalObservableDynamicState,
			InfluencesMap producedInfluences
	) {
		LearningEngineOperationNatural operation = new LearningEngineOperationNatural( level );
		for( LevelIdentifier perceptibleLevel : levelsPublicLocalObservableDynamicState.keySet() ){
			operation.addObservableDynamicState( levelsPublicLocalObservableDynamicState.get( perceptibleLevel ) );
		}
		Set<IInfluence> influences = this.produceInfluencesOfNatural( level, levelsPublicLocalObservableDynamicState );
		for( IInfluence influence : influences ){
			operation.addInfluence( influence );
			producedInfluences.add( influence );
		}
		this.trace.addEngineOperation(
				new AbstractLearningEngineOperationMoment.LearningEngineOperationMomentAfter( levelsPublicLocalObservableDynamicState.get( level ).getTime() ), 
				operation
		);
	}
	
	/**
	 * An abstract method where users can define which influences are produced by the environment during its natural phase.
	 * The following influences can be produced:
	 * <ul>
	 * 	<li>{@link fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent}, with an instance of the {@link AbstractLearningAgent} class.</li>
	 * 	<li>{@link fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent}, with a public local state of an agent located in the perceived data.</li>
	 * 	<li> 
	 * 		{@link fr.lgi2a.similar.microkernel.libs.tools.learning.model.influence.LearningInfluenceAgentPublicLocalStateUpdate}, with a public local state of an agent located in the perceived data.
	 * 		During the reaction, this influence triggers a modification of the public local
	 * 		state, increasing its revision number. This operation models any modification 
	 * 		that can be made in the public local state of an agent.
	 * 	</li>
	 * <li> 
	 * 		{@link fr.lgi2a.similar.microkernel.libs.tools.learning.model.influence.LearningInfluenceEnvironmentPublicLocalStateUpdate}, with a public local state of the environment 
	 * 		located in the perceived data.
	 * 		During the reaction, this influence triggers a modification of the public local
	 * 		state, increasing its revision number. This operation models any modification 
	 * 		that can be made in the public local state of the environment.
	 * 	</li>
	 * </ul>
	 * <p>
	 * 	Warning: do not change the values contained in the arguments !
	 * 	Otherwise, the produced trace will be invalid.
	 * </p>
	 * @param level The level from which perception is made.
	 * @param levelsPublicLocalObservableDynamicState The dynamic state of the levels that are perceptible 
	 * from the <code>level</code> level.
	 * @return The set of influences that are produced by the natural action of the environment.
	 */
	protected abstract Set<IInfluence> produceInfluencesOfNatural(
			LevelIdentifier level, 
			IDynamicStateMap levelsPublicLocalObservableDynamicState
	);
}
