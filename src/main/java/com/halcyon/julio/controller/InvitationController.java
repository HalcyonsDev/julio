package com.halcyon.julio.controller;

import com.halcyon.julio.dto.invitation.NewInviteCodeDto;
import com.halcyon.julio.model.InviteCode;
import com.halcyon.julio.service.invitation.InvitationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Invitations")
public class InvitationController {
    private final InvitationService invitationService;

    @PostMapping
    @Operation(
            summary = "create invite code",
            description = "create invite code"
    )
    public ResponseEntity<InviteCode> create(@RequestBody @Valid NewInviteCodeDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getAllErrors().get(0).getDefaultMessage());
        }

        InviteCode createdInviteCode = invitationService.create(dto);
        return ResponseEntity.ok(createdInviteCode);
    }

    @GetMapping("/{value}")
    @Operation(
            summary = "find and return invite code by its value",
            description = "get invite code by value"
    )
    public ResponseEntity<InviteCode> getByValue(@PathVariable String value) {
        InviteCode inviteCode = invitationService.findByValue(value);
        return ResponseEntity.ok(inviteCode);
    }

    @GetMapping("/activate/{value}")
    @Operation(
            summary = "find and activate invite code by its value",
            description = "activate invite code by value"
    )
    public ResponseEntity<InviteCode> activate(@PathVariable String value) {
        InviteCode activatedInviteCode = invitationService.activate(value);
        return ResponseEntity.ok(activatedInviteCode);
    }
}