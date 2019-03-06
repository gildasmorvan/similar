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
 * implementation of Logo-like simulations using the SIMILAR API.
 * This software defines an API to implement such simulations, and also 
 * provides usage examples.
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

import java.util.Random;

import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.RandomGeneratorFactory;
import org.apache.commons.math3.random.SynchronizedRandomGenerator;
import org.apache.commons.math3.random.Well1024a;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.rng.LightRNG;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.rng.PCG;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.rng.Xoshiro256StarStar;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.rng.Xoroshiro128Plus;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.rng.Xorshift128Plus;

/**
 * A convenient wrapper for RandomGenerator objects.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class RandomGeneratorWrapper {
	
    public static final String XOROSHIRO_128 = "xoroshiro128+";
    
    public static final String XORSHIFT_128 = "xorshift128+";
    
    public static final String XOROSHIRO_256 = "xoshiro256**";
    
    public static final String LIGHT = "SplitMix64";
    
    public static final String MT_64 = "MT19937-64";
    
    public static final String WELL_1024 = "Well1024a";
    
    public static final String PCG = "PCG";
    
    public static final String JDK = "jdk";
    
	protected RandomGenerator random;
	
	/**
	 * Build a new instance of this class with a given rng.
	 * @param random the rng to be used
	 */
	public RandomGeneratorWrapper(RandomGenerator random) {
		this.random = random;
	}
	
	/**
	 * Build a new instance of this class with a given rng.
	 * @param random the rng to be used
	 * @param sync <code>true</code> if the generator is synchronized
	 */
	public RandomGeneratorWrapper(RandomGenerator random, boolean sync) {
		if(sync) {
			this.random = new SynchronizedRandomGenerator(random);
		} else {
			this.random = random;
		}
	}
	
	/**
	 * Build a new instance of this class with a given Random instance.
	 * @param random the Random instance to be used as generator
	 * @param sync <code>true</code> if the generator is synchronized
	 */
	public RandomGeneratorWrapper(Random random, boolean sync) {
		if(sync) {
			this.random = new SynchronizedRandomGenerator(
				RandomGeneratorFactory.createRandomGenerator(random)
			);
		} else {
			this.random = RandomGeneratorFactory.createRandomGenerator(random);
		}
	}
	
	/**
	 * Build a new instance of this class with a given Random instance.
	 * @param random the Random instance to be used as generator
	 */
	public RandomGeneratorWrapper(Random random) {
		this.random = RandomGeneratorFactory.createRandomGenerator(random);
	}
	
	/**
	 * Build a new instance of this class with a given rng name.
	 * @param random thename of the rng.
	 */
	public RandomGeneratorWrapper(String random) {
		this.random = getRandomGenerator(random, false);
	}
	
	/**
	 * Build a new instance of this class with a given rng name.
	 * @param random thename of the rng.
	 * @param sync <code>true</code> if the generator is synchronized
	 */
	public RandomGeneratorWrapper(String random, boolean sync) {
		this.random = getRandomGenerator(random, sync);
	}
	
	/**
	 * Build a new instance of this class with a given rng name and seed.
	 * @param random the name of the rng
	 * @param seed the seed of the rng
	 */
	public RandomGeneratorWrapper(String random, long seed) {
		this.random = getRandomGenerator(random, false);
		this.random.setSeed(seed);
	}
	
	/**
	 * Build a new instance of this class with a given rng name and seed.
	 * @param random the name of the rng
	 * @param seed the seed of the rng
	 * @param sync <code>true</code> if the generator is synchronized
	 */
	public RandomGeneratorWrapper(String random, long seed, boolean sync) {
		this.random = getRandomGenerator(random, sync);
		this.random.setSeed(seed);
	}
	
	/**
	 * @param randomLib  the name of the rng
	 * @param sync <code>true</code> if the generator is synchronized
	 * @return the corresponding rng
	 */
	private static RandomGenerator getRandomGenerator(String randomLib, boolean sync) {
		RandomGenerator random;
		switch(randomLib) {
		case XOROSHIRO_128:
			random = RandomGeneratorFactory.createRandomGenerator(new Xoroshiro128Plus());
			break;
		case XORSHIFT_128:
			random = RandomGeneratorFactory.createRandomGenerator(new Xorshift128Plus());
			break;
		case XOROSHIRO_256:
			random = RandomGeneratorFactory.createRandomGenerator(new Xoshiro256StarStar());
			break;
		case LIGHT:
			random = RandomGeneratorFactory.createRandomGenerator(new LightRNG());
			break;	
		case MT_64:
			random = new MersenneTwister();
			break;
		case JDK:
			random = RandomGeneratorFactory.createRandomGenerator(new Random());
			break;
		case WELL_1024:
			random = new Well1024a();
			break;
		case PCG:
			random = RandomGeneratorFactory.createRandomGenerator(new PCG());
			break;
		default:
			random = RandomGeneratorFactory.createRandomGenerator(new Xoroshiro128Plus());
		}
		if(sync) {
			return new SynchronizedRandomGenerator(random);
		}
		return random;
	}

	/**
	 * @return the random
	 */
	public RandomGenerator getRandom() {
		return random;
	}
	


	 
}
