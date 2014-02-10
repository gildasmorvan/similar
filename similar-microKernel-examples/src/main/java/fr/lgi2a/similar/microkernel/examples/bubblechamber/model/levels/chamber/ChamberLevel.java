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
package fr.lgi2a.similar.microkernel.examples.bubblechamber.model.levels.chamber;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.particle.chamber.AgtParticlePLSInChamber;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.environment.chamber.EnvPLSInChamber;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.influences.tochamber.RIUpdateParticlesSpatialStateInChamber;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractLevel;

/**
 * The "Chamber" level, where the particles move and cause 
 * the apparition of bubbles along their path.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class ChamberLevel extends AbstractLevel {
	/**
	 * Builds an uninitialized instance of this level.
	 * <p>
	 * 	The public local state of the environment in the level has 
	 * 	to be set during the initialization phase of the simulation.
	 * </p>
	 * @param initialTime The initial time of the simulation.
	 */
	public ChamberLevel(
			SimulationTimeStamp initialTime
	) {
		super(
			initialTime, 
			BubbleChamberLevelList.CHAMBER
		);
	}

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
		// This reaction manages the reaction to the RIUpdateParticlesSpatialStateInChamber influences last.
		Set<AgtParticlePLSInChamber> particlesUpdateList = new HashSet<AgtParticlePLSInChamber>();
		for( IInfluence influence : regularInfluencesOftransitoryStateDynamics ){
			if( influence.getCategory().equals( RIUpdateParticlesSpatialStateInChamber.CATEGORY ) ){
				RIUpdateParticlesSpatialStateInChamber castedInfluence = 
						(RIUpdateParticlesSpatialStateInChamber) influence;
				particlesUpdateList.addAll( castedInfluence.getParticlesToUpdate() );
			} else {
				throw new UnsupportedOperationException( 
					"The influence '" + influence.getCategory() + "' is currently not supported in this reaction." 
				);
			}
		}
		// Manage the reaction to the particles that were listed by the 
		// RIUpdateParticlesSpatialStateInChamber influences.
		this.reactionToRIUpdateParticlesSpatialStateInChamber( 
				(EnvPLSInChamber) consistentState.getPublicLocalStateOfEnvironment(),
				particlesUpdateList,
				remainingInfluences
		);
	}
	
	/**
	 * The reaction to the sum of all the {@link RIUpdateParticlesSpatialStateInChamber} influences
	 * that were sent to this level.
	 * @param chamberEnvState The public local state of the environment in the chamber level.
	 * @param particlesUpdateList The particles listed in these influences.
	 * @param remainingInfluences 
	 */
	private void reactionToRIUpdateParticlesSpatialStateInChamber( 
			EnvPLSInChamber chamberEnvState,
			Set<AgtParticlePLSInChamber> particlesUpdateList,
			Set<IInfluence> remainingInfluences
	){
		for( AgtParticlePLSInChamber particle : particlesUpdateList ) {
			particle.setLocation(
				particle.getLocation().getX() + particle.getVelocity().getX() + particle.getAcceleration().getX() / 2, 
				particle.getLocation().getY() + particle.getVelocity().getY() + particle.getAcceleration().getY() / 2
			);
			particle.setVelocity(
				particle.getVelocity().getX() + particle.getAcceleration().getX(), 
				particle.getVelocity().getY() + particle.getAcceleration().getY()
			);
			// Check if the particle goes out of the bounds of the chamber.
			// If true, the agent is removed from the simulation.
			if( ! chamberEnvState.getBounds().contains( particle.getLocation() ) ) {
				SystemInfluenceRemoveAgent rmInfluence = new SystemInfluenceRemoveAgent(
						this.getIdentifier(), 
						particle
				);
				remainingInfluences.add( rmInfluence );
			}
		}
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
		// Does nothing here.
	}
}
