package by.bsuir.ti.third.cipher;

import java.math.BigInteger;
import java.util.List;

public interface SimpleCipher {
    List<BigInteger> encrypt(byte[] fileBytes);

    List<BigInteger> decrypt(byte[] fileBytes);
}
