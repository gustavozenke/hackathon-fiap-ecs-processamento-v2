package br.com.fiap.hackathon.domain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StatusProcessamento {
    @JsonProperty("nome_video")
    private String nomeVideo;
    @JsonProperty("nome_usuario")
    private String nomeUsuario;
    @JsonProperty("status_processamento")
    private String statusProcessamento;
    @JsonProperty("url_video")
    private String urlVideo;
}