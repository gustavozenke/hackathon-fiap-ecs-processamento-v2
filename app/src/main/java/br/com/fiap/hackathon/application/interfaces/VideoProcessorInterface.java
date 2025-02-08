package br.com.fiap.hackathon.application.interfaces;

import java.nio.file.Path;

public interface VideoProcessorInterface {
    void extrairFrames(Path diretorioVideo, Path diretorioSaida, int intervalo);
}
