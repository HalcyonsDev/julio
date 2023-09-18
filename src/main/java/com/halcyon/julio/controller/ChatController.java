package com.halcyon.julio.controller;

import com.halcyon.julio.dto.chat.NewChatDto;
import com.halcyon.julio.model.Chat;
import com.halcyon.julio.service.chat.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Chats")
public class ChatController {
    private final ChatService chatService;

    @PostMapping
    @Operation(
            summary = "create chat",
            description = "create chat"
    )
    public ResponseEntity<Chat> create(@RequestBody @Valid NewChatDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        Chat createdChat = chatService.create(dto);
        return ResponseEntity.ok(createdChat);
    }

    @DeleteMapping("/delete/{chatId}")
    @Operation(
            summary = "find and delete chat by its id",
            description = "delete chat by id"
    )
    public ResponseEntity<String> deleteById(@PathVariable Long chatId) {
        String response = chatService.deleteById(chatId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/channel/{channelId}")
    @Operation(
            summary = "find and return all channel's chats by its id",
            description = "get all chats by channel id"
    )
    public ResponseEntity<List<Chat>> getAllByChannelId(@PathVariable Long channelId) {
        List<Chat> chats = chatService.findChatsByChannelId(channelId);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/channel/{channelId}/group/category")
    @Operation(
            summary = "find, group and return channel's chats",
            description = "get grouped channel's chats"
    )
    public ResponseEntity<Map<Long, List<Chat>>> getByChannelAndGrouped(@PathVariable Long channelId) {
        Map<Long, List<Chat>> groupedChats = chatService.findByChannelAndGrouped(channelId);
        return ResponseEntity.ok(groupedChats);
    }

    @PatchMapping("/{chatId}/category/{categoryId}")
    @Operation(
            summary = "find and update chat's category",
            description = "update chat's category"
    )
    public ResponseEntity<Chat> updateCategory(@PathVariable Long chatId, @PathVariable Long categoryId) {
        Chat updatedChat = chatService.updateCategory(chatId, categoryId);
        return ResponseEntity.ok(updatedChat);
    }

    @PatchMapping("/{chatId}/title/{title}")
    @Operation(
            summary = "find and update chat's title",
            description = "update chat's title"
    )
    public ResponseEntity<Chat> updateTitle(@PathVariable Long chatId, @PathVariable String title) {
        Chat updatedChat = chatService.updateTitle(chatId, title);
        return ResponseEntity.ok(updatedChat);
    }
}