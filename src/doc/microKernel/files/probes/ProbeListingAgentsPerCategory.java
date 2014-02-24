package fr.lgi2a.similar.customprobes;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import fr.lgi2a.similar.microkernel.AgentCategory;
import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;

/**
 * This probe creates a file describing the evolution of the population of
 * specific agent categories over time in the simulation, from a specific level.
 * <p>
 * 	Each line of the produced file describes the population of the agent categories
 * 	at a specific time.
 * 	This line is the concatenation of the following elements, separated by a 
 * 	separator character:
 * </p>
 * <ol>
 * 	<li>The identifier of the time stamp when the observation was made;</li>
 * 	<li>The number of instances of the first agent category;</li>
 * 	<li>The number of instances of the second agent category;</li>
 * 	<li>...</li>
 * </ol>
 */
public class ProbeListingAgentsPerCategory implements IProbe {
	/**
	 * The string separating each column displaying the number of agents.
	 */
	private final String separator;
	
	/**
	 * The identifier of the level where the number of agents is
	 * tracked.
	 */
	private LevelIdentifier levelId;
	/**
	 * An array containing the agent categories for which the number 
	 * of agents is tracked.
	 */
	private AgentCategory[] agentCategories;
	/**
	 * The stream where the evolution of the populations is written.
	 */
	private PrintStream stream;
	/**
	 * The radical of the file name used to generate the resulting file.
	 */
	private String fileName;
	
	/**
	 * Builds a probe displaying the evolution of the population of agents from
	 * specific categories for a specific level of the simulation.
	 * @param separator The string separating each column of the resulting file.
	 * @param levelId The identifier of the level for which the populations are 
	  * tracked.
	 * @param agentCategories The category of the agents which population is 
	 * tracked.
	 * @param fileName The radical of the file name used to generate the resulting 
	* file. 
	 * The file where the results are printed is named "fileName_date.dat" where 
	 * date models the time when the file was created.
	 */
	public ProbeListingAgentsPerCategory(
		String separator,
		LevelIdentifier levelId,
		AgentCategory[] agentCategories,
		String fileName
	){
		if(
			separator == null || levelId == null || 
			agentCategories == null || stream == null
		){
			throw new IllegalArgumentException( 
				"The arguments cannot be null." 
			);
		} else if( agentCategories.length == 0 ){
			throw new IllegalArgumentException( 
				"There must be at least one agent category." 
			);
		}
		
		this.separator = separator;
		this.levelId = levelId;
		this.agentCategories = new AgentCategory[agentCategories.length];
		for( int index = 0; index < agentCategories.length; index++ ){
			this.agentCategories[index] = agentCategories[index];
		}
		this.fileName = fileName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void prepareObservation() {
		// Create the file where the results are printed, and create a
		// print stream towards it.
		DateFormat dateFormat = new SimpleDateFormat( 
			"YYYYMMddHHmmssSSS" 
		);
		String resultFileName = this.fileName + 
						dateFormat.format( new Date() ) + 
						".dat";
		try {
			this.stream = new PrintStream( resultFileName );
		} catch ( FileNotFoundException e ) {
			throw new RuntimeException(
				"Cannot create the log file.", 
				e
			);
		}
		// Write the header of the file.
		String line = "# Time";
		for( AgentCategory category : this.agentCategories ){
			line += this.separator + category;
		}
		this.stream.println( line );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtInitialTimes(
		SimulationTimeStamp initialTimestamp,
		ISimulationEngine simulationEngine
	) {	
		this.writeLine(initialTimestamp, simulationEngine);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtPartialConsistentTime(
		SimulationTimeStamp timestamp,
		ISimulationEngine simulationEngine
	) {
		this.writeLine(timestamp, simulationEngine);
	}
	
	/**
	 * Writes a content line of the output file.
	 * @param timestamp The time stamp when the observation is made.
	 * @param simulationEngine The simulation engine where the simulation runs.
	 */
	private void writeLine(
		SimulationTimeStamp timestamp,
		ISimulationEngine simulationEngine
	){
		Map<AgentCategory,Integer> populations = 
				new LinkedHashMap<AgentCategory,Integer>( );
		// Fill the populations data structure.
		for( IAgent4Engine agent : simulationEngine.getAgents( this.levelId ) ){
			Integer agtNum = populations.get( agent.getCategory() );
			if( agtNum == null ){
				agtNum = 0;
			}
			agtNum++;
			populations.put( agent.getCategory(), agtNum );
		}
		// Display the populations in the file.
		String line = Long.toString( timestamp.getIdentifier() );
		for( AgentCategory category : this.agentCategories ){
			Integer agtPop = populations.get( category );
			if( agtPop == null ){
				agtPop = 0;
			}
			line += this.separator + agtPop;
		}
		this.stream.println( line );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtFinalTime(
		SimulationTimeStamp finalTimestamp,
		ISimulationEngine simulationEngine
	) {
		// Does nothing since the populations at that time
		// are already written by the last call to the 
		// observeAtPartialConsistentTime(...) method.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToError(
		String errorMessage, 
		Throwable cause
	) {
		// Does nothing in this context.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reactToAbortion(
		SimulationTimeStamp timestamp,
		ISimulationEngine simulationEngine
	) {
		// Does nothing in this context.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void endObservation() {
		// Close the file where the data are written.
		this.stream.flush();
		this.stream.close();
	}
}
