package br.com.fiap.hackathon.application.listener;

import br.com.fiap.hackathon.application.facade.ProcessarVideoFacade;
import br.com.fiap.hackathon.domain.entities.EventoVideo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SqsListenerAdatperTest {

    private SqsListenerAdatper sqsListenerAdatper;

    private ProcessarVideoFacade processarVideoFacade;

    @BeforeEach
    void setUp(){
        var objectMapper = new ObjectMapper();
        processarVideoFacade = mock(ProcessarVideoFacade.class);
        sqsListenerAdatper = new SqsListenerAdatper(processarVideoFacade, objectMapper);
    }

    @Test
    void deveProcessarMensagemComSucesso() {
        // Arrange
        String mensagem = "{'nome_video': 'teste', 'nome_usuario': 'usuario teste'}";

        doNothing().when(processarVideoFacade).execute(any(EventoVideo.class));

        // Act
        sqsListenerAdatper.onMessage(mensagem);

        // Assert
        verify(processarVideoFacade, times(0)).execute(any(EventoVideo.class));
    }

    @Test
    void deveRegistrarErroQuandoMensagemInvalida() {
        // Arrange
        String mensagemInvalida = "{invalid_json}";

        // Act
        sqsListenerAdatper.onMessage(mensagemInvalida);

        // Assert
        verify(processarVideoFacade, never()).execute(any());
    }
}
