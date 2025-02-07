package br.com.fiap.hackathon.application.listener;

import br.com.fiap.hackathon.application.usecases.ProcessarVideoUseCase;
import br.com.fiap.hackathon.domain.entities.VideoEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsListenerAdatper {

    private final ProcessarVideoUseCase processarVideoUseCase;

    @SqsListener(value = "${sqs.queue-processamento-name}")
    public void onMessage(String message) {
        try {
            log.info("Iniciando processamento");
            VideoEvent event = new ObjectMapper().readValue(message, VideoEvent.class);
            log.info("Evento recebido: {}", event);
            processarVideoUseCase.execute(event);
            log.info("Processamento concluido com sucesso");
        } catch (Exception e) {
            log.error("Erro ao processar mensagem SQS", e);
        }
    }
}
