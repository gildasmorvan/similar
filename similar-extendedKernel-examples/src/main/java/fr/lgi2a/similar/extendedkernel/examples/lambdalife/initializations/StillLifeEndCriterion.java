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
package fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife.initializations;

import fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife.model.environment.micro.EnvPLSInMicroLevel;
import fr.univ_artois.lgi2a.similar.extendedkernel.examples.lambdalife.model.levels.LambdaLifeLevelList;
import fr.univ_artois.lgi2a.similar.extendedkernel.simulationmodel.IEndCriterionModel;
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;

/**
 * This simulation end criterion stops the simulation when the grid stays still fore more than
 * a specified number of times.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 */
public class StillLifeEndCriterion implements IEndCriterionModel {
	/**
	 * The number of consecutive still states up until now.
	 */
	private int consecutiveStill;
	/**
	 * The last state of the grid.
	 */
	private boolean[][] lastState;
	/**
	 * The last time the criterion was computed.
	 */
	private SimulationTimeStamp lastTime;
	/**
	 * The number of consecutive still lives to detect before stopping the simulation.
	 */
	private int threshold;
	
	/**
	 * Builds a new freshly initialized end criterion.
	 * @param threshold The number of consecutive still lives to detect before stopping the simulation.
	 */
	public StillLifeEndCriterion( int threshold ) {
		this.consecutiveStill = 0;
		this.threshold = threshold;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFinalTimeOrAfter(
			SimulationTimeStamp currentTime,
			ISimulationEngine engine
	) {
		ILocalStateOfEnvironment rawEnvState = engine.getEnvironment().getPublicLocalState( LambdaLifeLevelList.MICRO );
		EnvPLSInMicroLevel envState = (EnvPLSInMicroLevel) rawEnvState;
		if( this.lastState == null ){
			this.lastTime = currentTime;
			this.lastState = new boolean[ envState.getWidth() ][];
			for( int x = 0; x < envState.getWidth(); x++ ){
				this.lastState[ x ] = new boolean[ envState.getHeight() ];
				for( int y = 0; y < envState.getHeight(); y++ ){
					this.lastState[ x ][ y ] = envState.getCellAt( x, y ).isAlive( );
				}
			}
			return false;
		} else {
			if( ! currentTime.equals( this.lastTime ) ){
				boolean hasChanged = false;
				for( int x = 0; x < envState.getWidth(); x++ ){
					for( int y = 0; y < envState.getHeight(); y++ ){
						boolean newState = envState.getCellAt( x, y ).isAlive( );
						hasChanged = hasChanged || ( newState != this.lastState[ x ][ y ] );
						this.lastState[ x ][ y ] = newState;
					}
				}
				if( hasChanged ){
					this.consecutiveStill = 0;
					return false;
				} else {
					this.consecutiveStill++;
				}
			}
			this.lastTime = currentTime;
			return this.consecutiveStill >= this.threshold;
		}
	}

}
