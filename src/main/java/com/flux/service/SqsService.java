package com.flux.service;

import com.google.api.client.util.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.QueueAttributeName;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Service
public class SqsService{

    private final SqsClient sqsClient;

    @Value("${aws.sqs.queue-url}")
    public String queueUrl;

    public SqsService(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }
    public String sendMessage(String message){

        GetQueueUrlRequest queueRequest = GetQueueUrlRequest.builder()
                .queueName("flux-report-queue")
                .build();

        String actualQueueUrl = sqsClient.getQueueUrl(queueRequest).queueUrl();

        SendMessageRequest request = SendMessageRequest.builder()
                .queueUrl(actualQueueUrl)
                .messageBody(message)
                .build();
        var response = sqsClient.sendMessage(request);
       return response.messageId();
    }
    public void testQueueAccess() {

        GetQueueUrlRequest request = GetQueueUrlRequest.builder()
                .queueName("flux-report-queue")
                .build();

        String actualQueueUrl = sqsClient.getQueueUrl(request).queueUrl();

        System.out.println("AWS FOUND QUEUE URL = " + actualQueueUrl);
    }
}
