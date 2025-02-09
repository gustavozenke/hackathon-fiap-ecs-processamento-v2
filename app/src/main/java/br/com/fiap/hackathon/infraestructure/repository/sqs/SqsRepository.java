package br.com.fiap.hackathon.infraestructure.repository.sqs;

import br.com.fiap.hackathon.domain.interfaces.SqsRepositoryInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class SqsRepository implements SqsRepositoryInterface {

    private final SqsAsyncClient sqsAsyncClient;

    @Override
    public void send(String queueUrl, String messageBody) {
        try {
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(messageBody)
                    .build();

            sqsAsyncClient.sendMessage(sendMessageRequest)
                    .whenCompleteAsync((response, exception) -> {
                        if (exception != null)
                            log.error("Erro ao enviar mensagem para a fila: {}. Mensagem: {}. Erro: {}",
                                    queueUrl, messageBody, exception.getMessage(), exception);
                    });
        } catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }
}
