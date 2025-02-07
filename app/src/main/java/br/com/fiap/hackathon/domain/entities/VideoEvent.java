package br.com.fiap.hackathon.domain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VideoEvent {
    @JsonProperty("nome_video")
    private String nomeVideo;
    @JsonProperty("nome_usuario")
    private String nomeUsuario;
}
