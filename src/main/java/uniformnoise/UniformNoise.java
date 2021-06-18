package uniformnoise;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Perlin noise distributed uniformly between [ 0, 1 ].
 * 
 * @author Michael Carleton
 *
 */
public class UniformNoise {

	private static int seed = ThreadLocalRandom.current().nextInt();

	public static void setSeed(final int seed) {
		UniformNoise.seed = seed;
	}

	/**
	 * Single-octave 2D uniform perlin noise.
	 * 
	 * @return approximately uniformly distributed noise value between [ 0, 1 ]
	 */
	public static float uniformNoise(float x, float y) {
		return G2D(PerlinNoiseLite.SinglePerlin(seed, x, y));
	}

	/**
	 * Single-octave 3D uniform perlin noise.
	 */
	public static float uniformNoise(float x, float y, float z) {
		return G3D(PerlinNoiseLite.SinglePerlin(seed, x, y, z));
	}

	public static float uniformNoise(float x, float y, int octaves, float persistence) {
		float noise = 0;

		float frequency = 1;
		float amplitude = 1;
		for (int i = 0; i < octaves; i++) {
			noise += PerlinNoiseLite.SinglePerlin(seed, x * frequency, y * frequency) * amplitude;
			amplitude *= persistence;
			frequency *= 2;
		}
		return H2D(noise);
	}

	/**
	 * OctaveNoise
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param octaves
	 * @param persistence
	 * @return
	 */
	public static float uniformNoise(float x, float y, float z, int octaves, float persistence) {
		float noise = 0;

		float frequency = 1;
		float amplitude = 1;
		for (int i = 0; i < octaves; i++) {
			noise += PerlinNoiseLite.SinglePerlin(seed, x * frequency, y * frequency, z * frequency) * amplitude;
			amplitude *= persistence;
			frequency *= 2;
		}
		return H3D(noise);
	}

	private static float G2D(float x) {
		if (x < -0.73) {
			return 0;
		}
		if (x > 0.751) {
			return 1;
		}
		return ((((1.0616674f * x - 4.779669e-2f) * x - 1.5704816f) * x + 2.3737413e-2f) * x + 1.2176103f) * x + 4.9882874e-1f;
	}

	/**
	 * Single-octave 3D noise
	 * 
	 * @param x
	 * @return
	 */
	private static float G3D(float x) {
		if (x < -0.662) {
			return 0;
		}
		if (x > 0.66) {
			return 1;
		}
		return ((((2.0015578f * x + 2.8363844e-3f) * x - 2.4018942f) * x - 9.9932467e-4f) * x + 1.4237269f) * x + 5.0005216e-1f;
	}

	/**
	 * Optimised for 3d perlin noise with 4 octaves, 0.5 falloff between -0.8 and
	 * 0.8. * Fifth-degree polynomial approximation of continuous distribution
	 * function for regular perlin noise. The purpose of this method is to flatten
	 * (since Perlin noise distributions follow a normal/gaussian distribution).
	 * 
	 * @param x raw (gaussian-like) perlin noise between [-1...1]
	 * @return approximately uniformly distributed noise value between [ 0, 1 ]
	 */
	private static float H3D(float x) {
		if (x < -0.796) {
			return 0;
		}
		if (x > 0.793) {
			return 1;
		}
		/*
		 * Approximation on interval [ -0.8, 0.8 ] with a polynomial of degree 5.
		 */
		return ((((9.8832232e-1f * x + 2.1932919e-3f) * x - 1.6058296f) * x - 1.3540642e-3f) * x + 1.2490637f) * x + 5.0019288e-1f;
	}

	/**
	 * Optimised for 2d perlin noise with 4 octaves, 0.5 persistence.
	 * 
	 * @param x
	 * @return
	 */
	private static float H2D(float x) {
		if (x < -0.897) {
			return 0;
		}
		if (x > 0.891) {
			return 1;
		}
		/*
		 * Approximation on interval [ -0.9, 0.9 ] with a polynomial of degree 5.
		 */
		return ((((5.5801775e-1f * x + 3.4876526e-3f) * x - 1.102035f) * x - 2.1366362e-3f) * x + 1.083566f) * x + 5.0009794e-1f;
	}

}
