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
package fr.lgi2a.similar.microKernel.libs.tools.learning;

import java.util.List;

import fr.lgi2a.similar.microKernel.I_Agent;
import fr.lgi2a.similar.microKernel.I_Influence;
import fr.lgi2a.similar.microKernel.LevelIdentifier;
import fr.lgi2a.similar.microKernel.SimulationTimeStamp;
import fr.lgi2a.similar.microKernel.libs.tools.learning.printer.Learning_EngineOperationPrinter;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.Learning_EngineOperation;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.Learning_EngineOperationMoment;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.Learning_SimulationDynamicState;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.SimulationExecutionTrace;
import fr.lgi2a.similar.microKernel.states.I_PublicLocalDynamicState;
import fr.lgi2a.similar.microKernel.states.I_PublicLocalStateOfAgent;
import fr.lgi2a.similar.microKernel.states.dynamicStates.Consistent_PublicLocalDynamicState;
import fr.lgi2a.similar.microKernel.states.dynamicStates.Transitory_PublicLocalDynamicState;
import fr.lgi2a.similar.microKernel.states.dynamicStates.map.I_DynamicState_Map;

/**
 * Provides methods printing the content of the trace on screen.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class Learning_TracePrinter {
	/**
	 * Prints on the standard output the trace of a simulation.
	 * @param trace The trace to print on screen.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public static void printTrace( SimulationExecutionTrace trace ) throws IllegalArgumentException {
		if( trace == null ) {
			throw new IllegalArgumentException( "The 'trace' argument cannot be null." );
		}
		System.out.println( "=== Start of the trace " );
		SimulationTimeStamp initialTime = trace.getInitialTime();
		SimulationTimeStamp finialTime = trace.getFinalTime();
		printIndentation( 1 );
		System.out.println( "Initial time : " + initialTime );
		printIndentation( 1 );
		System.out.println( "Final time : " + finialTime );
		System.out.println( );
		System.out.println( "Initial dynamic state of the simulation : " );
		printDynamicState( 1, trace.getDynamicStateAt( initialTime ) );
		System.out.println( );
		for( SimulationTimeStamp timeStamp : trace.getOrderedSimulationTimeStamps() ){
			System.out.println( "===Information related to the time " + timeStamp );
			printIndentation( 1 );
			System.out.println( "=== Dynamic state of the simulation at the time " + timeStamp );
			printDynamicState( 2, trace.getDynamicStateAt( timeStamp ) );
			printIndentation( 1 );
			System.out.println( "=== Operations happening close to the time " + timeStamp );
			if( ! timeStamp.equals( initialTime ) ){
				printIndentation( 2 );
				System.out.println( "Slightly before:" );
				List<Learning_EngineOperation> operations = trace.getOperationsFor( new Learning_EngineOperationMoment.Learning_EngineOperationMoment_Before( timeStamp ) );
				for( Learning_EngineOperation operation : operations ){
					Learning_EngineOperationPrinter.printOperation( 3, operation );
				}
			}
			if( ! timeStamp.equals( finialTime ) ){
				printIndentation( 2 );
				System.out.println( "Slightly after:" );
				List<Learning_EngineOperation> operations = trace.getOperationsFor( new Learning_EngineOperationMoment.Learning_EngineOperationMoment_After( timeStamp ) );
				for( Learning_EngineOperation operation : operations ){
					Learning_EngineOperationPrinter.printOperation( 3, operation );
				}
			}
		}
		System.out.println( );
		System.out.println( "Final dynamic state of the simulation : " );
		printDynamicState( 1, trace.getFinalDynamicState( ) );
	}
	
	/**
	 * Prints a specific number of indentation characters in the current line.
	 * @param indentation The number of indentation characters to print.
	 */
	public static void printIndentation( int indentation ) {
		for( int i = 0; i < indentation; i++ ){
			System.out.print( "\t" );
		}
	}
	
	/**
	 * Prints on the standard output the dynamic state of the simulation, using a specific indentation value.
	 * @param indentation The number of indentations used to print the dynamic state.
	 * @param dynamicState The dynamic state being printed.
	 */
	public static void printDynamicState( int indentation, Learning_SimulationDynamicState dynamicState ) {
		printIndentation( indentation );
		System.out.println( "Memory state of the agents:" );
		for( I_Agent agent : dynamicState.getAgents() ){
			printIndentation( indentation + 1 );
			System.out.println( "Agent '" + agent.getCategory() + "'" );
			printIndentation( indentation + 2 );
			System.out.println( dynamicState.getGlobalMemoryState( agent ) );
		}
		printIndentation( indentation );
		System.out.println( "Local dynamic state of the levels:" );
		I_DynamicState_Map localDynamicStates = dynamicState.getLocalDynamicStates();
		for( LevelIdentifier levelId : localDynamicStates.keySet() ){
			printLocalDynamicState( indentation + 1, localDynamicStates.get( levelId ) );
		}
	}
	
	/**
	 * Prints the local dynamic state of the simulation, as described in the trace of the simulation.
	 * @param indentation The number of indentations used to print the local dynamic state.
	 * @param localDynamicState The local dynamic state to print.
	 */
	public static void printLocalDynamicState( int indentation, I_PublicLocalDynamicState localDynamicState ) {
		printIndentation( indentation );
		System.out.println( "Local dynamic state of the level '" + localDynamicState.getLevel() + "':" );
		printIndentation( indentation + 1 );
		System.out.println( "Nature of the public local state of the level:" );
		printIndentation( indentation + 2 );
		if( localDynamicState instanceof Consistent_PublicLocalDynamicState ){
			System.out.println( "Consistent" );
		} else {
			System.out.println( "Transitory" );
		}
		printIndentation( indentation + 1 );
		System.out.println( "Last consistent time of the level:" );
		printIndentation( indentation + 2 );
		System.out.println( localDynamicState.getTime() );
		if( localDynamicState instanceof Transitory_PublicLocalDynamicState ) {
			Transitory_PublicLocalDynamicState casted = (Transitory_PublicLocalDynamicState) localDynamicState;
			printIndentation( indentation + 1 );
			System.out.println( "End of the transitory period:" );
			printIndentation( indentation + 2 );
			System.out.println( casted.getNextTime() );
		}
		printIndentation( indentation + 1 );
		System.out.println( "Public local state of the environment:" );
		printIndentation( indentation + 2 );
		System.out.println( localDynamicState.getPublicLocalStateOfEnvironment() );
		printIndentation( indentation + 2 );
		System.out.println( "Public local state of the agents:" );
		for( I_PublicLocalStateOfAgent agentState : localDynamicState.getPublicLocalStateOfAgents() ){
			printIndentation( indentation + 3 );
			System.out.println( agentState );
		}
		printIndentation( indentation + 1 );
		System.out.println( "State dynamics:" );
		for( I_Influence influence : localDynamicState.getStateDynamics() ){
			printIndentation( indentation + 2 );
			System.out.println( influence );
		}
	}
}
