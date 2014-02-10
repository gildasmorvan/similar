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
package fr.lgi2a.similar.microkernel.examples.bubblechamber.model.levels.external;

import java.util.Collection;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.ArithmeticParameters;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.cannon.external.AgtCannonPLSInExternal;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.influences.toexternal.RICoolDown;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.influences.toexternal.RIFireParticle;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractLevel;

/**
 * The "External" level, where the "particles cannon" exists and fires particles 
 * inside the "bubble chamber" level.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 *
 */
public class ExternalLevel extends AbstractLevel {
	/**
	 * Builds an uninitialized instance of this level.
	 * <p>
	 * 	The public local state of the environment in the level has 
	 * 	to be set during the initialization phase of the simulation.
	 * </p>
	 * @param initialTime The initial time of the simulation.
	 * @param powerToTemperatureConversionRatio The ratio used to convert a power into a temperature.
	 */
	public ExternalLevel(
			SimulationTimeStamp initialTime,
			double powerToTemperatureConversionRatio
	){
		super(
			initialTime, 
			BubbleChamberLevelList.EXTERNAL
		);
		this.addInfluenceableLevel( BubbleChamberLevelList.CHAMBER );
		this.powerToTemperatureConversionRatio = powerToTemperatureConversionRatio;
	}
	
	//
	//
	// Parameters of the level
	//
	//
	
	/**
	 * The ratio used to convert a power into a temperature.
	 */
	private double powerToTemperatureConversionRatio;
	
	//
	//
	// Behavior of the level
	//
	//

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SimulationTimeStamp getNextTime(
			SimulationTimeStamp currentTime
	) {
		return new SimulationTimeStamp( currentTime.getIdentifier() + 1 );
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
		// The cool down reaction are performed last.
		RICoolDown coolDownInfluence = null;
		for( IInfluence influence : regularInfluencesOftransitoryStateDynamics ){
			if( influence.getCategory().equals( RICoolDown.CATEGORY ) ){
				coolDownInfluence = (RICoolDown) influence;
			} else if( influence.getCategory().equals( RIFireParticle.CATEGORY ) ){
				RIFireParticle castedInfluence = (RIFireParticle) influence;
				this.reactionTo( castedInfluence, remainingInfluences );
			} else {
				throw new UnsupportedOperationException( 
					"The influence '" + influence.getCategory() + "' is currently not supported in this reaction." 
				);
			}
		}
		// Finally perform the cool down influence, if applicable.
		if( coolDownInfluence != null ){
			this.reactionTo( coolDownInfluence );
		}
	}
	
	/**
	 * Reacts to a {@link RICoolDown} influence.
	 * @param influence The influence to manage.
	 */
	private void reactionTo( 
			RICoolDown influence
	) {
		double ambientTemperature = influence.ambientTemperature;
		for( AgtCannonPLSInExternal cannon : influence.cannon ) {
			// Adjust the temperature of the cannon to the ambient external temperature.
			double cannonTemp = cannon.getTemperature( );
			double difference = ambientTemperature - cannonTemp;
//			double adjustment = Math.pow( Math.abs( difference ) / 10, 1.5 );
//			cannon.setTemperature( cannonTemp + Math.signum( difference ) * adjustment );
			double adjustment = Math.signum( difference ) * 1.0;
			if( Math.abs( difference )  > ArithmeticParameters.DOUBLE_PRECISION ){
				if( Math.abs( difference ) < 1.0 ){
					adjustment *= Math.abs( difference );
				}
				cannon.setTemperature( cannonTemp + adjustment );
			}
		}
	}
	
	/**
	 * Reacts to a {@link RIFireParticle} influence.
	 * @param influence The influence to manage.
	 */
	private void reactionTo( 
			RIFireParticle influence,
			Set<IInfluence> remainingInfluences
	) {
		// Add the fired particle to the simulation
		remainingInfluences.add( new SystemInfluenceAddAgent( 
				BubbleChamberLevelList.EXTERNAL, 
				influence.particle
		) );
		// Then heat up the cannon depending on the expended power
		AgtCannonPLSInExternal state = influence.cannon;
		state.setTemperature(
			state.getTemperature() + state.getPower() * this.powerToTemperatureConversionRatio
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
		// Does nothing.
	}
}
