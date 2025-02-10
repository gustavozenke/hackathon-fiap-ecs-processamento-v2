package br.com.fiap.hackathon.application.usecases;

import br.com.fiap.hackathon.domain.exceptions.GravarEventoProcessamentoException;
import br.com.fiap.hackathon.domain.interfaces.GravarEventoProcessamentoInterface;
import br.com.fiap.hackathon.domain.interfaces.SqsRepositoryInterface;
import br.com.fiap.hackathon.infraestructure.repository.sqs.SqsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GravarEventoProcessamentoUseCaseTest {

    private ObjectMapper objectMapper;
    private SqsRepositoryInterface sqsRepository;
    private GravarEventoProcessamentoInterface useCase;

    private static final String QUEUE_URL = "https://sqs.us-east-1.amazonaws.com/123456789012/queue-status";
    private static final String NOME_VIDEO = "video.mp4";
    private static final String NOME_USUARIO = "usuario123";
    private static final String PROCESSAMENTO_EM_ANDAMENTO = "PROCESSAMENTO_EM_ANDAMENTO";
    private static final String URL_VIDEO = "https://s3.amazonaws.com/bucket/video.mp4";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        sqsRepository = mock(SqsRepository.class);
        useCase = new GravarEventoProcessamentoUseCase(objectMapper, sqsRepository);
        ReflectionTestUtils.setField(useCase, "queueUrl", QUEUE_URL);
    }

    @Test
    void deveGravarEventoProcessamentoComSucesso(){
        // Arrange
        doNothing().when(sqsRepository).send(any(), any());

        // Act
        useCase.gravarEventoProcessamento(NOME_VIDEO, NOME_USUARIO, PROCESSAMENTO_EM_ANDAMENTO, URL_VIDEO);

        // Assert
        verify(sqsRepository, times(1)).send(any(), any());
    }

    @Test
    void deveLancarExcecaoQuandoFalharAoConverterObjetoParaJson() throws JsonProcessingException {
        // Arrange
        doThrow(new RuntimeException("Erro ao enviar evento")).when(sqsRepository).send(any(), any());

        // Acrt
        GravarEventoProcessamentoException exception = assertThrows(
                GravarEventoProcessamentoException.class,
                () -> useCase.gravarEventoProcessamento(NOME_VIDEO, NOME_USUARIO, PROCESSAMENTO_EM_ANDAMENTO, URL_VIDEO)
        );

        // Assert
        assertTrue(exception.getMessage().contains("Erro ao enviar evento"));
        verify(sqsRepository, times(1)).send(any(), any());
    }
}
