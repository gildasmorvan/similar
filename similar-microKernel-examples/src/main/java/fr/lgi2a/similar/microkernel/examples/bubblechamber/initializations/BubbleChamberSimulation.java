/**
 * Copyright or � or Copr. LGI2A
 * 
 * LGI2A - Laboratoire de Genie Informatique et d'Automatique de l'Artois - EA 3926 
 * Faculte des Sciences Appliquees
 * Technoparc Futura
 * 62400 - BETHUNE Cedex
 * http://www.lgi2a.univ-artois.fr/
 * 
 * Email: gildas.morvan@univ-artois.fr
 * 
 * Contributors:
 * 	Gildas MORVAN (creator of the IRM4MLS formalism)
 * 	Yoann KUBERA (designer, architect and developer of SIMILAR)
 * 
 * This software is a computer program whose purpose is to support the
 * implementation of multi-agent-based simulations using the formerly named
 * IRM4MLS meta-model. This software defines an API to implement such 
 * simulations, and also provides usage examples.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.lgi2a.similar.microkernel.examples.bubblechamber.initializations;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.BubbleChamberParameters;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.cannon.AgtCannon;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.cannon.AgtCannonFactory;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.environment.BubbleChamberEnvironment;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.environment.chamber.EnvPLSInChamber;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.environment.external.EnvPLSInExternal;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.levels.chamber.ChamberLevel;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.levels.external.ExternalLevel;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.tools.RandomValueFactory;
import fr.lgi2a.similar.microkernel.levels.ILevel4Engine;
import fr.lgi2a.similar.microkernel.libs.abstractimpl.AbstractSimulationModel;
import fr.lgi2a.similar.microkernel.libs.generic.EmptyLocalStateOfEnvironment;

/**
 * The simulation model of the "Bubble chamber" simulation.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class BubbleChamberSimulation extends AbstractSimulationModel {
	/**
	 * The parameters being used in the simulation.
	 */
	private BubbleChamberParameters parameters;
	/**
	 * The final time of the simulation.
	 */
	private SimulationTimeStamp finalTime;
	
	/**
	 * Builds a new model for the "Bubble chamber" simulation.
	 * @param initialTime The initial time of the simulation.
	 * @param finalTime The final time of the simulation.
	 * @throws IllegalArgumentException If the <code>parameters</code> or the <code>finalTime</code>
	 * arguments are <code>null</code>.
	 */
	public BubbleChamberSimulation(
			SimulationTimeStamp initialTime,
			SimulationTimeStamp finalTime,
			BubbleChamberParameters parameters
	) {
		super(initialTime);
		if( parameters == null || finalTime == null ){
			throw new IllegalArgumentException( "The arguments cannot be null." );
		} else {
			this.parameters = parameters;
			this.finalTime = finalTime;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFinalTimeOrAfter(
			SimulationTimeStamp currentTime,
			ISimulationEngine engine
	) {
		return currentTime.compareTo( this.finalTime ) >= 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ILevel4Engine> generateLevels(
			SimulationTimeStamp initialTime
	) {
		List<ILevel4Engine> result = new LinkedList<ILevel4Engine>();
		
		// Create the "external" level of the simulation.
		ExternalLevel external = new ExternalLevel(
			this.parameters.powerToTemperatureConversionRatio
		);
		result.add( external );
		// Create the "chamber" level of the simulation.
		ChamberLevel chamber = new ChamberLevel( );
		result.add( chamber );
		
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EnvironmentInitializationData generateEnvironment(
			SimulationTimeStamp initialTime, 
			Map<LevelIdentifier, ILevel4Engine> levels
	) {
		// Create the environment.
		BubbleChamberEnvironment environment = new BubbleChamberEnvironment( );
		// Set the local state of the environment for each level.
		environment.includeNewLevel(
				BubbleChamberLevelList.CHAMBER, 
				new EnvPLSInChamber( this.parameters.chamberBounds ),
				new EmptyLocalStateOfEnvironment( BubbleChamberLevelList.CHAMBER )
		);
		environment.includeNewLevel(
				BubbleChamberLevelList.EXTERNAL, 
				new EnvPLSInExternal( this.parameters.initialAmbientTemperature ),
				new EmptyLocalStateOfEnvironment( BubbleChamberLevelList.EXTERNAL )
		);
		
		return new EnvironmentInitializationData( environment );
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AgentInitializationData generateAgents(
			SimulationTimeStamp initialTime, 
			Map<LevelIdentifier, ILevel4Engine> levels
	) {
		AgentInitializationData result = new AgentInitializationData();
		
		// Create the agents initially lying in the simulation.
		double initialPower = RandomValueFactory.getStrategy().randomDouble(
				this.parameters.cannonPowerRange[ 0 ], 
				this.parameters.cannonPowerRange[ 1 ]
		);
		AgtCannon cannon = AgtCannonFactory.generate(
				this.parameters.chamberBounds.getX(), 
				this.parameters.chamberBounds.getCenterY(), 
				1.0, 
				0.0, 
				initialPower, 
				this.parameters.initialAmbientTemperature
		);
		result.getAgents().add( cannon );
		
		return result;
	}

}
