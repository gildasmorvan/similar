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
package fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.particle.chamber;

import java.awt.geom.Point2D;

import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractLocalStateOfAgent;

/**
 * The public local state of the "Particle" agent in the "Chamber" level.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class AgtParticlePLSInChamber extends AbstractLocalStateOfAgent {
	/**
	 * Builds an initialized instance of this public local state.
	 * @param owner The agent owning this public local state.
	 * @param initialX The initial x coordinate of the particle.
	 * @param initialY The initial y coordinate of the particle.
	 * @param initialVelocityAlongX The initial velocity of the particle along the X axis.
	 * @param initialVelocityAlongY The initial velocity of the particle along the Y axis.
	 */
	public AgtParticlePLSInChamber(
			IAgent4Engine owner,
			double initialX,
			double initialY,
			double initialVelocityAlongX,
			double initialVelocityAlongY,
			double initialAccelerationAlongX,
			double initialAccelerationAlongY
	) {
		super(
			BubbleChamberLevelList.CHAMBER,
			owner
		);
		this.location = new Point2D.Double( initialX, initialY );
		this.velocity = new Point2D.Double(
				initialVelocityAlongX,
				initialVelocityAlongY
		);
		this.acceleration = new Point2D.Double(
				initialAccelerationAlongX,
				initialAccelerationAlongY
		);
	}
	
	//
	//
	// Declaration of the content of the public local state
	//
	//
	
	/**
	 * The location of the particle in the chamber.
	 */
	private Point2D location;

	/**
	 * Gets the location of the particle in the chamber.
	 * @return The location of the particle in the chamber.
	 */
	public Point2D getLocation( ){
		return this.location;
	}

	/**
	 * Sets the location of the particle in the chamber.
	 * @param x The new x coordinate of the particle in the chamber.
	 * @param y The new y coordinate of the particle in the chamber.
	 */
	public void setLocation( double x, double y ){
		this.location.setLocation( x, y );
	}
	
	/**
	 * The vector determining the current velocity of the particle along both axes.
	 */
	private Point2D velocity;

	/**
	 * Gets the velocity of the particle in the chamber.
	 * @return The velocity of the particle in the chamber.
	 */
	public Point2D getVelocity( ){
		return this.velocity;
	}
	
	/**
	 * Sets the velocity of the particle in the chamber.
	 * @param dx The velocity of the particle along the x axis.
	 * @param dy The velocity of the particle along the y axis.
	 */
	public void setVelocity( double dx, double dy ){
		this.velocity.setLocation( dx, dy );
	}
	
	/**
	 * The vector determining the current acceleration of the particle along both axes.
	 */
	private Point2D acceleration;

	/**
	 * Gets the acceleration of the particle in the chamber.
	 * @return The acceleration of the particle in the chamber.
	 */
	public Point2D getAcceleration( ){
		return this.acceleration;
	}
	
	/**
	 * Sets the acceleration of the particle in the chamber.
	 * @param dx2 The new acceleration of the particle in the chamber, along the x axis.
	 * @param dy2 The new acceleration of the particle in the chamber, along the y axis.
	 */
	public void setAcceleration( double dx2, double dy2 ){
		this.acceleration.setLocation( dx2, dy2 );
	}
}
