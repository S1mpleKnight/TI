package by.bsuir.ti.fourth.hash.api;

public interface HashFunction {
    byte[] takeDigestInBytes(byte[] byteData);

    String takeDigestAsNumber(byte[] byteData);

    String takeDigestInHexString(byte[] byteData);
}
