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
package fr.univ_artois.lgi2a.similar.microkernel.libs.probes;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import fr.univ_artois.lgi2a.similar.microkernel.IProbe;
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;

/**
 * This probe prints the content of the simulation in a {@link JFrame}, using an image defined in
 * a {@link AbstractProbeImageSwingJPanel} probe.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class ProbeImageSwingJFrame extends JFrame implements IProbe {
	/**
	 * Serialization ID.
	 */
	private static final long serialVersionUID = -6450876291859894294L;
	/**
	 * The default width of the panel, when the initialization is being performed.
	 */
	private static final int DEFAULT_WIDTH = 400;
	/**
	 * The default height of the panel, when the initialization is being performed.
	 */
	private static final int DEFAULT_HEIGHT = 300;

	/**
	 * The probe defining the {@link JPanel} where the simulation is printed.
	 */
	private AbstractProbeImageSwingJPanel simulationPainter;
	/**
	 * Either the dimensions of the panel or <code>null</code> if the panel is automatically resized.
	 */
	private Dimension preferredDimensions;
	/**
	 * The closure strategy being used in this frame.
	 */
	private ClosingManagementStrategy closureStrategy;
	
	/**
	 * Builds the frame where the simulation is being printed on screen using a swing image.
	 * This frame ensures that its content matches the provided preferred dimensions.
	 * @param frameTitle The title of the frame being displayed on screen.
	 * @param simulationPainter The object building the image of the simulation.
	 * @param closureStrategy Defines what to do when the frame is closed by the user.
	 * @param preferredDimensions Either the dimensions of the content of the frame or 
	 * <code>null</code> if the frame is automatically resized.
	 * @throws IllegalArgumentException if either <code>frameTitle</code> or <code>simulationPainter</code> are <code>null</code>.
	 */
	public ProbeImageSwingJFrame(
		String frameTitle,
		AbstractProbeImageSwingJPanel simulationPainter,
		ClosingManagementStrategy closureStrategy,
		Dimension preferredDimensions
	) {
		if( frameTitle == null ){
			throw new IllegalArgumentException(
				"The title of the frame cannot be null."
			);
		} else if( simulationPainter == null){
			throw new IllegalArgumentException(
				"The simulation painter cannot be null."
			);
		} else if( closureStrategy == null ){
			throw new IllegalArgumentException(
				"The closure strategy cannot be null."
			);
		}
		this.setTitle( frameTitle );
		this.simulationPainter = simulationPainter;
		this.preferredDimensions = preferredDimensions;
		this.closureStrategy = closureStrategy;
		JScrollPane scroller = new JScrollPane( this.simulationPainter.getSwingView() );
		this.setContentPane( scroller );
		/*
		 * Resize the frame on screen.
		 */
		if( this.preferredDimensions != null ){
			this.setSize( DEFAULT_WIDTH, DEFAULT_HEIGHT );
		} else {
			this.pack( );
		}
	}
	
	/**
	 * Builds the frame where the simulation is being printed on screen using a swing image.
	 * This frame ensures that its content is resized automatically.
	 * When this frame is closed, the application will still wait for the simulation to end before stopping the program.
	 * @param frameTitle The title of the frame being displayed on screen.
	 * @param simulationPainter The object building the image of the simulation.
	 * @throws IllegalArgumentException if either <code>frameTitle</code> or <code>simulationPainter</code> are <code>null</code>.
	 */
	public ProbeImageSwingJFrame(
		String frameTitle,
		AbstractProbeImageSwingJPanel simulationPainter
	) {
		this(
			frameTitle,
			simulationPainter,
			ClosingManagementStrategy.HIDE_AND_WAIT_FOR_SIMULATION_END,
			null
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepareObservation() {
		/*
		 * Forward the probe event to the image displayer.
		 */
		this.simulationPainter.prepareObservation( );
		/*
		 * Then display the frame on screen.
		 */
		this.setVisible( true );
		this.setLocationRelativeTo( null );
		this.setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtInitialTimes(
		SimulationTimeStamp initialTimestamp,
		final ISimulationEngine simulationEngine
	) {
		/*
		 * Forward the probe event to the image displayer.
		 */
		this.simulationPainter.observeAtInitialTimes( 
			initialTimestamp, 
			simulationEngine 
		);
		/*
		 * Manage the frame closure strategy.
		 */
		switch( this.closureStrategy ){
		case ABORT_SIMULATION:
			this.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
			this.addWindowListener( new WindowAdapter() {
				@Override
				public void windowClosing( WindowEvent e ) {
					simulationEngine.requestSimulationAbortion();
				}
			});
			break;
		case NEVER_HIDE:
			this.setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
			break;
		case HIDE_AND_WAIT_FOR_SIMULATION_END:
		default:
			this.setDefaultCloseOperation( WindowConstants.DISPOSE_ON_CLOSE );
			break;
		}
		/*
		 * Resize the frame on screen.
		 */
		if( this.preferredDimensions != null ){
			this.setSize( this.preferredDimensions );
		} else {
			this.pack( );
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtPartialConsistentTime(
		SimulationTimeStamp timestamp,
		ISimulationEngine simulationEngine
	) {
		/*
		 * Forward the probe event to the image displayer.
		 */
		this.simulationPainter.observeAtPartialConsistentTime(
			timestamp, 
			simulationEngine
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtFinalTime(
		SimulationTimeStamp finalTimestamp,
		ISimulationEngine simulationEngine
	) {
		/*
		 * Forward the probe event to the image displayer.
		 */
		this.simulationPainter.observeAtFinalTime(
			finalTimestamp, 
			simulationEngine
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToError(
		String errorMessage, 
		Throwable cause
	) {
		/*
		 * Forward the probe event to the image displayer.
		 */
		this.simulationPainter.reactToError(
			errorMessage, 
			cause
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToAbortion(
		SimulationTimeStamp timestamp,
		ISimulationEngine simulationEngine
	) {
		/*
		 * Forward the probe event to the image displayer.
		 */
		this.simulationPainter.reactToAbortion(
			timestamp, 
			simulationEngine
		);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endObservation() {
		/*
		 * Forward the probe event to the image displayer.
		 */
		this.simulationPainter.endObservation( );
	}
	
	/**
	 * Defines the various strategies to the reaction of the application when the user requests
	 * the closure of the frame.
	 * 
	 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
	 */
	public enum ClosingManagementStrategy {
		/**
		 * The frame does not hide, even when the user clicks on the cross.
		 */
		NEVER_HIDE,
		/**
		 * The simulation will continue when the frame is closed.
		 */
		HIDE_AND_WAIT_FOR_SIMULATION_END,
		/**
		 * The simulation will be aborted if the frame is closed.
		 */
		ABORT_SIMULATION;
	}
}
