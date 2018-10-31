package fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.environment;

import java.awt.geom.Point2D;
import java.util.Map;

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.univ_artois.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.univ_artois.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.BubbleChamberAgentCategoriesList;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.bubble.AgtBubbleFactory;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.particle.chamber.AgtParticlePLSInChamber;
import fr.univ_artois.lgi2a.similar.microkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.univ_artois.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.univ_artois.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractEnvironment;

/**
 * The environment used in the "Bubble chamber" simulation.
 */
public class BubbleChamberEnvironment extends AbstractEnvironment {
   /**
    * {@inheritDoc}
    */
   @Override
   public void natural(
      LevelIdentifier level,
      SimulationTimeStamp timeLowerBound,
      SimulationTimeStamp timeUpperBound,
      Map<LevelIdentifier, ILocalStateOfEnvironment> publicLocalStates,
      ILocalStateOfEnvironment privateLocalState,
      IPublicDynamicStateMap dynamicStates,
      InfluencesMap producedInfluences
   ) {
      if( level.equals( BubbleChamberLevelList.CHAMBER ) ){
         this.naturalForChamberLevel(
            timeLowerBound,
            timeUpperBound, 
            publicLocalStates,
            privateLocalState,
            dynamicStates,
            producedInfluences
         );
      } else {
         throw new UnsupportedOperationException( 
            "The '" + level + "' does not belong to the " +
            "initial specification of the 'bubble chamber' simulation." 
         );
      }
   }
   
   /**
    * The natural action of the environment for the "Chamber" level.
    */
   public void naturalForChamberLevel(
      SimulationTimeStamp timeLowerBound,
      SimulationTimeStamp timeUpperBound,
      Map<LevelIdentifier, ILocalStateOfEnvironment> publicLocalStates,
      ILocalStateOfEnvironment privateLocalState,
      IPublicDynamicStateMap dynamicStates,
      InfluencesMap producedInfluences
   ) {
      // First get the dynamic state of the "Chamber" level, to list the 
      // particles
      IPublicLocalDynamicState dynamicState = dynamicStates.get( 
         BubbleChamberLevelList.CHAMBER 
      );
      // Search for particles among the agents lying in that state.
      for( ILocalStateOfAgent state : dynamicState.getPublicLocalStateOfAgents() ){
         // Check if the agent is a particle.
         if( state.getCategoryOfAgent().isA( BubbleChamberAgentCategoriesList.PARTICLE ) ){
            // Cast the public local state into the appropriate type.
            AgtParticlePLSInChamber castedState = (AgtParticlePLSInChamber) state;
            // Get the location of the particle.
            Point2D location = castedState.getLocation();
            // Create a "bubble" agent at that location.
            IAgent4Engine bubbleAgent = AgtBubbleFactory.generate(
                  location.getX(), 
                  location.getY()
            );
            // Add the "Bubble" agent to the simulation.
            SystemInfluenceAddAgent addInfluence = new SystemInfluenceAddAgent( 
                  BubbleChamberLevelList.CHAMBER, 
                  timeUpperBound,
                  timeLowerBound,
                  bubbleAgent
            );
            producedInfluences.add( addInfluence );
         }
      }
   }
}