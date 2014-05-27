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
package fr.lgi2a.similar.extendedkernel.examples.lambdalife.probes;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.agents.cell.micro.AgtCellPLSInMicroLevel;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.environment.micro.EnvPLSInMicroLevel;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.levels.LambdaLifeLevelList;
import fr.lgi2a.similar.microkernel.*;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;

/**
 * This probe exports as an image the last state that was reached by the simulation.
 * This probe prints the alive cells in blue if they will stay alive in the next step of the simulation, and in red 
 * if they should die according to the regular game of life rules.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 */
public class LambdaGameOfLifeLastStateExporter implements IProbe {
	/**
	 * The conversion ratio between cells and pixels.
	 */
	private static final int cellSizeInPixels = 100;
	
	/**
	 * The name of the file where the state of the simulation is exported.
	 */
	private String outputName;
	
	/**
	 * Builds a probe exporting the last state of the simulation as an image.
	 * @param fileName The name of the image file used for exportation.
	 */
	public LambdaGameOfLifeLastStateExporter(
		String fileName
	) {
		this.outputName = fileName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endObservation() { }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtFinalTime( SimulationTimeStamp time, ISimulationEngine engine ) { 
		// Get the public local state of the environment in the "Micro" level.
		IPublicDynamicStateMap dynamicState = engine.getSimulationDynamicStates();
		ILocalStateOfEnvironment rawEnPls = dynamicState.get( LambdaLifeLevelList.MICRO ).getPublicLocalStateOfEnvironment();
		EnvPLSInMicroLevel envPls = (EnvPLSInMicroLevel) rawEnPls;
		// Create an image where the simulation is drawn.
		BufferedImage image = new BufferedImage( 
			envPls.getWidth() * cellSizeInPixels, 
			envPls.getHeight() * cellSizeInPixels, 
			BufferedImage.TYPE_INT_ARGB
		);
		Graphics2D graphics = image.createGraphics( );
		graphics.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
		graphics.transform( AffineTransform.getScaleInstance(cellSizeInPixels, cellSizeInPixels) );
		// Draw the simulation.
		graphics.setBackground( Color.WHITE );
		graphics.clearRect( 0, 0, envPls.getWidth(), envPls.getHeight() );
		for( int x = 0; x < envPls.getWidth(); x++ ){
			for( int y = 0; y < envPls.getHeight(); y++ ){
				AgtCellPLSInMicroLevel cellPls = envPls.getCellAt( x, y );
				Color color = this.getColorFor( cellPls, envPls );
				if( color != null ){
					graphics.setColor( color );
					graphics.fillOval( 
						x, 
						y, 
						1, 
						1 
					);
				}
			}
		}
		// Then export the image in a file.
		graphics.dispose();
		try {
			ImageIO.write(
				image, 
				"PNG", 
				new File( this.outputName )
			);
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the color of a cell depending on the state of 
	 * the cell and the state of the environment.
	 * @param cellPls The state of the cell.
	 * @param envPls The state of the environment.
	 * @return The color that should be used to print the cell.
	 */
	private Color getColorFor( 
		AgtCellPLSInMicroLevel cellPls,
		EnvPLSInMicroLevel envPls
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
			if( neighbors == 2 || neighbors == 3 ){
				return Color.BLUE;
			} else {
				return Color.RED;
			}
		} else {
			if( neighbors == 3 ) {
				return Color.GREEN;
			} else {
				return null;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtInitialTimes( SimulationTimeStamp arg0, ISimulationEngine arg1 ) { }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtPartialConsistentTime(SimulationTimeStamp arg0,ISimulationEngine arg1) { }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepareObservation() { }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToAbortion(SimulationTimeStamp arg0, ISimulationEngine arg1) { }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToError(String arg0, Throwable arg1) { }
}
