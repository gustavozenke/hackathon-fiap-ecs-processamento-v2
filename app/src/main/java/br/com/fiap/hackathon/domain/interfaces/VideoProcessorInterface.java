package br.com.fiap.hackathon.domain.interfaces;

import java.nio.file.Path;

public interface VideoProcessorInterface {
    void extrairFrames(Path diretorioVideo, Path diretorioSaida, int intervalo);
}
