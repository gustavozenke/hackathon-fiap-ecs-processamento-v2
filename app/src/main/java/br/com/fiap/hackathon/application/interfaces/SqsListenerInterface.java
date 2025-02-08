package br.com.fiap.hackathon.application.interfaces;

public interface SqsListenerInterface {
    void onMessage(String message);
}
