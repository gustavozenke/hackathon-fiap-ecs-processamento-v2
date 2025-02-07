package br.com.fiap.hackathon.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    @Value("${bucket.processamento.name}")
    private String bucketProcessamentoName;
    @Value("${bucket.frames.name}")
    private String bucketZippedFramesName;
    @Value("${video.destination-path}")
    private String diretorioDestino;

    public Path download(String key) throws IOException {
        log.info("Buscando arquivo {} no bucket {}", key, bucketProcessamentoName);

        Path diretorioDestinoPath = Paths.get(diretorioDestino);
        Path videoPath = diretorioDestinoPath.resolve(key);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketProcessamentoName)
                .key(key)
                .build();

        var response = s3Client.getObject(getObjectRequest, videoPath);

        log.info("Arquivo {} encontrado com sucesso. Response={}", key, response);
        return videoPath;
    }

    public void upload(Path diretorioSaida, String key) {
        log.info("Realizando upload do arquivo {} no bucket {}", key, bucketZippedFramesName);

        var path = diretorioSaida.resolve(String.format("%s.zip", key));

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketZippedFramesName)
            .key(String.format("%s.zip", key))
            .contentType("application/zip")
            .build();

        var response = s3Client.putObject(putObjectRequest, RequestBody.fromFile(path));

        log.info("Upload realizado com sucesso. Response={}", response);
    }

    public URL generatePresignedUrl(String key, int expirationDays) {
        log.info("Gerando URL pré-assinada para o objeto {} no bucket {}", key, bucketZippedFramesName);

        try (S3Presigner presigner = S3Presigner.create()) {
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofDays(expirationDays))
                    .getObjectRequest(req -> req.bucket(bucketZippedFramesName).key(String.format("%s.zip", key)))
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            URL url = presignedRequest.url();

            log.info("URL pré-assinada gerada com sucesso: {}", url);
            return url;
        }
    }
}
