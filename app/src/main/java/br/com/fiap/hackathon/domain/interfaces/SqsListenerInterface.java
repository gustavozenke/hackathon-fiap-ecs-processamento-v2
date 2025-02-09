package br.com.fiap.hackathon.domain.interfaces;

public interface SqsListenerInterface {
    void onMessage(String message);
}
