package by.bsuir.ti.fourth.hash.impl;


import by.bsuir.ti.fourth.hash.api.HashFunction;

import java.math.BigInteger;
import java.util.Arrays;

public final class SHA1 implements HashFunction {
    private static final int[] ABCDE = {
            0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476, 0xc3d2e1f0
    };
    private final int[] digestInt = new int[5];
    private final int[] tmpData = new int[80];

    private static String byteToHexString(byte ib) {
        char[] digits = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c',
                'd', 'e', 'f'
        };
        char[] ob = new char[2];
        ob[0] = digits[(ib >>> 4) & 0X0F];
        ob[1] = digits[ib & 0X0F];
        return new String(ob);
    }

    private static String byteArrayToHexString(byte[] bytearray) {
        StringBuilder strDigest = new StringBuilder();
        for (byte b : bytearray) {
            strDigest.append(byteToHexString(b));
        }
        return strDigest.toString();
    }

    private void processInputBytes(byte[] bytedata) {
        System.arraycopy(ABCDE, 0, digestInt, 0, ABCDE.length);
        byte[] newByte = byteArrayFormatData(bytedata);
        int countOfBlocks = newByte.length / 64;
        for (int pos = 0; pos < countOfBlocks; pos++) {
            for (int j = 0; j < 16; j++) {
                tmpData[j] = byteArrayToInt(newByte, (pos * 64) + (j * 4));
            }
            takeHash();
        }
    }

    private byte[] byteArrayFormatData(byte[] bytedata) {
        int zeros = 0;
        int size = 0;
        int n = bytedata.length;
        int m = n % 64;
        if (m < 56) {
            zeros = 55 - m;
            size = n - m + 64;
        } else if (m == 56) {
            zeros = 63;
            size = n + 8 + 64;
        } else {
            zeros = 63 - m + 56;
            size = (n + 64) - m + 64;
        }
        byte[] newbyte = new byte[size];
        System.arraycopy(bytedata, 0, newbyte, 0, n);
        int l = n;
        newbyte[l++] = (byte) 0x80;
        for (int i = 0; i < zeros; i++) {
            newbyte[l++] = (byte) 0x00;
        }
        long N = (long) n * 8;
        byte h8 = (byte) (N & 0xFF);
        byte h7 = (byte) ((N >> 8) & 0xFF);
        byte h6 = (byte) ((N >> 16) & 0xFF);
        byte h5 = (byte) ((N >> 24) & 0xFF);
        byte h4 = (byte) ((N >> 32) & 0xFF);
        byte h3 = (byte) ((N >> 40) & 0xFF);
        byte h2 = (byte) ((N >> 48) & 0xFF);
        byte h1 = (byte) (N >> 56);
        newbyte[l++] = h1;
        newbyte[l++] = h2;
        newbyte[l++] = h3;
        newbyte[l++] = h4;
        newbyte[l++] = h5;
        newbyte[l++] = h6;
        newbyte[l++] = h7;
        newbyte[l++] = h8;
        return newbyte;
    }

    private int f1(int x, int y, int z) {
        return (x & y) | (~x & z);
    }

    private int f2(int x, int y, int z) {
        return x ^ y ^ z;
    }

    private int f3(int x, int y, int z) {
        return (x & y) | (x & z) | (y & z);
    }

    private int circleShift(int x, int y) {
        return (x << y) | x >>> (32 - y);
    }

    private void takeHash() {
        for (int i = 16; i <= 79; i++) {
            tmpData[i] = circleShift(tmpData[i - 3] ^ tmpData[i - 8] ^ tmpData[i - 14] ^ tmpData[i - 16], 1);
        }
        int[] tmpABCDE = new int[5];
        System.arraycopy(digestInt, 0, tmpABCDE, 0, tmpABCDE.length); //Temporary a,b,c,d,e
        calculateHash(tmpABCDE);
        for (int i = 0; i < tmpABCDE.length; i++) {
            digestInt[i] = digestInt[i] + tmpABCDE[i];
        }
        Arrays.fill(tmpData, 0); //Clear tmp array for next iterations
    }

    private void calculateHash(int[] tmpABCDE) {
        calculateFirstRange(tmpABCDE);
        caclulateSecondRange(tmpABCDE);
        calculateThirdRange(tmpABCDE);
        calculateFourthRange(tmpABCDE);
    }

    private void calculateFirstRange(int[] tmpABCDE) {
        for (int j = 0; j <= 19; j++) {
            int tmp = circleShift(tmpABCDE[0], 5)
                    + f1(tmpABCDE[1], tmpABCDE[2], tmpABCDE[3])
                    + tmpABCDE[4] + tmpData[j]
                    + 0x5a827999;
            tmpABCDE[4] = tmpABCDE[3];
            tmpABCDE[3] = tmpABCDE[2];
            tmpABCDE[2] = circleShift(tmpABCDE[1], 30);
            tmpABCDE[1] = tmpABCDE[0];
            tmpABCDE[0] = tmp;
        }
    }

    private void caclulateSecondRange(int[] tmpABCDE) {
        for (int k = 20; k <= 39; k++) {
            int tmp = circleShift(tmpABCDE[0], 5)
                    + f2(tmpABCDE[1], tmpABCDE[2], tmpABCDE[3])
                    + tmpABCDE[4]
                    + tmpData[k]
                    + 0x6ed9eba1;
            tmpABCDE[4] = tmpABCDE[3];
            tmpABCDE[3] = tmpABCDE[2];
            tmpABCDE[2] = circleShift(tmpABCDE[1], 30);
            tmpABCDE[1] = tmpABCDE[0];
            tmpABCDE[0] = tmp;
        }
    }

    private void calculateThirdRange(int[] tmpABCDE) {
        for (int l = 40; l <= 59; l++) {
            int tmp = circleShift(tmpABCDE[0], 5)
                    + f3(tmpABCDE[1], tmpABCDE[2], tmpABCDE[3])
                    + tmpABCDE[4]
                    + tmpData[l]
                    + 0x8f1bbcdc;
            tmpABCDE[4] = tmpABCDE[3];
            tmpABCDE[3] = tmpABCDE[2];
            tmpABCDE[2] = circleShift(tmpABCDE[1], 30);
            tmpABCDE[1] = tmpABCDE[0];
            tmpABCDE[0] = tmp;
        }
    }

    private void calculateFourthRange(int[] tmpABCDE) {
        for (int m = 60; m <= 79; m++) {
            int tmp = circleShift(tmpABCDE[0], 5)
                    + f2(tmpABCDE[1], tmpABCDE[2], tmpABCDE[3])
                    + tmpABCDE[4]
                    + tmpData[m]
                    + 0xca62c1d6;
            tmpABCDE[4] = tmpABCDE[3];
            tmpABCDE[3] = tmpABCDE[2];
            tmpABCDE[2] = circleShift(tmpABCDE[1], 30);
            tmpABCDE[1] = tmpABCDE[0];
            tmpABCDE[0] = tmp;
        }
    }

    private int byteArrayToInt(byte[] bytedata, int i) {
        return ((bytedata[i] & 0xff) << 24) | ((bytedata[i + 1] & 0xff) << 16)
                | ((bytedata[i + 2] & 0xff) << 8) | (bytedata[i + 3] & 0xff);
    }

    private void intToByteArray(int intValue, byte[] byteData, int i) {
        byteData[i] = (byte) (intValue >>> 24);
        byteData[i + 1] = (byte) (intValue >>> 16);
        byteData[i + 2] = (byte) (intValue >>> 8);
        byteData[i + 3] = (byte) intValue;
    }

    public byte[] takeDigestInBytes(byte[] byteData) {
        processInputBytes(byteData);
        byte[] digest = new byte[20];
        for (int i = 0; i < digestInt.length; i++) {
            intToByteArray(digestInt[i], digest, i * 4);
        }
        return digest;
    }

    public String takeDigestAsNumber(byte[] byteData){
        return new BigInteger(byteData).toString();
    }

    public String takeDigestInHexString(byte[] byteData) {
        return byteArrayToHexString(takeDigestInBytes(byteData));
    }
}
