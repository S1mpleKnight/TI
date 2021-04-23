package by.bsuir.ti.third.decorator;

import by.bsuir.ti.third.cipher.SimpleCipher;
import by.bsuir.ti.third.util.FileWorker;

import java.math.BigInteger;
import java.util.List;

public final class OutputDecorator extends SimpleDecorator{
    private static final String ENCRYPTED_INFO = "encrypt.txt";
    private static final String DECRYPTED_INFO = "decrypt.txt";

    public OutputDecorator(SimpleCipher cipher) {
        super(cipher);
    }

    @Override
    public List<BigInteger> encrypt(byte[] fileBytes) {
        List<BigInteger> result = super.encrypt(fileBytes);
        FileWorker.writeNumbers(result, ENCRYPTED_INFO);
        return result;
    }

    @Override
    public List<BigInteger> decrypt(byte[] fileBytes) {
        List<BigInteger> result = super.decrypt(fileBytes);
        FileWorker.writeNumbers(result, DECRYPTED_INFO);
        return result;
    }
}
