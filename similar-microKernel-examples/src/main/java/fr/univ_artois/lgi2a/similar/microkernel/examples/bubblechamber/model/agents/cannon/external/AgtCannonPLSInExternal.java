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
package fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.cannon.external;

import java.awt.geom.Point2D;

import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractLocalStateOfAgent;

/**
 * The public local state of the "Cannon" agent in the "External" level.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 */
public class AgtCannonPLSInExternal extends AbstractLocalStateOfAgent {
	/**
	 * Builds an initialized instance of this public local state.
	 * @param owner The agent owning this public local state.
	 * @param cannonEndX The x coordinate in the chamber where the end of the cannonPublicState points to.
	 * @param cannonEndY The y coordinate in the chamber where the end of the cannonPublicState points to.
	 * @param initialDirectionX The x-axis element of the initial direction where the cannonPublicState points to.
	 * @param initialDirectionY The y-axis element of the initial  direction where the cannonPublicState points to.
	 * @param initialPower The initial power of the particle cannonPublicState.
	 * @param initialTemperature The initial temperature of the particle cannonPublicState.
	 * @throws IllegalArgumentException If the direction is a null vector.
	 */
	public AgtCannonPLSInExternal(
			IAgent4Engine owner,
			double cannonEndX,
			double cannonEndY,
			double initialDirectionX,
			double initialDirectionY,
			double initialPower,
			double initialTemperature
	) {
		super(
			BubbleChamberLevelList.EXTERNAL,
			owner
		);
		this.entryPointInChamber = new Point2D.Double( cannonEndX, cannonEndY );
		// Set the direction as an unitary vector.
		if( initialDirectionX == 0.0 && initialDirectionY == 0.0 ){
			throw new IllegalArgumentException( "The direction cannot be (0; 0) !" );
		} else {
			double length = Math.sqrt( Math.pow( initialDirectionX, 2) + Math.pow( initialDirectionY, 2) );
			this.direction= new Point2D.Double(
					initialDirectionX / length, 
					initialDirectionY /length
			);
		}
		this.power = initialPower;
		this.temperature = initialTemperature;
	}
	
	//
	//
	// Declaration of the content of the public local state
	//
	//

	/**
	 * The coordinates in the chamber where the end of the cannonPublicState points to.
	 */
	private Point2D entryPointInChamber;

	/**
	 * Gets the coordinates in the chamber where the end of the cannonPublicState points to.
	 * @return The coordinates in the chamber where the end of the cannonPublicState points to.
	 */
	public Point2D getEntryPointInChamber( ){
		return this.entryPointInChamber;
	}
	
	/**
	 * The direction where the cannonPublicState points to.
	 */
	private Point2D direction;
	
	/**
	 * Gets the direction where the cannonPublicState points to.
	 * @return The direction where the cannonPublicState points to.
	 */
	public Point2D getDirection( ) {
		return this.direction;
	}
	
	/**
	 * Sets the direction where the cannonPublicState points to.
	 * @param power The new direction where the cannonPublicState points to.
	 */
	public void setDirection( Point2D direction ){
		this.direction = direction;
	}
	
	/**
	 * The power of the particle cannonPublicState.
	 */
	private double power;
	
	/**
	 * Gets the power of the particle cannonPublicState.
	 * @return The power of the particle cannonPublicState.
	 */
	public double getPower( ){
		return this.power;
	}
	
	/**
	 * Sets the power of the particle cannonPublicState.
	 * @param power The new power of the particle cannonPublicState.
	 */
	public void setPower( double power ){
		this.power = power;
	}
	
	/**
	 * The current temperature of the particle cannonPublicState.
	 */
	private double temperature;
	
	/**
	 * Gets the power of the particle cannonPublicState.
	 * @return The power of the particle cannonPublicState.
	 */
	public double getTemperature( ){
		return this.temperature;
	}
	
	/**
	 * Sets the power of the particle cannonPublicState.
	 * @param power The new power of the particle cannonPublicState.
	 */
	public void setTemperature( double temperature ){
		this.temperature = temperature;
	}
}
