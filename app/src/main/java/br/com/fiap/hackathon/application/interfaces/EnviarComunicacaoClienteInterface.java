package br.com.fiap.hackathon.application.interfaces;

public interface EnviarComunicacaoClienteInterface {
    void notificar(String nomeVideo, String nomeUsuario, boolean comunicacaoErro, String mensagem);
}
