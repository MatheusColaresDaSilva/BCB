package com.bcb.bcb.controller;


import com.bcb.bcb.queue.MessageQueueFactory;
import com.bcb.bcb.queue.MessageQueueStrategy;
import com.bcb.bcb.service.QueueService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/queue")
@AllArgsConstructor
public class QueueController extends BaseController{

    private QueueService queueService;

    private MessageQueueFactory messageQueueFactory;

    @GetMapping("/print")
    public ResponseEntity<String> getQueue(@RequestParam String type) {

        MessageQueueStrategy queue = messageQueueFactory.getQueue(type.toLowerCase());
        return ResponseEntity.ok(queue.print());
    }

    @PostMapping("/process")
    public ResponseEntity<String> processQueue() {
        queueService.processQueue();
        return ResponseEntity.ok("Queue processing started");
    }
}
