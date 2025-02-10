package br.com.fiap.hackathon.application.usecases;

import br.com.fiap.hackathon.domain.exceptions.DownloadVideoException;
import br.com.fiap.hackathon.domain.interfaces.DownloadVideoInterface;
import br.com.fiap.hackathon.domain.interfaces.S3RepositoryInterface;
import br.com.fiap.hackathon.infraestructure.repository.s3.S3Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DownloadVideoInterfaceUseCaseTest {

    private S3RepositoryInterface s3Repository;
    private DownloadVideoInterface downloadVideoUseCase;

    private String videoKey;
    private Path expectedVideoPath;

    @BeforeEach
    void setUp() {
        videoKey = "video.mp4";
        String diretorioDestino = "diretorio/temp";
        expectedVideoPath = Paths.get(diretorioDestino, videoKey);

        s3Repository = mock(S3Repository.class);
        downloadVideoUseCase = new DownloadVideoInterfaceUseCase(s3Repository);

        ReflectionTestUtils.setField(downloadVideoUseCase, "diretorioDestino", "diretorio/temp");
        ReflectionTestUtils.setField(downloadVideoUseCase, "bucketProcessamentoName", "nome-bucket-teste");
    }

    @Test
    void testDownload_Success() {
        // Arrange
        doNothing().when(s3Repository).download(videoKey, expectedVideoPath);

        // Act
        Path result = downloadVideoUseCase.download(videoKey);

        // Assert
        assertNotNull(result);
        assertEquals(expectedVideoPath, result);
        verify(s3Repository, times(1)).download(videoKey, expectedVideoPath);
    }

    @Test
    void testDownload_Failure() {
        // Arrange
        doThrow(new RuntimeException("Erro de download"))
                .when(s3Repository).download(videoKey, expectedVideoPath);

        // Act
        Exception exception = assertThrows(DownloadVideoException.class, () -> downloadVideoUseCase.download(videoKey));

        // Assert
        assertTrue(exception.getMessage().contains("Erro ao realizar downaload do video"));
        verify(s3Repository, times(1)).download(videoKey, expectedVideoPath);
    }
}