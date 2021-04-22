package by.bsuir.ti.third.cipher;

import java.util.List;

public interface SimpleCipher {
    byte[] encrypt(byte[] fileBytes);

    byte[] decrypt(byte[] fileBytes);
}
