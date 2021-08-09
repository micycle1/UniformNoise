package micycle.uniformnoise;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Perlin noise distributed uniformly between [ 0, 1 ].
 * 
 * @author Michael Carleton
 *
 */
public class UniformNoise {

	private static int seed = ThreadLocalRandom.current().nextInt();

	/**
	 * Sets the seed used by all static noise methods.
	 */
	public static void setSeed(final int seed) {
		UniformNoise.seed = seed;
	}

	/**
	 * Generates single-octave 2D uniform perlin noise for the coordinates given.
	 * 
	 * @return approximately uniformly distributed noise value between [ 0, 1 ]
	 */
	public static float uniformNoise(float x, float y) {
		return cdf2D(PerlinNoiseLite.SinglePerlin(seed, x, y));
	}

	/**
	 * Generates single-octave 2D uniform perlin noise for the coordinates given.
	 * 
	 * @return approximately uniformly distributed noise value between [ 0, 1 ]
	 */
	public static float uniformNoise(double x, double y) {
		return cdf2D(PerlinNoiseLite.SinglePerlin(seed, (float) x, (float) y));
	}

	/**
	 * Single-octave 3D uniform perlin noise for the coordinates given.
	 * 
	 * @return approximately uniformly distributed noise value between [ 0, 1 ]
	 */
	public static float uniformNoise(float x, float y, float z) {
		return cdf3D(PerlinNoiseLite.SinglePerlin(seed, x, y, z));
	}

	/**
	 * Single-octave 3D uniform perlin noise for the coordinates given.
	 * 
	 * @return approximately uniformly distributed noise value between [ 0, 1 ]
	 */
	public static float uniformNoise(double x, double y, double z) {
		return cdf3D(PerlinNoiseLite.SinglePerlin(seed, (float) x, (float) y, (float) z));
	}

	/**
	 * Generates multi-octave 2D uniform perlin noise for the coordinates given.
	 * 
	 * @param x
	 * @param y
	 * @param octaves     the number of levels of detail the perlin noise has
	 * @param persistence determines how quickly the amplitudes fall for each
	 *                    successive octave
	 * @return approximately uniformly distributed noise value between [ 0, 1 ]
	 */
	public static float uniformNoise(float x, float y, int octaves, float persistence) {
		float noise = 0;

		float frequency = 1;
		float amplitude = 1;
		for (int i = 0; i < octaves; i++) {
			noise += PerlinNoiseLite.SinglePerlin(seed, x * frequency, y * frequency) * amplitude;
			amplitude *= persistence;
			frequency *= 2; // lacunarity
		}
		return cdfOctaves2D(noise);
	}

	/**
	 * Generates multi-octave 2D uniform perlin noise for the coordinates given.
	 * 
	 * @param x
	 * @param y
	 * @param octaves     the number of levels of detail the perlin noise has
	 * @param persistence determines how quickly the amplitudes fall for each
	 *                    successive octave
	 * @return approximately uniformly distributed noise value between [ 0, 1 ]
	 */
	public static float uniformNoise(double x, double y, int octaves, double persistence) {
		return uniformNoise((float) x, (float) y, octaves, (float) persistence);
	}

	/**
	 * Generates multi-octave 3D uniform perlin noise for the coordinates given.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param octaves     the number of levels of detail the perlin noise has
	 * @param persistence determines how quickly the amplitudes fall for each
	 *                    successive octave
	 * @return approximately uniformly distributed noise value between [ 0, 1 ]
	 */
	public static float uniformNoise(float x, float y, float z, int octaves, float persistence) {
		float noise = 0;

		float frequency = 1;
		float amplitude = 1;
		for (int i = 0; i < octaves; i++) {
			noise += PerlinNoiseLite.SinglePerlin(seed, x * frequency, y * frequency, z * frequency) * amplitude;
			amplitude *= persistence;
			frequency *= 2; // lacunarity
		}
		return cdfOctaves3D(noise);
	}

	/**
	 * Generates multi-octave 3D uniform perlin noise for the coordinates given.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param octaves     the number of levels of detail the perlin noise has
	 * @param persistence determines how quickly the amplitudes fall for each
	 *                    successive octave
	 * @return approximately uniformly distributed noise value between [ 0, 1 ]
	 */
	public static float uniformNoise(double x, double y, double z, int octaves, double persistence) {
		return uniformNoise((float) x, (float) y, (float) z, octaves, (float) persistence);
	}

	/**
	 * Fifth-degree polynomial approximation of a continuous distribution function
	 * for 2D perlin noise.
	 * <p>
	 * This approximation is derived from a set of 2D single-octave noise values.
	 * 
	 * @param x raw (gaussian-like) perlin noise between [-1...1]
	 * @return approximately uniformly distributed noise value between [ 0, 1 ]
	 */
	private static float cdf2D(float x) {
		if (x < -0.729) {
			return 0;
		}
		if (x > 0.751) {
			return 1;
		}
		return ((((1.0616674f * x - 4.779669e-2f) * x - 1.5704816f) * x + 2.3737413e-2f) * x + 1.2176103f) * x + 4.9882874e-1f;
	}

	/**
	 * Fifth-degree polynomial approximation of a continuous distribution function
	 * for 3D perlin noise.
	 * <p>
	 * This approximation is derived from a set of 3D single-octave noise values.
	 * 
	 * @param x raw (gaussian-like) perlin noise between [-1...1]
	 * @return approximately uniformly distributed noise value between [ 0, 1 ]
	 */
	private static float cdf3D(float x) {
		if (x < -0.662) {
			return 0;
		}
		if (x > 0.66) {
			return 1;
		}
		return ((((2.0015578f * x + 2.8363844e-3f) * x - 2.4018942f) * x - 9.9932467e-4f) * x + 1.4237269f) * x + 5.0005216e-1f;
	}

	/**
	 * Fifth-degree polynomial approximation of a continuous distribution function
	 * for 2D perlin noise.
	 * <p>
	 * This approximation is derived from a set of 2D noise values having octaves=4
	 * and persistence=0.5; the output may be increasingly non-uniform with input
	 * noise generated using a different number of octaves or persistence.
	 * 
	 * @param x raw (gaussian-like) perlin noise between [-1...1]
	 * @return approximately uniformly distributed noise value between [ 0, 1 ]
	 */
	private static float cdfOctaves2D(float x) {
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

	/**
	 * Fifth-degree polynomial approximation of a continuous distribution function
	 * for 3D perlin noise.
	 * <p>
	 * This approximation is derived from a set of 3D noise values having octaves=4
	 * and persistence=0.5; the output may be increasingly non-uniform with input
	 * noise generated using a different number of octaves or persistence.
	 * 
	 * @param x raw (gaussian-like) 3D perlin noise between [-1...1]
	 * @return approximately uniformly distributed noise value between [ 0, 1 ]
	 */
	private static float cdfOctaves3D(float x) {
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

}
