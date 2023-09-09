package com.halcyon.julio.event.user;

import com.halcyon.julio.model.Channel;
import com.halcyon.julio.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinedEvent {
    private User user;
    private Channel channel;
}