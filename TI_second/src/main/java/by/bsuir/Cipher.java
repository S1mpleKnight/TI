package by.bsuir;

public interface Cipher {
    byte[] encrypt();

    byte[] decrypt();

    String showKeyBytes();

    String showTextBytes();

    String showEncryptedBytes();
}
