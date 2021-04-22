package by.bsuir.ti.third.cipher;

import by.bsuir.ti.third.util.SomeMath;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public byte[] encrypt(byte[] fileBytes) {
        List<BigInteger> encryptedNumbers = new ArrayList<>();
        for (byte fileByte : fileBytes) {
            //a = g^k mod p
            encryptedNumbers.add(a);
            //b = y^k * m mod p = ((y^k % p) * (m % p)) % p
            //todo: m must be positive
            short tmp = (short) (fileByte & 0xFF);
            encryptedNumbers.add(y.modPow(k, p).multiply(BigInteger.valueOf(tmp).mod(p)).mod(p));
        }
        List<Byte> numbers =  takeAllEncryptedBytes(encryptedNumbers);
        return takeByteArray(numbers);
    }

    private byte[] takeByteArray(List<Byte> numbers){
        byte[] bytes = new byte[numbers.size()];
        for (int i = 0; i < numbers.size(); i++){
            bytes[i] = numbers.get(i);
        }
        return bytes;
    }

    private List<Byte> takeAllEncryptedBytes(List<BigInteger> numbers){
        numbers = SomeMath.fixSizeOfNumbers(numbers);
        return takeBytesFromBigs(numbers);
    }

    private List<Byte> takeBytesFromBigs(List<BigInteger> numbers) {
        List<Byte> bytes = new ArrayList<>();
        for (BigInteger number : numbers){
            for (byte bytik : number.toByteArray()){
                bytes.add(bytik);
            }
        }
        return bytes;
    }

    @Override
    public byte[] decrypt(byte[] fileBytes) {
        List<BigInteger> decryptedNumbers = new ArrayList<>();
        int amount = SomeMath.AMOUNT_OF_BYTES_FOR_SYMBOL;
        System.out.println(Arrays.toString(a.toByteArray()));
        byte[] temp;
        for (int i = 0; i < fileBytes.length; ){
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
        List<Byte> numbers = takeBytesFromBigs(decryptedNumbers);
        return takeByteArray(numbers);
    }
}
