package by.bsuir;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class LFSR implements Cipher {
    public static final int BYTES_NUMBER = 10;
    private static final long KEY_MASK = 0b00000000_00000000_00000000_00000001_11111110_00000000_00000000_00000000L;
    private final byte[] message;
    private final ArrayList<Byte> textBitsList;
    private final ArrayList<Byte> encryptedBitsList;
    private final ArrayList<Byte> keyBitsList;
    private int key;

    public LFSR(int key, byte[] message) {
        this.message = message;
        this.key = key;
        this.keyBitsList = new ArrayList<>();
        this.textBitsList = new ArrayList<>();
        this.encryptedBitsList = new ArrayList<>();
    }

    @Override
    public byte[] encrypt() {
        byte[] encryptBytes = new byte[message.length];
        for (int i = 0; i < message.length; i++) {
            encryptBytes[i] = manipulateByte(message[i]);
            if (i < BYTES_NUMBER) {
                addTextByte(message[i]);
                addEncryptedByte(encryptBytes[i]);
            }
        }
        return encryptBytes;
    }

    private void addTextByte(byte eightBits) {
        textBitsList.add(eightBits);
    }

    private void addEncryptedByte(byte eightBits) {
        encryptedBitsList.add(eightBits);
    }

    private byte manipulateByte(byte sequence) {
        byte keyByte = createNextKeyByte();
        keyBitAddition(keyByte);
        return (byte) (keyByte ^ sequence);
    }

    private byte createNextKeyByte() {
        long temp = key;
        for (int i = 0; i < 8; i++) {
            int bit = calculateLastBit(temp);
            temp <<= 1;
            temp += bit;
        }
        key = (int) temp;
        return (byte) (0xFF & (temp & KEY_MASK >>> 26));
    }

    private int calculateLastBit(long temp) {
        String binary = Integer.toBinaryString((int) temp);
        if (binary.length() < 3) {
            System.out.println("first");
            return 0;
        }
        int third = binary.charAt(binary.length() - 3) - '0';
        if (binary.length() < 25) {
            return third;
        }
        int twentyFifth = binary.charAt(binary.length() - 25) - '0';
        return twentyFifth ^ third;
    }

    @Override
    public byte[] decrypt() {
        byte[] decryptBytes = new byte[message.length];
        for (int i = 0; i < message.length; i++) {
            decryptBytes[i] = manipulateByte(message[i]);
        }
        return decryptBytes;
    }

    @Override
    public String showKeyBytes() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BYTES_NUMBER; i++) {
            sb.append(Integer.toBinaryString(keyBitsList.get(i)));
            sb.append(" ");
        }
        return sb.toString();
    }


    private void keyBitAddition(byte key) {
        keyBitsList.add(key);
    }

    @Override
    public String showTextBytes() {
        return textBitsList
                .stream()
                .map(Integer::toBinaryString)
                .collect(Collectors.joining(" "));
    }

    @Override
    public String showEncryptedBytes() {
        return encryptedBitsList
                .stream()
                .map(Integer::toBinaryString)
                .collect(Collectors.joining(" "));
    }
}
