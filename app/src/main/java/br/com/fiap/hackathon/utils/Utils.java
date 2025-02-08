package br.com.fiap.hackathon.utils;

public class Utils {

    public static String toNomeSemExtensao(String nomeArquivo) {
        if (nomeArquivo == null || nomeArquivo.isEmpty())
            return nomeArquivo;

        int lastDotIndex = nomeArquivo.lastIndexOf(".");

        if (lastDotIndex == -1)
            return nomeArquivo;

        return nomeArquivo.substring(0, lastDotIndex);
    }
}
