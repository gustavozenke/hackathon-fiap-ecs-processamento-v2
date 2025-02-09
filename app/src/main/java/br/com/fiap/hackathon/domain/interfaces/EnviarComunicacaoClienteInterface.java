package br.com.fiap.hackathon.domain.interfaces;

public interface EnviarComunicacaoClienteInterface {
    void notificar(String nomeVideo, String nomeUsuario, boolean comunicacaoErro, String mensagem);
}
