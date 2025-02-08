package br.com.fiap.hackathon.application.interfaces;

import java.nio.file.Path;

public interface ZipImagemInterface {
    Path zipImagens(Path diretorioVideo, String nomeSemExtensao);
}
