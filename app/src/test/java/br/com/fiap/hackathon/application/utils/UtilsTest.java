package br.com.fiap.hackathon.application.utils;

import org.junit.jupiter.api.Test;

import static br.com.fiap.hackathon.utils.Utils.toNomeSemExtensao;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UtilsTest {

    @Test
    void deveRetornarNomeSemExtensao() {
        assertEquals("arquivo", toNomeSemExtensao("arquivo.txt"));
        assertEquals("video", toNomeSemExtensao("video.mp4"));
        assertEquals("semextensao", toNomeSemExtensao("semextensao"));
        assertNull(toNomeSemExtensao(null));
        assertEquals("", toNomeSemExtensao(""));
    }
}