package by.bsuir;

import java.util.ArrayList;

public class LFSR implements Cipher {
    private static final int twentyFifthPositionMask = 0b0000_0001_0000_0000_0000_0000_0000_0000;
    private static final int thirdPositionMask = 0b0000_0000_0000_0000_0000_0000_0000_0100;
    private static final long keyMask = 0b00000000_00000000_00000000_00000001_11111110_00000000_00000000_00000000L;

    private final byte[] message;
    private final ArrayList<Byte> list;
    private int key;

    public LFSR(int key, byte[] message) {
        this.message = message;
        this.key = key;
        this.list = new ArrayList<>();
    }

    @Override
    public byte[] encrypt() {
        byte[] encryptBytes = new byte[message.length];
        for (int i = 0; i < message.length; i++) {
            encryptBytes[i] = manipulateByte(message[i]);
        }
        return encryptBytes;
    }

    private byte manipulateByte(byte sequence) {
        byte keyByte = createNextKeyByte();
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
        byte key = (byte) (0xFF & (temp & keyMask >>> 26));
        list.add(key);
        return key;
    }

    private int calculateLastBit(long temp) {
        String binary = Integer.toBinaryString((int) temp);
        if (binary.length() < 3){
            System.out.println("first");
            return 0;
        }
        int third = binary.charAt(binary.length() - 3) - '0';
        if (binary.length() < 25){
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
        for (Byte bytek : list){
            sb.append(Integer.toBinaryString(bytek));
            sb.append(" ");
        }
        return sb.toString();
    }
}
