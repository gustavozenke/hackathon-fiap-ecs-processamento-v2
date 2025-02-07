package br.com.fiap.hackathon.application.usecases;

import br.com.fiap.hackathon.domain.exceptions.ExtrairFramesException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class FFmpegVideoProcessor {

    public void extrairFrames(Path diretorioVideo, Path diretorioSaida, String nomeVideo, int intervalSeconds) throws IOException, InterruptedException {
        log.info("Iniciando extracao de frames. Diretorio do arquivo de entrada no container: {}", diretorioVideo);

        Files.createDirectories(diretorioSaida);
        log.info("Diretorio do arquivo de saida container: {}", diretorioSaida);

        var outputPattern = diretorioSaida.resolve(nomeVideo+"-frame_%04d.png").toString();
        var processBuilder = new ProcessBuilder(
                "ffmpeg", "-i", diretorioVideo.toString(),
                "-vf", "fps=1/20", outputPattern,
                "-hide_banner", "-loglevel", "error"
        );

        var process = processBuilder.start();
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            log.info("Extracao concluida com sucesso. Imagens salvas em: {}", diretorioSaida);
        } else {
            var mensagemErro = String.format("Erro ao extrair frames. Código de saida: %s", exitCode);
            log.error(mensagemErro);
            throw new ExtrairFramesException(mensagemErro);
        }
    }
}
