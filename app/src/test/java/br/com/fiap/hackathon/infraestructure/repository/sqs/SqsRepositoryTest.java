package br.com.fiap.hackathon.infraestructure.repository.sqs;

import br.com.fiap.hackathon.domain.interfaces.SqsRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

class SqsRepositoryTest {

    private SqsAsyncClient sqsAsyncClient;
    private SqsRepositoryInterface sqsRepository;

    @BeforeEach
    void setUp() {
        sqsAsyncClient = mock(SqsAsyncClient.class);
        sqsRepository = new SqsRepository(sqsAsyncClient);
    }

    @Test
    void deveEnviarMensagemComSucesso() {
        // Arrange
        SendMessageResponse mockResponse = SendMessageResponse.builder().build();
        CompletableFuture<SendMessageResponse> future = CompletableFuture.completedFuture(mockResponse);
        when(sqsAsyncClient.sendMessage(any(SendMessageRequest.class))).thenReturn(future);

        // Act
        sqsRepository.send("queueUrl", "messageBody");

        // Assert
        verify(sqsAsyncClient, times(1)).sendMessage(any(SendMessageRequest.class));
    }
}