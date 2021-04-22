package by.bsuir.ti.third.cipher;

import by.bsuir.ti.third.util.FileWorker;
import by.bsuir.ti.third.util.SomeMath;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class ElGAmalCipher implements SimpleCipher{
    private final BigInteger p;
    private final BigInteger g;
    private final BigInteger k;
    private final BigInteger x;
    private BigInteger a;
    private BigInteger y;


    public ElGAmalCipher(BigInteger p, BigInteger g, BigInteger k, BigInteger x) {
        this.p = p;
        this.g = g;
        this.k = k;
        this.x = x;
        this.y = g.modPow(x, p);
        this.a = g.modPow(k, p);
    }

    @Override
    public List<Byte> encrypt(byte[] fileBytes) {
        List<BigInteger> encryptedNumbers = new ArrayList<>();
        for (byte fileByte : fileBytes) {
            encryptedNumbers.add(a);
            //b = y^k * m mod p = ((y^k % p) * (m % p)) % p
            encryptedNumbers.add(y.modPow(k, p).multiply(BigInteger.valueOf(fileByte).mod(p)).mod(p));
        }
        return takeAllBytes(encryptedNumbers);
    }

    private List<Byte> takeAllBytes(List<BigInteger> encryptedNumbers){
        encryptedNumbers = SomeMath.fixSizeOfNumbers(encryptedNumbers);
        List<Byte> bytes = new ArrayList<>();
        for (BigInteger number : encryptedNumbers){
            for (byte bytik : number.toByteArray()){
                bytes.add(bytik);
            }
        }
        return bytes;
    }

    @Override
    public List<Byte> decrypt(byte[] fileBytes) {
        for (int i = 0; i < fileBytes.length; i++){

        }
        return null;
    }
}
