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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;

/**
 * Models the execution trace of a simulation in SIMILAR.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class SimulationExecutionTrace {
	/**
	 * The trace of the dynamic states for each time stamp of the simulation.
	 */
	private Map<SimulationTimeStamp,Learning_SimulationDynamicState> dynamicStatesTrace;
	/**
	 * The order in which the simulation engine did perform operations.
	 */
	private Map<Learning_EngineOperationMoment, List<Learning_EngineOperation>> engineOperationsOrder;
	/**
	 * The operations performed by the simulation engine, sorted by type.
	 */
	private Map<Learning_EngineOperationMoment, Map<Learning_EngineOperationType, List<Learning_EngineOperation>>> engineOperationsByType;
	/**
	 * Models the reason why the simulation ended.
	 */
	private Learning_ReasonOfSimulationEnd reasonOfEnd;
	/**
	 * The final time stamp of the simulation (if the simulation ended without errors).
	 */
	private SimulationTimeStamp finalTime;
	/**
	 * The dynamic state of the simulation when the final time was reached.
	 */
	private Learning_SimulationDynamicState finalDynamicState;
	
	/**
	 * Builds an empty simulation execution trace.
	 */
	public SimulationExecutionTrace( ) {
		this.dynamicStatesTrace = new LinkedHashMap<SimulationTimeStamp, Learning_SimulationDynamicState>( );
		this.engineOperationsOrder = new TreeMap<Learning_EngineOperationMoment, List<Learning_EngineOperation>>();
		this.engineOperationsByType = new HashMap<Learning_EngineOperationMoment, Map<Learning_EngineOperationType, List<Learning_EngineOperation>>>();
	}
	
	/**
	 * Gets all the time stamps of the simulation, in increasing order.
	 * @return An ordered set containing all the time stamps of the simulation in increasing order.
	 */
	public Set<SimulationTimeStamp> getOrderedSimulationTimeStamps(){
		return this.dynamicStatesTrace.keySet();
	}
	
	/**
	 * Gets the dynamic state of the simulation at a specific time stamp of the simulation.
	 * @param timestamp The time stamp for which the dynamic state of the simulation is retrieved.
	 * @return The dynamic state of the simulation at that time.
	 * @throws IllegalArgumentException If the argument is <code>null</code>.
	 * @throws NoSuchElementException If no dynamic state is defined for the provided time stamp.
	 */
	public Learning_SimulationDynamicState getDynamicStateAt( 
			SimulationTimeStamp timestamp 
	) throws IllegalArgumentException, NoSuchElementException {
		if( timestamp == null ){
			throw new IllegalArgumentException( "The 'timestamp' argument cannot be null." );
		} else if( ! this.dynamicStatesTrace.containsKey( timestamp ) ) {
			throw new NoSuchElementException( "No half-consistent dynamic state was defined for the time stamp '" + timestamp + "'." );
		}
		return this.dynamicStatesTrace.get( timestamp );
	}
	
	/**
	 * Registers the dynamic state of the simulation at a specific time stamp to this trace.
	 * @param timestamp The time stamp of the dynamic state of the simulation.
	 * @param dynamicState The dynamic state of the simulation at that time.
	 * @throws IllegalArgumentException If an argument is <code>null</code>, or if a dynamic 
	 * state is already defined for that time.
	 */
	public void addDynamicState(
			SimulationTimeStamp timestamp,
			Learning_SimulationDynamicState dynamicState
	) throws IllegalArgumentException {
		if( timestamp == null ){
			throw new IllegalArgumentException( "The 'timestamp' argument cannot be null." );
		} else if( dynamicState == null ){
			throw new IllegalArgumentException( "The 'dynamicState' argument cannot be null." );
		} else if( this.dynamicStatesTrace.containsKey( timestamp ) ){
			throw new IllegalArgumentException( "A dynamic state is already defined for the timestamp '" + timestamp + "'." );
		}
		this.dynamicStatesTrace.put( new SimulationTimeStamp( timestamp ), dynamicState );
	}

	/**
	 * Gets the dynamic state of the simulation when the final time was reached.
	 * @return The dynamic state of the simulation when the final time was reached.
	 */
	public Learning_SimulationDynamicState getFinalDynamicState( ) {
		return this.finalDynamicState;
	}

	/**
	 * Sets the dynamic state of the simulation when the final time was reached.
	 * @param finalDynamicState The dynamic state of the simulation when the final time was reached.
	 * @throws IllegalArgumentException If an argument was <code>null</code>.
	 */
	public void setFinalDynamicState( SimulationTimeStamp finalTime, Learning_SimulationDynamicState finalDynamicState ) throws IllegalArgumentException {
		if( finalDynamicState == null ){
			throw new IllegalArgumentException( "The 'finalDynamicState' argument cannot be null." );
		} else if( finalTime == null ) {
			throw new IllegalArgumentException( "The 'finalTime' argument cannot be null." );
		}
		this.finalDynamicState = finalDynamicState;
		this.finalTime = finalTime;
	}
	
	/**
	 * Gets the final time stamp of the simulation (if the simulation ended without errors).
	 * @return The final time stamp of the simulation (if the simulation ended without errors).
	 */
	public SimulationTimeStamp getInitialTime( ) {
		if( this.getOrderedSimulationTimeStamps().isEmpty() ){
			return null;
		}
		return this.getOrderedSimulationTimeStamps().iterator().next();
	}
	
	/**
	 * Gets the final time stamp of the simulation (if the simulation ended without errors).
	 * @return The final time stamp of the simulation (if the simulation ended without errors).
	 */
	public SimulationTimeStamp getFinalTime( ) {
		return this.finalTime;
	}
	
	/**
	 * Gets the ordered set of moments when the simulation engine performed operations.
	 * @return The ordered set of moments when the simulation engine performed operations.
	 */
	public Set<Learning_EngineOperationMoment> getOperationsMoments(){
		return this.engineOperationsOrder.keySet();
	}
	
	/**
	 * Gets the list of operations that were performed by the simulation engine at a specific moment of the simulation.
	 * @param moment The moment of the simulation where the operations were performed.
	 * @return The list of operations that were performed by the simulation, in their execution order (or approximate execution 
	 * order if the simulation engine uses threads).
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 * @throws NoSuchElementException If no operation was performed by the simulation engine at that moment.
	 */
	public List<Learning_EngineOperation> getOperationsFor( 
			Learning_EngineOperationMoment moment 
	) throws IllegalArgumentException, NoSuchElementException {
		if( moment == null ){
			throw new IllegalArgumentException( "The 'moment' argument cannot be null." );
		} else if( ! this.engineOperationsOrder.containsKey( moment ) ) {
			throw new NoSuchElementException( "No operation was performed by the simulation engine at the moment '" + moment + "'." );
		}
		return this.engineOperationsOrder.get( moment );
	}
	
	/**
	 * Gets the list of operations of a specific type that were performed by the simulation engine at a specific moment of the simulation.
	 * @param moment The moment of the simulation where the operations were performed.
	 * @return The list of operations of that type that were performed by the simulation, in their execution order (or approximate execution 
	 * order if the simulation engine uses threads).
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 * @throws NoSuchElementException If no operation of that type was performed by the simulation engine at that moment.
	 */
	public List<Learning_EngineOperation> getOperationsFor( 
			Learning_EngineOperationMoment moment,
			Learning_EngineOperationType type
	) throws IllegalArgumentException, NoSuchElementException {
		if( moment == null ){
			throw new IllegalArgumentException( "The 'moment' argument cannot be null." );
		} else if( type == null ){
				throw new IllegalArgumentException( "The 'type' argument cannot be null." );
		} else if( ! this.dynamicStatesTrace.containsKey( moment ) ) {
			throw new NoSuchElementException( "No operation was performed by the simulation engine at the moment '" + moment + "'." );
		}
		List<Learning_EngineOperation> result = this.engineOperationsByType.get( moment ).get( type );
		if( result == null ){
			throw new NoSuchElementException( 
					"No operation of the type '" + type + "' was performed by the simulation engine at the moment '" + moment + "'." 
			);
		}
		return this.engineOperationsOrder.get( moment );
	}
	
	/**
	 * Adds an engine operation to this trace.
	 * @param moment The moment when this operation occurred.
	 * @param operation The operation to add to this trace.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public void addEngineOperation(
			Learning_EngineOperationMoment moment,
			Learning_EngineOperation operation
	) throws IllegalArgumentException {
		if( moment == null ){
			throw new IllegalArgumentException( "The 'moment' argument cannot be null." );
		} else if( operation == null ){
			throw new IllegalArgumentException( "The 'operation' argument cannot be null." );
		}
		List<Learning_EngineOperation> operations = this.engineOperationsOrder.get( moment );
		if( operations == null ){
			operations = new LinkedList<Learning_EngineOperation>();
			this.engineOperationsOrder.put( moment, operations );
		}
		operations.add( operation );
		Map<Learning_EngineOperationType, List<Learning_EngineOperation>> operationsByType = this.engineOperationsByType.get( moment );
		if( operationsByType == null ){
			operationsByType = new HashMap<Learning_EngineOperationType, List<Learning_EngineOperation>>();
			this.engineOperationsByType.put( moment, operationsByType );
		}
		operations = operationsByType.get( operation.getOperationType() );
		if( operations == null ){
			operations = new LinkedList<Learning_EngineOperation>();
			operationsByType.put( operation.getOperationType(), operations );
		}
		operations.add( operation );
	}
	
	/**
	 * Gets the reason why the simulation ended.
	 * @return The reason why the simulation ended.
	 */
	public Learning_ReasonOfSimulationEnd getReasonOfEnd( ) {
		return this.reasonOfEnd;
	}
	
	/**
	 * Sets the reason why the simulation ended.
	 * @param reasonOfEnd The reason why the simulation ended.
	 * @throws IllegalArgumentException If the argument is <code>null</code>.
	 */
	public void setReasonOfEnd( Learning_ReasonOfSimulationEnd reasonOfEnd ) throws IllegalArgumentException {
		if( reasonOfEnd == null ){
			throw new IllegalArgumentException( "The argument 'reasonOfEnd' cannot be null." );
		}
		this.reasonOfEnd = reasonOfEnd;
	}
	
	/**
	 * Clears the content of the trace.
	 */
	public void clearTrace( ) {
		this.dynamicStatesTrace.clear();
		this.engineOperationsByType.clear();
		this.engineOperationsOrder.clear();
		this.reasonOfEnd = null;
		this.finalDynamicState = null;
	}
}