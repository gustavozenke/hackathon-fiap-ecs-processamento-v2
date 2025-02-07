package br.com.fiap.hackathon.application.usecases;

import br.com.fiap.hackathon.application.service.GravarEventoProcessamentoService;
import br.com.fiap.hackathon.application.service.S3Service;
import br.com.fiap.hackathon.domain.entities.VideoEvent;
import br.com.fiap.hackathon.domain.entities.enums.StatusProcessamento;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static br.com.fiap.hackathon.utils.FileUtils.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessarVideoUseCase {

    private final S3Service s3Service;
    private final FFmpegVideoProcessor processor;
    private final ZipImagemUsecase zipImagemUsecase;
    private final GravarEventoProcessamentoService gravarEventoProcessamentoService;

    public void execute(VideoEvent event) throws IOException, InterruptedException {
        gravarEventoProcessamentoService.gravarEventoProcessamento(
                event.getNomeVideo(),
                event.getNomeUsuario(),
                StatusProcessamento.PROCESSAMENTO_INICIADO.name(),
                null);

        var diretorioVideo = s3Service.download(event.getNomeVideo());

        gravarEventoProcessamentoService.gravarEventoProcessamento(
                event.getNomeVideo(),
                event.getNomeUsuario(),
                StatusProcessamento.PROCESSAMENTO_EM_ANDAMENTO.name(),
        null);

        String nomeSemExtensao = removeExtensao(diretorioVideo.getFileName().toString());
        var diretorioSaida = diretorioVideo.getParent().resolve(String.format("frames-%s", nomeSemExtensao));

        processor.extrairFrames(diretorioVideo, diretorioSaida, nomeSemExtensao,20);
        zipImagemUsecase.zipImagens(diretorioSaida, nomeSemExtensao);
        s3Service.upload(diretorioSaida, nomeSemExtensao);
        var url = s3Service.generatePresignedUrl(nomeSemExtensao, 7);

        gravarEventoProcessamentoService.gravarEventoProcessamento(
                event.getNomeVideo(),
                event.getNomeUsuario(),
                StatusProcessamento.PROCESSAMENTO_FINALIZADO_SUCESSO.name(),
                url.toString());

        deleteDiretorioFrames(diretorioSaida);
        deleteVideo(diretorioVideo);
    }
}
