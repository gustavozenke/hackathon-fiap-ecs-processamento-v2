package br.com.fiap.hackathon.domain.entities.enums;

public enum TipoComunicacao {
    SMS,
    EMAIL;

    public static TipoComunicacao fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("O valor não pode ser nulo ou vazio.");
        }

        try {
            return TipoComunicacao.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de comunicação inválido: " + value);
        }
    }
}
