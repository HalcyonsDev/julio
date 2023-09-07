package com.halcyon.julio.dto.channel;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewChannelDto {
    @Size(min = 3, max = 20, message = "Channel title must be greater than 3 characters and less than 20 characters!")
    private String title;
}