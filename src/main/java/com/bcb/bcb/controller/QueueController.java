package com.bcb.bcb.controller;


import com.bcb.bcb.queue.PriorityMessageQueue;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/queue")
@AllArgsConstructor
public class QueueController extends BaseController{

    private PriorityMessageQueue priorityMessageQueue;


    @GetMapping("/print")
    public ResponseEntity<String> getQueue() {
        return ResponseEntity.ok(priorityMessageQueue.print());
    }


}
