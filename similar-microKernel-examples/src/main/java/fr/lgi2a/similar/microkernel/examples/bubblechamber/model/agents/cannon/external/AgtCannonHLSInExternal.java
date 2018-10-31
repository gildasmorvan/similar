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
package fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.cannon.external;

import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.lgi2a.similar.microkernel.libs.abstractimpl.AbstractLocalStateOfAgent;

/**
 * The private local state of the "Cannon" agent in the "External" level.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 */
public class AgtCannonHLSInExternal extends AbstractLocalStateOfAgent {
	/**
	 * Builds an initialized instance of this private local state.
	 * @param owner The agent owning this private local state.
	 * @param initialOverheatTemperature The temperature starting which the cannonPublicState 
	 * is considered as overheated.
	 * @param maxAngle The maximal angle of the cannonPublicState (in rad).
	 * @param angularSpeed The angular speed (in rad/step) of the cannonPublicState.
	 * @throws IllegalArgumentException If the direction is a null vector.
	 */
	public AgtCannonHLSInExternal(
			IAgent4Engine owner,
			double initialOverheatTemperature,
			double maxAngle,
			double angularSpeed
			
	) {
		super(
			BubbleChamberLevelList.EXTERNAL,
			owner
		);
		this.overheatTemperature = initialOverheatTemperature;
		this.maxAngle = maxAngle;
		this.angularSpeed = angularSpeed;
		this.clockWiseRotation = false;
	}
	
	//
	//
	// Declaration of the content of the private local state
	//
	//

	/**
	 * The overheat temperature of the cannonPublicState.
	 */
	private double overheatTemperature;
	
	/**
	 * The maximal angle of the cannonPublicState (in rad).
	 */
	private double maxAngle;
	
	/**
	 * The angular speed (in rad/step) of the cannonPublicState.
	 */
	private double angularSpeed;
	
	/**
	 * <code>true</code> if the cannonPublicState rotates clockwise.
	 */
	private boolean clockWiseRotation;


	/**
	 * Gets the overheat temperature of the cannonPublicState.
	 * @return The overheat temperature of the cannonPublicState.
	 */
	public double getOverheatTemperature( ){
		return this.overheatTemperature;
	}


	/**
	 * @return the maximal angle of the cannonPublicState (in rad).
	 */
	public double getMaxAngle() {
		return maxAngle;
	}


	/**
	 * @return the angular speed (in rad/step) of the cannonPublicState.
	 */
	public double getAngularSpeed() {
		return angularSpeed;
	}


	/**
	 * @return <code>true</code> if the cannonPublicState rotates clockwise.
	 */
	public boolean isClockWiseRotation() {
		return clockWiseRotation;
	}


	/**
	 * @param clockWiseRotation <code>true</code> if the cannonPublicState rotates clockwise.
	 */
	public void setClockWiseRotation(boolean clockWiseRotation) {
		this.clockWiseRotation = clockWiseRotation;
	}
}
