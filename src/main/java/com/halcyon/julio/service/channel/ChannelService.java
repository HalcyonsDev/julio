package com.halcyon.julio.service.channel;

import com.halcyon.julio.dto.channel.NewChannelDto;
import com.halcyon.julio.model.Channel;
import com.halcyon.julio.model.User;
import com.halcyon.julio.repository.IChannelRepository;
import com.halcyon.julio.service.auth.AuthService;
import com.halcyon.julio.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final IChannelRepository channelRepository;
    private final UserService userService;
    private final AuthService authService;

    public Channel create(NewChannelDto dto) {
        Channel channel = new Channel(dto.getTitle());
        User owner = userService.findByEmail(authService.getAuthInfo().getEmail());

        channel.setOwner(owner);
        channel.setMembers(List.of(owner));

        // TODO --realize default chats for new created channel

        return channelRepository.save(channel);
    }

    public String deleteById(Long channelId) {
        Channel channel = findById(channelId);
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());

        if (!channel.getOwner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this channel.");
        }

        channelRepository.deleteById(channelId);
        return "Channel deleted successfully.";
    }

    public String leaveById(Long channelId) {
        Channel channel = findById(channelId);
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());

        if (channel.getOwner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't leave from your owned channel.");
        }

        channel.getMembers().remove(user);
        channelRepository.save(channel);

        return "You have successfully left the channel.";
    }

    public Channel findById(Long channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Channel with this id not found."));
    }

    public List<User> findMembersById(Long channelId) {
        Channel channel = findById(channelId);
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());

        if (!channel.getMembers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this channel.");
        }

        return channel.getMembers();
    }

    public List<Channel> findByMember() {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        return channelRepository.findChannelsByMembersContaining(user);
    }

    public List<Channel> findByOwner() {
        User owner = userService.findByEmail(authService.getAuthInfo().getEmail());
        return channelRepository.findChannelsByOwner(owner);
    }

    public void addMember(Long channelId, User user) {
        Channel channel = findById(channelId);
        channel.getMembers().add(user);
        channelRepository.save(channel);
    }

    public String deleteMemberByEmail(Long channelId, String email) {
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());
        Channel channel = findById(channelId);

        if (!channel.getOwner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this channel.");
        }

        User userForDelete = userService.findByEmail(email);
        channel.getMembers().remove(user);
        channelRepository.save(channel);

        return "User deleted from channel successfully.";
    }
}