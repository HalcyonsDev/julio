package com.halcyon.julio.service.message;

import com.halcyon.julio.dto.message.NewMessageDto;
import com.halcyon.julio.event.message.MessageCreatedEvent;
import com.halcyon.julio.model.Chat;
import com.halcyon.julio.model.Message;
import com.halcyon.julio.model.User;
import com.halcyon.julio.repository.IMessageRepository;
import com.halcyon.julio.service.auth.AuthService;
import com.halcyon.julio.service.chat.ChatService;
import com.halcyon.julio.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final ApplicationEventPublisher eventPublisher;
    private final IMessageRepository messageRepository;
    private final ChatService chatService;
    private final UserService userService;
    private final AuthService authService;

    public Message create(Message message) {
        return messageRepository.save(message);
    }

    public Message create(NewMessageDto dto) {
        Chat chat = chatService.findById(dto.getChatId());
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());

        if (!chat.getChannel().getOwner().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this channel");
        }

        Message message = new Message(dto.getValue());
        message.setChat(chat);
        message.setSender(user);

        messageRepository.save(message);
        eventPublisher.publishEvent(new MessageCreatedEvent(message));

        return message;
    }

    public String deleteById(Long messageId) {
        Message message = findById(messageId);
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());

        if (!message.getSender().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this message.");
        }

        messageRepository.delete(message);

        return "Message deleted.";
    }

    public Message findById(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message with this id not found."));
    }

    public Page<Message> findMessagesByChatId(Long chatId, Integer offset, Integer limit) {
        Chat chat = chatService.findById(chatId);
        User user = userService.findByEmail(authService.getAuthInfo().getEmail());

        if (!chat.getChannel().getMembers().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this channel.");
        }

        return messageRepository.findMessagesByChat(chat,
                PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, "id")));
    }
}