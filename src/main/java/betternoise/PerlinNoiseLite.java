package betternoise;

/**
 * Extracted from https://github.com/Auburn/FastNoise
 */
class PerlinNoiseLite {

	// Hashing
	private static final int PrimeX = 501125321;
	private static final int PrimeY = 1136930381;
	private static final int PrimeZ = 1720413743;

	static float SinglePerlin(int seed, float x, float y) {
		int x0 = FastFloor(x);
		int y0 = FastFloor(y);

		float xd0 = (x - x0);
		float yd0 = (y - y0);
		float xd1 = xd0 - 1;
		float yd1 = yd0 - 1;

		float xs = InterpQuintic(xd0);
		float ys = InterpQuintic(yd0);

		x0 *= PrimeX;
		y0 *= PrimeY;
		int x1 = x0 + PrimeX;
		int y1 = y0 + PrimeY;

		float xf0 = Lerp(GradCoord(seed, x0, y0, xd0, yd0), GradCoord(seed, x1, y0, xd1, yd0), xs);
		float xf1 = Lerp(GradCoord(seed, x0, y1, xd0, yd1), GradCoord(seed, x1, y1, xd1, yd1), xs);

		return Lerp(xf0, xf1, ys) * 1.4247691104677813f;
	}

	static float SinglePerlin(int seed, float x, float y, float z) {
		int x0 = FastFloor(x);
		int y0 = FastFloor(y);
		int z0 = FastFloor(z);

		float xd0 = (x - x0);
		float yd0 = (y - y0);
		float zd0 = (z - z0);
		float xd1 = xd0 - 1;
		float yd1 = yd0 - 1;
		float zd1 = zd0 - 1;

		float xs = InterpQuintic(xd0);
		float ys = InterpQuintic(yd0);
		float zs = InterpQuintic(zd0);

		x0 *= PrimeX;
		y0 *= PrimeY;
		z0 *= PrimeZ;
		int x1 = x0 + PrimeX;
		int y1 = y0 + PrimeY;
		int z1 = z0 + PrimeZ;

		float xf00 = Lerp(GradCoord(seed, x0, y0, z0, xd0, yd0, zd0), GradCoord(seed, x1, y0, z0, xd1, yd0, zd0), xs);
		float xf10 = Lerp(GradCoord(seed, x0, y1, z0, xd0, yd1, zd0), GradCoord(seed, x1, y1, z0, xd1, yd1, zd0), xs);
		float xf01 = Lerp(GradCoord(seed, x0, y0, z1, xd0, yd0, zd1), GradCoord(seed, x1, y0, z1, xd1, yd0, zd1), xs);
		float xf11 = Lerp(GradCoord(seed, x0, y1, z1, xd0, yd1, zd1), GradCoord(seed, x1, y1, z1, xd1, yd1, zd1), xs);

		float yf0 = Lerp(xf00, xf10, ys);
		float yf1 = Lerp(xf01, xf11, ys);

		return Lerp(yf0, yf1, zs) * 0.964921414852142333984375f;
	}

	private static float GradCoord(int seed, int xPrimed, int yPrimed, float xd, float yd) {
		int hash = Hash(seed, xPrimed, yPrimed);
		hash ^= hash >> 15;
		hash &= 127 << 1;

		float xg = Gradients2D[hash];
		float yg = Gradients2D[hash | 1];

		return xd * xg + yd * yg;
	}

	private static float GradCoord(int seed, int xPrimed, int yPrimed, int zPrimed, float xd, float yd, float zd) {
		int hash = Hash(seed, xPrimed, yPrimed, zPrimed);
		hash ^= hash >> 15;
		hash &= 63 << 2;

		float xg = Gradients3D[hash];
		float yg = Gradients3D[hash | 1];
		float zg = Gradients3D[hash | 2];

		return xd * xg + yd * yg + zd * zg;
	}

	private static int Hash(int seed, int xPrimed, int yPrimed) {
		int hash = seed ^ xPrimed ^ yPrimed;
		hash *= 0x27d4eb2d;
		return hash;
	}

	private static int Hash(int seed, int xPrimed, int yPrimed, int zPrimed) {
		int hash = seed ^ xPrimed ^ yPrimed ^ zPrimed;
		hash *= 0x27d4eb2d;
		return hash;
	}

	private static float InterpQuintic(float t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}

	private static int FastFloor(float f) {
		return f >= 0 ? (int) f : (int) f - 1;
	}

	private static float Lerp(float a, float b, float t) {
		return a + t * (b - a);
	}

	private static final float[] Gradients2D = { 0.130526192220052f, 0.99144486137381f, 0.38268343236509f,
			0.923879532511287f, 0.608761429008721f, 0.793353340291235f, 0.793353340291235f, 0.608761429008721f,
			0.923879532511287f, 0.38268343236509f, 0.99144486137381f, 0.130526192220051f, 0.99144486137381f,
			-0.130526192220051f, 0.923879532511287f, -0.38268343236509f, 0.793353340291235f, -0.60876142900872f,
			0.608761429008721f, -0.793353340291235f, 0.38268343236509f, -0.923879532511287f, 0.130526192220052f,
			-0.99144486137381f, -0.130526192220052f, -0.99144486137381f, -0.38268343236509f, -0.923879532511287f,
			-0.608761429008721f, -0.793353340291235f, -0.793353340291235f, -0.608761429008721f, -0.923879532511287f,
			-0.38268343236509f, -0.99144486137381f, -0.130526192220052f, -0.99144486137381f, 0.130526192220051f,
			-0.923879532511287f, 0.38268343236509f, -0.793353340291235f, 0.608761429008721f, -0.608761429008721f,
			0.793353340291235f, -0.38268343236509f, 0.923879532511287f, -0.130526192220052f, 0.99144486137381f,
			0.130526192220052f, 0.99144486137381f, 0.38268343236509f, 0.923879532511287f, 0.608761429008721f,
			0.793353340291235f, 0.793353340291235f, 0.608761429008721f, 0.923879532511287f, 0.38268343236509f,
			0.99144486137381f, 0.130526192220051f, 0.99144486137381f, -0.130526192220051f, 0.923879532511287f,
			-0.38268343236509f, 0.793353340291235f, -0.60876142900872f, 0.608761429008721f, -0.793353340291235f,
			0.38268343236509f, -0.923879532511287f, 0.130526192220052f, -0.99144486137381f, -0.130526192220052f,
			-0.99144486137381f, -0.38268343236509f, -0.923879532511287f, -0.608761429008721f, -0.793353340291235f,
			-0.793353340291235f, -0.608761429008721f, -0.923879532511287f, -0.38268343236509f, -0.99144486137381f,
			-0.130526192220052f, -0.99144486137381f, 0.130526192220051f, -0.923879532511287f, 0.38268343236509f,
			-0.793353340291235f, 0.608761429008721f, -0.608761429008721f, 0.793353340291235f, -0.38268343236509f,
			0.923879532511287f, -0.130526192220052f, 0.99144486137381f, 0.130526192220052f, 0.99144486137381f,
			0.38268343236509f, 0.923879532511287f, 0.608761429008721f, 0.793353340291235f, 0.793353340291235f,
			0.608761429008721f, 0.923879532511287f, 0.38268343236509f, 0.99144486137381f, 0.130526192220051f,
			0.99144486137381f, -0.130526192220051f, 0.923879532511287f, -0.38268343236509f, 0.793353340291235f,
			-0.60876142900872f, 0.608761429008721f, -0.793353340291235f, 0.38268343236509f, -0.923879532511287f,
			0.130526192220052f, -0.99144486137381f, -0.130526192220052f, -0.99144486137381f, -0.38268343236509f,
			-0.923879532511287f, -0.608761429008721f, -0.793353340291235f, -0.793353340291235f, -0.608761429008721f,
			-0.923879532511287f, -0.38268343236509f, -0.99144486137381f, -0.130526192220052f, -0.99144486137381f,
			0.130526192220051f, -0.923879532511287f, 0.38268343236509f, -0.793353340291235f, 0.608761429008721f,
			-0.608761429008721f, 0.793353340291235f, -0.38268343236509f, 0.923879532511287f, -0.130526192220052f,
			0.99144486137381f, 0.130526192220052f, 0.99144486137381f, 0.38268343236509f, 0.923879532511287f,
			0.608761429008721f, 0.793353340291235f, 0.793353340291235f, 0.608761429008721f, 0.923879532511287f,
			0.38268343236509f, 0.99144486137381f, 0.130526192220051f, 0.99144486137381f, -0.130526192220051f,
			0.923879532511287f, -0.38268343236509f, 0.793353340291235f, -0.60876142900872f, 0.608761429008721f,
			-0.793353340291235f, 0.38268343236509f, -0.923879532511287f, 0.130526192220052f, -0.99144486137381f,
			-0.130526192220052f, -0.99144486137381f, -0.38268343236509f, -0.923879532511287f, -0.608761429008721f,
			-0.793353340291235f, -0.793353340291235f, -0.608761429008721f, -0.923879532511287f, -0.38268343236509f,
			-0.99144486137381f, -0.130526192220052f, -0.99144486137381f, 0.130526192220051f, -0.923879532511287f,
			0.38268343236509f, -0.793353340291235f, 0.608761429008721f, -0.608761429008721f, 0.793353340291235f,
			-0.38268343236509f, 0.923879532511287f, -0.130526192220052f, 0.99144486137381f, 0.130526192220052f,
			0.99144486137381f, 0.38268343236509f, 0.923879532511287f, 0.608761429008721f, 0.793353340291235f,
			0.793353340291235f, 0.608761429008721f, 0.923879532511287f, 0.38268343236509f, 0.99144486137381f,
			0.130526192220051f, 0.99144486137381f, -0.130526192220051f, 0.923879532511287f, -0.38268343236509f,
			0.793353340291235f, -0.60876142900872f, 0.608761429008721f, -0.793353340291235f, 0.38268343236509f,
			-0.923879532511287f, 0.130526192220052f, -0.99144486137381f, -0.130526192220052f, -0.99144486137381f,
			-0.38268343236509f, -0.923879532511287f, -0.608761429008721f, -0.793353340291235f, -0.793353340291235f,
			-0.608761429008721f, -0.923879532511287f, -0.38268343236509f, -0.99144486137381f, -0.130526192220052f,
			-0.99144486137381f, 0.130526192220051f, -0.923879532511287f, 0.38268343236509f, -0.793353340291235f,
			0.608761429008721f, -0.608761429008721f, 0.793353340291235f, -0.38268343236509f, 0.923879532511287f,
			-0.130526192220052f, 0.99144486137381f, 0.38268343236509f, 0.923879532511287f, 0.923879532511287f,
			0.38268343236509f, 0.923879532511287f, -0.38268343236509f, 0.38268343236509f, -0.923879532511287f,
			-0.38268343236509f, -0.923879532511287f, -0.923879532511287f, -0.38268343236509f, -0.923879532511287f,
			0.38268343236509f, -0.38268343236509f, 0.923879532511287f, };

	private static final float[] Gradients3D = { 0, 1, 1, 0, 0, -1, 1, 0, 0, 1, -1, 0, 0, -1, -1, 0, 1, 0, 1, 0, -1, 0,
			1, 0, 1, 0, -1, 0, -1, 0, -1, 0, 1, 1, 0, 0, -1, 1, 0, 0, 1, -1, 0, 0, -1, -1, 0, 0, 0, 1, 1, 0, 0, -1, 1,
			0, 0, 1, -1, 0, 0, -1, -1, 0, 1, 0, 1, 0, -1, 0, 1, 0, 1, 0, -1, 0, -1, 0, -1, 0, 1, 1, 0, 0, -1, 1, 0, 0,
			1, -1, 0, 0, -1, -1, 0, 0, 0, 1, 1, 0, 0, -1, 1, 0, 0, 1, -1, 0, 0, -1, -1, 0, 1, 0, 1, 0, -1, 0, 1, 0, 1,
			0, -1, 0, -1, 0, -1, 0, 1, 1, 0, 0, -1, 1, 0, 0, 1, -1, 0, 0, -1, -1, 0, 0, 0, 1, 1, 0, 0, -1, 1, 0, 0, 1,
			-1, 0, 0, -1, -1, 0, 1, 0, 1, 0, -1, 0, 1, 0, 1, 0, -1, 0, -1, 0, -1, 0, 1, 1, 0, 0, -1, 1, 0, 0, 1, -1, 0,
			0, -1, -1, 0, 0, 0, 1, 1, 0, 0, -1, 1, 0, 0, 1, -1, 0, 0, -1, -1, 0, 1, 0, 1, 0, -1, 0, 1, 0, 1, 0, -1, 0,
			-1, 0, -1, 0, 1, 1, 0, 0, -1, 1, 0, 0, 1, -1, 0, 0, -1, -1, 0, 0, 1, 1, 0, 0, 0, -1, 1, 0, -1, 1, 0, 0, 0,
			-1, -1, 0 };

}
