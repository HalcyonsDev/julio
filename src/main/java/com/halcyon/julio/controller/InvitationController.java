package com.halcyon.julio.controller;

import com.halcyon.julio.dto.invitation.NewInviteCodeDto;
import com.halcyon.julio.model.InviteCode;
import com.halcyon.julio.service.invitation.InvitationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/invitations")
@RequiredArgsConstructor
public class InvitationController {
    private final InvitationService invitationService;

    @PostMapping
    public ResponseEntity<InviteCode> create(@RequestBody @Valid NewInviteCodeDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        InviteCode createdInviteCode = invitationService.create(dto);
        return ResponseEntity.ok(createdInviteCode);
    }

    @GetMapping("/{value}")
    public ResponseEntity<InviteCode> findByValue(@PathVariable String value) {
        InviteCode inviteCode = invitationService.findByValue(value);
        return ResponseEntity.ok(inviteCode);
    }

    @GetMapping("/activate/{value}")
    public ResponseEntity<InviteCode> activate(@PathVariable String value) {
        InviteCode activatedInviteCode = invitationService.activate(value);
        return ResponseEntity.ok(activatedInviteCode);
    }
}