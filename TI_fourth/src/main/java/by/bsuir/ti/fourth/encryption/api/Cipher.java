package by.bsuir.ti.fourth.encryption.api;

public interface Cipher {
    byte[] encrypt(byte[] data);

    byte[] decrypt(byte[] data);
}
