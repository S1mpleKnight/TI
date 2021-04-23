package by.bsuir.ti.third.decorator;

import by.bsuir.ti.third.cipher.SimpleCipher;

import java.math.BigInteger;
import java.util.List;

public class SimpleDecorator implements SimpleCipher {
    protected SimpleCipher cipher;

    public SimpleDecorator(SimpleCipher cipher) {
        this.cipher = cipher;
    }

    @Override
    public List<BigInteger> encrypt(byte[] fileBytes) {
        return cipher.encrypt(fileBytes);
    }

    @Override
    public List<BigInteger> decrypt(byte[] fileBytes) {
        return cipher.decrypt(fileBytes);
    }
}
