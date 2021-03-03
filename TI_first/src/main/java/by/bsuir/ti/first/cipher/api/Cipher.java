package by.bsuir.ti.first.cipher.api;

import java.util.List;

public interface Cipher {
    String encrypt(List<String> list);

    String decrypt(List<String> list);
}
