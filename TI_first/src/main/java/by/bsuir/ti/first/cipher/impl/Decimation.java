package by.bsuir.ti.first.cipher.impl;

import by.bsuir.ti.first.cipher.api.Cipher;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Decimation implements Cipher {
    private static final int ALPHABET_LENGTH = 26;
    private static final List<Character> ALPHABET = Arrays.asList(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    );
    private final int key;
    private HashMap<Character, Character> encryptTable;

    public Decimation(int key) {
        this.key = key;
    }

    public static int getAlphabetLength() {
        return ALPHABET_LENGTH;
    }

    @Override
    public String encrypt(List<String> list) {
        createEncryptTable();
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            sb.append(encryptString(str));
        }
        return sb.toString();
    }

    public void createEncryptTable() {
        HashMap<Character, Character> encryptTable = new HashMap<>();
        for (int i = 0; i < ALPHABET_LENGTH; i++) {
            encryptTable.put(ALPHABET.get(i), calculateEncryptSymbol(i + 1, this.key));
        }
        this.setEncryptTable(encryptTable);
    }

    private char calculateEncryptSymbol(int position, int key) {
        return ALPHABET.get((position * key) % ALPHABET_LENGTH);
    }

    private void setEncryptTable(HashMap<Character, Character> encryptTable) {
        this.encryptTable = encryptTable;
    }

    private String encryptString(String str) {
        str = str.toLowerCase();
        StringBuilder sb = new StringBuilder();
        for (String letter : str.split("")) {
            if (letter.length() == 0) {
                continue;
            }
            sb.append(encryptLetter(letter.charAt(0)));
        }
        return sb.toString();
    }

    public String encryptLetter(char letter) {
        if (isNeededLetter(letter)) {
            return String.valueOf(encryptTable.get(letter));
        } else {
            return "";
        }
    }

    private boolean isNeededLetter(char letter) {
        return letter >= 97 && letter <= 122;
    }

    public char decryptLetter(char letter) {
        if (isNeededLetter(letter)) {
            return calculateDecryptSymbol(letter);
        } else {
            return Character.MIN_VALUE;
        }
    }

    private Character calculateDecryptSymbol(char symbol) {
        if (symbol == 'a') {
            return 'z';
        }
        int calculationResult = getPositionInAlphabet(symbol);
        while (calculationResult % key - 1 != 0) {
            calculationResult += ALPHABET_LENGTH;
        }
        return ALPHABET.get(calculationResult / this.key - 1);
    }

    private int getPositionInAlphabet(char letter) {
        int result = -1;
        for (int i = 0; i < ALPHABET.size(); i++) {
            Character symbol = ALPHABET.get(i);
            if (letter == symbol) {
                result = i;
            }
        }
        return result + 1;
    }

    @Override
    public String decrypt(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            for (String letter : str.toLowerCase().split("")) {
                if (letter.length() != 0) {
                    sb.append(decryptLetter(letter.charAt(0)));
                }
            }
        }
        if (sb.length() == 0){
            return "";
        } else {
            return checkDecryptString(sb.toString());
        }
    }

    private String checkDecryptString(String str) {
        return Arrays.stream(str
                .split(""))
                .map(s -> s.charAt(0))
                .filter(letter -> letter != Character.MIN_VALUE)
                .map(Object::toString)
                .collect(Collectors.joining(""));
    }
}
