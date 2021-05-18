package by.bsuir.ti.fourth.encryption.impl;

import by.bsuir.ti.fourth.encryption.api.Cipher;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static by.bsuir.ti.fourth.math.Math.extendedEuclideanAlgorithm;
import static by.bsuir.ti.fourth.math.Math.fastExpMod;

public final class RSA implements Cipher {
    private final static int AMOUNT_OF_BYTES_BY_DATA = 4;
    private final BigInteger p;
    private final BigInteger q;
    private final BigInteger r;
    private final BigInteger phi;
    private final BigInteger d;
    private BigInteger e;

    public RSA(String pStringForm, String qStringForm, String eStringForm) {
        this.d = new BigInteger(eStringForm);
        this.p = new BigInteger(pStringForm);
        this.q = new BigInteger(qStringForm);
        this.r = p.multiply(q);
        this.phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
    }

    public BigInteger getE() {
        if (e != null) {
            return e;
        } else {
            return extendedEuclideanAlgorithm(phi, d)[1];
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
        return fastExpMod(new BigInteger(String.valueOf(dataByte)), d, r);
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
        return fastExpMod(value, getE(), r);
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
