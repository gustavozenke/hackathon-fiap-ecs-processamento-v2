package br.com.fiap.hackathon.application.usecases;

import br.com.fiap.hackathon.application.interfaces.GerarUrlPreAssinadaDownloadInterface;
import br.com.fiap.hackathon.application.interfaces.S3RepositoryInterface;
import br.com.fiap.hackathon.domain.exceptions.GerarUrlPreAssinadaDownloadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GerarUrlPreAssinadaDownloadUseCase implements GerarUrlPreAssinadaDownloadInterface {

    private final S3RepositoryInterface s3Repository;

    @Override
    public String gerarUrlPreAssinada(String key) {
        log.info("Gerando URL pre assinada para download do arquivo {}", key);
        try {
            var ulr = s3Repository.presignGetObject(key, 7).toString();
            log.info("URL pre assinada gerada com sucesso. URL: {} ", ulr);
            return ulr;
        } catch (Exception e){
            var message = String.format("Erro ao realizar downaload do video %s. Erro: %s", key, e.getMessage());
            log.error(message);
            throw new GerarUrlPreAssinadaDownloadException(message);
        }
    }
}
