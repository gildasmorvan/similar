package fr.univ_artois.lgi2a.underpressure.model.agents.testsubject;

import fr.univ_artois.lgi2a.similar.microkernel.agents.IGlobalState;

/**
 * The global state of a 'Test subject' agent.
 */
public class AgtTestSubjectGS implements IGlobalState {
	/**
	 * Builds an initialized instance of the global state.
	 * @param owner The agent owning this global state.
	 * @param initialStressDegree The initial stress degree of the agent.
	 */
	public AgtTestSubjectGS(
		double initialStressDegree
	){
		this.stressDegree = initialStressDegree;
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
