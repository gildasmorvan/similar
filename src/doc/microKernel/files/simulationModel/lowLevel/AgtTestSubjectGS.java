package fr.lgi2a.underpressure.model.agents.testsubject;

import fr.lgi2a.similar.microkernel.IAgent;
import fr.lgi2a.similar.microkernel.IGlobalMemoryState;

/**
 * The global state of a 'Test subject' agent, implemented using the 
*  micro-kernel only.
 */
public class AgtTestSubjectGS implements IGlobalMemoryState {
	/**
	 * Builds an initialized instance of the global state.
	 * @param owner The agent owning this global state.
	 * @param initialStressDegree The initial stress degree of the agent.
	 */
	public AgtTestSubjectGS(
		IAgent owner,
		double initialStressDegree
	){
		if( owner == null ){
			throw new IllegalArgumentException(
				"The owner of the state cannot be null."
			);
		}
		this.owner = owner;
		this.stressDegree = initialStressDegree;
	}

	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  // 
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
	// // 
	// //   Generic methods of the global state
	// //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
	
	/**
	 * The owner of this global state.
	 */
	private final IAgent owner;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IAgent getOwner() {
		return this.owner;
	}

	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  // 
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
	// // 
	// //   Agent-specific methods of the global state
	// //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
	// //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //
	
	/**
	 * The stress degree of the agent.
	 */
	private double stressDegree;
	
	/**
	 * Gets the current stress degree of the agent.
	 * @return The current stress degree of the agent.
	 */
	public double getStressDegree( ){
		return this.stressDegree;
	}
	
	/**
	 * Sets the stress degree of the agent to a new value.
	 * @param stressDegree The new stress degree of the agent.
	 */
	public void setStressDegree( double stressDegree ) {
		this.stressDegree = stressDegree;
	}
}
