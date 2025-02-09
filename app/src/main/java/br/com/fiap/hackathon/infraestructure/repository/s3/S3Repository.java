package br.com.fiap.hackathon.infraestructure.repository.s3;

import br.com.fiap.hackathon.domain.interfaces.S3RepositoryInterface;
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

import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Repository implements S3RepositoryInterface {

    private final S3Client s3Client;
    @Value("${bucket.processamento.name}")
    private String bucketProcessamentoName;
    @Value("${bucket.frames.name}")
    private String bucketZippedFramesName;
    @Value("${video.destination-path}")
    private String diretorioDestino;

    @Override
    public void download(String key, Path path) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketProcessamentoName)
                    .key(key)
                    .build();
            s3Client.getObject(getObjectRequest, path);
        } catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void upload(String key, Path path) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketZippedFramesName)
                    .key(key)
                    .contentType("application/zip")
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromFile(path));
        } catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public URL presignGetObject(String key, int diasExpiracao) {
        try {
            try (S3Presigner presigner = S3Presigner.create()) {
                GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofDays(diasExpiracao))
                        .getObjectRequest(req -> req.bucket(bucketZippedFramesName).key(key))
                        .build();
                PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
                return presignedRequest.url();
            }
        } catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }
}
