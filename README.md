# Uniform Noise

Perlin noise distributed uniformly between [ 0, 1 ].

Natural Perlin noise has a gaussian-like distribution and range [ −√N/4, √N/4 ] (where N is the number of dimensions, single octave only). These characteristics are often desirable but there are times when a linear distribution of noise (between 0 and 1) is more appropriate for the application at hand — this is where this small library steps in.

Functions have been derived to transform raw Perlin noise values and map them to an approximately linear output between [ 0, 1 ]. Virtually no overhead.

*UniformNoise* uses Perlin noise routines from [*FastNoiseLite*](https://github.com/Auburn/FastNoiseLite) (note these routes implement Ken Perlin's *Improved Perlin Noise*, which uses quintic interpolation).

### Usage

```
noise(float x, float y, float z) // single octave
noise(float x, float y, float z, int octaves, float persistence)
```

### Appendix: Derivation Method

* Generate millions of samples
* Sort samples
* Sample samples to list of coordinates 
* Plot an approximate CDF from coordinates/samples
* Fit a anayltical curve to coordinates with Fityk (using [EMG](http://fityk.nieto.pl/model.html?highlight=emg#built-in-functions)+Polynomial degree 6)
* Approximate the curve over reduced range with [LolRemez](https://github.com/samhocevar/lolremez).
* Find roots (f(x)=0; f(x)=1) for approximate function (since can dip over/above) -- restrict range to this in function (return 0 if below; 1 if above)