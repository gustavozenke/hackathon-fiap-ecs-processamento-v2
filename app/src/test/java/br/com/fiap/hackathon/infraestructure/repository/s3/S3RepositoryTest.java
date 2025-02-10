package br.com.fiap.hackathon.infraestructure.repository.s3;

import br.com.fiap.hackathon.domain.interfaces.S3RepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class S3RepositoryTest {

    private S3Client mockS3Client;
    private S3Presigner mockS3Presigner;
    private S3RepositoryInterface s3Repository;
    private Path tempFile;

    @BeforeEach
    void setUp() {
        mockS3Client = mock(S3Client.class);
        mockS3Presigner = mock(S3Presigner.class);
        s3Repository = new S3Repository(mockS3Client);

        ReflectionTestUtils.setField(s3Repository, "bucketProcessamentoName", "bucket-processamento-teste");
        ReflectionTestUtils.setField(s3Repository, "bucketZippedFramesName", "bucket-zip-teste");
        ReflectionTestUtils.setField(s3Repository, "bucketZippedFramesName", "/destino");
    }

    @Test
    void deveRealizarDownloadComSucesso() {
        // Arrange
        Path mockPath = Paths.get("mock/path");

        // Act
        s3Repository.download("mock-key", mockPath);

        // Assert
        verify(mockS3Client, times(1)).getObject(any(GetObjectRequest.class), any(Path.class));
    }

    @Test
    void deveRealizarUploadComSucesso() throws IOException {
        // Arrange
        tempFile = Files.createTempFile("testFile", ".zip");
        tempFile.toFile().deleteOnExit(); // Garante a exclusão após o término do teste

        // Act
        s3Repository.upload("file-key", tempFile);

        // Assert
        verify(mockS3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void deveGerarUrlPreAssinada() throws MalformedURLException {
        // Arrange
        var url = new URL("https://s3.amazonaws.com/bucket/video.mp4?presigned=true");
        String key = "key-teste";
        int mockDiasExpiracao = 7;
        var mockPresignedRequest = mock(PresignedGetObjectRequest.class);

        mockStatic(S3Presigner.class);
        when(S3Presigner.create()).thenReturn(mockS3Presigner);
        when(mockS3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(mockPresignedRequest);
        when(mockPresignedRequest.url()).thenReturn(url);

        // Act
        URL resultUrl = s3Repository.presignGetObject(key, mockDiasExpiracao);

        // Assert
        assertEquals(url, resultUrl);
    }
}