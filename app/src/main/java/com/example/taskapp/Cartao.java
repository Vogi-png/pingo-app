// Arquivo: Cartao.java
package com.example.taskapp;

public class Cartao {
    private String cardholderName;
    private String lastFourDigits;
    private String expirationDate;
    private String cardBrand; // <-- CAMPO ADICIONADO

    // Construtor vazio é OBRIGATÓRIO para o Firestore
    public Cartao() {}

    // Getters
    public String getCardholderName() {
        return cardholderName;
    }

    public String getLastFourDigits() {
        return lastFourDigits;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    // Getter para o novo campo
    public String getCardBrand() { // <-- GETTER ADICIONADO
        return cardBrand;
    }
}