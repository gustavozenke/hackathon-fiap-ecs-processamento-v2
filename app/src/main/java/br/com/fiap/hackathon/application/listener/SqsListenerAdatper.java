package br.com.fiap.hackathon.application.listener;

import br.com.fiap.hackathon.domain.interfaces.ProcessarVideoInterface;
import br.com.fiap.hackathon.domain.interfaces.SqsListenerInterface;
import br.com.fiap.hackathon.domain.entities.EventoVideo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SqsListenerAdatper implements SqsListenerInterface {

    private final ProcessarVideoInterface processarVideoFacade;
    private final ObjectMapper objectMapper;

    @Override
    @SqsListener(value = "${sqs.queue-processamento.name}")
    public void onMessage(String message) {
        try {
            log.info("Iniciando processamento");
            EventoVideo event = objectMapper.readValue(message, EventoVideo.class);
            log.info("Evento recebido: {}", event);
            processarVideoFacade.execute(event);
            log.info("Processamento concluido com sucesso");
        } catch (Exception e) {
            log.error("Erro ao processar mensagem SQS", e);
        }
    }
}
