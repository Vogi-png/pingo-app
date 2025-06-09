// Local: app/src/main/java/com/example/taskapp/Cartao.java
package com.example.taskapp;

import com.google.firebase.firestore.DocumentId; // <-- PASSO 1.1: IMPORTAR

public class Cartao {

    @DocumentId // <-- PASSO 1.2: Adicionar esta anotação
    private String documentId; // <-- PASSO 1.3: Adicionar o campo para o ID

    // Seus campos existentes
    private String cardholderName;
    private String lastFourDigits;
    private String expirationDate;
    private String cardBrand;

    // Construtor vazio (obrigatório para o Firestore)
    public Cartao() {}

    // --- PASSO 1.4: Adicionar o Getter para o ID ---
    public String getDocumentId() {
        return documentId;
    }

    // --- Seus outros Getters ---
    public String getCardholderName() { return cardholderName; }
    public String getLastFourDigits() { return lastFourDigits; }
    public String getExpirationDate() { return expirationDate; }
    public String getCardBrand() { return cardBrand; }
}