package br.com.fiap.hackathon.application.interfaces;

import java.nio.file.Path;

public interface DownloadVideoInterface {
    Path download(String key);
}
