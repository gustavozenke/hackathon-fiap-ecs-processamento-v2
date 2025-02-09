package br.com.fiap.hackathon.application.processor;

import br.com.fiap.hackathon.domain.interfaces.VideoProcessorInterface;
import br.com.fiap.hackathon.domain.exceptions.ExtrairFramesException;
import br.com.fiap.hackathon.domain.exceptions.FFmpegVideoProcessorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Component
public class FFmpegVideoProcessor implements VideoProcessorInterface {

    @Override
    public void extrairFrames(Path diretorioVideo, Path diretorioSaida, int intervalo) {
        try {
            log.info("Iniciando extracao de frames");

            Files.createDirectories(diretorioSaida);

            var outputPattern = diretorioSaida.resolve(diretorioVideo.getFileName() + "-frame_%04d.png").toString();
            var processBuilder = new ProcessBuilder(
                    "ffmpeg", "-i", diretorioVideo.toString(),
                    "-vf", String.format("fps=1/%d", intervalo), outputPattern,
                    "-hide_banner", "-loglevel", "error"
            );

            var process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                log.info("Extracao de frames concluida com sucesso");
            } else {
                var mensagemErro = String.format("Erro ao extrair frames. Código de saida: %s", exitCode);
                log.error(mensagemErro);
                throw new FFmpegVideoProcessorException(mensagemErro);
            }
        } catch (Exception e){
            var message = String.format("Erro ao extrair frames do video %s. Erro: %s", diretorioVideo.getFileName(), e.getMessage());
            log.error(message);
            throw new ExtrairFramesException(message);
        }
    }
}
