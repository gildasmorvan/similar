package fr.lgi2a.similar.extendedkernel.examples.densitycontrolledlife.model;

import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.LambdaLifeParameters;

public class DensityControlledLifeParameters extends LambdaLifeParameters {
	
	/**
	 * LAMBDA_LIFE parameter.
	 */
	private static final double LAMBDA_LIFE = 0.2734375;
	
	/**
	 * Defines the value of the proportional gain of the controller
	 */
	public double kP;
	
	/**
	 * The expected density of the cell cluster
	 */
	public double expectedDensity;
	
	/**
	 * Defines the xlength of the cell clusters
	 */
	public int xlength;
	
	/**
	 * Defines the ylength of the cell clusters
	 */
	public int ylength;
	
	public DensityControlledLifeParameters( ) {
		super( );
		this.lambda = LAMBDA_LIFE;
		this.expectedDensity = 0.05;
		this.kP = 10*this.expectedDensity;
		this.xlength = 10;
		this.ylength = 10;
	}

}
