package br.com.fiap.hackathon.application.usecases;

import br.com.fiap.hackathon.domain.exceptions.GravarEventoProcessamentoException;
import br.com.fiap.hackathon.domain.interfaces.EnviarComunicacaoClienteInterface;
import br.com.fiap.hackathon.domain.interfaces.SqsRepositoryInterface;
import br.com.fiap.hackathon.infraestructure.repository.sqs.SqsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EnviarComunicacaoClienteUseCaseTest {

    private ObjectMapper objectMapper;
    private SqsRepositoryInterface sqsRepository;
    private EnviarComunicacaoClienteInterface useCase;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        sqsRepository = mock(SqsRepository.class);
        useCase = new EnviarComunicacaoClienteUseCase(objectMapper, sqsRepository);

        ReflectionTestUtils.setField(useCase, "queueUrl", "url-teste");
        ReflectionTestUtils.setField(useCase, "tipoComunicacao", "SMS");
    }

    @Test
    void deveNotificarComSucesso() {
        // Arrange
        String nomeVideo = "video.mp4";
        String nomeUsuario = "usuario123";
        boolean comunicacaoErro = false;
        String mensagem = "Processamento concluído";
        doNothing().when(sqsRepository).send(any(), any());

        // Act
        useCase.notificar(nomeVideo, nomeUsuario, comunicacaoErro, mensagem);

        // Assert
        verify(sqsRepository, times(1)).send(any(), any());
    }

    @Test
    void deveLancarExcecaoQuandoFalharAoSerializar() {
        doThrow(new RuntimeException("Erro ao enviar para SQS")).when(sqsRepository).send(any(), any());

        assertThrows(GravarEventoProcessamentoException.class, () ->
                useCase.notificar(any(), any(), anyBoolean(), any()));
    }
}
