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
package fr.univ_artois.lgi2a.similar.microkernel;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.environment.IEnvironment4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar.microkernel.levels.ILevel;


/**
 * Models a simulation that can be performed using a simulation engine.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public interface ISimulationModel {
	/**
	 * Gets the initial time of this time model.
	 * @return The initial time of this model.
	 */
	SimulationTimeStamp getInitialTime( );
	
	/**
	 * Tells if a time stamp is greater or equal to the final time stamp.
	 * @param currentTime The last time the dynamic state of the simulation was in a partly consistent state.
	 * @param engine The simulation engine containing information about the currently running simulation.
	 * @return <code>true</code> if the final time of the level was reached.
	 */
	boolean isFinalTimeOrAfter( SimulationTimeStamp currentTime, ISimulationEngine engine );
	
	/**
	 * Generates the bare levels of the simulation. These levels contain no agents and define no environment.
	 * @param initialTime The initial time of the simulation.
	 * @return The bare levels of the simulation.
	 */
	List<ILevel> generateLevels( SimulationTimeStamp initialTime );
	
	/**
	 * Generates the environment of the simulation. At this stage, no agent are generated in the simulation.
	 * <p>
	 * 	This method should set the public local state of the environment for each level of the simulation.
	 * </p>
	 * @param initialTime The initial time of the simulation.
	 * @param levels The levels of the simulation.
	 * @return The generated environment and the influences to put in the state dynamics of the initial 
	 * dynamic state of the levels.
	 */
	EnvironmentInitializationData generateEnvironment( 
			SimulationTimeStamp initialTime, 
			Map<LevelIdentifier, ILevel> levels 
	);
	
	/**
	 * Generates the agents of the simulation.
	 * <p>
	 * 	This method should only create the agents, without adding them in the levels. This task is performed by the simulation engine.
	 * </p>
	 * @param initialTime The initial time of the simulation
	 * @param levels The levels of the simulation.
	 * @return The generated agents and the influences to put in the state dynamics of the initial 
	 * dynamic state of the levels.
	 */
	AgentInitializationData generateAgents( 
			SimulationTimeStamp initialTime, 
			Map<LevelIdentifier, ILevel> levels 
	);
	
	/**
	 * Models the initialization data coming from the generation of the environment of the simulation.
	 * 
	 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
	 */
	class EnvironmentInitializationData {
		/**
		 * The environment of the simulation.
		 */
		private final IEnvironment4Engine environment;
		/**
		 * The influences resulting from the generation of the environment.
		 * These influences are put inside the state dynamics of the initial consistent 
		 * public dynamic state of each level.
		 */
		private final InfluencesMap influences;
		
		/**
		 * Builds the initialization data coming from the generation of the environment of the simulation.
		 * In this object, the influences map is initially empty.
		 * @param environment The environment of the simulation.
		 */
		public EnvironmentInitializationData(
				IEnvironment4Engine environment
		){
			this.environment = environment;
			this.influences = new InfluencesMap( );
		}
		
		/**
		 * Gets the environment of the simulation.
		 * @return The environment of the simulation.
		 */
		public IEnvironment4Engine getEnvironment( ) {
			return this.environment;
		}
		
		/**
		 * Gets the influences resulting from the generation of the environment.
		 * These influences are put inside the state dynamics of the initial consistent 
		 * public dynamic state of each level.
		 * @return The influences resulting from the generation of the environment.
		 */
		public InfluencesMap getInfluences( ) {
			return this.influences;
		}
	}
	
	/**
	 * Models the initialization data coming from the generation of the initial agents of the simulation.
	 * 
	 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
	 */
	class AgentInitializationData {
		/**
		 * The agents of the simulation.
		 */
		private final Set<IAgent4Engine> agents;
		/**
		 * The influences resulting from the generation of the agents.
		 * These influences are put inside the state dynamics of the initial consistent 
		 * public dynamic state of each level.
		 */
		private final InfluencesMap influences;
		
		/**
		 * Builds the initialization data coming from the generation of the initial agents of the simulation.
		 * In this object, the influences map and the agents list are initially empty.
		 */
		public AgentInitializationData( ){
			this.agents = new LinkedHashSet<>( );
			this.influences = new InfluencesMap( );
		}
		
		/**
		 * Gets the agents of the simulation.
		 * @return The agents of the simulation.
		 */
		public Set<IAgent4Engine> getAgents( ) {
			return this.agents;
		}
		
		/**
		 * Gets the influences resulting from the generation of the agents.
		 * These influences are put inside the state dynamics of the initial consistent 
		 * public dynamic state of each level.
		 * @return The influences resulting from the generation of the agents.
		 */
		public InfluencesMap getInfluences( ) {
			return this.influences;
		}
	}
}
