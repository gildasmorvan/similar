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
package fr.lgi2a.similar.microkernel.dynamicstate;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * This class models a set as the union of two other sets. The union is dynamic: an element added to one set of the 
 * union is automatically added to the union. The same goes for removal.
 * 
 * <h2>Warning</h2>
 * <p>
 * 	This implementation assumes that the two sets are disjoint.
 * </p>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class ViewOnSetUnion<E> extends AbstractSet<E> {
	/**
	 * The first set of the union.
	 */
	private Set<? extends E> set1;
	/**
	 * The second set of the union.
	 */
	private Set<? extends E> set2;
	
	/**
	 * Builds a view on the dynamic union of two sets.
	 * @param set1 The first set of the union.
	 * @param set2 The second set of the union.
	 */
	public ViewOnSetUnion(
			Set<? extends E> set1,
			Set<? extends E> set2
	) {
		if( set1 == null || set2 == null ){
			throw new IllegalArgumentException( "The arguments cannot be null." );
		}
		this.set1 = set1;
		this.set2 = set2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<E> iterator() {
		return new SetUnionIterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return this.set1.size() + this.set2.size();
	}

	/**
	 * The object iterating over the two sets contained in an instance of the {@link ViewOnSetUnion} class.
	 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
	 */
	private class SetUnionIterator implements Iterator<E> {
		/**
		 * Identifies the set over which the iteration is currently being performed.
		 * -1 : iteration has not started yet.
		 * 0 : iteration currently on set 1
		 * 1 : iteration currently on set 2
		 */
		private int currentIndex;
		/**
		 * The iterator over a set of the union.
		 */
		private Iterator<? extends E> currentIterator;
		
		/**
		 * Builds an initialized iterator which first call to the next method will return the first item of the union.
		 */
		public SetUnionIterator( ) {
			this.currentIterator = null;
			this.currentIndex = -1;
		}
		
		/**
		 * {@inheritDoc}
		 */
		public boolean hasNext() {
			if( this.currentIndex == -1 ){
				return ! ViewOnSetUnion.this.isEmpty();
			} else if( this.currentIndex == 0 ) {
				return this.currentIterator.hasNext() || ! ViewOnSetUnion.this.set2.isEmpty();
			} else {
				return this.currentIterator.hasNext();
			}
		}
		
		/**
		 * {@inheritDoc}
		 */
		public E next() {
			if( ! this.hasNext() ){
				throw new NoSuchElementException( );
			} else {
				if( this.currentIterator == null || ! this.currentIterator.hasNext() ){
					// Case where the iteration has just started, or where the iteration over
					// the current set has finished.
					if( this.currentIndex == -1 ) {
						if( ViewOnSetUnion.this.set1.isEmpty() ){
							this.currentIndex = 1;
							this.currentIterator = ViewOnSetUnion.this.set2.iterator();
						} else {
							this.currentIndex = 0;
							this.currentIterator = ViewOnSetUnion.this.set1.iterator();
						}
					} else if( this.currentIndex == 0 ){
						this.currentIndex = 1;
						this.currentIterator = ViewOnSetUnion.this.set2.iterator();
					} else {
						throw new NoSuchElementException( "The set has no other elements." );
					}
				}
				return this.currentIterator.next();
			}
		}
		
		/**
		 * This operation is not supported.
		 */
		@Override
		public void remove() {
			if( this.currentIterator == null ){
				throw new IllegalStateException( "The iterator is not pointing to a valid item." );
			} else {
				this.currentIterator.remove();
			}
		}
	}
}
