package fr.lgi2a.similar.extendedkernel.examples.lambdaenergylife.model.levels.micro;

import java.util.Set;

import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.influences.tomicro.NextStateInfluence;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.model.levels.micro.LvlMicroReaction;
import fr.lgi2a.similar.extendedkernel.examples.lambdalife.tools.RandomValueFactory;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.influences.IInfluence;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;

public class LvlMicroEnergyReaction extends LvlMicroReaction {

	
	/**
	 * The energy parameter of the lambda game of life.
	 */
	protected double energy;
	
	/**
	 * The norm parameter of the lambda energy game of life.
	 */
	protected int norm;
	
	/**
	 * Builds the reaction of the "Micro" level, based on lambda and energy parameters.
	 * @param lambda The lambda parameter.
	 */
	public LvlMicroEnergyReaction(double lambda, double energy, int boardSize) {
		super(lambda);
		this.energy = energy;
		this.norm = boardSize;
	}
	
	
	//TODO : Use super.makeRegularReaction for dead to alive influences 
	/**
     * {@inheritDoc}
     */
	@Override
	public void makeRegularReaction(
		SimulationTimeStamp transitoryTimeMin,
		SimulationTimeStamp transitoryTimeMax,
		ConsistentPublicLocalDynamicState consistentState,
		Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
		InfluencesMap remainingInfluences
	) {
		
		double localEnergy = ((double) regularInfluencesOftransitoryStateDynamics.size())/norm;
		/*double localEnergy = 0;
		for( IInfluence influence : regularInfluencesOftransitoryStateDynamics ){
			if( influence.getCategory().equals( NextStateInfluence.CATEGORY ) ){
				NextStateInfluence castedNextStateInfluence = (NextStateInfluence) influence;
				if( 
					castedNextStateInfluence.getNextState()
				) {
					localEnergy++;
				}
			}
		}
		localEnergy/=regularInfluencesOftransitoryStateDynamics.size();*/
		
		for( IInfluence influence : regularInfluencesOftransitoryStateDynamics ){
			if( influence.getCategory().equals( NextStateInfluence.CATEGORY ) ){
				NextStateInfluence castedNextStateInfluence = (NextStateInfluence) influence;
				if( 
					castedNextStateInfluence.getTarget().isAlive() && !castedNextStateInfluence.getNextState()
				) {
					// Case where the cell should die.
					// In the lambda game of life, there is a lambda probability that the cell
					// stays alive anyway.
					double randomDouble = RandomValueFactory.getStrategy().randomDouble();
					if(localEnergy >= this.energy) {
						castedNextStateInfluence.getTarget().setAlive( randomDouble < this.p);
					}
					else {
						//castedNextStateInfluence.getTarget().setAlive( castedNextStateInfluence.getNextState() );
						castedNextStateInfluence.getTarget().setAlive( randomDouble < this.p*localEnergy/this.energy);
					}
				} else {
            		castedNextStateInfluence.getTarget().setAlive( castedNextStateInfluence.getNextState() );
				}
			}
		}
	}


}
