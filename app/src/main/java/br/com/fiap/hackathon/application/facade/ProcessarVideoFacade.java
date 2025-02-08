package br.com.fiap.hackathon.application.facade;

import br.com.fiap.hackathon.application.interfaces.*;
import br.com.fiap.hackathon.domain.entities.EventoVideo;
import br.com.fiap.hackathon.domain.entities.enums.StatusProcessamento;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static br.com.fiap.hackathon.utils.PathUtils.deleteDiretorioFrames;
import static br.com.fiap.hackathon.utils.PathUtils.deleteVideo;
import static br.com.fiap.hackathon.utils.Utils.toNomeSemExtensao;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessarVideoFacade implements ProcessarVideoInterface {

    private final DownloadVideoInterface downloadVideo;
    private final UploadZipFramesInterface uploadZipFrames;
    private final GerarUrlPreAssinadaDownloadInterface gerarUrlPreAssinadaDownload;
    private final VideoProcessorInterface videoProcessor;
    private final ZipImagemInterface zipImagens;
    private final GravarEventoProcessamentoInterface gravarEventoProcessamento;
    private final EnviarComunicacaoClienteInterface enviarComunicacaoCliente;

    @Override
    public void execute(EventoVideo event) {
        try {
            gravarEventoProcessamento.gravarEventoProcessamento(
                    event.getNomeVideo(),
                    event.getNomeUsuario(),
                    StatusProcessamento.PROCESSAMENTO_INICIADO.name(),
                    null);

            var pathVideo = downloadVideo.download(event.getNomeVideo());

            gravarEventoProcessamento.gravarEventoProcessamento(
                    event.getNomeVideo(),
                    event.getNomeUsuario(),
                    StatusProcessamento.PROCESSAMENTO_EM_ANDAMENTO.name(),
                    null);

            String nomeSemExtensao = toNomeSemExtensao(pathVideo.getFileName().toString());
            var diretorioFrames = pathVideo.getParent().resolve(String.format("frames-%s", nomeSemExtensao));

            videoProcessor.extrairFrames(pathVideo, diretorioFrames, 20);
            var pathArquivoZip = zipImagens.zipImagens(diretorioFrames, nomeSemExtensao);
            uploadZipFrames.upload(pathArquivoZip);

            var url = gerarUrlPreAssinadaDownload.gerarUrlPreAssinada(pathArquivoZip.getFileName().toString());

            gravarEventoProcessamento.gravarEventoProcessamento(
                    event.getNomeVideo(),
                    event.getNomeUsuario(),
                    StatusProcessamento.PROCESSAMENTO_FINALIZADO_SUCESSO.name(),
                    url);

            deleteDiretorioFrames(diretorioFrames);
            deleteVideo(pathVideo);

            var mensagem = String.format("O processamento do video %s foi finalizado com sucesso!", event.getNomeVideo());
            enviarComunicacaoCliente.notificar(event.getNomeVideo(), event.getNomeUsuario(), false, mensagem);
        } catch (Exception exception){
            log.error("Um erro ocorreu durante o processamento do video: {}", exception.toString());
            var mensagem = "Ocorreu um erro ao processar seu video, por favor tente novamente!";
            enviarComunicacaoCliente.notificar(event.getNomeVideo(), event.getNomeUsuario(), true, mensagem);
        }
    }
}
