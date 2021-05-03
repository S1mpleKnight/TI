package by.bsuir.ti.third.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class SomeMath {
    public static final int AMOUNT_OF_BYTES_FOR_SYMBOL = 8;
    private static final int AMOUNT_OF_CHECKS = 10;

    //x = a^z mod n
    public static BigInteger fastExpMod(BigInteger a, BigInteger z, BigInteger n){
        BigInteger a1 = new BigInteger(a.toString());                               //a1 = a
        BigInteger z1 = new BigInteger(z.toString());                               //z1 = z
        BigInteger x = BigInteger.valueOf(1);                                       //x = 1
        while (z1.compareTo(BigInteger.ZERO) != 0){                                 //while z1 != 0
            while (z1.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0){  //while z1 mod 2 == 0
                BigInteger last = z1.mod(BigInteger.valueOf(2));                    //z1 = z1 div 2
                z1 = z1.subtract(last).divide(BigInteger.valueOf(2));               //a1 = a1 * a1 mod n
                a1 = a1.pow(2).mod(n);
            }
            z1 = z1.subtract(BigInteger.ONE);                                       //z1 = z1 - 1
            x = x.multiply(a1).mod(n);                                              //x = x * a1 mod n
        }
        return x;
    }
    
    public static BigInteger fastInvExpMod(BigInteger a, BigInteger z, BigInteger n){
        BigDecimal a1 = new BigDecimal(a.toString());
        a1 = BigDecimal.ONE.divide(a1, 10, RoundingMode.CEILING);
        BigDecimal z1 = new BigDecimal(z.toString());
        BigDecimal x = BigDecimal.valueOf(1);
        BigDecimal an = new BigDecimal(n.toString());
        while (z1.compareTo(BigDecimal.ZERO) != 0){
            while (z1.remainder(BigDecimal.valueOf(2)).compareTo(BigDecimal.ZERO) == 0){
                BigDecimal last = z1.remainder(BigDecimal.valueOf(2));
                z1 = z1.subtract(last).divide(BigDecimal.valueOf(2), 10, RoundingMode.CEILING);
                a1 = a1.pow(2).remainder(an);
            }
            z1 = z1.subtract(BigDecimal.ONE);
            x = x.multiply(a1).remainder(an);
        }
        return x.toBigInteger();
    }

    public static BigInteger calculateEulerFunction(BigInteger p) {
        if (isPrime(p)) {
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
            if (fastExpMod(BigInteger.valueOf(g), eulerResult, p).compareTo(BigInteger.ONE) != 0
                    || phi.gcd(BigInteger.valueOf(g)).compareTo(BigInteger.ONE) != 0) {
                continue;
            }
            boolean state = isPrimitiveRoot(p, eulerResult, g, phi);
            if (state) {
                roots.add(BigInteger.valueOf(g));
            }
        }
        return roots;
    }

    private static boolean isPrimitiveRoot(BigInteger p, BigInteger eulerResult, int g, BigInteger phi) {
        boolean state = true;
        for (int i = 2; BigInteger.valueOf((long) i * i).compareTo(phi) < 1 && state; i++) {
            if (phi.mod(BigInteger.valueOf(i)).compareTo(BigInteger.ZERO) == 0) {
                if (fastExpMod(BigInteger.valueOf(g), eulerResult.divide(BigInteger.valueOf(i)), p)
                        .compareTo(BigInteger.ONE) == 0) {
                    state = false;
                } else {
                    while (phi.mod(BigInteger.valueOf(i)).compareTo(BigInteger.ZERO) == 0) {
                        phi = phi.divide(BigInteger.valueOf(i));
                    }
                }
            }
        }
        return state;
    }

    public static List<Byte> fixSizeOfNumbers(List<BigInteger> numbers) {
        List<Byte> updatedValues = new ArrayList<>();
        byte[] anotherBytes, bytes;
        for (BigInteger number : numbers) {
            bytes = number.toByteArray();
            anotherBytes = new byte[AMOUNT_OF_BYTES_FOR_SYMBOL];
            System.arraycopy(bytes, 0, anotherBytes, anotherBytes.length - bytes.length, bytes.length);
            for (byte bytik : anotherBytes) {
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
                if (i == 0 && byteArray.length >= 2) {
                    if (byteArray[i] == 0 && byteArray[i + 1] < 0) {
                        continue;
                    }
                }
                bytes.add(byteArray[i]);
            }
        }
        return takeByteArray(bytes);
    }

    public static boolean isPrime(BigInteger number) {
        return millerRabinTest(number);
        //  return number.isProbablePrime(AMOUNT_OF_CHECKS);
    }

    private static boolean millerRabinTest(BigInteger n) {
        if (n.compareTo(BigInteger.valueOf(2)) == 0 || n.compareTo(BigInteger.valueOf(3)) == 0) {
            return true;
        }
        if (n.compareTo(BigInteger.TWO) < 0 || n.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0) {
            return false;
        }
        // представим n − 1 в виде (2^s)·t, где t нечётно, это можно сделать последовательным делением n - 1 на 2
        BigInteger t = n.subtract(BigInteger.ONE);
        int s = 0;
        while (t.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0) {
            t = t.divide(BigInteger.TWO);
            s += 1;
        }
        // повторить AMOUNT_OF_CHECKS раз
        for (int i = 0; i < AMOUNT_OF_CHECKS; i++) {
            // выберем криптослучайное целое число a в отрезке [2, n − 2]
            SecureRandom rng = new SecureRandom();
            byte[] arr = new byte[n.toByteArray().length];
            BigInteger a;
            do {
                rng.nextBytes(arr);
                a = new BigInteger(arr);
            } while (a.compareTo(BigInteger.TWO) < 0 || a.compareTo(n.subtract(BigInteger.TWO)) >= 0);
            // x ← a^t mod n, вычислим с помощью возведения в степень по модулю
            BigInteger x = SomeMath.fastExpMod(a, t, n);
            // если x == 1 или x == n − 1, то перейти на следующую итерацию цикла
            if (x.compareTo(BigInteger.ONE) == 0 || x.compareTo(n.subtract(BigInteger.ONE)) == 0)
                continue;
            // повторить s − 1 раз
            for (int r = 1; r < s; r++) {
                // x ← x^2 mod n
                x = SomeMath.fastExpMod(x, BigInteger.TWO, n);
                // если x == 1, то вернуть "составное"
                if (x.compareTo(BigInteger.ONE) == 0)
                    return false;
                // если x == n − 1, то перейти на следующую итерацию внешнего цикла
                if (x.compareTo(n.subtract(BigInteger.ONE)) == 0)
                    break;
            }
            if (x.compareTo(n.subtract(BigInteger.ONE)) != 0)
                return false;
        }
        // вернуть "вероятно простое"
        return true;
    }
}
