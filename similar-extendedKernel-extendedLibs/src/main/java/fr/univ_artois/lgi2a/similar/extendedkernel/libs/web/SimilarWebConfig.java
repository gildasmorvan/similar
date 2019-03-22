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

import java.io.IOException;
import java.io.InputStream;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.view.SimilarHtmlGenerator;

/**
 * The configuration options of Similar simulations using the HTML web interface.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="mailto:Antoine-Lecoutre@outlook.com">Antoine Lecoutre</a>
 */
public class SimilarWebConfig {
	/**
	 * Flag set to true whenever the initialization of the runner is done.
	 * Once true, the configuration cannot be modified any more.
	 */
	protected volatile boolean initializationDone;
	/**
	 * The name of the simulation, displayed in the HTML view.
	 */
	protected String simulationName;
	
	/**
	 * The HTML code to use as the body of the simulation being displayed.
	 */
	protected String customHtmlBody;
	
	/**
	 * The error message displayed when an IllegalStateException is thrown.
	 */
	protected static final String ERROR_MESSAGE = "The runner is already initialized and cannot be configured any more.";
	
	/**
	 * Creates a default content for the configuration.
	 */
	public SimilarWebConfig( ) {
		this.initializationDone = false;
		this.simulationName = null;
		this.setCustomHtmlBodyFromString("");
	}
	
	/**
	 * Finalizes the configuration, and prevent its future modifications.
	 * This method is called when the runner is being initialized to preserve 
	 * the coherence of the initialization process.
	 */
	public void finalizeConfiguration( ) {
		this.initializationDone = true;
	}
	
	/**
	 * Check if the corresponding runner is already initialized or not.
	 * @return <code>true</code> if the runner is initialized, <code>false</code> else.
	 */
	public boolean isAlreadyInitialized( ) {
		return this.initializationDone;
	}
	
	/**
	 * Gets the name of the simulation, displayed in the HTML view.
	 * @return The name of the simulation.
	 */
	public String getSimulationName(){
		return this.simulationName;
	}
	
	/**
	 * Gets the custom HTML body defined in this configuration, if it has been defined.
	 * @return 	<code>null</code> if the default configuration has to be used.
	 * 			Else, the HTML code to use as the body of the simulation being displayed.
	 */
	public String getCustomHtmlBody( ) {
		return this.customHtmlBody;
	}


	/**
	 * Sets a custom HTML body to use as the canvas where the simulation is displayed.
	 * @param customHtmlBody The HTML code to use as the body of the simulation being displayed.
	 * @return The updated configuration.
	 */
	public final SimilarWebConfig setCustomHtmlBodyFromString( String customHtmlBody ) {
		if( ! this.initializationDone ){
			this.customHtmlBody = customHtmlBody;
		} else {
			throw new IllegalStateException( ERROR_MESSAGE );
		}
		return this;
	}
	
	/**
	 * Sets a custom HTML body to use as the canvas where the simulation is displayed.
	 * @param resource The InputStream of the HTML body.
	 * @return The updated configuration.
	 * @throws IOException If the input stream cannot be read.
	 */
	public final SimilarWebConfig setCustomHtmlBody( InputStream resource ) {
		if( ! this.initializationDone ){
			this.customHtmlBody = SimilarHtmlGenerator.getViewResource( resource );
		} else {
			throw new IllegalStateException( ERROR_MESSAGE );
		}
		return this;
	}
	
	/**
	 * Sets the name of the simulation, displayed in the HTML view.
	 * @param simulationName The name of the simulation.
	 * @return The updated configuration.
	 * @throws IOException If the input stream cannot be read.
	 */
	public SimilarWebConfig setSimulationName( String simulationName ) {
		if( ! this.initializationDone ){
			this.simulationName = simulationName;
		} else {
			throw new IllegalStateException( ERROR_MESSAGE );
		}
		return this;
	}
}