package by.bsuir.ti.third.cipher;

import by.bsuir.ti.third.util.SomeMath;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public final class ElGAmalCipher implements SimpleCipher {
    private final BigInteger p;
    private final BigInteger g;
    private final BigInteger k;
    private final BigInteger x;
    private final BigInteger a;
    private final BigInteger y;


    public ElGAmalCipher(BigInteger p, BigInteger g, BigInteger k, BigInteger x) {
        this.p = p;
        this.g = g;
        this.k = k;
        this.x = x;
        this.y = g.modPow(x, p);
        this.a = g.modPow(k, p);
    }

    @Override
    public List<BigInteger> encrypt(byte[] fileBytes) {
        List<BigInteger> encryptedNumbers = new ArrayList<>();
        for (byte fileByte : fileBytes) {
            //a = g^k mod p
            encryptedNumbers.add(a);
            //b = y^k * m mod p = ((y^k % p) * (m % p)) % p
            short tmp = (short) (fileByte & 0xFF);
            encryptedNumbers.add(y.modPow(k, p).multiply(BigInteger.valueOf(tmp).mod(p)).mod(p));
        }
        return encryptedNumbers;
    }

    @Override
    public List<BigInteger> decrypt(byte[] fileBytes) {
        List<BigInteger> decryptedNumbers = new ArrayList<>();
        int amount = SomeMath.AMOUNT_OF_BYTES_FOR_SYMBOL;
        byte[] temp;
        for (int i = 0; i < fileBytes.length; ) {
            temp = new byte[amount];
            System.arraycopy(fileBytes, i, temp, 0, amount);
            i += amount;
            BigInteger a = new BigInteger(1, temp);
            temp = new byte[amount];
            System.arraycopy(fileBytes, i, temp, 0, amount);
            i += amount;
            BigInteger b = new BigInteger(1, temp);
            //m = a^(-x) * b mod p = ((a^(-x) % p) * (b % p)) % p
            //todo: m must be positive
            //System.out.println(Arrays.toString(a.toByteArray()));
            //decryptedNumbers.add(a.modInverse(x).multiply(b.mod(p)).mod(p));
            decryptedNumbers.add(a.pow((int) x.longValue()).modInverse(p).multiply(b.mod(p)).mod(p));
        }
        return decryptedNumbers;
    }
}
