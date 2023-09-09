package com.halcyon.julio.service.invitation;

import com.halcyon.julio.dto.invitation.NewInviteCodeDto;
import com.halcyon.julio.event.user.UserJoinedEvent;
import com.halcyon.julio.model.Channel;
import com.halcyon.julio.model.InviteCode;
import com.halcyon.julio.model.User;
import com.halcyon.julio.repository.IInviteCodeRepository;
import com.halcyon.julio.service.auth.AuthService;
import com.halcyon.julio.service.channel.ChannelService;
import com.halcyon.julio.service.user.UserService;
import com.halcyon.julio.util.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InvitationService {
    private final ApplicationEventPublisher eventPublisher;
    private final IInviteCodeRepository inviteCodeRepository;
    private final ChannelService channelService;
    private final UserService userService;
    private final AuthService authService;

    public InviteCode create(NewInviteCodeDto dto) {
        Channel channel = channelService.findById(dto.getChannelId());
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());

        if (!channel.getMembers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this channel.");
        }

        String value = RandomUtil.generateInviteCode(7);
        LocalDateTime expirationTime = LocalDateTime.now().plusSeconds(dto.getExpirationsTimeInSeconds());

        InviteCode inviteCode = InviteCode.builder()
                .value(value)
                .activationsLeft(dto.getActivationsLeft())
                .expirationsTime(expirationTime)
                .creator(user)
                .channel(channel)
                .build();

        return inviteCodeRepository.save(inviteCode);
    }

    public InviteCode findByValue(String value) {
        return inviteCodeRepository.findByValue(value)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invite code with this id not found."));
    }

    public InviteCode activate(String value) {
        InviteCode inviteCode = findByValue(value);
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Channel channel = inviteCode.getChannel();

        if (channel.getMembers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are already on this channel.");
        }

        if (inviteCode.getActivationsLeft() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Number of activations exceeded!");
        }

        if (inviteCode.getExpirationsTime().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Code is not valid!");
        }

        channelService.addMember(channel.getId(), user);
        inviteCodeRepository.updateActivationsLeftById(inviteCode.getActivationsLeft() - 1, inviteCode.getId());

        eventPublisher.publishEvent(new UserJoinedEvent(user, channel));

        return inviteCode;
    }
}