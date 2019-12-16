package five;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;
import java.time.Year;


public class ElGamal {

    public static String decodeMessage(BigInteger m) {
        return new String(m.toByteArray());
    }

    public static void main(String[] arg) {
        String filename = "/home/jerge/git/TDA352-ass4/ass4/src/five/input.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            BigInteger p = new BigInteger(br.readLine().split("=")[1]);
            BigInteger g = new BigInteger(br.readLine().split("=")[1]);
            BigInteger y = new BigInteger(br.readLine().split("=")[1]);
            String line = br.readLine().split("=")[1];
            String date = line.split(" ")[0];
            String time = line.split(" ")[1];
            int year  = Integer.parseInt(date.split("-")[0]);
            int month = Integer.parseInt(date.split("-")[1]);
            int day   = Integer.parseInt(date.split("-")[2]);
            int hour   = Integer.parseInt(time.split(":")[0]);
            int minute = Integer.parseInt(time.split(":")[1]);
            int second = Integer.parseInt(time.split(":")[2]);
            BigInteger c1 = new BigInteger(br.readLine().split("=")[1]);
            BigInteger c2 = new BigInteger(br.readLine().split("=")[1]);
            br.close();
            BigInteger m = recoverSecret(p, g, y, year, month, day, hour, minute,
                    second, c1, c2);
            System.out.println("Recovered message: \n" + m);
            System.out.println("Decoded text: " + decodeMessage(m));
        } catch (Exception err) {
            System.err.println("Error handling file.");
            err.printStackTrace();
            System.exit(1);
        }
    }

    private static BigInteger generateRandomNumber(int year, int month, int day, int hour, int minute,
                                                   int second, BigInteger g, BigInteger p, BigInteger c1) {
        BigInteger bigYear = BigInteger.valueOf(year);
        BigInteger bigMonth = BigInteger.valueOf(month);
        BigInteger bigDay = BigInteger.valueOf(day);
        BigInteger bigHour = BigInteger.valueOf(hour);
        BigInteger bigMinute = BigInteger.valueOf(minute);
        BigInteger bigSecond = BigInteger.valueOf(second);

        BigInteger bigAdded =
            bigYear.multiply(BigInteger.TEN.pow(10)).add(
            bigMonth.multiply(BigInteger.TEN.pow(8)).add(
            bigDay.multiply(BigInteger.TEN.pow(6)).add(
            bigHour.multiply(BigInteger.TEN.pow(4)).add(
            bigMinute.multiply(BigInteger.TEN.pow(2)).add(
            bigSecond
        )))));

        BigInteger r;
        for (int i = 0; i < 999; i++) {
            r = bigAdded.add(BigInteger.valueOf(i));
            if (c1.equals(g.modPow(r,p))) {
                return r;
            }
        }

        return BigInteger.ZERO;
    }

    /**
     *
     * @param p the Modulus / the size of the group
     * @param g the Generator for the group
     * @param y the public key y=g^x (mod p), where x is the secret key
     * @param c1 the first part of the cipher text := g^r
     * @param c2 the second part of the cipher text := m*y^r
     * @return
     */
    public static BigInteger recoverSecret(BigInteger p, BigInteger g,
                                           BigInteger y, int year, int month, int day, int hour, int minute,
                                           int second, BigInteger c1, BigInteger c2) {
        BigInteger r = generateRandomNumber(year,month,day,hour,minute,second,g,p,c1);

        // h^(-1) (mod p)
        BigInteger yInv = y.modInverse(p);
        // h^(-1)^r=h^(-r) (mod p)
        BigInteger s = yInv.modPow(r,p);
        // c2 = m*h^r => m*h^r*h^(-r) = m = c2*h^(-r) (mod p)
        return c2.multiply(s).mod(p);
    }

}

