/**
 * Copyright or © or Copr. LGI2A
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
package fr.lgi2a.similar.microkernel.examples.concepts;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.lgi2a.similar.microkernel.ILevel;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.ISimulationModel;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.alien.AgtAlien;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.citizen.AgtCitizen;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.editorinchief.AgtEditorInChief;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.editorinchief.AgtFactoryEditorInChief;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.fbi.AgtFBI;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.EnvForConceptsSimulation;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.Cities;
import fr.lgi2a.similar.microkernel.examples.concepts.level.PhysicalLevel;
import fr.lgi2a.similar.microkernel.examples.concepts.level.SocialLevel;
import fr.lgi2a.similar.microkernel.examples.concepts.level.SpaceLevel;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractSimulationModel;

/**
 * Models a simulation containing three levels 'space', 'social' and 'physical' where 'Citizen', 'FBI', 'Editor in chief' and 'Alien' agents coexist.
 * 
 * <h1>Simulation models in the SIMILAR API suite.</h1>
 * <p>
 * 	In the micro-kernel of SIMILAR, a simulation model is implemented as an 
 * 	instance of either the {@link ISimulationModel} interface, or of the {@link AbstractSimulationModel} 
 * 	abstract class.
 * 	In this example, we use the abstract class which is easier to implement.
 * </p>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class ConceptsSimulationModel extends AbstractSimulationModel {
	/**
	 * The final time stamp of the simulation, after which the execution of the simulation stops.
	 */
	private SimulationTimeStamp finalTime;
	/**
	 * The parameters used in this simulation model.
	 */
	private ConceptsSimulationParameters parameters;

	/**
	 * @param initialTime The initial time stamp of the simulation.
	 * @param finalTime The final time stamp of the simulation, after which the execution of the simulation stops.
	 * @param parameters The parameters used in this simulation model.
	 */
	public ConceptsSimulationModel( 
			SimulationTimeStamp initialTime,
			SimulationTimeStamp finalTime,
			ConceptsSimulationParameters parameters
	) throws IllegalArgumentException {
		super(initialTime);
		this.finalTime = finalTime;
		this.parameters = parameters;
	}

	/**
	 * Defines when the simulation has to stop, based on the information contained in the dynamic state of the simulation 
	 * and the current time stamp of the simulation.
	 * The simulation ends after the reaction performed slightly before the time stamp <code>currentTime</code> if this method returns <code>true</code>.
	 * @param currentTime The current time stamp of the simulation.
	 * @param engine The engine containing the dynamic state of the currently running simulation (see the 
	 * {@link ISimulationEngine#getSimulationDynamicStates()} method).
	 * @return <code>true</code> if no change occur in the dynamic state of the simulation after the time stamp <code>currentTime</code>.
	 */
	@Override
	public boolean isFinalTimeOrAfter(
			SimulationTimeStamp currentTime,
			ISimulationEngine engine
	) {
		// In this simulation, the simulation stops when a specific time stamp is reached.
		return currentTime.compareTo( this.finalTime ) >= 0;
	}

	/**
	 * This method defines the initial levels of the simulation. These levels do not define the public local state of the environment or 
	 * of the agents initially lying in the simulation (these tasks are performed in the other methods of the simulation model).
	 * The role of this method is to create bare instances of the {@link ILevel} class for each level of the simulation, and to generate the 
	 * influence/perception relation graphs.
	 * @param initialTime The initial time of the simulation.
	 */
	@Override
	public List<ILevel> generateLevels( SimulationTimeStamp initialTime ) {
		List<ILevel> result = new LinkedList<ILevel>();
		// In this model, the initialization of the perception and influence relation graphs is made inside the constructor of each level.
		result.add( new PhysicalLevel( 
				initialTime, 
				this.parameters.getAlienExperimentSpeed(), 
				this.parameters.getCitizenStrangePhysicalManifestationsApparitionRate(), 
				this.parameters.getFbiCaptureEfficiency()
		) );
		result.add( new SocialLevel( 
				initialTime 
		) );
		result.add( new SpaceLevel( 
				initialTime, 
				this.parameters.getTimeEvolutionDescriptorOfSpaceLevel() 
		) );
		return result;
	}

	/**
	 * This method defines the environment of the simulation. This environment has to define a physical state for each level generated by
	 * the {@link ISimulationModel#generateLevels(SimulationTimeStamp)} method. It also defines a natural action of the environment on the dynamic state 
	 * of the simulation (for instance updating a day/night cycle).
	 * @param initialTime The initial time of the simulation.
	 * @param levels The levels that were created by the {@link ISimulationModel#generateLevels(SimulationTimeStamp)} method call.
	 */
	@Override
	public EnvironmentInitializationData generateEnvironment(
			SimulationTimeStamp initialTime,
			Map<LevelIdentifier, ILevel> levels
	) {
		// Create the environment and its public local states.
		// To preserve the coherence of the time model, the current time of the day is computed using the
		// value of the current time stamp. This computation is performed in a class modeling the interpretation of time.
		EnvForConceptsSimulation environment = new EnvForConceptsSimulation( 
				ConceptsSimulationTimeInterpretationModel.INSTANCE.getTimeOfTheDay( initialTime ), 
				this.parameters.getFbiAdvisedThreshold()
		);
		// Create the object containing the initialization data of the environment.
		EnvironmentInitializationData initializationData = new EnvironmentInitializationData(environment);
		// During this step of the initialization of the simulation, influences can be added to the state dynamics of the various levels
		// using the accessors of the influences map returned by the initializationData.getInfluences() method.
		// In this simulation model, no specific influences are added.
		return initializationData;
	}

	/**
	 * This method defines the initial agents of the simulation. These agents have to define a public local state 
	 * for each level they are lying into.
	 * @param initialTime The initial time of the simulation.
	 * @param levels The levels that were generated by the {@link ISimulationModel#generateLevels(SimulationTimeStamp)} methods and where the public local
	 * state of the environment in the various levels is defined by the {@link ISimulationModel#generateEnvironment(SimulationTimeStamp, Map)} method.
	 */
	@Override
	public AgentInitializationData generateAgents(
			SimulationTimeStamp initialTime, 
			Map<LevelIdentifier, ILevel> levels
	) {
		// Create the object containing the initialization data of the agents.
		AgentInitializationData initializationData = new AgentInitializationData( );
		// The initial agents are specified using the access methods of the set returned by
		// the initializationData.getAgents() method.
		//
		// Create the initial editor in chief agent.
		//
		// Get an address at random for this agent.
		Cities randomCity = Cities.values()[ ConceptsSimulationRandom.randomInt( Cities.values().length ) ];
		AgtEditorInChief editor = AgtFactoryEditorInChief.INSTANCE.generateAgent(
				randomCity, 
				this.parameters.getFbiAdvisedThreshold()
		);
		// First method to add agents initially lying in the simulation: 
		// include them in the initializationData.getAgents() set. Note that if these agents have to be included in the
		// public local state of the environment (this is the case for instance for citizens that have to be added to 
		// the people living in a specific city), this operation has to be manually done, since the reaction to the system reaction
		// does not apply to the agents that are initially in the simulation.
		initializationData.getAgents().add( editor );
		// Create the FBI agent.
		AgtFBI fbi = new AgtFBI(
				this.parameters.getFbiThresholdBeforeCitizenLobotomy(), 
				this.parameters.getFbiAdvisedThreshold()
		);
		initializationData.getAgents().add( fbi );
		// Create the citizen agents.
		for( Cities city : Cities.values() ){
			int numberOfCitizen = this.parameters.getRangeOfCitizenPerCity()[ 0 ] + ConceptsSimulationRandom.randomInt( 
					this.parameters.getRangeOfCitizenPerCity()[ 1 ] - this.parameters.getRangeOfCitizenPerCity()[ 0 ]
			);
			while( numberOfCitizen > 0 ) {
				// The initialization of the agent (public local states, global memory state, ...) is performed 
				// in the constructor of the agent.
				AgtCitizen citizenAgent = new AgtCitizen( city );
				// Second approach to the addition of the agents: add them during the first reaction of a level (here the 'physical' level).
				SystemInfluenceAddAgent addAgentInfluence = new SystemInfluenceAddAgent(
						ConceptsSimulationLevelIdentifiers.PHYSICAL_LEVEL, 
						citizenAgent
				);
				initializationData.getInfluences().add( addAgentInfluence  );
				numberOfCitizen--;
			}
		}
		// Create the alien agents.
		for( int alienIdx = 0; alienIdx < this.parameters.getAliensNumber(); alienIdx++ ) {
			AgtAlien alien = new AgtAlien(
					this.parameters.getAlienCitiesPerPerception(), 
					this.parameters.getAlienExperimentsEfficiency() 
			);
			// Second approach to the addition of the agents: add them during the first reaction of a level (here the 'space' level).
			SystemInfluenceAddAgent addAgentInfluence = new SystemInfluenceAddAgent(
					ConceptsSimulationLevelIdentifiers.SPACE_LEVEL, 
					alien
			);
			initializationData.getInfluences().add( addAgentInfluence  );
		}
		// During this step of the initialization of the simulation, influences can be added to the state dynamics of the various levels
		// using the accessors of the influences map returned by the initializationData.getInfluences() method.
		// In this simulation model, no specific influences are added.
		return initializationData;
	}
}
