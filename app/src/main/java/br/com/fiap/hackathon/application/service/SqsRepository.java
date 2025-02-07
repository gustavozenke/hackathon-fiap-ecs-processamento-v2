package br.com.fiap.hackathon.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class SqsRepository {

    private final SqsAsyncClient sqsAsyncClient;
    private final ObjectMapper objectMapper;
    @Value("${sqs.queue-status-url}")
    private String queueUrl;

    @SneakyThrows
    public CompletableFuture<SendMessageResponse> send(String messageBody) {
        SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(messageBody)
                .build();
        return sqsAsyncClient.sendMessage(sendMessageRequest);
    }
}
