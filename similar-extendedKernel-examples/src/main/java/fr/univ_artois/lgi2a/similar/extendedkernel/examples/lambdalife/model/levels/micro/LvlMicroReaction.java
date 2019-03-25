/**
 * Copyright or � or Copr. LGI2A
 * 
 * LGI2A - Laboratoire de Genie Informatique et d'Automatique de l'Artois - EA 3926 
 * Faculte des Sciences Appliquees
 * Technoparc Futura
 * 62400 - BETHUNE Cedex
 * http://www.lgi2a.univ-artois.fr/
 * 
 * Email: gildas.morvan@univ-artois.fr
 * 		  hassane.abouaissa@univ-artois.fr
 * 
 * Contributors:
 * 	Hassane ABOUAISSA (designer)
 * 	Gildas MORVAN (designer, creator of the IRM4MLS formalism)
 * 	Yoann KUBERA (designer, architect and developer of SIMILAR)
 * 
 * This software is a computer program whose purpose is run road traffic
 * simulations using a dynamic hybrid approach.
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
package fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife.model.levels.micro;

import java.util.Collection;
import java.util.Set;

import fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife.model.influences.tomicro.NextStateInfluence;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife.tools.RandomValueFactory;
import fr.univ_artois.lgi2a.similar.extendedkernel.levels.ILevelReactionModel;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar.microkernel.influences.IInfluence;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;

/**
 * Models the user-side of the reaction of the "Micro" level to the influence it 
 * received during its most recent transitory period.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 */
public class LvlMicroReaction implements ILevelReactionModel {
	/**
	 * A parameter used to compute p using lambda.
	 */
	protected static final double A = 0.2734375;
	/**
	 * A parameter used to compute p using lambda.
	 */
	private static final double B = 0.3359;
	/**
	 * The p parameter of the lambda game of life.
	 */
	protected double p;
	
	/**
	 * Builds the reaction of the "Micro" level, based on a lambda parameter.
	 * @param lambda The lambda parameter.
	 */
	public LvlMicroReaction( 
		double lambda
	) {
		if(lambda > LvlMicroReaction.A) {
			this.p = (lambda - A)/B;
		}
		else{
			this.p = 0;
		}
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public void makeRegularReaction(
		SimulationTimeStamp transitoryTimeMin,
		SimulationTimeStamp transitoryTimeMax,
		ConsistentPublicLocalDynamicState consistentState,
		Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
		InfluencesMap remainingInfluences
	) {
		for( IInfluence influence : regularInfluencesOftransitoryStateDynamics ){
			if( influence.getCategory().equals( NextStateInfluence.CATEGORY ) ){
				NextStateInfluence castedNextStateInfluence = (NextStateInfluence) influence;
				if( 
					castedNextStateInfluence.getTarget().isAlive() && !castedNextStateInfluence.getNextState()
				) {
					// Case where the cell should die.
					// In the lambda game of life, there is a lambda probability that the cell
					// stays alive anyway.
					double randomDouble = RandomValueFactory.getStrategy().randomDouble();
	            	castedNextStateInfluence.getTarget().setAlive( randomDouble < this.p );
				} else {
            		castedNextStateInfluence.getTarget().setAlive( castedNextStateInfluence.getNextState() );
				}
			}
		}
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
	public void makeSystemReaction(
		SimulationTimeStamp transitoryTimeMin,
		SimulationTimeStamp transitoryTimeMax,
		ConsistentPublicLocalDynamicState consistentState,
		Collection<IInfluence> systemInfluencesToManage,
		boolean happensBeforeRegularReaction,
		InfluencesMap newInfluencesToProcess
	) {	}
}
