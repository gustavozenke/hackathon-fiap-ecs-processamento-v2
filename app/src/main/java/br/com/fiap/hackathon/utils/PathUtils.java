package br.com.fiap.hackathon.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

@Slf4j
public class PathUtils {

    public static void deleteDiretorioFrames(Path diretorioFrames) throws IOException {
        if (Files.exists(diretorioFrames) && Files.isDirectory(diretorioFrames)) {
            log.info("Deletando diretorio de frames: {}", diretorioFrames);
            Files.walkFileTree(diretorioFrames, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    public static void deleteVideo(Path pathVideo) throws IOException {
        if (Files.exists(pathVideo) && Files.isRegularFile(pathVideo)) {
            log.info("Deletando video: {}", pathVideo.getFileName());
            Files.delete(pathVideo);
        }
    }
}
