package by.bsuir.ti.first.cipher.impl;

import by.bsuir.ti.first.cipher.api.Cipher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Column implements Cipher {
    private static final List<Character> ALPHABET = Arrays.asList(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    );
    private final String key;
    private final List<StringBuilder> matrix;

    public Column(String key) {
        this.key = key;
        this.matrix = new ArrayList<>(key.length());
    }

    @Override
    public String encrypt(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            sb.append(str.toLowerCase());
        }
        return encryptText(sb.toString());
    }

    @Override
    public String decrypt(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            sb.append(str.toLowerCase());
        }
        return decryptText(sb.toString());
    }

    private String decryptText(String text) {
        String neededText = initialization(text);
        int numberOfRow = (int) Math.ceil((double) neededText.length() / key.length());
        int fullColumns = neededText.length() % key.length() - 1;
        String order = Arrays.stream(key.split("")).sorted().collect(Collectors.joining(""));
        StringBuilder orderKey = new StringBuilder(order);
        StringBuilder keyCopy = new StringBuilder(key);
        int cursor = 0;
        decryptedColumnFormatting(neededText, numberOfRow, fullColumns, orderKey, keyCopy, cursor);
        return takeDecryptedText(neededText);
    }

    private void decryptedColumnFormatting(String neededText, int numberOfRow, int fullColumns,
                                           StringBuilder orderKey, StringBuilder keyCopy, int cursor) {
        for (int i = 0; i < orderKey.length(); i++) {
            int pos = keyCopy.indexOf(String.valueOf(orderKey.charAt(i)));
            int columnSize;
            if (neededText.length() % key.length() != 0) {
                columnSize = pos > fullColumns
                        ? numberOfRow - 1
                        : numberOfRow;
            } else {
                columnSize = numberOfRow;
            }
            matrix.get(pos).append(neededText, cursor, cursor + columnSize);
            keyCopy.replace(pos, pos + 1, "x");
            cursor += columnSize;
        }
    }

    private String takeDecryptedText(String neededText) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < neededText.length(); i++) {
            if (matrix.get(i % key.length()).length() == 0) {
                continue;
            }
            sb.append(matrix.get(i % key.length()).toString().charAt(0));
            matrix.get(i % key.length()).deleteCharAt(0);
        }
        return sb.toString();
    }

    private String encryptText(String text) {
        String neededText = initialization(text);
        for (int i = 0; i < neededText.length(); i++) {
            matrix.get(i % key.length()).append(neededText.charAt(i));
        }
        return encryptionTextFormatting();
    }

    private String encryptionTextFormatting() {
        StringBuilder sb = new StringBuilder();
        for (Character character : ALPHABET) {
            for (int j = 0; j < key.length(); j++) {
                if (character == key.charAt(j)) {
                    sb.append(matrix.get(j));
                }
            }
        }
        return sb.toString();
    }

    private String initialization(String text) {
        for (int i = 0; i < key.length(); i++) {
            matrix.add(new StringBuilder());
        }
        return String.join("", text.split("[^a-z]"));
    }
}
