package by.bsuir.ti.first.cipher.impl;

import by.bsuir.ti.first.cipher.api.Cipher;

import java.util.Arrays;
import java.util.List;

public class Vigener implements Cipher {
    private final static List<Character> ALPHABET = Arrays.asList(
            'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н',
            'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ь', 'ы', 'ъ',
            'э', 'ю', 'я'
    );
    private final String key;
    private int counter;
    private int order;

    public Vigener(String key) {
        this.key = key;
        this.counter = 0;
        this.order = 0;
    }

    private int takePosition(char symbol) {
        return ALPHABET.indexOf(symbol);
    }

    private boolean isNeededLetter(char letter) {
        return ALPHABET.contains(letter);
    }

    private String encryptString(String str) {
        StringBuilder sb = new StringBuilder();
        for (String symbol : str.split("")) {
            sb.append(encryptLetter(symbol));
        }
        return sb.toString();
    }

    private String encryptLetter(String symbol) {
        char letter = symbol.charAt(0);
        if (isNeededLetter(letter)) {
            int letterPos = takePosition(letter);
            int keyPos = calculateKeyLetterPosition();
            return String.valueOf(ALPHABET.get((letterPos + keyPos) % ALPHABET.size()));
        } else {
            return "";
        }
    }

    private int calculateKeyLetterPosition() {
        int position = takePosition(key.charAt(order)) + counter;
        order++;
        if (order == key.length()) {
            counter += 1;
            counter %= ALPHABET.size();
            order = 0;
        }
        return position;
    }

    @Override
    public String encrypt(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            str = String.join("", str.split("[^а-яА-ЯЁё]"));
            if (str.length() != 0){
                sb.append(encryptString(str.toLowerCase()));
            }
        }
        return sb.toString();
    }

    @Override
    public String decrypt(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String str : list) {
            str = String.join("", str.split("[^а-яА-ЯёЁ]"));
            if (str.length() != 0){
                sb.append(decryptString(str.toLowerCase()));
            }
        }
        return sb.toString();
    }

    private String decryptString(String str){
        StringBuilder sb = new StringBuilder();
        for (String symbol: str.split("")){
            sb.append(decryptLetter(symbol));
        }
        return sb.toString();
    }

    private String decryptLetter(String symbol){
        if (isNeededLetter(symbol.charAt(0))){
            return calculateDecryptedSymbol(symbol);
        } else {
            return "";
        }
    }

    private String calculateDecryptedSymbol(String symbol){
        int letterPos = takePosition(symbol.charAt(0));
        int keyPos = calculateKeyLetterPosition();
        return String.valueOf(ALPHABET.get((letterPos + ALPHABET.size() * key.length() - keyPos) % ALPHABET.size()));
    }
}
