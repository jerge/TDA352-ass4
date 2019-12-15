package three;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

public class CBCXor {

    public static void main(String[] args) {
        String filename = "/home/jerge/git/TDA352-ass4/ass4/src/three/input.txt";
        byte[] first_block = null;
        byte[] encrypted = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            first_block = br.readLine().getBytes();
            encrypted = DatatypeConverter.parseHexBinary(br.readLine());
            br.close();
        } catch (Exception err) {
            System.err.println("Error handling file.");
            err.printStackTrace();
            System.exit(1);
        }
        String m = recoverMessage(first_block, encrypted);
        System.out.println("Recovered message: " + m);
    }

    /**
     * Recover the encrypted message (CBC encrypted with XOR, block size = 12).
     *
     * @param first_block
     *            We know that this is the value of the first block of plain
     *            text.
     * @param encrypted
     *            The encrypted text, of the form IV | C0 | C1 | ... where each
     *            block is 12 bytes long.
     */
    private static String recoverMessage(byte[] first_block, byte[] encrypted) {
        byte[] iv = Arrays.copyOfRange(encrypted, 0, 12);
        byte[] c0 = Arrays.copyOfRange(encrypted, 12, 24);
        byte[] key = xor3(iv,c0,first_block);

        byte[] m0 = xor3(iv,c0,key);

        StringBuilder message = new StringBuilder();
        message.append(new String(first_block));
        byte[] ciold = c0;
        for (int i = 2; i < encrypted.length/12; i++) {
            byte[] ci = Arrays.copyOfRange(encrypted,i*12,i*12+12);
            byte[] mi = xor3(ci,key,ciold);
            message.append(new String(mi));
            ciold = ci;
        }
        return message.toString();
    }

    private static byte[] xor3(byte[] a1, byte[] a2, byte[] a3) {
        byte[] a0 = new byte[a1.length];
        int i = 0;
        for (byte b : a1)
            a0[i] = (byte) (b ^ a2[i] ^ a3[i++]);
        return a0;
    }
}
