/**
 * To the extent possible under law, the author has dedicated all copyright
 * and related and neighboring rights to this software to the public domain
 * worldwide. This software is distributed without any warranty.
 *
 * See <http://creativecommons.org/publicdomain/zero/1.0/>
 */
package fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.rng;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Implementation of Random based on the xoshiro256** RNG. No-dependencies
 * Java port of the <a href="http://xoshiro.di.unimi.it/xoshiro256starstar.c">original C code</a>,
 * which is public domain. This Java port is similarly dedicated to the public
 * domain.
 * <p>
 * Individual instances are not thread-safe. Each thread must have its own
 * instance which is not shared.
 *
 * @see <a href="http://xoshiro.di.unimi.it/">http://xoshiro.di.unimi.it/</a>
 * @author David Blackman and Sebastiano Vigna &lt;vigna@acm.org (original C code)
 * @author Una Thompson &lt;una@unascribed.com (Java port)
 */
public class Xoshiro256StarStar extends Random {

	private static final long serialVersionUID = -6294584098036826543L;

	private static final long[] s = new long[4];

	public Xoshiro256StarStar() {
		this((new SecureRandom()).nextLong());
	}

	public Xoshiro256StarStar(long seed) {
		super(seed);
		// super will call setSeed, but at that point, s will not be initialized
		setSeed(seed);
	}

	public Xoshiro256StarStar(long s1, long s2, long s3, long s4) {
		setState(s1, s2, s3, s4);
	}

	private static long splitmix64_1(long x) {
		return (x + 0x9E3779B97F4A7C15L);
	}

	private static long splitmix64_2(long z) {
		z = (z ^ (z >> 30)) * 0xBF58476D1CE4E5B9L;
		z = (z ^ (z >> 27)) * 0x94D049BB133111EBL;
		return z ^ (z >> 31);
	}

	@Override
	public void setSeed(long seed) {
		super.setSeed(seed);
		long sms = splitmix64_1(seed);
		s[0] = splitmix64_2(sms);
		sms = splitmix64_1(sms);
		s[1] = splitmix64_2(sms);
		sms = splitmix64_1(sms);
		s[2] = splitmix64_2(sms);
		sms = splitmix64_1(sms);
		s[3] = splitmix64_2(sms);
	}

	public void setState(long s1, long s2, long s3, long s4) {
		if (s1 == 0 && s2 == 0 && s3 == 0 && s4 == 0)
			throw new IllegalArgumentException("xoshiro256** state cannot be all zeroes");
		s[0] = s1;
		s[1] = s2;
		s[2] = s3;
		s[3] = s4;
	}

	// not called, implemented instead of just throwing for completeness
	@Override
	protected int next(int bits) {
		return (int)(nextLong() & ((1L << bits) - 1));
	}

	@Override
	public int nextInt() {
		return (int)nextLong();
	}

	@Override
	public int nextInt(int bound) {
		return (int)nextLong(bound);
	}

	public long nextLong(long bound) {
		if (bound <= 0) throw new IllegalArgumentException("bound must be positive");
		// clear sign bit for positive-only, modulo to bound
		return (nextLong() & Long.MAX_VALUE) % bound;
	}

	@Override
	public double nextDouble() {
		return (nextLong() >>> 11) * 0x1.0P-53;
	}

	@Override
	public float nextFloat() {
		return (nextLong() >>> 40) * 0x1.0P-24f;
	}

	@Override
	public boolean nextBoolean() {
		return (nextLong() & 1) != 0;
	}

	@Override
	public void nextBytes(byte[] bytes) {
		int j = 8;
		long l = 0;
		for (int i = 0; i < bytes.length; i++) {
			if (j >= 8) {
				l = nextLong();
				j = 0;
			}
			bytes[i] = (byte)(l&0xFF);
			l =  l >>> 8L;
			j++;
		}
	}


	/* This is xoshiro256** 1.0, our all-purpose, rock-solid generator. It has
	   excellent (sub-ns) speed, a state (256 bits) that is large enough for
	   any parallel application, and it passes all tests we are aware of.

	   For generating just floating-point numbers, xoshiro256+ is even faster.

	   The state must be seeded so that it is not everywhere zero. If you have
	   a 64-bit seed, we suggest to seed a splitmix64 generator and use its
	   output to fill s. */

	private static long rotl(long x, int k) {
		return (x << k) | (x >>> (64 - k));
	}

	@Override
	public long nextLong() {
		long result_starstar = rotl(s[1] * 5, 7) * 9;

		long t = s[1] << 17;

		s[2] ^= s[0];
		s[3] ^= s[1];
		s[1] ^= s[2];
		s[0] ^= s[3];

		s[2] ^= t;

		s[3] = rotl(s[3], 45);

		return result_starstar;
	}

}
