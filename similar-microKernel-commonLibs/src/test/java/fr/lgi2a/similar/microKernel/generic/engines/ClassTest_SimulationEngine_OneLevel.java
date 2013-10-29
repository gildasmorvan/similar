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
package fr.lgi2a.similar.microkernel.generic.engines;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.notNullValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.generic.engines.tools.Test_Agent;
import fr.lgi2a.similar.microkernel.generic.engines.tools.Test_LevelIdentifiers;
import fr.lgi2a.similar.microkernel.libs.tools.learning.LearningSimilar_SimulationModel;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.Learning_AbstractAgent;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.Learning_AbstractEnvironment;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.Learning_GlobalMemoryState;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.Learning_Level;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.Learning_PublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.influence.Learning_Influence_AgentPublicLocalStateUpdate;
import fr.lgi2a.similar.microkernel.libs.tools.learning.model.influence.Learning_Influence_EnvironmentPublicLocalStateUpdate;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.Learning_EngineOperationMoment;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.Learning_EngineOperationType;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.Learning_Probe;
import fr.lgi2a.similar.microkernel.libs.tools.learning.trace.SimulationExecutionTrace;
import fr.lgi2a.similar.microkernel.states.IPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.states.IPublicLocalStateOfAgent;
import fr.lgi2a.similar.microkernel.states.dynamicstate.map.IDynamicStateMap;

/**
 * Tests the behavior of the {@link ISimulationEngine#runNewSimulation(fr.lgi2a.similar.microkernel.I_SimulationModel)} method for 
 * a simulation occurring in a single level.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public abstract class ClassTest_SimulationEngine_OneLevel implements IProbe {
	/**
	 * The exception that was caught by the simulation engine during the execution of 
	 * the {@link ISimulationEngine#runNewSimulation(fr.lgi2a.similar.microkernel.I_SimulationModel)} method.
	 * Equals to <code>null</code> if no exception occurred.
	 */
	private Throwable caughtException;
	
	/**
	 * Creates the simulation engine being used in a test of this test set.
	 * @return A new instance of the simulation engine being used in a test of this test set.
	 */
	protected abstract ISimulationEngine createEngine( );
	
	/**
	 * {@inheritDoc}
	 */
	public void prepareObservation( ){}
	/**
	 * {@inheritDoc}
	 */
	public void observeAtInitialTimes(
			SimulationTimeStamp initialTimestamp,
			ISimulationEngine simulationEngine
	){ }
	/**
	 * {@inheritDoc}
	 */
	public void observeAtPartialConsistentTime(
			SimulationTimeStamp timestamp,
			ISimulationEngine simulationEngine
	){ }
	/**
	 * {@inheritDoc}
	 */
	public void observeAtFinalTime(
			SimulationTimeStamp finalTimestamp,
			ISimulationEngine simulationEngine
	){ }
	/**
	 * {@inheritDoc}
	 */
	public void reactToError(
			String errorMessage,
			Throwable cause
	){ 
		this.caughtException = cause;
	}
	/**
	 * {@inheritDoc}
	 */
	public void reactToAbortion( 
			SimulationTimeStamp timestamp,
			ISimulationEngine simulationEngine
	){ }
	/**
	 * {@inheritDoc}
	 */
	public void endObservation( ){ }
	
	private LearningSimilar_SimulationModel generateSimulationModel( 
			SimulationTimeStamp initialTime,
			SimulationTimeStamp finalTime,
			final LevelIdentifier[][] agentLevels,
			final Map<LevelIdentifier,Set<LevelIdentifier>> perceptionRelationGraph,
			final Map<LevelIdentifier,Set<LevelIdentifier>> influenceRelationGraph
	) {
		LearningSimilar_SimulationModel model = new LearningSimilar_SimulationModel(
				initialTime, 
				finalTime
		){
			protected List<Learning_Level> generateCastedLevels(
					SimulationTimeStamp initialTime,
					SimulationExecutionTrace trace
			) {
				List<Learning_Level> levels = new LinkedList<Learning_Level>();
				for( LevelIdentifier levelId : perceptionRelationGraph.keySet() ){
					Learning_Level newLevel = new Learning_Level( initialTime, levelId, trace ) {
						public SimulationTimeStamp getNextTime( SimulationTimeStamp currentTime ) {
							return new SimulationTimeStamp( currentTime.getIdentifier() + 1 );
						}
					};
					for( LevelIdentifier perceptibleLevel : perceptionRelationGraph.get( levelId ) ){
						newLevel.addPerceptibleLevel( perceptibleLevel );
					}
					for( LevelIdentifier influenceableLevel : influenceRelationGraph.get( levelId ) ){
						newLevel.addInfluenceableLevel( influenceableLevel );
					}
					levels.add( newLevel );
				}
				return levels;
			}
			protected Learning_AbstractEnvironment createEnvironment(
					SimulationExecutionTrace trace
			) {
				Learning_AbstractEnvironment result = new Learning_AbstractEnvironment( trace ) {
					protected Set<IInfluence> produceInfluencesOfNatural(
							LevelIdentifier level,
							IDynamicStateMap levelsPublicLocalObservableDynamicState
					) {
						Set<IInfluence> influences = new LinkedHashSet<IInfluence>();
						for( LevelIdentifier levelId : levelsPublicLocalObservableDynamicState.keySet() ){
							IPublicLocalDynamicState dynamicState = levelsPublicLocalObservableDynamicState.get( levelId );
							influences.add( new Learning_Influence_EnvironmentPublicLocalStateUpdate( 
									levelId, 
									dynamicState.getPublicLocalStateOfEnvironment( )
							) );
							for( IPublicLocalStateOfAgent agentState : dynamicState.getPublicLocalStateOfAgents() ){
								influences.add( new Learning_Influence_AgentPublicLocalStateUpdate(
										levelId, 
										agentState
								) );
							}
						}
						return influences;
					}
				};
				return result;
			}
			protected List<Learning_AbstractAgent> createAgents(
					SimulationExecutionTrace trace
			) {
				List<Learning_AbstractAgent> result = new LinkedList<Learning_AbstractAgent>( );
				int agentId = 1;
				for( LevelIdentifier[] levelsId : agentLevels ){
					Test_Agent agent = new Test_Agent( "Agent#" + (agentId++), trace );
					agent.initializeGlobalMemoryState( new Learning_GlobalMemoryState( agent ) );
					for( LevelIdentifier levelId : levelsId ){
						agent.includeNewLevel( levelId, new Learning_PublicLocalStateOfAgent( levelId, agent ) );
					}
					result.add( agent );
				}
				return result;
			}
			
		};
		return model;
	}
	
	
	//  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //
	//
	//	TESTS OF THE SIMULATION ENGINE
	//
	//
	
	/**
	 * Tests that the simulation engine has an appropriate behavior when the final time is lower or equal to the initial time.
	 */
	@Test
	public void test_oneAgent_FinalTimeLowerOrEqualToInitialTime_NoError(){
		//
		// Prepare the tested object
		//
		// Create the engine.
		ISimulationEngine engine = this.createEngine( );
		// Add a probe listening to the errors to the simulation.
		engine.addProbe( "Test", this );
		// Create a model checking the behavior of the simulation engine.
		SimulationTimeStamp initialTime = new SimulationTimeStamp( 0 );
		SimulationTimeStamp finalTime = new SimulationTimeStamp( 0 );
		LevelIdentifier[][] agentLevels = new LevelIdentifier[][]{
			new LevelIdentifier[]{
					Test_LevelIdentifiers.ID1
			}
		};
		Map<LevelIdentifier, Set<LevelIdentifier>> perceptionRelationGraph = new HashMap<LevelIdentifier, Set<LevelIdentifier>>( );
		Map<LevelIdentifier, Set<LevelIdentifier>> influenceRelationGraph = new HashMap<LevelIdentifier, Set<LevelIdentifier>>( );
		LevelIdentifier[] levelsOfSimulation = new LevelIdentifier[]{
				Test_LevelIdentifiers.ID1
		};
		for( LevelIdentifier levelId : levelsOfSimulation ){
			Set<LevelIdentifier> perceptibleLevels = new LinkedHashSet<LevelIdentifier>();
			perceptionRelationGraph.put( levelId, perceptibleLevels );
			Set<LevelIdentifier> influenceableLevels = new LinkedHashSet<LevelIdentifier>();
			influenceRelationGraph.put( levelId, influenceableLevels );
			// We build a complete perception and influence graph.
			perceptibleLevels.addAll( Arrays.asList( levelsOfSimulation ) );
			influenceableLevels.addAll( Arrays.asList( levelsOfSimulation ) );
		}
		LearningSimilar_SimulationModel model = this.generateSimulationModel(
				initialTime, 
				finalTime, 
				agentLevels, 
				perceptionRelationGraph, 
				influenceRelationGraph
		);
		engine.addProbe( "Trace update", new Learning_Probe( model.getTrace() ) );
		//
		// Run the tested method.
		//
		engine.runNewSimulation( model );
		//
		// Check the validity of the obtained results.
		//
		assertThat( "The exception that happened during the execution of the simulation", this.caughtException, 
				is( equalTo( null ) )
		);
		assertThat( "The time stamps of the simulation", model.getTrace().getOrderedSimulationTimeStamps(), allOf(
				is( notNullValue() ),
				contains( initialTime )
			)
		);
		assertThat(
				"The final time stamp of the simulation", model.getTrace().getFinalTime(),
				is( equalTo( finalTime ) )
		);
		assertThat(
				"The moments when operations were performed during simulation", model.getTrace().getOperationsMoments(),
				is( empty() )
		);
	}
	
	/**
	 * Tests that the simulation engine has an appropriate behavior when the final time slightly higher than the initial time.
	 */
	@Test
	public void test_oneAgent_FinalTimeSlightlyHigherThanInitialTime_NoError(){
		//
		// Prepare the tested object
		//
		// Create the engine.
		ISimulationEngine engine = this.createEngine( );
		// Add a probe listening to the errors to the simulation.
		engine.addProbe( "Test", this );
		// Create a model checking the behavior of the simulation engine.
		SimulationTimeStamp initialTime = new SimulationTimeStamp( 0 );
		SimulationTimeStamp finalTime = new SimulationTimeStamp( 1 );
		LevelIdentifier[][] agentLevels = new LevelIdentifier[][]{
			new LevelIdentifier[]{
					Test_LevelIdentifiers.ID1
			}
		};
		Map<LevelIdentifier, Set<LevelIdentifier>> perceptionRelationGraph = new HashMap<LevelIdentifier, Set<LevelIdentifier>>( );
		Map<LevelIdentifier, Set<LevelIdentifier>> influenceRelationGraph = new HashMap<LevelIdentifier, Set<LevelIdentifier>>( );
		LevelIdentifier[] levelsOfSimulation = new LevelIdentifier[]{
				Test_LevelIdentifiers.ID1
		};
		for( LevelIdentifier levelId : levelsOfSimulation ){
			Set<LevelIdentifier> perceptibleLevels = new LinkedHashSet<LevelIdentifier>();
			perceptionRelationGraph.put( levelId, perceptibleLevels );
			Set<LevelIdentifier> influenceableLevels = new LinkedHashSet<LevelIdentifier>();
			influenceRelationGraph.put( levelId, influenceableLevels );
			// We build a complete perception and influence graph.
			perceptibleLevels.addAll( Arrays.asList( levelsOfSimulation ) );
			influenceableLevels.addAll( Arrays.asList( levelsOfSimulation ) );
		}
		LearningSimilar_SimulationModel model = this.generateSimulationModel(
				initialTime, 
				finalTime, 
				agentLevels, 
				perceptionRelationGraph, 
				influenceRelationGraph
		);
		engine.addProbe( "Trace update", new Learning_Probe( model.getTrace() ) );
		//
		// Run the tested method.
		//
		engine.runNewSimulation( model );
		//
		// Check the validity of the obtained results.
		//
		assertThat( "The exception that happened during the execution of the simulation", this.caughtException, 
				is( equalTo( null ) )
		);
		assertThat( "The time stamps of the simulation", model.getTrace().getOrderedSimulationTimeStamps(), allOf(
				is( notNullValue() ),
				contains( initialTime, finalTime )
			)
		);
		assertThat( "The final time stamp of the simulation", model.getTrace().getFinalTime(),
				is( equalTo( finalTime ) )
		);
		assertThat( "The moments when operations were performed during simulation", model.getTrace().getOperationsMoments(), 
				contains( 
						(Learning_EngineOperationMoment) new Learning_EngineOperationMoment.Learning_EngineOperationMoment_After( initialTime ),
						(Learning_EngineOperationMoment) new Learning_EngineOperationMoment.Learning_EngineOperationMoment_Before( finalTime ) 
				) 
		);
		Learning_EngineOperationMoment beforeFirstReaction = new Learning_EngineOperationMoment.Learning_EngineOperationMoment_After( initialTime );
		assertThat( "The operations that were performed slightly after the time " + initialTime, model.getTrace().getOperationsFor( beforeFirstReaction ), 
				hasSize( 4 ) // natural & perception => memory revision => decision
		);
		Learning_EngineOperationMoment afterFirstReaction = new Learning_EngineOperationMoment.Learning_EngineOperationMoment_Before( finalTime );
		assertThat( "The operations that were performed slightly after the time " + finalTime, model.getTrace().getOperationsFor( afterFirstReaction ), 
				hasSize( 3 ) // system reaction => regular reaction => system reaction
		);
	}
	
	/**
	 * Tests that the simulation engine has an appropriate behavior when the final time is significantly higher than the initial time.
	 */
	@Test
	public void test_oneAgent_FinalTimeSignificantlyHigherThanInitialTime_NoError(){
		//
		// Prepare the tested object
		//
		// Create the engine.
		ISimulationEngine engine = this.createEngine( );
		// Add a probe listening to the errors to the simulation.
		engine.addProbe( "Test", this );
		// Create a model checking the behavior of the simulation engine.
		SimulationTimeStamp initialTime = new SimulationTimeStamp( 0 );
		SimulationTimeStamp finalTime = new SimulationTimeStamp( 10 );
		LevelIdentifier[][] agentLevels = new LevelIdentifier[][]{
			new LevelIdentifier[]{
					Test_LevelIdentifiers.ID1
			}
		};
		Map<LevelIdentifier, Set<LevelIdentifier>> perceptionRelationGraph = new HashMap<LevelIdentifier, Set<LevelIdentifier>>( );
		Map<LevelIdentifier, Set<LevelIdentifier>> influenceRelationGraph = new HashMap<LevelIdentifier, Set<LevelIdentifier>>( );
		LevelIdentifier[] levelsOfSimulation = new LevelIdentifier[]{
				Test_LevelIdentifiers.ID1
		};
		for( LevelIdentifier levelId : levelsOfSimulation ){
			Set<LevelIdentifier> perceptibleLevels = new LinkedHashSet<LevelIdentifier>();
			perceptionRelationGraph.put( levelId, perceptibleLevels );
			Set<LevelIdentifier> influenceableLevels = new LinkedHashSet<LevelIdentifier>();
			influenceRelationGraph.put( levelId, influenceableLevels );
			// We build a complete perception and influence graph.
			perceptibleLevels.addAll( Arrays.asList( levelsOfSimulation ) );
			influenceableLevels.addAll( Arrays.asList( levelsOfSimulation ) );
		}
		LearningSimilar_SimulationModel model = this.generateSimulationModel(
				initialTime, 
				finalTime, 
				agentLevels, 
				perceptionRelationGraph, 
				influenceRelationGraph
		);
		engine.addProbe( "Trace update", new Learning_Probe( model.getTrace() ) );
		//
		// Run the tested method.
		//
		engine.runNewSimulation( model );
		//
		// Check the validity of the obtained results.
		//
		assertThat( "The exception that happened during the execution of the simulation", this.caughtException, 
				is( equalTo( null ) )
		);
		SimulationTimeStamp[] expectedTimestamps = new SimulationTimeStamp[ (int) (1 + finalTime.getIdentifier() - initialTime.getIdentifier() ) ] ;
		expectedTimestamps[ 0 ] = initialTime;
		for( int index = 1; index <= finalTime.getIdentifier() - initialTime.getIdentifier(); index++ ){
			expectedTimestamps[ index ] = new SimulationTimeStamp( initialTime.getIdentifier() + index );
		}
		assertThat( "The time stamps of the simulation", model.getTrace().getOrderedSimulationTimeStamps(), allOf(
				is( notNullValue() ),
				contains( expectedTimestamps )
			)
		);
		assertThat( "The final time stamp of the simulation", model.getTrace().getFinalTime(),
				is( equalTo( finalTime ) )
		);
		Learning_EngineOperationMoment[] moments = new Learning_EngineOperationMoment[ (int)( finalTime.getIdentifier() - initialTime.getIdentifier() ) * 2 ];
		for( int index = 0; index < moments.length; index++ ){
			// Convert the index into a time stamp id.
			boolean beforeTimestamp = (index % 2) == 1;
			long timeStampId = initialTime.getIdentifier() + ( index + 1 ) / 2;
			Learning_EngineOperationMoment moment = null;
			if( beforeTimestamp ){
				moment = new Learning_EngineOperationMoment.Learning_EngineOperationMoment_Before( new SimulationTimeStamp( timeStampId ) );
			} else {
				moment = new Learning_EngineOperationMoment.Learning_EngineOperationMoment_After( new SimulationTimeStamp( timeStampId ) );
			}
			moments[ index ] = moment;
		}
		assertThat( "The moments when operations were performed during simulation", model.getTrace().getOperationsMoments(), 
				contains( moments )
		);
		// Check that the operation performed during each moment of the simulation were made in the appropriate order.
		for( long timestampId = initialTime.getIdentifier(); timestampId <= finalTime.getIdentifier(); timestampId++ ){
			//
			// Check all operations performed slightly after arriving to a time step (perception/memory revision/decision/natural)
			//
			if( timestampId != finalTime.getIdentifier() ){
				Learning_EngineOperationMoment beforeReaction = new Learning_EngineOperationMoment.Learning_EngineOperationMoment_After( new SimulationTimeStamp( timestampId ) );
				assertThat( "The operations that were performed slightly after the time " + new SimulationTimeStamp( timestampId ), model.getTrace().getOperationsFor( beforeReaction ), 
						hasSize( 4 )
				);
				boolean perceptionDone = false;
				boolean memorizyRevisionDone = false;
				boolean decisionDone = false;
				boolean naturalDone = false;
				Learning_EngineOperationType[] expectedValues = new Learning_EngineOperationType[]{
						Learning_EngineOperationType.PERCEPTION,
						Learning_EngineOperationType.MEMORIZATION,
						Learning_EngineOperationType.DECISION,
						Learning_EngineOperationType.NATURAL
				};
				for( int index = 0; index < 4; index++ ){
					Learning_EngineOperationType opType = model.getTrace().getOperationsFor( beforeReaction ).get( index ).getOperationType();
					assertThat( "The type of the " + index + "th operation performed slightly after the time " + beforeReaction, opType, allOf(
							is( notNullValue() ),
							isIn( expectedValues  )
					) );
					switch( opType ){
						case DECISION:
							assertThat( "The decision operation of the time " + beforeReaction + " happened after perception", perceptionDone,
									is( equalTo( true ) )
							);
							assertThat( "The decision operation of the time " + beforeReaction + " happened after memorization", memorizyRevisionDone,
									is( equalTo( true ) )
							);
							decisionDone = true;
							break;
						case MEMORIZATION:
							assertThat( "The memory revision operation of the time " + beforeReaction + " happened after perception", perceptionDone,
									is( equalTo( true ) )
							);
							memorizyRevisionDone = true;
							break;
						case NATURAL:
							naturalDone = true;
							break;
						case PERCEPTION:
							perceptionDone = true;
							break;
						default:
							// Should never happen (see the assertion before the switch instruction.
							throw new IllegalStateException( "An illogic exception was caught." );
					}
				}
				assertThat( "The perception operation of the time " + beforeReaction + " was performed", perceptionDone,
						is( equalTo( true ) )
				);
				assertThat( "The memory revision operation of the time " + beforeReaction + " was performed", memorizyRevisionDone,
						is( equalTo( true ) )
				);
				assertThat( "The decision operation of the time " + beforeReaction + " was performed", decisionDone,
						is( equalTo( true ) )
				);
				assertThat( "The natural operation of the time " + beforeReaction + " was performed", naturalDone,
						is( equalTo( true ) )
				);
				//
				// Check all operations performed slightly before arriving to a new time step (reaction)
				//
				if( timestampId != initialTime.getIdentifier() ){
					Learning_EngineOperationMoment afterReaction = new Learning_EngineOperationMoment.Learning_EngineOperationMoment_Before( new SimulationTimeStamp( timestampId ) );
					assertThat( "The operations that were performed slightly after the time " + new SimulationTimeStamp( timestampId ), model.getTrace().getOperationsFor( afterReaction ), 
							hasSize( 3 )
					);
					expectedValues = new Learning_EngineOperationType[]{
							Learning_EngineOperationType.SYSTEMREACTION,
							Learning_EngineOperationType.REGULARREACTION
					};
					boolean firstSystemReactionDone = false;
					boolean regularReactionDone = false;
					boolean secondSystemReactionDone = false;
					for( int index = 0; index < 3; index++ ){
						Learning_EngineOperationType opType = model.getTrace().getOperationsFor( afterReaction ).get( index ).getOperationType();
						assertThat( "The type of the " + index + "th operation performed slightly before the time " + beforeReaction, opType, allOf(
								is( notNullValue() ),
								isIn( expectedValues  )
						) );
						switch( opType ){
							case SYSTEMREACTION:
								if( ! firstSystemReactionDone ){
									firstSystemReactionDone = true;
								} else {
									assertThat( "The second system reaction operation of the time " + afterReaction + " happened after the first system reaction", firstSystemReactionDone,
											is( equalTo( true ) )
									);
									assertThat( "The second system reaction operation of the time " + afterReaction + " happened after the regular reaction", regularReactionDone,
											is( equalTo( true ) )
									);
									secondSystemReactionDone = true;
								}
								break;
							case REGULARREACTION:
								assertThat( "The regular reaction operation of the time " + afterReaction + " happened after the first system reaction", firstSystemReactionDone,
										is( equalTo( true ) )
								);
								regularReactionDone = true;
								break;
							default:
								// Should never happen (see the assertion before the switch instruction.
								throw new IllegalStateException( "An illogic exception was caught." );
						}
					}
					assertThat( "The first system reaction operation of the time " + afterReaction + " was performed", firstSystemReactionDone,
							is( equalTo( true ) )
					);
					assertThat( "The regular reaction operation of the time " + afterReaction + " was performed", regularReactionDone,
							is( equalTo( true ) )
					);
					assertThat( "The second system reaction operation of the time " + afterReaction + " was performed", secondSystemReactionDone,
							is( equalTo( true ) )
					);
				}
			}
		}
	}
}
