package com.halcyon.julio.event.message;

import com.halcyon.julio.model.Chat;
import com.halcyon.julio.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageCreatedEventHandler {
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void handleMessageCreatedEvent(MessageCreatedEvent event) {
        Message message = event.getMessage();
        Chat chat = message.getChat();

        messagingTemplate.convertAndSend("/topic/chat-" + chat.getId(), message);
    }
}