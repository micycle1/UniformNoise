package betternoise;

import java.util.concurrent.ThreadLocalRandom;

public class BetterNoise {

	private static int seed = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);

	public static void setSeed(final int seed) {
		BetterNoise.seed = seed;
	}

	public static float noise(float x, float y) {
		return F(PerlinNoiseLite.SinglePerlin(seed, x, y));
	}

	public static float noise(float x, float y, float z) {
		return F2(PerlinNoiseLite.SinglePerlin(seed, x, y, z));
	}

	/**
	 * Fifth-degree polynomial approximation of continuous distribution function for
	 * regular perlin noise. The purpose of this method is to flatten (since Perlin
	 * noise distributions follow a normal/gaussian distribution).
	 * 
	 * Probability_integral_transform https://stackoverflow.com/a/26858554/9808792
	 * 
	 * @param x raw perlin noise [-1...1]
	 * @return approximate uniformly distributed (perlin) noise between [0...1]
	 */
	private static float F(float x) {
		return (((((0.745671f * x + 0.00309887f) * x - 1.53841f) * x - 0.00343488f) * x + 1.29551f) * x) + 0.500516f;
	}

	/**
	 * This function doesn't quite pass through points (-1,0) and (1,1) so you could
	 * correct it as...
	 * 
	 * @param x
	 * @return
	 */
	private static float F1(float x) {
//		return F(x);
		return (F(x) + 0.002591009999999949f) * 0.99448854448f;
	}

	// clamp to [0...1]
	/**
	 * The function also strays just above 1 near x = 1 and just below 0 near x =
	 * -1, so you should clamp it between 0 and 1 if that matters to you.
	 * 
	 * @param x
	 * @return
	 */
	private static float F2(float x) {
		return Math.max(Math.min(F1(x), 1), 0);
	}

}
