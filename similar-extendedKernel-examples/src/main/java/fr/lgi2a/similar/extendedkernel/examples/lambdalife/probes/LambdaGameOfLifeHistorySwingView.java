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
package fr.lgi2a.similar.extendedkernel.examples.lambdalife.probes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.agents.cell.micro.AgtCellPLSInMicroLevel;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.environment.micro.EnvPLSInMicroLevel;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.levels.LambdaLifeLevelList;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;
import fr.lgi2a.similar.microkernel.libs.probes.AbstractProbeImageSwingJPanel;

/**
 * This probe displays the game of life grid as a Swing {@link JPanel}.
 * The cell color corresponds to the time of the last state change.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 */
public class LambdaGameOfLifeHistorySwingView extends AbstractProbeImageSwingJPanel {
	/**
	 * The conversion ratio between cells and pixels.
	 */
	private static final int cellSizeInPixels = 10;
	
	/**
	 * The last {@link SimulationTimeStamp} at which a change occurred
	 */
	private long[][] lastChange;
	
	/**
	 * Creates the probe displaying the simulation as an image in a {@link JPanel}.
	 * @param backgroundColor The background color of the {@link JPanel}. 
	 * <code>null</code> if transparent.
	 */
	public LambdaGameOfLifeHistorySwingView(
		Color backgroundColor,
		int width,
		int height,
		SimulationTimeStamp initialTime
	) {
		super( backgroundColor );
		this.lastChange = new long[width][height];
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				this.lastChange[x][y]=initialTime.getIdentifier();
			}
		}
			
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Dimension computeSimulationImageDimensions(
		IPublicDynamicStateMap dynamicState, 
		SimulationTimeStamp initialTime
	) {
		// Get the public local state of the environment in the "Micro" level.
		ILocalStateOfEnvironment rawEnPls = dynamicState.get( LambdaLifeLevelList.MICRO ).getPublicLocalStateOfEnvironment();
		EnvPLSInMicroLevel envPls = (EnvPLSInMicroLevel) rawEnPls;
		// Create the dimensions of the images.
		return new Dimension(
			envPls.getWidth() * cellSizeInPixels,
			envPls.getHeight() * cellSizeInPixels
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateGraphics(
		IPublicDynamicStateMap dynamicState,
		SimulationTimeStamp currentTime, 
		Graphics2D graphics, 
		int imgWidth,
		int imgHeight
	) {
		graphics.setBackground( null );
		graphics.clearRect(
			0, 
			0, 
			imgWidth, 
			imgHeight
		);
		// Get the public local state of the environment in the "Micro" level.
		ILocalStateOfEnvironment rawEnPls = dynamicState.get( LambdaLifeLevelList.MICRO ).getPublicLocalStateOfEnvironment();
		EnvPLSInMicroLevel envPls = (EnvPLSInMicroLevel) rawEnPls;
		// Create the content of the graphics.
		for( int x = 0; x < envPls.getWidth(); x++ ){
			for( int y = 0; y < envPls.getHeight(); y++ ){
				AgtCellPLSInMicroLevel cellPls = envPls.getCellAt( x, y );
				Color color = this.getColorFor( cellPls, envPls,  currentTime);
				if( color != null ){
					graphics.setColor( color );
					graphics.fillRect( 
						x * cellSizeInPixels, 
						y * cellSizeInPixels, 
						cellSizeInPixels, 
						cellSizeInPixels 
					);
				}
			}
		}
	}
	
	/**
	 * Gets the color of a cell depending on the state of 
	 * the cell and the last time it changed
	 * and the state of the environment.
	 * @param cellPls The state of the cell.
	 * @param envPls The state of the environment.
	 * @return The color that should be used to print the cell.
	 */
	private Color getColorFor( 
			AgtCellPLSInMicroLevel cellPls,
			EnvPLSInMicroLevel envPls, SimulationTimeStamp currentTime
		) {
			// Compute the number of neighbors
			int neighbors = 0;
			for( int dx = -1; dx <= 1; dx++ ){
				for( int dy = -1; dy <= 1; dy++ ){
					if( dx != 0 || dy != 0 ){
						AgtCellPLSInMicroLevel neighbor = envPls.getCellAt( cellPls.getX() + dx, cellPls.getY() + dy );
						if( neighbor != null && neighbor.isAlive() ){
							neighbors++;
						}
					}
				}
			}
			if( cellPls.isAlive() ){
				if( neighbors != 2 && neighbors != 3 ){
					lastChange[cellPls.getX()][cellPls.getY()] = currentTime.getIdentifier();
				}
				if(currentTime.getIdentifier()-this.lastChange[cellPls.getX()][cellPls.getY()] <= 255) {
					return  new Color(
							255- (int) (currentTime.getIdentifier()-this.lastChange[cellPls.getX()][cellPls.getY()]),
							255,
							(int) (currentTime.getIdentifier()-this.lastChange[cellPls.getX()][cellPls.getY()])
						);
				}
				if(currentTime.getIdentifier()-this.lastChange[cellPls.getX()][cellPls.getY()] <= 510) {
					return  new Color(
							0,
							510 - (int) (currentTime.getIdentifier()-this.lastChange[cellPls.getX()][cellPls.getY()]),
							255
						);
				}
				return Color.BLUE;
			} else {
				if( neighbors == 3 ) {
					lastChange[cellPls.getX()][cellPls.getY()] = currentTime.getIdentifier();
				}
				if(currentTime.getIdentifier()-this.lastChange[cellPls.getX()][cellPls.getY()] <= 255) {
					return  new Color(
							255,
							(int) (currentTime.getIdentifier()-this.lastChange[cellPls.getX()][cellPls.getY()]),
							0
							);
				}
				if(currentTime.getIdentifier()-this.lastChange[cellPls.getX()][cellPls.getY()] <= 510) {
					return  new Color(
							255,
							255,
							(int) (currentTime.getIdentifier()-this.lastChange[cellPls.getX()][cellPls.getY()]) - 255
						);
				}
				return null;
				
			}
		}
}
