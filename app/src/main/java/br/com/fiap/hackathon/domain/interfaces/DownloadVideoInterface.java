package br.com.fiap.hackathon.domain.interfaces;

import java.nio.file.Path;

public interface DownloadVideoInterface {
    Path download(String key);
}
