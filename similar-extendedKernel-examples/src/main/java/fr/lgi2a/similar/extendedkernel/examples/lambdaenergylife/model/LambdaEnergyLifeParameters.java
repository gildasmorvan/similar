package fr.lgi2a.similar.extendedkernel.examples.lambdaenergylife.model;

import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.LambdaLifeParameters;

public class LambdaEnergyLifeParameters extends LambdaLifeParameters {

	/**
	 * Defines the energy at which the lambda value is maximal.
	 */
	public double energy;
	
	public LambdaEnergyLifeParameters() {
		super();
		this.energy = 0.2;	
	}

	
	
	
}
