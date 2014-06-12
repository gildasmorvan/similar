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
package fr.lgi2a.similar.microkernel.examples.bubblechamber.probes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicLocalDynamicState;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.BubbleChamberAgentCategoriesList;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.bubble.chamber.AgtBubblePLSInChamber;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.agents.cannon.external.AgtCannonPLSInExternal;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.environment.chamber.EnvPLSInChamber;
import fr.lgi2a.similar.microkernel.examples.bubblechamber.model.levels.BubbleChamberLevelList;
import fr.lgi2a.similar.microkernel.influences.IInfluence;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceRemoveAgent;
import fr.lgi2a.similar.microkernel.libs.probes.AbstractProbeImageSwingJPanel;

/**
 * The probe displaying the content of the "liquid" level of the bubble chamber simulation using a 2 dimensional image.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class ChamberLevelSwingViewer extends AbstractProbeImageSwingJPanel {

	/**
	 * The size in pixels of a simulation distance unit.
	 */
	private static final int MULTIPLICATION_FACTOR = 5;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Dimension computeSimulationImageDimensions(
			IPublicDynamicStateMap dynamicStateMap, SimulationTimeStamp timeStamp) {
		// Get the dimensions of the bubble chamber.
		EnvPLSInChamber chamberLevel = (EnvPLSInChamber) dynamicStateMap.get(BubbleChamberLevelList.CHAMBER).getPublicLocalStateOfEnvironment();
		return new Dimension( 
				MULTIPLICATION_FACTOR * ((int) chamberLevel.getBounds().getWidth() +1),
				MULTIPLICATION_FACTOR * ((int) chamberLevel.getBounds().getHeight() +1)
				);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateGraphics(IPublicDynamicStateMap dynamicStateMap,
			SimulationTimeStamp timeStamp, Graphics2D graphics, int imgWidth, 
			int imgHeight) {
		// First draw the background.
		graphics.setColor( Color.LIGHT_GRAY );
		graphics.fillRect( 0, 0, imgWidth, imgHeight );
		// Then draw the agents.
		IPublicLocalDynamicState chamberLevel = dynamicStateMap.get(BubbleChamberLevelList.CHAMBER);
		IPublicLocalDynamicState externalLevel = dynamicStateMap.get(BubbleChamberLevelList.EXTERNAL);
		//Draw the bubbles
		for(ILocalStateOfAgent agentPLS : chamberLevel.getPublicLocalStateOfAgents()) {
			if(agentPLS.getCategoryOfAgent().equals(BubbleChamberAgentCategoriesList.BUBBLE)) {
				AgtBubblePLSInChamber bubblePLS = (AgtBubblePLSInChamber) agentPLS;
				graphics.setColor( Color.WHITE );
				graphics.fillOval( 
					(int) Math.floor( MULTIPLICATION_FACTOR * ( bubblePLS.getLocation().getX() - 0.5 ) ), 
					(int) Math.floor( MULTIPLICATION_FACTOR * ( bubblePLS.getLocation().getY() - 0.5 ) ), 
					MULTIPLICATION_FACTOR, 
					MULTIPLICATION_FACTOR 
				);
				graphics.setColor( Color.BLACK );
				graphics.drawOval(
					(int) Math.floor( MULTIPLICATION_FACTOR * ( bubblePLS.getLocation().getX() - 0.5 ) ), 
					(int) Math.floor( MULTIPLICATION_FACTOR * ( bubblePLS.getLocation().getY() - 0.5 ) ), 
					MULTIPLICATION_FACTOR, 
					MULTIPLICATION_FACTOR 
				);
			}
		}
		
		// We also want to print an animation for the bubbles that disappeared the previous time.
		// We look into the previous temporary influences the ones corresponding to the removal
		//of the physical state of a bubble, and we draw that bubble.
		
		for(IInfluence influence : chamberLevel.getSystemInfluencesOfStateDynamics()) {
			if(influence.getCategory().equals(SystemInfluenceRemoveAgent.CATEGORY)) {
				SystemInfluenceRemoveAgent castedInfluence = (SystemInfluenceRemoveAgent) influence;
				if(castedInfluence.getAgent().getCategory().equals(BubbleChamberAgentCategoriesList.BUBBLE)) {
					AgtBubblePLSInChamber bubblePLS = (AgtBubblePLSInChamber) castedInfluence.getAgent().getPublicLocalState(BubbleChamberLevelList.CHAMBER);
					graphics.setColor( Color.WHITE );
					graphics.fillOval( 
						(int) Math.floor( MULTIPLICATION_FACTOR * ( bubblePLS.getLocation().getX() - 0.25 ) ), 
						(int) Math.floor( MULTIPLICATION_FACTOR * ( bubblePLS.getLocation().getY() - 0.25 ) ), 
						MULTIPLICATION_FACTOR / 2, 
						MULTIPLICATION_FACTOR / 2
					);
					graphics.setColor( Color.BLACK );
					graphics.drawOval(
						(int) Math.floor( MULTIPLICATION_FACTOR * ( bubblePLS.getLocation().getX() - 0.25 ) ), 
						(int) Math.floor( MULTIPLICATION_FACTOR * ( bubblePLS.getLocation().getY() - 0.25 ) ), 
						MULTIPLICATION_FACTOR / 2, 
						MULTIPLICATION_FACTOR / 2
					);
				}
			}
		}
		
		// Finally, we print the canons.
		for(ILocalStateOfAgent agentPLS : externalLevel.getPublicLocalStateOfAgents()) {
			if(agentPLS.getCategoryOfAgent().equals(BubbleChamberAgentCategoriesList.CANNON)) {
				AgtCannonPLSInExternal cannonPLS = (AgtCannonPLSInExternal) agentPLS;
				
				Polygon canonPolygon = new Polygon( );
				canonPolygon.addPoint( (int) Math.floor(cannonPLS.getEntryPointInChamber( ).getX()), (int) Math.floor(MULTIPLICATION_FACTOR * (cannonPLS.getEntryPointInChamber( ).getY() - 1 )) );
				canonPolygon.addPoint( (int) Math.floor( MULTIPLICATION_FACTOR * cannonPLS.getEntryPointInChamber( ).getX()), (int) Math.floor(MULTIPLICATION_FACTOR * ( cannonPLS.getEntryPointInChamber( ).getY()  + 1 ) ) );
				canonPolygon.addPoint( (int) Math.floor( MULTIPLICATION_FACTOR * cannonPLS.getEntryPointInChamber( ).getX() + 20), (int) Math.floor(MULTIPLICATION_FACTOR * ( cannonPLS.getEntryPointInChamber( ).getY()  + 1 ) ) );
				canonPolygon.addPoint( (int) Math.floor( MULTIPLICATION_FACTOR * cannonPLS.getEntryPointInChamber( ).getX() + 20), (int) Math.floor(MULTIPLICATION_FACTOR * ( cannonPLS.getEntryPointInChamber( ).getY() - 1 ) ) );
				graphics.setColor( Color.RED );
				graphics.fillPolygon( canonPolygon );
			}
		}
		
	}

}
