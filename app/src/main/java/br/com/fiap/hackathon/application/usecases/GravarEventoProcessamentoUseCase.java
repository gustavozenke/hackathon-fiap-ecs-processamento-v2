package br.com.fiap.hackathon.application.usecases;

import br.com.fiap.hackathon.domain.interfaces.GravarEventoProcessamentoInterface;
import br.com.fiap.hackathon.domain.interfaces.SqsRepositoryInterface;
import br.com.fiap.hackathon.domain.entities.StatusProcessamento;
import br.com.fiap.hackathon.domain.exceptions.GravarEventoProcessamentoException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class GravarEventoProcessamentoUseCase implements GravarEventoProcessamentoInterface {

    private final ObjectMapper objectMapper;
    private final SqsRepositoryInterface sqsRepository;
    @Value("${sqs.queue-status-processamento.url}")
    private String queueUrl;

    @Override
    public void gravarEventoProcessamento(String nomeVideo, String nomeUsuario, String statusProcessamento, String urlVideo) {
        try {
            StatusProcessamento payload = StatusProcessamento.builder()
                    .nomeVideo(nomeVideo)
                    .nomeUsuario(nomeUsuario)
                    .statusProcessamento(statusProcessamento)
                    .urlVideo(urlVideo)
                    .build();
            var messageBody = objectMapper.writeValueAsString(payload);
            sqsRepository.send(queueUrl, messageBody);
        }catch (Exception e){
            var message = String.format("Erro ao enviar evento %s do video %s para gravacao de status. Erro: %s", statusProcessamento, nomeVideo, e.getMessage());
            log.error(message);
            throw new GravarEventoProcessamentoException(message);
        }
    }
}
