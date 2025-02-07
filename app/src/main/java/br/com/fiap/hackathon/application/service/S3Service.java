package br.com.fiap.hackathon.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    @Value("${bucket.name}")
    private String bucketName;
    @Value("${bucket.destination.path}")
    private String diretorioDestino;

    public Path download(String key) throws IOException {
        log.info("Buscando arquivo {} no bucket {}", key, bucketName);

        Path diretorioDestinoPath = Paths.get(diretorioDestino);
        Path videoPath = diretorioDestinoPath.resolve(key);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        var response = s3Client.getObject(getObjectRequest, videoPath);

        log.info("Arquivo {} encontrado com sucesso. Response={}", key, response);
        return videoPath;
    }

    public void upload(Path diretorioSaida, String key) {
        log.info("Realizando upload do arquivo {} no bucket {}", key, bucketName);

        var path = diretorioSaida.resolve(String.format("%s.zip", key));

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .contentType("application/zip")
            .build();

            var response = s3Client.putObject(putObjectRequest, RequestBody.fromFile(path));

            log.info("Upload realizado com sucesso. Response={}", response);
    }
}
