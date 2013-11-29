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
package fr.lgi2a.similar.microkernel.examples.concepts.level;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import fr.lgi2a.similar.microkernel.IInfluence;
import fr.lgi2a.similar.microkernel.ILevel;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.ConsistentPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationLevelIdentifiers;
import fr.lgi2a.similar.microkernel.examples.concepts.ConceptsSimulationTimeInterpretationModel;
import fr.lgi2a.similar.microkernel.examples.concepts.agents.fbi.AgtFBI;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.physical.TimeOfTheDay;
import fr.lgi2a.similar.microkernel.examples.concepts.environment.social.EnvPLSSocial;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.tosocial.RISocialChangeBroadcast;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.tosocial.RISocialPublishExperimentReport;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.tosocial.RISocialRemoveAllPublications;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.tosocial.RISocialRemovePublications;
import fr.lgi2a.similar.microkernel.examples.concepts.influences.tosocial.RISocialReplaceEditorInChief;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;
import fr.lgi2a.similar.microkernel.libs.abstractimplementation.AbstractLevel;

/**
 * Models the 'Social' level of the simulation.
 * 
 * <h1>Levels in the SIMILAR API suite.</h1>
 * <p>
 * 	In the micro-kernel of SIMILAR, a level is implemented as an 
 * 	instance of either the {@link ILevel} interface, or of the {@link AbstractLevel} 
 * 	abstract class.
 * 	In this example, we use the abstract class which is easier to implement.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class SocialLevel extends AbstractLevel {
	/**
	 * Builds an initialized instance of the 'social' level.
	 * @param initialTime The initial time stamp of the simulation. This value is provided during 
	 * the initialization process of a simulation.
	 */
	public SocialLevel(
			SimulationTimeStamp initialTime
	) {
		super( initialTime, ConceptsSimulationLevelIdentifiers.SOCIAL_LEVEL );
		//
		// Build the perception relation graph of the level, i.e. identify the level that can be perceived
		// by the agents lying in this level.
		//
		// The agents of the 'social' level only perceive data inside the 'social' level. Thus no modifications
		// are applied to the graph.
		//
		// Build the influence relation graph of the level, i.e. identify the level that can be influenced
		// by the agents lying in this level.
		//
		// The agents of the 'social' level only influence the 'social' level. Thus no modifications
		// are applied to the graph.
	}

	// // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
	//
	//
	//	TIME MODEL RELATED METHOD
	//
	//
	//

	/**
	 * This method determines the next time when the next reaction will be performed for this level.
	 * @return The next time a reaction will be performed for this model.
	 */
	@Override
	public SimulationTimeStamp getNextTime( SimulationTimeStamp currentTime ) {
		// In this level, the times moves from evening to evening.
		return ConceptsSimulationTimeInterpretationModel.getInstance().getNext( currentTime, TimeOfTheDay.EVENING );
	}

	// // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
	//
	//
	//	METHOD RELATED TO THE REACTION TO SYSTEM INFLUENCES
	//
	//
	//

	/**
	 * Performs a user-defined reaction to system influences.
	 * <p>
	 * 	In SIMILAR, the reaction is the process moving the public local dynamic state of a level from its state
	 * 	at a specific time stamp to its state at its next time stamp.
	 * </p>
	 * <h1>Calls to this method</h1>
	 * This method is called twice during the reaction phase of a level:
	 * <ol>
	 * 	<li>
	 * 		After the reaction of the simulation engine to the system influences, but before the reaction to the regular influences
	 *	</li>
	 * 	<li>
	 * 		After the reaction of the simulation engine to the system influences that were produced by the reaction to the regular influences.
	 *	</li>
	 * </ol>
	 * <p>
	 * 	It provides to users the possibility to write a customized reaction to system influences.
	 * </p>
	 * @param previousConsistentStateTime The time stamp of the last time a reaction was computed for this level.
	 * @param newConsistentStateTime The time stamp of when this reaction is computed for this level.
	 * @param consistentState The public dynamic local state of the level being updated by the reaction to reach its new state.
	 * @param systemInfluencesToManage The system influences that were managed by the simulation engine during the current reaction right before the
	 * call to this method.
	 * @param happensBeforeRegularReaction <code>true</code> if this method is called in the first situation of 
	 * the enumeration (see the documentation of the method).
	 * @param newInfluencesToProcess The data structure where the influences that were produced by this user-defined reaction are put.
	 */
	@Override
	public void makeSystemReaction(
			SimulationTimeStamp previousConsistentStateTime,
			SimulationTimeStamp newConsistentStateTime,
			ConsistentPublicLocalDynamicState consistentState,
			Collection<IInfluence> systemInfluencesToManage,
			boolean happensBeforeRegularReaction,
			Collection<IInfluence> newInfluencesToProcess
	) {
		// The 'Social' level does not need a user-defined reaction to system influences.
	}

	// // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // // //
	//
	//
	//	METHOD RELATED TO THE REACTION TO REGULAR INFLUENCES
	//
	//
	//

	/**
	 * Performs a user-defined reaction to the regular influences.
	 * <p>
	 * 	In SIMILAR, the reaction is the process moving the public local dynamic state of a level from its state
	 * 	at a specific time stamp to its state at its next time stamp.
	 * </p>
	 * <h1>Usage</h1>
	 * <p>
	 * 	This reaction reads the influences contained in the <code>regularInfluencesOftransitoryStateDynamics</code>
	 * 	argument and performs operations into the public local dynamic state of the level (<code>consistentState</code> 
	 * 	argument), or in the public local state of the environment or agents it contains.
	 * </p>
	 * <p>
	 * 	The influence can either be consumed by the reaction (they disappear from the simulation) or can persist after the reaction
	 *  (if the influence models an unfinished action when the reaction ends).
	 *  In the second case, the influence has to be included into the <code>remainingInfluences</code> set.
	 * </p>
	 * <p>
	 * 	If the reaction produces other influences, these influences have to be added to the <code>remainingInfluences</code> set.
	 * 	Note that the system influences aimed at levels currently performing their reaction are managed right after the end of the call 
	 * 	to this method. Otherwise, these influences are managed during the next reaction of the level they are aimed at.
	 * </p>
	 * @param previousConsistentStateTime The time stamp of the last time a reaction was computed for this level.
	 * @param newConsistentStateTime The time stamp of when this reaction is computed for this level.
	 * @param consistentState The public dynamic local state of the level being updated by the reaction to reach its new state.
	 * @param regularInfluencesOftransitoryStateDynamics The <b>regular</b> influences that have to be managed by this reaction to go from the 
	 * previous consistent state to the next consistent state of the level.
	 * @param remainingInfluences The set that will contain the influences that were produced by the user during the invocation of 
	 * this method, or the influences that persist after this reaction.
	 */
	@Override
	public void makeRegularReaction(
			SimulationTimeStamp previousConsistentStateTime,
			SimulationTimeStamp newConsistentStateTime,
			ConsistentPublicLocalDynamicState consistentState,
			Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
			Set<IInfluence> remainingInfluences
	) {
		//
		// The management of the reaction to the RISocialChangeBroadcast is special since
		// when an Editor in chief goes mad, this reaction has to solve a conflict between
		// the value provided by the mad editor in chief, and the value provided by the FBI to
		// correct the behavior of the editor in chief.
		// Consequently, instead of managing individually the RISocialChangeBroadcast influences
		// their management is grouped.
		//
		// The same problem goes with the addition of posts on the Internet (RISocialPublishExperimentReport influences)
		// and the deletion of posts with the RISocialRemoveAllPublications and RISocialRemovePublications influences.
		//
		// Declare the set where the RISocialChangeBroadcast influences are grouped together.
		Set<RISocialChangeBroadcast> broadcastInfluences = new LinkedHashSet<RISocialChangeBroadcast>();
		Set<RISocialPublishExperimentReport> publishInfluences = new LinkedHashSet<RISocialPublishExperimentReport>();
		Set<IInfluence> censorshipInfluences = new LinkedHashSet<IInfluence>();
		for( IInfluence influence : regularInfluencesOftransitoryStateDynamics ){
			// Dispatch the management of the influence to the appropriate methods.
			if( RISocialChangeBroadcast.CATEGORY.equals( influence.getCategory() ) ){
				// The management of this influence is performed in a group to solve conflicts.
				broadcastInfluences.add( (RISocialChangeBroadcast) influence );
			} else if( RISocialPublishExperimentReport.CATEGORY.equals( influence.getCategory() ) ) {
				// The management of this influence is performed in a group to solve conflicts.
				publishInfluences.add( (RISocialPublishExperimentReport) influence );
			} else if( RISocialReplaceEditorInChief.CATEGORY.equals( influence.getCategory() ) ) {
				// Dispatch the management of the influence.
				this.reactTo( 
						(RISocialReplaceEditorInChief) influence, 
						remainingInfluences 
				);
			} else if( RISocialRemoveAllPublications.CATEGORY.equals( influence.getCategory() ) ) {
				// The management of this influence is performed in a group to solve conflicts.
				censorshipInfluences.add( influence );
			} else if( RISocialRemovePublications.CATEGORY.equals( influence.getCategory() ) ) {
				// The management of this influence is performed in a group to solve conflicts.
				censorshipInfluences.add( influence );
			} else {
				// This case is out of the bounds of the behavior of the reaction.
				// Consequently, we throw an exception telling that this case should not happen
				// in an appropriate simulation.
				throw new UnsupportedOperationException( "Cannot manage the influence '" + influence.getCategory() + "' in the reaction " +
						"of the level '" + this.getIdentifier() + "'." );
			}
		}
		//
		// Then perform a grouped management of the broadcast influences.
		//
		if( ! broadcastInfluences.isEmpty() ){
			EnvPLSSocial envState = (EnvPLSSocial) consistentState.getPublicLocalStateOfEnvironment();
			this.managedGroupedChangeBroadcastInfluences( broadcastInfluences, envState );
		}
		//
		// Then perform a grouped management of the post publication/censorship influences.
		//
		this.manageGroupedPostsOnTheInternetInfluences(
				publishInfluences, 
				censorshipInfluences, 
				consistentState
		);
	}
	
	/**
	 * Solves the possible conflicts between a set of influences and modifies the public local dynamic state of the simulation
	 * accordingly.
	 * <h1>Conflicts</h1>
	 * <p>
	 * 	The management of the reaction to the {@link RISocialPublishExperimentReport}, {@link RISocialRemoveAllPublications} and
	 *  {@link RISocialRemovePublications} influences is special, since this reaction has to solve a conflict between
	 * 	the addition of posts and the removal of posts.
	 * 	Consequently, instead of managing individually the influences their management is grouped and favors removal:
	 * 	the posts added by the {@link RISocialPublishExperimentReport} are removed by the {@link RISocialRemovePublications} and
	 * 	{@link RISocialRemoveAllPublications} when possible.
	 * </p>
	 * @param publishInfluences The set of influences where a conflict can happen.
	 * @param censorshipInfluences The set of influences where a conflict can happen.
	 * @param consistentState The public dynamic local state of the level being updated by the reaction to reach its new state.
	 */
	private void manageGroupedPostsOnTheInternetInfluences( 
			Set<RISocialPublishExperimentReport> publishInfluences,
			Set<IInfluence> censorshipInfluences,
			ConsistentPublicLocalDynamicState consistentState
	) {
		// First manage the post addition influences.
		for( RISocialPublishExperimentReport influence : publishInfluences ){
			this.reactTo(
					influence, 
					consistentState
			);
		}
		// Then manage the post censorship influences.
		for( IInfluence influence : censorshipInfluences ) {
			if( RISocialRemoveAllPublications.CATEGORY.equals( influence.getCategory() ) ) {
				this.reactToRemoveAllPublications(
						consistentState
				);
			} else if( RISocialRemovePublications.CATEGORY.equals( influence.getCategory() ) ) {
				this.reactTo(
						(RISocialRemovePublications) influence, 
						consistentState
				);
			} else {
				throw new UnsupportedOperationException( "Cannot manage the reaction to '" + influence.getCategory() + "' influences." );
			}
		}
	}
	
	/**
	 * Solves the possible conflicts between a set of influences and modifies the public local dynamic state of the simulation
	 * accordingly.
	 * <h1>Conflicts</h1>
	 * <p>
	 * 	The management of the reaction to the {@link RISocialChangeBroadcast} influence is special since
	 * 	when an Editor in chief goes mad, this reaction has to solve a conflict between
	 * 	the value provided by the mad editor in chief, and the value provided by the FBI to
	 * 	correct the behavior of the editor in chief.
	 * 	Consequently, instead of managing individually the RISocialChangeBroadcast influences
	 * 	their management is grouped.
	 * </p>
	 * @param broadcastInfluences The set of influences where a conflict can happen.
	 * @param envPublicLocalState The public local state of the environment being modified in the reaction.
	 */
	private void managedGroupedChangeBroadcastInfluences( 
			Set<RISocialChangeBroadcast> broadcastInfluences,
			EnvPLSSocial envPublicLocalState
	) {
		// The value that has to be broadcasted.
		int valueToBroadCast = Integer.MAX_VALUE;
		// Tells that the FBI did set the value.
		boolean valueSetByTheFBI = false;
		for( RISocialChangeBroadcast influence : broadcastInfluences ){
			if( AgtFBI.CATEGORY.equals( influence.getAuthorCategory() ) ){
				valueSetByTheFBI = true;
				valueToBroadCast = influence.getNewThreshold();
			} else if( ! valueSetByTheFBI ) {
				// If the FBI did not meddle with broadcast, always take into consideration the lowest threshold.
				valueToBroadCast = Math.min( valueToBroadCast, influence.getNewThreshold() );
			}
		}
		// Modify the broadcasted value in the TV program.
		envPublicLocalState.setTvBroadcastedThresholdForStrangePhysicalManifestations( valueToBroadCast );
	}
	
	/**
	 * Manages the reaction to a {@link RISocialPublishExperimentReport} influence.
	 * @param influence The influence which reaction is managed by this method call.
	 * @param consistentState The public dynamic local state of the level being updated by the reaction to reach its new state.
	 */
	private void reactTo(
			RISocialPublishExperimentReport influence,
			ConsistentPublicLocalDynamicState consistentState
	){
		//
		// In reaction to this influence, the post is included in the Internet (public local state of the environment).
		//
		// First get the casted public local state of the environment.
		EnvPLSSocial castedEnv = (EnvPLSSocial) consistentState.getPublicLocalStateOfEnvironment();
		castedEnv.addPost( influence.getPost() );
	}
	
	/**
	 * Manages the reaction to a {@link RISocialReplaceEditorInChief} influence.
	 * @param influence The influence which reaction is managed by this method call.
	 * @param remainingInfluences The set that will contain the influences that were produced by the user during the invocation of 
	 * this method, or the influences that persist after this reaction.
	 */
	private void reactTo(
			RISocialReplaceEditorInChief influence,
			Set<IInfluence> remainingInfluences
	){
		//
		// In reaction to this influence, the current editor in chief is removed from the simulation and the new one is added to the simulation.
		//
		// First remove the current editor in chief from the simulation. This task is performed by using a system reaction.
		// Since we want to remove the agent before the end of this reaction, the system influence has to be managed in the system reaction
		// directly following this reaction to regular influences. This is achieved by aiming the influence at this level.
		SystemInfluenceRemoveAgent removeAgtInfluence = new SystemInfluenceRemoveAgent( this.getIdentifier(), influence.getEditorInChiefToReplace() );
		// Add the influence to the influence to manage.
		remainingInfluences.add( removeAgtInfluence );
		// Then add the new editor in chief to the simulation. This task is also performed using a system reaction.
		SystemInfluenceAddAgent addAgtInfluence = new SystemInfluenceAddAgent( this.getIdentifier(), influence.getNewEditorInChief() );
		// Add the influence to the influence to manage.
		remainingInfluences.add( addAgtInfluence );
	}
	
	/**
	 * Manages the reaction to a {@link RISocialRemoveAllPublications} influence.
	 * @param consistentState The public dynamic local state of the level being updated by the reaction to reach its new state.
	 */
	private void reactToRemoveAllPublications(
			ConsistentPublicLocalDynamicState consistentState
	){
		//
		// In reaction to this influence, all the posts are removed from the Internet (public local state of the environment).
		//
		// First get the casted public local state of the environment.
		EnvPLSSocial castedEnv = (EnvPLSSocial) consistentState.getPublicLocalStateOfEnvironment();
		// Then remove all the posts on the Internet.
		castedEnv.removeAllPostsFromTheInternet( );
	}
	
	/**
	 * Manages the reaction to a {@link RISocialRemovePublications} influence.
	 * @param influence The influence which reaction is managed by this method call.
	 * @param consistentState The public dynamic local state of the level being updated by the reaction to reach its new state.
	 */
	private void reactTo(
			RISocialRemovePublications influence,
			ConsistentPublicLocalDynamicState consistentState
	){
		//
		// In reaction to this influence, all the posts of the citizen are removed from the Internet (public local state of the environment).
		//
		// First get the casted public local state of the environment.
		EnvPLSSocial castedEnv = (EnvPLSSocial) consistentState.getPublicLocalStateOfEnvironment();
		// Then remove the posts of the citizen.
		castedEnv.removeAllPostsOfCitizen( influence.getCitizen() );
	}
}
