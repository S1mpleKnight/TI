package by.bsuir;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LFSR implements Cipher {
    private static final int twentyFifthPositionMask = 0b0000_0001_0000_0000_0000_0000_0000_0000;
    private static final int thirdPositionMask = 0b0000_0000_0000_0000_0000_0000_0000_0100;
    private static final long keyMask = 0b00000000_00000000_00000000_00000001_11111110_00000000_00000000_00000000L;

    private final byte[] message;
    private int key;
    private final StringBuilder keyBuffer;

    public LFSR(int key, byte[] message) {
        this.message = message;
        this.key = key;
        this.keyBuffer = null;
    }

    @Override
    public String encrypt() {
        byte[] encryptBytes = new byte[message.length];
        for (int i = 0; i < message.length; i++) {
            encryptBytes[i] = manipulateByte(message[i]);
        }
        return new String(encryptBytes);
    }

    private byte manipulateByte(byte sequence) {
        byte keyByte = createNextKeyByte();
        return (byte) (keyByte ^ sequence);
    }

    private byte createNextKeyByte() {
        long temp = key & 0xFFFF_FFFF;
        for (int i = 0; i < 8; i++) {
            int bit = calculateLastBit();
            temp <<= 2;
            temp |= bit;
        }
        key = (int) temp;
        return (byte) (0xFF & (temp & keyMask >>> 26));
    }

    private int calculateLastBit() {
        if (((key & twentyFifthPositionMask) == twentyFifthPositionMask)
                && ((key & thirdPositionMask) == thirdPositionMask)
                || (((key & twentyFifthPositionMask) != twentyFifthPositionMask)
                && ((key & thirdPositionMask) != thirdPositionMask))) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public List<Byte> decrypt() {
        List<Byte> list = new ArrayList<>();
        for (byte b : message) {
            list.add(manipulateByte(b));
        }
        return list;
    }
}
