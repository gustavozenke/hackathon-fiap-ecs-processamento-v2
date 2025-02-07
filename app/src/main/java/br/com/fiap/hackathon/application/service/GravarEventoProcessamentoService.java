package br.com.fiap.hackathon.application.service;

import br.com.fiap.hackathon.domain.entities.SqsStatusProcessamento;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GravarEventoProcessamentoService {

    private final ObjectMapper objectMapper;
    private final SqsRepository sqsRepository;

    @SneakyThrows
    public void gravarEventoProcessamento(String nomeVideo, String nomeUsuario, String statusProcessamento, String urlVideo) {

        SqsStatusProcessamento payload = SqsStatusProcessamento.builder()
                .nomeVideo(nomeVideo)
                .nomeUsuario(nomeUsuario)
                .statusProcessamento(statusProcessamento)
                .urlVideo(urlVideo)
                .build();

        var messageBody = objectMapper.writeValueAsString(payload);
        sqsRepository.send(messageBody);
    }
}
