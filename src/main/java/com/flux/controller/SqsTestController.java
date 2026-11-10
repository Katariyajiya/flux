package com.flux.controller;

import com.flux.service.SqsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SqsTestController {
    private final SqsService sqsService;

    public SqsTestController(SqsService sqsService) {
        this.sqsService = sqsService;
    }

    @GetMapping("/api/test/sqs")
    public String testSqs(){
        String messageId = sqsService.sendMessage("Flux SQS test message");
        return "SQS message sent successfully. Message ID: " + messageId;
    }
}
