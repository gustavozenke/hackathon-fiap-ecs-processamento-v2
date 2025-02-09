package br.com.fiap.hackathon.application.usecases;

import br.com.fiap.hackathon.domain.interfaces.S3RepositoryInterface;
import br.com.fiap.hackathon.domain.interfaces.UploadZipFramesInterface;
import br.com.fiap.hackathon.domain.exceptions.UploadZipFramesException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Slf4j
@Component
@RequiredArgsConstructor
public class UploadZipFramesUseCase implements UploadZipFramesInterface {

    private final S3RepositoryInterface s3Repository;
    @Value("${bucket.frames.name}")
    private String bucketZippedFramesName;
    @Value("${video.destination-path}")
    private String diretorioDestino;

    @Override
    public void upload(Path diretorioArquivoZip) {
        log.info("Realizando upload do arquivo {} no bucket {}", diretorioArquivoZip.getFileName(), bucketZippedFramesName);
        try {
            s3Repository.upload(diretorioArquivoZip.getFileName().toString(), diretorioArquivoZip);
            log.info("Upload do arquivo {} realizado com sucesso", diretorioArquivoZip.getFileName());
        } catch (Exception e){
            var message = String.format("Erro ao realizar upload do arquivo %s. Erro: %s", diretorioArquivoZip.getFileName(), e.getMessage());
            log.error(message);
            throw new UploadZipFramesException(message);
        }
    }
}
