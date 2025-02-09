package br.com.fiap.hackathon.application.usecases;

import br.com.fiap.hackathon.domain.interfaces.DownloadVideoInterface;
import br.com.fiap.hackathon.domain.interfaces.S3RepositoryInterface;
import br.com.fiap.hackathon.domain.exceptions.DownloadVideoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
@RequiredArgsConstructor
public class DownloadVideoInterfaceUseCase implements DownloadVideoInterface {

    private final S3RepositoryInterface s3Repository;
    @Value("${bucket.processamento.name}")
    private String bucketProcessamentoName;
    @Value("${video.destination-path}")
    private String diretorioDestino;

    @Override
    public Path download(String key) {
        log.info("Buscando arquivo {} no bucket {}", key, bucketProcessamentoName);
        try {
            Path diretorioDestinoPath = Paths.get(diretorioDestino);
            Path videoPath = diretorioDestinoPath.resolve(key);
            s3Repository.download(key, videoPath);
            log.info("Download do arquivo {} realizado com sucesso", key);
            return videoPath;
        } catch (Exception e){
            var message = String.format("Erro ao realizar downaload do video %s. Erro: %s", key, e.getMessage());
            log.error(message);
            throw new DownloadVideoException(message);
        }
    }
}
