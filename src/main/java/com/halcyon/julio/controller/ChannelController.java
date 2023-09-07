package com.halcyon.julio.controller;

import com.halcyon.julio.dto.channel.NewChannelDto;
import com.halcyon.julio.model.Channel;
import com.halcyon.julio.model.User;
import com.halcyon.julio.service.channel.ChannelService;
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
public class ChannelController {
    private final ChannelService channelService;

    @PostMapping
    public ResponseEntity<Channel> create(@RequestBody @Valid NewChannelDto dto, BindingResult bindingResult) {
        System.out.println("/???");

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        System.out.println("!!!!");

        Channel createdChannel = channelService.create(dto);
        System.out.println(createdChannel);
        return ResponseEntity.ok(createdChannel);
    }

    @DeleteMapping("/delete/{channelId}")
    public ResponseEntity<String> delete(@PathVariable Long channelId) {
        String response = channelService.deleteById(channelId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/leave/{channelId}")
    public ResponseEntity<String> leave(@PathVariable Long channelId) {
        String response = channelService.leaveById(channelId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/members/{channelId}")
    public ResponseEntity<List<User>> findMembersById(@PathVariable Long channelId) {
        List<User> members = channelService.findMembersById(channelId);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/my")
    public ResponseEntity<List<Channel>> findUserChannels() {
        List<Channel> channels = channelService.findByMember();
        return ResponseEntity.ok(channels);
    }
}