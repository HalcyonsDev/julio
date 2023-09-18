package com.halcyon.julio.controller;

import com.halcyon.julio.dto.channel.NewChannelDto;
import com.halcyon.julio.model.Channel;
import com.halcyon.julio.model.User;
import com.halcyon.julio.service.channel.ChannelService;
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

@RestController
@RequestMapping("/api/v1/channels")
@RequiredArgsConstructor
@Tag(name = "Channels")
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping
    @Operation(
            summary = "create channel",
            description = "create channel"
    )
    public ResponseEntity<Channel> create(@RequestBody @Valid NewChannelDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        Channel createdChannel = channelService.create(dto);
        return ResponseEntity.ok(createdChannel);
    }

    @DeleteMapping("/delete/{channelId}")
    @Operation(
            summary = "find and delete channel by its id",
            description = "delete channel"
    )
    public ResponseEntity<String> delete(@PathVariable Long channelId) {
        String response = channelService.deleteById(channelId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/leave/{channelId}")
    @Operation(
            summary = "find and leave from channel",
            description = "leave from channel"
    )
    public ResponseEntity<String> leave(@PathVariable Long channelId) {
        String response = channelService.leaveById(channelId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/members/{channelId}")
    @Operation(
            summary = "find and return channel's members by its id",
            description = "get channel's members by id"
    )
    public ResponseEntity<List<User>> getMembersById(@PathVariable Long channelId) {
        List<User> members = channelService.findMembersById(channelId);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/my")
    @Operation(
            summary = "find and return user's channels",
            description = "get user's channels"
    )
    public ResponseEntity<List<Channel>> getUserChannels() {
        List<Channel> channels = channelService.findByMember();
        return ResponseEntity.ok(channels);
    }
}