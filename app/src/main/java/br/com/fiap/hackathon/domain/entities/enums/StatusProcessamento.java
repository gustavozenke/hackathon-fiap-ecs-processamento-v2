package br.com.fiap.hackathon.domain.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum StatusProcessamento {

    PROCESSAMENTO_NAO_INICIADO("Processamento não iniciado"),
    PROCESSAMENTO_INICIADO("Processamento iniciado"),
    PROCESSAMENTO_EM_ANDAMENTO("Processamento em andamento"),
    PROCESSAMENTO_FINALIZADO_SUCESSO("Processamento finalizado com sucesso"),
    PROCESSAMENTO_FINALIZADO_ERRO("Processamento finalizado com erro");

    private final String descricao;

    public static StatusProcessamento fromString(String valor) {
        for (StatusProcessamento status : StatusProcessamento.values()) {
            if (status.name().equals(valor)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status de processamento inválido: '" + valor + "'");
    }
}
