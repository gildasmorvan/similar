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
package fr.lgi2a.similar.microkernel.libs.tools.learning;

import java.io.PrintStream;
import java.util.List;

import fr.lgi2a.similar.microkernel.IAgent;
import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.libs.tools.learning.printer.LearningEngineOperationPrinter;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.AbstractLearningEngineOperationMoment;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.ILearningEngineOperation;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.LearningReasonOfSimulationEnd;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.LearningSimulationDynamicState;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.SimulationExecutionTrace;
import fr.lgi2a.similar.microkernel.states.IPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.states.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.states.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.states.dynamicstate.TransitoryPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.states.dynamicstate.map.IDynamicStateMap;

/**
 * Provides methods printing the content of the trace on screen.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class LearningTracePrinter {
	/**
	 * The object printing the trace of the simulation.
	 */
	public static final PrintStream PRINTER = System.out;
	
	/**
	 * Prints on the standard output the trace of a simulation.
	 * @param trace The trace to print on screen.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public static void printTrace( SimulationExecutionTrace trace ) throws IllegalArgumentException {
		if( trace == null ) {
			throw new IllegalArgumentException( "The 'trace' argument cannot be null." );
		}
		LearningTracePrinter.PRINTER.println( "=== Start of the trace " );
		SimulationTimeStamp initialTime = trace.getInitialTime();
		SimulationTimeStamp finialTime = trace.getFinalTime();
		printIndentation( 1 );
		LearningTracePrinter.PRINTER.println( "Initial time : " + initialTime );
		printIndentation( 1 );
		LearningTracePrinter.PRINTER.println( "Final time : " + finialTime );
		LearningTracePrinter.PRINTER.println( );
		LearningTracePrinter.PRINTER.println( "Initial dynamic state of the simulation : " );
		printDynamicState( 1, trace.getDynamicStateAt( initialTime ) );
		LearningTracePrinter.PRINTER.println( );
		for( SimulationTimeStamp timeStamp : trace.getOrderedSimulationTimeStamps() ){
			LearningTracePrinter.PRINTER.println( "===Information related to the time " + timeStamp );
			printIndentation( 1 );
			LearningTracePrinter.PRINTER.println( "=== Dynamic state of the simulation at the time " + timeStamp );
			printDynamicState( 2, trace.getDynamicStateAt( timeStamp ) );
			printIndentation( 1 );
			LearningTracePrinter.PRINTER.println( "=== Operations happening close to the time " + timeStamp );
			if( ! timeStamp.equals( initialTime ) ){
				printIndentation( 2 );
				LearningTracePrinter.PRINTER.println( "Slightly before:" );
				List<ILearningEngineOperation> operations = trace.getOperationsFor( new AbstractLearningEngineOperationMoment.Learning_EngineOperationMoment_Before( timeStamp ) );
				for( ILearningEngineOperation operation : operations ){
					LearningEngineOperationPrinter.printOperation( 3, operation );
				}
			}
			if( ! timeStamp.equals( finialTime ) ){
				printIndentation( 2 );
				LearningTracePrinter.PRINTER.println( "Slightly after:" );
				List<ILearningEngineOperation> operations = trace.getOperationsFor( new AbstractLearningEngineOperationMoment.Learning_EngineOperationMoment_After( timeStamp ) );
				for( ILearningEngineOperation operation : operations ){
					LearningEngineOperationPrinter.printOperation( 3, operation );
				}
			}
		}
		LearningTracePrinter.PRINTER.println( );
		if( trace.getReasonOfEnd().equals( LearningReasonOfSimulationEnd.END_CRITERION_REACHED ) ){
			LearningTracePrinter.PRINTER.println( "Final dynamic state of the simulation : " );
			printDynamicState( 1, trace.getFinalDynamicState( ) );
		} else if( trace.getReasonOfEnd().equals( LearningReasonOfSimulationEnd.ABORTED ) ) {
			LearningTracePrinter.PRINTER.println( "The simulation has ended because it was aborted." );
		} else {
			LearningReasonOfSimulationEnd.ExceptionCaught castedReason = (LearningReasonOfSimulationEnd.ExceptionCaught) trace.getReasonOfEnd();
			LearningTracePrinter.PRINTER.println( "The simulation has ended because of the following error:" );
			printIndentation( 1 );
			LearningTracePrinter.PRINTER.println( castedReason.getErrorMessage() );
			castedReason.getError().printStackTrace( LearningTracePrinter.PRINTER );
		}
	}
	
	/**
	 * Prints a specific number of indentation characters in the current line.
	 * @param indentation The number of indentation characters to print.
	 */
	public static void printIndentation( int indentation ) {
		for( int i = 0; i < indentation; i++ ){
			LearningTracePrinter.PRINTER.print( "\t" );
		}
	}
	
	/**
	 * Prints on the standard output the dynamic state of the simulation, using a specific indentation value.
	 * @param indentation The number of indentations used to print the dynamic state.
	 * @param dynamicState The dynamic state being printed.
	 */
	public static void printDynamicState( int indentation, LearningSimulationDynamicState dynamicState ) {
		printIndentation( indentation );
		LearningTracePrinter.PRINTER.println( "Memory state of the agents:" );
		for( IAgent agent : dynamicState.getAgents() ){
			printIndentation( indentation + 1 );
			LearningTracePrinter.PRINTER.println( "Agent '" + agent.getCategory() + "'" );
			printIndentation( indentation + 2 );
			LearningTracePrinter.PRINTER.println( dynamicState.getGlobalMemoryState( agent ) );
		}
		printIndentation( indentation );
		LearningTracePrinter.PRINTER.println( "Local dynamic state of the levels:" );
		IDynamicStateMap localDynamicStates = dynamicState.getLocalDynamicStates();
		for( LevelIdentifier levelId : localDynamicStates.keySet() ){
			printLocalDynamicState( indentation + 1, localDynamicStates.get( levelId ) );
		}
	}
	
	/**
	 * Prints the local dynamic state of the simulation, as described in the trace of the simulation.
	 * @param indentation The number of indentations used to print the local dynamic state.
	 * @param localDynamicState The local dynamic state to print.
	 */
	public static void printLocalDynamicState( int indentation, IPublicLocalDynamicState localDynamicState ) {
		printIndentation( indentation );
		LearningTracePrinter.PRINTER.println( "Local dynamic state of the level '" + localDynamicState.getLevel() + "':" );
		printIndentation( indentation + 1 );
		LearningTracePrinter.PRINTER.println( "Nature of the public local state of the level:" );
		printIndentation( indentation + 2 );
		if( localDynamicState instanceof ConsistentPublicLocalDynamicState ){
			LearningTracePrinter.PRINTER.println( "Consistent" );
		} else {
			LearningTracePrinter.PRINTER.println( "Transitory" );
		}
		printIndentation( indentation + 1 );
		LearningTracePrinter.PRINTER.println( "Last consistent time of the level:" );
		printIndentation( indentation + 2 );
		LearningTracePrinter.PRINTER.println( localDynamicState.getTime() );
		if( localDynamicState instanceof TransitoryPublicLocalDynamicState ) {
			TransitoryPublicLocalDynamicState casted = (TransitoryPublicLocalDynamicState) localDynamicState;
			printIndentation( indentation + 1 );
			LearningTracePrinter.PRINTER.println( "End of the transitory period:" );
			printIndentation( indentation + 2 );
			LearningTracePrinter.PRINTER.println( casted.getNextTime() );
		}
		printIndentation( indentation + 1 );
		LearningTracePrinter.PRINTER.println( "Public local state of the environment:" );
		printIndentation( indentation + 2 );
		LearningTracePrinter.PRINTER.println( localDynamicState.getPublicLocalStateOfEnvironment() );
		printIndentation( indentation + 2 );
		LearningTracePrinter.PRINTER.println( "Public local state of the agents:" );
		for( IPublicLocalStateOfAgent agentState : localDynamicState.getPublicLocalStateOfAgents() ){
			printIndentation( indentation + 3 );
			LearningTracePrinter.PRINTER.println( agentState );
		}
		printIndentation( indentation + 1 );
		LearningTracePrinter.PRINTER.println( "State dynamics:" );
		for( IInfluence influence : localDynamicState.getStateDynamics() ){
			printIndentation( indentation + 2 );
			LearningTracePrinter.PRINTER.println( influence );
		}
	}
}
