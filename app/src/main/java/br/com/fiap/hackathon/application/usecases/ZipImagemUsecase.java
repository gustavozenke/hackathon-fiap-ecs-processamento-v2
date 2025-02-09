package br.com.fiap.hackathon.application.usecases;

import br.com.fiap.hackathon.domain.interfaces.ZipImagemInterface;
import br.com.fiap.hackathon.domain.exceptions.ZipImagemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Component
public class ZipImagemUsecase implements ZipImagemInterface {

    @Override
    public Path zipImagens(Path diretorioVideo, String nomeSemExtensao) {
        log.info("Iniciando geracao do arquivo zip");
        var pathArquivoZip = diretorioVideo.resolve(String.format("%s.zip", nomeSemExtensao));

        try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(pathArquivoZip))) {
            try (Stream<Path> files = Files.list(diretorioVideo)) {
                files.filter(path -> path.toString().matches(".*\\.(jpg|jpeg|png|gif|bmp|tiff)$"))
                        .forEach(image -> {
                            try (InputStream fis = Files.newInputStream(image)) {

                                ZipEntry zipEntry = new ZipEntry(image.getFileName().toString());
                                zipOut.putNextEntry(zipEntry);

                                byte[] buffer = new byte[1024];
                                int length;

                                while ((length = fis.read(buffer)) > 0)
                                    zipOut.write(buffer, 0, length);

                                zipOut.closeEntry();
                                log.info("Arquivo adicionado ao zip: {}", image.getFileName());
                            } catch (IOException e) {
                                log.error("Erro ao adicionar arquivo ao zip: {}", image.getFileName());
                            }
                        });
            }
            log.info("Arquivo zip criado com sucesso: {}",  pathArquivoZip.getFileName());
            return pathArquivoZip;
        } catch (Exception e) {
            var message = String.format("Erro ao realizar zip dos frames do video %s. Erro: %s", nomeSemExtensao, e.getMessage());
            log.error(message);
            throw new ZipImagemException(message);        }
    }
}
