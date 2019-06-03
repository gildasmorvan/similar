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

import static j2html.TagCreator.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jetty.util.log.Log;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.IHtmlInitializationData;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.ResourceNotFoundException;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.annotations.Parameter;
import fr.univ_artois.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import j2html.tags.DomContent;
import spark.utils.IOUtils;

/**
 * The helper class generating the HTML interface used by the SimilarWebRunner.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="mailto:Antoine-Lecoutre@outlook.com">Antoine Lecoutre</a>
 */
public class SimilarHtmlGenerator {
	
	/**
	 * The path and content of the binary resources.
	 */
	protected static final Map<String, byte[]> binaryResources = new LinkedHashMap<>();
	
	/**
	 * The path and content of the text resources.
	 */
	protected static final Map<String, String> textResources = new LinkedHashMap<>();
	
	static {
		addTextResource("js/jquery-3.3.1.min.js", SimilarHtmlGenerator.class);
		addTextResource("js/bootstrap.min.js", SimilarHtmlGenerator.class);
		addTextResource("css/bootstrap.min.css", SimilarHtmlGenerator.class);
		addTextResource("js/similar-gui.js", SimilarHtmlGenerator.class);
		addTextResource("css/similar-gui.css", SimilarHtmlGenerator.class);
		addBinaryResource("img/similar-logo.png", SimilarHtmlGenerator.class);
	}

	/**
	 * The object providing initialization data to this view.
	 */
	protected IHtmlInitializationData initializationData;
	/**
	 * The body of the web GUI.
	 */
	protected DomContent htmlBody;
	
	/**
	 * Builds a HTML code generator where the body of the web GUI is manually defined.
	 * @param htmlBody The body of the web GUI.
	 * @param initializationData The object providing initialization data to this view.
	 */
	public SimilarHtmlGenerator(
		String htmlBody,
		IHtmlInitializationData initializationData
	) {
		this.htmlBody = rawHtml(htmlBody);
		this.initializationData = initializationData;
	}

	/**
	 * Builds a HTML code generator where the body of the web GUI is empty.
	 * @param initializationData The object providing initialization data to this view.
	 */
	public SimilarHtmlGenerator(
		IHtmlInitializationData initializationData
	) {
		this( "", initializationData );
	}
	
	/**
	 * Builds a HTML code generator where the body of the web GUI is obtained through an input stream.
	 * @param htmlBody The body of the web GUI.
	 * @param initializationData The object providing initialization data to this view.
	 * @throws IOException 
	 */
	public SimilarHtmlGenerator(
		InputStream htmlBody,
		IHtmlInitializationData initializationData
	) throws IOException {
		this( IOUtils.toString(htmlBody), initializationData );
	}
	
	public static final void addTextResource(String path, Class<?> c) {
		try {
			textResources.put(path, IOUtils.toString(c.getResourceAsStream(path)));
		} catch (IOException e) {
			throw new ResourceNotFoundException(e);
		}
	}
	
	public static final void addBinaryResource(String path, Class<?> c) {
		try {
			binaryResources.put(path, IOUtils.toByteArray(c.getResourceAsStream(path)));
		} catch (IOException e) {
			throw new ResourceNotFoundException(e);
		}
	}
	
	public DomContent renderView( ) {
		return html(
			renderHtmlHeader(),
			body(
				div().withClass("container").with(
					renderSimulationControl(),
					div(),
					h2().withId("simulation-title").withText(this.initializationData.getConfig().getSimulationName()),
					div().withClass("row").with(
						div().withClass("col-md-2 col-lg-2").with(
							renderParameters(this.initializationData.getSimulationParameters())
						),
						div().withClass("col-md-10 col-lg-10").with(
							this.htmlBody
						)
					)
				)
			)
		);
	}
	
	/**
	 * Generates the HTML code of the header of the GUI.
	 * @return the header of the web GUI.
	 */
	protected DomContent renderHtmlHeader( ) {
		
		return head(
			meta().attr("http-equiv", "X-UA-Compatible").withContent("IE=edge"),
			meta().withName("viewport").withContent("width=device-width, initial-scale=1"),
			title(initializationData.getConfig().getSimulationName()),
			each(
				filter(
					textResources.entrySet(), r -> r.getKey().endsWith(".css")
				), r -> link().withRel("stylesheet").withHref(r.getKey())
			),
			each(
				filter(
					textResources.entrySet(), r -> r.getKey().endsWith(".js")
				), r -> script().withSrc(r.getKey())
			)
		);
	}
	
	/**
	 * Generates the HTML code of the simulation control of the GUI.
	 * @return the simulation control of the web GUI.
	 */
	protected DomContent renderSimulationControl( ) {		
		return div().withClass("page-header").with(
				div().withClass("btn-toolbar pull-right").with(
					button().withId("startSimulation")
							.withType("button")
							.withClass("btn btn-success")
							.attr("onClick", "startSimulation()")
							.with(rawHtml("&nbsp;&#9654;&nbsp;")),
					button().withId("stopSimulation")
							.withType("button")
							.withClass("btn btn-danger")
							.attr("onClick", "stopSimulation()")
							.with(rawHtml("&nbsp;&#9632;&nbsp;")),
					button().withId("pauseSimulation")
							.withType("button")
							.withClass("btn btn-primary")
							.attr("onClick", "pauseSimulation()")
							.with(rawHtml("&#9616;&#9616;&nbsp;")),
					button().withId("exitSimulation")
							.withType("button")
							.withClass("btn btn-primary")
							.attr("onClick", "exitSimulation()")
							.with(rawHtml("&nbsp;&#9167;&nbsp;"))
			),
			a().withHref("https://www.lgi2a.univ-artois.fr/~morvan/similar.html")
			   .withTarget("_blank")
			   .with(img().withSrc("img/similar-logo.png").attr("height", "60"))
		);
	}

	/**
	 * Generates the HTML code of the interface that allows users to modify the parameters of the simulation.
	 * @param parameters The parameters of the simulation.
	 * @param output the html interface that allows users to modify the parameters.
	 */
	protected static DomContent renderParameters(
		ISimulationParameters parameters
	) {
		return form().attr("data-toggle", "validator").with(
			each(Arrays.asList(parameters.getClass().getFields()), parameter -> renderParameter(parameters, parameter))
		);
	}
	
	/**
	 * Generates the HTML code of the interface that allows users to modify a parameter of the simulation.
	 * @param parameters The parameters of the simulation.
	 * @param parameter The rendered form entry for the parameter.
	 * @param output the html interface that allows users to modify the parameters.
	 */
	protected static DomContent renderParameter(
		ISimulationParameters parameters, 
		Field parameter
	) {
		if(parameter.getAnnotation(Parameter.class) != null && parameter.getType().isPrimitive() ) {
			try {
				if(parameter.getType().equals(boolean.class)) {
					return div().withClass("form-check form-check-sm").with(
						label().withClass("form-check-label").with(
							input().withClass("form-check-input")
								   .withType("checkbox")
								   .withId(parameter.getName())
								   .attr("data-toggle", "popover")
								   .attr("data-trigger", "hover")
								   .attr("data-placement", "right")
								   .attr("data-content", parameter.getAnnotation(Parameter.class).description())
								   //.condAttr(parameters.getClass().getField(parameter.getName()).get(parameters).equals(true), "checked")
								   .attr("onclick", "updateBooleanParameter(\'"+parameter.getName()+"\')"),
							strong(parameter.getAnnotation(Parameter.class).name())
						)				
					);		
				} else {
					return div().withClass("form-group form-group-sm row").with(
						label().withClass("col-12 col-form-label")
							   .attr("for", parameter.getName())
							   .withText(parameter.getAnnotation(Parameter.class).name()),
						div().withClass("col-12").with(
							input().withClass("form-control bfh-number text-right")
								   .withType("number")
								   .withId(parameter.getName())
								   .attr("data-toggle", "popover")
								   .attr("data-trigger", "hover")
								   .attr("data-placement", "right")
								   .attr("data-content", parameter.getAnnotation(Parameter.class).description())
								   .condAttr(parameter.getType().equals(int.class), "step", "1")
								   .condAttr(!parameter.getType().equals(int.class), "step", "0.01")
								   .attr("size", "5")
								   .withValue(parameters.getClass().getField(parameter.getName()).get(parameters).toString())
								   .attr("onchange", "updateNumericParameter(\'"+parameter.getName()+"\')")
						)				
					);
				}
			} catch (
				 IllegalArgumentException
				   | IllegalAccessException
				   | NoSuchFieldException
				   | SecurityException e
			) {
				Log.getRootLogger().warn(
	        		"The parameter "+ parameter + " cannot be found", e
	        	);
			}
		}
		return text("");
	}

	/**
	 * Generates the HTML code of the body of the GUI.
	 * @return the body of the web GUI.
	 */
	public DomContent renderHtmlBody() {
		return this.htmlBody;
	}
}
