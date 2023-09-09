package com.halcyon.julio.controller;

import com.halcyon.julio.dto.message.NewMessageDto;
import com.halcyon.julio.model.Message;
import com.halcyon.julio.service.message.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> create(@RequestBody @Valid NewMessageDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        Message createdMessage = messageService.create(dto);
        return ResponseEntity.ok(createdMessage);
    }

    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<String> deleteById(@PathVariable Long messageId) {
        String response = messageService.deleteById(messageId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat/{chatId}")
    public Page<Message> findMessagesByChatId(
            @PathVariable Long chatId,
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit
    ) {
        return messageService.findMessagesByChatId(chatId, offset, limit);
    }
}