package fr.lgi2a.wildlifesimulation.model.environment.savannah;

import java.util.HashSet;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IPublicLocalState;
import fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;

/**
 * The public local state of the environment in the "Savannah" level of the 
 * simulation.
 */
public class PLSEnvInSavannahLevel implements IPublicLocalState {
	/**
	 * Builds an initialized instance of this public local state.
	 * @param initialTemperature The initial ambient temperature in the level, defined in 
	 * Celsius degrees.
	 * @param initialHumidity The initial ambient humidity level in the "Savannah" level, 
	 * defined as a percentage between 0 and 100.
	 * @param gridWidth The width of the grid, expressed in meters relatively to an 
	 * origin point. This value has to be greater than 0.
	 * @param gridHeight The width of the grid, expressed in meters relatively to an 
	 * origin point. This value has to be greater than 0.
	 * @throws IllegalArgumentException If an argument has an invalid value.
	 */
	@SuppressWarnings("unchecked")
	public PLSEnvInSavannahLevel( 
		double initialTemperature,
		int initialHumidity,
		int gridWidth,
		int gridHeight
	){
		this.setAmbientTemperature( initialTemperature );
		this.setAmbientHumidity( initialHumidity );
		if( gridWidth <= 0 ){
			throw new IllegalArgumentException(
				"The width of the grid has to be at least equal to 1."
			);
		}
		this.gridWidth = gridWidth;
		if( gridHeight <= 0 ){
			throw new IllegalArgumentException(
				"The height of the grid has to be at least equal to 1."
			);
		}
		this.gridHeight = gridHeight;
		this.grid = new Set[ this.gridWidth ][];
		for( int x = 0; x < this.gridWidth; x++ ){
			this.grid[ x ] = new Set[ this.gridHeight ];
			for( int y = 0; y < this.gridHeight; y++ ){
				this.grid[ x ][ y ] = new HashSet<IPublicLocalStateOfAgent>();
			}
		}
	}

	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
	// // 
	// //   Generic methods of the public local state
	// //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LevelIdentifier getLevel() {
		return WildlifeLevelList.SAVANNAH;
	}

	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
	// // 
	// //   Declaration of the perceptible data of the environment
	// //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
	
	/**
	 * The ambient temperature in the "Savannah" level, defined in Celsius degrees.
	 */
	private double ambientTemperature;
	/**
	 * The ambient humidity level in the "Savannah" level, defined as a percentage 
	 * between 0 and 100.
	 */
	private int ambientHumidity;

	/**
	 * Gets the ambient temperature in the "Savannah" level, defined in Celsius 
	 * degrees.
	 * @return The ambient temperature in the "Savannah" level.
	 */
	public double getAmbientTemperature(){
		return this.ambientTemperature;
	}

	/**
	 * Sets the ambient temperature in the "Savannah" level.
	 * @param newAmbientTemperature The new ambient temperature in the level, defined in 
	 * Celsius degrees.
	 */
	public void setAmbientTemperature( double newAmbientTemperature ){
		this.ambientTemperature = newAmbientTemperature;
	}

	/**
	 * Gets the ambient humidity level in the "Savannah" level, defined as a percentage 
	 * between 0 and 100.
	 * @return The ambient humidity level in the "Savannah" level.
	 */
	public int getAmbientHumidity( ){
		return this.ambientHumidity;
	}

	/**
	 * The upper bound of the humidity level of the "Savannah".
	 */
	private static final int HUMIDITY_UPPER_BOUND = 100;
	/**
	 * The lower bound of the humidity level of the "Savannah".
	 */
	private static final int HUMIDITY_LOWER_BOUND = 0;

	/**
	 * Sets the ambient humidity level in the "Savannah" level.
	 * @param newAmbientHumidity The new ambient humidity level in the "Savannah" level, 
	 * defined as a percentage between 0 and 100.
	 */
	public void setAmbientHumidity( int newAmbientHumidity ){
		if( 
			newAmbientHumidity < HUMIDITY_LOWER_BOUND || 
			newAmbientHumidity > HUMIDITY_UPPER_BOUND 
		) {
			throw new IllegalArgumentException( 
				"The ambient humidity has to be within the bounds [" +
				HUMIDITY_LOWER_BOUND + ";" +	
				HUMIDITY_UPPER_BOUND + "]"
			);
		}
		this.ambientHumidity = newAmbientHumidity;
	}
	
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
	// // 
	// //   Declaration of the topology of the environment
	// //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //
	
	/**
	 * The width of the grid, expressed in meters relatively to an origin point.
	 */
	private int gridWidth;
	/**
	 * The height of the grid, expressed in meters relatively to an origin point.
	 */
	private int gridHeight;
	
	/**
	 * The grid providing a fast access to the public local state of the agents contained 
	 * in this level.
	 */
	private Set<IPublicLocalStateOfAgent>[][] grid;

	/**
	 * Gets the width of the grid, expressed in meters relatively to an origin point.
	 * @return The width of the grid.
	 */
	public int getGridWidth( ) {
		return this.gridWidth;
	}
	
	/**
	 * Gets the height of the grid, expressed in meters relatively to an origin point.
	 * @return The height of the grid.
	 */
	public int getGridHeight( ) {
		return this.gridHeight;
	}
	
	/**
	 * Checks if an x coordinate is valid for the grid of the topology.
	 * @param x The x coordinate of the agent, expressed as a distance in meters 
	 * from the origin of the grid. This value cannot be greater or equal to 
	 * <code>grid.width</code>.
	 * @return the line index in the grid where an agent having <code>x</code>
	 * as a coordinate can be found.
	 * @throws IllegalArgumentException If the value of <code>x</code> 
	 * is not appropriate. This exception is not thrown when
	 * the state of the agent already lies in the topology of the environment.
	 */
	private int checkXCoordinate( double x ) {
		int intX = (int) Math.floor( x );
		if( intX < 0 || intX > this.getGridWidth() ){
			throw new IllegalArgumentException(
				"An agent cannot have an x coordinate out " +
				"of the bounds [0;" + this.getGridWidth() + "]"
			);
		} else {
			return intX;
		}
	}
	
	/**
	 * Checks if an y coordinate is valid for the grid of the topology.
	 * @param y The y coordinate of the agent, expressed as a distance in meters 
	 * from the origin of the grid. This value cannot be greater or equal to 
	 * <code>grid.height</code>.
	 * @return the column index in the grid where an agent having <code>y</code>
	 * as a coordinate can be found.
	 * @throws IllegalArgumentException If the value of <code>y</code> 
	 * is not appropriate. This exception is not thrown when
	 * the state of the agent already lies in the topology of the environment.
	 */
	private int checkYCoordinate( double y ) {
		int intY = (int) Math.floor( y );
		if( intY < 0 || intY > this.getGridHeight() ){
			throw new IllegalArgumentException(
				"An agent cannot have an y coordinate out " +
				"of the bounds [0;" + this.getGridHeight() + "]"
			);
		} else {
			return intY;
		}
	}
	
	/**
	 * Adds an <code>agent</code> having the (<code>x</code>, 
	 * <code>y</code>) coordinates to the topology of the environment.
	 * <p>
	 * 	This method does not check if the agent already lies in the 
	 * 	environment topology.
	 * </p>
	 * @param agent The public local state of the agent to add. This state must
	 * not already be present in the topology of the environment.
	 * @param x The x coordinate of the agent, expressed as a distance in meters 
	 * from the origin of the grid. This value cannot be greater or equal to 
	 * <code>grid.width</code>.
	 * @param y The y coordinate of the agent, expressed as a distance in meters 
	 * from the origin of the grid. This value cannot be greater or equal to 
	 * <code>grid.height</code>.
	 * @throws IllegalArgumentException If the value of <code>x</code> or 
	 * <code>y</code> is not appropriate. This exception is not thrown when
	 * the state of the agent already lies in the topology of the environment.
	 */
	public void addAgent( IPublicLocalStateOfAgent agent, double x, double y ) {
		int intX = this.checkXCoordinate( x );
		int intY = this.checkYCoordinate( y );
		this.grid[ intX ][ intY ].add( agent );
	}
	
	/**
	 * Removes an <code>agent</code> having the (<code>x</code>, 
	 * <code>y</code>) coordinates from the topology of the environment.
	 * <p>
	 * 	This method does not check if the agent already lies in the 
	 * 	environment topology.
	 * </p>
	 * @param x The x coordinate of the agent, expressed as a distance in meters 
	 * from the origin of the grid. This value cannot be greater or equal to 
	 * <code>grid.width</code>.
	 * @param y The y coordinate of the agent, expressed as a distance in meters 
	 * from the origin of the grid. This value cannot be greater or equal to 
	 * <code>grid.height</code>.
	 * @throws IllegalArgumentException If the value of <code>x</code> or 
	 * <code>y</code> is not appropriate. This exception is not thrown when
	 * the state of the agent already lies in the topology of the environment.
	 */
	public void removeAgent( IPublicLocalStateOfAgent agent, double x, double y ) {
		int intX = this.checkXCoordinate( x );
		int intY = this.checkYCoordinate( y );
		this.grid[ intX ][ intY ].remove( agent );
	}
	
	/**
	 * Gets the agent located in a specific cell of the grid.
	 * @param x The x coordinate in the grid where to search for agents.
	 * This value has to be within the range [0, grid.width[
	 * @param y The y coordinate in the grid where to search for agents.
	 * This value has to be within the range [0, grid.height[
	 * @return The set of agents located at the specified position in the grid.
	 * @throws IllegalArgumentException If the value of <code>x</code> or 
	 * <code>y</code> is not appropriate.
	 */
	public Set<IPublicLocalStateOfAgent> getAgentsLocatedAt( int x, int y ){
		this.checkXCoordinate( x );
		this.checkXCoordinate( y );
		return this.grid[ x ][ y ];
	}
}
