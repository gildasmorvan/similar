package fr.lgi2a.wildlifesimulation.model.agents.lion;

import fr.lgi2a.similar.microkernel.IAgent;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractGlobalMemoryState;

/**
 * The global state of the "Lion" agent.
 */
public class AgtLionGS extends AbstractGlobalMemoryState {
	/**
	 *  Builds an initialized instance of the global state.
	 * @param owner The agent owning this global state.
	 */
	public AgtLionGS(IAgent owner) {
		super(owner);
	}
}
