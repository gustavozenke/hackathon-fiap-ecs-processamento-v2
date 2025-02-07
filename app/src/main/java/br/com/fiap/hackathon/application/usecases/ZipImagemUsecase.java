package br.com.fiap.hackathon.application.usecases;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class ZipImagemUsecase {

    public void zipImagens(Path diretorioVideo, String nomeSemExtensao) {
        log.info("Iniciando geracao do arquivo zip. Diretorio e imagens do container: {}", diretorioVideo);

        if (!Files.exists(diretorioVideo) || !Files.isDirectory(diretorioVideo)) {
            log.error("Diretório invalido: {}", diretorioVideo);
            return;
        }

        var diretorioArquivoZip = diretorioVideo.resolve(String.format("%s.zip", nomeSemExtensao));

        try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(diretorioArquivoZip))) {
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
                                log.info("Arquivo adicionado: {}", image.getFileName());
                            } catch (IOException e) {
                                log.error("Erro ao adicionar arquivo: {}", image.getFileName());
                            }
                        });
            }
            log.info("Arquivo ZIP criado com sucesso: {}",  diretorioArquivoZip);
        } catch (IOException e) {
            log.error("Erro ao criar o ZIP: {}", e.getMessage());
        }
    }
}
