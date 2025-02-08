package br.com.fiap.hackathon.application.usecases;

import br.com.fiap.hackathon.application.interfaces.EnviarComunicacaoClienteInterface;
import br.com.fiap.hackathon.domain.entities.NotificacaoCliente;
import br.com.fiap.hackathon.domain.exceptions.GravarEventoProcessamentoException;
import br.com.fiap.hackathon.infraestructure.repository.sqs.SqsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnviarComunicacaoClienteUseCase implements EnviarComunicacaoClienteInterface {

    private final ObjectMapper objectMapper;
    private final SqsRepository sqsRepository;
    @Value("${sqs.queue-comunicacao-cliente.url}")
    private String queueUrl;

    @Override
    public void notificar(String nomeVideo, String nomeUsuario, boolean comunicacaoErro, String mensagem) {
        try {
            NotificacaoCliente payload = NotificacaoCliente.builder()
                    .nomeVideo(nomeVideo)
                    .nomeUsuario(nomeUsuario)
                    .comunicacaoErro(comunicacaoErro)
                    .mensagem(mensagem)
                    .build();
            var messageBody = objectMapper.writeValueAsString(payload);
            sqsRepository.send(queueUrl, messageBody);
        }catch (Exception e){
            var message = String.format("Erro ao enviar evento de comunicacao com o cliente. Erro: %s", e.getMessage());
            log.error(message);
            throw new GravarEventoProcessamentoException(message);
        }
    }
}
