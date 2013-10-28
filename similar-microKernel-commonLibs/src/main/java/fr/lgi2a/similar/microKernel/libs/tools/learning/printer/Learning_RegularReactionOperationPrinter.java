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
package fr.lgi2a.similar.microKernel.libs.tools.learning.printer;

import static fr.lgi2a.similar.microKernel.libs.tools.learning.Learning_TracePrinter.printIndentation;
import fr.lgi2a.similar.microKernel.I_Influence;
import fr.lgi2a.similar.microKernel.libs.tools.learning.Learning_TracePrinter;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.Learning_EngineOperation;
import fr.lgi2a.similar.microKernel.libs.tools.learning.simulationTrace.operations.Learning_EngineOperation_RegularReaction;

/**
 * An element of the engine operation printing chain of responsibility.
 * This object displays the content of the 'system reaction' operation performed by a level.
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class Learning_RegularReactionOperationPrinter extends Learning_EngineOperationPrinter {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canPrint( Learning_EngineOperation operation ) {
		return operation instanceof Learning_EngineOperation_RegularReaction;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void print( int indentation, Learning_EngineOperation operation ) {
		Learning_EngineOperation_RegularReaction castedop = (Learning_EngineOperation_RegularReaction) operation;
		printIndentation( indentation );
		System.out.println( "User-defined reaction to the regular influences of the level '" + castedop.getNewConsistentStateAtBeginning().getLevel() + "':" );
		printIndentation( indentation + 1 );
		System.out.println( "This reaction is performed for the following transitory period of the level:" );
		printIndentation( indentation + 2 );
		System.out.println( "From " +  castedop.getPreviousConsistentStateTime() + " to " + castedop.getNewConsistentStateTime() );
		printIndentation( indentation + 1 );
		System.out.println( "Public local state of the level before the user-defined reaction:" );
		Learning_TracePrinter.printLocalDynamicState( indentation + 2, castedop.getNewConsistentStateAtBeginning() );
		printIndentation( indentation + 1 );
		System.out.println( "The regular influences for which the user-defined reaction is measured were:" );
		if( castedop.getRegularInfluencesOftransitoryStateDynamicsArgument().isEmpty() ){
			printIndentation( indentation + 2 );
			System.out.println( "NO INFLUENCES" );
		} else {
			for( I_Influence influence : castedop.getRegularInfluencesOftransitoryStateDynamicsArgument() ){
				printIndentation( indentation + 2 );
				System.out.println( influence );
			}
		}
		printIndentation( indentation + 1 );
		System.out.println( "Public local state of the level after the user-defined reaction:" );
		Learning_TracePrinter.printLocalDynamicState( indentation + 2, castedop.getNewConsistentStateAtEnd() );
		printIndentation( indentation + 1 );
		System.out.println( "Influences that were produced by the user-defined reaction:" );
		if( castedop.getProducedInfluences().isEmpty() ){
			printIndentation( indentation + 2 );
			System.out.println( "NO INFLUENCES" );
		} else {
			for( I_Influence influence : castedop.getProducedInfluences() ){
				printIndentation( indentation + 2 );
				System.out.println( influence );
			}
		}
	}
}
