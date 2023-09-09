package com.halcyon.julio.event.message;

import com.halcyon.julio.model.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreatedEvent {
    private Message message;
}