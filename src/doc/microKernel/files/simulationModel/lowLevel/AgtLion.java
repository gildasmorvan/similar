package fr.lgi2a.wildlifesimulation.model.agents.lion;

import java.awt.geom.Point2D;
import java.util.Map;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IDynamicStateMap;
import fr.lgi2a.similar.microkernel.IGlobalMemoryState;
import fr.lgi2a.similar.microkernel.IPerceivedDataOfAgent;
import fr.lgi2a.similar.microkernel.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.InfluencesMap;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractAgent;
import fr.lgi2a.wildlifesimulation.model.agents.WildlifeAgentCategoriesList;
import fr.lgi2a.wildlifesimulation.model.agents.gazelle.savannah.AgtGazellePLSInSavannahLevel;
import fr.lgi2a.wildlifesimulation.model.agents.lion.savannah.AgtLionPDFSavannah;
import fr.lgi2a.wildlifesimulation.model.agents.lion.savannah.AgtLionPLSInSavannahLevel;
import fr.lgi2a.wildlifesimulation.model.environment.savannah.PLSEnvInSavannahLevel;
import fr.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;

/**
 * Models an agent from the 'Lion' category.
 */
public class AgtLion extends AbstractAgent {
	/**
	 * Builds a "Lion" agent without initializing the global state and the public
	 * local state of the agent.
	 * The setter of these elements have to be called manually.
	 * @param gazellePerceptionRadius The perception radius (in meters) of the 
	 * young gazelle agents.
	 * @param gazelleAgeThreshold The threshold under which a lion considers that a gazelle 
	 * is young enough to be preyed on.
	 * @throws IllegalArgumentException If the <code>gazellePerceptionRadius</code>
	 * argument is negative.
	 */
	public AgtLion( 
			int gazellePerceptionRadius,
			long gazelleAgeThreshold
	) {
		super( WildlifeAgentCategoriesList.LION );
		if( gazellePerceptionRadius < 0 ){
			throw new IllegalArgumentException(
				"The perception radius of Gazelles cannot be negative."
			);
		}
		this.gazellePerceptionRadius = gazellePerceptionRadius;
		this.gazelleAgeThreshold = gazelleAgeThreshold;
	}
	
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // // 
   // //   Private local state of the agent. 
   // //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
	
	private final int gazellePerceptionRadius;
	
	private final long gazelleAgeThreshold;
	
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // // 
   // //   Perception related method of the agent. 
   // //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPerceivedDataOfAgent perceive(
			LevelIdentifier level,
			IPublicLocalStateOfAgent publicLocalStateInLevel,
			IDynamicStateMap levelsPublicLocalObservableDynamicState
	) {
		if( level.equals( WildlifeLevelList.SAVANNAH ) ){
			return this.perceiveFromSavannah(
					level, 
					publicLocalStateInLevel, 
					levelsPublicLocalObservableDynamicState
			);
		} else {
			throw new IllegalStateException(
				"The 'Lion' agetn cannot perceive from the level '" + level + "'"
			);
		}
	}
	
	/**
	 * Models the perception process preceding a decision from the Savannah level.
	 */
	private AgtLionPDFSavannah perceiveFromSavannah(
			LevelIdentifier level,
			IPublicLocalStateOfAgent publicLocalStateInLevel,
			IDynamicStateMap levelsPublicLocalObservableDynamicState
	) {
		// Create an empty perceived data model.
		AgtLionPDFSavannah perceivedData = new AgtLionPDFSavannah( );
		// First get the coordinates of the lion agent.
		AgtLionPLSInSavannahLevel lionStateInSavannah = (AgtLionPLSInSavannahLevel) publicLocalStateInLevel;
		Point2D.Double lionCoordinates = lionStateInSavannah.getCoordinates();
		// Find the nearby young Gazelles.
		int x = (int) Math.floor( lionCoordinates.getX() );
		int y = (int) Math.floor( lionCoordinates.getY() );
		PLSEnvInSavannahLevel envStateInSavannah = (PLSEnvInSavannahLevel)
				levelsPublicLocalObservableDynamicState.get( WildlifeLevelList.SAVANNAH );
		for( 
				int dx = x - this.gazellePerceptionRadius / 2; 
				dx <= x + this.gazellePerceptionRadius / 2; 
				dx++ 
		){
			for( 
					int dy = y - this.gazellePerceptionRadius / 2; 
					dy <= y + this.gazellePerceptionRadius / 2; 
					dy++ 
			){
				// Get the agents located in the specified cell of the grid.
				try{
					Set<IPublicLocalStateOfAgent> agentsInCell = 
							envStateInSavannah.getAgentsLocatedAt(dx, dy);
					for( IPublicLocalStateOfAgent agtState : agentsInCell ){
						// Check if the agent is a gazelle
						if( agtState.getOwner().getCategory().equals( WildlifeAgentCategoriesList.GAZELLE ) ){
							// Check the age of the gazelle
							AgtGazellePLSInSavannahLevel gazelleState = (AgtGazellePLSInSavannahLevel) agtState;
							if( gazelleState.getAge() < this.gazelleAgeThreshold ){
								perceivedData.addNearbyPrey( gazelleState );
							}
						}
					}
				} catch( IllegalArgumentException e ) {
					// Case where the cell is not in the grid: do nothing.
				}
			}
			
		}
		return perceivedData;
	}

	   
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // // 
   // //   The remaining methods are managed in a later step of the simulation 
   // //   design process. 
   // //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //  //
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void reviseMemory(
			Map<LevelIdentifier, 
			IPerceivedDataOfAgent> perceivedData,
			IGlobalMemoryState memoryState
	) {
	      throw new UnsupportedOperationException( 
	              "This operation currently has no specification." 
	        );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decide(LevelIdentifier level, 
			IGlobalMemoryState memoryState,
			IPerceivedDataOfAgent perceivedData,
			InfluencesMap producedInfluences
	) {
	      throw new UnsupportedOperationException( 
	              "This operation currently has no specification." 
	        );
	}
}
