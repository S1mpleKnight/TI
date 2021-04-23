package by.bsuir.ti.third.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SomeMath {
    private static final int AMOUNT_OF_CHECKS = 10;
    public static final int AMOUNT_OF_BYTES_FOR_SYMBOL = 8;

    public static boolean isPrime(BigInteger number) {
        return number.isProbablePrime(AMOUNT_OF_CHECKS);
    }

    public static BigInteger calculateEulerFunction(BigInteger p) {
        if (isPrime(p)){
            return p.subtract(BigInteger.ONE);
        }
        BigInteger result = p;
        for (int i = 2; BigInteger.valueOf((long) i * i).compareTo(p) <= 0; ++i)
            if (p.mod(BigInteger.valueOf(i)).compareTo(BigInteger.ZERO) == 0) {
                while (p.mod(BigInteger.valueOf(i)).compareTo(BigInteger.ZERO) == 0) {
                    p = p.divide(BigInteger.valueOf(i));
                }
                result = result.subtract(result.divide(BigInteger.valueOf(i)));
            }
        if (p.compareTo(BigInteger.ONE) > 0)
            result = result.subtract(result.divide(p));
        return result;
    }

    public static List<BigInteger> takePrimitiveRoots(BigInteger p) {
        List<BigInteger> roots = new ArrayList<>();
        BigInteger eulerResult = calculateEulerFunction(p);
        BigInteger amountOfRoots = calculateEulerFunction(eulerResult);
        for (int g = 1; BigInteger.valueOf(roots.size()).compareTo(amountOfRoots) < 0; g++) {
            BigInteger phi = p.subtract(BigInteger.ONE);
            if (BigInteger.valueOf(g).modPow(eulerResult, p).compareTo(BigInteger.ONE) != 0
                || phi.gcd(BigInteger.valueOf(g)).compareTo(BigInteger.ONE) != 0){
                continue;
            }
            boolean state = isPrimitiveRoot(p, eulerResult, g, phi);
            if (state){
                roots.add(BigInteger.valueOf(g));
            }
        }
        return roots;
    }

    private static boolean isPrimitiveRoot(BigInteger p, BigInteger eulerResult, int g, BigInteger phi) {
        boolean state = true;
        for (int i = 2; BigInteger.valueOf((long) i * i).compareTo(phi) < 1 && state; i++) {
            if (phi.mod(BigInteger.valueOf(i)).compareTo(BigInteger.ZERO) == 0) {
                if (BigInteger.valueOf(g).modPow(eulerResult.divide(BigInteger.valueOf(i)), p)
                        .compareTo(BigInteger.ONE) == 0){
                    state = false;
                } else {
                    while (phi.mod(BigInteger.valueOf(i)).compareTo(BigInteger.ZERO) == 0){
                        phi = phi.divide(BigInteger.valueOf(i));
                    }
                }
            }
        }
        return state;
    }

    public static List<Byte> fixSizeOfNumbers(List<BigInteger> numbers){
        List<Byte> updatedValues = new ArrayList<>();
        byte[] anotherBytes, bytes;
        for (BigInteger number : numbers){
            bytes = number.toByteArray();
            anotherBytes = new byte[AMOUNT_OF_BYTES_FOR_SYMBOL];
            System.arraycopy(bytes, 0, anotherBytes, anotherBytes.length - bytes.length, bytes.length);
            for (byte bytik : anotherBytes){
                updatedValues.add(bytik);
            }
        }
        return updatedValues;
    }

    public static byte[] takeEncryptedBytes(List<BigInteger> numbers) {
        List<Byte> bytes = fixSizeOfNumbers(numbers);
        return takeByteArray(bytes);
    }

    private static byte[] takeByteArray(List<Byte> numbers) {
        byte[] bytes = new byte[numbers.size()];
        for (int i = 0; i < numbers.size(); i++) {
            bytes[i] = numbers.get(i);
        }
        return bytes;
    }

    public static byte[] takeDecryptedBytes(List<BigInteger> numbers) {
        List<Byte> bytes = new ArrayList<>();
        for (BigInteger number : numbers) {
            byte[] byteArray = number.toByteArray();
            for (int i = 0; i < byteArray.length; i++) {
                if (i == 0 && byteArray.length >= 2){
                    if (byteArray[i] == 0 && byteArray[i+1] < 0){
                        continue;
                    }
                }
                bytes.add(byteArray[i]);
            }
        }
        return takeByteArray(bytes);
    }
}
