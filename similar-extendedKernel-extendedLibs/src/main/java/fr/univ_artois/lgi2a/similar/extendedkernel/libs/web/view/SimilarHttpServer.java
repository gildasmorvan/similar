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
package fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.view;

import static spark.Spark.*;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.tika.Tika;
import org.eclipse.jetty.util.log.Log;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.IHtmlControls;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.IHtmlInitializationData;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.IHtmlRequests;

/**
 * The Spark HTTP server used as a connection point between the HTML view on the simulation
 * and the java based simulation controller.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="mailto:Antoine-Lecoutre@outlook.com">Antoine Lecoutre</a>
 */
public class SimilarHttpServer implements IHtmlControls {
	/**
	 * The controller to which the requests coming from the view are forwarded.
	 */
	protected IHtmlRequests controller;

	/**
	 * The tool generating the HTML code of the view.
	 */
	protected SimilarHtmlGenerator htmlCodeGenerator;
	
	/**
	 * Creates a new Spark Http Server managing the HTML view on the simulation.
	 * @param controller The controller to which the view will be bound.
	 * @param initializationData The object providing initialization data to this server.
	 */
	public SimilarHttpServer(
		IHtmlRequests controller,
		IHtmlInitializationData initializationData
	) {
		this.controller = controller;
		// Initialize the tool generating the view
		this.htmlCodeGenerator = new SimilarHtmlGenerator(
			initializationData.getConfig().getCustomHtmlBody(),
			initializationData
		);
	}
	
	/**
	 * Initializes the web server
	 */
	public void initServer( ) {
		
		//Listens to 8080
		port(8080);

		get("/", (request, response) -> {
			response.type("text/html");
			return this.htmlCodeGenerator.renderView().render();
		});
		get("/state", (request, response) -> SimilarHttpServer.this.controller.handleSimulationStateRequest( ));
		get("/start", (request, response) -> {
			SimilarHttpServer.this.controller.handleNewSimulationRequest();
    		return "";
    	});
		get("/stop", (request, response) -> {
			SimilarHttpServer.this.controller.handleSimulationAbortionRequest();
    		return "";
		});
		get("/pause", (request, response) -> {
			SimilarHttpServer.this.controller.handleSimulationPauseRequest();
    		return "";
		});
		get("/shutdown", (request, response) -> {
			SimilarHttpServer.this.controller.handleShutDownRequest();
    		return "";
		});
		get("/setParameter", (request, response) -> {
			for( String param : request.queryParams()) {
				SimilarHttpServer.this.controller.setParameter(param, request.queryParams(param));
			}
			return "";
		});
		get("/getParameter", (request, response) -> {
			StringBuilder output =  new StringBuilder();
			for( String param : request.queryParams()) {
				output.append(param+": ");
				output.append(SimilarHttpServer.this.controller.getParameter(param)+"\n");
			}
			response.type("text/plain");
		    return output.toString();
		});
		Tika tika = new Tika();
		for(Map.Entry<String, String> resource : SimilarHtmlGenerator.textResources.entrySet()) {
			get("/"+resource.getKey(), (request, response) -> {
				response.type(tika.detect(resource.getKey())); 
				return resource.getValue();
			});
		}
		for(Map.Entry<String, byte[]> resource : SimilarHtmlGenerator.binaryResources.entrySet()) {
			get("/"+resource.getKey(), (request, response) -> {
				HttpServletResponse raw = response.raw();
				response.type(tika.detect(resource.getKey())); 
				raw.getOutputStream().write(resource.getValue());
	            raw.getOutputStream().flush();
	            raw.getOutputStream().close();
				return raw;
			});
		}
	}

	
	/**
	 * Launches the web browser of the client, where the simulation can be viewed and controlled.
	 */
	public void showView(){
		// Waits for the end of the initialization of the view
		awaitInitialization();
		// Start the browser
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {        	
	            desktop.browse(new URI("http://localhost:8080"));
	        } catch (IOException | URISyntaxException e) {
	        		Log.getRootLogger().warn(
	        			"The browser cannot be opened. Browse to http://localhost:8080", e
	        		);
	        }
	    }
	}
	
	/**
	 * Sets whether the start button is enabled or not in the view.
	 * @param active 	<code>true</code> if the start button has to be interactive. 
	 * 					<code>false</code> if it has to be deactivated in the view.
	 */
	public void setStartButtonState(boolean active) {
		//Does nothing
	}

	/**
	 * Sets whether the pause button is enabled or not in the view.
	 * @param active 	<code>true</code> if the pause button has to be interactive. 
	 * 					<code>false</code> if it has to be deactivated in the view.
	 */
	@Override
	public void setPauseButtonState(boolean active) {
		//Does nothing
	}

	/**
	 * Sets whether the abort button is enabled or not in the view.
	 * @param active 	<code>true</code> if the pause button has to be interactive. 
	 * 					<code>false</code> if it has to be deactivated in the view.
	 */
	@Override
	public void setAbortButtonState(boolean active) {
		//Does nothing
	}
	
	/**
	 * Requests the view to shut down.
	 */
	@Override
	public void shutDownView() {
	    stop();
	}
}
