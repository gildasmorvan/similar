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
package fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.bubble.chamber;

import java.awt.geom.Point2D;

import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractLocalStateOfAgent;

/**
 * The public local state of the "Bubble" agent in the "Chamber" level.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class AgtBubblePLSInChamber extends AbstractLocalStateOfAgent {
	/**
	 * Builds an initialized instance of this public local state.
	 * @param owner The agent owning this public local state.
	 * @param initialX The initial x coordinate of the bubble.
	 * @param initialY The initial y coordinate of the bubble.
	 * @param initialDiameter The initial diameter of the bubble.
	 * @throws IllegalArgumentException If the diameter is lower or equal to 0.
	 */
	public AgtBubblePLSInChamber(
			IAgent4Engine owner,
			double initialX,
			double initialY,
			double initialDiameter
	) {
		super(
			BubbleChamberLevelList.CHAMBER,
			owner
		);
		this.location = new Point2D.Double( initialX, initialY );
		if( initialDiameter <= 0 ){
			throw new IllegalArgumentException( "The diameter of the bubble cannot be null." );
		} else {
			this.diameter = initialDiameter;
		}
	}
	
	//
	//
	// Declaration of the content of the public local state
	//
	//
	
	/**
	 * The location of the center of the bubble in the chamber.
	 */
	private Point2D location;

	/**
	 * Gets the location of the center of the bubble in the chamber.
	 * @return The location of the center of the bubble in the chamber.
	 */
	public Point2D getLocation( ){
		return this.location;
	}

	/**
	 * The diameter of the bubble.
	 */
	private double diameter;

	/**
	 * Gets the diameter of the bubble.
	 * @return The diameter of the bubble.
	 */
	public double getDiameter( ){
		return this.diameter;
	}

	/**
	 * Sets the diameter of the bubble.
	 * @param diameter The new diameter of the bubble.
	 */
	public void setDiameter( double diameter ){
		this.diameter = diameter;
	}
}
