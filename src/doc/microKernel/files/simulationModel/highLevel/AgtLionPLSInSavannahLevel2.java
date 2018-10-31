package fr.univ_artois.lgi2a.wildlifesimulation.model.agents.lion.savannah;

import java.awt.geom.Point2D;

import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractLocalStateOfAgent;
import fr.univ_artois.lgi2a.wildlifesimulation.model.levels.WildlifeLevelList;

/**
 * The public local state of an agent from the "Lion" category in the level 
 * identified by "Savannah".
 */
public class AgtLionPLSInSavannahLevel2 extends AbstractLocalStateOfAgent {
   /**
    * Builds an initialized instance of this public local state.
    * @param owner The agent owning this public local state. This value cannot 
    * be <code>null</code>.
    * @param initialX The initial x coordinate of the agent.
    * @param initialY The initial y coordinate of the agent.
    * @param initialAge The initial age of the agent, in days. This value 
    * cannot be negative.
    * @throws IllegalArgumentException If an argument had an inappropriate value.
    */
   public AgtLionPLSInSavannahLevel2(
      IAgent4Engine owner,
      double initialX,
      double initialY,
      long initialAge
   ){
      super( 
         WildlifeLevelList.SAVANNAH, 
         owner 
      );
      this.coordinates = new Point2D.Double( );
      this.setCoordinates( initialX, initialY );
      this.setAge( initialAge );
   }

   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //
   // // 
   // //   Declaration of the perceptible data of the agent
   // //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //
   // //  //   //  // //  //   //  // //  //   //  // //  //   //  // //  //   //

   /**
    * The coordinates of the agent in the grid of the "Savannah" level.
    */
   private Point2D.Double coordinates;

   /**
    * Gets the coordinates of the agent in the grid of the "Savannah" level.
    * @return The coordinates of the agent in the grid of the "Savannah" level.
    */
   public Point2D.Double getCoordinates( ){
      return this.coordinates;
   }

   /**
    * Sets the new coordinates of the agent in the grid of the "Savannah" level.
    * @param newX The new x coordinate of the agent in the grid.
    * @param newY The new y coordinate of the agent in the grid.
    */
   public void setCoordinates( double newX, double newY ){
      this.coordinates.setLocation( newX, newY );
   }
   
   /**
    * The age of the agent, in days.
    */
   private long age;
   
   /**
    * Gets the age of the agent, in days.
    * @return The age of the agent.
    */
   public long getAge( ) {
      return this.age;
   }
   
   /**
    * Sets the agent of the agent to a new value.
    * @param newAge The new age of the agent, in days.
    * @throws IllegalArgumentException If the age is negative.
    */
   public void setAge( long newAge ) {
      if( newAge < 0 ){
         throw new IllegalArgumentException(
            "The age of the agent cannot be negative."
         );
      } else {
         this.age = newAge;
      }
   }
}
