package br.com.fiap.hackathon.utils;

public class FileNameUtils {
    public static String removeExtensao(String fileName) {
        if (fileName == null || fileName.isEmpty())
            return fileName;

        int lastDotIndex = fileName.lastIndexOf(".");

        if (lastDotIndex == -1)
            return fileName;

        return fileName.substring(0, lastDotIndex);
    }
}
