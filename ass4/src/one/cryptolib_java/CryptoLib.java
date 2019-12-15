// Compilation (CryptoLibTest contains the main-method):
//   javac CryptoLibTest.java
// Running:
//   java CryptoLibTest
package one.cryptolib_java;

import java.math.BigInteger;

public class CryptoLib {

	/**
	 * Returns an array "result" with the values "result[0] = gcd",
	 * "result[1] = s" and "result[2] = t" such that "gcd" is the greatest
	 * common divisor of "a" and "b", and "gcd = a * s + b * t".
	 **/
	public static int[] EEA(int a, int b) {
		// Note: as you can see in the test suite,
		// your function should work for any (positive) value of a and b.

		// For efficiency
		if (a == b) {
			return new int[]{a, 1, 0};
		}

		int s1 = 0; int s2 = 1;
		int r1 = b; int r2 = a;
		int q,rt,st;
		while (r1 > 0) {
			q =  r2 / r1;
			rt = r2; r2 = r1; r1 = rt - q * r1;
			st = s2; s2 = s1; s1 = st - q * s1;
		}


		// b*t = r2 - a * s2

		int[] result = new int[3];
		result[0] = r2;
		result[1] = s2;
		result[2] = (r2 - a* s2)/b;
		return result;
	}

	/**
	 * Returns Euler's Totient for value "n".
	 **/
	public static int EulerPhi(int n) {
		int sum = 0;
		for (int i = 1; i < n; i++) {
			// EEA(n,1)[0] is the same as gcd(n,i)
			if (EEA(n,i)[0] == 1) {
				sum += 1;
			}
		}
		return sum;
	}



	/**
	 * Returns the value "v" such that "n*v = 1 (mod m)". Returns 0 if the
	 * modular inverse does not exist.
	 **/
	public static int ModInv(int n, int m) {
		// n^(Phi(m)) = 1 mod m => n * n^(Phi(m)-1) = 1
		// Java's implementation of modulo doesn't work for large numbers, and since we weren't allowed to
		while (n < 0) n += m;
		for (int i = 0; i < m; i++) {
			if (n*i % m == 1) return i;
		}
		/* Not permitted solution
		int ep = EulerPhi(m)-1;
		System.out.println(ep + ", " + Math.pow(n,ep) % m);
		System.out.println();
		BigInteger n2 = BigInteger.valueOf(n);
		BigInteger nphi = n2.pow(ep);
		nphi = nphi.mod(BigInteger.valueOf(m));
		return nphi.intValue();*/
		return 0;
	}

	// Since we're not allowed to use a mod for large numbers or power
	private static int powerWithMod(int a, int b, int m) {
		int sum = 1;
		for (int i = 0; i < b; i++) {
			sum *= a;
			sum = sum % m;
		}
		return sum;
	}

	/**
	 * Returns 0 if "n" is a Fermat Prime, otherwise it returns the lowest
	 * Fermat Witness. Tests values from 2 (inclusive) to "n/3" (exclusive).
	 **/
	public static int FermatPT(int n) {
		for (int a = 2; a <= n / 3; a++) {
			int s = powerWithMod(a, n - 1, n); // s=a^(n-1) mod n
			if (s != 1) {
				return a;
			}
		}
		return 0;
	}

		/**
         * Returns the probability that calling a perfect hash function with
         * "n_samples" (uniformly distributed) will give one collision (i.e. that
         * two samples result in the same hash) -- where "size" is the number of
         * different output values the hash function can produce.
         **/
	public static double HashCP(double n_samples, double size) {
		double prob = 1;
		for (int i = 1; i < n_samples; i++) {
			prob *= 1-i/size;
		}
		return 1-prob;
	}

}
