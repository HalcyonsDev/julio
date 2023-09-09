package com.halcyon.julio.event.user;

import com.halcyon.julio.model.Channel;
import com.halcyon.julio.model.Chat;
import com.halcyon.julio.model.Message;
import com.halcyon.julio.model.User;
import com.halcyon.julio.service.chat.ChatService;
import com.halcyon.julio.service.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserJoinedEventHandler {
    private final MessageService messageService;

    @EventListener
    public void handleUserJoinedEvent(UserJoinedEvent event) {
        Channel channel = event.getChannel();
        User user = event.getUser();

        if (channel.getChats() != null) {
            Chat chat = channel.getChats().get(0);
            Message helloMessage = new Message(String.format(
                    "%s %s join the channel!",
                    user.getFirstname(),
                    user.getLastname()
            ));

            helloMessage.setChat(chat);
            helloMessage.setSender(user);

            messageService.create(helloMessage);
        }
    }
}