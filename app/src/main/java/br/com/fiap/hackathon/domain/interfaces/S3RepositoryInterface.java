package br.com.fiap.hackathon.domain.interfaces;

import java.net.URL;
import java.nio.file.Path;

public interface S3RepositoryInterface {
    void download(String key, Path path);
    void upload(String key, Path path);
    URL presignGetObject(String key, int diasExpiracao);
}
