package br.com.fiap.hackathon.application.usecases;

import br.com.fiap.hackathon.domain.exceptions.UploadZipFramesException;
import br.com.fiap.hackathon.domain.interfaces.S3RepositoryInterface;
import br.com.fiap.hackathon.domain.interfaces.UploadZipFramesInterface;
import br.com.fiap.hackathon.infraestructure.repository.s3.S3Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UploadZipFramesUseCaseTest {

    private S3RepositoryInterface s3Repository;
    private UploadZipFramesInterface uploadZip;

    @BeforeEach
    public void setUp() {
        s3Repository = mock(S3Repository.class);
        uploadZip = new UploadZipFramesUseCase(s3Repository);
    }

    @Test
    public void deveRealizerUploadComSucesso() {
        // Arrange
        Path diretorioArquivoZip = Paths.get("caminho/do/arquivo.zip");
        doNothing().when(s3Repository).upload(diretorioArquivoZip.getFileName().toString(), diretorioArquivoZip);

        // Act
        uploadZip.upload(diretorioArquivoZip);

        // Assert
        verify(s3Repository).upload(diretorioArquivoZip.getFileName().toString(), diretorioArquivoZip);
    }

    @Test
    public void deveFalharAoTentarRealizerUpload() {
        // Arrange
        Path diretorioArquivoZip = Paths.get("caminho/do/arquivo.zip");

        Exception exception = new RuntimeException("Erro no upload");
        doThrow(exception).when(s3Repository).upload(diretorioArquivoZip.getFileName().toString(), diretorioArquivoZip);

        try { // Act
            uploadZip.upload(diretorioArquivoZip);
        } catch (UploadZipFramesException e) {
            // Assert
            assertEquals("Erro ao realizar upload do arquivo arquivo.zip. Erro: Erro no upload", e.getMessage());
        }
    }
}
