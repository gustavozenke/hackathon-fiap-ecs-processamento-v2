package br.com.fiap.hackathon.application.facade;

import br.com.fiap.hackathon.application.processor.FFmpegVideoProcessor;
import br.com.fiap.hackathon.application.usecases.*;
import br.com.fiap.hackathon.domain.entities.EventoVideo;
import br.com.fiap.hackathon.domain.interfaces.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProcessarVideoFacadeTest {

    private ProcessarVideoInterface processarVideoFacade;
    private DownloadVideoInterface downloadVideo;
    private UploadZipFramesInterface uploadZipFrames;
    private GerarUrlPreAssinadaDownloadInterface gerarUrlPreAssinadaDownload;
    private VideoProcessorInterface videoProcessor;
    private ZipImagemInterface zipImagens;
    private GravarEventoProcessamentoInterface gravarEventoProcessamento;
    private EnviarComunicacaoClienteInterface enviarComunicacaoCliente;

    private Path videoPath;
    private Path zipPath;
    private String urlPreAssinada;
    private EventoVideo video;

    @BeforeEach
    void setUp(){
        downloadVideo = mock(DownloadVideoInterfaceUseCase.class);
        uploadZipFrames = mock(UploadZipFramesUseCase.class);
        gerarUrlPreAssinadaDownload = mock(GerarUrlPreAssinadaDownloadUseCase.class);
        videoProcessor = mock(FFmpegVideoProcessor.class);
        zipImagens = mock(ZipImagemUsecase.class);
        gravarEventoProcessamento = mock(GravarEventoProcessamentoUseCase.class);
        enviarComunicacaoCliente = mock(EnviarComunicacaoClienteUseCase.class);

        videoPath = Path.of("/tmp/video.mp4");
        zipPath = Path.of("/tmp/video.zip");
        urlPreAssinada = "https://s3.amazon.com/bucket/video.zip";

        video = EventoVideo.builder()
                .nomeVideo("nome_video")
                .nomeUsuario("nome_usuario")
                .build();

        processarVideoFacade = new ProcessarVideoFacade(downloadVideo,
                uploadZipFrames,
                gerarUrlPreAssinadaDownload,
                videoProcessor,
                zipImagens,
                gravarEventoProcessamento,
                enviarComunicacaoCliente);
    }

    @Test
    void deveExecutarComSucesso() {
        // Arrange
        doNothing().when(gravarEventoProcessamento).gravarEventoProcessamento(any(), any(), any(), any());
        when(downloadVideo.download(any())).thenReturn(videoPath);
        doNothing().when(videoProcessor).extrairFrames(any(), any(), anyInt());
        when(zipImagens.zipImagens(any(), any())).thenReturn(zipPath);
        when(gerarUrlPreAssinadaDownload.gerarUrlPreAssinada(any())).thenReturn(urlPreAssinada);
        doNothing().when(enviarComunicacaoCliente).notificar(any(), any(), anyBoolean(), any());

        // Act
        processarVideoFacade.execute(video);

        // Assert
        verify(gravarEventoProcessamento, times(3)).gravarEventoProcessamento(any(), any(), any(), any());
        verify(downloadVideo, times(1)).download(any());
        verify(videoProcessor, times(1)).extrairFrames(any(), any(), anyInt());
        verify(zipImagens, times(1)).zipImagens(any(), any());
        verify(gerarUrlPreAssinadaDownload, times(1)).gerarUrlPreAssinada(any());
        verify(enviarComunicacaoCliente, times(1)).notificar(any(), any(), anyBoolean(), any());
    }

    @Test
    void deveLancarException() {
        // Arrange
        when(downloadVideo.download(video.getNomeVideo())).thenThrow(new RuntimeException("Erro ao baixar o vídeo"));
        doNothing().when(enviarComunicacaoCliente).notificar(any(), any(), anyBoolean(), any());

        // Act
        processarVideoFacade.execute(video);

        // Assert
        verify(enviarComunicacaoCliente, times(1)).notificar(any(), any(), anyBoolean(), any());
    }
}