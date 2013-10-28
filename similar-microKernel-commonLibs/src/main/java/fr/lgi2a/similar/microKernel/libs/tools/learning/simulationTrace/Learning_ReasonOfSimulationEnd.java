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
package fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace;

/**
 * Models the reason why a simulation has ended.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class Learning_ReasonOfSimulationEnd {
	/**
	 * Models the case where the simulation has ended because it reached its final time.
	 */
	public static final Learning_ReasonOfSimulationEnd END_CRITERION_REACHED = new Learning_ReasonOfSimulationEnd();
	/**
	 * Models the case where the simulation has ended because it was aborted.
	 */
	public static final Learning_ReasonOfSimulationEnd ABORTED = new Learning_ReasonOfSimulationEnd();
	
	/**
	 * The private constructor of this object.
	 */
	private Learning_ReasonOfSimulationEnd( ) { }
	
	/**
	 * Models the case where the simulation ended because of an error.
	 * 
	 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
	 */
	public static final class ExceptionCaught extends Learning_ReasonOfSimulationEnd {
		/**
		 * The error message describing why the simulation failed.
		 */
		private String errorMessage;
		/**
		 * The error that caused the simulation to fail.
		 */
		private Throwable error;
		
		/**
		 * Builds the model of the reason why a simulation has ended when it ended because of an error.
		 * @param errorMessage The error message describing why the simulation failed.
		 * @param error The error that caused the simulation to fail.
		 * @throws IllegalArgumentException If an argument is <code>null</code>.
		 */
		public ExceptionCaught( String errorMessage, Throwable error ) throws IllegalArgumentException {
			if( errorMessage == null ){
				throw new IllegalArgumentException( "The argument 'errorMessage' cannot be null." );
			}
			if( error == null ){
				throw new IllegalArgumentException( "The argument 'error' cannot be null." );
			}
			this.error = error;
			this.errorMessage = errorMessage;
		}
		
		/**
		 * Gets the error message describing why the simulation failed.
		 * @return The error message describing why the simulation failed.
		 */
		public String getErrorMessage( ) {
			return this.errorMessage;
		}
		
		/**
		 * Gets the error that caused the simulation to fail.
		 * @return The error that caused the simulation to fail.
		 */
		public Throwable getError( ) {
			return this.error;
		}
	}
}
