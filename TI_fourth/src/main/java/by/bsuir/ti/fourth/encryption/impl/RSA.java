package by.bsuir.ti.fourth.encryption.impl;

import by.bsuir.ti.fourth.encryption.api.Cipher;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public final class RSA implements Cipher {
    private final static int AMOUNT_OF_BYTES_BY_DATA = 4;
    private final BigInteger p;
    private final BigInteger q;
    private final BigInteger r;
    private final BigInteger phi;
    private final BigInteger e;
    private BigInteger d;

    public RSA(String pStringForm, String qStringForm, String eStringForm) {
        this.e = new BigInteger(eStringForm);
        this.p = new BigInteger(pStringForm);
        this.q = new BigInteger(qStringForm);
        this.r = p.multiply(q);
        this.phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
    }

    public BigInteger getD() {
        if (d != null) {
            return d;
        } else {
            return extendedEuclideanAlgorithm()[1];
        }
    }

    @Override
    public byte[] encrypt(byte[] data) {
        List<BigInteger> encrypted = new ArrayList<>();
        for (byte datum : data) {
            encrypted.add(encryptByte(datum));
        }
        return convertToBytes(encrypted);
    }

    private BigInteger encryptByte(byte dataByte) {
        return fastExpMod(new BigInteger(String.valueOf(dataByte)), e);
    }

    @Override
    public byte[] decrypt(byte[] data) {
        List<BigInteger> decrypted = new ArrayList<>();
        for (int i = 0; i < data.length; i += AMOUNT_OF_BYTES_BY_DATA) {
            byte[] datum = new byte[AMOUNT_OF_BYTES_BY_DATA];
            System.arraycopy(data, i, datum, 0, AMOUNT_OF_BYTES_BY_DATA);
            decrypted.add(decryptBytes(new BigInteger(datum)));
        }
        return anotherConversion(decrypted);
    }

    private byte[] anotherConversion(List<BigInteger> numbers) {
        byte[] result = new byte[numbers.size()];
        int i = 0;
        for (BigInteger number : numbers) {
            result[i++] = number.toByteArray()[0];
        }
        return result;
    }

    private BigInteger decryptBytes(BigInteger value) {
        return fastExpMod(value, getD());
    }

    //x = a^z mod n
    public BigInteger fastExpMod(BigInteger a, BigInteger z) {
        BigInteger a1 = new BigInteger(a.toString());                               //a1 = a
        BigInteger z1 = new BigInteger(z.toString());                               //z1 = z
        BigInteger x = BigInteger.valueOf(1);                                       //x = 1
        while (z1.compareTo(BigInteger.ZERO) != 0) {                                 //while z1 != 0
            while (z1.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0) {  //while z1 mod 2 == 0
                BigInteger last = z1.mod(BigInteger.valueOf(2));                    //z1 = z1 div 2
                z1 = z1.subtract(last).divide(BigInteger.valueOf(2));               //a1 = a1 * a1 mod n
                a1 = a1.pow(2).mod(r);
            }
            z1 = z1.subtract(BigInteger.ONE);                                       //z1 = z1 - 1
            x = x.multiply(a1).mod(r);                                              //x = x * a1 mod n
        }
        return x;
    }

    private BigInteger[] extendedEuclideanAlgorithm() {
        BigInteger d0 = new BigInteger(phi.toString());
        BigInteger d1 = new BigInteger(e.toString());
        BigInteger x0 = BigInteger.ONE;
        BigInteger x1 = BigInteger.ZERO;
        BigInteger y0 = BigInteger.ZERO;
        BigInteger y1 = BigInteger.ONE;
        while (d1.compareTo(BigInteger.ONE) > 0) {
            BigInteger q = d0.subtract(d0.mod(d1)).divide(d1);
            BigInteger d2 = d0.mod(d1);
            BigInteger x2 = x0.subtract(q.multiply(x1));
            BigInteger y2 = y0.subtract(q.multiply(y1));
            d0 = d1;
            d1 = d2;
            x0 = x1;
            x1 = x2;
            y0 = y1;
            y1 = y2;
        }
        if (y1.signum() == -1) {
            y1 = y1.add(phi);
        }
        return new BigInteger[]{x1, y1, d1};
    }

    private byte[] convertToBytes(List<BigInteger> numbers) {
        byte[] result = new byte[numbers.size() * AMOUNT_OF_BYTES_BY_DATA];
        int i = 0;
        for (BigInteger number : numbers) {
            for (byte single : getBytesWithoutSign(number)) {
                result[i++] = single;
            }
        }
        return result;
    }

    private byte[] getBytesWithoutSign(BigInteger number) {
        byte[] sourceArray = number.toByteArray();
        if (sourceArray[0] != 0) {
            byte[] withoutSign = new byte[AMOUNT_OF_BYTES_BY_DATA];
            System.arraycopy(sourceArray, 0, withoutSign, withoutSign.length - sourceArray.length,
                    sourceArray.length);
            return withoutSign;
        } else {
            byte[] withoutSign = new byte[AMOUNT_OF_BYTES_BY_DATA];
            System.arraycopy(sourceArray, 1, withoutSign, withoutSign.length - sourceArray.length + 1,
                    sourceArray.length - 1);
            return withoutSign;
        }
    }
}
