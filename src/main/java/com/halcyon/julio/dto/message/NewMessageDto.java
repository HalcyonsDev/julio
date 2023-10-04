package com.halcyon.julio.dto.message;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewMessageDto {
    private Long chatId;

    @Size(min = 2, max = 128, message = "Chat value must be greater than 1 character and less than 128 characters.")
    public String value;
}