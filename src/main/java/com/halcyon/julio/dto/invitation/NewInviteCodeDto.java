package com.halcyon.julio.dto.invitation;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewInviteCodeDto {
    private Long channelId;

    @Min(value = 2, message = "The number of activations must be greater than 1 character.")
    public Integer activationsLeft;

    @Min(value = 2, message = "Action time must be greater than 1 second!")
    private Integer expirationsTimeInSeconds;
}