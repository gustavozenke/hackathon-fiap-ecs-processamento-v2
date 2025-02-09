package br.com.fiap.hackathon.domain.interfaces;

public interface SqsRepositoryInterface {
    void send(String queueUrl, String messageBody);
}
