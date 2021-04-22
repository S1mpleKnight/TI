package by.bsuir.ti.third.cipher;

import java.util.List;

public interface SimpleCipher {
    List<Byte> encrypt(byte[] fileBytes);

    List<Byte> decrypt(byte[] fileBytes);
}
