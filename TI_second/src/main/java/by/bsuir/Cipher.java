package by.bsuir;

import java.util.List;

public interface Cipher {
    String encrypt();

    List<Byte> decrypt();
}
