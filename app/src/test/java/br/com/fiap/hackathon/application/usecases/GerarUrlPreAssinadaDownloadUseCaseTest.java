package br.com.fiap.hackathon.application.usecases;

import br.com.fiap.hackathon.domain.exceptions.GerarUrlPreAssinadaDownloadException;
import br.com.fiap.hackathon.domain.interfaces.GerarUrlPreAssinadaDownloadInterface;
import br.com.fiap.hackathon.domain.interfaces.S3RepositoryInterface;
import br.com.fiap.hackathon.infraestructure.repository.s3.S3Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class GerarUrlPreAssinadaDownloadUseCaseTest {

    private S3RepositoryInterface s3Repository;
    private GerarUrlPreAssinadaDownloadInterface useCase;

    private static final String KEY = "video.mp4";
    private URL url;

    @BeforeEach
    void setUp() throws MalformedURLException {
        url = new URL("https://s3.amazonaws.com/bucket/video.mp4?presigned=true");
        s3Repository = mock(S3Repository.class);
        useCase = new GerarUrlPreAssinadaDownloadUseCase(s3Repository);

    }

    @Test
    void deveGerarUrlPreAssinadaComSucesso() {
        when(s3Repository.presignGetObject(KEY, 7)).thenReturn(url);

        String url = useCase.gerarUrlPreAssinada(KEY);

        assertNotNull(url);
        verify(s3Repository, times(1)).presignGetObject(KEY, 7);
    }

    @Test
    void deveLancarExcecaoQuandoRepositorioFalha() {
        when(s3Repository.presignGetObject(KEY, 7)).thenThrow(new RuntimeException("Erro no S3"));

        GerarUrlPreAssinadaDownloadException exception = assertThrows(
                GerarUrlPreAssinadaDownloadException.class,
                () -> useCase.gerarUrlPreAssinada(KEY)
        );

        assertTrue(exception.getMessage().contains("Erro ao realizar downaload do video"));
        verify(s3Repository, times(1)).presignGetObject(KEY, 7);
    }
}