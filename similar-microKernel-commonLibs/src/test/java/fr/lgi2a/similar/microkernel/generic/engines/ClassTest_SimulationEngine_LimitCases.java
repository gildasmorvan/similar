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
package fr.lgi2a.similar.microkernel.generic.engines;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import fr.lgi2a.similar.microkernel.IDynamicStateMap;
import fr.lgi2a.similar.microkernel.ILevel;
import fr.lgi2a.similar.microkernel.IProbe;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.ISimulationModel;
import fr.lgi2a.similar.microkernel.InfluencesMap;
import fr.lgi2a.similar.microkernel.ISimulationModel.AgentInitializationData;
import fr.lgi2a.similar.microkernel.ISimulationModel.EnvironmentInitializationData;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.generic.engines.tools.Test_LevelIdentifiers;
import fr.lgi2a.similar.microkernel.generic.engines.tools.UnitTest_Level;
import fr.lgi2a.similar.microkernel.generic.engines.tools.UnitTest_SimulationModel;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractEnvironment;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractPublicLocalState;

/**
 * This unit test checks that erroneous simulation models do raise exceptions when appropriate for a specific 
 * simulation engine.
 * <p>
 * 	The tested simulation engine is determined in the implementation
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public abstract class ClassTest_SimulationEngine_LimitCases implements IProbe {
	/**
	 * This boolean tells if an exception was caught during the execution of the runSimulation method.
	 */
	private boolean caughtException;
	/**
	 * If the 'caughtException' field is true, this field determines the type of the caught exception.
	 */
	private Object caughtExceptionClass;
	
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
		this.caughtException = true;
		this.caughtExceptionClass = cause.getClass();
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
	
	
	//  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //  //
	//
	//	TESTS OF THE SIMULATION ENGINE
	//
	//

	/**
	 * Tests that an {@link IllegalStateException} exception is caught if the 
	 * {@link ISimulationModel#getInitialTime()} method of the model used with 
	 * the simulation engine returns <code>null</code>.
	 */
	@Test
	public void test_initialTimeNull_ExceptionCaught( ) {
		// Create the engine.
		ISimulationEngine engine = this.createEngine( );
		//
		// Create the tested simulation model.
		//
		// Define the value returned by the various methods of the simulation model.
		SimulationTimeStamp initialTime = null;
		SimulationTimeStamp nextTime = new SimulationTimeStamp( 1 );
		boolean finalTimeOrAfter = true;
		List<ILevel> generateLevels = new LinkedList<ILevel>();
		generateLevels.add( new UnitTest_Level(
				new SimulationTimeStamp( 0 ),
				Test_LevelIdentifiers.ID1, 
				true, 
				nextTime
		));
		AbstractEnvironment environment = new AbstractEnvironment() {
			public void natural(
					LevelIdentifier level,
					SimulationTimeStamp time,
					IDynamicStateMap levelsPublicLocalObservableDynamicState,
					InfluencesMap producedInfluences
			) {
				// Does nothing.
			}
		};
		environment.includeNewLevel( Test_LevelIdentifiers.ID1, new AbstractPublicLocalState( Test_LevelIdentifiers.ID1 ) );
		EnvironmentInitializationData generateEnvironment = new EnvironmentInitializationData( environment );
		AgentInitializationData generateAgents = new AgentInitializationData( );
		// Create the simulation model.
		ISimulationModel model = new UnitTest_SimulationModel(
				initialTime, 
				nextTime, 
				finalTimeOrAfter, 
				generateLevels, 
				generateEnvironment, 
				generateAgents
		);
		//
		// Add a probe listening to the errors to the simulation.
		//
		engine.addProbe( "Test", this );
		//
		// Run the tested method.
		//
		engine.runNewSimulation( model );
		//
		// Check the validity of the obtained results.
		//
		assertThat( "An exception was caught in the probe", this.caughtException, 
				is( equalTo( true ) )
		);
		assertThat( "The class of the caught exception", (Object) this.caughtExceptionClass,
				is( equalTo( (Object) IllegalStateException.class ) )
		);
	}
	
	/**
	 * Tests that an {@link IllegalStateException} exception is caught if the 
	 * {@link ISimulationModel#generateLevels(fr.lgi2a.similar.microkernel.SimulationTimeStamp)} method
	 * of the model used with the simulation engine returns <code>null</code>.
	 */
	@Test
	public void test_generateLevelsReturningNull_ExceptionCaught( ) {
		// Create the engine.
		ISimulationEngine engine = this.createEngine( );
		//
		// Create the tested simulation model.
		//
		// Define the value returned by the various methods of the simulation model.
		SimulationTimeStamp initialTime = new SimulationTimeStamp( 0 );
		SimulationTimeStamp nextTime = new SimulationTimeStamp( 1 );
		boolean finalTimeOrAfter = true;
		List<ILevel> generateLevels = null;
		AbstractEnvironment environment = new AbstractEnvironment() {
			public void natural(
					LevelIdentifier level,
					SimulationTimeStamp time,
					IDynamicStateMap levelsPublicLocalObservableDynamicState,
					InfluencesMap producedInfluences
			) {
				// Does nothing.
			}
		};
		environment.includeNewLevel( Test_LevelIdentifiers.ID1, new AbstractPublicLocalState( Test_LevelIdentifiers.ID1 ) );
		EnvironmentInitializationData generateEnvironment = new EnvironmentInitializationData( environment );
		AgentInitializationData generateAgents = new AgentInitializationData( );
		// Create the simulation model.
		ISimulationModel model = new UnitTest_SimulationModel(
				initialTime, 
				nextTime, 
				finalTimeOrAfter, 
				generateLevels, 
				generateEnvironment, 
				generateAgents
		);
		//
		// Add a probe listening to the errors to the simulation.
		//
		engine.addProbe( "Test", this );
		//
		// Run the tested method.
		//
		engine.runNewSimulation( model );
		//
		// Check the validity of the obtained results.
		//
		assertThat( "An exception was caught in the probe", this.caughtException, 
				is( equalTo( true ) )
		);
		assertThat( "The class of the caught exception", (Object) this.caughtExceptionClass,
				is( equalTo( (Object) IllegalStateException.class ) )
		);
	}
}
