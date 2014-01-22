package fr.lgi2a.mysimulation.model.environment;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import fr.lgi2a.similar.microkernel.IDynamicStateMap;
import fr.lgi2a.similar.microkernel.IEnvironment;
import fr.lgi2a.similar.microkernel.IPublicLocalState;
import fr.lgi2a.similar.microkernel.InfluencesMap;
import fr.lgi2a.similar.microkernel.LevelIdentifier;

/**
 * The model of the environment in the simulation.
 */
public class EnvironmentSkeleton implements IEnvironment {
	/**
	 * The public local states of the environment in the various levels of the 
	 * simulation.
	 */
	private Map<LevelIdentifier,IPublicLocalState> publicLocalStates;
	
	/**
	 * Builds an environment for a simulation containing no levels.
	 * Levels are then added using the 
	 * {@link EnvironmentSkeleton#includeNewLevel(LevelIdentifier, IPublicLocalState)} 
	 * method.
	 */
	public EnvironmentSkeleton( ) {
		this.publicLocalStates = new HashMap<LevelIdentifier,IPublicLocalState>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IPublicLocalState getPublicLocalState( 
		LevelIdentifier level 
	) throws NoSuchElementException {
		IPublicLocalState result = this.publicLocalStates.get( level );
		if( result == null ){
			throw new NoSuchElementException( 
				"No public local state is defined in the environment " +
				"for the level '" +	level + "'." 
			);
		}
		return result;
	}
	
	/**
	 * Introduces the level-related data of the environment for a new level.
	 * @param level The level for which data are added.
	 * @param publicLocalState The public local state of the environment for 
	 * that level.
	 * @throws IllegalArgumentException If an argument is <code>null</code>, 
	 * or if the level is already present in the environment.
	 */
	public void includeNewLevel( 
		LevelIdentifier level, 
		IPublicLocalState publicLocalState 
	){
		if( level == null || publicLocalState == null){
			throw new IllegalArgumentException( "An argument is null." );
		} else if( this.publicLocalStates.containsKey( level ) ){
			throw new IllegalArgumentException( 
				"The level '" + level + "' is already defined " + 
				"for this environment." 
			);
		}
		this.publicLocalStates.put( level, publicLocalState );
	}
	
	
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
	// //							           //  //
	// //	The remaining methods are managed in a later step of the   //  //
	// //   simulation design process. 				   //  //
	// //							           //  //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void natural(LevelIdentifier level,
			IDynamicStateMap levelsPublicLocalObservableDynamicState,
			InfluencesMap producedInfluences
	) {
		throw new UnsupportedOperationException( 
			"This operation currently has no specification." 
		);
	}
}
