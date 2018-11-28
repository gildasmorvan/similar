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
 * 		  hassane.abouaissa@univ-artois.fr
 * 
 * Contributors:
 * 	Hassane ABOUAISSA (designer)
 * 	Gildas MORVAN (designer, creator of the IRM4MLS formalism)
 * 	Yoann KUBERA (designer, architect and developer of SIMILAR)
 * 
 * This software is a computer program whose purpose is run road traffic
 * simulations using a dynamic hybrid approach.
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
package fr.univ_artois.lgi2a.similar.extendedkernel.libs.random;

import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

/**
 * The random values factory used in the simulation.
 * <p>
 *	By default, this factory uses a strategy based on a XoRoRNG instance.
 * </p>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan/" target="_blank">Gildas Morvan</a>
 */
public final class PRNG {
	
	
	private static final int SHUFFLE_THRESHOLD = 5;
	
	/**
	 * The random values generation strategy currently used in the simulation.
	 * The default strategy is based on a XoRoRNG instance.
	 */
	private static RandomGeneratorWrapper instance = new RandomGeneratorWrapper(RandomGeneratorWrapper.XORO);
	
	/**
	 * Private Constructor to prevent class instantiation.
	 */
	private PRNG() {	
	}
	
	/**
	 * Sets the random value generation strategy used in the simulation.
	 * @param strategy The random value generation strategy used in the simulation.
	 */
	public static void set( RandomGeneratorWrapper  strategy ) {
		if( strategy != null ) {
			instance = strategy ;
		}
	}

	/**
	 * @return the random value generation strategy used in the simulation.
	 */
	private static RandomGeneratorWrapper get( ) {
		return instance;
	}
	
	/**
	 * Gets a random number between 0 (included) and 1 (excluded).
	 * @return A random number between 0 (included) and 1 (excluded).
	 */
	public static double randomDouble() {
		return get().getRandom().nextDouble();
	}
	
	/**
	 * Generates a random double within a range.
	 * @param lowerBound The lower bound of the generation (included).
	 * @param higherBound The higher bound of the generation (excluded).
	 * @return A random double within the range <code>[lowerBound, higherBound[</code>.
	 * @throws IllegalArgumentException If <code>lowerBound</code> is 
	 * higher or equal to <code>higherBound</code>.
	 */
	public static double randomDouble(
			double lowerBound, 
			double higherBound
	) {
		if( lowerBound >= higherBound ) {
			throw new IllegalArgumentException( "The lower bound " + lowerBound + " is greater " +
					"or equal to the higher bound " + higherBound  );
		}
		return (higherBound - lowerBound) * get().getRandom().nextDouble() + lowerBound;
	}

	/**
	 * Gets a random angle between -pi (included) and pi (excluded).
	 * @return a random angle between -pi (included) and pi (excluded).
	 */
	public static double randomAngle() {
		return Math.PI - get().getRandom().nextDouble() * 2 * Math.PI;
	}

	/**
	 * Gets a random boolean.
	 * @return A random boolean.
	 */
	public static boolean randomBoolean() {
		return get().getRandom().nextBoolean();
	}

	/**
	 * Gets a random integer from 0 to bound - 1.
	 * @param bound the (excluded) upper bound.
	 * @return A random integer.
	 */
	public static int randomInt(int bound) {
		return get().getRandom().nextInt(bound);
	}
	
	/**
	 * Gets -1 or +1.
	 * @return -1 or +1.
	 */
	public static int randomSign() {
		return get().getRandom().nextBoolean() ? 1 : -1;
	}

	/**
	 * Gets a Gaussian ("normally") distributed double value with mean 0.0 and standard deviation 1.0.
	 * @return A Gaussian ("normally") distributed double value with mean 0.0 and standard deviation 1.0.
	 */
	public static double randomGaussian() {
		return get().getRandom().nextGaussian();
	}
	
	/**
	 * Gets a Gaussian ("normally") distributed double value
	 * with a given mean and standard deviation.
	 * @param mean the mean.
	 * @param sd the standard deviation.
	 * @return A Gaussian ("normally") distributed double value with a given mean and standard deviation.
	 */
	public static double randomGaussian(double mean, double sd) {
		return get().getRandom().nextGaussian()*sd+mean;
	}
	
	/**
	 *  Shuffles the given collection.
	 *  
	 * @param list the collection to shuffle
	 */
	@SuppressWarnings({"rawtypes", "unchecked"})
	public static void shuffle(List<?> list) {
		int size = list.size();
        if (size < SHUFFLE_THRESHOLD || list instanceof RandomAccess) {
            for (int i=size; i>1; i--) {
            		swap(list, i-1, get().getRandom().nextInt(i));
            }
        } else {
            Object[] arr = list.toArray();

            // Shuffle array
            for (int i=size; i>1; i--) {
            		swap(arr, i-1, get().getRandom().nextInt(i));
            }

            // Dump array back into list
            // instead of using a raw type here, it's possible to capture
            // the wildcard but it will require a call to a supplementary
            // private method
            ListIterator it = list.listIterator();
            for (int i=0; i<arr.length; i++) {
                it.next();
                it.set(arr[i]);
            }
        }

	}
	
    
	 /**
     * Swaps the elements at the specified positions in the specified list.
     * (If the specified positions are equal, invoking this method leaves
     * the list unchanged.)
     *
     * @param list The list in which to swap elements.
     * @param i the index of one element to be swapped.
     * @param j the index of the other element to be swapped.
     * @throws IndexOutOfBoundsException if either <tt>i</tt> or <tt>j</tt>
     *         is out of range (i &lt; 0 || i &gt;= list.size()
     *         || j &lt; 0 || j &gt;= list.size()).
     * @since 1.4
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void swap(List<?> list, int i, int j) {
        // instead of using a raw type here, it's possible to capture
        // the wildcard but it will require a call to a supplementary
        // private method
        final List l = list;
        l.set(i, l.set(j, l.get(i)));
    }
    
    /**
     * Swaps the two specified elements in the specified array.
     */
    private static void swap(Object[] arr, int i, int j) {
        Object tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}
