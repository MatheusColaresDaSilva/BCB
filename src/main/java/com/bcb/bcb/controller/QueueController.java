package com.bcb.bcb.controller;


import com.bcb.bcb.queue.PriorityMessageQueue;
import com.bcb.bcb.service.QueueService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/queue")
@AllArgsConstructor
public class QueueController extends BaseController{

    private PriorityMessageQueue priorityMessageQueue;

    private QueueService queueService;

    @GetMapping("/print")
    public ResponseEntity<String> getQueue() {
        return ResponseEntity.ok(priorityMessageQueue.print());
    }

    @PostMapping("/process")
    public ResponseEntity<String> processQueue() {
        queueService.processQueue();
        return ResponseEntity.ok("Queue processing started");
    }
}
