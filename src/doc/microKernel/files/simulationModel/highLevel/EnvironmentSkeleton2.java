package fr.lgi2a.mysimulation.model.environment;

import fr.lgi2a.similar.microkernel.IDynamicStateMap;
import fr.lgi2a.similar.microkernel.IPublicLocalState;
import fr.lgi2a.similar.microkernel.InfluencesMap;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractEnvironment;

/**
 * The model of the environment in the simulation.
 */
public class EnvironmentSkeleton2 extends AbstractEnvironment {	
	/**
	 * Builds an environment for a simulation containing no levels.
	 * Levels are then added using the 
	 * {@link EnvironmentSkeleton2#includeNewLevel(LevelIdentifier, IPublicLocalState)} 
	 * method.
	 */
	public EnvironmentSkeleton2( ) {
		
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
