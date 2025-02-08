package br.com.fiap.hackathon.application.interfaces;

public interface SqsRepositoryInterface {
    void send(String queueUrl, String messageBody);
}
