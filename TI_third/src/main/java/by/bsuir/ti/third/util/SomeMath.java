package by.bsuir.ti.third.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public final class SomeMath {
    private static final int AMOUNT_OF_CHECKS = 10;
    private static final int AMOUNT_OF_BYTES_FOR_SYMBOL = 10;

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

    public static List<BigInteger> fixSizeOfNumbers(List<BigInteger> numbers){
        List<BigInteger> updatedValues = new ArrayList<>();
        byte[] anotherBytes, bytes;
        for (BigInteger number : numbers){
            bytes = number.toByteArray();
            anotherBytes = new byte[AMOUNT_OF_BYTES_FOR_SYMBOL];
            System.arraycopy(bytes,
                    0, anotherBytes,
                    anotherBytes.length - bytes.length,
                    bytes.length-1);
            updatedValues.add(new BigInteger(1, anotherBytes));
        }
        return updatedValues;
    }
}
