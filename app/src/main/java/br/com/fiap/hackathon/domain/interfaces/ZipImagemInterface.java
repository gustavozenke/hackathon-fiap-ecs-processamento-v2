package br.com.fiap.hackathon.domain.interfaces;

import java.nio.file.Path;

public interface ZipImagemInterface {
    Path zipImagens(Path diretorioVideo, String nomeSemExtensao);
}
