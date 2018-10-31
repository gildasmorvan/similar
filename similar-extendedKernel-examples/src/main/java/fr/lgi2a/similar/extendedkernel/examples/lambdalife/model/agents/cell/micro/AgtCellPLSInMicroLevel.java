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
package fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.agents.cell.micro;

import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.levels.LambdaLifeLevelList;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.libs.abstractimpl.AbstractLocalStateOfAgent;

/**
 * The public local state of the "Cell" agent in the "Micro" level.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 */
public class AgtCellPLSInMicroLevel extends AbstractLocalStateOfAgent {
	/**
	 * The x coordinate of the cell in the grid.
	 */
	private int x;
	/**
	 * The y coordinate of the cell in the grid.
	 */
	private int y;
	/**
	 * The state of the cell: false (dead) or true (alive).
	 */
	private boolean alive;
	
	/**
	 * Builds an initialized public local state of a "cell" agent.
	 * @param owner The agent owning the public local state.
	 * @param initiallyAlive <code>true</code> if the cell is initially alive.
	 * @param x The x coordinate of the cell in the grid.
	 * @param y The y coordinate of the cell in the grid.
	 */
	public AgtCellPLSInMicroLevel(
		IAgent4Engine owner, 
		boolean initiallyAlive,
		int x,
		int y
	) {
		super( LambdaLifeLevelList.MICRO, owner );
		this.alive = initiallyAlive;
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Gets the x coordinate of the cell in the grid.
	 * @return The x coordinate of the cell in the grid.
	 */
	public int getX( ){
		return this.x;
	}
	
	/**
	 * Gets the y coordinate of the cell in the grid.
	 * @return The y coordinate of the cell in the grid.
	 */
	public int getY( ){
		return this.y;
	}
	
	/**
	 * Gets if the cell is alive or not.
	 * @return <code>true</code> if the cell is alive.
	 */
	public boolean isAlive( ) {
		return this.alive;
	}
	
	/**
	 * Sets if the cell becomes alive or not.
	 * @param alive <code>true</code> if the cell becomes alive.
	 */
	public void setAlive( boolean alive ) {
		this.alive = alive;
	}
}
