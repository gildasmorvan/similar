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
 * implementation of Logo-like simulations using the SIMILAR API.
 * This software defines an API to implement such simulations, and also 
 * provides usage examples.
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
package fr.univ_artois.lgi2a.similar.extendedkernel.libs.web;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.probes.Slf4jExceptionPrinter;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.probes.Slf4jExecutionTracker;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.control.SimilarWebController;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.view.SimilarHttpServer;
import fr.univ_artois.lgi2a.similar.extendedkernel.simulationmodel.AbstractExtendedSimulationModel;
import fr.univ_artois.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.univ_artois.lgi2a.similar.microkernel.IProbe;
import fr.univ_artois.lgi2a.similar.microkernel.ISimulationEngine;
import fr.univ_artois.lgi2a.similar.microkernel.libs.engines.EngineMonothreadedDefaultdisambiguation;

/**
 * Facilitates the execution of Similar simulations using the HTML web interface.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="mailto:Antoine-Lecoutre@outlook.com">Antoine Lecoutre</a>
 */
public class SimilarWebRunner implements IHtmlInitializationData {
	/**
	 * The configuration of the runner.
	 */
	protected SimilarWebConfig config;
	/**
	 * The parameters of the simulation.
	 */
	protected ISimulationParameters simulationParameters;
	/**
	 * The engine of the simulation 
	 */
	protected ISimulationEngine engine;
	/**
	 * The object managing the HTML view.
	 */
	protected SimilarHttpServer view;
	/**
	 * The controller managing the interaction between the engine and the view.
	 */
	protected SimilarWebController controller;
	
	/**
	 * Creates a new runner using the default configuration.
	 */
	public SimilarWebRunner( ) {
		this.config = new SimilarWebConfig( );
	}
	
	/**
	 * Initializes the runner with a specific simulation model, using the current 
	 * configuration of the runner.
	 * 
	 * This operation can only be performed once.
	 * 
	 * @param model The model of the simulation.
	 * @throws IllegalStateException If the runner has already been initialized.
	 */
	public void initializeRunner(AbstractExtendedSimulationModel model) {
		
		if( model == null ){
			throw new IllegalArgumentException( "The model cannot be Null" );
		}
		
		// Check if the runner can be initialized
		if( this.config.isAlreadyInitialized() ) {
			throw new IllegalStateException( "The runner is alread initialized" );
		}
		// Define the name of the simulation.
		if( this.config.getSimulationName() == null ) {
			this.config.setSimulationName( model.getClass().getSimpleName() );
		}
		// Tag the runner as initializing
		this.config.finalizeConfiguration( );
		// Create the engine
		this.engine = new EngineMonothreadedDefaultdisambiguation( );
		// Creates the probes that will listen to the execution of the simulation.
		this.engine.addProbe( 
			"Error printer", 
			new Slf4jExceptionPrinter( )
		);
		this.engine.addProbe(
			"Trace printer", 
		    new Slf4jExecutionTracker( false )
		);
		// Identify the simulation parameters
		this.simulationParameters = model.getSimulationParameters();
		// Create the controller managing the interaction between the engine and the view.
		this.controller = new SimilarWebController( this.engine, model );
		// Create the SPARK HTTP server that will generate the HTML pages
		this.view = new SimilarHttpServer( this.controller, this );
		this.view.initServer();
		// Bind the view and the controller
		this.controller.setViewControls( this.view );
	}
	
	/**
	 * Opens the view on the simulation.
	 * @throws IllegalStateException If the runner is not initialized.
	 */
	public void showView( ) {
		// If the runner is not initialized, the view cannot be shown.
		if( ! this.config.isAlreadyInitialized() ) {
			throw new IllegalStateException( "The runner is not initialized." );
		}
		// Show the GUI
		this.view.showView( );
		// Then tell the controller that it has to listen to events
		this.controller.listenToViewRequests();
	}

	/**
	 * Gets the configuration that will be used by the runner and the view.
	 * @return The configuration.
	 */
	@Override
	public SimilarWebConfig getConfig() {
		return this.config;
	}

	/**
	 * Gets the parameters of the simulation.
	 * @return The parameters of the simulation.
	 */
	@Override
	public ISimulationParameters getSimulationParameters() {
		return this.simulationParameters;
	}
	
	/**
	 * Adds a probe to the registered probes of the simulation engine.
	 * @param name The name of the probe.
	 * @param probe The probe to register.
	 * @throws IllegalStateException If the runner is not initialized yet.
	 */
	public void addProbe(String name, IProbe probe) {
		if( ! this.config.isAlreadyInitialized() ) {
			throw new IllegalStateException( "The runner has to be initialized before adding other probes." );
		}
		this.engine.addProbe(name, probe);
	}

	/**
	 * @return the engine of the simulation 
	 */
	public ISimulationEngine getEngine() {
		return engine;
	}

	/**
	 * @return the controller of the web server
	 */
	public SimilarWebController getController() {
		return controller;
	}
}