package br.com.fiap.hackathon.domain.interfaces;

public interface GravarEventoProcessamentoInterface {
    void gravarEventoProcessamento(String nomeVideo, String nomeUsuario, String statusProcessamento, String urlVideo);
}
