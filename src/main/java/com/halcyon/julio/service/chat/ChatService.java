package com.halcyon.julio.service.chat;

import com.halcyon.julio.dto.chat.NewChatDto;
import com.halcyon.julio.model.Category;
import com.halcyon.julio.model.Channel;
import com.halcyon.julio.model.Chat;
import com.halcyon.julio.model.User;
import com.halcyon.julio.repository.IChatRepository;
import com.halcyon.julio.service.auth.AuthService;
import com.halcyon.julio.service.category.CategoryService;
import com.halcyon.julio.service.channel.ChannelService;
import com.halcyon.julio.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final IChatRepository chatRepository;
    private final CategoryService categoryService;
    private final ChannelService channelService;
    private final UserService userService;
    private final AuthService authService;

    public Chat create(NewChatDto dto) {
        Channel channel = channelService.findById(dto.getChannelId());
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());

        if (!channel.getOwner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this channel.");
        }

        Chat chat = new Chat(dto.getTitle());
        chat.setChannel(channel);

        return chatRepository.save(chat);
    }

    public String deleteById(Long chatId) {
        Chat chat = findById(chatId);
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());

        if (!chat.getChannel().getOwner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this channel.");
        }

        chatRepository.delete(chat);

        return "Chat deleted successfully.";
    }

    public Chat findById(Long chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat with this is not found."));
    }

    public List<Chat> findChatsByChannelId(Long channelId) {
        Channel channel = channelService.findById(channelId);
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());

        if (!channel.getOwner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this channel.");
        }

        return chatRepository.findChatsByChannel(channel);
    }

    public Map<Long, List<Chat>> findByChannelAndGrouped(Long channelId) {
        List<Chat> chats = findChatsByChannelId(channelId);

        return chats.stream()
                .collect(Collectors.groupingBy(
                        chat -> chat.getCategory() != null ? chat.getCategory().getId() : -1L,
                        Collectors.toList()
                ));
    }

    public Chat updateCategory(Long chatId, Long categoryId) {
        Chat chat = findById(chatId);
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());

        if (!chat.getChannel().getOwner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this channel.");
        }

        Category category = categoryService.findById(categoryId);

        chatRepository.updateCategoryById(category, chatId);
        chat.setCategory(category);

        return chat;
    }

    public Chat updateTitle(Long chatId, String title) {
        Chat chat = findById(chatId);
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());

        if (!chat.getChannel().getOwner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this channel.");
        }

        chatRepository.updateTitleById(title, chatId);
        chat.setTitle(title);

        return chat;
    }

    public Chat getSpecialChat(Long channelId, String title) {
        Channel channel = channelService.findById(channelId);

        return chatRepository.findByTitleAndChannel(title, channel)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chat with this data not found."));
    }
}