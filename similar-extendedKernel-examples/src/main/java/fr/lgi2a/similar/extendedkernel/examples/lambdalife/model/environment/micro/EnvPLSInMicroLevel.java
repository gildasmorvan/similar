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
package fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.environment.micro;

import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.agents.cell.micro.AgtCellPLSInMicroLevel;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.levels.LambdaLifeLevelList;
import fr.lgi2a.similar.microkernel.libs.abstractimpl.AbstractLocalStateOfEnvironment;

/**
 * The public local state of the environment in the "micro" level of the Lambda game of life simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 */
public class EnvPLSInMicroLevel extends AbstractLocalStateOfEnvironment {
	/**
	 * The width of the grid of the environment.
	 */
	private int width;
	/**
	 * The height of the grid of the environment.
	 */
	private int height;
	/**
	 * <code>true</code> if the grid is a torus along the x axis.
	 */
	private boolean xAxisTorus;
	/**
	 * <code>true</code> if the grid is a torus along the y axis.
	 */
	private boolean yAxisTorus;
	/**
	 * The grid of agents used in the level.
	 */
	private AgtCellPLSInMicroLevel[][] grid;
	
	/**
	 * Creates a new instance of the public local state of the environment in the "micro" level.
	 * <p>
	 * 	After the call to this method, the agents in the grid have to be initialized by calls to the 
	 * 	{@link EnvPLSInMicroLevel#setCellAt(AgtCellHLSInMicroLevel)} method.
	 * </p>
	 * @param width The width of the grid of the environment.
	 * @param height The height of the grid of the environment.
	 * @param xAxisTorus <code>true</code> if the grid is a torus along the x axis.
	 * @param yAxisTorus <code>true</code> if the grid is a torus along the y axis.
	 */
	public EnvPLSInMicroLevel( 
		int width,
		int height,
		boolean xAxisTorus,
		boolean yAxisTorus
	) {
		super( LambdaLifeLevelList.MICRO );
		this.width = width;
		this.height = height;
		this.xAxisTorus = xAxisTorus;
		this.yAxisTorus = yAxisTorus;
		this.grid = new AgtCellPLSInMicroLevel[ this.width ][];
		for( int x = 0; x < this.width; x++ ){
			this.grid[ x ] = new AgtCellPLSInMicroLevel[ this.height ];
		}
	}
	
	/**
	 * Gets the width of the grid of the environment.
	 * @return The width of the grid of the environment.
	 */
	public int getWidth( ) {
		return this.width;
	}
	
	/**
	 * Gets the height of the grid of the environment.
	 * @return The height of the grid of the environment.
	 */
	public int getHeight( ){
		return this.height;
	}
	
	/**
	 * Gets if the grid is a torus along the x axis or not.
	 * @return <code>true</code> if the grid is a torus along the x axis.
	 */
	public boolean isXAxisTorus( ){
		return this.xAxisTorus;
	};
	
	/**
	 * Gets if the grid is a torus along the y axis or not.
	 * @return <code>true</code> if the grid is a torus along the y axis.
	 */
	public boolean isYAxisTorus( ){
		return this.yAxisTorus;
	};
	
	/**
	 * Gets the public local state of the cell agent located at the specified location.
	 * @param x The x coordinate where to get the public local state of the agent.
	 * @param y The y coordinate where to get the public local state of the agent.
	 * @return The public local state of the agent, or <code>null</code> if the location 
	 * is out of the simulation.
	 */
	public AgtCellPLSInMicroLevel getCellAt( 
		int x, 
		int y 
	){
		int correctedX = x;
		if( this.xAxisTorus ){
			while( correctedX < 0 ){
				correctedX += this.width;
			}
			correctedX = correctedX % this.width;
		}
		int correctedY = y;
		if( this.yAxisTorus ){
			while( correctedY < 0 ){
				correctedY += this.height;
			}
			correctedY = correctedY % this.height;
		}
		try {
			return this.grid[ correctedX ][ correctedY ];
		} catch( ArrayIndexOutOfBoundsException e ){
			return null;
		}
	}

	/**
	 * Defines the public local state of the cell agent located at the specified location.
	 * @param cell The public local state of the cell agent. 
	 * The x and y coordinates of the cell have to be valid.
	 */
	public void setCellAt(
		AgtCellPLSInMicroLevel cell
	){
		this.grid[ cell.getX() ][ cell.getY() ] = cell;
	}
}
