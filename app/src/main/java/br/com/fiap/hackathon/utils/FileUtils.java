package br.com.fiap.hackathon.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

@Slf4j
public class FileUtils {
    public static String removeExtensao(String fileName) {
        if (fileName == null || fileName.isEmpty())
            return fileName;

        int lastDotIndex = fileName.lastIndexOf(".");

        if (lastDotIndex == -1)
            return fileName;

        return fileName.substring(0, lastDotIndex);
    }

    public static void deleteDiretorioFrames(Path diretorioFrames) throws IOException {
        if (Files.exists(diretorioFrames) && Files.isDirectory(diretorioFrames)) {
            log.info("Deletando diretorio frames: {}", diretorioFrames);
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

    public static void deleteVideo(Path diretorioVideo) throws IOException {
        if (Files.exists(diretorioVideo) && Files.isRegularFile(diretorioVideo)) {
            log.info("Deletando video: {}", diretorioVideo);
            Files.delete(diretorioVideo);
        }
    }
}
