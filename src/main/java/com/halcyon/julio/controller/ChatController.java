package com.halcyon.julio.controller;

import com.halcyon.julio.dto.chat.NewChatDto;
import com.halcyon.julio.model.Chat;
import com.halcyon.julio.service.chat.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<Chat> create(@RequestBody @Valid NewChatDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        Chat createdChat = chatService.create(dto);
        return ResponseEntity.ok(createdChat);
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<String> deleteById(@PathVariable Long chatId) {
        String response = chatService.deleteById(chatId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<Chat>> findAllByChannelId(@PathVariable Long channelId) {
        List<Chat> chats = chatService.findChatsByChannelId(channelId);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/channel/{channelId}/group/category")
    public ResponseEntity<Map<Long, List<Chat>>> findByChannelAndGrouped(@PathVariable Long channelId) {
        Map<Long, List<Chat>> groupedChats = chatService.findByChannelAndGrouped(channelId);
        return ResponseEntity.ok(groupedChats);
    }

    @PatchMapping("/{chatId}/category/{categoryId}")
    public ResponseEntity<Chat> updateCategory(@PathVariable Long chatId, @PathVariable Long categoryId) {
        Chat updatedChat = chatService.updateCategory(chatId, categoryId);
        return ResponseEntity.ok(updatedChat);
    }

    @PatchMapping("/{chatId}/title/{title}")
    public ResponseEntity<Chat> updateTitle(@PathVariable Long chatId, @PathVariable String title) {
        Chat updatedChat = chatService.updateTitle(chatId, title);
        return ResponseEntity.ok(updatedChat);
    }
}