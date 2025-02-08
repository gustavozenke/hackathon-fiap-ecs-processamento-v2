package br.com.fiap.hackathon.domain.entities;

import br.com.fiap.hackathon.domain.entities.enums.TipoComunicacao;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class NotificacaoCliente {
    @JsonProperty("nome_video")
    private String nomeVideo;
    @JsonProperty("nome_usuario")
    private String nomeUsuario;
    @JsonProperty("comunicacao_erro")
    private boolean comunicacaoErro;
    @JsonProperty("mensagem")
    private String mensagem;
    @JsonProperty("tipo_comunicacao")
    private TipoComunicacao tipoComunicacao;
}