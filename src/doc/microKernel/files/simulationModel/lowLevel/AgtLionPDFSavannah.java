package fr.lgi2a.wildlifesimulation.model.agents.lion.savannah;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IPerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;

/**
 * Models the data being perceived by a "lion" agent from the "Savannah" level.
 */
public class AgtLionPDFSavannah implements IPerceivedDataOfAgent {
	/**
	 * Builds an initialized instance of these perceived data.
	 */
	public AgtLionPDFSavannah( ) {
		this.level = WildlifeLevelList.SAVANNAH;
		this.nearbyPreys = new HashSet<>();
		this.vulturesLocation = new HashSet<>();
	}

	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  // 
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
	// // 
	// //   Generic methods of the perceived data
	// //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //

	
	/**
	 * The level from which the perception of these data was made.
	 */
	private LevelIdentifier level;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LevelIdentifier getLevel() {
		return this.level;
	}

	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
	// // 
	// //   Declaration of the perceived data of the agent
	// //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
	
	/**
	 * The set containing the nearby preys.
	 */
	private Set<IPublicLocalStateOfAgent> nearbyPreys;
	
	/**
	 * Gets a new iterator over the set of nearby preys.
	 * @return A new iterator over the set of nearby preys.
	 */
	public Iterator<IPublicLocalStateOfAgent> nearbyPreysIterator( ) {
		return this.nearbyPreys.iterator();
	}
	
	/**
	 * Adds a prey to the perceived nearby preys.
	 * @param prey The prey to add to the set of perceived nearby preys.
	 * @throws IllegalArgumentException If an argument had an 
	 * inappropriate value.
	 */
	public void addNearbyPrey( IPublicLocalStateOfAgent prey ) {
		if( prey == null ){
			throw new IllegalArgumentException(
				"The prey cannot be null."
			);
		}
		this.nearbyPreys.add( prey );
	}
	
	/**
	 * The location of the perceived vultures from the "Sky" level.
	 */
	private Set<Point2D> vulturesLocation;
	
	/**
	 * Gets a new iterator over the set of perceived vultures from the 
	 * "Sky" level.
	 * @return A new iterator over the set of perceived vultures from the 
	 * "Sky" level.
	 */
	public Iterator<Point2D> vulturesLocationIterator( ) {
		return this.vulturesLocation.iterator();
	}
	
	/**
	 * Adds the location of a vulture to the perceived vultures from the 
	 * "Sky" level.
	 * @param location The added location of a vulture.
	 * @throws IllegalArgumentException If an argument had an 
	 * inappropriate value.
	 */
	public void addVultureLocation( Point2D location ) {
		if( location == null ){
			throw new IllegalArgumentException(
				"The location cannot be null."
			);
		}
		this.vulturesLocation.add( location );
	}
}
