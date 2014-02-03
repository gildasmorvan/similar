package fr.lgi2a.underpressure.model.agents.testsubject;

import fr.lgi2a.similar.microkernel.IAgent;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.*;

/**
 * The global state of a 'Test subject' agent, implemented using the 
 * micro-kernel and the common libs.
 */
public class AgtTestSubjectGS2 extends AbstractGlobalMemoryState {
	/**
	 * Builds an initialized instance of the global state.
	 * @param owner The agent owning this global state.
	 * @param initialStressDegree The initial stress degree of the agent.
	 */
	public AgtTestSubjectGS2(
		IAgent owner,
		double initialStressDegree
	){
		super( owner );
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
