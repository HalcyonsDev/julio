package com.halcyon.julio.dto.chat;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewChatDto {
    private Long channelId;

    @Size(min = 4, max = 20, message = "Chat title must be greater than 3 characters and less than 20 characters.")
    private String title;
}