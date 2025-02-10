package br.com.fiap.hackathon.application.listener;

import br.com.fiap.hackathon.application.facade.ProcessarVideoFacade;
import br.com.fiap.hackathon.domain.entities.EventoVideo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SqsListenerAdatperTest {

    private SqsListenerAdatper sqsListenerAdatper;

    private ProcessarVideoFacade processarVideoFacade;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp(){
        objectMapper = mock(ObjectMapper.class);
        processarVideoFacade = mock(ProcessarVideoFacade.class);
        sqsListenerAdatper = new SqsListenerAdatper(processarVideoFacade, objectMapper);
    }

    @Test
    void deveProcessarMensagemComSucesso() throws JsonProcessingException {
        // Arrange;
        var message = "{ \"nome_video\" : \"video_exemplo.mp4\", \"nome_usuario\" : usuario_teste }";

        EventoVideo evento = EventoVideo.builder()
                .nomeVideo("video_exemplo.mp4")
                .nomeUsuario("usuario_teste")
                .build();

        when(objectMapper.readValue(message, EventoVideo.class)).thenReturn(evento);
        doNothing().when(processarVideoFacade).execute(any(EventoVideo.class));

        // Act
        sqsListenerAdatper.onMessage(message);

        // Assert
        verify(processarVideoFacade, times(1)).execute(any(EventoVideo.class));
    }

    @Test
    void deveRegistrarErroQuandoMensagemInvalida() throws JsonProcessingException {
        // Arrange
        var mensagemInvalida = "{invalid_json}";

        doThrow(new RuntimeException("Erro")).when(objectMapper).readValue(mensagemInvalida, EventoVideo.class);

        // Act
        sqsListenerAdatper.onMessage(mensagemInvalida);

        // Assert
        verify(processarVideoFacade, never()).execute(any());
    }
}
