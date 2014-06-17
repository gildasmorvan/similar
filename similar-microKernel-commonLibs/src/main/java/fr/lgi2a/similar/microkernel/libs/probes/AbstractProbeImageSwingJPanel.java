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
package fr.lgi2a.similar.microkernel.libs.probes;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;

/**
 * Models a probe where the simulation data are displayed into an image printed 
 * in a Swing {@link JPanel} object.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 */
public abstract class AbstractProbeImageSwingJPanel implements IProbe {
	/**
	 * The double buffer displaying the images in the swing panel.
	 */
	private BufferedImage[] doubleBuffer;
	/**
	 * The index of the double buffer pointing to the image currently being used by the display 
	 * panel to display the content of the simulation.
	 */
	private int currentBufferIndex;
	/**
	 * The panel where the probe is displayed.
	 */
	private MainJPanel mainPanel;
	
	/**
	 * Builds a swing displayer using a transparent background in the drawing area.
	 */
	protected AbstractProbeImageSwingJPanel( ) {
		this(
			null
		);
	}

	/**
	 * Builds a swing displayer using a custom background in the drawing area.
	 * @param backgroundColor A background color used when the drawing area is not filled. 
	 * <code>null</code> means that the background remains transparent.
	 */
	protected AbstractProbeImageSwingJPanel( 
		Color backgroundColor
	)  {
		super();
		this.mainPanel = new MainJPanel( backgroundColor );
		this.doubleBuffer = new BufferedImage[2];
		this.currentBufferIndex = 0;
	}
	

	/**
	 * Gets the swing panel where the simulation is displayed.
	 * @return The swing panel where the simulation is displayed.
	 */
	public JPanel getSwingView( ){
		return this.mainPanel;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepareObservation() {
		/*
		 * First release the resources of the previously displayed simulation.
		 */
		for(int index = 0; index < this.doubleBuffer.length; index++){
			if( this.doubleBuffer[index] != null ){
				this.doubleBuffer[index].flush( );
				this.doubleBuffer[index] = null;
			}
		}
		/*
		 * Then initialize the state of the panel by showing the
		 * label telling that the initialization label has to be displayed.
		 */
		this.mainPanel.showInitializationLabel( );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtInitialTimes(
		SimulationTimeStamp initialTimestamp,
		ISimulationEngine simulationEngine
	) {
		/*
		 * Build the images used in the double buffer.
		 */
		Dimension imageDimensions = this.computeSimulationImageDimensions( 
			simulationEngine.getSimulationDynamicStates(), 
			initialTimestamp 
		);
		for(int index = 0; index < this.doubleBuffer.length; index++){
			this.doubleBuffer[index] = new BufferedImage( 
				imageDimensions.width, 
				imageDimensions.height, 
				BufferedImage.TYPE_INT_ARGB
			);
		}
		/*
		 * Create the first image used in the double buffer.
		 */
		Graphics2D graphics = this.doubleBuffer[this.currentBufferIndex].createGraphics( );
		this.updateGraphics( 
			simulationEngine.getSimulationDynamicStates(), 
			initialTimestamp, 
			graphics, 
			this.doubleBuffer[this.currentBufferIndex].getWidth( ),
			this.doubleBuffer[this.currentBufferIndex].getHeight( )
		);
		graphics.dispose( );
		/*
		 * Perform the double buffer swap to display the newly refreshed image.
		 */
		this.mainPanel.updateSimulationPanel( this.doubleBuffer[this.currentBufferIndex] );
		/*
		 * Hide the label telling that the simulation is initializing and show the image.
		 */
		this.mainPanel.showSimulationPanel( );
	}

	/**
	 * Updates the contents of a graphics object with the contents of the levels of the simulation.
	 * <p>
	 * 	Usually, the implementation of this method consists in:
	 * </p>
	 * <ol>
	 * 	<li>
	 * 		Clearing the drawing area either with the background color or with a custom color, using
	 * 		the {@link Graphics2D#setBackground(Color)} and {@link Graphics2D#clearRect(int, int, int, int)} methods.
	 * 	</li>
	 * 	<li>
	 * 		Printing the data of the simulation contained in the <code>dynamicState</code> argument, using the various filling and drawing 
	 * 		methods of the <code>graphics</code> argument after setting either the drawing color with {@link Graphics2D#setColor(Color)} or a
	 * 		more complex painting style using {@link Graphics2D#setPaint(java.awt.Paint)}.
	 * 	</li>
	 * </ol>
	 * @param dynamicState The current dynamic state of the simulation.
	 * @param currentTime The current time of the simulation.
	 * @param graphics The graphical context used to modify the image.
	 * @param imgWidth The width of the image where the simulation is painted.
	 * @param imgHeight The height of the image where the simulation is painted.
	 */
	protected abstract void updateGraphics(
		IPublicDynamicStateMap dynamicState, 
		SimulationTimeStamp currentTime, 
		Graphics2D graphics, 
		int imgWidth, 
		int imgHeight
	);

	/**
	 * Computes the dimensions of the image that will display the simulation.
	 * @param dynamicState The current dynamic state of the simulation.
	 * @param initialTime The initial time of the simulation.
	 * @return The dimensions of the image where the content of the simulation will be displayed.
	 */
	protected abstract Dimension computeSimulationImageDimensions(
		IPublicDynamicStateMap dynamicState, 
		SimulationTimeStamp initialTime
	);
	
	/**
	 * Refreshes the not displayed image of the double buffer with the simulation data 
	 * defined as arguments of this method, and then performs a double buffer swap to display
	 * its content on screen.
	 * @param currentTime The current time of the simulation.
	 * @param dynamicState The dynamic state of the simulation to display on screen.
	 */
	protected void swapDoubleBufferAndDisplayImage(
		SimulationTimeStamp currentTime,
		IPublicDynamicStateMap dynamicState
	){
		/*
		 * Update the double buffer index being used.
		 * After this instruction, the 'currentBufferIndex' field contains the index
		 * of the image of the double buffer to display on screen.
		 */
		this.currentBufferIndex = (this.currentBufferIndex + 1) % 2;
		/*
		 * Refresh the image that has to be displayed on screen.
		 */
		Graphics2D graphics = this.doubleBuffer[this.currentBufferIndex].createGraphics( );
		this.updateGraphics( 
				dynamicState, 
				currentTime, 
				graphics, 
				this.doubleBuffer[this.currentBufferIndex].getWidth( ),
				this.doubleBuffer[this.currentBufferIndex].getHeight( )
		);
		graphics.dispose( );
		/*
		 * Perform the double buffer swap and tell Swing to display the appropriate image.
		 */
		this.mainPanel.updateSimulationPanel( this.doubleBuffer[this.currentBufferIndex] );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtPartialConsistentTime(
		SimulationTimeStamp time,
		ISimulationEngine engine
	) {
		/*
		 * Refresh the image displayed on screen. 
		 */
		this.swapDoubleBufferAndDisplayImage(
			time, 
			engine.getSimulationDynamicStates()
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtFinalTime(
			SimulationTimeStamp lastTime,
			ISimulationEngine engine
	) {
		/*
		 * Refresh the image displayed on screen. 
		 */
		this.swapDoubleBufferAndDisplayImage(
			lastTime, 
			engine.getSimulationDynamicStates()
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToAbortion(
		SimulationTimeStamp time, 
		ISimulationEngine engine
	) {
		/*
		 * Does nothing in this class.
		 */
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToError(
		String time, 
		Throwable cause
	) {
		/*
		 * Does nothing in this class.
		 */
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endObservation() {
		/*
		 * Does nothing in this class.
		 */
	}
	
	/**
	 * The panel where the simulation is being displayed.
	 * This panel also displays a label in case where the simulation is not yet initialized.
	 * 
	 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
	 */
	protected class MainJPanel extends JPanel {
		/**
		 * Serialization ID.
		 */
		private static final long serialVersionUID = -6032047184854450026L;
		/**
		 * The identifier of the component displayed when the simulation is yet to be initialized.
		 */
		private static final String SIMULATION_NOT_INITIALIZED_JCOMPONENT = "notInitializedLabel";
		/**
		 * The identifier of the component displayed when the simulation is initialized.
		 */
		private static final String SIMULATION_JCOMPONENT = "levelPainter";
		
		/**
		 * The layout used either to display the content of the simulation or to display
		 * a message telling that the simulation is not yet initialized.
		 */
		private CardLayout cardLayout;
		/**
		 * The label displaying that the simulation is not yet initialized.
		 */
		private JLabel notInitializedLabel;
		/**
		 * The object used to paint the simulation.
		 */
		private SimulationPainter simulationPainter;
		
		/**
		 * Builds an initialized JPanel where the probe is being displayed.
		 * @param background The background color used in the panel.
		 */
		public MainJPanel ( Color background ){
			this.notInitializedLabel = new JLabel( "The simulation is not yet initialized." );
			this.simulationPainter = new SimulationPainter( background );
			this.cardLayout = new CardLayout( );
			this.setLayout( this.cardLayout );
			this.add( this.notInitializedLabel, MainJPanel.SIMULATION_NOT_INITIALIZED_JCOMPONENT );
			this.add( this.simulationPainter, MainJPanel.SIMULATION_JCOMPONENT );
			
			this.showInitializationLabel( );
		}

		/**
		 * Displays the initialization label in the panel.
		 */
		public final void showInitializationLabel(){
			this.cardLayout.show( this, MainJPanel.SIMULATION_NOT_INITIALIZED_JCOMPONENT );
		}

		/**
		 * Displays the simulation panel in the panel.
		 */
		public final void showSimulationPanel( ) {
			this.cardLayout.show( this, MainJPanel.SIMULATION_JCOMPONENT );
		}
		
		/**
		 * Swaps the image being printed into the panel.
		 * @param printerImage The image containing a 2D representation of the level of the simulation.
		 */
		public final void updateSimulationPanel( BufferedImage printerImage ){
			this.simulationPainter.printerImage = printerImage;
			this.simulationPainter.repaint( );
		}
	}

	/**
	 * The panel where the simulation is painted.
	 * 
	 * @author <a href="http://www.lgi2a.univ-artois.fr/" target="_blank">LGI2A</a> -- <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
	 */
	private class SimulationPainter extends JComponent {
		/**
		 * Serialization ID of this class.
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * The default dimensions of this panel.
		 */
		private final Dimension defaultPanelDimensions = new Dimension( 100, 100 );
		/**
		 * The insets used in the empty border.
		 */
		private static final int BORDER_INSETS = 5;
		/**
		 * The image containing a 2D representation of the level of the simulation.
		 */
		private BufferedImage printerImage;

		/**
		 * Constructor of the class {@link SimulationPainter}.
		 * @param background The background color of the drawing area. <code>null</code> if it remains transparent.
		 */
		public SimulationPainter( Color background ){
			super();
			this.setBorder( new CompoundBorder(
				new EmptyBorder( BORDER_INSETS, BORDER_INSETS, BORDER_INSETS, BORDER_INSETS),
				new EtchedBorder( )
			) );
			if( background != null ){
				this.setBackground( background );
			}
		}

		/**
		 * {@inheritDoc}
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent( g );
			g.setColor( this.getBackground( ) );
			g.fillRect( 
				this.getInsets( ).left, 
				this.getInsets( ).top, 
				this.getWidth( ) - this.getInsets( ).left - this.getInsets( ).right, 
				this.getHeight( ) - this.getInsets( ).top - this.getInsets( ).bottom
			);
			if(this.printerImage != null){
				Graphics2D graphics2d = (Graphics2D) g;
				graphics2d.drawImage( this.printerImage, getInsets( ).left, getInsets( ).top, null );
			}
		}

		/**
		 * {@inheritDoc}
		 * @see javax.swing.JComponent#getPreferredSize()
		 */
		public Dimension getPreferredSize(){
			Dimension preferredSize;
			if( this.printerImage != null ){
				preferredSize = new Dimension( 
					this.printerImage.getWidth( ) + getInsets( ).left + getInsets( ).right, 
					this.printerImage.getHeight( ) + getInsets( ).top + getInsets( ).bottom
				);
			} else {
				preferredSize = this.defaultPanelDimensions;
			}
			return preferredSize;
		}
	}
}
