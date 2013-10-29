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
package fr.lgi2a.similar.microkernel.libs.tools.learning.printer;

import static fr.lgi2a.similar.microkernel.libs.tools.learning.LearningTracePrinter.*;

import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.ILearningEngineOperation;

/**
 * Provides a method printing the description of an operation of the simulation engine performed during the execution of a simulation.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public abstract class LearningEngineOperationPrinter {
	/**
	 * The chain of responsibility displaying any operation performed by a simulation engine during the execution of a simulation.
	 */
	private static final LearningEngineOperationPrinter[] CHAIN_OF_RESPONSIBILITY = new LearningEngineOperationPrinter[]{
		new LearningPerceptionOperationPrinter(),
		new LearningMemoryRevisionOperationPrinter(),
		new LearningDecisionOperationPrinter(),
		new LearningNaturalOperationPrinter(),
		new LearningSystemReactionOperationPrinter(),
		new LearningRegularReactionOperationPrinter(),
		new DefaultPrinter( )
	};
	
	/**
	 * Prints on the standard output the description of an operation of the simulation engine performed 
	 * during the execution of a simulation, using a specific indentation value.
	 * @param indentation The number of indentations used to print the dynamic state.
	 * @param operation The operation being printed.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public static void printOperation( int indentation, ILearningEngineOperation operation ) throws IllegalArgumentException {
		if( operation == null ){
			throw new IllegalArgumentException( "The 'operation' argument cannot be null." );
		}
		boolean printed = false;
		for( int index = 0; !printed && index < CHAIN_OF_RESPONSIBILITY.length; index++ ){
			LearningEngineOperationPrinter printer = CHAIN_OF_RESPONSIBILITY[ index ];
			if( printer.canPrint( operation ) ){
				printed = true;
				printer.print( indentation, operation );
			}
		}
	}
	
	/**
	 * The accept criterion of the "chain of responsibility" design pattern.
	 * It tells whether if this instance is capable of printing the operation or not.
	 * @param operation The operation to print.
	 * @return <code>true</code> if this instance is capable of printing the operation.
	 */
	public abstract boolean canPrint( ILearningEngineOperation operation );
	
	/**
	 * Prints on the standard output the description of the operation, using a specific indentation value.
	 * @param indentation The number of indentations used to print the dynamic state.
	 * @param operation The operation being printed.
	 */
	public abstract void print( int indentation, ILearningEngineOperation operation );
	
	/**
	 * The printer used if no other printer could display an operation of the simulation engine.
	 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
	 */
	private static class DefaultPrinter extends LearningEngineOperationPrinter {
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean canPrint(ILearningEngineOperation operation) {
			return true;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void print( int indentation, ILearningEngineOperation operation ) {
			printIndentation( indentation );
			System.out.println( "Unknown operation (type " + operation.getOperationType() + ")" );
		}
	}
}
