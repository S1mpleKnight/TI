package by.bsuir.ti.fourth.math;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Math {
    private final static int AMOUNT_OF_CHECKS = 10;

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

    public static boolean isPrime(BigInteger number) {
        return millerRabinTest(number);
    }

    private static boolean millerRabinTest(BigInteger n) {
        if (n.compareTo(BigInteger.valueOf(2)) == 0 || n.compareTo(BigInteger.valueOf(3)) == 0) {
            return true;
        }
        if (n.compareTo(BigInteger.TWO) < 0 || n.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0) {
            return false;
        }
        BigInteger t = n.subtract(BigInteger.ONE);
        int s = 0;
        while (t.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0) {
            t = t.divide(BigInteger.TWO);
            s += 1;
        }
        for (int i = 0; i < AMOUNT_OF_CHECKS; i++) {
            SecureRandom rng = new SecureRandom();
            byte[] arr = new byte[n.toByteArray().length];
            BigInteger a;
            do {
                rng.nextBytes(arr);
                a = new BigInteger(arr);
            } while (a.compareTo(BigInteger.TWO) < 0 || a.compareTo(n.subtract(BigInteger.TWO)) >= 0);
            BigInteger x = fastExpMod(a, t, n);
            if (x.compareTo(BigInteger.ONE) == 0 || x.compareTo(n.subtract(BigInteger.ONE)) == 0)
                continue;
            for (int r = 1; r < s; r++) {
                x = fastExpMod(x, BigInteger.TWO, n);
                if (x.compareTo(BigInteger.ONE) == 0)
                    return false;
                if (x.compareTo(n.subtract(BigInteger.ONE)) == 0)
                    break;
            }
            if (x.compareTo(n.subtract(BigInteger.ONE)) != 0)
                return false;
        }
        return true;
    }

    public static BigInteger fastExpMod(BigInteger a, BigInteger z, BigInteger n) {
        BigInteger a1 = new BigInteger(a.toString());                               //a1 = a
        BigInteger z1 = new BigInteger(z.toString());                               //z1 = z
        BigInteger x = BigInteger.valueOf(1);                                       //x = 1
        while (z1.compareTo(BigInteger.ZERO) != 0) {                                 //while z1 != 0
            while (z1.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0) {  //while z1 mod 2 == 0
                BigInteger last = z1.mod(BigInteger.valueOf(2));                    //z1 = z1 div 2
                z1 = z1.subtract(last).divide(BigInteger.valueOf(2));               //a1 = a1 * a1 mod n
                a1 = a1.pow(2).mod(n);
            }
            z1 = z1.subtract(BigInteger.ONE);                                       //z1 = z1 - 1
            x = x.multiply(a1).mod(n);                                              //x = x * a1 mod n
        }
        return x;
    }

    public static BigInteger[] extendedEuclideanAlgorithm(BigInteger phi, BigInteger d) {
        BigInteger d0 = new BigInteger(phi.toString());
        BigInteger d1 = new BigInteger(d.toString());
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
}
