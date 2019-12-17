package two;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;

public class FiatShamir {

    public static class ProtocolRun {
        public final BigInteger R;
        public final int c;
        public final BigInteger s;

        public ProtocolRun(BigInteger R, int c, BigInteger s) {
            this.R = R;
            this.c = c;
            this.s = s;
        }
    }

    public static void main(String[] args) {
        String path = "/home/jerge/git/TDA352-ass4/ass4/src/two/";
        String filename = path + "input.txt";
        BigInteger N = BigInteger.ZERO;
        BigInteger X = BigInteger.ZERO;
        ProtocolRun[] runs = new ProtocolRun[10];
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            N = new BigInteger(br.readLine().split("=")[1]);
            X = new BigInteger(br.readLine().split("=")[1]);
            for (int i = 0; i < 10; i++) {
                String line = br.readLine();
                String[] elem = line.split(",");
                runs[i] = new ProtocolRun(
                        new BigInteger(elem[0].split("=")[1]),
                        Integer.parseInt(elem[1].split("=")[1]),
                        new BigInteger(elem[2].split("=")[1]));
            }
            br.close();
        } catch (Exception err) {
            System.err.println("Error handling file.");
            err.printStackTrace();
            System.exit(1);
        }
        BigInteger m = recoverSecret(N, X, runs);
        System.out.println("Recovered message: " + m);
        System.out.println("Decoded text: " + decodeMessage(m));
    }

    public static String decodeMessage(BigInteger m) {
        return new String(m.toByteArray());
    }

    /**
     * Recovers the secret used in this collection of Fiat-Shamir protocol runs.
     *
     * @param N also known as n
     *            The modulus
     * @param X also known as v
     *            The public component
     * @param runs
     *            Ten runs of the protocol.
     *            R is the random value sent to the verifier := r^2 mod n
     *            c is the challenge sent to the prover A := random e in {0,1}
     *            s is the proof sent back to the verifier := rs^e mod n
     * @return x also known as s
     *            The secret key v=s^2 (mod n)
     */
    private static BigInteger recoverSecret(BigInteger N, BigInteger X,
                                            ProtocolRun[] runs) {
        // Known: Ri=Rj => ri=rj=rx =>
        // ai = rx*s^(ci)=rx, aj=rx*s^(cj) =>
        // ai^(-1)*aj = x OR ai*aj^(-1) = x (one of the two in case c is different)
        // Here we rely on ci != cj, to be able to distinguish x

        BigInteger x;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                // Check if x^2 = X (mod N)
                x = runs[i].s.multiply(runs[j].s.modInverse(N));
                if (X.equals(x.modPow(BigInteger.valueOf(2),N))) {
                    return x.mod(N);
                }
            }
        }
        return BigInteger.ZERO;
    }
}
